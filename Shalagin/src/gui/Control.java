package gui;

import data.PlanetMap;
import data.Ship;
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
    Model m;
    Timer updateTimer;

    PlanetMap map;
    Ship ship;
    EmulationReport rep;

    double stepLength;
    long numberOfSteps;

    public void start() {
        v = new View();
        m = new Model();
        initData();
        v.init();
        emulate();
        v.rep = rep;
//        v.setStatic();
//        v.repaint();
        updateTimer = new Timer(100, v.adapter);
        v.setDynamic();
        updateTimer.start();
    }

    private void emulate() {
        rep = Emulator.emulate(map, numberOfSteps, stepLength);
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
        v.adapter = new ActionAdapter(v);
    }
}
