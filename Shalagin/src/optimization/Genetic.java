package optimization;

import data.Const;
import data.PlanetMap;
import data.Ship;
import data_struct.Vec;
import emulation.EmulationReport;
import emulation.EmulationThread;
import emulation.Emulator;
import gui.Control;
import io.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Genetic {
    private static Random rnd = new Random(982392);
    private static double speedBound = 2000;

    private static ArrayList<Vec> generateRandomThrust(int size) {
        ArrayList<Vec> ans = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ans.add(randomVec(Const.maxLowThrustForce));
        }

        return ans;
    }

    private static Vec randomVec(double bound) {
        double rest = Math.max(rnd.nextDouble(), .6) * bound;
        rest *= rest;
        double x = Math.sqrt(rnd.nextDouble() * rest);
        if (rnd.nextInt(2) == 1) {
            x = -x;
        }
        rest -= x * x;
        double y = Math.sqrt(rnd.nextDouble() * rest);
        if (rnd.nextInt(2) == 1) {
            y = -y;
        }
        rest -= y * y;
        double z = Math.sqrt(rnd.nextDouble() * rest);
        if (rnd.nextInt(2) == 1) {
            z = -z;
        }
        return new Vec(x, y, z);
    }

    public static EmulationReport onePlusOne(PlanetMap map, Vec[] stars, long steps, double step, int generations) {
        int best = -1;
        EmulationReport bestRep = null;
        for (Ship ship : map.getShips()) {
            ArrayList<Vec> thrust = generateRandomThrust(Const.divisionSize);
            ship.setThrust(thrust);
        }
        for (int gen = 0; gen < generations; gen++) {
            System.out.println("Starting generation " + gen + " / " + generations);
            PlanetMap newMap = map.clone();
            int changeInd = rnd.nextInt(map.getShips().size());
            Vec newSpeed = randomVec(speedBound);
            newMap.getShips().get(changeInd).setStartSpeed(newSpeed);
            changeInd = rnd.nextInt(map.getShips().size());
            ArrayList<Vec> newAns = generateRandomThrust(Const.divisionSize);
            newMap.getShips().get(changeInd).setThrust(newAns);

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

    public static EmulationReport muPlusLambda(PlanetMap map, Vec[] stars, long steps, double step, int generations) {
        int mu = 4, lambda = 2 * mu;
        ArrayList<Genotype> genes = new ArrayList<>();
        for (int i = 0; i < mu; i++) {
            ArrayList<Ship> shipsCopy = new ArrayList<>();
            for (Ship ship : map.getShips()) {
                shipsCopy.add(ship.clone());
            }
            genes.add(new Genotype(shipsCopy));
        }
        int best = -1;
        EmulationReport bestRep = null;
        for (int gen = 0; gen < generations; gen++) {
            System.out.println("Starting generation " + gen);
            ArrayList<Genotype> newShips = new ArrayList<>();
            for (int i = 0; i < lambda; i++) {
                int parentInd = rnd.nextInt(mu);
                newShips.add(mutate(genes.get(parentInd)));
            }

            Thread[] threads = new Thread[lambda];
            final EmulationReport[] results = new EmulationReport[lambda];
            for (int i = 0; i < lambda; i++) {
                PlanetMap newMap = map.clone();
                newMap.setShips(newShips.get(i).clone().getData());
                threads[i] = new Thread(new EmulationThread(newMap, stars, steps, step, i, results));
            }
            for (Thread tr : threads) {
                tr.start();
            }
            for (Thread tr : threads) {
                try {
                    tr.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < lambda; i++) {
                int fit = FitnessFunction.fitness(results[i]);
                newShips.get(i).setFit(fit);
                if (fit > best) {
                    best = fit;
                    System.out.println("New best " + fit);
                    bestRep = results[i];
                }
            }
            newShips.addAll(genes);
            Collections.sort(newShips, Collections.reverseOrder());
            genes.clear();
            for (int i = 0; i < mu; i++) {
                genes.add(newShips.get(i));
            }
            for (Genotype loh : genes) {
                System.out.println(loh.getFit());
            }
            if (gen % 10 == 0) {
                Control.saveImg(bestRep, map);
                Logger.writeReport(bestRep);
            }

        }

        System.out.println("After " + generations + " generations best is " + best);

        return bestRep;
    }

    private static Genotype mutate(Genotype gen) {
        ArrayList<Ship> mutated = new ArrayList<>();

        int ind = rnd.nextInt(gen.size());
        for (int i = 0; i < gen.size(); i++) {
            Ship newShip = gen.getData().get(i).clone();
            if (i != ind) {
                mutated.add(newShip);
                continue;
            }
            if (rnd.nextInt(2) == 1) { //change start speed
                Vec newSpeed = randomVec(speedBound);
                newShip.setStartSpeed(newSpeed);
            } else { //change thrust vector
                int startIndex = rnd.nextInt(Const.divisionSize);
                ArrayList<Vec> newThrust = generateRandomThrust(Const.divisionSize);
                for (int j = startIndex; j < newThrust.size(); j++) {
                    newShip.getThrustArr().set(j, newThrust.get(j));
                }
                newShip.setThrust(newThrust);
            }
            Vec newCoord = randomVec(80000000);
            newShip.setCoords(newCoord);
            mutated.add(newShip);
        }

        return new Genotype(mutated);

    }
}
