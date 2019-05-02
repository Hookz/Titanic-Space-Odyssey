package FinalDifferentialEq;

public class AngleOfRotation implements DerivFunction{
    private double u = 0.0D;

    public double deriv(double w, double ignore){
        double dydx = w;
        return dydx;
    }

    public void setU(double u){
        this.u = u;
    }
}
