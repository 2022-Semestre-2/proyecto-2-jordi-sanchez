/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jordi
 */
public class CPU {

  private final List<Process> finishedProcesses = new ArrayList<>();
  private List<Process> listProcess = new ArrayList<>(); // guarda todos los procesos ingresados
  private List<String> orderProcess = new ArrayList<>(); // guardara el array del orden de la ejecución segun el algoritmo
  private Process currentProcess;
  private int currentLine = 0;
  private int lastTime; // creara los tiempos de los procesos y guardará el tiempo de ingreso del último proceso
  private String algorithm = "FCFS";
  private int qbit;
  private int currentTick = 0;
  private List<Partition> fixedPartitions = new ArrayList<>();

  public void setQbit(int qbit) {
    this.qbit = qbit;
  }

  public List<Process> getListProcess() {
    return listProcess;
  }

  public void setListProcess(List<Process> listProcess) {
    this.listProcess = listProcess;
  }

  public List<String> getOrderProcess() {
    return orderProcess;
  }

  public void setOrderProcess(List<String> orderProcess) {
    this.orderProcess = orderProcess;
  }

  public List<Process> getFinishedProcesses() {
    return finishedProcesses;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public boolean isInstructionIrregular() {
    String type = "";
    if (currentProcess.getListInstructions().size() > currentLine) {
      type = currentProcess.getListInstructions().get(currentLine).getType();
    }
    return "INT".equals(type) || "JMP".equals(type) || "JNE".equals(type) || "JE".equals(type);
  }

  public boolean isJMP() {
    return "JMP".equals(currentProcess.getListInstructions().get(currentLine).getType());
  }
  
  public boolean isJNE() {
    return "JNE".equals(currentProcess.getListInstructions().get(currentLine).getType());
  }
  
  public boolean isJE() {
    return "JE".equals(currentProcess.getListInstructions().get(currentLine).getType());
  }
  
   public boolean isINT() {
        return "INT".equals(currentProcess.getListInstructions().get(currentLine).getType());
    }

  public Instruction getCurrentInstruction() {
    return currentProcess.getListInstructions().get(currentLine);
  }

  public Process getCurrentProcess() {
    return currentProcess;
  }

  public void setCurrentProcess(Process currentProcess) {
    this.currentProcess = currentProcess;
  }

  public int getCurrentLine() {
    return currentLine;
  }

  public void setCurrentLine(int currentLine) {
    this.currentLine = currentLine;
  }

  public boolean ejecuteProcessInstruction() {
    if (currentLine == 0) {
      LocalDateTime ldt = LocalDateTime.now();
      currentProcess.setStarted(ldt);
    }
    if (currentProcess.getListInstructions().size() > currentLine) {
      //PA = PlanificationAlgorithm
      //executePACorresponds();
      currentProcess.getBcp().setPC(currentProcess.getListInstructions().get(currentLine).getLine() + "");
      setValueRegister("IR", currentProcess.getListInstructions().get(currentLine).getInst());
      ejecuteInstruction();
      this.currentLine += 1;
      return true;
    } else {
      LocalDateTime ldt = LocalDateTime.now();
      currentProcess.setFinished(ldt);
      currentProcess.setState("Finalizado");
      finishedProcesses.add(currentProcess);
      currentLine = 0;
      return false;
    }
  }

  private void ejecuteInstruction() {
    switch (currentProcess.getListInstructions().get(currentLine).getType()) {
      case "LOAD" ->
        ejecuteLoad();
      case "STORE" ->
        ejecuteStore();
      case "MOV" ->
        ejecuteMov();
      case "ADD" ->
        ejecuteAdd();
      case "SUB" ->
        ejecuteSub();
      case "INC" ->
        ejecuteInc();
      case "DEC" ->
        ejecuteDec();
      case "SWAP" ->
        ejecuteSwap();
      case "CMP" ->
        ejecuteCmp();
      case "PUSH" ->
        ejecutePush();
      case "POP" ->
        ejecutePop();
      case "INT" -> ejecuteINT();
      case "JMP" ->
          ejecuteJump();
      case "JNE" ->
          ejecuteJump();
      case "JE" ->
          ejecuteJump();
      default ->
        System.out.println("no entá disponible");
    }
  }

  // ############################# Ejecucion de instrucciones ###################################
  
    private void ejecuteJump() {
        System.out.println("Lee la instruccion JMP");
        /*int currentInstruction = currentProcess.getActualInstruction();
        int jumpNumber = currentProcess.getListInstructions().get(currentLine).getNumber();
        int numberInstructions = currentProcess.getListInstructions().size();
        if (jumpNumber < numberInstructions - currentInstruction) {
            int newLine = currentProcess.getActualInstruction() + jumpNumber;
            currentProcess.setActualInstruction(newLine);
        }*/
    }
  
    private void ejecuteINT(){
        System.out.println("Lee la instruccion INT");
    }
  
  private void ejecuteCmp() {
    // Compara los dos registros de la instruccion
    String valueRegister1 = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
    String valueRegister2 = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister2());
    if (valueRegister1.equals(valueRegister2)) {
      System.out.println("Ambos registros son iguales");
    } else {
      System.out.println("Ambos registros NO son iguales");
    }
  }

