package ControlSystem;

import FinalDifferentialEq.RungeKutta;

import java.util.ArrayList;

public class ClosedLoopV2 {
    private static ArrayList<Vector3D> velocityList;
//    private static ArrayList<Vector3D> coordinatesList;

    public static ArrayList<Vector3D> storeCoordinate() {
        ArrayList<Vector3D> coordinatesList = new ArrayList<>();
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v

        double timePassed = 0.1;
        double t = 0;

        while(spaceShip.coordinates.y > 0) {
            //temp for old velocity and location
            double tempT;
            Vector3D tempLocation = spaceShip.coordinates;
            Vector3D tempVelocity = velocity;

            //tempLocation = spaceShip.coordinates;
            //tempVelocity = velocity;

            spaceShip.coordinates.y = (spaceShip.coordinates.y - spaceShip.coordinates.y * timePassed);
            spaceShip.coordinates.z = (spaceShip.calcTilt(spaceShip.coordinates.y));
            spaceShip.coordinates.x = (spaceShip.calcDisplacement(spaceShip.coordinates.y));

            double newVX = tempLocation.x + spaceShip.calcDisplacement(spaceShip.coordinates.y)/ timePassed;
            double newVY = (tempLocation.y + yDisplacement(spaceShip.coordinates.y)) / timePassed;
            double newVZ = (tempLocation.z + spaceShip.calcTilt(spaceShip.coordinates.y)) /timePassed;

            //change velocity
            velocity.x = newVX; //change time-step for dynamic solver :)
            velocity.y = newVY;
            velocity.z = newVZ;

//            System.out.println("T velocity x: " + velocity.x);
            double tx = 2 * (tempLocation.x) / velocity.x; //use this as time
//            System.out.println("T t x: " + tx);
            double a = velocity.x * velocity.z / (2 * tempLocation.x);
//            System.out.println("T a x: " + a);
            velocity.x = (velocity.x + a * tx);
//            System.out.println("T new velocity x: " + velocity.x);
            thurster.x = (velocity.x * velocity.x / (2 * tempLocation.x * Math.sin(spaceShip.coordinates.z)));
//            System.out.println("T thruster x: " + thurster.x); //print this out
            spaceShip.coordinates.x =(thurster.x * Math.sin(spaceShip.coordinates.z));
//            System.out.println("New x: " + spaceShip.coordinates.x);

//            System.out.println("T velocity z: " + velocity.z);
//            System.out.println("T z: " + spaceShip.coordinates.z);
            double vDot = rungeKutta4rth(timePassed, velocity.z, 1, spaceShip.TIME_SLICE);
//            System.out.println("vDot: " + vDot);
            double ty = Math.sqrt((spaceShip.coordinates.z - tempLocation.z) / vDot);
//            System.out.println("T t z: " + ty);
            velocity.z = (tempVelocity.z + velocity.z * ty);
//            System.out.println("T velocity z: " + velocity.z);
            spaceShip.coordinates.z = (spaceShip.coordinates.z + tempVelocity.z * ty + vDot*ty*ty /2);
//            System.out.println("T new z: " + spaceShip.coordinates.z);

            tempT = ty + 0.1 + tx;
            t += tempT;

            coordinatesList.add(spaceShip.coordinates);

            timePassed += spaceShip.TIME_SLICE;
            if(spaceShip.coordinates.x > 0.1 || spaceShip.coordinates.x < -0.1) {
                continue;
            }  else {

                coordinatesList.add(spaceShip.coordinates);
//                System.out.println("T velocity y: " + velocity.y);
                //fully rotated
//                System.out.println("T coordinate y: " + spaceShip.coordinates.z);
                thurster.x = (g / Math.cos(spaceShip.coordinates.z));
//                System.out.println("T thruster y: " + thurster.x);
                spaceShip.coordinates.y = (thurster.x * Math.cos(spaceShip.coordinates.z) - g);
//                System.out.println("T new y: " + spaceShip.coordinates.y);

                t += tempT;
            }

            timePassed += spaceShip.TIME_SLICE;
        }

        System.out.println("Total time: " + timePassed);
        return coordinatesList;
    }

    public static Vector3D velocity = new Vector3D(0.1,0.1,0.1);
    public static Vector2D thurster = new Vector2D();
    public static final double g = 1.352;

    public static SpaceShip spaceShip = new SpaceShip(5000,200,200,0,600,600);

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
        ArrayList<Vector3D> s = storeCoordinate();
        Vector3D temp = null;
        for(int i = 0; i<s.size();i++){
            temp = s.get(i);
            System.out.println("x = " + temp.x + " at " + i);
            System.out.println("y = " + temp.y + " at " + i);
            System.out.println("z = " + temp.z + " at " + i);
        }
    }
}
