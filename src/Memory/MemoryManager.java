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

    public boolean allocateProcess(Process process) {
        for(int i = 0; i < memoryBlocks.size(); i++) {
            MemoryBlock block = memoryBlocks.get(i);

            if(block.isFree() && block.getSize() >= process.getMemoryMB()) {
                if(block.getSize() > process.getMemoryMB()) {
                    memoryBlocks.add(i+1, new MemoryBlock(block.getSize()-process.getMemoryMB()));
                    block = memoryBlocks.get(i);
                    block.allocate(process);
                    block.setSize(process.getMemoryMB());
                } else {
                    block.allocate(process);
                }

                return true;
            }
        }

        return false;
    }

    public void freeProcess(Process process) {
        for(MemoryBlock block : memoryBlocks) {
            if(!block.isFree() && block.getProcess() == process) {
                block.setFree();
            }
        }

        mergeFreeBlocks();
    }

    private void mergeFreeBlocks() {
        for(int i = 0; i <memoryBlocks.size()-1; i++) {
            MemoryBlock curr = memoryBlocks.get(i);
            MemoryBlock next = memoryBlocks.get(i+1);

            if(curr.isFree() && next.isFree()) {
                int size = curr.getSize() + next.getSize();
                curr.setSize(size);
                memoryBlocks.remove(i+1);
                i--;
            }
        }
    }

    public int getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
    }
}
