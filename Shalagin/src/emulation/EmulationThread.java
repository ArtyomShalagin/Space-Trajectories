package emulation;

import data.PlanetMap;
import data_struct.Vec;

public class EmulationThread implements Runnable {
    private PlanetMap map;
    private Vec[] stars;
    private long steps;
    private double step;
    private int id;
    private final EmulationReport[] result;

    public EmulationThread(PlanetMap map, Vec[] stars, long steps, double step, int id, EmulationReport[] result) {
        this.map = map;
        this.stars = stars;
        this.steps = steps;
        this.step = step;
        this.result = result;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void run() {
        EmulationReport rep = new Emulator().emulate(map, stars, steps, step);
        synchronized (result) {
            result[id] = rep;
        }
        System.out.println("Thread " + id + " done");
    }
}
