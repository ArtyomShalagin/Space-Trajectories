package io;

import data.Planet;
import data.PlanetMap;
import data_struct.Vec;
import emulation.EmulationReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Reader {
    public static void read(String filename, PlanetMap map) {
        try {
            Scanner sc = new Scanner(new File(filename));
            int id = 0;
            while (sc.hasNext()) {
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                double z = sc.nextDouble();
                double mass = sc.nextDouble();
                double rad = sc.nextDouble();
                double vx = sc.nextDouble();
                double vy = sc.nextDouble();
                double vz = sc.nextDouble();
                map.add(new Planet(new Vec(x, y, z), mass, rad,
                        new Vec(vx, vy, vz), sc.nextBoolean(), id++));
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static EmulationReport readReport(String filename) {
        EmulationReport rep = null;
        try {
            FileInputStream fis = new FileInputStream("rep/" + filename);
            ObjectInputStream oin = new ObjectInputStream(fis);
            rep = (EmulationReport) oin.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return rep;
    }
}
