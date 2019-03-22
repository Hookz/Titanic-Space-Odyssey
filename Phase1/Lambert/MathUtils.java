package Lambert;
import java.io.*;


public class MathUtils implements Serializable {

    public MathUtils() {
    }

    public static double sign(double a, double b) {
        double amag = Math.abs(a);
        double out = amag;
        if (b < 0.0)
            out = -1.0 * amag;
        return out;
    }

}
