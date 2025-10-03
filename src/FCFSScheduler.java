import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFSScheduler {
    private final List<Process> processList;
    private List<ExecutionRecord> FCFS;

    public FCFSScheduler(List<Process> processList) {
        this.processList = processList;
        this.FCFS = scheduler();
    }

    public List<ExecutionRecord> scheduler() {
        List<ExecutionRecord> timeline = new ArrayList<>();

        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;

        for(Process p: processList) {
            p.setStartTime(Math.max(currentTime, p.getArrivalTime()));
            p.setEndTime(p.getStartTime()+p.getBurstTime());
            p.calculateTAT();
            p.calculateWT();

            timeline.add(new ExecutionRecord(p, p.getStartTime(), p.getEndTime()));
        }

        return timeline;
    }

    public double calculateAvgWTS() {
        int sum = 0;
        int count = FCFS.size();
        for(ExecutionRecord record: FCFS) {
            sum += record.getProcess().getWaitingTime();
        }

        return (double) sum /count;
    }

    public double calculateAvgTAT() {
        int sum = 0;
        int count = FCFS.size();
        for(ExecutionRecord record: FCFS) {
            sum += record.getProcess().getTurnAroundTime();
        }

        return (double) sum /count;
    }
}
