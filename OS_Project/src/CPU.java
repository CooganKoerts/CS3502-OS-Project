/*
    This is our CPU file
 */

public class CPU implements Runnable {

    private long threadID;

    // CPU PCB
    private int cpuNumber;
    public int programCounter; // holds address of instruction to fetch
    public int instructCounter; // holds number of instructions
    public int currentJobNum;
    Integer priority; // of the process, extracted from JOB control line

    /*
    Reg-0 (0000) being the Accumulator.
    Reg-1(0001) being the Zero register, which contains the value 0.
    All other registers are general purpose register.
     */
    public int[] registers = new int[16];

    public String[] cache;
    public int tempBuffer;
    public int inputBuffer;
    public int outputBuffer;

    // Decode Method Variables
    public int instant;
    public int operationCode; // hex string
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
        threadID = Thread.currentThread().getId();
        System.out.println("Running Job " + currentJobNum + " on thread: " + threadID);
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
        System.out.println("Fetching instruction " + programCounter + " from job " + currentJobNum);
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

        System.out.println("Decoding instruction " + instruction + " for job " + currentJobNum);
        // get binary conversion of last 30 bits of instruction
        String binary_instructions = Helpers.hex_to_binary(instruction);
        String temporary_instructions = binary_instructions.substring(2);

        // first 2 bits
        instant = Integer.parseInt(binary_instructions.substring(0, 2));

