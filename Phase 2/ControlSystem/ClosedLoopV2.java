package ControlSystem;

import java.util.ArrayList;

public class ClosedLoopV2 {
    public static Vector3D velocity = new Vector3D(0.1,0.1,0.1);
    public static Vector2D thurster = new Vector2D();
    public static final double g = 1.352;


    public static SpaceShip spaceShip = new SpaceShip(5000,200,700,0.0,600,600, true);
    public static boolean landed = false;
    public static ArrayList<Vector3D> successfullCoordinate;

    public static ArrayList<Vector3D> storeCoordinate() {
        ArrayList<Vector3D> coordinatesList = new ArrayList<>();
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v

        double timePassed = 0.1; //ten seconds
        double totalTime = 100000;
        double oldY = spaceShip.coordinates.y;
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

            double vDot = rungeKutta4rth(t, velocity.getZ(), 1, spaceShip.TIME_SLICE);
            double ty = Math.sqrt((spaceShip.coordinates.getZ() - tempLocation.getZ())/vDot);
            velocity.setZ(tempVelocity.getZ() + velocity.getZ()*ty);
            spaceShip.coordinates.setZ(spaceShip.coordinates.getZ() + tempVelocity.getZ()*ty + vDot*ty*ty/2);


            timePassed += spaceShip.TIME_SLICE;
            if(spaceShip.coordinates.getX() <= 0.1 && spaceShip.coordinates.getX() >= -0.1 && spaceShip.coordinates.getZ() <= 0.02 && spaceShip.coordinates.getZ() >= -0.02) {
                //fully rotated
                thurster.setX(g / Math.cos(spaceShip.coordinates.getZ()));
                spaceShip.coordinates.setY(thurster.getX() * Math.cos(spaceShip.coordinates.getZ()) - g);
                coordinatesList.add(spaceShip.coordinates.copy());
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


    public static double timeOnFreeFall(double vy0, double y0) {
        double t = (-vy0 + Math.sqrt(Math.abs(vy0*vy0 - 4*(g/2)* (1/4.0) * y0) ))/g;
        return t;
    }


    //velocity dot for half rotation.
    public static double vDot(double y,double t){
        return ((y-velocity.getZ())/t); //t*t
    }

    //solvers
    //https://www.geeksforgeeks.org/euler-method-solving-differential-equation/
    public static double euler(double x0, double y, double h, double x) {
        double temp = -0;

        while(x0 < x) {
            temp = y;
            y = y + h * vDot(y,h); //add initial here
            x0 = x0 + h;
        }

        return y;
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

    public static void main(String args[]) {
        //ArrayList<Vector3D> s = storeCoordinate();

        helperClosedLoop();
        System.out.println("________________________________________________________________________________");
        for(int i = 0; i<successfullCoordinate.size(); i++){
            Vector3D temp = new Vector3D(successfullCoordinate.get(i));
            System.out.println("x = " + temp.getX());
            System.out.println("y = " + temp.getY());
            System.out.println("z = " + temp.getZ());
        }
    }
}
