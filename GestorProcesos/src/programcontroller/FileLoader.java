/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programcontroller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Kevin
 */
public class FileLoader {
    
    private String EXTENSION = ".asm";
    private List<String> filesContent = new ArrayList<>();
    
    public FileLoader() {}
    
    public boolean isExtensionASM(String nameFile){
        String extensionFile = nameFile.substring(nameFile.length()-4);
        return extensionFile.equals(EXTENSION);
    }
    
    public void loadFile() {
        File[] files;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".asm", "asm");
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(filter);
        fileChooser.showOpenDialog(null);
        
        files = fileChooser.getSelectedFiles();
        
        for (File file : files){
            String nameFile = file.getName();
            System.out.println(nameFile);
            
            if (isExtensionASM(nameFile)) {
                String content = "";
                try (FileReader fileReader = new FileReader(file)) {
                    int value = fileReader.read();
                    while (value != -1) {
                        content = content + (char) value;
                        value = fileReader.read();
                    }
                    filesContent.add(content);
                } catch (IOException el) {
                    el.printStackTrace();
                }
            }
        } 
    }
    
    public List<String> getContent() {
        return filesContent;
    }
}
