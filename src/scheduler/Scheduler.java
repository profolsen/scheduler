package scheduler;

/**
 * Created by po917265 on 2/12/20.
 */
public abstract class Scheduler implements Iterable<ProcessControlBlock> {

    private int contextSwitchTime;  //how long a context switch takes.
    private int clock;  //the number of cycles since the system was "started" (the current time).
    private int lastUpdateTime;  //the time at which the last update happened.

    public Scheduler(int contextSwitchTime) {
        this.contextSwitchTime = contextSwitchTime;
        clock = 0;
    }

    public abstract void add(ProcessControlBlock pcb);
    public abstract ProcessControlBlock next();
    public abstract boolean isEmpty();
    public abstract void execute(ProcessControlBlock pcb);

    public void execute() {
        System.out.println("Executing Processes");
        while(!isEmpty()) {
            ProcessControlBlock pcb = next();
            clock += contextSwitchTime;  //a context switch is happening...
            if(pcb == null) {
                tick();
                continue;
            }
            System.out.println("running " + pcb.pid() + " @" + clock);
            execute(pcb);
            updateProcessControlBlocks();
            add(pcb);
        }
    }

    public void tick() {
        clock++;
    }

    public void tick(int time) {
        clock += time;
    }

    public void updateProcessControlBlocks() {
        int timeSinceLastUpdate = clock - lastUpdateTime;
        for(ProcessControlBlock pcb : this) {
            pcb.update(timeSinceLastUpdate);
        }
        lastUpdateTime = clock;
    }

}
