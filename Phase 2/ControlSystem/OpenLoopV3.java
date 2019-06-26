package ControlSystem;

public class OpenLoopV3 {
    public static void main(String[] main) {
    	
        System.out.println("initial x loc is " + spaceShip.getCoordinates().x);
        System.out.println("initial y loc is " + spaceShip.getCoordinates().y);

        double freeFall = timeOnFreeFall(velocity.y, spaceShip.getCoordinates().y);
        System.out.println("time on free fall: " + freeFall);

        //temp for old velocity and location
        Vector3D tempLocation = spaceShip.coordinates;
        Vector3D tempVelocity = velocity;

        double newVX = (spaceShip.coordinates.x + spaceShip.calcDisplacement(spaceShip.coordinates.y*3/4))/ (freeFall/10);
        double newVY = (spaceShip.coordinates.y + yDisplacement(spaceShip.coordinates.y)) / (freeFall/10);
        double newVZ = (spaceShip.coordinates.z + spaceShip.calcTilt(125)) /(freeFall/10);
        
        newVY = freeFall * g;

        System.out.println("VX " + newVX);
        System.out.println("VY " + newVY);
        System.out.println("VZ " + newVZ);
        
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
    public static Vector2D thruster = new Vector2D();
    public static final double g = 1.352;

    public static SpaceShip spaceShip = new SpaceShip(5000,50,350,0,600,600, true);

    public static double yDisplacement(double y0) {
        return y0/4;
    }

    //which formula is this?????
    public static double timeOnFreeFall(double vy0, double y0) {
        //double t = (-vy0 + Math.sqrt(Math.abs(vy0*vy0 - 4*(g/2)* (1/4.0) * y0) ))/g;
    	double t = Math.sqrt((y0 * 3/4 *1000)/ (0.5 * g));
        return t;
    }

    //Controller for z
    public static void ZControl(Vector3D tempCoordinate, Vector3D tempVelocity, double timeOnFreeFall) {
    	ODESolvers solver = new ODESolvers(spaceShip);
    	
    	System.out.println("T velocity z: " + velocity.z);
        System.out.println("T z: " + spaceShip.coordinates.z);
        Vector3D velocCopy = new Vector3D(velocity.x, velocity.y, velocity.z);
        double vDot = solver.rungaKutta4thOrder(timeOnFreeFall, 1, spaceShip.TIME_SLICE, velocCopy.z);
        System.out.println("vDot: " + vDot);
        double t = Math.sqrt((spaceShip.coordinates.z - tempCoordinate.z) / vDot);
        System.out.println("T t z: " + t);
        velocity.z = tempVelocity.z + velocity.z * t;
        System.out.println("T velocity z: " + velocity.z);
        spaceShip.coordinates.z = spaceShip.coordinates.z + tempVelocity.z * t + vDot*t*t /2;
        System.out.println("T new z: " + spaceShip.coordinates.z);
    }

    //Controller for y
    public static void YControl(){
        System.out.println("T velocity y: " + velocity.y);
        //fully rotated
        System.out.println("T coordinate y: " + spaceShip.coordinates.z);
        thruster.x = g / Math.cos(spaceShip.coordinates.z);
        System.out.println("T thruster y: " + thruster.x);
        spaceShip.coordinates.y = thruster.x * Math.cos(spaceShip.coordinates.z) - g;
        System.out.println("T new y: " + spaceShip.coordinates.y);
    }

    //Controller for x
    public static void XControl(Vector3D tempCoordinate) {
        System.out.println("T velocity x: " + velocity.x);
        double t = 2 * (tempCoordinate.x) / velocity.x; //use this as time
        System.out.println("T t x: " + t);
        double a = velocity.x * velocity.x / (2 * tempCoordinate.x);
        System.out.println("T a x: " + a);
        velocity.x = velocity.x + a * t;
        System.out.println("T new velocity x: " + velocity.x);
        thruster.x = velocity.x * velocity.x / (2 * tempCoordinate.x * Math.sin(spaceShip.coordinates.z));
        System.out.println("T thruster x: " + thruster.x); //print this out
        tempCoordinate.x = thruster.x * Math.sin(spaceShip.coordinates.z);
        System.out.println("New x: " + tempCoordinate.x); //plot x
    }

}
