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
public class instructionMaker {

    public instructionMaker() {
    }
    
    // crea una lista de instrucciones creado a partir de un texto proveniente de un archivo
    // primero se splitean las lineas luego se parcean estas
    public List<Instruction> createListInstruction(String text) {
        String[] arrayIntruction = text.split("\n");
        List<Instruction> instructionList = new ArrayList<>();
        int lineInstruction = 1;
        for (String line : arrayIntruction) {
            instructionList.add(parseStringtoInstruction(line, lineInstruction));
            lineInstruction += 1;
        }
        return instructionList;
    }
    
    //parcea un texto para crear una linea, según sea válido el texto por lo que puede identificar 
    //errores de estructuracion del texto
    private Instruction parseStringtoInstruction(String text, int lineInstruction) {
        if (text.length() >= 3){ // verifica que el texto tenga almenos 3 letras
            switch (text.substring(0, 3)) {
                case "LOA": return loadpushInstruction(text, "LOAD", 2, lineInstruction);
                case "STO": return storeInstruction(text, 2, lineInstruction);
                case "MOV": return movInstruction(text, 1, lineInstruction);
                case "ADD": return generalInstruction(text, "ADD", 3, lineInstruction);
                case "SUB": return generalInstruction(text, "SUB", 3, lineInstruction);
                case "INC": return incdecInstruction(text, "INC", 1, lineInstruction);
                case "DEC": return incdecInstruction(text, "DEC", 1, lineInstruction);
                case "SWA": return swapInstruction(text, 1, lineInstruction);
                case "INT": return intInstruction(text, lineInstruction);
                case "JMP": return jmpjejneInstruction(text, "JMP", 2, lineInstruction);
                case "CMP": return cmpInstruction(text, 2, lineInstruction);
                case "JE ": return jmpjejneInstruction(text, "JE", 2, lineInstruction);
                case "JNE": return jmpjejneInstruction(text, "JNE", 2, lineInstruction);
//                case "PAR": AAAAAA;
                case "PUS": return loadpushInstruction(text, "PUSH", 1, lineInstruction);
                case "POP": return generalInstruction(text, "POP", 1, lineInstruction);

                default:
                    return instructionErrorDefault(lineInstruction);
            }
        } else {
            return instructionErrorDefault(lineInstruction);
        }
    }
    
    //#########################  MAKING INSTRUCTION ####################################
    
    //Crea una instruccion load o push con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion load o push
    private Instruction loadpushInstruction(String text, String type, int wei, int line) {
        Instruction loadpush = new Instruction();
        if (text.length() < 4) { // verifica que el texto tenga almenos 4 letras
            return instructionErrorDefault(line);
        } else {
            if (text.substring(0, 4).equals(type)) {
                loadpush.setType(type);
                String registro = registerInstruction(text.substring(5, 7));
                if (registro.equals("ERROR")) {
                    return instructionErrorDefault(line);
                } else {
                    loadpush.setRegister1(registro);
                }
            } else {
                return instructionErrorDefault(line);
            }
            loadpush.setLine(line);
            loadpush.setInst(text);
            return loadpush;
        }
    }
    
    //Crea una instruccion mov con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion mov, verifica que el primer registro exista, que el segundo
    //parametro sea un registro valido o un número valido
    private Instruction movInstruction(String text, int wei, int line) {
        Instruction mov = new Instruction();
        mov.setType("MOV");
        if (text.length() < 6) { // verifica que el texto tenga almenos 4 letras
            return instructionErrorDefault(line);
        } else {
            String register1 = registerInstruction(text.substring(4, 6));
            if (register1.equals("ERROR")) { // verifica que el registro exista, si no es un error
                return instructionErrorDefault(line);
            } else { // si el registro existe sigue
                mov.setRegister1(register1); 
                if (text.length() >= 10) { // verifica que sea posible la existencia un registro y no un numero de 1 digito
                    String register2 = registerInstruction(text.substring(8, 10));
                    if (register2.equals("ERROR")) { // si es error, no es un registro es un numero o un error
                        if (isNumeric(text.substring(8).trim())) { // si es un numero lo registra
                            int num = Integer.parseInt(text.substring(8).trim());
                            mov.setNumber(num);
                        } else { // sino es un numero es un error
                            return instructionErrorDefault(line);
                        }
                    } else { // es un registro 
                        mov.setRegister2(register2);
                    }
                } else { // es posiblemente un numero de 1 dígito
                    if (isNumeric(text.substring(8).trim())) { // si es un numero lo registra
                        int num = Integer.parseInt(text.substring(8).trim());
                        mov.setNumber(num);
                    } else { // sino es un numero es un error
                        return instructionErrorDefault(line);
                    }
                }
            }
        mov.setInst(text);
        mov.setLine(line);
        return mov;
        }
    }
    
    //Crea una instruccion store con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion store
    private Instruction storeInstruction(String text, int wei, int line) {
        Instruction store = new Instruction();
        if (text.length() < 5) { // verifica que el texto tenga almenos 4 letras
            System.out.println("entro1");
            return instructionErrorDefault(line);
        } else {
            if (text.substring(0, 5).equals("STORE")) {
                store.setType("STORE");
                String registro = registerInstruction(text.substring(6, 8));
                if (registro.equals("ERROR")) {
                    System.out.println("entro2");
                    return instructionErrorDefault(line);
                } else {
                    store.setRegister1(registro);
                }
            } else {
                System.out.println("entro3");
                return instructionErrorDefault(line);
            }
            store.setInst(text);
            store.setLine(line);
            return store;
        }
    }
    
