package App;


import Domain.Allocation.Allocation;
import DataStruct.ResourceNode;
import Domain.Scheduler;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final String filePath = "data.xlsx";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        Scheduler problem = Preprocessing.generateDemoData();
        SolverFactory<Scheduler> solverFactory = SolverFactory.createFromXmlResource("apsDemoConfig.xml");
        Solver<Scheduler> solver = solverFactory.buildSolver();
        Scheduler solution = solver.solve(problem);
        Preprocessing.print(solution);
    }
}
