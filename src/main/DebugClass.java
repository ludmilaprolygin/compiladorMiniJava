package main;

import ar.edu.uns.cs.cei.ceivmapi.CeIVMAPI;
import ar.edu.uns.cs.cei.ceivmapi.exceptions.*;
import model.codeGeneration.Instructions;

import java.util.Scanner;

public class DebugClass {

    public static void main(String[] args) throws Exception {
        CeIVMAPI ceivm = new CeIVMAPI();

        // 1) Parsear + ensamblar
        ceivm.parseAndAssemble("/home/ludmila-prolygin/Documents/compiladorMiniJava/[sinError_13.java].out");

        // 2) Cargar a memoria
        ceivm.loadProgram();

        // 3) Inicializar registros
        ceivm.initializeVM();

        System.out.println("=======================================");
        System.out.println("        CeIVM DEBUG - STEP MODE        ");
        System.out.println("=======================================");

        dumpProgram(ceivm);

        Scanner sc = new Scanner(System.in);

        while (true) {
            printDebugState(ceivm);

            System.out.print("ENTER para siguiente instrucción...");
            sc.nextLine();

            // DEBUG adicional: justo antes de ejecutar la instrucción
            int pc = ceivm.getRegisterFileAccess().getPc();
            int sp = ceivm.getRegisterFileAccess().getSp();
            System.out.printf("DEBUG BEFORE STEP: PC=%d SP=%d%n", pc, sp);

// leer cima de pila (posición SP)
            try {
                int topValue = ceivm.getMemoryAccess().read(sp);
                System.out.println("  TOP_OF_STACK = " + topValue);

                // intentar interpretar topValue como dirección de código
                if (topValue >= 0 && topValue < ceivm.getRegisterFileAccess().getHp()) {
                    try {
                        int opcodeAtTarget = ceivm.getMemoryAccess().read(topValue);
                        String mnemonic = ceivm.disassembleOpcode(opcodeAtTarget);
                        System.out.printf("  MEM_AT_TOP [%d] -> OPCODE=%d (%s)%n", topValue, opcodeAtTarget, mnemonic);
                        // si tiene argumento:
                        // int arg = ceivm.getMemoryAccess().read(topValue + 1);
                        // System.out.printf("    ARG = %d%n", arg);
                    } catch (Exception ex) {
                        System.out.printf("  MEM_AT_TOP [%d] NO VÁLIDO: %s%n", topValue, ex.getMessage());
                    }
                } else {
                    System.out.println("  TOP_OF_STACK no parece una dirección válida en CODE.");
                }
            } catch (Exception e) {
                System.out.println("  NO PUDE LEER TOP (excepcion): " + e.getMessage());
            }


            ceivm.executeNextStep();
        }

    }

    // ---------------------------------------------------------
    // IMPRIME EL ESTADO ANTES DE EJECUTAR CADA INSTRUCCIÓN
    // ---------------------------------------------------------

    private static void printDebugState(CeIVMAPI ceivm) throws Exception {
        int pc = ceivm.getRegisterFileAccess().getPc();
        int sp = ceivm.getRegisterFileAccess().getSp();
        int fp = ceivm.getRegisterFileAccess().getFp();
        int hp = ceivm.getRegisterFileAccess().getHp();

        int opcode = ceivm.getMemoryAccess().read(pc);
        String mnemonic = ceivm.disassembleOpcode(opcode);

        System.out.println("---------------------------------------");
        System.out.printf("PC=%d | SP=%d | FP=%d | HP=%d%n", pc, sp, fp, hp);
        System.out.printf("OPCODE=%d (%s)%n", opcode, mnemonic);

        if (instrHasArg(opcode)) {
            int arg = ceivm.getMemoryAccess().read(pc + 1);
            System.out.printf("ARG=%d%n", arg);
        }

        dumpStack(ceivm, sp);
        System.out.println("---------------------------------------");
    }


    // ---------------------------------------------------------
    // DUMP COMPLETO DEL PROGRAMA ENSAMBLADO
    // ---------------------------------------------------------

    private static void dumpProgram(CeIVMAPI ceivm) throws Exception {
        System.out.println("\n=== PROGRAMA ENSAMBLADO EN MEMORIA ===");

        // Hasta el heap
        int end = ceivm.getRegisterFileAccess().getHp();

        for (int i = 0; i < end; i++) {
            int op = ceivm.getMemoryAccess().read(i);
            String mnemonic = ceivm.disassembleOpcode(op);
            System.out.printf("%04d: %d (%s)%n", i, op, mnemonic);
        }

        System.out.println("=======================================\n");
    }


    // ---------------------------------------------------------
    // PRINT DE LA PILA (5 valores alrededor del SP)
    // ---------------------------------------------------------

    // Safe stack dump — pega esto en tu DebugClass en lugar del dumpStack anterior
    private static void dumpStack(CeIVMAPI ceivm, int sp) {
        System.out.println("STACK (cerca de la cima):");

        // la cima real es SP - 1 (SP apunta a la primera celda libre)
        int spTop = sp - 1;

        // imprimimos unas pocas entradas alrededor de la cima, de forma segura
        for (int offset = 3; offset >= -3; offset--) {
            int idx = spTop + offset;
            if (idx < 0) continue; // fuera de la memoria válida (antes del stack)
            try {
                int val = ceivm.getMemoryAccess().read(idx);
                if (idx == spTop) {
                    System.out.printf("[%04d] = %d   <-- TOP (SP-1)%n", idx, val);
                } else {
                    System.out.printf("[%04d] = %d%n", idx, val);
                }
            } catch (Exception e) {
                // no podemos leer (fuera de memoria), lo marcamos y seguimos
                if (idx == spTop) {
                    System.out.printf("[%04d] = <NO-READ>   <-- TOP (SP-1)%n", idx);
                } else {
                    System.out.printf("[%04d] = <NO-READ>%n", idx);
                }
            }
        }
    }


    // ---------------------------------------------------------
    // SABER SI UNA INSTRUCCIÓN TIENE PARÁMETRO
    // (PONÉ ACÁ EL SET REAL DE TU ISA)
    // ---------------------------------------------------------

    private static boolean instrHasArg(int opcode) {
        return opcode == Instructions.LOAD.ordinal() ||        // offset
                opcode == Instructions.STORE.ordinal() ||       // offset
                opcode == Instructions.LOADREF.ordinal() ||     // offset/ref
                opcode == Instructions.STOREREF.ordinal() ||    // offset/ref
                opcode == Instructions.RMEM.ordinal() ||        // cantidad
                opcode == Instructions.FMEM.ordinal() ||        // cantidad
                opcode == Instructions.JUMP.ordinal() ||        // dirección
                opcode == Instructions.BF.ordinal() ||          // dirección
                opcode == Instructions.BT.ordinal() ||          // dirección
                opcode == Instructions.CALL.ordinal() ||        // dirección
                opcode == Instructions.DW.ordinal();            // valor
    }

}
