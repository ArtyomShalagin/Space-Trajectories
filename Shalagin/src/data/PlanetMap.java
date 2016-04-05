package data;

import data_struct.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlanetMap implements Serializable {
    private ArrayList<Gravitationable> planets;
    private ArrayList<Gravitationable> ships;
    private ArrayList<Gravitationable> elements;
    private Random rnd;
    private Pair<Double, Double> boundsX;
    private Pair<Double, Double> boundsY;
    private Pair<Double, Double> boundsZ;

    public PlanetMap() {
        elements = new ArrayList<>();
        planets = new ArrayList<>();
        ships = new ArrayList<>();
        rnd = new Random();
        boundsX = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
        boundsY = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
        boundsZ = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    public void addPlanet(Planet p) {
        rebound(p);
        planets.add(p);
        elements.add(p);
    }

    public void setPlanets(ArrayList<Gravitationable> planets) {
        elements.removeAll(this.planets);
        this.planets = planets;
        elements.addAll(this.planets);
    }

    public ArrayList<Gravitationable> getPlanets() {
        return planets;
    }

    public void addShip(Ship s) {
        ships.add(s);
        elements.add(s);
    }

    public void setShips(ArrayList<Gravitationable> ships) {
        elements.removeAll(this.ships);
        this.ships = ships;
        elements.addAll(this.ships);
    }

    public ArrayList<Gravitationable> getShips() {
        return ships;
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
        return o instanceof PlanetMap && equals((PlanetMap) o);
    }

    public boolean equals(PlanetMap p) {
        return planets.equals(p.planets) && ships.equals(p.ships);
    }

    public int hashCode() {
        return planets.hashCode() * 87424 + ships.hashCode() * 63152;
    }

    public String toString() {
        return planets.toString() + " " + ships.toString();
    }
}
