/*

    This class builds the registers that will be used by the operating system
    and accesses RAM and the hard-drive disk

    This program will be used to load processes into the hard-drive disk simulator

 */

public class Memory {
    private int[] registers = new int[16];
    private static Disk disk = new Disk();
    private static Object[] memoryAddresses;
    //Ram ram; //
    //TODO: simulated RAM


    //Memory takes two parameters n & k for the number of registers and the number of memory addresses, respectively
    public Memory(int n, int k) {
        registers = new int[n];
        memoryAddresses = new Object[k];

    }

    public int[] getRegisters() {
        return registers;
    }

    public Disk getDisk() {
        return disk;
    }

    public void setRegisters(int numOfRegisters) {
        registers = new int[numOfRegisters];
    }

    public int sizeOfMemory() {
        return registers.length;
    }

    public int getDiskSize() {
        return disk.diskSize();
    }

    public void writeToMemory(int memAddress, int n) {
        if (memAddress >= sizeOfMemory()) {
            System.out.println("No address available (Memory Address specified is too high)");
        } else {
            registers[memAddress] = n;
        }
    }
}
