package App;

import Domain.Scheduler;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final String filePath = "data.xlsx";
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        logger.info("Starting the solver application");
        Scheduler problem = Preprocessing.generateDemoData();
        SolverFactory<Scheduler> solverFactory = SolverFactory.createFromXmlResource("apsDemoConfig.xml");
        Solver<Scheduler> solver = solverFactory.buildSolver();
        Scheduler solution = solver.solve(problem);
        Preprocessing.print(solution);
        logger.info("Finished the solver application");
    }
}
