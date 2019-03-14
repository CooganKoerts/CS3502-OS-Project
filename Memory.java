/*

    This class builds the registers that will be used by the operating system

 */

public class Memory {
    private int[] registers;
    private char[] disk; //TODO
    //Ram ram; //TODO
    //TODO: simulated RAM & simulated hard drive called 'disk'

    public Memory() {
    }

    public Memory(int numOfRegisters) {
        registers = new int[numOfRegisters];
        //disk
        //ram
    }

    public int[] getRegisters() {
        return registers;
    }

    public char[] getDisk() {
        return disk;
    }

    public void setRegisters(int numOfRegisters) {
        registers = new int[numOfRegisters];
    }

    /* public void setDisk(char disk) {
        //TODO
    } */

    public int sizeOfMemory() {
        return registers.length;
    }

    public int getDiskSize() {
        return disk.length;
    }

    public void writeToMemory(int memAddress, int n) {
        if (memAddress >= sizeOfMemory()) {
            System.out.println("No address available (Memory Address specified is too high)");
        } else {
            registers[memAddress] = n;
        }
    }
}