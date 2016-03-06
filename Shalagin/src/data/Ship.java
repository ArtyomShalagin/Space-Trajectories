package data;

import data_struct.Triple;

public class Ship implements Gravitationable {
    private double mass, rad;
    private Triple speed, coords;
    private boolean isMovable;

    public Ship(double x, double y, double z, double mass, double rad) {
        coords = new Triple(x, y, z);
        this.mass = mass;
        speed = new Triple(0, 0, 0);
        this.rad = rad;
    }

    public double[] getCoords() {
        return coords.get();
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

    public double getMass() {
        return mass;
    }

    public double getRad() {
        return rad;
    }

    public double[] getV() {
        return speed.get();
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

    public void setX(double x) {
        coords.get()[0] = x;
    }

    public void setY(double y) {
        coords.get()[1] = y;
    }

    public void setZ(double z) {
        coords.get()[2] = z;
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
        if (o instanceof Ship)
            return equals((Ship) o);
        return false;
    }

    public boolean equals(Ship p) {
        return coords.get().equals(p.coords.get()) && mass == p.mass
                && speed.equals(p.speed);
    }

    public int hashCode() {
        return (int) (coords.get().hashCode() + speed.hashCode() * 6742);
    }

    public String toString() {
        return "[" + coords.get()[0] + "; " + coords.get()[1] + "; " +
                mass + "; " + speed.toString() + "]";
    }

}
