package Domain;

import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PlanningSolution
public class Scheduler {
    private String id;

    public Scheduler() {
    }

    public boolean dataCheck() {
        /*
         1.check the resource timeslots never null
         2.check the execution mode is never null
         3.no loop in craft path

         */
        return true;
    }
}


