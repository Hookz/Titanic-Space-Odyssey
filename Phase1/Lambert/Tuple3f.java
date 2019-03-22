package Lambert;

public abstract class Tuple3f implements java.io.Serializable, Cloneable {

    public	float	x;
    public	float	y;
    public	float	z;

    public Tuple3f()
    {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public final void set(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public final void set(float[] t)
    {
        this.x = t[0];
        this.y = t[1];
        this.z = t[2];
    }

    public final void set(Tuple3f t1)
    {
        this.x = t1.x;
        this.y = t1.y;
        this.z = t1.z;
    }

    public final void set(Tuple3d t1)
    {
        this.x = (float) t1.x;
        this.y = (float) t1.y;
        this.z = (float) t1.z;
    }

    public final void get(float[] t)
    {
        t[0] = this.x;
        t[1] = this.y;
        t[2] = this.z;
    }

    public final void get(Tuple3f t)
    {
        t.x = this.x;
        t.y = this.y;
        t.z = this.z;
    }

    public final void add(Tuple3f t1, Tuple3f t2)
    {
        this.x = t1.x + t2.x;
        this.y = t1.y + t2.y;
        this.z = t1.z + t2.z;
    }

    public final void add(Tuple3f t1)
    {
        this.x += t1.x;
        this.y += t1.y;
        this.z += t1.z;
    }

    @Override
    public boolean equals(Object t1) {
        try {
            Tuple3f t2 = (Tuple3f) t1;
            return (this.x == t2.x && this.y == t2.y && this.z == t2.z);
        } catch (NullPointerException e2) {
            return false;
        } catch (ClassCastException e1) {
            return false;
        }
    }

    @Override
    public Object clone() {

        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }

    public final float getX() {
        return x;
    }

    public final void setX(float x) {
        this.x = x;
    }

    public final float getY() {
        return y;
    }

    public final void setY(float y) {
        this.y = y;
    }

    public final float getZ() {
        return z;
    }

    public final void setZ(float z) {
        this.z = z;
    }
}
