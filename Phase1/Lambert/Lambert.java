package Lambert;
public class Lambert implements ScalarFunction {

    private double s = 0.0;
    private double c = 0.0;
    private double dt = 0.0;
    private double mu = 0.0;
    private boolean aflag = false;
    private boolean bflag = false;
    public boolean debug_print = false;

    //initial deltaV
    public VectorN deltav0;
    //final deltaV
    public VectorN deltavf;

    //time of flight
    public double tof;

    public void reset() {
        s = 0.0;
        c = 0.0;
        aflag = false;
        bflag = false;
        debug_print = false;
    }


    public Lambert(double mu) {
        this.mu = mu;
    }

    private double getalpha(double a) {
        double alpha = 2.0 * Math.asin(Math.sqrt(s / (2.0 * a)));
        if (this.aflag) {
            alpha = 2.0 * Constants.pi - alpha;
        }
        return alpha;
    }

    private double getbeta(double a) {
        double beta = 2.0 * Math.asin(Math.sqrt((s - c) / (2.0 * a)));
        if (this.bflag) {
            beta = -1.0 * beta;
        }
        return beta;
    }

    private double getdt(double a, double alpha, double beta) {
        double sa = Math.sin(alpha);
        double sb = Math.sin(beta);
        double dt = Math.pow(a, 1.5) * (alpha - sa - beta + sb) / Math.sqrt(mu);
        return dt;
    }

    //get the semi-major axis.
    public double evaluate(double a) {
        double alpha = getalpha(a);
        double beta = getbeta(a);
        double out = dt - getdt(a, alpha, beta);
        return out;
    }

    //(tof, initial position vector, initial velocity vector, desired final position vector, desired final velocity vector.

    public double compute(VectorN r0, VectorN v0, VectorN rf, VectorN vf, double dt) throws LambertException {
        reset();
        double tp = 0.0;

        this.dt = dt;
        double magr0 = r0.mag();
        double magrf = rf.mag();

        VectorN dr = r0.minus(rf);
        this.c = dr.mag();
        this.s = (magr0 + magrf + c) / 2.0;
        double amin = s / 2.0;
        if (debug_print)
            System.out.println("amin = " + amin);

        double dtheta = Math.acos(r0.dotProduct(rf) / (magr0 * magrf));

        if (debug_print)
            System.out.println("dtheta = " + dtheta);

        if (dtheta < Constants.pi) {
            tp = Math.sqrt(2.0 / (mu)) * (Math.pow(s, 1.5) - Math.pow(s - c, 1.5)) / 3.0;
        }
        if (dtheta > Constants.pi) {
            tp = Math.sqrt(2.0 / (mu)) * (Math.pow(s, 1.5) + Math.pow(s - c, 1.5)) / 3.0;
            this.bflag = true;
        }

        if (debug_print)
            System.out.println("tp = " + tp);

        double betam = getbeta(amin);
        double tm = getdt(amin, Constants.pi, betam);

        if (debug_print)
            System.out.println("tm = " + tm);

        if (dtheta == Constants.pi) {
            throw new LambertException("dtheta = 180.0. Do a Hohmann");
        }

        double ahigh = 1000.0 * amin;
        double npts = 3000.0;
        if (debug_print)
            System.out.println("dt = " + dt);

        if (debug_print)
            System.out.println("************************************************");

        if (this.dt < tp) {
            throw new LambertException("No elliptical path possible ");
        }

        if (this.dt > tm) {
            this.aflag = true;
        }

        double fm = evaluate(amin);
        double ftemp = evaluate(ahigh);

        if ((fm * ftemp) >= 0.0) {
            throw new LambertException(" initial guesses do not bound ");
        }

        ZeroFinder regfalsi = new ZeroFinder(this, 10000, 1.0E-6, 1.0E-15);

        double sma = regfalsi.regulaFalsi(amin, ahigh);

        double alpha = getalpha(sma);
        double beta = getbeta(sma);

        double de = alpha - beta;

        double f = 1.0 - (sma / magr0) * (1.0 - Math.cos(de));
        double g = dt - Math.sqrt(sma * sma * sma / mu) * (de - Math.sin(de));

        VectorN newv0 = new VectorN(3);
        VectorN newvf = new VectorN(3);

        newv0.x[0] = (rf.x[0] - f * r0.x[0]) / g;
        newv0.x[1] = (rf.x[1] - f * r0.x[1]) / g;
        newv0.x[2] = (rf.x[2] - f * r0.x[2]) / g;

        this.deltav0 = newv0.minus(v0);
        if (debug_print)
            this.deltav0.print("deltav-0");

        double dv0 = deltav0.mag();

        double fdot = -1.0 * (Math.sqrt(mu * sma) / (magr0 * magrf)) * Math.sin(de);
        double gdot = 1.0 - (sma / magrf) * (1.0 - Math.cos(de));

        newvf.x[0] = fdot * r0.x[0] + gdot * newv0.x[0];
        newvf.x[1] = fdot * r0.x[1] + gdot * newv0.x[1];
        newvf.x[2] = fdot * r0.x[2] + gdot * newv0.x[2];

        this.deltavf = vf.minus(newvf);
        double dvf = deltavf.mag();
        if (debug_print)
            this.deltavf.print("deltav-f");

        double totaldv = dv0 + dvf;

        this.tof = dt;

        if (debug_print)
            System.out.println("dt = " + dt + " dv0 = " + dv0 + " dvf = " + dvf + " total dv = " + totaldv + " sma = " + sma);
        return totaldv;
    }

    public static void main(String args[]) {
        LinePrinter lp = new LinePrinter();

        //no path as this is for titan orbiting saturn
//        TwoBody elem0 = new TwoBody(47504e+8, 0.065, 0.11, 191.9601164245929, 275.302776889241, 347.71291963833);
//        TwoBody elemf = new TwoBody(1221870, 0.0288, 0.34854,  24.502,  185.671, 0.280);

        //earth to saturn data from 2004.  get later data.
        TwoBody elem0 = new TwoBody(47504e+8, 0.065, 0.11, 191.9601164245929, 275.302776889241, 347.71291963833);
        TwoBody elemf = new TwoBody(1426725412588.0, 0.05415060, 2.48446, 113.71504, 92.43194, 49.94432);

//        TwoBody elem0 = new TwoBody(40000.0, 0.2, 0.0, 0.0, 45.0, 0.0);
//        TwoBody elemf = new TwoBody(80000.0, 0.2, 0.0, 0.0, 270.0, 286.0);

        elem0.propagate(0.0, (0.89 * 86400.0), lp, false);
        elemf.propagate(0.0, (2.70 * 86400.0), lp, false);

        elem0.print("SC1");
        elemf.print("SC2");

        VectorN r0 = elem0.getR();
        VectorN v0 = elem0.getV();
        VectorN rf = elemf.getR();
        VectorN vf = elemf.getV();

        Lambert lambert = new Lambert(Constants.mu);
        try {
            double totaldv = lambert.compute(r0, v0, rf, vf, (2.70 - 0.89) * 86400);
        } catch (LambertException e) {
            e.printStackTrace();
        }

    }
}
