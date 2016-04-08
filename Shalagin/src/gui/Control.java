package gui;

import data.Planet;
import data.PlanetMap;
import data.Ship;
import data_struct.Vec;
import emulation.EmulationReport;
import emulation.Emulator;
import io.Logger;
import io.Reader;
import io.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Control {
    View v;
    Timer updateTimer;

    PlanetMap map;
    Vec[] stars;
    Ship ship;
    EmulationReport rep;

    double stepLength;
    long numberOfSteps;

    public void start() {
        v = new View();
        initData();
        v.init();
        emulate();
//        rep = Reader.readReport("rep_8.3_11:9:52.rep");

//        v.rep = rep;
//        updateTimer = new Timer(100, v.adapter);
//        v.setDynamic();
//        updateTimer.start();

//        map = rep.getPlanetMap();
//        emulate();
//        System.out.println("done");
        v.rep = rep;
//        v.setStatic();
//        v.repaint();
        updateTimer = new Timer(100, v.adapter);
        v.setDynamic();
        updateTimer.start();
    }

    private void emulate() {
        rep = new Emulator().emulate(map, v.stars, numberOfSteps, stepLength);
        BufferedImage image = new BufferedImage(v.width, v.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, v.width, v.height);
        v.drawStatic(g, map);
        v.drawTrajectories(rep, g);
        Logger.writeResult(image);
        Logger.writeReport(rep);
    }

    public static void saveImg(EmulationReport rep, PlanetMap map) {
        View v = new View();
        v.init();
        BufferedImage image = new BufferedImage(v.width, v.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, v.width, v.height);
        v.drawTrajectories(rep, g);
        Logger.writeResult(image);
    }

    private void initData() {
        map = new PlanetMap();
        String filename = "";
        try {
            Scanner in = new Scanner(new File("settings"));
            filename = in.next();
            numberOfSteps = in.nextLong();
            stepLength = in.nextDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader.read(filename, map);
        v.stars = Reader.readStars(new File("radio.txt"));
        v.adapter = new ActionAdapter(v);
    }
}
