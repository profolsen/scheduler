package scheduler;

/**
 * Created by po917265 on 2/12/20.
 */
public class ProcessControlBlock {
    private int duration;
    private int cpuBurstTime;
    private int ioBurstTime;
    private int currentBurstDuration;  //how much of the current burst is left.
    private String state; //the current state of the process.
    private int pid;  //the pid of the process.
    private int ioRequestTime; //the moment in time when an I/O request is made.

    private static int pidSource = 2;

    public static final String READY = "READY";
    public static final String NEW = "NEW";
    public static final String WAITING = "WAITING";
    public static final String TERMINATED = "TERMINATED";
    public static final String RUNNING = "RUNNING";

    /**
     * Creates a new ProcessControlBlock with the following info:
     * @param duration how long until the process terminates.
     * @param cpuBurstTime how long the process can use the CPU before it must request I/O
     * @param ioBurstTime how long it takes for an I/O request to be fulfilled.
     */
    public ProcessControlBlock(int duration, int cpuBurstTime, int ioBurstTime) {
        this.duration = duration;
        this.cpuBurstTime = cpuBurstTime;
        this.ioBurstTime = ioBurstTime;
        this.currentBurstDuration = cpuBurstTime;
        state = READY;
        pid = pidSource++;
    }

    public int execute(int quantum, int clock) {
        if(quantum < currentBurstDuration) {
            System.out.println(pid + " will use entire quantum.");
            currentBurstDuration -= quantum;
            duration -= quantum;
            System.out.println(pid + " remaining duration: " + duration);
            return quantum;
        } else if(currentBurstDuration < duration){
            System.out.println(pid + " will complete CPU burst.");
            int usedTime = currentBurstDuration;
            duration -= currentBurstDuration;
            currentBurstDuration = ioBurstTime;
            state = WAITING;
            ioRequestTime = clock;
            System.out.println(pid + " remaining duration: " + duration);
            return usedTime;
        } else {
            System.out.println(pid + " will terminate.");
            int usedTime = duration;
            duration = 0;
            state = TERMINATED;
            System.out.println(pid + " remaining duration: " + duration);
            return usedTime;
        }
    }

    public void update(int clock) {
        //System.out.println(pid + " clock: " + clock + "; currentBurstDuration: " + currentBurstDuration + "; ioRequestTime: " + ioRequestTime);
        if(state.equals(WAITING)) {
            if(clock - currentBurstDuration > ioRequestTime) {
                currentBurstDuration = cpuBurstTime;
                state = READY;
            }
        }
    }


    public String state()   {  return state;  }


    public int pid()     {  return pid;    }

    public boolean equals(Object o) {
        if(o instanceof ProcessControlBlock) {
            return ((ProcessControlBlock) o).pid == pid;
        }
        return false;
    }

    public String toString() {
        return "{" + pid + ", " + state + ", " + duration + ", " + cpuBurstTime +
                    ", " + ioBurstTime + ", " + currentBurstDuration + "}";
    }
}
