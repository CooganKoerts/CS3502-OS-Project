/*
    This class decides which program will be placed into the ready queue next which will then be pulled and accessed by
    the CPU.
 */
public class Dispatcher {

    /*
        Processes will originally be placed in the ready queue in the order for which they are placed in the RAM. The only
        processes that will be sent to the READY queue are those that are marked READY.
     */
    public static void sendToReadyQueue(ProcessControlBlock pcb) {
        if (pcb.status == "READY") {
            Driver.queueREADY.add(pcb);
            Driver.queueNEW.remove(pcb);
        }
    }

    public static void dispatchProcesstoCPU(ProcessControlBlock job)
    {
        Driver.cpu.setCache(job);
        System.out.println("CPU RUN");
        job.status = "RUNNING";
        int id = Helpers.hex_to_decimal(job.jobID);
        PCBList.getPCB(id-1).status = "RUNNING";

        Driver.jobStats[id-1].setTimeStamp(System.currentTimeMillis());
        Driver.jobStats[id-1].setJobNumber(id);
        Driver.jobStats[id-1].setEndWaitTime(System.currentTimeMillis());
        Driver.jobStats[id-1].setCpuNo(1);
        Driver.updateJobStat(Driver.jobStats[id-1]);

        Driver.cpu.run();
    }

    /*
        Sends processes that are marked "SUSPENDED" to the suspended queue
     */
    public static void sendToSuspendedQueue(ProcessControlBlock pcb) {
        //TODO
    }

    /*
        Sends processes that are marked "WAITING" to the waiting queue
     */
    public static void sendToWaitingQueue(ProcessControlBlock pcb) {
        //TODO
    }

    /*
        when a process is interrupted and/or a new process will begin execution contextSwitch will save the state of the
        current process and load the new process into execution and retrieve the previous process when it is time for it
        to begin execution again.
        Note: when a process re-enters the ready queue it will need to enter back into the queue based off its priority
    */
    public static void contextSwitch(ProcessControlBlock pcbOLD, ProcessControlBlock pcbNEW) {
        //TODO
        pcbOLD.status = "BLOCKED";
        dispatchProcesstoCPU(pcbNEW);
    }
}