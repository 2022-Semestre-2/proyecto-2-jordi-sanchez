/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

import java.util.Stack;

/**
 *
 * @author jordi
 */
public class BCP {
    private int location;
    private String AX = "0";
    private String BX = "0";
    private String CX = "0";
    private String DX = "0";
    private String PC = "0";
    private String IR = "0";
    private String AC = "0";
    private String CPU;
    private String initTime;
    private String currentTime;
    private String State;
    private Stack<String> stack = new Stack<>();

    public void pushStack(String value) {
        stack.push(value);
    }
    
    public void popStack(String register) {
        switch(register){
            case "AX" -> setAX(stack.pop());
            case "BX" -> setBX(stack.pop());
            case "CX" -> setCX(stack.pop());
            case "DX" -> setDX(stack.pop());
            default -> System.out.println("no entá disponible");
        }
    }

    public boolean cmpRegisters (String register1, String register2) {
        return (getRegister(register1).equals(getRegister(register2)));
    }
    
    public int getLocation() {
        return location;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getIR() {
        return IR;
    }

    public void setIR(String IR) {
        this.IR = IR;
    }

    public String getAC() {
        return AC;
    }

    public void setAC(String AC) {
        this.AC = AC;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getAX() {
        return AX;
    }

    public void setAX(String AX) {
        this.AX = AX;
    }

    public String getBX() {
        return BX;
    }

    public void setBX(String BX) {
        this.BX = BX;
    }

    public String getCX() {
        return CX;
    }

    public void setCX(String CX) {
        this.CX = CX;
    }

    public String getDX() {
        return DX;
    }

    public void setDX(String DX) {
        this.DX = DX;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }
    
        private String getRegister(String register){
        switch (register) {
            case "AX" -> {
                return AX;
            }
            case "BX" -> {
                return BX;
            }
               case "CX" -> {
                   return CX;
            }
               case "DX" -> {
                   return DX;
            }
           default -> {
               return  "";
            }
        }
    }
  
}
