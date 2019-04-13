package OS_Project.src;/*
    The Loader Class is responsible for loading programs onto the disk.
    Programs that are to be loaded onto the disk are those in programFile.txt.

    Some additional methods are present at the bottom to help assisting other
    methods and processes.

 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Loader {

    public static void readProgramFile() {

        String attributes = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("OS_Project/ProgramFile.txt"));
            String line;
            int jobID = 0, k = 0;
            while ((line = reader.readLine()) != null) {

                /*
                When line contains // check for:
                // JOB hex hex hex
                instruct
                // DATA hex hex hex
                input data
                // END
                 */

                if (line.contains("//")) {
                    if (line.contains("JOB")) {
                        attributes = line + " ";
                        jobID = getJobID(line);
                        k = 0;
                    } else if (line.contains("Data") && !attributes.equals("")) {
                        attributes += line;
                        // Adds String
                        addJobToPCB(attributes);
                    }
                } else {
                    Driver.disk.addToDisk(jobID, k, line);
                    k++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    } // end of readProgramFile()

    /*
    Add Process Control block for new process to queue object
    String format: // JOB a b c // Data d e f
     */
    public static void addJobToPCB(String data) {
        String jobID;           // a
        String numOfWords;      // b
        String priority_num;    // c
        String inputBuff;       // d
        String outputBuff;      // e
        String tempBuff;        // f

        String[] splitLine = data.split("\\s+");

        jobID = splitLine[2];
        numOfWords = splitLine[3];
        priority_num = splitLine[4];
        inputBuff = splitLine[7];
        outputBuff = splitLine[8];
        tempBuff = splitLine[9];

        Driver.queueNEW.add(new ProcessControlBlock(jobID, numOfWords, priority_num, inputBuff, outputBuff, tempBuff));
    }

    // Method for retrieving the JobID to help with
    // loading the programs onto the disk.
    public static int getJobID(String data) {
        String[] splitLine = data.split("\\s+");
        return Driver.hexToDec(splitLine[2]);
    }
}
