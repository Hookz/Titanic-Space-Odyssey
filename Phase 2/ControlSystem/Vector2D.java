package ControlSystem;

public class Vector2D {
    public double x;
	public double y;

    public Vector2D() { }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(double[] vec2D) {
        if(vec2D.length > 2) {
            throw new IllegalArgumentException("Illegal array length.");
        } else {
            this.x = vec2D[0];
            this.y = vec2D[1];
        }
    }

    public Vector2D(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2D add(Vector2D other) {
        if (other != null) {
            x += other.x;
            y += other.y;
        }
        return this;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
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
        return xx + yy;
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
        return String.format("xAxis = %f, yAxis = %f", x, y);
    }

    public Vector2D copy() {
        Vector2D copy = new Vector2D(this.x, this.y);
        return copy;
    }
}
