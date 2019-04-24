
/*
    Queue of PCBs initialized in Loader
 */

import java.util.LinkedList;

public class ProcessControlBlock {
    String jobID; // hex number
    String numOfWords; // hex number
    String priority; // hex number
    String inputBufferSize; // hex number
    String outputBufferSize; // hex number
    String tempBufferSize; // hex number
    int programCounter;
    int jobSize;

    String status; // "RUNNING", "READY", "BLOCKED", "NEW", "COMPLETED"

    String[] wordList;

    /*
        registers[ ] is an array representing the registers in the PCB that holds six values:
        registers[0] = Location of "block"/row index that the Job is located in the RAM. This acts as the Base-Register
        registers[1] = Location of the start of the "Data" portion of the program in the RAM
        registers[2] = Location of the end of the program in RAM
        registers[3] = Location of the start of input buffer
        registers[4] = Location of the start of output buffer
        registers[5] = Location of the start of temp buffer
     */
    int[] registers = new int[6];

    /*
        This 2D String array represents the virtual pages that all of the contents of the Job will be stored in. Each
        row will represent a "page" and each page can only contain 4 words, thus the row length will be of size 4. Made
        20 rows for simplicity.
    */
    String[][] virtualPages = new String[20][4];
    int[] startLocationInDiskPages = new int[2];

    public ProcessControlBlock(String job_id, String num_of_words, String _priority, String input_buffer_size,
                               String output_buffer_size, String temp_buffer_size)
    {
        this.jobID = job_id;
        this.numOfWords = num_of_words;
        this.priority = _priority;
        this.inputBufferSize = input_buffer_size;
        this.outputBufferSize = output_buffer_size;
        this.tempBufferSize = temp_buffer_size;
        this.status = "NEW";

        jobSize = Driver.hexToDec(numOfWords) + Driver.hexToDec(inputBufferSize) + Driver.hexToDec(outputBufferSize) +
                  Driver.hexToDec(tempBufferSize);

        wordList = new String[jobSize];
        loadWordList();
        loadVirtualPages();
    }

    private void loadWordList() {
        wordList = Driver.disk.getDiskMemory()[Driver.hexToDec(jobID)-1];
    }

    private void loadVirtualPages() {
        int counter = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 4; j++) {
                virtualPages[i][j] = wordList[counter];
                counter++;
                if (counter == jobSize) {
                    break;
                }
            }
            if (counter == jobSize) {
                break;
            }
        }
    }
}