/*

    This class builds the registers that will be used by the operating system
    and accesses RAM.

    Note: This is the only class that can DIRECTLY access and manipulate the RAM.

 */

public class Memory {
    // Simulated RAM, programs will be stored here based off priority
    private static String[][] RAM = new String[32][];

    // Write program from disk to RAM based off the physical address of the program
    // stored on the disk.

    /*public static void writeToRAM(int address, int priority) {
        RAM[0][0] = Loader.disk.getDiskMemory()[address][0];
    } */
}