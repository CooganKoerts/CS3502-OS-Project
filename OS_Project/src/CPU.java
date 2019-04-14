/*
    This is our CPU file
 */

import com.sun.tools.corba.se.idl.TypedefGen;

public class CPU
{
    // CPU PCB
    private int cpuNumber;
    public int programCounter; // holds address of instruction to fetch
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
    // Int priority; // of the process, extracted from JOB control line
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


    public void execute() {}

    /*
    fetch() returns a hex string of one (of the many) instructions for a job
    @param RAMblock: the index in RAM where an array of instructions for a job is stored
    @param programCounter: The CPU Program counter. assuming it is the instruction num
                            to be called (for a job)

    to find value of RAMblock for a job:
    JobID = #;
    for (int i = 0; i < Driver.queueREADY.size; i++)
    {
        if (jobID == Driver.queueREADY.get(i).jobID)
        {
            RAMblock = Driver.queueREADY.get(i).registers[0];
        }
    }
     */
    public String fetch(int RAMblock, int programCounter){
        String instruction = "";
        if (Memory.getRAMSize() > 0 && Memory.pullFromRam(RAMblock).length >= programCounter)
        {
            String[] RAMListOfInstruct = Memory.pullFromRam(RAMblock);
            instruction = RAMListOfInstruct[programCounter];
        }
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
        String binary_instructions = hex_to_binary(instruction);
        String temporary_instructions = binary_instructions.substring(2);

        // first 2 bits
        instant = Integer.parseInt(binary_instructions.substring(0, 2));

        switch (instant) {
            case 00: { // Arithmetic
                operationCode = binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_sreg1 = binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_sreg2 = binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_Dst_reg = binary_to_decimal(temporary_instructions.substring(14, 18));
                break;
            }

            case 01: { // Conditional branch and Immediate format
                operationCode = binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_breg = binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_Dst_reg = binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_address = binary_to_decimal(temporary_instructions.substring(14));
                break;
            }

            case 10: { // Unconditional jump
                operationCode = binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_address = binary_to_decimal(temporary_instructions.substring(6));
                break;
            }

            case 11: { //IO operation
                operationCode = binary_to_hexadecimal(temporary_instructions.substring(0, 6));
                temporary_reg_1 = binary_to_decimal(temporary_instructions.substring(6, 10));
                temporary_reg_2 = binary_to_decimal(temporary_instructions.substring(10, 14));
                temporary_address = binary_to_decimal(temporary_instructions.substring(14));
                break;
            }

            default: {
                System.out.println("EXCEPTION: Invalid instruction type");
            }
        }
        return instant;
    }

    // The instruction may read the content of Address/Reg 2 into Reg 1.
    // given reg1, reg2, address
    // Reads content of I/P buffer into a accumulator
    /*public void read(int jobID) {
        if (temporary_reg_2 > 0) {
            registers[temporary_reg_1] =
        }
        else {

        }
    }*/

    public static String hex_to_binary(String s)
    {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }

        String val2 = Integer.toBinaryString(val);
        return val2;
    }

    public static int hex_to_decimal(String s)
    {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }

    public int binary_to_Integer(String binary) {
        char[] numbers = binary.toCharArray();
        int result = 0;
        for(int i=numbers.length - 1; i>=0; i--)
            if(numbers[i]=='1')
                result += Math.pow(2, (numbers.length-i - 1));
        return result;
    }

    public boolean isBinary(String num) {
        boolean isBinary = false;
        if (num != null && !num.isEmpty())
        {
            long number = Long.parseLong(num);
            while (number > 0) {
                if (number % 10 <= 1) {
                    isBinary = true;
                }
                else {
                    isBinary = false;
                    break;
                }
                number /= 10;
            }
        }
        return isBinary;
    }

    public int binary_to_decimal(String binary) {
        int length = binary.length() - 1;
        int decimal = 0;
        if (isBinary(binary)) {
            char[] digits = binary.toCharArray();
            for (char digit : digits) {
                if (String.valueOf(digit).equals("1")) {
                    decimal += Math.pow(2, length);
                }
                length--;
            }
        }
        return decimal;
    }

    public String binary_to_hexadecimal(String binary) {
        String hexa = "";
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        if (binary != null && !binary.isEmpty())
        {
            int decimal = binary_to_decimal(binary);
            while (decimal > 0) {
                hexa = hex[decimal % 16] + hexa;
                decimal /= 16;
            }
        }
        return hexa;
    }

    
    /*
    Param AddressingMode a: Direct or Indirect
    Param int base: when a job is scheduled and loaded into RAM, base will be the index in RAM (?)
     */
    public void effective_Addr(AddressingMode a, int base) {
        if (a.equals(AddressingMode.Direct))
        {

        }
        else if (a.equals(AddressingMode.Indirect))
        {

        }
    }
}
