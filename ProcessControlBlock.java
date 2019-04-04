/*
    Queue of PCBs initialized in Loader
 */

public class ProcessControlBlock {
    String jobID; // hex number
    String numOfWords; // hex number
    String priority; // hex number
    String inputBufferSize; // hex number
    String outputBufferSize; // hex number
    String tempBufferSize; // hex number

    String status; // "RUNNING", "READY", "BLOCKED", "NEW", "COMPLETED"

    /*
        registers[ ] is an array representing the registers in the PCB that holds six values:
        registers[0] = Location of "block"/row index that the Job is located in the RAM
        registers[1] = Location of the start of the "Data" portion of the program in the RAM
        registers[2] = Location of the end of the program in RAM
        registers[3] = Location of the start of input buffer
        registers[4] = Location of the start of output buffer
        registers[5] = Location of the start of temp buffer
     */
    int[] registers = new int[6];

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
    }
}
