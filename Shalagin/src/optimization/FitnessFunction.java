package optimization;

import emulation.EmulationReport;

public class FitnessFunction {
    public static int fitness(EmulationReport rep) {
        return rep.getCaptures().size();
    }
}
