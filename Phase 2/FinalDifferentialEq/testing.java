package FinalDifferentialEq;

public class testing {
    public static void main(String[] args){
        // Create instance of the class holding the derivative evaluation method
        Derivn1 dn = new Derivn1();

        // Assign values to constants in the derivative function
        double k1 = 5.0D;
        double k2 = 2.0D;
        dn.setKvalues(k1, k2);

        // Variable declarations
        int n = 3;                            // number of differential equations
        double h = 0.01;                      // step size
        double x0 = 0.0D;                     // initial value of x
        double xn = 1.0D;                     // final value of x
        double[ ] y0 = new double[n];         // initial values of the y[i]
        double[ ] yn = new double[n];         // returned value of the y[i] at x = xn


        // Assign initial values of y[i]
        y0[0] = 5.0D;
        y0[1] = 1.0D;
        y0[2] = 0.5D;

        // Create and instance of RungeKutta
        RungeKutta rk = new RungeKutta();

        // Set values needed by fixed step size method
        rk.setInitialValueOfX(x0);
        rk.setFinalValueOfX(xn);
        rk.setInitialValuesOfY(y0);
        rk.setStepSize(h);

        // Call Fourth Order Runge-Kutta method
        yn = rk.fourthOrder(dn);

        // Output the results
        System.out.println("Fourth order Runge-Kutta procedure");
        System.out.println("The value of y[0] at x = " + xn + " is " + yn[0]);
        System.out.println("The value of y[1] at x = " + xn + " is " + yn[1]);
        System.out.println("The value of y[2] at x = " + xn + " is " + yn[2]);
        System.out.println("Number of iterations = " + rk.getNumberOfIterations());

    }
}
