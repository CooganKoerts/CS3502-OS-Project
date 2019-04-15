/*
    This is our CPU file
 */

public class CPU implements Runnable {

    private long threadID;

    // CPU PCB
    private int cpuNumber;
    public int programCounter; // holds address of instruction to fetch
    public int instructCounter; // holds number of instructions
    Integer priority; // of the process, extracted from JOB control line

    // struct state; // record of environment that is saved on interrupt
    // int codeSize; // extracted from the JOB control line

    /*
    Reg-0 (0000) being the Accumulator.
    Reg-1(0001) being the Zero register, which contains the value 0.
    All other registers are general purpose register.
     */
    public int[] registers = new int[16];


    // struct sched; // burst time, priority, queue type, time slice, remain type
    // struct accounts; // cpu time, time limit, time delays, start/end times, io times
    // struct memories; // page table base, pages, page size, b regs
    // struct progeny; // child procid, child code pointers
    // parent: ptr; // pointer to parent
    // struct resources; // file pointers, io devices - unit class, unit #, open file tables
    // string status; // running, ready, blocked, new
    // status_info; // pointer to ready list of active processes or blocked processes

    public String[] cache;
    public int tempBuffer;
    public int inputBuffer;
    public int outputBuffer;

    // Decode Method Variables
    public int instant;
    public String operationCode; // hex string
    public int temporary_reg_1;
    public int temporary_reg_2;
    public int temporary_Dst_reg;
    public int temporary_breg;
    public int temporary_sreg1;
    public int temporary_sreg2;
    public int temporary_address;

    /*
     Addressing mode is a parameter for effective address computation
     */
    enum AddressingMode
    {
        Direct, Indirect;
    }

    public CPU(int cpuNumber)
    {
        this.cpuNumber = cpuNumber;
        registers[1]=0;
    }

    public void run()
    {
        // increments through
        threadID = Thread.currentThread().getId();
        // loops through instructions for job stored in cache
        while (programCounter < instructCounter)
        {
            String instruction = fetch(programCounter);
            int opCode = Decode(instruction);
            Execute(opCode);
            programCounter++;
        }
    }

    /*
    fetch() returns a hex string of one (of the many) instructions for a job
    @param programCounter: The CPU Program counter. assuming it is the instruction num
                            to be called (for a job)
     */
    public String fetch(int programCounter){
        String instruction = cache[programCounter];
        return instruction;
    }

    /*
    @param: instruction is the original hex instruction passed in to the loader
    Returns int representative of instruction type: either 00, 01, 10, or 11
    May need to add binary to hex conversion so OPcode can be matched
    with those found in InstructionSet under project specs on d2l
    */
    public Integer Decode(String instruction) {

        // get binary conversion of last 30 bits of instruction
        String binary_instructions = Helpers.hex_to_binary(instruction);
        String temporary_instructions = binary_instructions.substring(2);

        // first 2 bits
        instant = Integer.parseInt(binary_instructions.substring(0, 2));

        switch (instant) {
            case 00: { // Arithmetic
                operationCode = Helpers.binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_sreg1 = Helpers.binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_sreg2 = Helpers.binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_Dst_reg = Helpers.binary_to_decimal(temporary_instructions.substring(14, 18));
                break;
            }

            case 01: { // Conditional branch and Immediate format
                operationCode = Helpers.binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_breg = Helpers.binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_Dst_reg = Helpers.binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_address = Helpers.binary_to_decimal(temporary_instructions.substring(14));
                break;
            }

            case 10: { // Unconditional jump
                operationCode = Helpers.binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_address = Helpers.binary_to_decimal(temporary_instructions.substring(6));
                break;
            }

            case 11: { //IO operation
                operationCode = Helpers.binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_reg_1 = Helpers.binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_reg_2 = Helpers.binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_address = Helpers.binary_to_decimal(temporary_instructions.substring(14));
                break;
            }

            default: {
                System.out.println("EXCEPTION: Invalid instruction type");
            }
        }
        return instant;
    }


