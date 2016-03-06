package data;

import data_struct.Triple;

import java.io.Serializable;

public class Planet implements Gravitationable, Serializable {

    private Triple coords;
    private Triple speed;
    private double mass, rad;
    private boolean isMovable;
    private int id;

    public Planet(double x, double y, double z, double mass,
                  double rad, boolean isMovable, int id) {
        init(x, y, z, mass, rad, isMovable, id);
    }

    public Planet(double x, double y, double z, double mass, double rad, double vx,
                  double vy, double vz, boolean isMovable, int id) {
        init(x, y, z, mass, rad, isMovable, id);
        speed = new Triple(vx, vy, vz);
    }

    private void init(double x, double y, double z, double mass,
                      double rad, boolean isMovable, int id) {
        coords = new Triple(x, y, z);
        this.mass = mass;
        this.isMovable = isMovable;
        speed = new Triple(0, 0, 0);
        this.id = id;
        this.rad = rad;
    }

    public double getX() {
        return coords.get()[0];
    }

    public double getY() {
        return coords.get()[1];
    }

    public double getZ() {
        return coords.get()[2];
    }

    public double[] getCoords() {
        return coords.get();
    }

    public double getMass() {
        return mass;
    }

    public double getRad() {
        return rad;
    }

    public void setX(double x) {
        coords.get()[0] = x;
    }

    public void setY(double y) {
        coords.get()[1] = y;
    }

    public void setZ(double z) {
        coords.get()[2] = z;
    }

    public double getVX() {
        return speed.get()[0];
    }

    public double getVY() {
        return speed.get()[1];
    }

    public double getVZ() {
        return speed.get()[2];
    }

    public void setVX(double vx) {
        speed.get()[0] = vx;
    }

    public void setVY(double vy) {
        speed.get()[1] = vy;
    }

    public void setVZ(double vz) {
        speed.get()[2] = vz;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public boolean equals(Object o) {
        if (o instanceof Planet)
            return equals((Planet) o);
        return false;
    }

//    public boolean equals(Planet p) {
//        return coords.get()[0] == p.coords.get()[0] &&
//                coords.get()[1] == p.coords.get()[1] &&
//                coords.get()[2] == p.coords.get()[2] && mass == p.mass;
//    }

    public boolean equals(Planet p) {
        return id == p.id;
    }

//    public int hashCode() {
//        return (int) ((coords.hashCode() * 672 + speed.hashCode() * 562
//                + mass * 784863) % Integer.MAX_VALUE);
//    }
    public int hashCode() {
        return id;
    }

    public String toString() {
        return "[" + coords.get()[0] + "; " + coords.get()[1] + "; " +
                coords.get()[2] + "; " + mass + "; " + isMovable + "]";
    }
}
