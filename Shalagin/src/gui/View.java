package gui;

import com.sun.org.apache.xerces.internal.impl.xpath.XPath;
import data.*;
import data_struct.Pair;
import data_struct.Triple;
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

public class View {

    int width, height;
    double observableSpaceSize = 6e8;

    JFrame frame;
    JPanel drawPanel, settingsPanel;
    JSlider observableSlider, speedSlider;
    JButton playButton;

    ActionAdapter adapter;

    EmulationReport rep;

    PaintingType state;

    int dynamicDrawingInd;
    int dynamicStep = 100;

    private void initFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        width = 800;
        height = 850;
        frame.setPreferredSize(new Dimension(width, height));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLayout(null);

        System.out.println("Frame inited");
    }

    private void initPanels() {
        settingsPanel = new JPanel();
        frame.add(settingsPanel);
        settingsPanel.setBounds(0, 0, width, 50);
        settingsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        settingsPanel.setLayout(null);

        observableSlider = new JSlider();
        settingsPanel.add(observableSlider);
        observableSlider.setBounds(0, 15, width / 3, 20);
        observableSlider.setMinimum(10);
        observableSlider.setMaximum(1000);
        observableSlider.setValue((int) (observableSpaceSize / 1e6));
        observableSlider.addChangeListener(adapter);

        speedSlider = new JSlider();
        settingsPanel.add(speedSlider);
        speedSlider.setBounds(width / 3 + 5, 15, width / 3, 20);
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(1000);
        speedSlider.setValue(dynamicStep);
        speedSlider.addChangeListener(adapter);
//
//        playButton = new JButton("Play");
//        settingsPanel.add(playButton);
//        playButton.setBounds(width * 2 / 3, 15, width / 3 - 20, 20);
//        playButton.setVisible(true);
//        playButton.addActionListener(adapter);


        drawPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                redraw(g);
            }
        };
        frame.add(drawPanel);
        drawPanel.setVisible(true);
        drawPanel.setBounds(0, 50, width, height - 50);

        System.out.println("Panels inited");
    }

    protected void init() {
        initFrame();
        initPanels();
    }

    private void drawStaticTraj(Graphics g) {
//        if (rep == null) {
//            g.drawString("Emulation not ready...", width / 2 - 30, height / 2);
//            return;
//        }

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        drawStatic(g, rep.getPlanetMap());
        drawTrajectories(rep, g);
    }

    private void drawDynamicTraj(Graphics g) {
        dynamicDrawingInd += dynamicStep;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        drawStatic(g, rep.getPlanetMap());
        g.setColor(Color.BLACK);

        Pair<Double, Double> zVal = getMaxZ(rep);
        double dif = zVal.get2() - zVal.get1();
        for (Gravitationable grav : rep.getElements()) {
            //in case of collision trajectory may be short
            if (rep.getTrajectory(grav).size() > dynamicDrawingInd) {
                double[] coord = rep.getTrajectory(grav).getPoint(dynamicDrawingInd);
                int size = resize(grav.getRad() * 2, 0) - width / 2;
                size = Math.max(size, 5);
                g.setColor(new Color(
                        Math.min((int) (255. * (coord[2] - zVal.get1()) / dif), 255), 0, 0));
                g.fillOval(resize(coord[0], 0) - size / 2,
                        resize(coord[1], 1) - size / 2, size, size);
                int bound = 5000;
                for (int i = dynamicDrawingInd;
                     i >= Math.max(dynamicDrawingInd - bound, 0);
                     i -= Math.max(dynamicStep / 10, 1)) {
                    coord = rep.getTrajectory(grav).getPoint(i);
                    g.fillOval(resize(coord[0], 0), resize(coord[1], 1), 1, 2);
                }
            }
        }

    }

    protected void redraw(Graphics g) {
        if (rep == null) {
            g.drawString("Emulation not ready...", width / 2 - 30, height / 2);
            return;
        }

        if (state == PaintingType.STATIC) {
            drawStaticTraj(g);
        } else if (state == PaintingType.DYNAMIC) {
            drawDynamicTraj(g);
        }
    }

    protected void repaint() {
        drawPanel.repaint();
    }

    protected void setStatic() {
        state = PaintingType.STATIC;
    }

    protected void setDynamic() {
        state = PaintingType.DYNAMIC;
    }

    protected void drawTrajectories(EmulationReport rep, Graphics g) {
        Pair<Double, Double> zVal = getMaxZ(rep);
        double dif = zVal.get2() - zVal.get1();
        double maxR = Double.MIN_VALUE, minR = Double.MAX_VALUE;
        for (Gravitationable gr : rep.getElements()) {
            for (Triple p : rep.getTrajectory(gr).getPoints()) {
                double[] c = p.get();
                g.setColor(new Color(Math.min((int) (255. * (c[2] - zVal.get1()) / dif), 255), 0, 0));
                g.fillOval(resize(c[0], 0), resize(c[1], 1), 1, 2);

                double r = Math.sqrt(c[0] * c[0] + c[1] * c[1] + c[2] * c[2]);
                minR = Math.min(r, minR);
                maxR = Math.max(r, maxR);
            }
        }
//        System.out.println("min R = " + minR + ", max R = " + maxR);
    }

    private Pair<Double, Double> getMaxZ(EmulationReport rep) {
        double minz = Double.MAX_VALUE, maxz = Double.MIN_VALUE;
        for (Gravitationable gr : rep.getElements()) {
            for (Triple p : rep.getTrajectory(gr).getPoints()) {
                double[] c = p.get();
                maxz = Math.max(maxz, c[2]);
                minz = Math.min(minz, c[2]);
            }
        }
        return new Pair<>(minz, maxz);
    }

    //draws immovable objects
    protected void drawStatic(Graphics g, PlanetMap map) {
        map.getElements().forEach(p -> {
            if (!p.isMovable()) {
                g.setColor(Color.BLUE);
                int size = resize(p.getRad() * 2, 0) - width / 2;
                g.fillOval(resize(p.getX(), 0) - size / 2,
                        resize(p.getY(), 1) - size / 2, size, size);
            }
        });
    }

    //type 0 for X, 1 for Y, 2 for Z
    private int resize(double coord, int type) {
        double screenSize = width;

        double min = -observableSpaceSize;
        double max = observableSpaceSize;

        double dif = max - min;
        int result = (int) (screenSize * (coord - min) / dif);

        return result;
    }

    private enum PaintingType {
        STATIC, DYNAMIC;
    }
}
