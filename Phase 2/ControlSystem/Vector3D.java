package ControlSystem;

public class Vector3D {
    protected double x;
    protected double y;
    protected double z;

    public Vector3D() {
    }

    public Vector3D(double[] vec2D) {
        if(vec2D.length > 3) {
            throw new IllegalArgumentException("Illegal array length.");
        } else {
            this.x = vec2D[0];
            this.y = vec2D[1];
            this.z = vec2D[2];
        }
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

    public double norm() {
        return Math.sqrt(x*x + y*y + z*z);
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

    public Vector3D setElements(double x, double y, double z){
        return new Vector3D(x,y,z);
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }
    public void setZ(double z){
        this.z = z;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }
    @Override
    public String toString() {

        return String.format("xAxis = %f, yAxis = %f, theta = %f", x, y, z);
    }

    public Vector3D unitVector(){
        double magnitude = Math.sqrt(lengthSquared());
        double newX = this.x/magnitude;
        double newY = this.y/magnitude;
        double newZ = this.z/magnitude;
        Vector3D unitVec = new Vector3D(newX, newY, newZ);
        return unitVec;
    }

    public Vector3D multiplyDimencions(Vector3D other){
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
        return this;
    }

}