package data;

import data_struct.Vec;

public class Ship implements Gravitationable {
    private double mass, rad;
    private Vec speed, coords;
    private boolean isMovable, collided;

    public Ship(double x, double y, double z, double mass, double rad) {
        coords = new Vec(x, y, z);
        this.mass = mass;
        speed = new Vec(0, 0, 0);
        this.rad = rad;
    }

    public double getMass() {
        return mass;
    }

    public double getRad() {
        return rad;
    }

    public Vec getCoords() {
        return coords;
    }

    public Vec getSpeed() {
        return speed;
    }

    public void setCoords(Vec coords) {
        this.coords = coords;
    }

    public void setSpeed(Vec speed) {
        this.speed = speed;
    }

    public void setMovable(boolean isMovable) {
        this.isMovable = isMovable;
    }

    public boolean isMovable() {
        return isMovable;
    }

    public boolean collided() {
        return collided;
    }

    public void setCollided() {
        collided = true;
    }

    public boolean equals(Object o) {
        if (o instanceof Ship)
            return equals((Ship) o);
        return false;
    }

    public boolean equals(Ship p) {
        return coords.equals(p.coords) && mass == p.mass
                && speed.equals(p.speed);
    }

    public int hashCode() {
        return (int) (coords.hashCode() + speed.hashCode() * 6742);
    }

    public String toString() {
        return "[" + coords.getX() + "; " + coords.getY() + "; " +
                mass + "; " + speed.toString() + "]";
    }

}
