package OS_Project.src;/*
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

    /*
    * @param: Integer instruction from decode method
    * Reg-0 is the accumulator
    * Reg-1 is the zero register
    * */
    public void Execute(Integer inst){

        switch (operationCode)
        {
            // Inst: RD Type: I/0
            case 0:
            {
                if(temporary_reg_2 > 0)
                {
                    registers[temporary_reg_1] = hex_to_decimal(cache[registers[temporary_reg_2]/4].substring(2));
                }
                else
                {
                    registers[temporary_reg_1] = hex_to_decimal(cache[temporary_address/4].substring(2));
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
                    cache[temporary_address/4] = "0x" + decimal_to_hex(registers[temporary_reg_1]);
                    //dma write with temporary_address
                }



                break;
            }
            case 2: //2 ST
            {
                cache[registers[temporary_Dst_reg]/4] = "0x" + decimal_to_hex( registers[temporary_breg]);
                break;
            }
            case 3: //LW
            {
                registers[temporary_Dst_reg]= hex_to_decimal(cache[(registers[temporary_breg]/4) + temporary_address].substring(2));

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
        programCounter++;

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

    public String decimal_to_hex(int decimal){
        int sizeOfIntInHalfBytes = 8;
        int numberOfBitsInAHalfByte = 4;
        int halfByte = 0x0F;
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        StringBuilder hexBuilder = new StringBuilder(sizeOfIntInHalfBytes);
        hexBuilder.setLength(sizeOfIntInHalfBytes);
        for (int i = sizeOfIntInHalfBytes - 1; i >= 0; --i)
        {
            int j = decimal & halfByte;
            hexBuilder.setCharAt(i, hexDigits[j]);
            decimal >>= numberOfBitsInAHalfByte;
        }
        return hexBuilder.toString();
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
