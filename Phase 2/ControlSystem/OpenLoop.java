package ControlSystem;

public class OpenLoop {
    //change to long?
    public static void main(String args[]) {

        Vector3D coordinates = new Vector3D(250,250,0);
        Vector3D initialVelocity = new Vector3D(0,0,0);
        Vector2D thruster = new Vector2D(); //(u(t), v(t)



//        double trotate = timeForHalfRotation(36,1.2);
        double u = mainThruster(2.4E-15);
        double mu = mainThrusterForce(1.25E-15,100, 2.4E-15);
        double a = accelarationForXCorrection(1.25E-15, 100);
        double rotate = findRotation(31.6, a, initialVelocity.y, 0.5);
        double trotate = timeForHalfRotation(36,a);
        double rotateX = rotateSpaceCraftForX(31.6, 30, 0, 100);
//        double tvy = thrustAgainstVy(0,)
        System.out.println(a);
        System.out.println(rotate);
        System.out.println(trotate);
        System.out.println(u);
        System.out.println(rotateX);
    }

    public static double angularVelocity;
    public static final double g = 1.352;

    //TODO: vDot is a function not a constants.  Find a differential equation way of solving.

//    public double vDot(double t, double y) {
//        return 0;
//    }
    /**
     *
     * @param t timestep.
     * @param vDot velocity dot is first derivative of velocity.
     * @param theta0 rotation at starting point.
     * @param av0 angular velocity at 0.
     * @return a new rotation replacing the old rotation
     */
    public static double findRotation(double t, double vDot, double theta0, double av0){
        angularVelocity = av0 + vDot * t;
        double newRotation = theta0 + av0 + vDot * t * t / 2;
        return newRotation;
    }

    /**
     *
     * @param t time for half rotation.
     * @param vDot velocity dot is first derivative of velocity.
     * @return
     */
    public static double timeForHalfRotation(double t, double vDot) {
        double deltaTheta = vDot * t * t; //wrong just returning t.
        double newt = Math.sqrt(deltaTheta/vDot);
        //check if rotation is inline
        return newt;
    }

    //TODO: yDoubleDot is also a differntial equation.

    /**
     *
     * @param vy0 initial y velocity
     * @param yDoubleDot differential for y
     * @param t timestep
     * @return
     */
    public static double thrustAgainstVy(double vy0, double yDoubleDot, double t){
        vy0 = 0;
        double vyt = vy0 + yDoubleDot*t;
        return vyt;
    }

    //TODO: change name of mainThruster to u
    //TODO: change thetaT to findRotation method.

    /**
     *
     * @param thetaT
     * @return
     */
    public static double mainThruster(double thetaT){
        double u = g/Math.cos(thetaT);
        return u;
    }

    /**
     *
     * @param t
     * @param a
     * @param vx0
     * @param x0
     * @return
     */
    public static double rotateSpaceCraftForX(double t, double a, double vx0, double x0) {
        double vxt = vx0 + a*t; //set to 0
//        double vxt = 0;
        double xt = x0 + vx0*t + a*t*t/2;
        return xt;
    }

    /**
     *
     * @param vxt
     * @param x0
     * @return
     */
    public static double accelarationForXCorrection(double vxt, double x0) {
        double a = vxt * vxt / (2 *x0);
        return a;
    }

    /**
     *
     * @param x0
     * @param vx0
     * @return
     */
    public static  double timeForXCorrection(double x0, double vx0) {
        double t = 2 * x0 / vx0;
        return t;
    }

    /**
     *
     * @param vxt
     * @param x0
     * @param thetaT
     * @return
     */
    public static double mainThrusterForce(double vxt, double x0, double thetaT) {
        double u = vxt * vxt / (2 * x0 * Math.sin(thetaT));
        return u;
    }

    //land successfully
    public static boolean landSuccessfully(){
        return false;
    }
}
