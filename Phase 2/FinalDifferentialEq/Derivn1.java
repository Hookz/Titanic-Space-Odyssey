package FinalDifferentialEq;

class Derivn1 implements DerivnFunction{

    private double k1 = 0.0D, k2 = 0.0D;

    public double[ ] derivn(double x, double[ ] y){
        double[ ] dydx = new double [y.length];

        dydx[0] = -k1*y[0];
        dydx[1] =  k1*y[0] - k2*y[1];
        dydx[2] =  k2*y[1];

        return dydx;
    }

    public void setKvalues(double k1, double k2){
        this.k1 = k1;
        this.k2 = k2;
    }



    public double u(double t){
        return t;
    }
    public double v(double t) {
        return t;
    }
}