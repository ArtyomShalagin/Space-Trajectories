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

    public static EmulationReport emulate(PlanetMap map, long steps, double step) {
        long start = System.currentTimeMillis();

        EmulationReport rep = new EmulationReport(map);
//        long steps = 5500000000L;
//        int steps = 235872;
//        steps = 1000000;
//        int steps = 10;
        long points = 100000;
        long distBetweenPoints = max(steps / points, 1);
        double currTime = 0;
//        step = 10;
        for (long i = 0; i < steps; i++) {
            for (Gravitationable g : rep.getElements()) {
                calc(map, g, currTime, step);
                if (i % distBetweenPoints == 0) {
                    //add new point to g's traj
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

        //default OX projection
        private double proj(double ax, double ay, double az, double rad) {
            //module of the vector
            double a = sqrt(ax * ax + ay * ay + az * az);
            //angle between vector and XZ plane
            double angleXZ = atan2(ay, sqrt(ax * ax + az * az));
            //angle between projection on XZ and X
            double angleX = atan2(az, ax);
            //result is d_x = |d| * cos(phi) * cos(eta)
            double proj = rad * cos(angleXZ) * cos(angleX);

            return proj;
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
