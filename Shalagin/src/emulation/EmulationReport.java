package emulation;

import data.Gravitationable;
import data.PlanetMap;
import data.Trajectory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class EmulationReport implements Serializable {
    public long versionID = 0;

    private ArrayList<Gravitationable> elements;
    private HashMap<Gravitationable, Trajectory> map;

    public EmulationReport(PlanetMap m) {
        elements = new ArrayList<>();
        map = new HashMap<>();
        m.getElements().forEach(g -> {
            if (g.isMovable()) {
                elements.add(g);
                map.put(g, new Trajectory());
            }
        });
    }

    public void add(Gravitationable g, double[] coord) {
        Trajectory tr = map.get(g);
        tr.add(coord);
    }

    public void add(Gravitationable g, double x, double y, double z) {
        Trajectory tr = map.get(g);
        tr.add(x, y, z);
    }
    
    public ArrayList<Gravitationable> getElements() {
        return elements;
    }

    public Trajectory getTrajectory(Gravitationable g) {
        return map.get(g);
    }

}
