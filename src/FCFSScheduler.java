import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FCFSScheduler implements Scheduler{
    private List<ExecutionRecord> FCFS;

    public FCFSScheduler(List<Process> processList) {
        this.FCFS = schedule(processList);
    }

    public List<ExecutionRecord> schedule(List<Process> processList) {
        List<ExecutionRecord> timeline = new ArrayList<>();

        processList.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;

        for(Process p: processList) {
            p.setStartTime(Math.max(currentTime, p.getArrivalTime()));
            p.setEndTime(p.getStartTime()+p.getBurstTime());
            p.calculateTAT();
            p.calculateWT();

            timeline.add(new ExecutionRecord(p, p.getStartTime(), p.getEndTime()));
            currentTime = p.getEndTime();
        }

        return timeline;
    }

    public double calculateAvgWTS() {
        int sum = 0;
        int count = FCFS.size();
        for(ExecutionRecord record: FCFS) {
            sum += record.getProcess().getWaitingTime();
        }

        double res = Math.round(((double) sum /count) * 100.0) / 100.0;
        return res;
    }

    public double calculateAvgTAT() {
        int sum = 0;
        int count = FCFS.size();
        for(ExecutionRecord record: FCFS) {
            sum += record.getProcess().getTurnAroundTime();
        }

        double res = Math.round(((double) sum /count) * 100.0) / 100.0;
        return res;
    }

    public List<ExecutionRecord> getFCFS() {
        return FCFS;
    }
}
