package Lambert;

public class KeplerElements
{
    /** Semimajor axis in km */
    public double a; // sma in km
    /** Eccentricity */
    public double e; // eccentricity
    /** Inclination in radians */
    public double i; // inclination in radians
    /** Right ascension of the ascending node in radians */
    public double raan; // right ascension of ascending node in radians
    /** Argument of perigee in radians */
    public double w; // argument of perigee in radians
    /** True anomaly in radians */
    public double ta; // true anomaly in radians

    /**
     * @param a Semimajor axis in km
     * @param e Eccentricity
     * @param i Inclination in radians
     * @param raan Right ascension of ascending node in radians
     * @param w Argument of perigee in radians
     * @param ta True anomaly in radians
     */
    public KeplerElements(double a, double e, double i, double raan, double w, double ta)
    {
        this.a = a;
        this.e = e;
        this.i = i;
        this.raan = raan;
        this.w = w;
        this.ta = ta;
    }
}
