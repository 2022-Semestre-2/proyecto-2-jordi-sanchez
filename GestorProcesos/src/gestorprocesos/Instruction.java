/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestorprocesos;

/**
 *
 * @author jordi
 */
public class Instruction {
    private String type = "";
    private String register1 = "";
    private String register2 = "";
    private String inst;
    private int number;
    private int line;
    private int segment;
    
    private String error = "";

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public Instruction() {
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }

    
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Instruction(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegister1() {
        return register1;
    }

    public void setRegister1(String register) {
        this.register1 = register;
    }
    
    public String getRegister2() {
        return register2;
    }

    public void setRegister2(String register) {
        this.register2 = register;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

   public int verifyRegister1(){
       switch (this.register1) {
            case "AX" -> {
                return 1;
            }
            case "BX" -> {
                return 2;
            }
               case "CX" -> {
                   return 3;
            }
               case "DX" -> {
                   return 4;
            }
               /*case null -> {
                   return 5;
               }*/
           default -> throw new AssertionError();
       }
   }

    @Override
    public String toString() {
        String InstructionText= type;
        if(!register1.equals("")) {
            InstructionText += " "+register1;
            if(!register2.equals("")) {
                InstructionText += ", "+register2;
            }
            if(number != 0) {
                if (type == "JE " || type == "JNE" || type == "JMP"){
                    InstructionText += " "+Integer.toString(number);
                } else {
                    InstructionText += ", "+Integer.toString(number);
                }
            }
        }
        return InstructionText;
    }
    
   
}
