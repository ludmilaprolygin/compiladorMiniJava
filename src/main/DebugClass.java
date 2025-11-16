package main;

import ar.edu.uns.cs.cei.ceivmapi.CeIVMAPI;
import ar.edu.uns.cs.cei.ceivmapi.CeIVMAPISpecialRegs;
import ar.edu.uns.cs.cei.ceivmapi.exceptions.CeIVMHaltException;
import ar.edu.uns.cs.cei.ceivmapi.exceptions.CeIVMAPIInvalidStateException;
import ar.edu.uns.cs.cei.ceivmapi.exceptions.CeIVMRuntimeException;
import ar.edu.uns.cs.cei.ceivmapi.exceptions.CeIVMMemoryException;

public class DebugClass {

    public static void main(String[] args) {
        try {
            CeIVMAPI ceivm = new CeIVMAPI();
            ceivm.parseAndAssemble("/home/ludmila-prolygin/Documents/compiladorMiniJava/[sinError_12.java].out");
            ceivm.loadProgram();
            ceivm.initializeVM();

            System.out.println("Debug CeIVM - ejecución paso a paso");
            System.out.println("----------------------------------");

            boolean halted = false;

            while (!halted) {
                // Obtener registros
                CeIVMAPISpecialRegs regs = ceivm.getRegisterFileAccess();

                System.out.printf("PC=%d SP=%d FP=%d HP=%d HL=%d\n",
                        regs.getPc(),
                        regs.getSp(),
                        regs.getFp(),
                        regs.getHp(),
                        regs.getHl());
                //ceivm.peekStack()); // cima de la pila

                // Ejecutar siguiente instrucción
                ceivm.executeNextStep();

            }

        } catch (CeIVMAPIInvalidStateException e) {
            System.err.println("Error de estado de la VM: " + e.getMessage());
        } catch (CeIVMRuntimeException e) {
            System.err.println("Error de ejecución: " + e.getMessage());
        } catch (CeIVMMemoryException e) {
            System.err.println("Error de memoria: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
    }
}
