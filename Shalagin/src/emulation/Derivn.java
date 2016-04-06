package emulation;

import data.Const;
import data.Gravitationable;
import data.PlanetMap;
import data.Ship;
import flanagan.integration.DerivnFunction;

import static java.lang.Math.sqrt;

//class to count values of derivations
public class Derivn implements DerivnFunction {

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
            double dx = var[0] - p.getCoords().getX();
            double dy = var[1] - p.getCoords().getY();
            double dz = var[2] - p.getCoords().getZ();
            double rad = dx * dx + dy * dy + dz * dz;
            deriv[3] -= projX(dx, dy, dz, Const.G * p.getMass() / rad);
            deriv[4] -= projY(dx, dy, dz, Const.G * p.getMass() / rad);
            deriv[5] -= projZ(dx, dy, dz, Const.G * p.getMass() / rad);
        }
        if (curr instanceof Ship) {
            deriv[3] -= ((Ship) curr).getThrust().getX() / curr.getMass();
            deriv[4] -= ((Ship) curr).getThrust().getY() / curr.getMass();
            deriv[5] -= ((Ship) curr).getThrust().getZ() / curr.getMass();
        }

        return deriv;
    }
}
