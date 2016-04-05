package data;

import data_struct.Vec;

import java.io.Serializable;

public class Ship extends SpaceObject {

    public Ship(Vec coords, double mass, double rad, boolean isMovable, int id) {
        init(coords, mass, rad, new Vec(0, 0, 0), isMovable, id);
    }

    public Ship(Vec coords, double mass, double rad, Vec speed, boolean isMovable, int id) {
        init(coords, mass, rad, speed, isMovable, id);
    }
}
