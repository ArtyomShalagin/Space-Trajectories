package emulation;

import data.Gravitationable;
import data.Planet;
import data.PlanetMap;
import data.Trajectory;
import data_struct.Vec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class EmulationReport implements Serializable {
    public long versionID = 0;

    private ArrayList<Gravitationable> elements;
    private HashMap<Gravitationable, Trajectory> map;
    private PlanetMap planetMap;

    public EmulationReport(PlanetMap m) {
        planetMap = m;
        elements = new ArrayList<>();
        map = new HashMap<>();
        m.getElements().forEach(g -> {
            if (g.isMovable()) {
                elements.add(g);
                map.put(g, new Trajectory());
            }
        });
    }

    public void add(Gravitationable g, Vec coord) {
        Trajectory tr = map.get(g);
        tr.add(coord);
    }

    public PlanetMap getPlanetMap() {
        return planetMap;
    }
    
    public ArrayList<Gravitationable> getElements() {
        return elements;
    }

    public Trajectory getTrajectory(Gravitationable g) {
        return map.get(g);
    }

}
