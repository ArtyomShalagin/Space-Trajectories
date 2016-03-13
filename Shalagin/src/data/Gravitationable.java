package data;

import data_struct.Vec;

public interface Gravitationable {
    double getMass();
    double getRad();
    Vec getCoords();
    Vec getSpeed();
    void setCoords(Vec coords);
    void setSpeed(Vec speed);
    boolean isMovable();
    boolean collided();
    void setCollided();
}
