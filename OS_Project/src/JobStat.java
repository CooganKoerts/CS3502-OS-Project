public class JobStat {

    int jobNumber = -1;
    int blocksUsed = 0;
    int cpuNo = -1;
    int waitTime = 0;
    int runTime = 0;
    long timeStamp = 0;
    long startTime = 0;
    long endTime = 0;
    long startWait = 0;
    long endWait = 0;
    int i_o_operations = 0;

    public JobStat() {

    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int number) {
        this.jobNumber = number;
    }

    public int getBlocksUsed() {
        return blocksUsed;
    }

    public void setBlocksUsed(int blocks) {
        this.blocksUsed = blocks;
    }

    public int getCpuNo() {
        return cpuNo;
    }

    public void setCpuNo(int num) {
        this.cpuNo = cpuNo;
    }

    public int getWaitTime() {
        if (getEndWaitTime() == 0) {
            return 0;
        }
        return (int)(getEndWaitTime() - getStartWaitTime());
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getRunTime() {
        if (getEndRunTime() == 0) {
            return 0;
        }
        return (int)(getEndRunTime() - getStartRunTime());
    }

    public void setRunTime(int time) {
        this.runTime = time;
    }

    public long getEndWaitTime() {
        return endWait;
    }

    public void setEndWaitTime(long time) {
        this.endWait = time;
    }

    public long getStartWaitTime() {
        return startWait;
    }

    public void setStartWaitTime(long time) {
        this.startWait = time;
    }

    public long getEndRunTime() {
        return endTime;
    }

    public void setEndRunTime(long time) {
        this.endTime = time;
    }

    public long getStartRunTime() {
        return startTime;
    }

    public void setStartRunTime(long time) {
        this.startTime = time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long stamp) {
        this.timeStamp = stamp;
    }

    public int getI_o_operations() {
        return i_o_operations;
    }

    public void setI_o_operations(int i_o) {
        this.i_o_operations = i_o;
    }

    public void update(JobStat stat) {
        setBlocksUsed(stat.getBlocksUsed());
        setTimeStamp(stat.getTimeStamp());
        setCpuNo(stat.getCpuNo());
        setEndRunTime(stat.getEndRunTime());
        setEndWaitTime(stat.getEndWaitTime());
        setStartRunTime(stat.getStartRunTime());
        setStartWaitTime(stat.getStartWaitTime());
        setJobNumber(stat.getJobNumber());
        setI_o_operations(stat.getI_o_operations());
    }

}
