package Schedulers;

import Core.Process;

import java.util.List;


public interface Scheduler {
    List<ExecutionRecord> schedule(List<Process> processList);

    double calculateAvgWTS();

    double calculateAvgTAT();
}
