package emulation;

import data.*;
import data_struct.Vec;
import flanagan.integration.RungeKutta;
import gui.Control;
import io.Logger;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Emulator {

    private int timeout = 0;
    private int wins = 0;

    private void setResult(Gravitationable g, double[] y) {
        g.setCoords(new Vec(y[0], y[1], y[2]));
        g.setSpeed(new Vec(y[3], y[4], y[5]));
    }

    private RungeKutta setRungeKutta(double x0, double xn, double[] initial, double step) {
        RungeKutta rk = new RungeKutta();
        rk.setInitialValueOfX(x0);
        rk.setFinalValueOfX(xn);
        rk.setInitialValuesOfY(initial);
        rk.setStepSize(step);

        return rk;
    }

    private void calc(PlanetMap map, Gravitationable g, double currTime, double step) {
        Derivn der = new Derivn(map, g);
        double[] initial = {g.getCoords().getX(), g.getCoords().getY(), g.getCoords().getZ(),
                g.getSpeed().getX(), g.getSpeed().getY(), g.getSpeed().getZ()};
        RungeKutta rk = setRungeKutta(currTime, currTime + step, initial, step);
        double[] result = rk.fourthOrder(der);
        setResult(g, result);
    }

    private void checkCollisions(PlanetMap map) {
        for (Gravitationable g1 : map.getElements()) {
            for (Gravitationable g2 : map.getElements()) {
                if (g1 == g2) {
                    continue;
                }
                if (collided(g1, g2)) {
                    g1.setCollided();
                    g2.setCollided();
                }
            }
        }
        for (Ship s : map.getShips()) {
            if (s.collided()) {
                continue;
            }
            if (s.getCoords().module() > 10000000000L) {
                s.setCollided();
                System.out.println("gone too far");
            }
        }
    }

    private boolean collided(Gravitationable g1, Gravitationable g2) {
        double dist = Vec.dist(g1.getCoords(), g2.getCoords());
        return dist < g1.getRad() + g2.getRad();
    }

    private Vec[] checkStars(PlanetMap map, Vec[] stars) {
        if (map.getShips().size() < 3) {
            return new Vec[]{};
        }
        ArrayList<Vec> captures = new ArrayList<>();
        for (int i1 = 0; i1 < map.getShips().size(); i1++) {
            if (map.getShips().get(i1).collided()) {
                continue;
            }
            for (int i2 = i1 + 1; i2 < map.getShips().size(); i2++) {
                if (map.getShips().get(i2).collided()) {
                    continue;
                }
                for (int i3 = i2 + 1; i3 < map.getShips().size(); i3++) {
                    if (map.getShips().get(i3).collided()) {
                        continue;
                    }
                    Vec v1 = map.getShips().get(i1).getCoords();
                    Vec v2 = map.getShips().get(i2).getCoords();
                    Vec v3 = map.getShips().get(i3).getCoords();
                    Vec perp = Vec.vecMult(v1.minus(v2), v1.minus(v3));
                    for (Vec v : stars) {
                        double angle = Vec.angle(perp, v);
                        if (angle < PI / 500) {
                            captures.add(v);
                        }
                    }
                }
            }
        }
        Vec[] ans = new Vec[captures.size()];
        for (int i = 0; i < captures.size(); i++) {
            ans[i] = captures.get(i);
        }

        return ans;
    }

    public EmulationReport emulate(PlanetMap map, Vec[] stars, long steps, double step) {
        long start = System.currentTimeMillis();
        PlanetMap back = map.clone();
        EmulationReport rep = new EmulationReport(map);
        long numberOfPoints = 100000;
        long distBetweenPoints = max(steps / numberOfPoints, 1);
        double currTime = 0;
        for (Ship ship : map.getShips()) {
            ship.setTotalSteps(steps);
            ship.setSpeed(ship.getStartSpeed());
        }
        double collTime = 0, captTime = 0, calcTime = 0;
        double t;
        for (long i = 0; i < steps; i++) {
            t = System.currentTimeMillis();
            checkCollisions(map);
            collTime += System.currentTimeMillis() - t;
//            if (i % 10 == 0) {
            if (timeout <= 0) {
                t = System.currentTimeMillis();
                Vec[] captures = checkStars(map, stars);
                captTime += System.currentTimeMillis() - t;
                if (captures.length > 0) {
                    rep.addCapture(captures);
                    System.out.println("Captured on " + i + " step");
                    timeout = 43200;
                }
                wins += captures.length;
            } else {
                timeout -= step;
            }
//            }
            for (Gravitationable g : rep.getElements()) {
                if (g.collided()) {
                    continue;
                }
                t = System.currentTimeMillis();
                calc(map, g, currTime, step);
                calcTime += System.currentTimeMillis() - t;

                if (i % distBetweenPoints == 0) {
                    rep.add(g, g.getCoords().clone());
                }

                currTime += step;
            }
            for (Ship ship : map.getShips()) {
                ship.setDoneSteps(ship.getDoneSteps() + 1);
            }
//            if (i % max(steps / 5, 1) == 0) {
//                System.out.println("Done " + i + " out of " + steps + ", " +
//                        ((long) ((double) i * 100 / steps)) +
//                        " percent in " + (System.currentTimeMillis() - start) / 1000 + "s");
//            }
        }
        int coll = 0;
        for (Ship s : map.getShips()) {
            if (s.collided()) {
                coll++;
            }
        }
        System.out.println("Done " + steps + " steps in " +
                (System.currentTimeMillis() - start) / 1000 + "s, " + collTime / 1000 + " checking collisions, " +
                captTime / 1000 + " checking captures, " + calcTime / 1000 + " doing calculations; " + coll +
                " ships collided");
        System.out.println(wins + " captures in " +
                (int) (steps * step / (60 * 60 * 24)) + " days");

//            Control.saveImg(rep, map);
//        if (coll == 0) {
//            Logger.writeReport(rep);
//        }
        rep.setPlanetMap(back);

        return rep;
    }
}
