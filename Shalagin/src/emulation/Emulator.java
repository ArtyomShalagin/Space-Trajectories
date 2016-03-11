package emulation;

import data.*;
import flanagan.integration.DerivnFunction;
import flanagan.integration.RungeKutta;

import static java.lang.Math.*;

public class Emulator {

    private static void setResult(Gravitationable g, double[] y) {
        g.setX(y[0]);
        g.setY(y[1]);
        g.setZ(y[2]);
        g.setVX(y[3]);
        g.setVY(y[4]);
        g.setVZ(y[5]);
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
        double[] initial = {g.getX(), g.getY(), g.getZ(),
                g.getVX(), g.getVY(), g.getVZ()};
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
        double dist = dist(g1.getCoords(), g2.getCoords());
        return dist < g1.getRad() + g2.getRad();
    }

    private static double dist(double[] x1, double[] x2) {
        return sqrt(pow(x1[0] - x2[0], 2) + pow(x1[1] - x2[1], 2)
                + pow(x1[2] - x2[2], 2));
    }

    public static EmulationReport emulate(PlanetMap map, long steps, double step) {
        long start = System.currentTimeMillis();

        EmulationReport rep = new EmulationReport(map);
//        long steps = 5500000000L;
//        double distBetweenPoints = 100000;
        long numberOfPoints = 100000;
        long distBetweenPoints = max(steps / numberOfPoints, 1);
        double currTime = 0;
        for (long i = 0; i < steps; i++) {
            checkCollisions(map);
            for (Gravitationable g : rep.getElements()) {
                if (g.collided()) {
                    continue;
                }
                calc(map, g, currTime, step);

//                double dist;
//                if (rep.getTrajectory(g).size() != 0) {
//                    dist = dist(g.getCoords(), rep.getTrajectory(g).
//                            getPoint(rep.getTrajectory(g).size() - 1));
//                } else {
//                    dist = Double.MAX_VALUE;
//                }
//                if (dist > distBetweenPoints) {
//                    rep.add(g, g.getCoords().clone());
//                }

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

        return rep;
    }

    //class to count values of derivations
    private static class Derivn implements DerivnFunction {

        private PlanetMap map;
        private Gravitationable curr;

        public Derivn(PlanetMap map, Gravitationable curr) {
            this.map = map;
            this.curr = curr;
        }

        private double proj(double ax, double ay, double az, double rad) {
            double c = ax / sqrt(ax * ax + ay * ay + az * az);

            return rad * c;
        }

        private double projX(double ax, double ay, double az, double rad) {
            return proj(ax, ay, az, rad);
        }

        private double projY(double ax, double ay, double az, double rad) {
            return proj(ay, az, ax, rad);
        }

        private double projZ(double ax, double ay, double az, double rad) {
            return proj(az, ax, ay, rad);
        }

        //main counting method
        public double[] derivn(double t, double[] var) {
            double[] deriv = new double[var.length];
            //first 3 derivs are coord' = speed, speed is last 3 var args
            deriv[0] = var[3];
            deriv[1] = var[4];
            deriv[2] = var[5];
            for (Gravitationable p : map.getElements()) {
                if (p.equals(curr)) {
                    continue;
                }
                double dx = var[0] - p.getCoords()[0];
                double dy = var[1] - p.getCoords()[1];
                double dz = var[2] - p.getCoords()[2];
                double rad = dx * dx + dy * dy + dz * dz;
                deriv[3] -= projX(dx, dy, dz, Const.G * p.getMass() / rad);
                deriv[4] -= projY(dx, dy, dz, Const.G * p.getMass() / rad);
                deriv[5] -= projZ(dx, dy, dz, Const.G * p.getMass() / rad);
            }

            return deriv;
        }
    }

}
