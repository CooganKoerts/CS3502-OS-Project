/*
    The Loader Class is responsible for loading programs onto the disk.
    Programs that are to be loaded onto the disk are those in programFile.txt.

    Some additional methods are present at the bottom to help assisting other
    methods and processes.

 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class Loader {
    static LinkedList<ProcessControlBlock> queue = new LinkedList<ProcessControlBlock>();
    static Disk disk = new Disk();

    /*  public static void main(String[] args) {
            readProgramFile();
            for (int i = 0; disk.getDiskMemory()[i][0] != null; i++) {
                System.out.println("\n\nJob #" + ((int)i+1));
                for (int j = 0; disk.getDiskMemory()[i][j] != null; j++) {
                    System.out.println(disk.getDiskMemory()[i][j]);
                }
            }
        } */

    public static void readProgramFile() {

        String attributes = "";
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("ProgramFile.txt"));
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
                    disk.addToDisk(jobID, k, line);
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

        queue.add(new ProcessControlBlock(jobID, numOfWords, priority_num, inputBuff, outputBuff, tempBuff));
    }

    // Method for retrieving the JobID to help with
    // loading the programs onto the disk.
    public static int getJobID(String data) {
        String[] splitLine = data.split("\\s+");
        return hexToDec(splitLine[2]);
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
