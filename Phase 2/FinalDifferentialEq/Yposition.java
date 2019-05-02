package FinalDifferentialEq;

public class Yposition implements DerivFunction{
    private double u = 0.0D;

    public double deriv(double u, double delta){
        double dydx = u*Math.cos(delta) - Physics.gravitationalAccerationTitan;
        return dydx;
    }

    public void setU(double u){
        this.u = u;
    }
}
