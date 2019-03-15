import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Loader {
    LinkedList<ProcessControlBlock> queue;
    Disk disk;

    // This is here for running and testing this class' methods
    public void main(String args[])
    {
        readProgramFile();
    }

    private void readProgramFile()
    {
        String attributes = "";
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader("ProgramFile.txt"));
            String line = reader.readLine();
            while (line != null)
            {
                /*
                When line contains // check for:
                // JOB hex hex hex
                // END
                // DATA hex hex hex
                 */
                if (line.contains("//"))
                {
                    if (line.contains("JOB"))
                    {
                        attributes = line + " ";
                    }
                    else if (line.contains("Data"))
                    {
                        attributes += line;
                        addJobToPCB(attributes);
                        attributes = ""; // reset attributes
                    }
                }
                else if (!line.contains("//"))
                {
                    disk.addToDisk(line);
                }
                line = reader.readLine(); // reads next line
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    } // end of readProgramFile()

    /*
    Add Process Control block for new process to queue object
    String format: // JOB a b c // Data d e f
     */
    private void addJobToPCB(String data)
    {
        String jobID;
        String numOfWords;
        String priority_num;
        String inputBuff;
        String outputBuff;
        String tempBuff;

        String[] splitLine = data.split("\\s+");
        jobID = splitLine[2];
        numOfWords = splitLine[3];
        priority_num = splitLine[4];
        inputBuff = splitLine[7];
        outputBuff = splitLine[8];
        tempBuff = splitLine[9];

        queue.add(new ProcessControlBlock(jobID, numOfWords, priority_num, inputBuff, outputBuff, tempBuff));
    }

}
