/*
    This is our CPU file
 */

import java.util.ArrayList;

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

    public String fetch(int programCounter){
        String instruction = cache[programCounter];
        return instruction;
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

    public void ConvertInstruct(String instruction) {
        String binary_instructions = hex_to_binary(instruction.substring(2));
        String temporary_instructions = binary_instructions;
        instant = Integer.parseInt(temporary_instructions.substring(0, 2));
        operationCode = hex_to_decimal(instruction.substring(2, 8));

        switch (instant) {
            case 00: {
                temporary_sreg1 = binary_to_Integer(temporary_instructions.substring(8, 12));
                temporary_sreg2 = binary_to_Integer(temporary_instructions.substring(12, 16));
                temporary_Dst_reg = binary_to_Integer(temporary_instructions.substring(16, 20));
                break;
            }

            case 01: {
                temporary_breg = binary_to_Integer(temporary_instructions.substring(8, 12));
                temporary_Dst_reg = binary_to_Integer(temporary_instructions.substring(12, 16));
                temporary_address = binary_to_Integer(temporary_instructions.substring(16));
                break;
            }

            case 10: {
                temporary_address = binary_to_Integer(temporary_instructions.substring(8));
                break;
            }

            case 11: {
                //IO operation
                temporary_reg_1 = binary_to_Integer(temporary_instructions.substring(8, 12));
                temporary_reg_2 = binary_to_Integer(temporary_instructions.substring(12, 16));
                temporary_address = binary_to_Integer(temporary_instructions.substring(16));
                break;
            }

            default: {
                System.out.println("EXCEPTION: Invalid instruction type");
            }
        }
    }

    /*
    Returns array of strings InstructionSet
    Each string is a binary number except for any OPCODE values,
    which should be converted to hex already in this method

    When parsing through this, start by checking InstructionSet[0]
    either equals 00, 01, 10, or 11. take indexes based on how many values
    come with that instruction (opcode, reg, dreg, address, etc) and when
    those are taken, go to the next index and check if 00, 01, 10, 11,
    and repeat this.
     */
    public ArrayList<String> decode(int binaryInstruct)
    {
        ArrayList<String> InstructionSet = new ArrayList<String>();

        // Format as string so it can be split
        String binaryInstructStr = String.valueOf(binaryInstruct);

        String firstTwoBits = binaryInstructStr.substring(0, 2); // first 2 bits
        String lastThirty = binaryInstructStr.substring(2); // remaining 30 bits

        // Variables for components of instruction
        String OPcode = "";
        String sReg1 = ""; // Source register - 4 bits
        String sReg2 = ""; // Source Register - (another) 4 bits
        String dReg = ""; // Destination Register - 4 bits
        String bReg = ""; // Base Register - 4 bits
        String address = ""; // Address may at times be treated as data, which is
                            // direct addressing, if not calculate indirect ?
        String reg1 = "";
        String reg2 = "";

        // Instruction Type
        if (firstTwoBits.equals("00")) // Arithmetic
        {
            OPcode = lastThirty.substring(0, 6);
            // CONVERT OPCODE TO HEX HERE
            sReg1 = lastThirty.substring(6, 10);
            sReg2 = lastThirty.substring(10, 14);
            dReg = lastThirty.substring(14, 18);
            // 000000000000 not used
            InstructionSet.add("00");
            InstructionSet.add(OPcode);
            InstructionSet.add(sReg1);
            InstructionSet.add(sReg2);
            InstructionSet.add(dReg);
        }
        else if (firstTwoBits.equals("01")) // Conditional branch and Immediate format
        {
            OPcode = lastThirty.substring(0, 6);
            // CONVERT OPCODE TO HEX HERE
            bReg = lastThirty.substring(6, 10);
            dReg = lastThirty.substring(10, 14);
            address = lastThirty.substring(14);
            InstructionSet.add("01");
            InstructionSet.add(OPcode);
            InstructionSet.add(bReg);
            InstructionSet.add(dReg);
            InstructionSet.add(address);
        }
        else if (firstTwoBits.equals("10")) // Unconditional Jump
        {
            OPcode = lastThirty.substring(0, 6);
            // CONVERT OPCODE TO HEX HERE
            address = lastThirty.substring(6);
            InstructionSet.add("10");
            InstructionSet.add(OPcode);
            InstructionSet.add(address);

        }
        else if (firstTwoBits.equals("11"))
        {
            OPcode = lastThirty.substring(0, 6);
            // CONVERT OPCODE TO HEX HERE
            reg1 = lastThirty.substring(6, 10);
            reg2 = lastThirty.substring(10, 14);
            address = lastThirty.substring(14);
            InstructionSet.add("11");
            InstructionSet.add(OPcode);
            InstructionSet.add(reg1);
            InstructionSet.add(reg2);
            InstructionSet.add(address);
        }
        return InstructionSet;
    }
    
    public void execute() {}
    
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
