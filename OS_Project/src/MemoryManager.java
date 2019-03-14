/*

    Memory manager class will be in-charge of manipulating memory addresses and registers

 */

public class MemoryManager {
    private Memory memory = new Memory();
    private int[] registers = memory.getRegisters();

    public int getRegisterSize() {
        return memory.sizeOfMemory();
    }

    /*public int getRAMSize() {
        return memory.ramSize(); //TODO
    }*/

    public int getDiskSize() {
        return memory.getDiskSize();
    }

    public void writeTORegister(int n, int value) {
        registers[n] = value;
    }

    public void writeToRAM() { }
    public void writeToDisk() { }

}
