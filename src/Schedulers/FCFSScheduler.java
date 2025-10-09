package Schedulers;

import Core.Process;
import Memory.MemoryManager;

import java.util.*;

public class FCFSScheduler implements Scheduler {
    private List<ExecutionRecord> FCFS;
    private MemoryManager memoryManager;

    public FCFSScheduler(List<Process> processList, int memorySize) {
        this.memoryManager = new MemoryManager(memorySize);
        this.FCFS = schedule(processList);
    }

    //scheduling implementation for the FIFO algorithm
    public List<ExecutionRecord> schedule(List<Process> processList) {
        List<ExecutionRecord> timeline = new ArrayList<>();

        //sorts processes by arrival time
        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        Queue<Process> readyList = new LinkedList<>();
        int currentTime = 0;
        int index = 0;

        while(index < processList.size() || !readyList.isEmpty()) {

            //adds arrived processes to ready list
            while(index < processList.size() && processList.get(index).getArrivalTime() <= currentTime) {
                Process p = processList.get(index);

                //process will never be able to run
                if (p.getMemoryMB() > memoryManager.getTotalMemory()) {
                    System.out.println("Process PID "+p.getID()+" requires more memory than total available. Skipping.");
                    processList.remove(p);
                    continue;
                }

                if(memoryManager.allocateProcess(p)) {
                    //only add if memory can be successfully allocated
                    //Sets process state to ready
                    System.out.println("Allocating process PID P"+p.getID());
                    memoryManager.printMemoryState();
                    p.setState(Process.State.READY);
                    readyList.add(p);
                    index++;
                } else {
                    break;
                }
            }

            if(readyList.isEmpty()) {
                currentTime++;
                continue;
            }

            Process p = readyList.poll();
            p.setState(Process.State.RUNNING);
            p.setStartTime(currentTime);
            p.setEndTime(currentTime+ p.getBurstTime());
            p.calculateTAT();
            p.calculateWT();

            timeline.add(new ExecutionRecord(p, p.getStartTime(), p.getEndTime()));
            currentTime = p.getEndTime();

            //sets state to terminated when finishes running
            p.setState(Process.State.TERMINATED);
        }

        return timeline;
    }

    //calculating average wait time
    public double calculateAvgWTS() {
        int sum = 0;
        int count = FCFS.size();
        for(ExecutionRecord record: FCFS) {
            sum += record.getProcess().getWaitingTime();
        }

        double res = Math.round(((double) sum /count) * 100.0) / 100.0;
        return res;
    }

    //calculating average turn around time
    public double calculateAvgTAT() {
        int sum = 0;
        int count = FCFS.size();
        for(ExecutionRecord record: FCFS) {
            sum += record.getProcess().getTurnAroundTime();
        }

        double res = Math.round(((double) sum /count) * 100.0) / 100.0;
        return res;
    }

    //getters and setters
    public List<ExecutionRecord> getFCFS() {
        return FCFS;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public void setMemoryManager(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }
}
