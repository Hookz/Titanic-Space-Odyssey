package Lambert;

public class ZeroFinder {

    // max iteration before calling it quits
    private int maxit = 500;

    //how close to 0 do we need to it to be called 0.
    private double accuracy = 1.0E-12;

    //calculation error
    private double dxmin = 1.0E-12;

    //func to be solve
    private ScalarFunction func;

    //error counter
    private int err = 0;

    //(function to be solved, max iterations, f(x) accuracy to 0, how many # before 0)
    public ZeroFinder(ScalarFunction f, int max, double acc, double dxm) {
        setMaxIterations(max);
        setAccuracy(acc);
        set_dxmin(dxm);
        this.func = f;
    }

    public void setMaxIterations(int max) {
        this.maxit = max;
    }

    public void setAccuracy(double acc) {
        this.accuracy = acc;
    }

    public void set_dxmin(double x) {
        this.dxmin = x;
    }

    //using false position method.  Taking in lower and upper limits of x.
    public double regulaFalsi(double x1, double x2) {
        double xlow;
        double xhigh;
        double del;
        double out = 0.0;
        double f;

        double fl = this.func.evaluate(x1);
        double fh = this.func.evaluate(x2);

        double test = fl * fh;

        if (test > 0.0) {
            if (fl == 0.0) return x1;
            if (fh == 0.0) return x2;
            err++;
            System.out.println("Root must be bracketed in ZeroFinder "+err);
        }

        if (fl < 0.0) {
            xlow = x1;
            xhigh = x2;
        } else {
            xlow = x2;
            xhigh = x1;
            double temp = fl;
            fl = fh;
            fh = temp;
        }

        double dx = xhigh - xlow;

        for (int i = 1; i < this.maxit; i++) {
            out = xlow + dx * fl / (fl - fh);
            f = this.func.evaluate(out);

            if (f < 0.0) {
                del = xlow - out;
                xlow = out;
                fl = f;
            } else {
                del = xhigh - out;
                xhigh = out;
                fh = f;
            }

            dx = xhigh - xlow;

            if ((Math.abs(del) < this.dxmin)
                    || (Math.abs(f) < this.accuracy)) {
                return out;
            }
        }

        System.out.println(
                " Regula Falsi exceeded " + this.maxit + " iterations ");
        return out;
    }

}
