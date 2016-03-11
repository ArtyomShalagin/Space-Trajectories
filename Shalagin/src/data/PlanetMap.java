package data;

import data_struct.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlanetMap implements Serializable {
    private ArrayList<Gravitationable> elements;
    private Random rnd;
    private Pair<Double, Double> boundsX;
    private Pair<Double, Double> boundsY;
    private Pair<Double, Double> boundsZ;

    public PlanetMap() {
        elements = new ArrayList<>();
        rnd = new Random();
        boundsX = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
        boundsY = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
        boundsZ = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    public void generateRandom(int seed, int n) {
        rnd = new Random(seed);
        for (int i = 0; i < n; i++) {
            Planet p = new Planet(rnd.nextDouble() * 400000000,
                    rnd.nextDouble() * 400000000, 0, rnd.nextDouble() * 6 * 1e24, 1e4, true, i);
            rebound(p);
            elements.add(p);
        }
    }

    public void add(Planet p) {
        rebound(p);
        elements.add(p);
    }

    public void set(int ind, Planet p) {
        if (ind >= elements.size()) {
            throw new IndexOutOfBoundsException("Index bigger than amount of elements");
        }
        rebound(p);
        elements.set(ind, p);
    }

    public void set(ArrayList<Gravitationable> newelements) {
        elements = newelements;
    }

    public ArrayList<Gravitationable> getElements() {
        return elements;
    }

    public Pair<Double, Double> getBoundsX() {
        return boundsX;
    }

    public Pair<Double, Double> getBoundsY() {
        return boundsY;
    }

    public Pair<Double, Double> getBoundsZ() {
        return boundsZ;
    }

    private void rebound(Planet p) {
//        boundsX.set1(Math.min(boundsX.get1(), p.getX() * 2));
//        boundsY.set1(Math.min(boundsY.get1(), p.getY() * 2));
//        boundsZ.set1(Math.min(boundsZ.get1(), p.getZ() * 2));
//        boundsX.set2(Math.max(boundsX.get2(), p.getX() * 2));
//        boundsY.set2(Math.max(boundsY.get2(), p.getY() * 2));
//        boundsZ.set2(Math.max(boundsZ.get2(), p.getZ() * 2));
//
//        boundsX.set1(Math.min(0, boundsX.get1()));
//        boundsY.set1(Math.min(0, boundsY.get1()));
//        boundsZ.set1(Math.min(0, boundsZ.get1()));
    }

    public boolean equals(Object o) {
        if (o instanceof PlanetMap)
            return equals((PlanetMap) o);
        return false;
    }

    public boolean equals(PlanetMap p) {
        return elements.equals(p.elements);
    }

    public int hashCode() {
        return elements.hashCode() * 87424 + rnd.hashCode() * 63152;
    }

    public String toString() {
        return elements.toString();
    }
}
