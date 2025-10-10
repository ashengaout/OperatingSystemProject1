package Memory;

import Core.Process;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MemoryManager {
    private List<MemoryBlock> memoryBlocks;
    private Queue<Process> fifoQueue;
    private int totalMemory;

    public MemoryManager(int totalMemory) {
        memoryBlocks = new ArrayList<>();
        memoryBlocks.add(new MemoryBlock(totalMemory));
        this.fifoQueue = new LinkedList<>();
        this.totalMemory = totalMemory;
    }

    //I am opting for the lazy release for first fit allocation
    public boolean allocateProcess(Process process) {

        for(int i = 0; i < memoryBlocks.size(); i++) {
            MemoryBlock block = memoryBlocks.get(i);

            if(block.isFree() && block.getSize() >= process.getMemoryMB()) {
                int remaining = block.getSize() - process.getMemoryMB();

                block.allocate(process);
                block.setSize(process.getMemoryMB());
                fifoQueue.add(process);

                if(remaining > 0 ) {
                    memoryBlocks.add(i+1, new MemoryBlock(remaining));
                }

                return true;
            }
        }

        int requiredMem = process.getMemoryMB();
        int availableMem = 0;

        //finds available memory
        for(MemoryBlock block : memoryBlocks) {
            if(block.isFree()) {
                availableMem += block.getSize();
            }
        }

        //no free block found, free terminated process (lazy release)
        while(!fifoQueue.isEmpty() && availableMem < requiredMem) {
            Process oldest = fifoQueue.poll();

            for(MemoryBlock block : memoryBlocks) {
                if(!block.isFree() && block.getProcess() == oldest) {
                    availableMem += block.getSize();
                    block.setFree();
                }
            }
        }

        mergeFreeBlocks();

        for(int i = 0; i < memoryBlocks.size(); i++) {
            MemoryBlock block = memoryBlocks.get(i);

            if(block.isFree() && block.getSize() >= process.getMemoryMB()) {
                int remaining = block.getSize() - process.getMemoryMB();

                block.allocate(process);
                block.setSize(process.getMemoryMB());
                fifoQueue.add(process);

                if (remaining > 0) {
                    memoryBlocks.add(i + 1, new MemoryBlock(remaining));
                }

                return true;
            }
        }

        return false;
    }

    private void mergeFreeBlocks() {
        for(int i = 0; i < memoryBlocks.size()-1; i ++) {
            MemoryBlock curr = memoryBlocks.get(i);
            MemoryBlock next = memoryBlocks.get(i+1);

            if(curr.isFree() && next.isFree()) {
                curr.setSize(curr.getSize()+ next.getSize());
                memoryBlocks.remove(i+1);
                i--;
            }
        }
    }

    public void printMemoryState() {
        System.out.println("Memory Blocks:");
        int used = 0;
        int free = 0;

        for(MemoryBlock block : memoryBlocks) {
            if(block.isFree()) {
                System.out.println("    Free Block "+block.getSize()+" MB");
                free += block.getSize();
            } else {
                System.out.println("    Allocated to PID P"+block.getProcess().getID()+ ": "+block.getSize()+" MB ");
                used += block.getSize();
            }
        }

        System.out.println("Total memory used: "+used+" MB");
        System.out.println("Total memory free: "+free+" MB");
        System.out.println("---------------------------");
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Queue<Process> getFifoQueue() {
        return fifoQueue;
    }

    public void setFifoQueue(Queue<Process> fifoQueue) {
        this.fifoQueue = fifoQueue;
    }
}
