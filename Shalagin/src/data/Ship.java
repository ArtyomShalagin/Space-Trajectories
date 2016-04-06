package data;

import data_struct.Vec;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Ship extends SpaceObject {
    private ArrayList<Vec> thrust;
    private long totalSteps, doneSteps;
    private Vec startSpeed;

    public Ship(Vec coords, double mass, double rad, boolean isMovable, int id) {
        init(coords, mass, rad, new Vec(0, 0, 0), isMovable, id);
    }

    public Ship(Vec coords, double mass, double rad, Vec speed, boolean isMovable, int id) {
        init(coords, mass, rad, speed, isMovable, id);
        thrust = new ArrayList<>(Collections.singletonList(new Vec(0, 0, 0)));
    }

    public void setThrust(ArrayList<Vec> thrust) {
        this.thrust = thrust;
    }

    public void setTotalSteps(long totalSteps) {
        this.totalSteps = totalSteps;
        doneSteps = 0;
    }

    public void setDoneSteps(long doneSteps) {
        this.doneSteps = doneSteps;
    }

    public void setStartSpeed(Vec startSpeed) {
        this.startSpeed = startSpeed;
    }

    public Vec getStartSpeed() {
        return startSpeed;
    }

    /**
     * Returns current thrust vector getting the finished part of the expedition
     *
     * @param done double from 0 to 1 which represents the finished part
     * @throws IllegalArgumentException if done < 0 or done > 1
     */
    public Vec getThrust(double done) {
        if (done < 0 || done > 1) {
            throw new IllegalArgumentException("done must be: 0 <= done <= 1, got " + done);
        }
        int index = (int) ((thrust.size() - 1) * done);
        return thrust.get(index);
    }

    public Vec getThrust() {
        return getThrust((double) doneSteps / totalSteps);
    }

    public ArrayList<Vec> getThrustArr() {
        return thrust;
    }

    public Ship clone() {
        Ship ans = new Ship(getCoords().clone(), getMass(), getRad(),
                getSpeed().clone(), isMovable(), getId());
        ans.setTotalSteps(totalSteps);
        ans.setDoneSteps(doneSteps);
        ans.setStartSpeed(startSpeed.clone());
        ArrayList<Vec> thrustCopy = new ArrayList<>();
        if (thrust != null) {
            for (Vec v : thrust) {
                thrustCopy.add(v.clone());
            }
        }
        ans.setThrust(thrustCopy);
        return ans;
    }
}
