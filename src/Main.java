import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) {
        Process p1 = new Process(1, 0, 5, 1);
        Process p2 = new Process(2, 9, 8, 2);
        Process p3 = new Process(3, 8, 14, 3);

        List<Process> processList = new ArrayList<>();
        processList.add(p1);
        processList.add(p2);
        processList.add(p3);

        FCFSScheduler fcfsScheduler = new FCFSScheduler(new ArrayList<>(processList));

        printGanttChart(fcfsScheduler.getFCFS());
        System.out.println(STR."Avg wait time: \{fcfsScheduler.calculateAvgWTS()}");
        System.out.println(STR."Avg turnaround time: \{fcfsScheduler.calculateAvgTAT()}");

        System.out.println();

        SJPScheduler sjpScheduler = new SJPScheduler(new ArrayList<>(processList));
        printGanttChart(sjpScheduler.getSJP());
        System.out.println(STR."Avg wait time: \{sjpScheduler.calculateAvgWTS()}");
        System.out.println(STR."Avg turnaround time: \{sjpScheduler.calculateAvgTAT()}");
    }

    public static void printGanttChart(List<ExecutionRecord> records) {
        if(records == null || records.isEmpty()) {
            System.out.println("No records to display.");
            return;
        }

        //add idle segments
        List<ExecutionRecord> fullTimeline = new ArrayList<>();
        int lastEnd = 0;
        for(ExecutionRecord rec : records) {
            if(rec.getStartTime() > lastEnd) {
                fullTimeline.add(new ExecutionRecord(null, lastEnd, rec.getStartTime()));
            }

            fullTimeline.add(rec);
            lastEnd = rec.getEndTime();
        }

        //print top border
        System.out.print(" ");
        for(ExecutionRecord record : fullTimeline) {
            int duration = record.getEndTime()-record.getStartTime();
            for(int i = 0; i < duration; i++) System.out.print("--");

            System.out.print(" ");
        }
        System.out.println();

        //print process labels
        for(ExecutionRecord record : fullTimeline) {
            int duration = record.getEndTime()- record.getStartTime();
            String label = (record.getProcess() == null) ? "IDLE" : STR."P\{record.getProcess().getID()}";

            int padding = duration*2-label.length();
            int paddingLeft = Math.max(padding/2, 0);
            int paddingRight = Math.max(padding-paddingLeft, 0);

            for(int i = 0; i < paddingLeft; i++) System.out.print(" ");
            System.out.print(label);
            for(int i = 0; i < paddingRight; i++) System.out.print(" ");
            System.out.print("|");
        }
        System.out.println();

        //print bottom border
        System.out.print(" ");
        for(ExecutionRecord record : fullTimeline) {
            int duration = record.getEndTime()-record.getStartTime();
            for(int i = 0; i < duration; i++) System.out.print("--");

            System.out.print(" ");
        }
        System.out.println();

        //print time labels
        System.out.print("0");
        for(ExecutionRecord record : fullTimeline) {
            int duration = record.getEndTime()-record.getStartTime();
            for(int i = 0; i < duration; i++) System.out.print("  ");
            System.out.print(record.getEndTime());
        }
        System.out.println();
    }
}