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
    private static int size = 1000;

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
        int best = -1;
        EmulationReport bestRep = null;
        for (Ship ship : map.getShips()) {
            ArrayList<Vec> thrust = generateRandom(size);
            ship.setThrust(thrust);
        }
        for (int gen = 0; gen < generations; gen++) {
            System.out.println("Starting generation " + gen);
            PlanetMap newMap = map.clone();
            int changeInd = rnd.nextInt(map.getShips().size());
            if (rnd.nextInt(2) == 0) { //change start speed
                double speedBound = 3000;
                Vec newSpeed = new Vec(rnd.nextDouble() * speedBound,
                        rnd.nextDouble() * speedBound, rnd.nextDouble() * speedBound);
                newMap.getShips().get(changeInd).setStartSpeed(newSpeed);
            } else { //change thrust vec
                ArrayList<Vec> newAns = generateRandom(size);
                newMap.getShips().get(changeInd).setThrust(newAns);
            }
            EmulationReport rep = new Emulator().emulate(newMap.clone(), stars, steps, step);

            int newRes = FitnessFunction.fitness(rep);
            if (newRes > best) {
                System.out.println("Found new best " + newRes);
                best = newRes;
                bestRep = rep;
                map = newMap;
            }
        }

        System.out.println("After " + generations + " generations best is " + best);

        return bestRep;
    }
}
