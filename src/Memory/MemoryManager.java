package Memory;

import Core.Process;

import java.util.ArrayList;
import java.util.List;

public class MemoryManager {
    private List<MemoryBlock> memoryBlocks;
    private int totalMemory;

    public MemoryManager(int totalMemory) {
        memoryBlocks = new ArrayList<>();
        memoryBlocks.add(new MemoryBlock(totalMemory));
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

                if(remaining > 0 ) {
                    memoryBlocks.add(i+1, new MemoryBlock(remaining));
                }

                return true;
            }
        }

        int requiredMem = process.getMemoryMB();
        int availableMem = 0;

        for(MemoryBlock block : memoryBlocks) {
            if(block.isFree()) {
                availableMem += block.getSize();
            }
        }

        //no free block found, free terminated process (lazy release)
        boolean freed = false;
        for(MemoryBlock block: memoryBlocks) {
            if(availableMem >= requiredMem) {
                break;
            }

            if(!block.isFree() && block.getProcess().getState() == Process.State.TERMINATED) {
                block.setFree();
                availableMem += block.getSize();
            }
        }

        mergeFreeBlocks();

        for(int i = 0; i < memoryBlocks.size(); i++) {
            MemoryBlock block = memoryBlocks.get(i);

            if(block.isFree() && block.getSize() >= process.getMemoryMB()) {
                int remaining = block.getSize() - process.getMemoryMB();

                block.allocate(process);
                block.setSize(process.getMemoryMB());

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
}
