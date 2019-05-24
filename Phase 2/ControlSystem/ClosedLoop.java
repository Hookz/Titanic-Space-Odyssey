package ControlSystem;

public class ClosedLoop {
    public static void main(String[] main) {
        Vector3D coordinates = new Vector3D(200,200,0); //change to spaceship gettter.
        //figure if coordinate is in Radians.
        Vector3D initialVelocity = new Vector3D(0,0,0);
        Vector3D velocity = new Vector3D(0,0,0);
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v
        Wind wind = new Wind();
        final double TIME_STEP = 0.1;
        double timePassed = 0;

        //velocity dot
        Vector3D acceleration = new Vector3D();

        //vDot....

        //Main loop:
        while(spaceShip.getCoordinates().y > 0.0) {
            //change velocity
            velocity.x =  (spaceShip.getCoordinates().x + spaceShip.calcDisplacement(spaceShip.getCoordinates().y))/TIME_STEP; //change time-step for dynamic solver :)
            velocity.y = spaceShip.getCoordinates().y + yDisplacement(spaceShip.getCoordinates().y) / TIME_STEP;
            velocity.z = (spaceShip.getCoordinates().z + spaceShip.calcTilt(spaceShip.getCoordinates().y)) / TIME_STEP;

            //change coordinates
            coordinates.x = spaceShip.calcDisplacement(spaceShip.getCoordinates().y);
            coordinates.z = spaceShip.calcTilt(spaceShip.getCoordinates().y);
            coordinates.y = yDisplacement(spaceShip.getCoordinates().y);

            //get distances


            //x correct
            thruster.x = mainThrusterForce(velocity.x, coordinates.x, velocity.z);
            //update x
            coordinates.x = thruster.x * Math.sin(spaceShip.getCoordinates().z); //theta(z) correct
            double timeForXToCorrect = timeForXCorrection(spaceShip.getCoordinates().x, velocity.x);

            //rotate back theta(t) = 0. use code: thruster.y


            //y correct after x correct
            double finalAcceleration = finalAcceleration(velocity.y, spaceShip.getCoordinates().y);
            double timeForVerticalLand = appropriateTvalue(velocity.y, finalAcceleration);
            double verticalLandingThruster = verticalLanding(velocity.y, spaceShip.getCoordinates().y);
            coordinates.y = verticalLandingThruster*Math.cos(spaceShip.getCoordinates().z)  - g; //coordinate or velocity?
            //update velocity of y.


            //update
            timePassed += TIME_STEP;
        }



        System.out.println(landSuccessfully(spaceShip.getCoordinates(), velocity));


        System.out.println(yDisplacement(spaceShip.getCoordinates().y));
    }


    public static final double g = 1.352;

    public static SpaceShip spaceShip = new SpaceShip(5000,200,200,0);
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

    /**
     *
     * @param finalCoordinates
     * @param finalVelocity
     * @return
     */
    public static boolean landSuccessfully(Vector3D finalCoordinates, Vector3D finalVelocity){
        if(finalCoordinates.x <=  spaceShip.getLandingXTolerance() && finalVelocity.x <= spaceShip.getFinalXVelocity() &&  finalVelocity.z <= spaceShip.getFinalAngularVelocity() && finalCoordinates.z <= spaceShip.getTiltTolerance()) {
            return true;
        }
        else {
            return false;
        }
    }


    //version 1
    public static double findRotation(double t, double vDot, double theta0, double av0){
        double angularVelocity = av0 + vDot * t;
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


    //closed loop
    //get distances
    public static double distance(Vector2D spaceShip, Vector2D goal) {
        return spaceShip.distance(goal);
    }

    public static double xDistanceFromGoal(double x0, double x1) {
        return Math.sqrt(Math.pow(x1 - x0, x1));
    }

    public static void correctX() {

    }

    public static void rotate(double x) { //return signum
        if (Math.signum(x) == 1.0) {

            //update distance(x coordinate)
        }
        else if (Math.signum(x) == -1.0) {
            //rotate right
            // update distance(x coordinate)
        }
        else {
            //just fall since x = 0
        }
    }

    public static double xDistanceSignum(double x) {
        return Math.signum(x); //1.0 for positive, 0 for 0, -1.0 for negative
    }

    public static void fall(double u, double theta) {
        //y*cos(theta) - g
    }

    public static Vector3D oldCoorinates(Vector3D temp) {
        return null;
    }

}
