package ControlSystem;

public class OpenLoopV3 {
    public static void main(String[] main) {
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v


        double freeFall = timeOnFreeFall(velocity.y, spaceShip.getCoordinates().y);
        System.out.println("time on free fall: " + freeFall);
        double timePassed = 0;

        //temp for old velocity and location
        Vector3D tempLocation = spaceShip.coordinates;
        Vector3D tempVelocity = velocity;


        double newVX = (spaceShip.coordinates.x + spaceShip.calcDisplacement(125))/ freeFall;
        double newVY = (spaceShip.coordinates.y + yDisplacement(spaceShip.coordinates.y)) / freeFall;
        double newVZ = (spaceShip.coordinates.z + spaceShip.calcTilt(125)) /freeFall;

        //change velocity
        velocity.setX(newVX); //change time-step for dynamic solver :)
        velocity.setY(newVY);
        velocity.setZ(newVZ);


        //double vDot = velocity.norm();

        //change coordinates
        spaceShip.coordinates.y = yDisplacement(spaceShip.coordinates.y);
        spaceShip.coordinates.x = spaceShip.coordinates.x + spaceShip.calcDisplacement(spaceShip.coordinates.y);
        spaceShip.coordinates.z = spaceShip.calcTilt(spaceShip.coordinates.y);
        System.out.println("new x coordinate: " + spaceShip.coordinates.x);
        System.out.println("new y coordinate: " + spaceShip.coordinates.y);
        System.out.println("new theta coordinate: " + spaceShip.coordinates.z);

        XControl(tempLocation);
        ZControl(tempLocation, tempVelocity, freeFall);
        YControl();


    }

    public static Vector3D velocity = new Vector3D(0.1,0.1,0.1);
    public static Vector2D thurster = new Vector2D();
    public static final double g = 1.352;

    public static SpaceShip spaceShip = new SpaceShip(5000,50,350,0,600,600);

    public static double yDisplacement(double y0) {
        return y0/4;
    }


    public static double timeOnFreeFall(double vy0, double y0) {
        double t = (-vy0 + Math.sqrt(Math.abs(vy0*vy0 - 4*(g/2)* (1/4.0) * y0) ))/g;
        return t;
    }


    //velocity dot for half rotation.
    public static double vDot(double y,double t){
        return ((y-velocity.z)/t); //t*t
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

    public static void ZControl(Vector3D tempCoordinate, Vector3D tempVelocity, double timeOnFreeFall) {
        System.out.println("T velocity z: " + velocity.z);
        System.out.println("T z: " + spaceShip.coordinates.z);
        double vDot = rungeKutta4rth(timeOnFreeFall, velocity.z, 1, spaceShip.TIME_SLICE);
        System.out.println("vDot: " + vDot);
        double t = Math.sqrt((spaceShip.coordinates.z - tempCoordinate.z) / vDot);
        System.out.println("T t z: " + t);
        velocity.z = tempVelocity.z + velocity.z * t;
        System.out.println("T velocity z: " + velocity.z);
        spaceShip.coordinates.z = spaceShip.coordinates.z + tempVelocity.z * t + vDot*t*t /2;
        System.out.println("T new z: " + spaceShip.coordinates.z);
    }

    public static void YControl(){
        System.out.println("T velocity y: " + velocity.y);
        //fully rotated
        System.out.println("T coordinate y: " + spaceShip.coordinates.z);
        thurster.x = g / Math.cos(spaceShip.coordinates.z);
        System.out.println("T thruster y: " + thurster.x);
        spaceShip.coordinates.y = thurster.x * Math.cos(spaceShip.coordinates.z) - g;
        System.out.println("T new y: " + spaceShip.coordinates.y);
    }

    public static void XControl(Vector3D tempCoordinate) {
        System.out.println("T velocity x: " + velocity.x);
        double t = 2 * (tempCoordinate.x) / velocity.x; //use this as time
        System.out.println("T t x: " + t);
        double a = velocity.x * velocity.x / (2 * tempCoordinate.x);
        System.out.println("T a x: " + a);
        velocity.x = velocity.x + a * t;
        System.out.println("T new velocity x: " + velocity.x);
        thurster.x = velocity.x * velocity.x / (2 * tempCoordinate.x * Math.sin(spaceShip.coordinates.z));
        System.out.println("T thruster x: " + thurster.x); //print this out
        tempCoordinate.x = thurster.x * Math.sin(spaceShip.coordinates.z);
        System.out.println("New x: " + tempCoordinate.x); //plot x
    }

}