    /*
    * @param: Integer instruction from decode method
    * Reg-0 is the accumulator
    * Reg-1 is the zero register
    * */
    public void Execute(Integer inst){
        int operationCode = inst;

        switch (operationCode)
        {
            // Inst: RD Type: I/0
            case 0:
            {
                if(temporary_reg_2 > 0)
                {
                    registers[temporary_reg_1] = Helpers.hex_to_decimal(cache[registers[temporary_reg_2]/4].substring(2));
                }
                else
                {
                    registers[temporary_reg_1] = Helpers.hex_to_decimal(cache[temporary_address/4].substring(2));
                }
                break;
            }
            case 1: //1 WR
            {
                //IO write
                if(temporary_reg_2 > 0)
                {
                    registers[temporary_reg_2] =registers[temporary_reg_1];
                }
                else
                {
                    cache[temporary_address/4] = "0x" + Helpers.decimal_to_hex(registers[temporary_reg_1]);
                    //dma write with temporary_address
                }



                break;
            }
            case 2: //2 ST
            {
                cache[registers[temporary_Dst_reg]/4] = "0x" + Helpers.decimal_to_hex( registers[temporary_breg]);
                break;
            }
            case 3: //LW
            {
                registers[temporary_Dst_reg]= Helpers.hex_to_decimal(cache[(registers[temporary_breg]/4) + temporary_address].substring(2));

                break;
            }
            case 4: //MOV
            {
                registers[temporary_Dst_reg]=registers[temporary_breg];
                break;
            }
            case 5: //ADD
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1];
                registers[temporary_Dst_reg]+=registers[temporary_sreg2];

                break;
            }
            case 6: //SUB
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1];
                registers[temporary_Dst_reg]=registers[temporary_Dst_reg]-registers[temporary_sreg2];

                break;
            }
            case 7: //MUL
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1]*registers[temporary_sreg2];

                break;
            }
            case 8: //DIV
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1]/registers[temporary_sreg2];

                break;
            }
            case 9: //AND
            {
                registers[temporary_Dst_reg]= registers[temporary_sreg1]&registers[temporary_sreg2];
                break;
            }
            case 10:    //0A OR
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1]^registers[temporary_sreg2];

                break;
            }
            case 11:    //0B MOVI
            {
                registers[temporary_Dst_reg]= temporary_address;
                break;
            }
            case 12:    //0C ADDI
            {
                registers[temporary_Dst_reg]+=temporary_address;
                break;
            }
            case 13:    //0D MULI
            {
                registers[temporary_Dst_reg]*=temporary_address;
                break;
            }
            case 14:    //0E DIVI
            {
                registers[temporary_Dst_reg]/=temporary_address;
                break;
            }
            case 15:    //0F LDI
            {

                registers[temporary_Dst_reg] = (temporary_address);
                break;
            }
            case 16:    //10 SLT
            {
                if(registers[temporary_sreg1] < registers[temporary_sreg2]){
                    registers[temporary_Dst_reg] = 1;
                }
                else{
                    registers[temporary_Dst_reg] = 0;
                }
                break;
            }
            case 17:    //11 SLTI
            {
                if(registers[temporary_sreg1] < (temporary_address/4)){
                    registers[temporary_Dst_reg] = 1;
                }
                else{
                    registers[temporary_Dst_reg] = 0;
                }
                break;
            }
            case 18:    //12 HLT
            {
                programCounter = Memory.getRAMSize();
                break;
            }
            case 19:    //13 NOP
            {
                //does nothing
                break;
            }
            case 20:    //14 JMP
            {
                programCounter = temporary_address/4;
               // jump = true;
                break;
            }
            case 21:    //15 BEQ
            {
                if(registers[temporary_breg] == registers[temporary_Dst_reg]){
                    programCounter = temporary_address/4;
                    //jump = true;
                }
                else{

                }
                break;
            }
            case 22: //16 BNE
            {
                if(registers[temporary_breg] != registers[temporary_Dst_reg]){
                    //branch
                    programCounter = temporary_address/4;
                    //jump = true;

                }
                else{

                }
                break;
            }
            case 23: //17 BEZ
            {
                if(registers[temporary_breg] == 0){
                    //branch
                    programCounter = temporary_address/4;
                    //jump = true;
                }
                else{
                }
                break;
            }
            case 24: //18 BNZ
            {
                if(registers[temporary_breg] != 0){
                    //branch
                    programCounter = temporary_address/4;
                    //jump = true;
                }
                else{
                }
                break;
            }
            case 25:    //19 BGZ
            {
                if(registers[temporary_breg] > 0){
                    //branch
                    programCounter = temporary_address/4;
                    //jump = true;
                }
                else{
                }
                break;
            }
            case 26:    //1A BLZ
            {
                if (registers[temporary_breg] < 0) {
                    //branch
                    programCounter = temporary_address / 4;
                   // jump = true;
                }
                else {
                }
                break;
            }
            default:
            {
                System.out.println("System error: invalid operation");
            }
        }
    }

    /*
        Param index: takes Index of Job to be loaded in READY queue

        "loads" all of the instructions into the cache
     */
    public void setCache(ProcessControlBlock job) {
        cache = Memory.pullFromRam(job.registers[0]); // this does same as previous line (below) ?
        //cache = Memory.pullFromRam(Driver.queueREADY.get(index).registers[0]);
        instructCounter = job.registers[2]; // gets num of instruction words for job
        programCounter = 0;
    }
    
    /*
    Param AddressingMode a: Direct or Indirect
    Param int base: a job's location in the READY queue
     */
    public int effective_Addr(AddressingMode a, int base) {
        // if "direct" then it is referring to a job location in the RAM
        // if "indirect" then it is referring to a job's location on the disk (?)
        if (a.equals(AddressingMode.Direct)) {
            return Driver.queueREADY.get(base).registers[0];
        } else {
            // jobID-1 is also coincidentally the jobs position on the disk since everything is loaded in order origianlly.
            return Driver.hexToDec(Driver.queueREADY.get(base).jobID) - 1 ;
        }
    }

}