  private void ejecutePush() {
    //guarda en la pila (stack) el valor del registro
    String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());

    currentProcess.getBcp().pushStack(valueRegister);
  }

  private void ejecutePop() {
    //guarda en el registro el valor almacenado en el stack
    String register = currentProcess.getListInstructions().get(currentLine).getRegister1();
    currentProcess.getBcp().popStack(register);
  }

  private void ejecuteLoad() {
    // registra el valor obtenido del registro a buscar en el bcp
    String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
    currentProcess.getBcp().setAC(valueRegister);
  }

  private void ejecuteStore() {
    // registra el valor obtenido del registro ac
    String valueRegister = getValueRegister("AC");
    setValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1(), valueRegister);
  }

  private void ejecuteMov() {
    if (currentProcess.getListInstructions().get(currentLine).getRegister2().isEmpty()) {
      // si el registro 2 es nulo es porque es un mov con numero
      // si entra cambia de una vez el primer registro por el numero
      setValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1(), currentProcess.getListInstructions().get(currentLine).getNumber() + "");
    } else {
      // sino busca el valor que hay en el bcp del registro del que queremos mover los valores
      String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister2());
      setValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1(), valueRegister);
    }
  }

  private void ejecuteAdd() {
    // registra el valor obtenido del registro a buscar en el bcp
    String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
    String valueRegisterAC = getValueRegister("AC");
    String val = "" + (Integer.parseInt(valueRegister) + Integer.parseInt(valueRegisterAC));
    currentProcess.getBcp().setAC(val);
  }

  private void ejecuteSub() {
    // registra el valor obtenido del registro a buscar en el bcp
    String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
    String valueRegisterAC = getValueRegister("AC");
    String val = "" + (Integer.parseInt(valueRegister) - Integer.parseInt(valueRegisterAC));
    currentProcess.getBcp().setAC(val);
  }

  private void ejecuteInc() {
    String valueRegisterAC = getValueRegister("AC");
    if (currentProcess.getListInstructions().get(currentLine).getRegister1().isBlank()) {
      currentProcess.getBcp().setAC((Integer.parseInt(valueRegisterAC) + 1) + "");
    } else {
      String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
      currentProcess.getBcp().setAC((Integer.parseInt(valueRegisterAC) + Integer.parseInt(valueRegister)) + "");
    }
  }

  private void ejecuteDec() {
    String valueRegisterAC = getValueRegister("AC");
    if (currentProcess.getListInstructions().get(currentLine).getRegister1().isBlank()) {
      currentProcess.getBcp().setAC((Integer.parseInt(valueRegisterAC) - 1) + "");
    } else {
      String valueRegister = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
      currentProcess.getBcp().setAC((Integer.parseInt(valueRegisterAC) - Integer.parseInt(valueRegister)) + "");
    }
  }

  private void ejecuteSwap() {
    String valueRegister1 = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1());
    String valueRegister2 = getValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister2());
    setValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister1(), valueRegister2);
    setValueRegister(currentProcess.getListInstructions().get(currentLine).getRegister2(), valueRegister1);
  }

  // #############################################################################################
  private String getValueRegister(String register) {
    switch (register) {
      case "AX" -> {
        return currentProcess.getBcp().getAX();
      }
      case "BX" -> {
        return currentProcess.getBcp().getBX();
      }
      case "CX" -> {
        return currentProcess.getBcp().getCX();
      }
      case "DX" -> {
        return currentProcess.getBcp().getDX();
      }
      case "AC" -> {
        return currentProcess.getBcp().getAC();
      }
      default ->
        throw new AssertionError();
    }
  }

  private void setValueRegister(String register, String value) {
    switch (register) {
      case "AX" -> {
        currentProcess.getBcp().setAX(value);
        break;
      }
      case "BX" -> {
        currentProcess.getBcp().setBX(value);
        break;
      }
      case "CX" -> {
        currentProcess.getBcp().setCX(value);
        break;
      }
      case "DX" -> {
        currentProcess.getBcp().setDX(value);
        break;
      }
      case "IR" -> {
        currentProcess.getBcp().setIR(value);
        break;
      }
      default ->
        throw new AssertionError();
    }
  }

  //setea los tiempos de llegada de los procesos detras del último entre 1 a 5 segundos despues del último processo en ser cargado
  public void setTimeProcess() {
    int x = 0;
    for (Process process : listProcess) {
      if (x != 0) {
        if (process.getTime() == -1) {
          Random r = new Random();
          int rand = r.nextInt((5 - 1) + 1) + 1;
          process.setTime(lastTime + rand);
          lastTime = lastTime + rand;
        }
      } else {
        x++;
      }

    }
  }

  private void executePACorresponds() {
    switch (algorithm) {
      case "FCFS" -> {

        break;
      }
      case "SPN" -> {
        executeSPN();
        break;
      }
      case "SRT" -> {
        executeSRT();
        break;
      }
      case "RR" -> {
        executeRR();
        break;
      }
      case "HRRN" -> {
        executeHRRN();
        break;
      }
      default ->
        throw new AssertionError();
    }
  }

  //
  public List<String> executeRR() {
    List<Process> listTemp = new ArrayList<>(listProcess);
    List<Process> listTempTimer = new ArrayList<>();
    List<String> listRet = new ArrayList<>();
    int bandera = 0;
    int qbitTemp = qbit;
    int currentTime = 0;
    while (!(listTemp.isEmpty() && listTempTimer.isEmpty())) {
      if (!listTemp.isEmpty()) {
        for (int i = 0; i < listTemp.size(); i++) {
          if (listTemp.get(i).getTime() <= currentTime) {
            listTempTimer.add(listTemp.get(i));
            listTemp.remove(i);
            i--;
          }
        }
      }
      int qbitdef = 0;
      for (int i = 1; i <= qbitTemp; i++) {
        if (!listTempTimer.isEmpty()) {
          if ((listTempTimer.get(0).getListInstructions().size() - listTempTimer.get(0).getActualInstruction()) > 0) {
            listTempTimer.get(0).setActualInstruction(listTempTimer.get(0).getActualInstruction() + 1);
            String id = listTempTimer.get(0).getID();
            listRet.add(id);
            qbitdef++;
          } else {
            if (i == qbitTemp) {
              listTempTimer.remove(0);
            }
          }
        }
      }
      currentTime += qbitdef;
      if (!listTemp.isEmpty()) {
        for (int i = 0; i < listTemp.size(); i++) {
          if (listTemp.get(i).getTime() <= currentTime) {
            listTempTimer.add(listTemp.get(i));
            listTemp.remove(i);
            i--;
          }
        }
      }
      firtTolastProcess(listTempTimer);
    }
    return listRet;
  }

  private List<Process> firtTolastProcess(List<Process> processes) {
    if (!processes.isEmpty()) {
      Process first = processes.get(0);
      processes.remove(0);
      processes.add(first);
    }
    return processes;
  }

  //
  public List<String> executeSPN() {
    List<Process> listTemp = new ArrayList<>(listProcess);
    List<Process> listTempTimer = new ArrayList<>();
    List<String> listRet = new ArrayList<>();
    int currentTime = 0;
    while (!(listTemp.isEmpty() && listTempTimer.isEmpty())) {
      if (!listTemp.isEmpty()) {
        for (int i = 0; i < listTemp.size(); i++) {
          if (listTemp.get(i).getTime() <= currentTime) {
            listTempTimer.add(listTemp.get(i));
            listTemp.remove(i);
            i--;
          }
        }
      }
      int tempExecute = 0;
      if (!listTempTimer.isEmpty()) {
        int processLess = getIndexLessInstruction(listTempTimer);
        for (int i = 0; i <= listTempTimer.get(processLess).getListInstructions().size(); i++) {
          String id = listTempTimer.get(processLess).getID();
          listRet.add(id);
          tempExecute++;
        }
        listTempTimer.remove(processLess);
      }
      currentTime += tempExecute;
    }
    return listRet;
  }

  //
  public List<String> executeSRT() {
    List<Process> listTemp = new ArrayList<>(listProcess);
    List<Process> listTempTimer = new ArrayList<>();
    List<String> listRet = new ArrayList<>();
    int currentTime = 0;
    while (!(listTemp.isEmpty() && listTempTimer.isEmpty())) {
      if (!listTemp.isEmpty()) {
        for (int i = 0; i < listTemp.size(); i++) {
          if (listTemp.get(i).getTime() <= currentTime) {
            listTempTimer.add(listTemp.get(i));
            listTemp.remove(i);
            i--;
          }
        }
      }
      if (!listTempTimer.isEmpty()) {
        int processLess = getIndexLessInstruction(listTempTimer);
        String id = listTempTimer.get(processLess).getID();
        listRet.add(id);
        listTempTimer.get(processLess).getListInstructions().remove(0);
        listTempTimer.get(processLess).setActualInstruction(listTempTimer.get(processLess).getActualInstruction() + 1);
        if (listTempTimer.get(processLess).getListInstructions().isEmpty()) {
          listTempTimer.remove(processLess);
        }
      }
      currentTime++;
    }
    return listRet;
  }

  //encuentra el index del processo con menor numero de instrucciones
  private int getIndexLessInstruction(List<Process> list) {
    int id = 0;
    int current = 0;
    int lastProcess = 0;
    for (Process process : list) {
      if (lastProcess == 0) {
        lastProcess = process.getListInstructions().size();
      } else {
        if (lastProcess > process.getListInstructions().size()) {
          lastProcess = process.getListInstructions().size();
          id = current;
        }
      }
      current++;
    }
    return id;
  }

  /**
   * Executes the highest response ratio next algorithm.
   *
   * @return an array processes' ids of the way they should execute.
   */
  public List<String> executeHRRN() {
    this.currentTick = 0;
    List<Process> allProcesses = new ArrayList<>(listProcess);
    List<Process> planificationProcesses = new ArrayList<>();
    List<Double> planificationProcessesHRRN = new ArrayList<>();
    List<String> planificationRes = new ArrayList<>();
    boolean planification = true;
    while (planification) {
      // Gets processes that arrived on current cpu tick.
      for (int i = 0; i < allProcesses.size(); i++) {
        if (allProcesses.get(i).getTime() <= this.currentTick) {
          planificationProcesses.add(allProcesses.get(i));
          allProcesses.remove(allProcesses.get(i));
          i--;
        }
      }
      // Gets the HRRN of each selected process.
      planificationProcessesHRRN = this.computeProcessHRRN(planificationProcesses);
      // Selects the process that is to be executed.
      Process selectedProcess = this.getMaxValue(planificationProcessesHRRN, planificationProcesses);

      // Inserts back all the processes that were not chosen.
      allProcesses.addAll(planificationProcesses);
      // Now that we have the process, we clear the planification and HRRN values.
      planificationProcesses.clear();
      planificationProcessesHRRN.clear();
      // and we remove the selected process from all the processes to execute.
      allProcesses.remove(selectedProcess);

      // Fills planificationArray with the id of the selected process.
      for (Instruction intrs : selectedProcess.getListInstructions()) {
        planificationRes.add(selectedProcess.getID());
        this.currentTick += 1;
      }
      // If there is only one process left, the iteration stops.
      if (allProcesses.size() == 1) {
        planification = false;
      }
    }
    // Adds the last process.
    for (Instruction intrs : allProcesses.get(0).getListInstructions()) {
      planificationRes.add(allProcesses.get(0).getID());
      this.currentTick += 1;
    }
    return planificationRes;

  }

  /**
   * Computes the HRRN value of each processes in the planificationProcesses
   * list.
   *
   * @param planificationProcesses all candidate processes to execute.
   * @return a list with the HRRN of each candidate.
   */
  private List<Double> computeProcessHRRN(List<Process> planificationProcesses) {
    List<Double> planificationProcessesHRRN = new ArrayList<>();
    // Computes the HRRN of each selected process.
    // H = (w + r) / r, w = CPU tick - process arriving time, r = program total instructions.
    for (Process process : planificationProcesses) {
      double processW = this.currentTick - process.getTime();
      double processR = process.getListInstructions().size();
      planificationProcessesHRRN.add((processW + processR) / processR);
    }
    return planificationProcessesHRRN;
  }

  /**
   * Gets the max HRRN value from planificationProcessesHRRN.
   *
   * @param planificationProcessesHRRN all the computed HRRN values.
   * @param planificationProcesses all the candidates.
   * @return
   */
  private Process getMaxValue(List<Double> planificationProcessesHRRN, List<Process> planificationProcesses) {

    double max = 0;
    // Selects the max HRRN value.
    for (double value : planificationProcessesHRRN) {
      max = value > max ? value : max;
    }
    // Gets the index of the max HRRN value so that it returns its correspondent process.
    int processIndex = planificationProcessesHRRN.indexOf(max);
    return planificationProcesses.get(processIndex);

  }

  /**
   * Executes the First Come, First Serve algorithm.
   *
   * @return an array processes' ids of the way they should execute.
   */
  public List<String> executeFCFS() {
    List<Process> allProcesses = new ArrayList<>(listProcess);
    List<String> planificationRes = new ArrayList<>();

    for (Process process : allProcesses) {
      for (Instruction intrs : process.getListInstructions()) {
        planificationRes.add(process.getID());
      }
    }

    return planificationRes;
  }
}
