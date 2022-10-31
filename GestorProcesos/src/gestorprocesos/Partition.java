/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 
 */
public class Partition {
  
  private int size;
  private Process currentProcess;
  private List<Process> processes;
  private List<String> partitionInstructions;
  private int startPos;
  private int finishPos;
  private int partitionID;
  private boolean free;

  public Partition(int size, int startPos, int finishPos, int id) {
    this.size = size;
    this.startPos = startPos;
    this.finishPos = finishPos;
    this.processes = new ArrayList<>();
    this.partitionInstructions = new ArrayList();
    this.free = true;
    this.partitionID = id;
  }

  public int getSize() {
    return size;
  }

  public int getPartitionID() {
    return partitionID;
  }

  public Process getCurrentProcess() {
    return currentProcess;
  }

  public List<Process> getProcesses() {
    return processes;
  }

  public int getStartPos() {
    return startPos;
  }

  public int getFinishPos() {
    return finishPos;
  }

  public List<String> getPartitionInstructions() {
    return this.partitionInstructions;
  }
  
  public void setPartitionInstructions() {
    int i = this.startPos;
    int j = 0;
    if (this.currentProcess != null) {
      List<Instruction> instructions = this.currentProcess.getListInstructions();
      int instructionsLen = instructions.size();
      while (i < this.finishPos) {
        if (j < instructionsLen) {
          this.partitionInstructions.add(instructions.get(j).getInst());
        } else {
          this.partitionInstructions.add("FREE");
        }
        j++;
        i++;
      }
    } else {
      while (i < this.finishPos) {
        this.partitionInstructions.add("FREE");
        i++;
      }
    }
  }
  
  public void showInstruction() {
    System.out.println("\nPARTITION ID: " + this.partitionID);
    for(String inst : this.partitionInstructions) {
      System.out.print(inst + " - ");
    }
  }

  public boolean isFree() {
    return free;
  }
  

  public void setCurrentProcess(Process currentProcess) {
    this.currentProcess = currentProcess;
  } 

  public void setFree(boolean free) {
    this.free = free;
  }
  

  public void addProcess(Process process) {
    this.processes.add(process);
  }

  @Override
  public String toString() {
    return "Partition{" + "size=" + size + ", currentProcess=" + currentProcess + ", processes=" + processes + ", startPos=" + startPos + ", finishPos=" + finishPos + ", free=" + free + '}';
  }
  
  
}
