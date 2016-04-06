package io;

import data.Planet;
import data.PlanetMap;
import data.Ship;
import data_struct.Vec;
import emulation.EmulationReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Reader {
    public static void read(String filename, PlanetMap map) {
        try {
            Scanner sc = new Scanner(new File(filename));
            int id = 0;
            while (sc.hasNext()) {
                String type = sc.next();
                double x = sc.nextDouble();
                double y = sc.nextDouble();
                double z = sc.nextDouble();
                double mass = sc.nextDouble();
                double rad = sc.nextDouble();
                double vx = sc.nextDouble();
                double vy = sc.nextDouble();
                double vz = sc.nextDouble();
                switch (type) {
                    case "planet" : map.addPlanet(new Planet(new Vec(x, y, z), mass, rad,
                            new Vec(vx, vy, vz), sc.nextBoolean(), id++)); break;
                    case "ship" :
                        Ship ship = new Ship(new Vec(x, y, z), mass, rad,
                                new Vec(vx, vy, vz), sc.nextBoolean(), id++);
                        ship.setStartSpeed(ship.getSpeed().clone());
                        map.addShip(ship);
                }
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Vec[] readStars(File f) {
        try {
            Scanner in = new Scanner(f);
            ArrayList<Vec> stars = new ArrayList<>();
            while (in.hasNext()) {
                in.next();
                double ra = toRadians(in.nextDouble());
                double dec = PI / 2 - toRadians(in.nextDouble());
                double x = sin(dec) * cos(ra);
                double y = sin(dec) * sin(ra);
                double z = cos(dec);
                stars.add(new Vec(x, y, z));
            }
            Vec[] ans = new Vec[stars.size()];
            for (int i = 0; i < stars.size(); i++) {
                ans[i] = stars.get(i);
            }
            in.close();
            return ans;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
