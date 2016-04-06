package data;

import data_struct.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class PlanetMap implements Serializable {
    private ArrayList<Planet> planets;
    private ArrayList<Ship> ships;
    private ArrayList<Gravitationable> elements;
    private Random rnd;
    private Pair<Double, Double> boundsX;
    private Pair<Double, Double> boundsY;
    private Pair<Double, Double> boundsZ;

    public PlanetMap() {
        elements = new ArrayList<>();
        planets = new ArrayList<>();
        ships = new ArrayList<>();
        rnd = new Random(566239);
        boundsX = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
        boundsY = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
        boundsZ = new Pair<>(Double.MAX_VALUE, Double.MIN_VALUE);
    }

    public void addPlanet(Planet p) {
        planets.add(p);
        elements.add(p);
    }

    public void setPlanets(ArrayList<Planet> planets) {
        elements.removeAll(this.planets);
        this.planets = planets;
        elements.addAll(this.planets);
    }

    public ArrayList<Planet> getPlanets() {
        return planets;
    }

    public void addShip(Ship s) {
        ships.add(s);
        elements.add(s);
    }

    public void setShips(ArrayList<Ship> ships) {
        elements.removeAll(this.ships);
        this.ships = ships;
        elements.addAll(this.ships);
    }

    public ArrayList<Ship> getShips() {
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

    public PlanetMap clone() {
        PlanetMap ans = new PlanetMap();
        for (Planet p : planets) {
            ans.addPlanet(p.clone());
        }
        for (Ship ship : ships) {
            ans.addShip(ship.clone());
        }

        return ans;
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
