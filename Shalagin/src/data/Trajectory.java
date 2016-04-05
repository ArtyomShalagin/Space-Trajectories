package data;

import data_struct.Vec;

import java.io.Serializable;
import java.util.ArrayList;

public class Trajectory implements Serializable {
    private ArrayList< Vec > traj;
    private ArrayList< Double > times;

    public Trajectory() {
        traj = new ArrayList<>();
        times = new ArrayList<>();
    }

    public void add(double x, double y, double z) {
        traj.add(new Vec(x, y, z));
    }

    public void add(Vec coord) {
        traj.add(coord);
    }

    public ArrayList< Vec > getPoints() {
        return traj;
    }

    public Vec getPoint(int ind) {
        return traj.get(ind);
    }

    public int size() {
        return traj.size();
    }

    public boolean equals(Object o) {
        return o instanceof Trajectory && equals((Trajectory) o);
    }

    public boolean equals(Trajectory t) {
        return traj.equals(t.traj) && times.equals(t.times);
    }

    public int hashCode() {
        return traj.hashCode() * 642 + times.hashCode() * 542;
    }

    public String toString() {
        return traj.toString();
    }
}
