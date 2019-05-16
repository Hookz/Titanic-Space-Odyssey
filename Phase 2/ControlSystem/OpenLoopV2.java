package ControlSystem;

//similar to old but with mission plan.
public class OpenLoopV2 {
    public static void main(String[] main) {
        Vector3D coordinates = new Vector3D(200,200,0);
        Vector3D initialVelocity = new Vector3D(0,0,0);
        Vector2D thruster = new Vector2D();

        double freeFall = timeOnFreeFall(0, coordinates.y);
        System.out.println(yDisplacement(coordinates.y));
        System.out.println(freeFall);
        System.out.println(newYInitial(g, freeFall, 0.0416));
    }


    public static final double g = 1.352;

    /**
     *
     * @param y0
     * @return
     */
    public static double yDisplacement(double y0) {
        return y0/4;
    }

    /**
     *
     * @param vy0
     * @param t
     * @param a
     * @return
     */
    public static double newYInitial(double vy0, double t, double a){
        double y0 = 4*(vy0 * t + a* t* t/2);
        return y0;
    }

    /**
     *
     * @param vy0
     * @param y0
     * @return
     */
    public static double timeOnFreeFall(double vy0, double y0) {
        double t = (-vy0 + Math.sqrt(Math.abs(vy0*vy0 - 4*(g/2)* (1/4.0) * y0) ))/g;
        return t;
    }

    /**
     *
     * @param vy0
     * @param a
     * @param t
     * @return
     */
    public static double newVy(double vy0, double a, double t) {
        double vyt = vy0 + a * t; //must be 0
        return vyt;
    }

    /**
     *
     * @param y0
     * @param vy0
     * @param t
     * @param a
     * @return
     */
    public static double newYt(double y0, double vy0,double t, double a){
        double yt = y0 + vy0*t + a*t*t/2; //set to 0
        return yt;
    }

    /**
     *
     * @param vy0
     * @param a
     * @return
     */
    public static double appropriateTvalue(double vy0, double a) {
        double t = vy0/a;
        return t;
    }

    /**
     *
     * @param vy0
     * @param y0
     * @return
     */
    public static double finalAcceleration(double vy0, double y0) {
        double a = vy0 * vy0 / y0;
        return a;
    }

    /**
     *
     * @param vy0
     * @param y0
     * @return
     */
    public static double verticalLanding(double vy0,double y0) {
        double u = vy0 * vy0/(2*y0) + 1.352; //theta has to 0
        return u;
    }

    //if initial case is to small.
    public static double thrustSmallCase(double thetaT){
        double u = g*2/Math.cos(thetaT);
        return u;
    }

    public static double timeForSmallCaseThruster(double yt, double y0) {
        double deltaY = yt - y0;
        double t = Math.sqrt(deltaY/g);
        return t;
    }

}
