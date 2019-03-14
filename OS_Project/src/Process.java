import java.util.Random;

public class Process {

    private int processId;
    private String processName;
    private String processJob;
    private int processSize;// generate random size in kb
    private int processPriority; // generate random priority (1-3, 1 being highest priority 3 being lowest)

    public Process(int processId, String processName, String processJob ,int processSize, int processPriority) {
        this.processId = processId;
        this.processName = processName;
        this.processJob = processJob;
        this.processSize = processSize;
        this.processPriority = processPriority;
    }

    public int getProcessId() {
        return processId;
    }

    public String getProcessName() {
        return processName;
    }

    public int getProcessPriority() {
        return processPriority;
    }

    public int getProcessSize() {
        return processSize;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setProcessPriority(int processPriority) {
        this.processPriority = processPriority;
    }

    public void setProcessSize(int processSize) {
        this.processSize = processSize;
    }
}