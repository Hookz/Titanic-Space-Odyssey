package FinalDifferentialEq;

class Xposition implements DerivFunction{

    private double u = 0.0D;

    public double deriv(double u, double delta){
        double dydx = u*Math.sin(delta);
        return dydx;
    }

    public void setU(double u){
        this.u = u;
    }
}