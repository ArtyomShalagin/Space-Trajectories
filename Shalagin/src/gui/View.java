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
    long numberOfSteps;
    double observableSpaceSize = 6e8;
    double stepLength;
    JFrame frame;
    JPanel drawPanel, settingsPanel;
    JSlider observableSlider, speedSlider;
    JButton playButton;

    PlanetMap map;
    Ship ship;
    Timer updateTimer;
    ActionAdapter adapter;

    EmulationReport rep;

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
//        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.X_AXIS));
        settingsPanel.setLayout(null);

        observableSlider = new JSlider();
        settingsPanel.add(observableSlider);
        observableSlider.setBounds(0, 15, width / 3, 20);
        observableSlider.setMinimum(10);
        observableSlider.setMaximum(1000);
        observableSlider.setValue((int) (observableSpaceSize / 1e6));
        observableSlider.addChangeListener(adapter);
//        System.out.println("Slider set");

//        speedSlider = new JSlider();
//        settingsPanel.add(speedSlider);
//        speedSlider.setBounds(width / 3 + 5, 15, width / 3, 20);
//        speedSlider.setMinimum(0);
//        speedSlider.setMaximum(100);
//        speedSlider.addChangeListener(adapter);
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
        adapter = new ActionAdapter(this);
//        updateTimer = new Timer(100, adapter);
//        updateTimer.start();

        System.out.println("Data inited");
    }

    private void emulate() {
        rep = Emulator.emulate(map, numberOfSteps, stepLength);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        drawStatic(g);
        drawTrajectories(rep, g);
        Logger.writeResult(image);
        Logger.writeReport(rep);

        drawPanel.repaint();
    }

    private void init() {
        initFrame();
        initData();
        initPanels();
        emulate();
    }

    protected void redraw(Graphics g2) {
        if (rep == null) {
            g2.drawString("Emulating...", width / 2 - 30, height / 2);
            return;
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        drawStatic(g);

        drawTrajectories(rep, g);

        g2.drawImage(image, 0, 0, width, height, null);
    }

    private void drawTrajectories(EmulationReport rep, Graphics g) {
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
                maxR = Math.max(r,maxR);
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
    private void drawStatic(Graphics g) {
        map.getElements().forEach(p ->  {
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

    public void start() {
        init();
    }
}
