package FinalDifferentialEq;

public class testing1 {
    public static void main(String[ ] arg){

        // Create instance of the class holding the derivative evaluation method
        Xposition d1 = new Xposition();

        // Assign values to constants in the derivative function
        double u = 10.0D;
        d1.setU(u);

        // Variable declarations
        double x0 = 0.0D;      // initial value of x
        double y0 = 0.0D;      // initial value of y
        double xn = 1.0D;      // final value of x
        double yn = 0.0D;      // returned value of y at x = xn
        double h = 0.01D;      // step size

        // Call Fourth Order Runge-Kutta method
        yn = RungeKutta.fourthOrder(d1, x0, y0, xn, h);
        System.out.println("The value of y at x = " + xn + " is " + yn);

//        // Output the result
//        for(int i = 0; i <= 10; i++){
//            xn+=1;
//            yn = RungeKutta.fourthOrder(d1, x0, y0, xn, h);
//            System.out.println("The value of y at x = " + xn + " is " + yn);
//        }

    }
}
