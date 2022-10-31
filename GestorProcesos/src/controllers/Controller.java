/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import gestorprocesos.BCP;
import gestorprocesos.CPU;
import gestorprocesos.Frame;
import gestorprocesos.Instruction;
import gestorprocesos.Memory;
import gestorprocesos.MemoryController;
import gestorprocesos.Page;
import gestorprocesos.Partition;
import gestorprocesos.instructionMaker;
import gestorprocesos.Process;
import gestorprocesos.ProcessFrame;
import gestorprocesos.Program;
import gui.Configuration;
import gui.GUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import programcontroller.FileLoader;

/**
 *
 * @author jordi
 */
public class Controller {
    static DefaultTableModel model;
    static DefaultTableModel model2;
    static DefaultTableModel model3;
    public GUI v;
    public Program p;
    public Configuration c;
    static String state = "";
    public int inst = 0;
    public int rowTable=0;
    public int columnTable=0;
    static boolean paused = false;
    static boolean isStepByStep = false;
    static boolean INT10HSbS = false;
    private BCP workingBCP;
    boolean comparator = false;
    private int processesCount = 1;

    abstract class threadProcess implements Runnable {

    }
    
    public Controller() {
        v = new GUI();
        p = new Program(128,512,64);
        c = new Configuration();
        configureTables();
    }
    private void configurePlanners(List<String> ListProcess, int countProcess){
        int pro=9;
      //Modelo de CPU 1  
      model2= new  DefaultTableModel(){
       @Override
       public boolean isCellEditable(int filas, int columnas) {
          if (columnas == 2) {
            return true;

          } else {
            return false;
          }
        }
      };
      //****MODELO DE CPU 2
      model3= new  DefaultTableModel(){
       @Override
       public boolean isCellEditable(int filas, int columnas) {
          if (columnas == 2) {
            return true;

          } else {
            return false;
          }
        }
      };
      int m=0;
      model2.addColumn("CPU 1");
      model3.addColumn("CPU 2");
      while(m<pro){
          
           v.getjTable1().getColumnModel().getColumn(0).setMinWidth(10);
           v.getjTable2().getColumnModel().getColumn(0).setMaxWidth(10);
           m++;
      }
      v.getjTable1().setDefaultRenderer(Object.class, new MyTableCellRenderer());
      v.getjTable2().setDefaultRenderer(Object.class, new MyTableCellRenderer());
      v.getjTable1().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                v.getjTable1().scrollRectToVisible(v.getjTable1().getCellRect(v.getjTable1().getRowCount() - 1, 0, true));
            }
        });
      v.getjTable1().setModel(model2);
      v.getjTable2().setModel(model3);
      int r=1;
      
      int sice=ListProcess.size();
      int w=0;
      for(String s: ListProcess){
            int num=Integer.valueOf(s.substring(s.length()-1));  
            model2.addColumn(w);
            model2.setNumRows(countProcess);
            if(w<countProcess){
                    int h=w+1;
                    v.getjTable1().setValueAt("P "+h, w, 0);
            }
            v.getjTable1().getModel().setValueAt("X", num-1,columnTable+1 );
            this.columnTable+=1;
            model3.addColumn(w);
            model3.setNumRows(sice);
            v.getjTable2().setValueAt("P "+w, w, 0);
            v.getjTable2().getModel().setValueAt("X", w, r);
            changeTable(v.getjTable1(),r,w);
            r+=1;
            w+=1;
  
          }
          for (int gg=0;gg<sice;gg++){
                changeTable(v.getjTable1(),gg,0);
           
           
      }
     
    }
    private void changeTable(JTable table, int columnIndex,int  rowIndex){
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer(){
            
            @Override
            
            public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column){
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String strX=(String) table.getValueAt(row,columnIndex );
                
                if(strX=="X"){
                    c.setBackground(Color.pink);

                }else{
                    c.setBackground(Color.white);
                }
                return c;
            }
        });
    }
    class MyTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Color getBackground() {
        return super.getBackground();
    }
}
    
    private void configureTables() {
        JTable tablePrimaryMemory = new JTable();
        JTable tableDiscMemory = new JTable();
        
        tablePrimaryMemory = v.getTableMemory();
        tableDiscMemory = v.getTableDisc();
        
        createModel(tablePrimaryMemory, p.getMemory1().getSize(), "Valor en memoria");
        createModel(tableDiscMemory, p.getMemory2().getSize(), "Valor en disco");
    }
    
    private static void createModel(JTable table, int sizeMemory, String typeOfMemory) {
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int rows, int columns) {
                if (columns == 2) {
                    return true;
                } else {
                    return false;
                }
            }   
        };
        model.addColumn("Pos");
        model.addColumn(typeOfMemory);
        
        table.setModel(model);
        table.getColumnModel().getColumn(0).setMinWidth(60);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        for(int i=0; i<sizeMemory; i++){
            model.setNumRows(sizeMemory);
            table.setValueAt(i, i, 0);
        }
    }
    
    private boolean verifyMemorys(Process process){
        process.setID("Proceso " + processesCount);
        processesCount++;
        if (p.getMemory1().addProcess(process)) {
            return true;
        } else {
            if (p.getMemory2().addProcess(process)) {
                return true;
            } else {
                if (p.getMemory3().addProcess(process)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
    
    private void setProcessLoad(Process process) {
        DefaultTableModel model = (DefaultTableModel) v.getTableProcesses().getModel();
        model.addRow(new Object[]{"Process "+ p.getProcesCount(), process.getState()});
    }
    
    private void setProcessTable(Process process) {
        int memoryProcess = process.getListInstructions().size();
        if (p.getMemory1().getcurrentAmountMemoryUsed() + memoryProcess <= p.getMemory1().getSize()) {
            setProcessesTableMemory(process);
        } else if (p.getMemory2().getcurrentAmountMemoryUsed() + memoryProcess <= p.getMemory2().getSize()) {
            setProcessesTableDisc(process);
        } else {
            System.out.println("Se ingreso en la memoria virtual");
        }
        
    }
    
    private void setProcessesTableMemory(Process process) {
        for (Instruction temp: process.getListInstructions()) {
            v.getTableMemory().setValueAt(temp.toString(), p.getMemory1().getcurrentAmountMemoryUsed(), 1);
            p.getMemory1().setcurrentAmountMemoryUsed(p.getMemory1().getcurrentAmountMemoryUsed()+1);
        }
    }
    
    private void setProcessesTableMemory2(Memory memory) {
        int i = 0;
        
        for (String temp: memory.getListInstructions()) {
            v.getTableMemory().setValueAt(temp, i, 1);
            i++;
        }
        
        i = 0;
        for (String temp: memory.getListInstructions()) {
            changeTableSegment(v.getTableMemory(), 1, i);
            i++;
        }
    }
    
    private void setProcessesTableMemoryPage(Memory memory) {
        for (ProcessFrame PFtemp: memory.getListProcessFrames()) {
            for (Integer frameIndex : PFtemp.getListFrames()){
                Page page = memory.getListFrames().get(frameIndex).getPage();
                int pageInit = memory.getListFrames().get(frameIndex).getPageInit();
                int index = 0;
                for(int i = pageInit; i < pageInit+16; i++) {
                    if (page.getListInstruction().size() > index) {
                        v.getTableMemory().setValueAt(page.getListInstruction().get(index), i, 1);
                        v.getTableMemory().setValueAt(Integer.toString(i) + " Page " + Integer.toString(frameIndex) , i, 0);
                    } else {
                        v.getTableMemory().setValueAt("", i, 1);
                        v.getTableMemory().setValueAt(Integer.toString(i) + " Page " + Integer.toString(frameIndex) , i, 0);
                    }
                    index++;
                }
            }
        }
        
//        int i = 0;
//        for (String temp: memory.getListInstructions()) {
//            changeTableSegment(v.getTableMemory(), 1, i);
//            i++;
//        }
    }
    
    private void changeTableSegment(JTable table, int columnIndex,int  rowIndex){
        table.getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer(){
            
            @Override
            
            public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column){
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                final List<Color> ListColors = listColors();
                String strX=(String) table.getValueAt(row,columnIndex );
                
                if(strX.length() >= 1){
                    String lastChar = String.valueOf(strX.charAt(strX.length()-1));
                    c.setBackground(ListColors.get(Integer.parseInt(lastChar)));
                    
                }else{
                    c.setBackground(Color.white);
                }
                return c;
            }
        });
    }
    
    private List<Color> listColors() {
        List<Color> listReturn = new ArrayList<>();
        listReturn.add(Color.red);
        listReturn.add(Color.yellow);
        listReturn.add(Color.orange);
        listReturn.add(Color.blue);
        listReturn.add(Color.green);
        listReturn.add(Color.magenta);
        listReturn.add(Color.pink);
        listReturn.add(Color.CYAN);
        listReturn.add(Color.LIGHT_GRAY);
        listReturn.add(Color.getHSBColor(21, 24, 102));
        listReturn.add(Color.getHSBColor(91, 2, 12));
        listReturn.add(Color.getHSBColor(0, 76, 102));
        listReturn.add(Color.getHSBColor(98, 21, 10));
        return listReturn;
    }
    
    private void setProcessesTableDisc(Process process) {
        for (Instruction temp: process.getListInstructions()) {
            v.getTableDisc().setValueAt(temp.toString(), p.getMemory2().getcurrentAmountMemoryUsed(), 1);
            p.getMemory2().setcurrentAmountMemoryUsed(p.getMemory2().getcurrentAmountMemoryUsed()+1);
        }
    }
    
    public void init() {
        
        v.setVisible(true);
        v.setLocationRelativeTo(null);

        v.getBtnLoadFile().addActionListener((ActionEvent e) -> {
            actionLoadFileBtn();
        });
        
        v.getStart_Btn().addActionListener((ActionEvent e) -> {
            actionStartBtn();
        });
         v.getBtnNextStep().addActionListener((ActionEvent e) -> {
            isStepByStep = true;
            v.getBtnExecute().setEnabled(false);
            actionBtnNextStep();
        });
        v.getBtnClean().addActionListener((ActionEvent e) -> {
            actionCleanBtn();
        });
        
        v.getBtnExecute().addActionListener((ActionEvent e) -> {
            isStepByStep = false;
            v.getBtnExecute().setEnabled(false);
            v.getBtnNextStep().setEnabled(false);
            autoExecute();
        });
        v.getBtnConfig().addActionListener((ActionEvent e) -> {
            configurationMenu();
        });
        v.getBtnStats().addActionListener((ActionEvent e) -> {
            actionShowStats();
        });

        c.getBtnCancel().addActionListener((ActionEvent e) -> {
            cancelConfig();
        });
        c.getBtnSave().addActionListener((ActionEvent e) -> {
            saveConfig();
        });
        v.getcmdButton().addActionListener((ActionEvent e) -> {
            if (isStepByStep) {
                if (INT10HSbS) {
                    String valueText = v.getjTextArea1().getText();
                    v.getlblInterruptionWarning().setVisible(false);
                    String valueInput = v.getTextInput().getText();
                    workingBCP.setDX(valueInput);
                    v.getjTextArea1().setText(valueText + ">>> " + valueInput + "\n");
                    v.getTextInput().setText("");
                    v.getTextInput().setEditable(false);
                }
                v.getlblInterruptionWarning().setVisible(false);
                v.getBtnNextStep().setEnabled(true);
            } else {
                if (paused == true) {
                    paused = false;
                }
            }
        });
    }
    
    private int calculateSecondsDif(Process current) {
        int SECONDS = 60;
        int SECONDS_PER_MINUTE = 60;
        int SECONDS_PER_HOUR = SECONDS_PER_MINUTE*SECONDS;
        
        int startHour = current.getStarted().getHour() * SECONDS_PER_HOUR;
        int startMinute = current.getStarted().getMinute() * SECONDS_PER_MINUTE;
        int startSecond = current.getStarted().getSecond();
        int amountSecondsStart = startHour + startMinute + startSecond;
        System.out.println(amountSecondsStart);
        
        int finishHour = current.getFinished().getHour() * SECONDS_PER_HOUR;
        int finishMinute = current.getFinished().getMinute() * SECONDS_PER_MINUTE;
        int finishSecond = current.getFinished().getSecond();
        int amountSecondsFinish = finishHour + finishMinute + finishSecond;
        System.out.println(amountSecondsFinish);
        
        return amountSecondsFinish - amountSecondsStart;
    }
    
    private void actionShowStats() {
        String allProcesses = "";
        for (Process temp : p.getCpu1().getFinishedProcesses()) {
            String name = temp.getID();
            String start = temp.getStarted().getHour()+":"+temp.getStarted().getMinute()+":"+temp.getStarted().getSecond();
            String finish = temp.getFinished().getHour()+":"+temp.getFinished().getMinute()+":"+temp.getFinished().getSecond();
            
            int secondsDif = calculateSecondsDif(temp);
            
            allProcesses += name + ", inicio: " + start + ", finalización: " + finish + ", Duración en segundos: "+ secondsDif +"\n";
            System.out.println("==============\n"+allProcesses);
        }
        for (Process temp : p.getCpu2().getFinishedProcesses()) {
            String name = temp.getID();
            String start = temp.getStarted().getHour()+":"+temp.getStarted().getMinute()+":"+temp.getStarted().getSecond();
            String finish = temp.getFinished().getHour()+":"+temp.getFinished().getMinute()+":"+temp.getFinished().getSecond();
            
            int secondsDif = calculateSecondsDif(temp);
            
            allProcesses += name + ", inicio: " + start + ", finalización: " + finish + ", Duración en segundos: "+ secondsDif + "\n";
            System.out.println("==============\n"+allProcesses);
        }
        
        JFrame f = new JFrame("frame");
                    JOptionPane.showMessageDialog(f ,
                        allProcesses ,
                        "Estadisticas de procesos" ,
                        JOptionPane.WARNING_MESSAGE);
    }
    
    private void cancelConfig(){
        c.getMemoryD().setText("");
        c.getMemoryP().setText("");
        c.getMemoryV().setText("");
        c.setVisible(false);
    }
    
    private void saveConfig(){
        if (!c.getMemoryP().getText().isEmpty() && !c.getMemoryD().getText().isEmpty() && !c.getMemoryP().getText().isEmpty()) {
            this.p = new Program(Integer.parseInt(c.getMemoryP().getText().trim()), Integer.parseInt(c.getMemoryD().getText().trim()), Integer.parseInt(c.getMemoryV().getText().trim()));
            configureTables();
            cancelConfig();
        }
    }
    
    private void configurationMenu() {
        c.setVisible(true);
    }
    
    private void actionLoadFileBtn() {
        FileLoader fileLoader = new FileLoader();
        fileLoader.loadFile();
        List<String> filesContent = fileLoader.getContent();
        for (String content: filesContent){
            System.out.println("=======================");
            System.out.println(content);
            
            instructionMaker instructMaker = new instructionMaker();
            Process process = new Process() {};
            process.setListInstructions(instructMaker.createListInstruction(content));
            if (!process.searchError()) {
                if (verifyMemorys(process)) {
                    setProcessLoad(process);
                    setProcessTable(process);
                    p.getCpu1().getListProcess().add(process);
                    p.getCpu2().getListProcess().add(process);
                    v.getStart_Btn().setEnabled(true);
                    v.getBtnConfig().setEnabled(false);
                } else {
                    JFrame f = new JFrame("frame");
                    JOptionPane.showMessageDialog(f ,
                        "No hay suficiente espacio" ,
                        "Error de archivo cargado" ,
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void startCPUS() {
        selectCPU();
        if (!p.getMemory1().getListProcess().isEmpty()) {
                p.getMemory1().setCuerrentProcess(p.getMemory1().getListProcess().get(0));
                if (p.getCPU_Use() == 1) {
                    p.getCpu1().setCurrentProcess(p.getMemory1().getCuerrentProcess());
                } else {
                    p.getCpu2().setCurrentProcess(p.getMemory1().getCuerrentProcess()); 
                }
                for (Process process : p.getMemory1().getListProcess()) {
                    process.setState("Preparado");
                }
                for (Process process : p.getMemory2().getListProcess()) {
                    process.setState("Preparado");
                }
                for (Process process : p.getMemory3().getListProcess()) {
                    process.setState("Preparado");
                }
        }
        
    }
    
    
    
    private void actionStartBtn() {
        p.getCpu1().setAlgorithm((String) v.getCmb_box_Planification().getSelectedItem());
        p.getCpu2().setAlgorithm((String) v.getCmb_box_Planification().getSelectedItem());
        if (((String) v.getCmb_box_Planification().getSelectedItem()).equals("RR")){
            p.getCpu1().setQbit((Integer.parseInt(v.getCmb_box_qbit().getSelectedItem().toString())));
            p.getCpu2().setQbit((Integer.parseInt(v.getCmb_box_qbit().getSelectedItem().toString())));
        }
        v.getBtnLoadFile().setEnabled(false);
        v.getBtnNextStep().setEnabled(true);
        v.getBtnExecute().setEnabled(true);
        v.getStart_Btn().setEnabled(false);
        startCPUS();
        p.getCpu1().setTimeProcess();

        this.memoryAlgorithm();
        List<String> list = getPlanificateAlgorithm();   
        configurePlanners(list,p.getProcesCount());
    }
    
    private void memoryAlgorithm(){
        switch (v.getCmb_box_MemoryA().getSelectedItem().toString()) {
            case "Fijo":
                this.fixed();
                break;
            case "Dinámico":
                this.dinamicate();
                break;
            case "Segmentación":
                this.segmentate();
                break;
            case "Paginación":
                this.paginate();
                break;
            default:
                throw new AssertionError();
        }
    }
    
    private void fixed(){
        MemoryController mc = new MemoryController();
        List<Partition> partitions = mc.fixedPartition(p.getMemory1());
        setProcessesTableMemoryFixed(p.getMemory1(), partitions); 
    }
    
    private void segmentate(){
        MemoryController m = new MemoryController();
        m.segmentation(p.getMemory1());
        setProcessesTableMemory2(p.getMemory1());
    }
    
    private void dinamicate(){
        MemoryController m = new MemoryController();
        m.dinamic(p.getMemory1());
        setProcessesTableMemory2(p.getMemory1());
        
                
    }
    
    private void paginate(){
        MemoryController m = new MemoryController();
        m.paginate(p.getMemory1());
        setProcessesTableMemoryPage(p.getMemory1());
    }
    
  private void setProcessesTableMemoryFixed(Memory memory, List<Partition> partitions) {
    int i = 0;
    int partitionSize = partitions.get(0).getSize();
    List<String> instructions = new ArrayList<>();
    for (Partition partition : partitions) {
      partition.showInstruction();
      System.out.println("PARTITION STARTING POS: " + partition.getStartPos());
      System.out.println("PARTITION INSTRUCTIONS SIZE: " + partition.getPartitionInstructions().size());
      List<String> listInst = partition.getPartitionInstructions();
      i = partition.getStartPos();
      for (String inst : listInst) {
        System.out.println("i IS: " + i);
        instructions.add(inst);
        v.getTableMemory().setValueAt(inst, i, 1);
        i++;
      }
      System.out.println("PARTITION FINISHING POS: " + partition.getFinishPos());

    }
    memory.setListInstructions(instructions);
    
    i = 0;
        int colorIndex = 0;
    /*
    int colorChange = 0;
    for(String inst : memory.getListInstructions()) {
      if(colorChange == partitionSize) {
        colorIndex++;
        colorChange = 0;
        System.out.println("***************** COLOR INDEX WAS CHANGED *****************");
      }
      this.setPartitionColor(v.getTableMemory(), 1, i, colorIndex);
      colorChange++;
      i++;
    }
    */
    
    for(Partition part : partitions) {
      System.out.println("COLOR INDEX IS: " + colorIndex);
      part.showInstruction();
      for(String inst : part.getPartitionInstructions()) {
        this.setPartitionColor(v.getTableMemory(), 1, i, colorIndex);
        i++;
      }
      colorIndex++;
    }
 
  }
        
  private void setPartitionColor(JTable table, int columnIndex, int rowIndex, int colorIndex) {
    table.getColumnModel().getColumn(columnIndex).setCellRenderer(new DefaultTableCellRenderer() {

      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        final List<Color> ListColors = listColors();
        String strX = (String) table.getValueAt(row, columnIndex);

        System.out.println("strX is: " + strX);
        if (strX != null && strX.length() >= 1) {
          if(strX.equals("FREE")) {
            c.setBackground(Color.green);
          } else {
            c.setBackground(ListColors.get(colorIndex));
          }
        } else {
          c.setBackground(Color.white);
        }
        return c;
      }
    });
  }
    
    private List<String> getPlanificateAlgorithm() {
        switch (v.getCmb_box_Planification().getSelectedItem().toString()) {
            case "FCFS" -> {
                return p.getCpu1().executeFCFS();
            }
            case "SPN" -> {
                return p.getCpu1().executeSPN();
            }
            case "SRT" -> {
                return p.getCpu1().executeSRT();
            }
            case "RR" -> {
                return p.getCpu1().executeRR();
            }
            case "HRRN" -> {
                return p.getCpu1().executeHRRN();
            }
            default -> throw new AssertionError();
        }
    }
    private void CleanBtn_Program() {
        v.getBtnLoadFile().setEnabled(true);
        v.getBtnConfig().setEnabled(true);
        v.getBtnNextStep().setEnabled(false);
        v.getBtnExecute().setEnabled(false);
        v.getStart_Btn().setEnabled(false);
        v.getTextInput().setEditable(false);
        v.getBtnStats().setEnabled(false);
    }
            
    private void actionCleanBtn() {
            p = new Program(128,512,64);
            
            //limpia la tabla de procesos
            DefaultTableModel dtmProcess = new DefaultTableModel(0, 0);
            String headerProcess[] = new String[]{"Procesos", "Estados"};
            dtmProcess.setColumnIdentifiers(headerProcess);
            v.getTableProcesses().setModel(dtmProcess);
            
            //limpia la tabla de memoria
            DefaultTableModel dtmMemory = new DefaultTableModel(0, 0);
            String headerMemory[] = new String[]{"Pos", "Valor en memoria"};
            dtmMemory.setColumnIdentifiers(headerMemory);
            v.getTableMemory().setModel(dtmMemory);
            
            //limpia la tabla de disco
            DefaultTableModel dtmDisc = new DefaultTableModel(0, 0);
            String headerDisc[] = new String[]{"Pos", "Valor en disco"};
            dtmDisc.setColumnIdentifiers(headerDisc);
            v.getTableDisc().setModel(dtmDisc);
            
            //Limpia los botones
            CleanBtn_Program();
            
            //limpia los textFields de los BPCs
            cln_BCP();
            
            //limpia todo lo relacionado a la pantalla
            v.getTextInput().setText("");
            v.getjTextArea1().setText("");
            
            configureTables();
            cleanPlanningTables();
            
            paused = false;
            state = "";
            inst = 0;
            rowTable=0;
            columnTable=0;
            isStepByStep = false;
            comparator = false;
            INT10HSbS = false;
            processesCount = 1;
    }
    
    private void cleanPlanningTables() {
        JTable table1 = new JTable();
        JTable table2 = new JTable();
        
        table1 = v.getjTable1();
        table2 = v.getjTable2();
        
        /*Table CPU 1*/
        
        model2 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int rows, int columns) {
                return true;
            }   
        };
        model2.addColumn("CPU 1");
        model2.addColumn("Tiempo");
        model2.addColumn("Tiempo");
        model2.addColumn("Tiempo");
        
        table1.setModel(model2);
        table1.getColumnModel().getColumn(0).setMinWidth(60);
        table1.getColumnModel().getColumn(0).setMaxWidth(60);
        
        /*Table CPU 2*/
        
        model3 = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int rows, int columns) {
                return true;
            }   
        };
        model3.addColumn("CPU 2");
        model3.addColumn("Tiempo");
        model3.addColumn("Tiempo");
        model3.addColumn("Tiempo");
        
        table2.setModel(model3);
        table2.getColumnModel().getColumn(0).setMinWidth(60);
        table2.getColumnModel().getColumn(0).setMaxWidth(60);
    }
    
    private void executeInterruptions(CPU cpu) {
        int currentLine = cpu.getCurrentLine();
       
        if (cpu.getCurrentProcess().getListInstructions().size() > currentLine) {
            String register = cpu.getCurrentProcess().getListInstructions().get(currentLine).getRegister1();
            if ("INT".equals(cpu.getCurrentProcess().getListInstructions().get(currentLine).getType())) {
                switch (register) {
                    case "20H":
                        this.state = "END";
                                JFrame f = new JFrame("frame");
                                JOptionPane.showMessageDialog(f ,
                                "El Programa a terminado por la interrupcion 20H" ,
                                "Fin de ejecución" ,
                                JOptionPane.ERROR_MESSAGE);

                                v.getBtnStats().setEnabled(false);
                                v.getBtnNextStep().setEnabled(false);
                                v.getBtnExecute().setEnabled(false);
                        break;
                    case "09H":
                        if (isStepByStep){
                            INT09HStepByStep(cpu);
                        } else {
                            INT09HExecute(cpu);
                        }
                        break;
                    case "10H":
                        if (isStepByStep){
                            INT10HStepByStep(cpu);
                        } else {
                            INT10HExecute(cpu);
                        }
                        break;
                    default:
                        System.out.println("No era una interrupcion");
                }
            }
        }
    }
    
    private void INT09HStepByStep(CPU cpu) {
        
        v.getBtnNextStep().setEnabled(false);
        String valueDX = cpu.getCurrentProcess().getBcp().getDX();
        System.out.println(valueDX);
        String valuejTextArea = v.getjTextArea1().getText();
        v.getjTextArea1().setText(valuejTextArea + "Valor de DX: " + valueDX + "\n");
        v.getlblInterruptionWarning().setVisible(true);
        v.setlblInterruptionWarning("INT 09H, presione ok para continuar");
    }
    
    private void INT09HExecute(CPU cpu) {
        String valueDX = cpu.getCurrentProcess().getBcp().getDX();
        System.out.println(valueDX);
        String valuejTextArea = v.getjTextArea1().getText();
        v.getjTextArea1().setText(valuejTextArea + "Valor de DX: " + valueDX + "\n");
        v.getlblInterruptionWarning().setVisible(true);
        paused = true;
        while (paused) {
            v.setlblInterruptionWarning("INT 09H, presione ok para continuar");
        }
        v.getlblInterruptionWarning().setVisible(false);
    }
    
    private void INT10HStepByStep(CPU cpu) {
        INT10HSbS = true;
        v.getBtnNextStep().setEnabled(false);
        v.getTextInput().setEditable(true);
        v.getlblInterruptionWarning().setVisible(true);
        v.setlblInterruptionWarning("INT 10H, ingrese un valor para DX");
        workingBCP = cpu.getCurrentProcess().getBcp();
    }
    
    private void INT10HExecute(CPU cpu) {
        String valueText = v.getjTextArea1().getText();
        paused = true;
        v.getTextInput().setEditable(true);
        v.getlblInterruptionWarning().setVisible(true);
        while (paused) {
            v.setlblInterruptionWarning("INT 10H, ingrese un valor para DX");
        }
        v.getlblInterruptionWarning().setVisible(false);
        String valueInput = v.getTextInput().getText();
        cpu.getCurrentProcess().getBcp().setDX(valueInput);
        v.getjTextArea1().setText(valueText + ">>> " + valueInput + "\n");
        v.getTextInput().setText("");
        v.getTextInput().setEditable(false);
    }
    
    private void actionBtnNextStep() {
        if (p.getCPU_Use() == 1) {
            executeJMP_JNE_JN(p.getCpu1());
            executeInterruptions(p.getCpu1());   
                if (!p.getCpu1().ejecuteProcessInstruction()){
                    if (p.getMemory1().getListProcess().size() > 1) {
                        executeJMP_JNE_JN(p.getCpu1());
                        setMemoryFinishProcess();
                        startCPUS();
                        cln_BCP();
                        actionBtnNextStep();
                    } else {
                        this.state = "END";
                        JFrame f = new JFrame("frame");
                        JOptionPane.showMessageDialog(f ,
                                "El Programa a terminado" ,
                                "Fin de ejecución" ,
                                JOptionPane.ERROR_MESSAGE);
                        
                        v.getBtnStats().setEnabled(true);
                        v.getBtnNextStep().setEnabled(false);
                        v.getBtnExecute().setEnabled(false);
                    }
                }
        } else {
            executeJMP_JNE_JN(p.getCpu2());
            executeInterruptions(p.getCpu2());
                if (!p.getCpu2().ejecuteProcessInstruction()){
                    if (p.getMemory1().getListProcess().size() > 1) {
                        executeJMP_JNE_JN(p.getCpu2());
                        setMemoryFinishProcess();
                        startCPUS();
                        cln_BCP();
                        actionBtnNextStep();
                    } else {
                        this.state = "END";
                        JFrame f = new JFrame("frame");
                        JOptionPane.showMessageDialog(f ,
                        "El Programa a terminado" ,
                        "Fin de ejecución" ,
                        JOptionPane.ERROR_MESSAGE);
                        v.getBtnStats().setEnabled(true);
                        v.getBtnNextStep().setEnabled(false);
                        v.getBtnExecute().setEnabled(false);
                    }
                }
        }
        inst++;
        refreshBCP();
    }
    
    private void executeJMP_JNE_JN(CPU cpu) {
        int currentLine = cpu.getCurrentLine();
        if (cpu.getCurrentProcess().getListInstructions().size() > currentLine) {
            if(cpu.isJMP() || cpu.isJE() || cpu.isJNE()){
                int addsub = cpu.getCurrentLine() + cpu.getCurrentInstruction().getNumber() + 1;
                if (addsub < cpu.getCurrentProcess().getListInstructions().size() && addsub >= 0) {
                    cpu.setCurrentLine(addsub);
                } else {
                    JFrame f = new JFrame("frame");
                    JOptionPane.showMessageDialog(f ,
                    "La instruccion no fue posible por desbordamiento de proceso" ,
                    "Salto fallido" ,
                    JOptionPane.ERROR_MESSAGE);
                    v.getBtnStats().setEnabled(true);
                    v.getBtnNextStep().setEnabled(false);
                    v.getBtnExecute().setEnabled(false);
                }
            }
        }
    }
    
    /*private void verifyIrregular() {
        if (p.getCPU_Use() == 1) {
            if (p.getCpu1().isJMP()) {
                int addsub = p.getCpu1().getCurrentLine() + p.getCpu1().getCurrentInstruction().getNumber();
                if (addsub < p.getCpu1().getCurrentProcess().getListInstructions().size() && addsub >= 0) {
                    p.getCpu1().setCurrentLine(addsub);
                } else {
                    JFrame f = new JFrame("frame");
                        JOptionPane.showMessageDialog(f ,
                        "La instruccion JMP no fue posible por desbordamiento de proceso" ,
                        "JMP fallido" ,
                        JOptionPane.ERROR_MESSAGE);
                    v.getBtnStats().setEnabled(true);
                        v.getBtnNextStep().setEnabled(false);
                        v.getBtnExecute().setEnabled(false);
                }
            }
        } else {
            if (p.getCpu1().isJMP()) {
                int addsub = p.getCpu1().getCurrentLine() + p.getCpu1().getCurrentInstruction().getNumber();
                if (addsub < p.getCpu1().getCurrentProcess().getListInstructions().size() && addsub >= 0) {
                    p.getCpu1().setCurrentLine(addsub);
                } else {
                    JFrame f = new JFrame("frame");
                        JOptionPane.showMessageDialog(f ,
                        "La instruccion JMP no fue posible por desbordamiento de proceso" ,
                        "JMP fallido" ,
                        JOptionPane.ERROR_MESSAGE);
                    JOptionPane.showMessageDialog(f ,
                    "El Programa a terminado" ,
                    "Fin de ejecución" ,
                    JOptionPane.ERROR_MESSAGE);
                    
                    v.getBtnStats().setEnabled(true);
                }
            }
        }
        inst++;
        refreshBCP();
    }*/
    
    private void autoExecute() {
        Thread done;
        done = new Thread( new  threadProcess() {
            public void run() {
                while (state != "END") {
                    try {
                        Thread.sleep((1000));
                    } catch (InterruptedException e) {
                        System.err.println("se cayó");
                    }
                    actionBtnNextStep();
                }
            }
        });
        done.start();
    }
    
    private void cln_BCP(){
            v.getTxtAC_CPU1().setText("");
            v.getTxtAX_CPU1().setText("");
            v.getTxtBX_CPU1().setText("");
            v.getTxtCX_CPU1().setText("");
            v.getTxtDX_CPU1().setText("");
            v.getTxtPC_CPU1().setText("");
            v.getTxtIR_CPU1().setText("");
            
            v.getTxtAC_CPU2().setText("");
            v.getTxtAX_CPU2().setText("");
            v.getTxtBX_CPU2().setText("");
            v.getTxtCX_CPU2().setText("");
            v.getTxtDX_CPU2().setText("");
            v.getTxtPC_CPU2().setText("");
            v.getTxtIR_CPU2().setText("");
    }
    private void refreshBCP(){
        if (p.getCPU_Use() == 1) {
//            v.getTxtIR_CPU1();
//            v.getTxtPC_CPU1();
            v.getTxtPC_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getPC());
            v.getTxtIR_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getIR());
            v.getTxtAC_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getAC());
            v.getTxtAX_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getAX());
            v.getTxtBX_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getBX());
            v.getTxtCX_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getCX());
            v.getTxtDX_CPU1().setText(p.getCpu1().getCurrentProcess().getBcp().getDX());
          
        } else {
            v.getTxtPC_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getPC());
            v.getTxtIR_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getIR());
            v.getTxtAC_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getAC());
            v.getTxtAX_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getAX());
            v.getTxtBX_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getBX());
            v.getTxtCX_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getCX());
            v.getTxtDX_CPU2().setText(p.getCpu2().getCurrentProcess().getBcp().getDX());
            
        }
        
    }
    
    private void setMemoryFinishProcess() {
        p.getMemory1().setAvailableMemory(p.getMemory1().getAvailableMemory()-p.getMemory1().getCuerrentProcess().getListInstructions().size());
        p.getMemory1().getListProcess().remove(p.getMemory1().getCuerrentProcess());
        while (!p.getMemory2().getListProcess().isEmpty()&&  p.getMemory1().getAvailableMemory() < p.getMemory2().getListProcess().get(0).getListInstructions().size())  {
            p.getMemory1().addProcess(p.getMemory2().getListProcess().get(0));
            p.getMemory2().setAvailableMemory(p.getMemory2().getAvailableMemory()-p.getMemory2().getListProcess().get(0).getListInstructions().size());
            p.getMemory2().getListProcess().remove(p.getMemory2().getListProcess().remove(0));
                
            while (!p.getMemory3().getListProcess().isEmpty() && p.getMemory2().getAvailableMemory() < p.getMemory3().getListProcess().get(0).getListInstructions().size()) {
                p.getMemory2().addProcess(p.getMemory3().getListProcess().get(0));
                p.getMemory3().setAvailableMemory(p.getMemory3().getAvailableMemory()-p.getMemory3().getListProcess().get(0).getListInstructions().size());
                p.getMemory3().getListProcess().remove(p.getMemory3().getListProcess().remove(0));
            }
        }
    }
    
    private void selectCPU(){
        int tmp = (int) ( Math.random() * 2 + 1);
        p.setCPU_Use(tmp);
    }
  
}
