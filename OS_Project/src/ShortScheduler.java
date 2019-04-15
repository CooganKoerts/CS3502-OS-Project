/*
    The goal of this class is to decide which programs are ready to head to the dispatcher.
 */

/*
    The Dispatcher method assigns a process to the CPU. It is also responsible for context switching of
    jobs when necessary (more on this later!). For now, the dispatcher will extract parameter data from the
    PCB and accordingly set the CPU’s PC, and other registers, before the OS calls the CPU to execute
    the job
 */
public class ShortScheduler {

    /*
        Will send a PCB object to dispatch
     */
    public static void sendToDispatcher() {

        /*
            1.) if the SUSPENDED and WAITING queues are empty then send the PCB to dispatch.
                a. This means send PCBs in order that they are in the RAM.
        */

        if (Driver.queueSUSPENDED.size() == 0 & Driver.queueWAITING.size() == 0) {
            for (int i = 0; i < Memory.getRAMSize(); i++) {
                for (int j = 0; j < Driver.queueNEW.size(); j++) {
                    // 'i' is the current index of the RAM memory block and registers[0] is the record of which
                    // memory block a process is stored in. If registers[0] == i, set the process as READY and
                    // send to the dispatcher.
                    if (Driver.queueNEW.get(j).registers[0] == i) {
                        Driver.queueNEW.get(j).status = "READY";
                        Dispatcher.sendToReadyQueue(Driver.queueNEW.get(j));
                    }
                }
            }
        }
    }

    public static void scheduleJob()
    {
        if (Driver.queueREADY.size() > 0)
        {
            for (int i = 0; i < Driver.queueREADY.size(); i++)
            {
                Dispatcher.dispatchProcesstoCPU(Driver.queueREADY.get(i)); // sends one process to be dispatched
            }
        }
    }

}
