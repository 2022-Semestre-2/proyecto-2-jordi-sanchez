/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jordi
 */
public class MemoryController {
    
    public void initMemory(Memory memory) {
        List<String> listInstructionsString = new ArrayList<>();
        for (int i = 0; i <= memory.getSize()-1;i++){
            listInstructionsString.add("");
        }
        memory.setListInstructions(listInstructionsString);
    }
    
    private boolean freeSpace(int init, int size, List<String> list) {
        for(int i = init; i <= size; i++){
            if (!list.get(init).equals("")) {
                return false;
            }
        }
        return true;
    }
    
    public void segmentation(Memory memory){
        initMemory(memory); // inicializamos la memoria
        int segment = 1;
        for (Process process : memory.getListProcess()) {
            Random r = new Random();
            int rand = r.nextInt(10, memory.getSize()-process.getListInstructions().size()-1); //random desde 10 hasta el maximo de la memoria - # de instrucciones
            while(!freeSpace(rand, process.getListInstructions().size(), memory.getListInstructions() )){
                rand = r.nextInt(10, memory.getSize()-process.getListInstructions().size()-1);
            }
            for (int i = 0; i <= process.getListInstructions().size()-1; i++){
                memory.getListInstructions().set(rand, process.getListInstructions().get(i).getInst() +" "+ Integer.toString(segment)); 
                process.getListInstructions().get(i).setLine(rand);
                process.getListInstructions().get(i).setSegment(segment);
                rand++;
            } 
            segment++;
        }
    }
    
    public void dinamic(Memory memory){
        initMemory(memory); // inicializamos la memoria
        int segment = 1;
        for (Process process : memory.getListProcess()) {
            Random r = new Random();
            int rand = r.nextInt(10, memory.getSize()-process.getListInstructions().size()-1); //random desde 10 hasta el maximo de la memoria - # de instrucciones
            while(!freeSpace(rand, process.getListInstructions().size(), memory.getListInstructions() )){
                rand = r.nextInt(10, memory.getSize()-process.getListInstructions().size()-1);
            }
            for (int i = 0; i <= process.getListInstructions().size()-1; i++){
                memory.getListInstructions().set(rand, process.getListInstructions().get(i).getInst() +" "+ Integer.toString(segment)); 
                process.getListInstructions().get(i).setLine(rand);
                rand++;
            } 
            segment++;
        }
    }
    
    public void paginate(Memory memory) {
        createFramesOnMemory(memory);
        for (Process process : memory.getListProcess()) {
            createPagesProcessOnMemory(memory, process);
        }
        this.setPagesFree(memory);
    }
    
    private void setPagesFree(Memory memory){
        int i = 0;
        ProcessFrame pfLast = new ProcessFrame((Integer.toString(memory.getListProcessFrames().size()+1)));
        for (Frame listFrame : memory.getListFrames()) {
            if (listFrame.getPage() == null) {
                Page page = new Page();
                listFrame.setFrameId(i);
                listFrame.setPage(page);
                pfLast.getListFrames().add(i);
            }
            i++;
        }
        memory.getListProcessFrames().add(pfLast);
    }
    
    private void createFramesOnMemory(Memory memory) {
        int frames = (memory.getSize()-10)/16;
        for(int i = 0; i < frames; i++) {
            Frame frame = new Frame(16*i,i);
            memory.getListFrames().add(frame);
        }
    }
    
    private void createPagesProcessOnMemory(Memory memory, Process process) {
        Process processTemp = new Process(process);
        int instructionProcess = 0;
        while (!processTemp.getListInstructions().isEmpty()) {
            Page page = new Page();
            int indexFrame = FramesAvaibleInt(memory);
            int lineMemory = memory.getListFrames().get(indexFrame).getPageInit();
            for(int i = 0; i < 16;i++) {
                if (!processTemp.getListInstructions().isEmpty()) {
                    process.getListInstructions().get(instructionProcess).setLine(lineMemory+instructionProcess);
                    page.getListInstruction().add(processTemp.getListInstructions().get(0).getInst());
                    processTemp.getListInstructions().remove(0);
                } else {
                     page.getListInstruction().add("");
                }
                instructionProcess++;
            }
            addProcessFrame(memory, page, process.getID());
        }
    }
    
