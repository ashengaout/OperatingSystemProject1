package Memory;

import Core.Process;

public class MemoryBlock {
    private int size;
    private boolean isFree;
    private Process process;

    public MemoryBlock(int size) {
        this.size = size;
        this.isFree = true;
        this.process = null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree() {
        this.isFree = true;
        this.process = null;
    }

    public Process getProcess() {
        return process;
    }

    public void allocate(Process process) {
        this.process = process;
        this.isFree = false;
    }
}
