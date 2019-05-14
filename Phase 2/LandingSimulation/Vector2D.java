package LandingSimulation;

//TODO: make vector interface to go with Vector3D of Solar System model.

/**
 * Vector2D is an ADT reprenting a 2 dimensional space or a 2x1 matrix.
 */
public class Vector2D {
    public double x,y;

    public Vector2D() { }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D vec2D) {
        this.x = vec2D.getX();
        this.y = vec2D.getY();
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    //TODO: make arraylist getter for storing distance traveled
    public double[] getElements() {
        return new double[]{x, y};
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Vector2D) {
            Vector2D v = (Vector2D) obj;
            return (x == v.x) && (y == v.y);
        }
        return false;
    }

    @Override
    public Vector2D clone() {
        return new Vector2D(x, y);
    }

    public Vector2D sub(Vector2D other) {
        x -= other.x;
        y -= other.y;
        return this;
    }

    public Vector2D mul(double factor) {
        if (x != 0) {
            this.x *= factor;
        }
        if (y != 0) {
            this.y *= factor;
        }
        return this;
    }

    public Vector2D div(double factor) {
        if (x != 0) {
            this.x /= factor;
        }
        if (y != 0) {
            this.y /= factor;
        }
        return this;
    }

    public Vector2D normalize() {
        return mul(1.0/length());
    }

    public double lengthSquared() {
        double xx = 0;
        double yy = 0;
        if (this.x != 0 ) {
            xx = this.x*this.x;
        }
        if (this.y != 0 ) {
            yy = this.y*this.y;
        }
        return xx + yy + zz;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double distanceSquared(Vector2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx*dx+dy*dy;
    }
    public double distance(Vector2D other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public String toString() {
        return String.format("x = %f, y = %f", x, y);
    }

}
