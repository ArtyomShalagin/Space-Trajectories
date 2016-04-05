package data_struct;

import java.io.Serializable;
import java.util.Arrays;

import static java.lang.Math.acos;
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

    public Vec minus(Vec v) {
        return new Vec(getX() - v.getX(), getY() - v.getY(), getZ() - v.getZ());
    }

    public double module() {
//        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        return sqrt(scalarMult(this, this));
    }

    public static double dist(Vec x1, Vec x2) {
        return sqrt(pow(x1.getX() - x2.getX(), 2) + pow(x1.getY() - x2.getY(), 2)
                + pow(x1.getZ() - x2.getZ(), 2));
    }

    public static double scalarMult(Vec x1, Vec x2) {
        return x1.getX() * x2.getX() + x1.getY() * x2.getY() + x1.getZ() * x2.getZ();
    }

    public static Vec vecMult(Vec x1, Vec x2) {
        double x = x1.getY() * x2.getZ() - x1.getZ() * x2.getY();
        double y = x1.getZ() * x2.getX() - x1.getX() * x2.getZ();
        double z = x1.getX() * x2.getY() - x1.getY() * x2.getX();

        return new Vec(x, y, z);
    }

    public static double angle(Vec x1, Vec x2) {
        return acos(scalarMult(x1, x2) / (x1.module() * x2.module()));
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
