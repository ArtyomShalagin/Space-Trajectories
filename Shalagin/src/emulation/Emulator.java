package emulation;

import data.*;
import data_struct.Vec;
import flanagan.integration.RungeKutta;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Emulator {

    private static int timeout = 0;
    private static int wins = 0;

    private static void setResult(Gravitationable g, double[] y) {
        g.setCoords(new Vec(y[0], y[1], y[2]));
        g.setSpeed(new Vec(y[3], y[4], y[5]));
    }

    private static RungeKutta setRungeKutta(double x0, double xn, double[] initial, double step) {
        RungeKutta rk = new RungeKutta();
        rk.setInitialValueOfX(x0);
        rk.setFinalValueOfX(xn);
        rk.setInitialValuesOfY(initial);
        rk.setStepSize(step);

        return rk;
    }

    private static void calc(PlanetMap map, Gravitationable g, double currTime, double step) {
        Derivn der = new Derivn(map, g);
        double[] initial = {g.getCoords().getX(), g.getCoords().getY(), g.getCoords().getZ(),
                g.getSpeed().getX(), g.getSpeed().getY(), g.getSpeed().getZ()};
        RungeKutta rk = setRungeKutta(currTime, currTime + step, initial, step);
        double[] result = rk.fourthOrder(der);
        setResult(g, result);
    }

    private static void checkCollisions(PlanetMap map) {
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
    }

    private static boolean collided(Gravitationable g1, Gravitationable g2) {
        double dist = Vec.dist(g1.getCoords(), g2.getCoords());
        return dist < g1.getRad() + g2.getRad();
    }

    private static Vec[] checkStars(PlanetMap map, Vec[] stars) {
        if (map.getShips().size() < 3) {
            return new Vec[]{};
        }
        ArrayList<Vec> captures = new ArrayList<>();
        for (int i1 = 0; i1 < map.getShips().size(); i1++) {
            for (int i2 = i1 + 1; i2 < map.getShips().size(); i2++) {
                for (int i3 = i2 + 1; i3 < map.getShips().size(); i3++) {
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

    public static EmulationReport emulate(PlanetMap map, Vec[] stars, long steps, double step) {
        long start = System.currentTimeMillis();

        EmulationReport rep = new EmulationReport(map);
        long numberOfPoints = 100000;
        long distBetweenPoints = max(steps / numberOfPoints, 1);
        double currTime = 0;
        for (long i = 0; i < steps; i++) {
            checkCollisions(map);
            if (i % 10 == 0) {
                Vec[] captures = checkStars(map, stars);
                if (timeout <= 0 && captures.length > 0) {
                    rep.addCapture(captures);
                    timeout = 21600;
                    wins += captures.length;
                } else {
                    timeout -= distBetweenPoints;
                }
            }
            for (Gravitationable g : rep.getElements()) {
                if (g.collided()) {
                    continue;
                }
                calc(map, g, currTime, step);

                if (i % distBetweenPoints == 0) {
                    rep.add(g, g.getCoords().clone());
                }

                currTime += step;
            }
            if (i % max(steps / 20, 1) == 0) {
                System.out.println("Done " + i + " out of " + steps + ", " +
                        ((long) ((double) i * 100 / steps)) +
                        " percent in " + (System.currentTimeMillis() - start) / 1000 + "s");
            }
        }

        System.out.println("Done " + steps + " steps in " +
                (System.currentTimeMillis() - start) / 1000 + "s");
        System.out.println(wins + " captures in " +
                steps * step / (60 * 60 * 24) + " days");

        return rep;
    }
}
