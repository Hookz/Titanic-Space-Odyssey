package ControlSystem;

import java.util.ArrayList;

//main Closed Loop controller
public class ClosedLoopV2 {
    public static Vector3D velocity = new Vector3D(0.1,0.1,0.1);
    public static Vector2D thurster = new Vector2D();
    public static final double g = 1.352;
    public static ODESolvers solvers;


    public static SpaceShip spaceShip;//= new SpaceShip(5000,200,700,0.0,600,600, true);
    public static boolean landed = false;
    public static ArrayList<Vector3D> successfullCoordinate;

    public ClosedLoopV2(SpaceShip spaceship) {
    	this.spaceShip = spaceship;
    	this.solvers = new ODESolvers(spaceship);

    }

    //Closed Loop Controller.   Stores calculate coordinate at a Arraylist which it returns.
    public static ArrayList<Vector3D> storeCoordinate() {
        ArrayList<Vector3D> coordinatesList = new ArrayList<>();
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v

        double timePassed = 0.1; //ten seconds
        double totalTime = 100000;
        double oldY = spaceShip.coordinates.getY();
        spaceShip.coordinates.setY(oldY - oldY*timePassed);

        //while loop itereation
        double t = 0;
        int normalIndexing =0;
        final int MAX_ITERATION = 10000;

        while(!landed || normalIndexing < MAX_ITERATION) {

            Vector3D tempLocation = spaceShip.coordinates;
            Vector3D tempVelocity = velocity;

            spaceShip.coordinates.setZ(spaceShip.calcTilt(spaceShip.coordinates.y));
            spaceShip.coordinates.setX(spaceShip.coordinates.getX() + spaceShip.calcDisplacement(spaceShip.coordinates.y));

            double newVX = tempLocation.x + spaceShip.calcDisplacement(spaceShip.coordinates.y)/ timePassed;
            double newVY = (tempLocation.y + yDisplacement(spaceShip.coordinates.y)) / timePassed;
            double newVZ = (tempLocation.z + spaceShip.calcTilt(spaceShip.coordinates.y)) /timePassed;

            //change velocity
            velocity.setX(newVX);
            velocity.setY(newVY);
            velocity.setZ(newVZ);

            double tx = 2 * (tempLocation.getX()) / velocity.getX();
            double a = velocity.getX() * velocity.getZ() / (2 * tempLocation.getX());
            velocity.setX(velocity.getX() + a * tx);
            thurster.setX(velocity.getX() * velocity.getX() / (2 * tempLocation.getX() * Math.sin(spaceShip.coordinates.getZ())));
            spaceShip.coordinates.setX(thurster.getX() * Math.sin(spaceShip.coordinates.getZ()));

            
            //double vDot = rungeKutta4rth(t, velocity.getZ(), 1, spaceShip.TIME_SLICE); //old one
            double vDot = solvers.rungaKutta4thOrder(t,1,spaceShip.TIME_SLICE, velocity.getZ());
            double ty = Math.sqrt((spaceShip.coordinates.getZ() - tempLocation.getZ())/vDot);
            velocity.setZ(tempVelocity.getZ() + velocity.getZ()*ty);
            spaceShip.coordinates.setZ(spaceShip.coordinates.getZ() + tempVelocity.getZ()*ty + vDot*ty*ty/2);


            timePassed += spaceShip.TIME_SLICE;
            if(spaceShip.coordinates.getX() <= 0.1 && spaceShip.coordinates.getX() >= -0.1 && spaceShip.coordinates.getZ() <= 0.02 && spaceShip.coordinates.getZ() >= -0.02) {
                //fully rotated
                thurster.setX(g / Math.cos(spaceShip.coordinates.getZ()));
                //would normally correct but for now -> don't use thrust in these coordinates
                while (spaceShip.coordinates.y > 0) {
                	spaceShip.coordinates.y = spaceShip.coordinates.y + 0.5 * spaceShip.TIME_SLICE * spaceShip.TIME_SLICE * Math.cos(spaceShip.coordinates.getZ() - g) + spaceShip.TIME_SLICE - spaceShip.getVelocity().y;
                	spaceShip.velocity.y = spaceShip.velocity.y + (Math.cos(spaceShip.coordinates.getZ() - g)) * spaceShip.TIME_SLICE;
                	//spaceShip.coordinates.setY(thurster.getX() * Math.cos(spaceShip.coordinates.getZ()) - g);
                	coordinatesList.add(spaceShip.coordinates.copy());
                	t = t+ 0.1;
                }
                t += 0.1;
                totalTime = t;
                coordinatesList.add(spaceShip.coordinates.copy());
                System.out.println("X = " + spaceShip.coordinates.getX());
                System.out.println("Y= " + spaceShip.coordinates.getY());
                System.out.println("Z= " + spaceShip.coordinates.getZ());
                landed = true;
                break;
            } else {
                t += 0.1;
                normalIndexing++;
            }
            coordinatesList.add(spaceShip.coordinates.copy());

            if(normalIndexing == MAX_ITERATION) {
                break;
            }
        }

        System.out.println(normalIndexing);
        System.out.println(landed);
        System.out.println("Total time: " + t);
        return coordinatesList;

    }

    //Help to restart Conterller if it does not have a safety landing.
    public static void helperClosedLoop() {
        ArrayList<Vector3D> s = storeCoordinate();
        if(landed) {
            System.out.println("it landed");
            successfullCoordinate = s;
        } else {
            helperClosedLoop();
        }
    }



    public static double yDisplacement(double y0) {
        return y0/4;
    }


}