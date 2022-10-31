/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestorprocesos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jordi
 */
public class ProcessFrame {
    private String processId;
    private List<Integer> listFrames = new ArrayList<>();

    public ProcessFrame(String processId) {
        this.processId = processId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public List<Integer> getListFrames() {
        return listFrames;
    }

    public void setListFrames(List<Integer> listFrames) {
        this.listFrames = listFrames;
    }
        
}
