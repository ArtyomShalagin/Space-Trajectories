package optimization;

import data_struct.Vec;
import emulation.EmulationReport;

public class FitnessFunction {
    public static int fitness(EmulationReport rep) {
        int ans = 0;
        for (Vec[] v : rep.getCaptures()) {
            ans += v.length;
        }
        return ans;
    }
}
