package data_struct;

import java.io.Serializable;
import java.util.Arrays;

public class Triple implements Serializable {
    private double[] val;

    public Triple(double x, double y, double z) {
        val = new double[]{x, y, z};
    }

    public Triple(double[] val) {
        if (val.length != 3) {
            throw new IllegalArgumentException("Length must be 3");
        }
        this.val = val;
    }

    public double[] get() {
        return val;
    }

    public boolean equals(Object o) {
        if (o instanceof Triple) {
            return equals((Triple) o);
        }
        return false;
    }

    public boolean equals(Triple t) {
        return val[0] == t.val[0] && val[1] == t.val[1] &&
                val[2] == t.val[2];
    }

    public int hashCode() {
        return (int) (val[0] * 674 + val[1] * 672 + val[2] * 243);
    }

    public String toString() {
        return Arrays.toString(val);
    }
}
