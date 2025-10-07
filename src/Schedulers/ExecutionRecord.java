package Schedulers;

import Core.Process;

public class ExecutionRecord {
    private Process process;
    private int startTime;
    private int endTime;

    /**
     * This class is for simpler storage each instance that will be printed in the Gnatt chart
     * This is not necessary for nonpreemptive scheduling since each process finished in the same record it starts in
     * However in case any of any preemptive scheduling algorithms this should work well for them
     * The start and end time describes the start and end of each execution record
     */

    public ExecutionRecord(Process process, int start, int end) {
        this.process = process;
        this.startTime = start;
        this.endTime = end;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }
}
