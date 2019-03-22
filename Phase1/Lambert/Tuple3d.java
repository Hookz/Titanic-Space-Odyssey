package Lambert;

public abstract class Tuple3d implements java.io.Serializable, Cloneable {

    public	double	x;

    public	double	y;

    public	double	z;

    public Tuple3d()
    {
        this.x = (double) 0.0;
        this.y = (double) 0.0;
        this.z = (double) 0.0;
    }

    //(x,y,z) coordinate
    public final void set(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //(x,y,z) coordinate using arra
    public final void set(double[] t)
    {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public final void set(Tuple3d t1)
    {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }
    public final void set(Tuple3f t1)
    {
        this.x = (double) t1.x;
        this.y = (double) t1.y;
        this.z = (double) t1.z;
    }

    public final void get(double[] t)
    {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
    }

    public final void get(Tuple3d t)
    {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
    }

    public final void add(Tuple3d t1, Tuple3d t2)
    {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public final void add(Tuple3d t1)
    {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    @Override
    public boolean equals(Object t1)
    {
        try {
            Tuple3d t2 = (Tuple3d) t1;
            return(this.x == t2.x && this.y == t2.y && this.z == t2.z);
        }
        catch (ClassCastException   e1) {return false;}
        catch (NullPointerException e2) {return false;}

    }

    @Override
    public Object clone() {
        // Since there are no arrays we can just use Object.clone()
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    //getX
    public final double getX() {
        return x;
    }


    //setX
    public final void setX(double x) {
        this.x = x;
    }

    //getY
    public final double getY() {
        return y;
    }

    //setY
    public final void setY(double y) {
        this.y = y;
    }

    //getZ
    public final double getZ() {
        return z;
    }

    //setZ
    public final void setZ(double z) {
        this.z = z;
    }
}