        switch (instant) {
            case 00: { // Arithmetic
                operationCode = Helpers.binary_to_decimal(temporary_instructions.substring(0, 6));
                temporary_sreg1 = Helpers.binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_sreg2 = Helpers.binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_Dst_reg = Helpers.binary_to_decimal(temporary_instructions.substring(14, 18));
                break;
            }

            case 01: { // Conditional branch and Immediate format
                operationCode = Helpers.binary_to_decimal(temporary_instructions.substring(0, 6));
                temporary_breg = Helpers.binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_Dst_reg = Helpers.binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_address = Helpers.binary_to_decimal(temporary_instructions.substring(14));
                break;
            }

            case 10: { // Unconditional jump
                operationCode = Helpers.binary_to_decimal(temporary_instructions.substring(0, 6));
                temporary_address = Helpers.binary_to_decimal(temporary_instructions.substring(6));
                break;
            }

            case 11: { //IO operation
                operationCode = Helpers.binary_to_decimal(temporary_instructions.substring(0, 6));
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

        System.out.println("Executing JOB: " + currentJobNum + ", INSTRUCTION: " + programCounter);

        switch (operationCode)
        {
            // Inst: RD Type: I/0
            case 0:
            {
                if(temporary_reg_2 > 0)
                {
                    registers[temporary_reg_1] = Helpers.hex_to_decimal(cache[registers[temporary_reg_2]/4].substring(2));
                    System.out.println(String.format("RD - Reading val:%s from R%s into R%s",
                            registers[temporary_reg_1], temporary_reg_2, temporary_reg_1));
                }
                else
                {
                    registers[temporary_reg_1] = Helpers.hex_to_decimal(cache[temporary_address/4].substring(2));
                    System.out.println(String.format("RD - Reading from cache[%s] val:%s into R%s", temporary_address/4,
                            Helpers.hex_to_decimal(cache[temporary_address/4].substring(2)), temporary_reg_1));
                }
                break;
            }
            case 1: //1 WR
            {
                //IO write
                if(temporary_reg_2 > 0)
                {
                    registers[temporary_reg_2] =registers[temporary_reg_1];
                    System.out.println(String.format("WR - Writing from R%s val:%s to R%s val: %s", temporary_reg_1,
                            registers[temporary_reg_1], temporary_reg_2, registers[temporary_reg_2]));
                }
                else
                {
                    cache[temporary_address/4] = "0x" + Helpers.decimal_to_hex(registers[temporary_reg_1]);
                    System.out.println(String.format("WR - Writing from R%s val: %s to cache[%s] val:%s", temporary_reg_1,
                            registers[temporary_reg_1], temporary_address/4, cache[temporary_address/4]));
                    //dma write with temporary_address
                }
                break;
            }
            case 2: //2 ST
            {
                cache[registers[temporary_Dst_reg]/4] = "0x" + Helpers.decimal_to_hex( registers[temporary_breg]);
                System.out.println(String.format("ST - Storing from R%s val:%s into cache[%s] val:%s", temporary_breg,
                        registers[temporary_breg], registers[temporary_Dst_reg]/4,
                        cache[registers[temporary_Dst_reg]/4]));
                break;
            }
            case 3: //LW
            {
                registers[temporary_Dst_reg]= Helpers.hex_to_decimal(cache[(registers[temporary_breg]/4) + temporary_address].substring(2));
                System.out.println(String.format("LW - loading from cache[%s] val:%s into R%s val:%s",
                        (registers[temporary_breg]/4) + temporary_address, cache[(registers[temporary_breg]/4) + temporary_address].substring(2),
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 4: //MOV
            {
                registers[temporary_Dst_reg]=registers[temporary_breg];
                System.out.println(String.format("MOV - Move from R%s val:%s R%s", temporary_breg, registers[temporary_breg], temporary_Dst_reg));
                break;
            }
            case 5: //ADD
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1];
                registers[temporary_Dst_reg]+=registers[temporary_sreg2];
                System.out.println(String.format("ADD - add from R%s val:%s with R%s val:%s to R%s val%s",
                        temporary_sreg1, registers[temporary_sreg1], temporary_sreg2, registers[temporary_sreg2],
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 6: //SUB
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1];
                registers[temporary_Dst_reg]=registers[temporary_Dst_reg]-registers[temporary_sreg2];
                System.out.println(String.format("SUB - subtracts from R%s val:%s with R%s val:%s to R%s val%s",
                        temporary_sreg1, registers[temporary_sreg1], temporary_sreg2, registers[temporary_sreg2],
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 7: //MUL
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1]*registers[temporary_sreg2];
                System.out.println(String.format("MUL - multiplies from R%s val:%s with R%s val:%s to R%s val%s",
                        temporary_sreg1, registers[temporary_sreg1], temporary_sreg2, registers[temporary_sreg2],
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 8: //DIV
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1]/registers[temporary_sreg2];
                System.out.println(String.format("DIV - divides from R%s val:%s with R%s val:%s to R%s val%s",
                        temporary_sreg1, registers[temporary_sreg1], temporary_sreg2, registers[temporary_sreg2],
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 9: //AND
            {
                registers[temporary_Dst_reg]= registers[temporary_sreg1]&registers[temporary_sreg2];
                System.out.println(String.format("AND - logical AND of R%s val:%s and R%s val:%s to R%s val%s",
                        temporary_sreg1, registers[temporary_sreg1], temporary_sreg2, registers[temporary_sreg2],
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 10:    //0A OR
            {
                registers[temporary_Dst_reg]=registers[temporary_sreg1]^registers[temporary_sreg2];
                System.out.println(String.format("OR - logical OR of R%s val:%s and R%s val:%s to R%s val%s",
                        temporary_sreg1, registers[temporary_sreg1], temporary_sreg2, registers[temporary_sreg2],
                        temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 11:    //0B MOVI
            {
                registers[temporary_Dst_reg]= temporary_address;
                System.out.println(String.format("MOVI - transfers val:%s to R%s",
                        temporary_address, temporary_Dst_reg));
                break;
            }
            case 12:    //0C ADDI
            {
                registers[temporary_Dst_reg]+=temporary_address;
                System.out.println(String.format("ADDI - adds val:%s with R%s final val:%s",
                        temporary_address, temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 13:    //0D MULI
            {
                registers[temporary_Dst_reg]*=temporary_address;
                System.out.println(String.format("MULI - multiplies val:%s with R%s final val%s",
                        temporary_address, temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 14:    //0E DIVI
            {
                registers[temporary_Dst_reg]/=temporary_address;
                System.out.println(String.format("DIVI - divides val:%s with R%s final val:%s",
                        temporary_address, temporary_Dst_reg, registers[temporary_Dst_reg]));
                break;
            }
            case 15:    //0F LDI
            {
                registers[temporary_Dst_reg] = (temporary_address);
                System.out.println(String.format("LDI - loads val:%s into R%s",
                        temporary_address, temporary_Dst_reg));
                break;
            }
            case 16:    //10 SLT
            {
                if(registers[temporary_sreg1] < registers[temporary_sreg2]){
                    registers[temporary_Dst_reg] = 1;
                    System.out.println(String.format("SLT - sets R%s to %s because R%s val:%s < R%s val%s",
                            temporary_Dst_reg, registers[temporary_Dst_reg], temporary_sreg1, registers[temporary_sreg1],
                            temporary_sreg2, registers[temporary_sreg2]));
                }
                else{
                    registers[temporary_Dst_reg] = 0;
                    System.out.println(String.format("SLT - sets R%s to %s because R%s val:%s >= R%s val%s",
                            temporary_Dst_reg, registers[temporary_Dst_reg], temporary_sreg1, registers[temporary_sreg1],
                            temporary_sreg2, registers[temporary_sreg2]));
                }
                break;
            }
            case 17: //11 SLTI
            {
                if(registers[temporary_sreg1] < (temporary_address/4)){
                    registers[temporary_Dst_reg] = 1;
                    System.out.println(String.format("SLTI - sets R%s to %s because R%s val:%s < val%s",
                            temporary_Dst_reg, registers[temporary_Dst_reg], temporary_sreg1, registers[temporary_sreg1],
                            temporary_address/4));

                }
                else{
                    registers[temporary_Dst_reg] = 0;
                    System.out.println(String.format("SLTI - sets R%s to %s because R%s val:%s >= val%s",
                            temporary_Dst_reg, registers[temporary_Dst_reg], temporary_sreg1, registers[temporary_sreg1],
                            temporary_address/4));
                }
                break;
            }
            case 18:    //12 HLT
            {
                programCounter = instructCounter;
                System.out.println(String.format("HLT - logical end of program: %S",
                        programCounter));
                break;
            }
            case 19:    //13 NOP
            {
                //does nothing
                System.out.println("NOP - Does nothing and moves to next instruction");
                break;
            }
            case 20:    //14 JMP
            {
                programCounter = temporary_address/4;
                System.out.println(String.format("JMP - Jump to %s", temporary_address/4));
                break;
            }
            case 21:    //15 BEQ
            {
                if(registers[temporary_breg] == registers[temporary_Dst_reg]){
                    programCounter = temporary_address/4;
                    System.out.println(String.format("BEQ - Branches to address %s because R%s val:%s == R%s val:%s",
                            temporary_address/4, temporary_breg, registers[temporary_breg],
                            temporary_Dst_reg, registers[temporary_Dst_reg]));
                }
                else{
                    System.out.println(String.format("BEQ - Does not branch to address %s because R%s val:%s != R%s val:%s",
                            temporary_address/4, temporary_breg, registers[temporary_breg],
                            temporary_Dst_reg, registers[temporary_Dst_reg]));
                }
                break;
            }
            case 22: //16 BNE
            {
                if(registers[temporary_breg] != registers[temporary_Dst_reg]){
                    //branch
                    programCounter = temporary_address/4;
                    System.out.println(String.format("BNE - Branches to address %s because R%s val:%s != R%s val:%s",
                            temporary_address/4, temporary_breg, registers[temporary_breg],
                            temporary_Dst_reg, registers[temporary_Dst_reg]));

                }
                else{
                    System.out.println(String.format("BEQ - Does not branch to address %s because R%s val:%s == R%s val:%s",
                            temporary_address/4, temporary_breg, registers[temporary_breg],
                            temporary_Dst_reg, registers[temporary_Dst_reg]));
                }
                break;
            }
            case 23: //17 BEZ
            {
                if(registers[temporary_breg] == 0){
                    //branch
                    programCounter = temporary_address/4;
                    System.out.println(String.format("BEZ - Branches to address %s because R%s val:%s == 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                else{
                    System.out.println(String.format("BEZ - Does not branch to address %s because R%s val:%s != 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                break;
            }
            case 24: //18 BNZ
            {
                if(registers[temporary_breg] != 0){
                    //branch
                    programCounter = temporary_address/4;
                    System.out.println(String.format("BNZ - Branches to address %s because R%s val:%s != 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                else{
                    System.out.println(String.format("BNZ - Does nto branch to address %s because R%s val:%s == 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                break;
            }
            case 25:    //19 BGZ
            {
                if(registers[temporary_breg] > 0){
                    //branch
                    programCounter = temporary_address/4;
                    System.out.println(String.format("BGZ - Branches to address %s because R%s val:%s > 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                else{
                    System.out.println(String.format("BGZ - Does not branch to address %s because R%s val:%s < o",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                break;
            }
            case 26:    //1A BLZ
            {
                if (registers[temporary_breg] < 0) {
                    //branch
                    programCounter = temporary_address / 4;
                    System.out.println(String.format("BLZ - Branches to address %s because R%s val:%s < 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
                }
                else {
                    System.out.println(String.format("BLZ - Does not branch to address %s because R%s val:%s > 0",
                            temporary_address/4, temporary_breg, registers[temporary_breg]));
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
        priority = Helpers.hex_to_decimal(job.priority);
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
