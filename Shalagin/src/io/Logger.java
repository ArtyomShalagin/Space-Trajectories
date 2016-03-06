package io;

import emulation.EmulationReport;
import data.Trajectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class Logger {

    public static void writeReport(EmulationReport rep) {
        String name = "rep/rep_" + getTime() + ".rep";
        File f = new File("rep");
        f.mkdirs();
        serialize(rep, name);
    }

    public static void writeResult(BufferedImage image) {
        try {
            File f = new File("traj");
            f.mkdirs();
            ImageIO.write(image, "png", new File("traj/orbit_" + getTime() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void serialize(Serializable source, String destFilename) {
        try {
            FileOutputStream fos = new FileOutputStream(destFilename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(source);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getTime() {
        Calendar cal = Calendar.getInstance();
        String time = cal.get(Calendar.DAY_OF_MONTH) + "." +
                cal.get(Calendar.MONTH) + "_" + cal.get(Calendar.HOUR_OF_DAY) +
                ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
        return time;
    }
}
