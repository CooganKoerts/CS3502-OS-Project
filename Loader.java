import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Loader {

    // This is here for running and testing this class' methods
    public static void main(String args[])
    {
        readProgramFile();
    }

    private static void readProgramFile()
    {

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
                        String jobID;
                        String numOfWords;
                        String priority_num;
                        String[] splitLine = line.split("\\s+");
                        jobID = splitLine[2];
                        numOfWords = splitLine[3];
                        priority_num = splitLine[4];
                    }
                }
                line = reader.readLine(); // reads next line
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    } // end of readProgramFile()

}