    private void addProcessFrame(Memory memory, Page page, String id) {
        if (FramesAvaible(memory)) {
            if(isProcessFrame(id, memory)){
                addPFramePage(id, memory, page);
            } else {
                ProcessFrame pf = new ProcessFrame(id);
                memory.getListProcessFrames().add(pf);
                addPFramePage(id, memory, page);
            }
        }
    }
    
    private void addPFramePage(String id, Memory memory, Page page) {
        for (ProcessFrame PFrame : memory.getListProcessFrames()) {
            if (PFrame.getProcessId().equals(id)) {
                int flag = 0;
                for (Frame listFrame : memory.getListFrames()) {
                    if(listFrame.getPage() == null && flag == 0){
                        listFrame.setPage(page);
                        PFrame.getListFrames().add(listFrame.getFrameId());
                        flag++;
                    } 
                }
            }
        }
    }
    
    private boolean FramesAvaible(Memory memory){
        for (Frame frame : memory.getListFrames()) {
            if (frame.getPage() == null) {
                return true;
            }
        }
        return false;
    }
    
    private int FramesAvaibleInt(Memory memory){
        int i = 0;
        for (Frame frame : memory.getListFrames()) {
            if (frame.getPage() == null) {
                return i;
            }
            i++;
        }
        return 0;
    }
    
    private boolean isProcessFrame(String id,Memory memory){
        if (!memory.getListProcessFrames().isEmpty()) {
            for (ProcessFrame pf  : memory.getListProcessFrames()) {
                if(pf.getProcessId() == id){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
    
  public List<Partition> fixedPartition(Memory memory) {
    this.initMemory(memory);
    return this.setPartitions(memory);
  }
  public List<Partition> setPartitions(Memory memory) {
    //List<Process> allProcesses = new ArrayList<>(listProcess);
    Random randNum = new Random();
    int partitionSizeRand = randNum.nextInt(25, 35);
    System.out.println("PARTITION SIZE IS: " + partitionSizeRand);
    List<Partition> partitions = new ArrayList<>();
    int limit = memory.getSize();
    int currentPos = 10;
    int id = 0;
    while (limit > partitionSizeRand) {
      int lastPos = currentPos + partitionSizeRand;
      Partition newPartition = new Partition(partitionSizeRand, currentPos, lastPos, id);
      partitions.add(newPartition);
      currentPos += partitionSizeRand;
      limit -= partitionSizeRand;
      id++;
    }

    partitions = this.allocateProcessesOneQueque(partitions, memory);
    return partitions;
  }

  public List<Partition> allocateProcessesOneQueque(List<Partition> partitions, Memory memory) {

    List<Process> allProcesses = memory.getListProcess();
    for(Process p : allProcesses) {
      System.out.println("PROCESS INSTRUCTIONS");
      for(Instruction i : p.getListInstructions()) {
        System.out.print(i.getInst() + " - ");
      }
    }
    // for each process, a partition is chosen if it fits.
    for (Process process : allProcesses) {
      for (Partition partition : partitions) {
        if (process.getListInstructions().size() <= partition.getSize() && partition.isFree()) {
          //partition.addProcess(process);
          partition.setCurrentProcess(process);
          partition.setPartitionInstructions();
          partition.setFree(false);
          break;
        }
      }
    }
    //this.showPartitions();
    return partitions;
  }
  
  /*
    public void showPartitions() {
    for (Partition partition : this.fixedPartitions) {
      System.out.println(partition.toString());
    }
  }
*/
    
}
