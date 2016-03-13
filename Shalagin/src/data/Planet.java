package data;

import data_struct.Vec;

import java.io.Serializable;

public class Planet implements Gravitationable, Serializable {

    private Vec coords;
    private Vec speed;
    private double mass, rad;
    private boolean isMovable, collided;
    private int id;

    public Planet(Vec coords, double mass, double rad, boolean isMovable, int id) {
        init(coords, mass, rad, isMovable, id);
    }

    public Planet(Vec coords, double mass, double rad, Vec speed, boolean isMovable, int id) {
        init(coords, mass, rad, isMovable, id);
        this.speed = speed;
    }

    private void init(Vec coords, double mass, double rad, boolean isMovable, int id) {
        this.coords = coords;
        this.mass = mass;
        this.isMovable = isMovable;
        speed = new Vec(0, 0, 0);
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

    public boolean equals(Object o) {
        if (o instanceof Planet)
            return equals((Planet) o);
        return false;
    }

    public boolean equals(Planet p) {
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
