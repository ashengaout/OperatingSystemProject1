import Core.Process;
import Schedulers.ExecutionRecord;
import Schedulers.FCFSScheduler;
import Schedulers.SJPScheduler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter process file directory (press Enter to use default 'process.txt'): ");
        String input = sc.nextLine().trim();
        String fileName = input.isEmpty() ? "process.txt" : input;

        System.out.print("Enter total memory for this test:  ");
        int memory = sc.nextInt();

        List<Core.Process> processList = null;

        try {
            processList = parseProcessInput(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        if (processList.isEmpty()) {
            System.out.println("No valid processes found. Exiting.");
            return;
        }

        FCFSScheduler fcfsScheduler = new FCFSScheduler(processList, memory);

        System.out.println("First come first serve chart: ");
        printGanttChart(fcfsScheduler.getFCFS());
        System.out.println(STR."Avg wait time: \{fcfsScheduler.calculateAvgWTS()}");
        System.out.println(STR."Avg turnaround time: \{fcfsScheduler.calculateAvgTAT()}");

        System.out.println();

        SJPScheduler sjpScheduler = new SJPScheduler(processList, memory);

        System.out.println("Shortest job first chart: ");
        printGanttChart(sjpScheduler.getSJP());
        System.out.println(STR."Avg wait time: \{sjpScheduler.calculateAvgWTS()}");
        System.out.println(STR."Avg turnaround time: \{sjpScheduler.calculateAvgTAT()}");
    }

    //method for printing chart
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

    //method for processing file input
    public static List<Core.Process> parseProcessInput(String filename) throws FileNotFoundException {
        List <Core.Process> processList = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while((line = br.readLine()) != null) {
                line = line.trim();

                //skips empty lines
                if(line.isEmpty()) continue;

                //skip header row
                if(isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                //split by whitespace or tabs
                String[] parts = line.split("\\s+");
                if(parts.length != 5) continue; //skip malformed lines

                int pid = Integer.parseInt(parts[0]);
                int arrival = Integer.parseInt(parts[1]);
                int burst = Integer.parseInt(parts[2]);
                int priority = Integer.parseInt(parts[3]);
                int memory = Integer.parseInt(parts[4]);

                processList.add(new Process(pid, arrival, burst, priority, memory));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return processList;
    }
}