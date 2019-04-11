/*
    This is our CPU file
 */

public class CPU
{
    public int[] registers = new int[16];

    public String[] cache;

    public int temporary_reg_1;
    public int temporary_reg_2;
    public int temporary_Dst_reg;
    public int temporary_breg;
    public int temporary_sreg1;
    public int temporary_sreg2;
    public int temporary_address;

    private int cpuNumber;
    public int instant;
    public int operationCode;
    public int programCounter;
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
        //ArrayList<Integer> InstructionSet = new ArrayList<Integer>();

        // get binary conversion of last 30 bits of instruction
        String binary_instructions = hex_to_binary(instruction.substring(2));
        String temporary_instructions = binary_instructions;

        // first 2 bits
        instant = Integer.parseInt(instruction.substring(0, 2));

        switch (instant) {
            case 00: { // Arithmetic
                operationCode = binary_to_Integer(temporary_instructions.substring(0, 6)); // needs to be converted back to hex
                temporary_sreg1 = binary_to_Integer(temporary_instructions.substring(6, 10));
                temporary_sreg2 = binary_to_Integer(temporary_instructions.substring(10, 14));
                temporary_Dst_reg = binary_to_Integer(temporary_instructions.substring(14, 18));
                /*InstructionSet.add(00);
                InstructionSet.add(operationCode);
                InstructionSet.add(temporary_sreg1);
                InstructionSet.add(temporary_sreg2);
                InstructionSet.add(temporary_Dst_reg);*/
                break;
            }

            case 01: { // Conditional branch and Immediate format
                operationCode = binary_to_Integer(temporary_instructions.substring(0, 6)); // needs to be converted back to hex
                temporary_breg = binary_to_Integer(temporary_instructions.substring(6, 10));
                temporary_Dst_reg = binary_to_Integer(temporary_instructions.substring(10, 14));
                temporary_address = binary_to_Integer(temporary_instructions.substring(14));
                /*InstructionSet.add(01);
                InstructionSet.add(operationCode);
                InstructionSet.add(temporary_breg);
                InstructionSet.add(temporary_Dst_reg);
                InstructionSet.add(temporary_address);*/
                break;
            }

            case 10: { // Unconditional jump
                operationCode = binary_to_Integer(temporary_instructions.substring(0, 6)); // needs to be converted back to hex
                temporary_address = binary_to_Integer(temporary_instructions.substring(6));
                /*InstructionSet.add(10);
                InstructionSet.add(operationCode);
                InstructionSet.add(temporary_address);*/
                break;
            }

            case 11: { //IO operation
                operationCode = binary_to_Integer(temporary_instructions.substring(0, 6)); // needs to be converted back to hex
                temporary_reg_1 = binary_to_Integer(temporary_instructions.substring(6, 10));
                temporary_reg_2 = binary_to_Integer(temporary_instructions.substring(10, 14));
                temporary_address = binary_to_Integer(temporary_instructions.substring(14));
                /*InstructionSet.add(11);
                InstructionSet.add(operationCode);
                InstructionSet.add(temporary_reg_1);
                InstructionSet.add(temporary_reg_2);
                InstructionSet.add(temporary_address);*/
                break;
            }

            default: {
                System.out.println("EXCEPTION: Invalid instruction type");
            }
        }
        return instant;
    }

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
