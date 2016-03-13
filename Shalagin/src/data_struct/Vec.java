package data_struct;

import java.io.Serializable;
import java.util.Arrays;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Vec implements Serializable {
    private double[] coords;

    public Vec(double x, double y, double z) {
        coords = new double[]{x, y, z};
    }

    public Vec(double[] coords) {
        if (coords.length != 3) {
            throw new IllegalArgumentException("Length must be 3");
        }
        this.coords = coords;
    }

    public double[] get() {
        return coords;
    }

    public double getX() {
        return coords[0];
    }

    public double getY() {
        return coords[1];
    }

    public double getZ() {
        return coords[2];
    }

    public double dist(Vec x) {
        return sqrt(pow(getX() - x.getX(), 2) + pow(getY() - x.getY(), 2)
                + pow(getZ() - x.getZ(), 2));
    }

    public double module() {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    public Vec clone() {
        return new Vec(coords);
    }

    public boolean equals(Object o) {
        return o instanceof Vec && equals((Vec) o);
    }

    public boolean equals(Vec t) {
        return coords[0] == t.coords[0] && coords[1] == t.coords[1] &&
                coords[2] == t.coords[2];
    }

    public int hashCode() {
        return (int) (coords[0] * 674 + coords[1] * 672 + coords[2] * 243);
    }

    public String toString() {
        return Arrays.toString(coords);
    }
}
