package ControlSystem;
import Land.*;
public class OpenLoopV2 {
    public static void main(String[] main) {
        //figure if coordinate is in Radians.
        Vector3D initialVelocity = new Vector3D(0,0,0);
        Vector3D velocity = new Vector3D(0,0,0);
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v
        Wind wind = new Wind();
        final double TIME_STEP = 0.1;
        double timePassed = 0;

//        spaceShip.setMass(5000);
        //velocity dot
        Vector3D acceleration = new Vector3D();

        double freeFall = timeOnFreeFall(velocity.y, spaceShip.getCoordinates().y);
        System.out.println("time on free fall: " + freeFall);

        //temp for old velocity and location
        Vector3D tempLocation = new Vector3D();
        Vector3D tempVelocity = new Vector3D();

        //Main loop:


        while(spaceShip.coordinates.y > 0.0) {
            tempLocation = spaceShip.coordinates;
            tempVelocity = velocity;

            double newVX = (spaceShip.coordinates.x + spaceShip.calcDisplacement(200))/ freeFall;
            double newVY = (spaceShip.coordinates.y + yDisplacement(spaceShip.coordinates.y)) / freeFall;
            double newVZ = (spaceShip.coordinates.z + spaceShip.calcTilt(200)) /freeFall;

            //change velocity
            velocity.setX(newVX); //change time-step for dynamic solver :)
            velocity.setY(newVY);
            velocity.setZ(newVZ);

            velocity.toString();

            double vDot = velocity.norm();

            //change coordinates
            spaceShip.coordinates.x = spaceShip.calcDisplacement(spaceShip.coordinates.y);
            spaceShip.coordinates.z = spaceShip.calcTilt(spaceShip.coordinates.y);
            spaceShip.coordinates.y = yDisplacement(spaceShip.coordinates.y);

            System.out.println("x: " + spaceShip.coordinates.x);
            System.out.println("y: " + spaceShip.coordinates.y);
            System.out.println("theta: " + spaceShip.coordinates.z);

            //x correct
            thruster.x = mainThrusterForce(velocity.x, spaceShip.coordinates.x, velocity.z);
            //update x
            spaceShip.coordinates.x = thruster.x * Math.sin(spaceShip.coordinates.z); //theta(z) correct
            double timeForXToCorrect = timeForXCorrection(spaceShip.coordinates.x, velocity.x);
            System.out.println("time for x correct: " + timeForXToCorrect);
            System.out.println("Thruster force: " + thruster.x);


            //System.out.println("ad: " + (velocity.z));

            //rotate back theta(t) = 0. use code: thruster.y
            double timeForHalfRotationCorrection = timeForHalfRotation(freeFall + timeForXToCorrect, vDot); //fr
            double findRotationDegree = findRotation(timeForHalfRotationCorrection, vDot, spaceShip.coordinates.z, velocity.z);
            //check if rotation is 0.
            System.out.println("Time of half rotation correction: " + timeForHalfRotationCorrection);
            System.out.println("Current rotation: " + findRotationDegree);
            //y correct after x correct
            double finalAcceleration = finalAcceleration(velocity.y, spaceShip.coordinates.y);
            double timeForVerticalLand = appropriateTvalue(velocity.y, finalAcceleration);
            double verticalLandingThruster = verticalLanding(velocity.y, spaceShip.coordinates.y);
            spaceShip.coordinates.y = verticalLandingThruster*Math.cos(spaceShip.coordinates.z)  - g; //coordinate or velocity?
            //update velocity of y.
            spaceShip.coordinates.y = 0;


            spaceShip.coordinates.toString();
            System.out.println("Final accelartion: " + finalAcceleration);
            System.out.println("time on vertical landing: " + timeForVerticalLand);
            System.out.println("vertical landing thrust: " + verticalLandingThruster);

            System.out.println("coordinates y: " + spaceShip.coordinates.y);


//            //update
            timePassed += TIME_STEP;

        }


        //


//        System.out.println(landSuccessfully(coordinates, velocity));

        /*
        System.out.println(yDisplacement(spaceShip.coordinates.y));
        System.out.println(freeFall);
        System.out.println(newYInitial(0, 3*freeFall, 0.0416));
        */


    }


    public static final double g = 1.352;

    public static SpaceShip spaceShip = new SpaceShip(5000,600,600,0);

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
        if(finalCoordinates.x <= spaceShip.getLandingXTolerance()) {
            return true;
            //&& finalVelocity.x <= spaceShip.getFinalXVelocity() &&
            //                finalVelocity.z <= spaceShip.getFinalAngularVelocity() && finalCoordinates.z <= spaceShip.getTiltTolerance()
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
        double deltaTheta = vDot * t * t/2; //wrong just returning t.
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


    //velocity dot
    public static double vDot(double y,double t){
        return (y/t);
    }



    //solvers
    //https://www.geeksforgeeks.org/euler-method-solving-differential-equation/
    public static void euler(double x0, double y, double h, double x) {
        double temp = -0;

        while(x0 < x) {
            temp = y;
            y = y + h * vDot(y,h); //add initial here
            x0 = x0 + h;
        }

        System.out.println("Approximate solution at x = "
                + x + " is " + y);
    }

    //https://www.geeksforgeeks.org/runge-kutta-4th-order-method-solve-differential-equation/
    public static double rungeKutta4rth(double x0, double y0, double x, double h) {
        //number of iterations:
        int n = (int) ((x-x0)/h);

        double k1, k2, k3, k4, k5;

        //iterate

        double y = y0;
        for(int i = 1; i <= n; i++) {
            k1 = h * vDot(x0, y);
            k2 = h * vDot(x0 + 0.5*h, y + 0.5*k1);
            k3 = h * vDot(x0 + 0.5*h, y + 0.5*k2);
            k4 = h * vDot(x0 + h, y + k3);

            //update y
            y = y + (1/6.0) * (k1 + 2*k2 + 2*k3 + k4);
            x0 += h;
        }

        return y;
    }

}
