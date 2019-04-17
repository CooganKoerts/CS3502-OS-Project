


import java.util.ArrayList;
import java.util.LinkedList;

public class Driver {

    /*
        queueNEW is a queue of processes just created and added to the disk
        queueREADY is a queue of processes that are ready to execute
        queueWAITING is a queue of processes that are waiting
        queueSUSPENDED is a queue of processes that are suspended

        Note: One job cannot be in multiple queues, if it is added to a new queue it must be removed from its old one if
              it belonged to one.

     */
    public static LinkedList<ProcessControlBlock> queueNEW = new LinkedList<ProcessControlBlock>();
    public static LinkedList<ProcessControlBlock> queueREADY = new LinkedList<ProcessControlBlock>();
    public static LinkedList<ProcessControlBlock> queueWAITING = new LinkedList<ProcessControlBlock>();
    public static LinkedList<ProcessControlBlock> queueSUSPENDED = new LinkedList<ProcessControlBlock>();
    public static CPU cpu = new CPU(1); // will be CPU[] cpus when multiprocessor is implemented

    /*
        Disk object to simulate the hard drive that programs will be loaded into
     */
    public static Disk disk = new Disk();

    /*
    jobStats holds efficiency statistics for each completed job
     */
    public static JobStat[] jobStats;

    public static void main(String[] args) {
        System.out.println("starting OS...");
        Loader.readProgramFile();

        // Initializes a job stat object for each new job
        jobStats = new JobStat[Driver.queueNEW.size()];
        // initialize jobStat jobs
        for (int i = 0; i < jobStats.length; i++)
        {
            jobStats[i] = new JobStat();
        }

        LongScheduler.sendToMemory();
        ShortScheduler.sendToDispatcher();
        ShortScheduler.scheduleJob();
        /*
        for (int i = 0; i < queueNEW.size(); i++) {
            System.out.println("\nJobID: " + hexToDec(queueNEW.get(i).jobID) + "\tNumber of Words: " + hexToDec(queueNEW.get(i).numOfWords));
            for (int j = 0; j < Memory.getRAM()[queueNEW.get(i).registers[0]].length; j++) {
                if (j == queueNEW.get(i).registers[1]) {
                    System.out.println("DATA");
                    System.out.println(Memory.getRAM()[queueNEW.get(i).registers[0]][j]);
                } else {
                    System.out.println(Memory.getRAM()[queueNEW.get(i).registers[0]][j]);
                }
            }
        } */
    }

    private static int totalWaitTime = 0;
    private static int totalRunTime = 0;

    public static void updateJobStat(final JobStat stats) {
        jobStats[stats.getJobNumber()-1].update(stats);
        if (jobStats[stats.getJobNumber()-1].getWaitTime() > 0) {
            totalWaitTime +=jobStats[stats.getJobNumber()-1].getWaitTime();
        }
        if (jobStats[stats.getJobNumber()-1].getRunTime() > 0) {
            totalRunTime += jobStats[stats.getJobNumber()-1].getRunTime();
        }
    }

    /*
        Simple method to convert HexiDecimal values to Decimal values
     */
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

    /*
    outputs a table of all JobStat data
    most likely in text file
     */
    private void writeJobStats()
    {

    }
}
