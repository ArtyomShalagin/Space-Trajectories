import data.PlanetMap;
import data_struct.Vec;
import emulation.EmulationReport;
import gui.Control;
import io.Logger;
import io.Reader;
import io.Scanner;
import optimization.Genetic;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
//        Vec v1 = new Vec(1.0799996972768029E12, 8.399998863242154E8, 4.500796859925989E15);
//        Vec v2 = new Vec(0.0, 0.0, 4.5E15);
//        System.out.println(Vec.vecMult(v1, v2));
//        System.out.println(Vec.angle(v1, v2));

        new Control().start();
//        genetics();
    }

    public static void genetics() {
        System.out.println("Reading data");
        PlanetMap map = new PlanetMap();
        long numberOfSteps = 0;
        double stepLength = 0;
        int generations = 0;

        String filename = "";
        try {
            Scanner in = new Scanner(new File("settings"));
            filename = in.next();
            numberOfSteps = in.nextLong();
            stepLength = in.nextDouble();
            generations = in.nextInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reader.read(filename, map);
        Vec[] stars = Reader.readStars(new File("radio.txt"));
//        System.out.println("Starting 1+1");
//        EmulationReport rep = Genetic.onePlusOne(map, stars, numberOfSteps, stepLength, generations);
//        System.out.println("Done 1+1");
        System.out.println("Starting mu+lambda");
        EmulationReport rep = Genetic.muPlusLambda(map, stars, numberOfSteps, stepLength, generations);
        System.out.println("Done mu+lambda");

        Logger.writeReport(rep);
    }
}
