/*

    Disk class will be a disk object of an arrayList Data structures that will
    act as a virtual hard drive and store process "jobs" as strings

    process "jobs" will be stored on an arrayList of String objects and will be retrieved when it is time for the process to execute
 */

import java.util.ArrayList;

public class Disk {

    private static ArrayList<String> processes;

    public Disk() {
        processes = new ArrayList<String>();
    }

    public void addToDisk(String job) {
        processes.add(job);
    }

    public void removeFromDisk(String job) {
        processes.remove(job);
    }

    public String accessDisk(String job) {
        int indexOfJob = processes.indexOf(job);
        String jobToBePulled = processes.get(indexOfJob);
        processes.remove(indexOfJob);
        return jobToBePulled;
    }

    public int diskSize() {
        return processes.size();
    }

    public void setProcesses(ArrayList<String> processes) {
        this.processes = processes;
    }

    public ArrayList<String> getProcesses() {
        return processes;
    }
}
