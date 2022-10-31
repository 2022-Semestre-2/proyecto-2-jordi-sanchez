/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

/**
 *
 * @author jordi
 */
public class Program {
    private Memory memory1;
    private Memory memory2;
    private Memory memory3;
    
    private int CPU_Use;
    private CPU cpu1 = new CPU();
    private CPU cpu2 = new CPU();

    public Program(int m1, int m2, int m3) {
        Memory memory1L = new Memory(m1);
        Memory memory2L = new Memory(m2);
        Memory memory3L = new Memory(m3);
        this.memory1 = memory1L;
        this.memory2 = memory2L;
        this.memory3 = memory3L;
    }

    public int getCPU_Use() {
        return CPU_Use;
    }

    public void setCPU_Use(int CPU_Use) {
        this.CPU_Use = CPU_Use;
    }
    
    public int getProcesCount(){
        return memory1.getListProcess().size() + memory2.getListProcess().size() + memory3.getListProcess().size();
    }

    public Memory getMemory1() {
        return memory1;
    }

    public void setMemory1(Memory memory1) {
        this.memory1 = memory1;
    }

    public Memory getMemory2() {
        return memory2;
    }

    public void setMemory2(Memory memory2) {
        this.memory2 = memory2;
    }

    public Memory getMemory3() {
        return memory3;
    }

    public void setMemory3(Memory memory3) {
        this.memory3 = memory3;
    }

    public CPU getCpu1() {
        return cpu1;
    }

    public void setCpu1(CPU cpu1) {
        this.cpu1 = cpu1;
    }

    public CPU getCpu2() {
        return cpu2;
    }

    public void setCpu2(CPU cpu2) {
        this.cpu2 = cpu2;
    }
}
