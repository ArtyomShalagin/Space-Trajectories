package optimization;

import data.Ship;

import java.util.ArrayList;

public class Genotype implements Comparable<Genotype> {
    private ArrayList<Ship> data;
    private int fit;

    public Genotype(ArrayList<Ship> data) {
        this.data = data;
    }

    public void setData(ArrayList<Ship> data) {
        this.data = data;
    }

    public ArrayList<Ship> getData() {
        return data;
    }

    public void setFit(int fit) {
        this.fit = fit;
    }

    public int getFit() {
        return fit;
    }

    public int size() {
        return data.size();
    }

    public boolean equals(Object o) {
        return o instanceof Genotype && equals((Genotype) o);
    }

    public boolean equals(Genotype g) {
        if (data.size() != g.data.size()) {
            return false;
        }
        boolean eq = true;
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).equals(g.data.get(i))) {
                eq = false;
            }
        }

        return eq;
    }

    public int hashCode() {
        int hash = 0;
        for (Ship ship : data) {
            hash += 572367 * ship.hashCode() + 6742;
        }

        return hash;
    }

    public Genotype clone() {
        ArrayList<Ship> dataClone = new ArrayList<>();
        for (Ship ship : data) {
            dataClone.add(ship.clone());
        }

        return new Genotype(dataClone);
    }

    @Override
    public int compareTo(Genotype o) {
        return this.fit - o.fit;
    }
}
