package Lambert;

public class TwoBody implements Derivatives
{

    private double a; // sma in km
    private double e; // eccentricity
    private double i; // inclination in radians
    private double raan; // right ascension of ascending node in radians
    private double w; // argument of perigee in radians
    private double ta; // true anomaly in radians
    protected double mu = 398600.4415; // GM in km^3/s^2 (value from JGM-3
    private double steps = 500.;

    public VectorN rv; // position and velocity vector

    //TwoBody(Semi-major axis in km, Eccentricity, Inclination in degrees, RAAN in degrees, Argument of perigee in degrees, True anomaly in degrees)
    public TwoBody(double x1, double x2, double x3, double x4, double x5, double x6)
    {
        double[] temp = new double[6];
        this.a = x1;
        this.e = x2;
        this.i = x3 * Constants.deg2rad;
        this.raan = x4 * Constants.deg2rad;
        this.w = x5 * Constants.deg2rad;
        this.ta = x6 * Constants.deg2rad;
        temp = this.randv();
        this.rv = new VectorN(temp);
    }
    //get SMA
    public double semiMajorAxis()
    {
        return this.a;
    }

    //get eccen
    public double eccentricity()
    {
        return this.e;
    }

    //get inc.
    public double inclination()
    {
        return this.i;
    }

   //get RAAN
    public double RAAN()
    {
        return this.raan;
    }

   //get arg. of perigee
    public double argPerigee()
    {
        return this.w;
    }

    //get true anomaly
    public double trueAnomaly()
    {
        return this.ta;
    }

    //get eccentric anomaly
    public double eccentricAnomaly()
    {
        double cta = Math.cos(ta);
        double e0 = Math.acos((e + cta) / (1.0 + e * cta));
        return e0;
    }

   //get mean anomaly
    public double meanAnomaly()
    {
        double ea = eccentricAnomaly();
        double m = ea - e * Math.sin(ea);
        return m;
    }


    public double meanMotion()
    {
        double acubed = a * a * a;
        double n = Math.sqrt(this.mu / acubed);
        return n;
    }

    public double period()
    {
        double n = meanMotion();
        double period = 2.0 * Constants.pi / n;
        return period;
    }

    public void printElements(String title)
    {
        System.out.println(" Kepler Elset: " + title);
        System.out.println("   a = " + this.a);
        System.out.println("   e = " + this.e);
        System.out.println("   i = " + (this.i * Constants.rad2deg));
        System.out.println("   raan = " + (this.raan * Constants.rad2deg));
        System.out.println("   w = " + (this.w * Constants.rad2deg));
        System.out.println("   ta = " + (this.ta * Constants.rad2deg));
    }

    public void printVector(String title)
    {
        this.rv.print("r and v vector: " + title);
    }


    public void print(String title)
    {
        this.printElements(title);
        this.printVector(title);
    }

    /** Compute the PQW to ECI transformation matrix.
     * @return Matrix containing the transformation.
     */

    public Matrix PQW2ECI()
    {
        double cw = Math.cos(w);
        double sw = Math.sin(w);
        double craan = Math.cos(raan);
        double sraan = Math.sin(raan);
        double ci = Math.cos(i);
        double si = Math.sin(i);

        Matrix out = new Matrix(3, 3);
        out.A[0][0] = craan * cw - sraan * sw * ci;
        out.A[0][1] = -craan * sw - sraan * cw * ci;
        out.A[0][2] = sraan * si;
        out.A[1][0] = sraan * cw + craan * sw * ci;
        out.A[1][1] = -sraan * sw + craan * cw * ci;
        out.A[1][2] = -craan * si;
        out.A[2][0] = sw * si;
        out.A[2][1] = cw * si;
        out.A[2][2] = ci;
        return out;
    }

