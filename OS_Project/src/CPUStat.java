public class CPUStat {

    int cpuNumber = 0;
    int currentJobNumber = 0;
    int programCounter = 0;
    int totalInstructionNumber = 0;
    public enum CPU_STATE {IDLE, RUNNING, LOADING};
    public CPU_STATE currentState = CPU_STATE.IDLE;
    String currentInstruction = "None";

    public CPUStat(int cpuNum) {
        cpuNumber = cpuNum;
    }

    public void update(int job, int count, int totalInstr) {
        setCurrentJobNumber(job);
        setProgramCounter(count);
        setTotalInstructionNumber(totalInstr);
    }

    public void update(CPUStat stat) {
        setCurrentJobNumber(stat.currentJobNumber);
        setProgramCounter(stat.programCounter);
        setTotalInstructionNumber(stat.totalInstructionNumber);
        setCurrentInstruction(stat.currentInstruction);
        setCurrentState(stat.currentState);
        Driver.writeCpuStats();
    }

    public void setCpuNumber(int num) {
        this.cpuNumber = num;
    }

    public void setCurrentJobNumber(int num) {
        this.currentJobNumber = num;
    }

    public void setProgramCounter(int counter) {
        this.programCounter = counter;
    }

    public void setTotalInstructionNumber(int num) {
        this.totalInstructionNumber = num;
    }

    public void setCurrentInstruction(String instruct) {
        this.currentInstruction = instruct;
    }

    public int getCurrentJobNumber() {
        return currentJobNumber;
    }

    public int getTotalInstructionNumber() {
        return totalInstructionNumber;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setCurrentState(CPU_STATE state) {
        this.currentState = state;
    }

    public String getCurrentState() {
        switch (currentState)
        {
            case LOADING:
            {
                return  "Loading";
            }
            case RUNNING:
            {
                return "Running";
            }
            case IDLE: {
                return "Idle";
            }

        }
        return null;
    }
}
