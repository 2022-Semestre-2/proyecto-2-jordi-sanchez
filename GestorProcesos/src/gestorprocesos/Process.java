/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author jordi y Kevin
 */
public class Process {
    private final BCP bcp;
    private List<Instruction> listInstructions;
    private String state = "nuevo";
    private LocalDateTime started;
    private LocalDateTime finished;
    private String ID;
    private int time = -1; // tiempo de llegada
    private int actualInstruction = 0; // utilizarla para ver la pocisión de instrucción por la que va en la ejecución 
    private int memoryDir; // utilizarla para acomodar el proceso en las memorias

    public int getTime() {
        return time;
    }
    
    public Process(Process process){
        this.ID = process.getID();
        this.listInstructions = new ArrayList<Instruction>(process.getListInstructions());
        this.bcp = new BCP();
    }
    
    public void setTime(int time) {
        this.time = time;
    }
    
    public LocalDateTime getStarted() {
        return started;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getActualInstruction() {
        return actualInstruction;
    }

    public void setActualInstruction(int actualInstruction) {
        this.actualInstruction = actualInstruction;
    }

    public int getMemoryDir() {
        return memoryDir;
    }

    public void setMemoryDir(int memoryDir) {
        this.memoryDir = memoryDir;
    }
    
    public Process() {
        this.bcp = new BCP();
    }


    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }
    

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    
    private boolean isRegister(String register){
        switch (register) {
            case "AX" -> {
                return true;
            }
            case "BX" -> {
                return true;
            }
               case "CX" -> {
                   return true;
            }
               case "DX" -> {
                   return true;
            }
               /*case null -> { 
                   return true;
               }*/
           default -> {
               return  false;
            }
        }
    }
        
    public BCP getBcp() {
        return this.bcp;
    }

    public List<Instruction> getListInstructions() {
        return this.listInstructions;
    }

    public void setListInstructions(List<Instruction> listValues) {
        this.listInstructions = listValues;
    }
    
    public boolean searchError() {
        for (Instruction temp : listInstructions) {
            if(!temp.getError().equals("")) {
                JFrame f = new JFrame("frame");
                JOptionPane.showMessageDialog(f ,
                "Se ha encontrado un error en el codigo cargado en la linea "+Integer.toString(temp.getLine())+".",
                "Error de archivo cargado" ,
                JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }
        return false;
    }
}
