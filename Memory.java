/*

    This class builds the registers that will be used by the operating system
    and accesses RAM.

    Note: This is the only class that can DIRECTLY access and manipulate the RAM.
          This class also acts as the "Memory Manager".


 */

public class Memory {
    // Simulated RAM, programs will be stored here based off priority
    private static String[][] RAM = new String[30][];

    /*
        Write program from disk to RAM based off the physical address of the program
        stored on the disk. memoryAddress coincidently is also the JobID of the program due to the way the Disk
        is structured.
     */
    public static void writeToRAM(int jobID) {
        int spaceRequired = 0;
        int blockToWrite = 0; // default value, will be changed

        // find how much space to allocate in RAM
        for (int i = 0; Driver.disk.getDiskMemory()[jobID-1][i] != null; i++) {
            spaceRequired++;
        }

        // find which block has not been initialized and write the program to that block
        // this is to insure we do not overwrite any already existing data
        for (int i = 0; i < RAM.length; i++) {
            if (RAM[i] == null) {
                RAM[i] = new String[spaceRequired];
                blockToWrite = i;
                break;
            }
        }

        // this loop writes to the actual RAM
        for (int i = 0; i < RAM[blockToWrite].length; i++) {
            RAM[blockToWrite][i] = Driver.disk.getDiskMemory()[jobID-1][i];
        }

        // This loop sets the registers in the programs PCB
        for (int i = 0; i < Driver.queueNEW.size(); i++) {
            if (jobID == Driver.hexToDec(Driver.queueNEW.get(i).jobID)) {
                Driver.queueNEW.get(i).registers[0] = blockToWrite;
                Driver.queueNEW.get(i).registers[1] = Driver.hexToDec(Driver.queueNEW.get(i).numOfWords);
                Driver.queueNEW.get(i).registers[2] = RAM[blockToWrite].length-1;
                Driver.queueNEW.get(i).registers[3] = Driver.hexToDec(Driver.queueNEW.get(i).numOfWords);
                Driver.queueNEW.get(i).registers[4] = Driver.queueNEW.get(i).registers[3] + Driver.hexToDec(Driver.queueNEW.get(i).inputBufferSize);
                Driver.queueNEW.get(i).registers[5] = Driver.queueNEW.get(i).registers[4] + Driver.hexToDec(Driver.queueNEW.get(i).outputBufferSize);
            }
        }
    }

    /*
        This method pulls the contents from a block in the RAM and returns that job data
     */
    public static String[] pullFromRam(int block) {
        return RAM[block];
    }

    /*
        This method is mainly used for testing
     */
    public static String[][] getRAM() {
        return RAM;
    }

    public static int getRAMSize() { return RAM.length; }
}

/*
    Order in RAM: 19, 29, 1, 9, 14, 20, 5, 11, 28, 2, 17, 21, 26, 4, 7, 15, 23, 3, 6, 24, 16, 30, 12, 25, 27, 10, 22, 13, 18, 8
*/