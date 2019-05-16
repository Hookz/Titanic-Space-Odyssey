package LandingSimulation;

public class OpenLoop implements DerivFunction{
    public final Vector2D GOAL = new Vector2D(0,0);
    public final double g = 1.352; //acceleration due to gravity in Titan

    public double temp = 1.0;
    public static void main(String args[]) {
        double t = 0.1; //timestep
        Deriv d1 = new Deriv();
        RungeKutta rk = new RungeKutta(); //timestep

        // Variable declarations
        double x0 = 24.0D;      // initial value of x
        double y0 = 24.0D;      // initial value of y
        double xn = 0.0D;      // final value of x
        double yn = 0.0D;      // returned value of y at x = xn
        double h = 0.1D;      // step size

        // Set values needed by fixed step size method
        rk.setInitialValueOfX(x0);
        rk.setFinalValueOfX(xn);
        rk.setInitialValuesOfY(y0);
        rk.setStepSize(t);

        //Call rk4
        // Call Fourth Order Runge-Kutta method
        yn = rk.fourthOrder(d1); //change to method

        // Output the result
        System.out.println("Fourth order Runge-Kutta procedure");
        System.out.println("The value of y at x = " + xn + " is " + yn);
        System.out.println("Number of iterations = " + rk.getNumberOfIterations());

        TitanLander lander = new TitanLander();
        lander.setCoordinates(250,250, 0);
        lander.setVelocity(0,0,0);
    }
    //Rotation deriv is velocityDot
    public double deriv(double t, double y) {
        double vDot = (angularVelocity(t) - angularVelocity(t))/t;
        return vDot; //state vector for velocity
    }
    public OpenLoop() {
//        TitanLander lander = new TitanLander();
    }
    public double angularVelocity(double t) {
        return angularVelocity(0) + deriv(t, 0) * (t); //angularVelocity(0) is state vector getter and setter.
    }
    public double theta(double t) {
        return theta(0) + angularVelocity(t) * (t*t/2);
    }
    public double desiredAngle(double t){
        return theta(t) - theta(0);
    }
    //just to show derivation not really used.
    public double halfRotation(double t) {
        return 2*(angularVelocity(0)*t + deriv(t,0) * (t*t/2)); //initial angular velocity at 0.
    }
    //set this.t to timeForHalfRotationCorrection
    public double timeForHalfRotationCorrection(double t) {
        return Math.sqrt(desiredAngle(t)/ deriv(t,0));
    }


    //TODO: replace temp with spaceship state vectors of velocity[v0(t)] and xAxis(0) position.
    //TODO:
    //blocking Vx and Vy

    // Main thruster force.
    public double u(double t){
        return v0(t)*v0(t)/(2*x(0)*Math.sin(theta(t)));
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

    /**
     * From a given y and Vy free fall 3/4 of the way through the try to stop.
     * displacement = 1/4 * y(0)
     * (1/4 * y(0) = vy(0)*t + a*(t^2/2))
     */
    //time for free fall
    public double timeForFreeFall(double vy0, double y0){
        return (-vy0 + Math.sqrt(vy0*vy0 -4*(1.352/2)*(1/4.0)*y0))/1.352;
    }
    //expression for acceleration.
    public double a(double vy0, double y0) {
        return vy0*vy0/(2*y0)
    }
    //vertical landing
    public double verticalLanding(double t) {
        return vy(0)*vy(0)/(2*y(0)) - g;
    }

    //Condition if initial y position is very small.
    //Move vertically up until 40
    public double accelerateSmallCase(double t, double y0) {
        double babayetu = 2.704/Math.cos(theta(t));
        double changeInY = y(t) - y0;
        double timeForSmallCaseThrust = Math.sqrt(changeInY/1.352);
        return babayetu;
    }

    //Euler's method.
    public void euler(double x0, double y, double h, double x)
    {
        float temp = -0;

        // Iterating till the point at which we
        // need approximation
        while (x0 < x) {
            temp = y;
            y = y + h * func(x0, y);
            x0 = x0 + h;
        }

        // Printing approximation
        System.out.println("Approximate solution at x = "
                + x + " is " + y);
    }
}
