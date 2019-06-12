package ControlSystem;

public class FunctionMain {
    private static final double STEPSIZE = 0.000001;
    //The function that we are evaluating
    private static FunctionDefiner test = new FunctionDefiner();


    //Requires a<b
    public static double integral(Function func, double a, double b) {
        double area = 0;

        for(double i = a+STEPSIZE; i<b; i = i + STEPSIZE){
            double distanceFromA = i-a;
            //Calculate area of each trapezoid and add together
            area += 0.5*STEPSIZE*(func.f(a+distanceFromA)+func.f(a+distanceFromA-STEPSIZE));

        }

        return area;
    }

    public static void main (String [] args){
        System.out.println(integral(test,0,Math.PI));
    }
}
