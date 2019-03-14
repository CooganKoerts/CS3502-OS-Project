/*

    Disk class will be a disk object of an arrayList Data structures that will
    act as a virtual hard drive and store process "jobs" as strings

 */

import java.util.ArrayList;

public class Disk {

    private ArrayList<String> processes;

    public Disk() {
        processes = new ArrayList<String>();
    }

    public void addToDisk(String job) {
        processes.add(job);
    }

    public void removeFromDisk(String job) {
        processes.remove(job);
    }

    public void setProcesses(ArrayList<String> processes) {
        this.processes = processes;
    }

    public ArrayList<String> getProcesses() {
        return processes;
    }
}
