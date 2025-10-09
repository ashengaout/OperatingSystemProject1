package Schedulers;

import Core.Process;
import Memory.MemoryManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SJPScheduler implements Scheduler {
    private final List<ExecutionRecord> SJP;
    private MemoryManager memoryManager;

    /**
     * The scheduler class is for storing the results from the SJP algorithm and the FCFS algorithm
     * It can also calculate the average turn around time and wait time for each algorithm */
    public SJPScheduler(List<Process> processList, int memorySize) {
        this.memoryManager = new MemoryManager(memorySize);
        this.SJP = schedule(processList);
    }

    public List<ExecutionRecord> schedule(List<Process> processList) {
        List<ExecutionRecord> timeline = new ArrayList<>();
        int currentTime = 0;

        //sorts process by arrival time so that they are in order
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        //PQ ensures that the queue is ordered by burst time (so that the shortest bust time is at the front of the queue
        PriorityQueue<Process> readyList = new PriorityQueue<>(Comparator.comparingInt(Process::getBurstTime));

        int index = 0; //tracks processes added to queue
        while(index < processList.size() || !readyList.isEmpty()) {

            //adds processes that are arrived by current time
            while(index < processList.size() && processList.get(index).getArrivalTime() <= currentTime) {
                Process p = processList.get(index);

                if (p.getMemoryMB() > memoryManager.getTotalMemory()) {
                    System.out.println("Process PID "+p.getID()+" requires more memory than total available. Skipping.");
                    processList.remove(p);
                    continue;
                }

                //allocate memory and set state to ready
                if(memoryManager.allocateProcess(p)) {
                    System.out.println("Allocating process PID P"+p.getID());
                    memoryManager.printMemoryState();
                    p.setState(Process.State.READY);
                    readyList.add(p);
                    index++;
                } else {
                    break;
                }
            }

            //CPU is idle, jump to next process arrival
            if(readyList.isEmpty()) {
                currentTime = processList.get(index).getArrivalTime();
                continue;
            }

            //run process since memory available
            Process p = readyList.poll();
            p.setState(Process.State.RUNNING);
            p.setStartTime(currentTime);
            p.setEndTime(currentTime+ p.getBurstTime());
            p.calculateTAT();
            p.calculateWT();

            timeline.add(new ExecutionRecord(p, p.getStartTime(), p.getEndTime()));
            currentTime = p.getEndTime();
            p.setState(Process.State.TERMINATED);
        }

        return timeline;

    }

    public double calculateAvgWTS() {
        int sum = 0;
        int count = SJP.size();
        for(ExecutionRecord record: SJP) {
            sum += record.getProcess().getWaitingTime();
        }

        double res = Math.round(((double) sum /count) * 100.0) / 100.0;

        return res;
    }

    public double calculateAvgTAT() {
        int sum = 0;
        int count = SJP.size();
        for(ExecutionRecord record: SJP) {
            sum += record.getProcess().getTurnAroundTime();
        }

        double res = Math.round(((double) sum /count) * 100.0) / 100.0;

        return res;
    }

    public List<ExecutionRecord> getSJP() {
        return SJP;
    }

}