    //ECI position and velocity vectors
    public double[] randv()
    {
        double p = a * (1.0 - e * e);
        double cta = Math.cos(ta);
        double sta = Math.sin(ta);
        double opecta = 1.0 + e * cta;
        double sqmuop = Math.sqrt(this.mu / p);

        VectorN xpqw = new VectorN(6);
        xpqw.x[0] = p * cta / opecta;
        xpqw.x[1] = p * sta / opecta;
        xpqw.x[2] = 0.0;
        xpqw.x[3] = -sqmuop * sta;
        xpqw.x[4] = sqmuop * (e + cta);
        xpqw.x[5] = 0.0;

        Matrix cmat = PQW2ECI();

        VectorN rpqw = new VectorN(xpqw.x[0], xpqw.x[1], xpqw.x[2]);
        VectorN vpqw = new VectorN(xpqw.x[3], xpqw.x[4], xpqw.x[5]);

        VectorN rijk = cmat.times(rpqw);
        VectorN vijk = cmat.times(vpqw);

        double[] out = new double[6];

        for (int i = 0; i < 3; i++)
        {
            out[i] = rijk.x[i];
            out[i + 3] = vijk.x[i];
        }

        return out;
    }
    //get position vector
    public VectorN getR()
    {
        VectorN out = new VectorN(3);
        out.x[0] = this.rv.x[0];
        out.x[1] = this.rv.x[1];
        out.x[2] = this.rv.x[2];
        return out;
    }

    //get velocity vector
    public VectorN getV()
    {
        VectorN out = new VectorN(3);
        out.x[0] = this.rv.x[3];
        out.x[1] = this.rv.x[4];
        out.x[2] = this.rv.x[5];
        return out;
    }

    //solves kepler's equation
    public static double solveKepler(double mean_anomaly, double ecc)
    {
        if (Math.abs(ecc) < 0.000000001)
        {
            return mean_anomaly;
        }
        int maxit = 10000;
        int it = 0;

        double de = 1000.0;

        double ea = mean_anomaly;
        double old_m = mean_anomaly;

        while ((it < maxit) && (Math.abs(de) > 1.0E-10))
        {
            double new_m = ea - ecc * Math.sin(ea);
            de = (old_m - new_m) / (1.0 - ecc * Math.cos(ea));
            ea = ea + de;
            it = it + 1;
        }
        return ea;
    }

    public void propagate(double t0, double tf, Printable pr, boolean print_switch)
    {
        double[] temp = new double[6];


        // Determine step size
        double n = this.meanMotion();
        double period = this.period();
        double dt = period / steps;
        if ((t0 + dt) > tf) // check to see if we're going past tf
        {
            dt = tf - t0;
        }

        // determine initial E and M
        double sqrome2 = Math.sqrt(1.0 - this.e * this.e);
        double cta = Math.cos(this.ta);
        double sta = Math.sin(this.ta);
        double sine0 = (sqrome2 * sta) / (1.0 + this.e * cta);
        double cose0 = (this.e + cta) / (1.0 + this.e * cta);
        double e0 = Math.atan2(sine0, cose0);

        double ma = e0 - this.e * Math.sin(e0);

        // determine sqrt(1+e/1-e)

        double q = Math.sqrt((1.0 + this.e) / (1.0 - this.e));

        // initialize t

        double t = t0;

        if (print_switch)
        {
            temp = this.randv();
            pr.print(t, temp);
        }

        while (t < tf)
        {
            ma = ma + n * dt;
            double ea = solveKepler(ma, this.e);

            double sinE = Math.sin(ea);
            double cosE = Math.cos(ea);
            double den = 1.0 - this.e * cosE;

            double sinv = (sqrome2 * sinE) / den;
            double cosv = (cosE - this.e) / den;

            this.ta = Math.atan2(sinv, cosv);
            if (this.ta < 0.0)
            {
                this.ta = this.ta + 2.0 * Constants.pi;
            }

            t = t + dt;

            temp = this.randv();
            this.rv = new VectorN(temp);

            if (print_switch)
            {
                pr.print(t, temp);
            }

            if ((t + dt) > tf)
            {
                dt = tf - t;
            }

        }
    }

    //get derivatives for integration of two body equations. (time t, state vector yAxis)
    public double[] derivs(double t, double[] y)
    {
        double out[] = new double[6];
        VectorN r = new VectorN(y[0], y[1], y[2]);
        double rmag = r.mag();
        double rcubed = rmag * rmag * rmag;
        double muorc = -1.0 * this.mu / rcubed;

        out[0] = y[3];
        out[1] = y[4];
        out[2] = y[5];
        out[3] = muorc * y[0];
        out[4] = muorc * y[1];
        out[5] = muorc * y[2];

        return out;
    }

}

