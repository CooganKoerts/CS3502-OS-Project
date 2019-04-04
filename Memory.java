/*

    This class builds the registers that will be used by the operating system
    and accesses RAM.

    Note: This is the only class that can DIRECTLY access and manipulate the RAM.
          This class also acts as the "Memory Manager".


 */

public class Memory {
    // Simulated RAM, programs will be stored here based off priority
    private static String[][] RAM = new String[30][];

    // Main Method used for testing
    /*
    public static void main(String[] args) {
        Loader.readProgramFile();
        LongScheduler.sendToMemory();

        // Tests are to ensure that data is properly transferred from the
        for (int i = 0; i < Loader.queue.size(); i++) {
            System.out.println("\nJobID: "+ hexToDec(Loader.queue.get(i).jobID) + "\tNumber of Words: " + hexToDec(Loader.queue.get(i).numOfWords));
            //System.out.println("JobID: "+ hexToDec(Loader.queue.get(i).jobID) + "\tFirst word in RAM: " + RAM[Loader.queue.get(i).registers[0]][0]);
            for (int j = 0; j < RAM[Loader.queue.get(i).registers[0]].length; j++) {
                if (j == Loader.queue.get(i).registers[1]) {
                    System.out.println("DATA");
                    System.out.println(RAM[Loader.queue.get(i).registers[0]][j]);
                } else {
                    System.out.println(RAM[Loader.queue.get(i).registers[0]][j]);
                }
            }
        }
    }
    */

    // Write program from disk to RAM based off the physical address of the program
    // stored on the disk. memoryAddress coincidently is also the JobID of the program due to the way the Disk
    // is structured.
    public static void writeToRAM(int jobID) {
        int spaceRequired = 0;
        int blockToWrite = 0; // default value, will be changed

        // find how much space to allocate in RAM
        for (int i = 0; Loader.disk.getDiskMemory()[jobID-1][i] != null; i++) {
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
            RAM[blockToWrite][i] = Loader.disk.getDiskMemory()[jobID-1][i];
        }

        // This loop sets the registers in the programs PCB
        for (int i = 0; i < Loader.queue.size(); i++) {
            if (jobID == hexToDec(Loader.queue.get(i).jobID)) {
                Loader.queue.get(i).registers[0] = blockToWrite;
                Loader.queue.get(i).registers[1] = hexToDec(Loader.queue.get(i).numOfWords);
                Loader.queue.get(i).registers[2] = RAM[blockToWrite].length-1;
                Loader.queue.get(i).registers[3] = hexToDec(Loader.queue.get(i).numOfWords);
                Loader.queue.get(i).registers[4] = Loader.queue.get(i).registers[3] + hexToDec(Loader.queue.get(i).inputBufferSize);
                Loader.queue.get(i).registers[5] = Loader.queue.get(i).registers[4] + hexToDec(Loader.queue.get(i).outputBufferSize);
            }
        }
    }

    // Simple method to convert HexiDecimal values to Decimal values
    public static int hexToDec(String hexStr) {
        String hexValues = "0123456789ABCDEF";
        hexStr = hexStr.toUpperCase();
        int hexValue = 0;

        for (int i = 0; i < hexStr.length(); i++) {
            char j = hexStr.charAt(i);
            int k = hexValues.indexOf(j);
            hexValue = 16 * hexValue + k;
        }

        return hexValue;
    }
}

/*  Order in RAM: 19, 29, 1, 9, 14, 20, 5, 11, 28, 2, 17, 21, 26, 4, 7, 15, 23, 3, 6, 24, 16, 30, 12, 25, 27, 10, 22, 13, 18, 8
*/