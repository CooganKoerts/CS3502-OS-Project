/*
    Disk class will be a disk object of a 2d array Data structures that will
    act as a virtual hard drive and store a job's "words" as strings in each "block" .

    We disregarded the jobID, number of words, priority & Data buffers since those
    will have all been added to a queue of PCBs with one unique PCB object for each Job.

    The Jobs are loaded into the disk on a first come first serve basis and will be
    scheduled by priority by the scheduler.
 */

public class Disk {

    // The Disk object contains a 2d String array that is 32 x 256
    // Each row is symbolic of a memory block
    // There is supposed to be (2048 words * 4 bytes per word) 8192 bytes in size
    // Each memory block (row) has an evenly split up amount of bytes (256).
    private static String[][] diskMemory = new String[32][256];

    public void addToDisk(int jobID, int index, String words) {
        diskMemory[jobID-1][index] = words;
    }

    public void removeFromDisk(int jobID) {
        for (int i = 0; i < diskMemory[jobID].length; i++){
            diskMemory[jobID][i] = null;
        }
    }

    public int diskSize() { return diskMemory.length; }

    public void setDiskMemory(String[][] diskMemory) {
        this.diskMemory = diskMemory;
    }

    public String[][] getDiskMemory() { return diskMemory; }
}
