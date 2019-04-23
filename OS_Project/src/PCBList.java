import java.util.LinkedList;

public class PCBList {
    private static LinkedList<ProcessControlBlock> pcbList = new LinkedList<ProcessControlBlock>();

    public static void PCBList() {
        pcbList=new LinkedList<ProcessControlBlock>();
    }

    public static int getJobAmount() {
        return pcbList.size();
    }

    public static void insertPCB(ProcessControlBlock pcb) {
        pcbList.add(pcb);
    }

    public static ProcessControlBlock getPCB(int index) {
        return pcbList.get(index);
    }

}
