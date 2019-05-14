package LandingSimulation;

public class OpenLoop {
    public final Vector2D GOAL = new Vector2D(0,0);
    public final double g = 1.352; //acceleration due to gravity in Titan
    public double t = 0.1; //timestep

    public double temp = 1.0;
    //Rotation
    public double velocityDot(double t) {
        return t; //state vector for velocity
    }
    public double angularVelocity(double t) {
        return angularVelocity(0) + velocityDot(t) * (t); //angularVelocity(0) is state vector getter and setter.
    }
    public double theta(double t) {
        return theta(0) + angularVelocity(t) * (t*t/2);
    }
    public double desiredAngle(double t){
        return theta(t) - theta(0);
    }
    //just to show derivation not really used.
    public double halfRotation(double t) {
        return 2*(angularVelocity(0)*t + velocityDot(t) * (t*t/2));
    }
    //set this.t to timeForHalfRotationCorrection
    public double timeForHalfRotationCorrection(double t) {
        return Math.sqrt(desiredAngle(t)/velocityDot(t));
    }


    //TODO: replace temp with spaceship state vectors of velocity[v0(t)] and x(0) position.
    //TODO:
    //blocking Vx and Vy

    // Main thruster force.
    public double u(double t){
        return temp*temp/(2*x(0)*Math.sin(theta(t)));
    }

    public double yDoubleDot(double t) {
        return u(t) * Math.cos(theta(t)) - g;
    }

    //Thruster to counter Vy
    public double velocityYCounter(double t) {
        return yDoubleDot(t) * t;
    }

    //Thruster Vy
    public double blockVy(double y) {
        return g/Math.cos(theta(t));
    }

    //
}
