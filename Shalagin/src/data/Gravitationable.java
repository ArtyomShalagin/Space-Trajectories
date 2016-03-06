package data;

public interface Gravitationable {
    double getMass();
    double getX();
    double getY();
    double getZ();
    double getVX();
    double getVY();
    double getVZ();
    double getRad();
    void setX(double d);
    void setY(double d);
    void setZ(double d);
    void setVX(double d);
    void setVY(double d);
    void setVZ(double d);
    double[] getCoords();
    boolean isMovable();
}
