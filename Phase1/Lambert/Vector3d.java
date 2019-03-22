package Lambert;

public class Vector3d extends Tuple3d implements java.io.Serializable {

    public Vector3d()
    {
        super();
    }

    public final double length()
    {
        return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }

}