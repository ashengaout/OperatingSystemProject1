import java.util.*;

public class SJPScheduler implements Scheduler {
    private final List<ExecutionRecord> SJP;

    /**
     * The scheduler class is for storing the results from the SJP algorithm and the FCFS algorithm
     * It can also calculate the average turn around time and wait time for each algorithm */
    public SJPScheduler(List<Process> processList) {
        this.SJP = schedule(processList);
    }

    @Override
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
                readyList.add(processList.get(index));
                index++;
            }

            //CPU is idle, jump to next process arrival
            if(readyList.isEmpty()) {
                currentTime = processList.get(index).getArrivalTime();
                continue;
            }

            //picks shortest job from ready list
            Process p = readyList.poll();
            p.setStartTime(currentTime);
            p.setEndTime(currentTime+p.getBurstTime());
            p.calculateTAT();
            p.calculateWT();

            timeline.add(new ExecutionRecord(p, p.getStartTime(), p.getEndTime()));
            currentTime = p.getEndTime(); //advances current time
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