    //Crea una instruccion según el tipo con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion, utilizada en instrucciones de 3 letras más un registro
    // ejemplo: "SUB BX", "ADD CX", "POP AX","DEC BX", "INC DX"
    private Instruction generalInstruction(String text, String type, int wei, int line) {
        Instruction gen = new Instruction();
        gen.setType(type);
        if (text.length() < 6) { // verifica que el texto tenga almenos 6 letras
            return instructionErrorDefault(line);
        } else {
            String registro = registerInstruction(text.substring(4, 6));
            if (registro.equals("ERROR")) {
                return instructionErrorDefault(line);
            } else {
                gen.setRegister1(registro);
            }
            gen.setInst(text);
            return gen;
        }
    }
    
    //Crea una instruccion ind o dec con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion dec o inc, además verifica que sea un inc o dec con o sin registro
    private Instruction incdecInstruction(String text, String type, int wei, int line) {
        if (text.length() == 3) {
            Instruction incdec = new Instruction();
            incdec.setType(type);
            return incdec;
        } else {
            return generalInstruction(text, type, wei, line);
        }
    }
    
    //Crea una instruccion swap con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion swap
    private Instruction swapInstruction(String text, int wei, int line) {
        Instruction swap = new Instruction();
        if (text.length() < 11) { // verifica que el texto tenga almenos 4 letras
            return instructionErrorDefault(line);
        } else {
            if (text.substring(0, 4).equals("SWAP")) {
                swap.setType("SWAP");
                String register1 = registerInstruction(text.substring(5, 7));
                String register2 = registerInstruction(text.substring(9, 11));
                if (register1.equals("ERROR") || register2.equals("ERROR")) { // verifica que el registro exista, si no es un error
                    return instructionErrorDefault(line);
                } else { // si el registro existe sigue
                    swap.setRegister1(register1);
                    swap.setRegister2(register2);
                }
            } else {
                return instructionErrorDefault(line);
            }
            swap.setInst(text);
            swap.setLine(line);
            return swap;
        }
    }
    
    //Crea una instruccion jmp, je o jne con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion jmp, je o jne
    private Instruction jmpjejneInstruction(String text, String type, int wei, int line) {
        Instruction jmpjejne = new Instruction();
        if (text.length() > 4) {
            if (type.equals("JE")) {
                if (isNumeric(text.substring(3).trim())) {
                    jmpjejne.setNumber(Integer.parseInt(text.substring(3).trim()));
                } else {
                    return instructionErrorDefault(line);
                }
            } else {
                System.out.println(text.substring(4));
                if (isNumeric(text.substring(4).trim())) {
                    jmpjejne.setNumber(Integer.parseInt(text.substring(4).trim()));
                } else {
                    return instructionErrorDefault(line);
                }
            }
            jmpjejne.setInst(text);
            jmpjejne.setType(type);
            jmpjejne.setLine(line);
        }
        return jmpjejne;
    }
    
    //Crea una instruccion INT con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion INT
    private Instruction intInstruction(String text, int line) {
        Instruction iNt = new Instruction();
        if (text.length() < 7) {
            return instructionErrorDefault(line);
        } else {
            if (text.substring(4, 7).equals("20H") || text.substring(4, 7).equals("09H") || text.substring(4, 7).equals("10H")) {
                iNt.setRegister1(text.substring(4, 7));
            } else {
                return instructionErrorDefault(line);
            }
            iNt.setInst(text);
            iNt.setType("INT");
            iNt.setLine(line);
            return iNt;
        }
    }
    
    //Crea una instruccion cmp con sus validaciones necesarias, si la funcon tira una instruccion error es
    //por una mala estructura de la instruccion cmp
    private Instruction cmpInstruction(String text, int wei, int line) {
        Instruction cmp = new Instruction();
        if (text.length() < 10) { // verifica que el texto tenga almenos 4 letras
            return instructionErrorDefault(line);
        } else {    
            String register1 = registerInstruction(text.substring(4, 6));
            String register2 = registerInstruction(text.substring(8, 10));
            if (register1.equals("ERROR") || register2.equals("ERROR")) { // verifica que el registro exista, si no es un error
                return instructionErrorDefault(line);
            } else { // si el registro existe sigue
                cmp.setRegister1(register1);
                cmp.setRegister2(register2);
            }
            cmp.setInst(text);
            cmp.setType("CMP");
            cmp.setLine(line);
            return cmp;
        }
    }
    
    //Crea una instruccion de typo error por defaul
    private Instruction instructionErrorDefault(int line) {
        Instruction error = new Instruction();
        error.setError("Error de sintaxis de instruccion");
        error.setLine(line);
        
        return error;
    }
    
    //######################################################################
    
    // verifica si el string es un registro, si no es retorna un error
    private String registerInstruction(String register) {
        return switch (register) {
            case "AX" -> "AX";
            case "BX" -> "BX";
            case "CX" -> "CX";
            case "DX" -> "DX";
            default -> "ERROR";
        };  
    }
    
    //verifica que un string contenga solo numeros
    private static boolean isNumeric(String strNum) {
    if (strNum == null) {
        return false;
    }
    try {
        Integer.parseInt(strNum);
    } catch (NumberFormatException nfe) {
        return false;
    }
    return true;
    }
}
