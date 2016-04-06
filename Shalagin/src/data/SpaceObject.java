package data;

import data_struct.Vec;

import java.io.Serializable;

public abstract class SpaceObject implements Gravitationable, Serializable {
    private Vec coords;
    private Vec speed;
    private double mass, rad;
    private boolean isMovable, collided;
    private int id;

    protected void init(Vec coords, double mass, double rad, Vec speed, boolean isMovable, int id) {
        this.coords = coords;
        this.mass = mass;
        this.isMovable = isMovable;
        this.speed = speed;
        this.id = id;
        this.rad = rad;
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

    public double getMass() {
        return mass;
    }

    public double getRad() {
        return rad;
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

    public int getId() {
        return id;
    }

    public boolean equals(Object o) {
        if (o instanceof SpaceObject)
            return equals((SpaceObject) o);
        return false;
    }

    public boolean equals(SpaceObject p) {
        return id == p.id;
    }

    public int hashCode() {
        return id;
    }

    public String toString() {
        return "[" + coords.get()[0] + "; " + coords.get()[1] + "; " +
                coords.get()[2] + "; " + mass + "; " + isMovable + "]";
    }
}
