package optimization;

import data.Const;
import data.PlanetMap;
import data.Ship;
import data_struct.Vec;
import emulation.EmulationReport;
import emulation.Emulator;

import java.util.ArrayList;
import java.util.Random;

public class Genetic {
    private static Random rnd = new Random(239566);

    public static ArrayList<Vec> generateRandom(int size) {
        ArrayList<Vec> ans = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double rest = Const.maxLowThrustForce;
            rest *= rest;
            double x = Math.sqrt(rnd.nextDouble() * rest);
            rest -= x * x;
            double y = Math.sqrt(rnd.nextDouble() * rest);
            rest -= y * y;
            double z = Math.sqrt(rnd.nextDouble() * rest);
            ans.add(new Vec(x, y, z));
        }

        return ans;
    }

    public static EmulationReport onePlusOne(PlanetMap map, Vec[] stars, long steps, double step, int generations) {
        int size = 1000;
        int best = -1;
        EmulationReport bestRep = null;
        ArrayList<ArrayList<Vec>> curr = new ArrayList<>();
        for (Ship ship : map.getShips()) {
            ArrayList<Vec> thrust = generateRandom(size);
            curr.add(thrust);
            ship.setThrust(thrust);
        }
        for (int gen = 0; gen < generations; gen++) {
            System.out.println("Starting generation " + gen);
            int changeInd = rnd.nextInt(map.getShips().size());
            ArrayList<Vec> newAns = generateRandom(size);
            ArrayList<Vec> oldAns = curr.get(changeInd);
            map.getShips().get(changeInd).setThrust(newAns);
            EmulationReport rep = new Emulator().emulate(map.clone(), stars, steps, step);
            int newRes = FitnessFunction.fitness(rep);
            if (newRes > best) {
                System.out.println("Found new best " + newRes);
                best = newRes;
                bestRep = rep;
                curr.set(changeInd, newAns);
            } else {
                map.getShips().get(changeInd).setThrust(oldAns);
            }
        }

        System.out.println("After " + generations + " generations best is " + best);

        return bestRep;
    }
}
