import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// Loader
public class Loader {
    public static void main(String args[])
    {
        readProgramFile();
    }

    public void readProgramFile()
    {
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader(CS3502-OS-Project/ProgramFile.txt));

        }
    } // end of readProgramFile()

}
