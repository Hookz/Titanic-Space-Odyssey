package GUI;

/*
 * The same class as the one in Phase1.
 */

public class Vector3D {
    public double x;
    public double y;
    public double z;

    public Vector3D() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D(Vector3D other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vector3D add(Vector3D other) {
        if (other != null) {
            x += other.x;
            y += other.y;
            z += other.z;
        }
        return this;
    }

    public Vector3D sub(Vector3D other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;
        return this;
    }

    public Vector3D mul(double factor) {
        if (x != 0) {
            this.x *= factor;
        }
        if (y != 0) {
            this.y *= factor;
        }
        if (z != 0) {
            this.z *= factor;
        }
        return this;
    }

    public Vector3D div(double factor) {
        if (x != 0) {
            this.x /= factor;
        }
        if (y != 0) {
            this.y /= factor;
        }
        if (z != 0) {
            this.z /= factor;
        }
        return this;
    }

    public Vector3D normalize() {
        return mul(1.0/length());
    }

    public double lengthSquared() {
        double xx = 0;
        double yy = 0;
        double zz = 0;
        if (this.x != 0 ) {
            xx = this.x*this.x;
        }
        if (this.y != 0 ) {
            yy = this.y*this.y;
        }
        if (this.z != 0 ) {
            zz = this.z*this.z;
        }
        return xx + yy + zz;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double distanceSquared(Vector3D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        double dz = this.z - other.z;
        return dx*dx+dy*dy+dz*dz;
    }
    public double distance(Vector3D other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public String toString() {
        return String.format("xAxis = %f, yAxis = %f, z = %f", x, y, z);
    }

    public Vector3D copy() {
    	Vector3D copy = new Vector3D(this.x, this.y, this.z);
    	return copy;
    }

}
