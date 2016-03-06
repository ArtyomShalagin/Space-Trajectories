package data;

import data_struct.Triple;
import data_struct.TripleT;

import java.io.Serializable;
import java.util.ArrayList;

public class Trajectory implements Serializable {
    private ArrayList<Triple> traj;
    private ArrayList< Double > times;

    public Trajectory() {
        traj = new ArrayList<>();
        times = new ArrayList<>();
    }

    public void add(double x, double y, double z) {
        traj.add(new Triple(x, y, z));
    }

    public void add(double[] coord) {
        if (coord.length != 3) {
            throw new IllegalArgumentException("Coord length must be 3");
        }
        traj.add(new Triple(coord));
    }

    public ArrayList< Triple > getPoints() {
        return traj;
    }

    public double[] getPoint(int ind) {
        return traj.get(ind).get();
    }

    public boolean equals(Object o) {
        if (o instanceof Trajectory) {
            return equals((Trajectory) o);
        }
        return false;
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
