import java.util.ArrayList;
import java.util.List;

public class Main{
    public static void main(String[] args) {
        Process p1 = new Process(1, 0, 5, 1);
        Process p2 = new Process(2, 5, 8, 2);
        Process p3 = new Process(3, 2, 14, 3);

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
        if (records == null || records.isEmpty()) {
            System.out.println("No execution records to display.");
            return;
        }

        // Print top line
        System.out.print(" ");
        for (ExecutionRecord record : records) {
            int duration = record.getEndTime() - record.getStartTime();
            for (int i = 0; i < duration; i++) {
                System.out.print("--");
            }
            System.out.print(" ");
        }
        System.out.println();

        // Print process names
        System.out.print("|");
        for (ExecutionRecord record : records) {
            int duration = record.getEndTime() - record.getStartTime();
            String processName = String.valueOf(record.getProcess().getID()); // assuming Process has getName()
            int padding = duration * 2 - processName.length();
            int padLeft = padding / 2;
            int padRight = padding - padLeft;

            for (int i = 0; i < padLeft; i++) System.out.print(" ");
            System.out.print(STR."P\{processName}");
            for (int i = 0; i < padRight; i++) System.out.print(" ");
            System.out.print("|");
        }
        System.out.println();

        // Print bottom line
        System.out.print(" ");
        for (ExecutionRecord record : records) {
            int duration = record.getEndTime() - record.getStartTime();
            for (int i = 0; i < duration; i++) {
                System.out.print("--");
            }
            System.out.print(" ");
        }
        System.out.println();

        // Print time labels
        int currentTime = 0;
        System.out.print(currentTime);
        for (ExecutionRecord record : records) {
            int duration = record.getEndTime() - record.getStartTime();
            for (int i = 0; i < duration; i++) System.out.print("  ");
            currentTime = record.getEndTime();
            System.out.print(currentTime);
        }
        System.out.println();
    }
}