import java.util.Random;

public class Process {

    private int processId = 0;
    private String processName = "";
    private int processSize = 0;// generate random size in kb
    private int processPriority = 0; // generate random priority (1-3, 1 being highest priority 3 being lowest)

    public Process(int processId, String processName) {
        this.processId = processId;
        this.processName = processName;
        this.processSize = (int)(Math.random() * 100 + 1);
        this.processPriority = (int)(Math.random() * 3 + 1);
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
