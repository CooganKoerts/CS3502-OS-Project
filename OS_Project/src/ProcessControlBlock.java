public class ProcessControlBlock {
    String jobID; // hex number
    String numOfWords; // hex number
    String priority; // hex number
    String inputBufferSize; // hex number
    String outputBufferSize; // hex number
    String tempBufferSize; // hex number

    public ProcessControlBlock(String job_id, String num_of_words, String _priority, String input_buffer_size,
                               String output_buffer_size, String temp_buffer_size)
    {
        this.jobID = job_id;
        this.numOfWords = num_of_words;
        this.priority = _priority;
        this.inputBufferSize = input_buffer_size;
        this.outputBufferSize = output_buffer_size;
        this.tempBufferSize = temp_buffer_size;
    }
}