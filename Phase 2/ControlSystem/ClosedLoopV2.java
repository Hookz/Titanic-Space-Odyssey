package ControlSystem;

import FinalDifferentialEq.RungeKutta;

public class ClosedLoopV2 {
    public static void main(String[] main) {
        Vector2D thruster = new Vector2D(0,0); //x is u, y is v

        double timePassed = 0.1;
        double t = 0;

        while(spaceShip.coordinates.getY() > 0) {
            //temp for old velocity and location
            double tempT;
            Vector3D tempLocation = spaceShip.coordinates;
            Vector3D tempVelocity = velocity;

            //tempLocation = spaceShip.coordinates;
            //tempVelocity = velocity;

            spaceShip.coordinates.setY(spaceShip.coordinates.getY() - spaceShip.coordinates.getY() * timePassed);
            spaceShip.coordinates.setZ(spaceShip.calcTilt(spaceShip.coordinates.getY()));
            spaceShip.coordinates.setX(spaceShip.calcDisplacement(spaceShip.coordinates.getY()));

            double newVX = (tempLocation.getX() + spaceShip.calcDisplacement(spaceShip.coordinates.getY()))/ timePassed;
            double newVY = (tempLocation.getY() + yDisplacement(spaceShip.coordinates.getY())) / timePassed;
            double newVZ = (tempLocation.getZ() + spaceShip.calcTilt(200)) /timePassed;

            //change velocity
            velocity.setX(newVX); //change time-step for dynamic solver :)
            velocity.setY(newVY);
            velocity.setZ(newVZ);

            System.out.println("T velocity x: " + velocity.getX());
            double tx = 2 * (tempLocation.getX()) / velocity.getX(); //use this as time
            System.out.println("T t x: " + tx);
            double a = velocity.getX() * velocity.getX() / (2 * tempLocation.getX());
            System.out.println("T a x: " + a);
            velocity.setX(velocity.getX() + a * tx);
            System.out.println("T new velocity x: " + velocity.getX());
            thurster.setX(velocity.getX() * velocity.getX() / (2 * tempLocation.getX() * Math.sin(spaceShip.coordinates.getZ())));
            System.out.println("T thruster x: " + thurster.getX()); //print this out
            spaceShip.coordinates.setX(thurster.getX() * Math.sin(spaceShip.coordinates.getZ()));
            System.out.println("New x: " + spaceShip.coordinates.getX());

            System.out.println("T velocity z: " + velocity.getZ());
            System.out.println("T z: " + spaceShip.coordinates.getZ());
            double vDot = rungeKutta4rth(timePassed, velocity.getZ(), 1, spaceShip.TIME_SLICE);
            System.out.println("vDot: " + vDot);
            double ty = Math.sqrt((spaceShip.coordinates.getZ() - tempLocation.getZ()) / vDot);
            System.out.println("T t z: " + ty);
            velocity.setZ(tempVelocity.getZ() + velocity.getZ() * ty);
            System.out.println("T velocity z: " + velocity.getZ());
            spaceShip.coordinates.setZ(spaceShip.coordinates.getZ() + tempVelocity.getZ() * ty + vDot*ty*ty /2);
            System.out.println("T new z: " + spaceShip.coordinates.getZ());

            tempT = ty + 0.1 + tx;
            if(spaceShip.coordinates.getX() > 0.1 || spaceShip.coordinates.getX() < -0.1) {
                continue;
            }

            System.out.println("T velocity y: " + velocity.getY());
            //fully rotated
            System.out.println("T coordinate y: " + spaceShip.coordinates.getZ());
            thurster.setX(g / Math.cos(spaceShip.coordinates.getZ()));
            System.out.println("T thruster y: " + thurster.getX());
            spaceShip.coordinates.setY(thurster.getX() * Math.cos(spaceShip.coordinates.getZ()) - g);
            System.out.println("T new y: " + spaceShip.coordinates.getY());

            t += tempT;

            timePassed += spaceShip.TIME_SLICE;
        }

        System.out.println("Total time: " + timePassed);

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
}
