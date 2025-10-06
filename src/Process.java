public class Process {
    private int ID;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private int turnAroundTime;
    private int waitingTime;
    private int endTime;
    private int startTime;

    /**
     * This class is for storing each process instance read from the file
     * the start and end time refer to the actual start time of the process (running) and the actual end time of the process (terminated)
     * only four variables read from the file will be initialized
     * */

    public Process(int ID, int arrivalTime, int burstTime, int priority) {
        this.ID = ID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }

    void calculateTAT() {
        this.turnAroundTime = endTime-arrivalTime;
    }

    void calculateWT() {
        this.waitingTime = startTime-arrivalTime;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
