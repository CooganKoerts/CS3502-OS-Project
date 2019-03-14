/*

    This class builds the registers that will be used by the operating system

 */

public class Memory {
    int[] registers;
    //TODO: simulated RAM & simulated hard drive called 'disk'

    public Memory(int numOfRegisters) {
        registers = new int[numOfRegisters];
    }

    public int[] getRegisters() {
        return registers;
    }

    public int sizeOfMemory() {
        return registers.length;
    }

    public void writeToMemory(int memAddress, int n) {
        if (memAddress >= sizeOfMemory()) {
            System.out.println("No address available (Memory Address specified is too high)");
        } else {
            registers[memAddress] = n;
        }
    }


}
