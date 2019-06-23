package ControlSystem;

import javax.swing.*;
import java.util.ArrayList;

public class ClosedLoopV2 {

    public static ArrayList<Vector3D> storeCoordinate() {
        ArrayList<Vector3D> coordinatesList = new ArrayList<>();
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v

        double timePassed = 0.1;


        while(timePassed < 10000) {
            //temp for old velocity and location
            double tempT;
            Vector3D tempLocation = spaceShip.coordinates;
            Vector3D tempVelocity = velocity;



            spaceShip.coordinates.y = (spaceShip.coordinates.y - spaceShip.coordinates.y * timePassed);
            spaceShip.coordinates.z = (spaceShip.calcTilt(spaceShip.coordinates.y));
            spaceShip.coordinates.x = (spaceShip.calcDisplacement(spaceShip.coordinates.y));

            double newVX = tempLocation.x + spaceShip.calcDisplacement(spaceShip.coordinates.y)/ timePassed;
            double newVY = (tempLocation.y + yDisplacement(spaceShip.coordinates.y)) / timePassed;
            double newVZ = (tempLocation.z + spaceShip.calcTilt(spaceShip.coordinates.y)) /timePassed;

            //change velocity
            velocity.x = newVX;
            velocity.y = newVY;
            velocity.z = newVZ;

            double tx = 2 * (tempLocation.x) / velocity.x;
            double a = velocity.x * velocity.z / (2 * tempLocation.x);
            velocity.x = (velocity.x + a * tx);
            thurster.x = (velocity.x * velocity.x / (2 * tempLocation.x * Math.sin(spaceShip.coordinates.z)));
            spaceShip.coordinates.x =(thurster.x * Math.sin(spaceShip.coordinates.z));


            double vDot = rungeKutta4rth(timePassed, velocity.z, 1, spaceShip.TIME_SLICE);
            double ty = Math.sqrt((spaceShip.coordinates.z - tempLocation.z) / vDot);
            velocity.z = (tempVelocity.z + velocity.z * ty);
            spaceShip.coordinates.z = (spaceShip.coordinates.z + tempVelocity.z * ty + vDot*ty*ty /2);


            coordinatesList.add(spaceShip.coordinates);

            timePassed += spaceShip.TIME_SLICE;
            if(spaceShip.coordinates.x <= 0.1 && spaceShip.coordinates.x >= -0.1) {
                coordinatesList.add(spaceShip.coordinates);
                //fully rotated
                thurster.x = (g / Math.cos(spaceShip.coordinates.z));
                spaceShip.coordinates.y = (thurster.x * Math.cos(spaceShip.coordinates.z) - g);
                timePassed += 0.1;

            }  else {

                timePassed+=0.1;
                continue;
            }



            timePassed += spaceShip.TIME_SLICE;
        }
        System.out.println("Total time: " + timePassed);
        return coordinatesList;
    }

    public static Vector3D velocity = new Vector3D(0.1,0.1,0.1);
    public static Vector2D thurster = new Vector2D();
    public static final double g = 1.352;

    public static SpaceShip spaceShip = new SpaceShip(5000,200,200,0.0,600,600, true);

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
        for(int i = 0; i<s.size(); i++){
            temp = s.get(i);
            System.out.println("x = " + temp.x + " at index " + i);
            System.out.println("y = " + temp.y + " at index " + i);
            System.out.println("z = " + temp.z + " at index " + i);
        }
    }
}
