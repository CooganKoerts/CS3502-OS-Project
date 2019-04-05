/*

    The purpose for this class is to take programs from the Disk and
    load them into the memory (RAM) based off their priority #

 */

public class LongScheduler {
    private static int currentPriority = 1;

    /*
        1.) Iterate through Disk and find the first Job with the highest priority (1-A, 1 being the highest priority A
            being lowest priority)
        2.) Add program from Disk into Memory (RAM)
            a.  diskMemory[index of row] == JobID -> which can be used to pull info from PCB on the PCB queue in Loader
            b.  Add non-null elements from disk into RAM
            c.  TODO: Remove row from disk when transfer completed????
        3.) When program is loaded into Memory change status to "READY" & update the RAM address (start i.e. RAM[0][0] &
            end i.e. RAM[0][47])
            a. end address is the the last space in row that contains a non-null element
            b. might also be worthwhile to add Registers to the PCB which indicate the start of the "Data" portion of
               the JOB
     */

    /*
                Job ID: 1	Priority: 2
                Job ID: 2	Priority: 4
                Job ID: 3	Priority: 6
                Job ID: 4	Priority: 5
                Job ID: 5	Priority: 3
                Job ID: 6	Priority: 7
                Job ID: 7	Priority: 5
                Job ID: 8	Priority: 16
                Job ID: 9	Priority: 2
                Job ID: 10	Priority: 10
                Job ID: 11	Priority: 3
                Job ID: 12	Priority: 9
                Job ID: 13	Priority: 12
                Job ID: 14	Priority: 2
                Job ID: 15	Priority: 5
                Job ID: 16	Priority: 8
                Job ID: 17	Priority: 4
                Job ID: 18	Priority: 12
                Job ID: 19	Priority: 1
                Job ID: 20	Priority: 2
                Job ID: 21	Priority: 4
                Job ID: 22	Priority: 10
                Job ID: 23	Priority: 5
                Job ID: 24	Priority: 7
                Job ID: 25	Priority: 9
                Job ID: 26	Priority: 4
                Job ID: 27	Priority: 9
                Job ID: 28	Priority: 3
                Job ID: 29	Priority: 1
                Job ID: 30	Priority: 8

            Positions in RAM when done should be : 19, 29,1, 9, 14, 20, 5, 11, 28, 2, 17, 21, 26, 4, 7, 15, 23, 3, 6,
                                                   24, 16, 30, 12, 25, 27, 10, 22, 13, 18, 8
     */

    public static void sendToMemory() {
        while (currentPriority <= 16) {
            for (int i = 0; i < Driver.queueNEW.size(); i++) {
                if (Driver.hexToDec(Driver.queueNEW.get(i).priority) == currentPriority) {
                    Memory.writeToRAM(Driver.hexToDec(Driver.queueNEW.get(i).jobID));
                }
            }
            currentPriority++;
        }
    }
}
