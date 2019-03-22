package Lambert;

import java.io.Serializable;

public class VectorN implements Cloneable, Serializable {

    public double[] x;

    public int length;


    public VectorN(int n) {
        this.length = n;
        this.x = new double[n];
        for (int i = 0; i < n; i++) {
            this.x[i] = 0.0;
        }
    }

    public VectorN(double[] B) {
        this.length = B.length;
        this.x = new double[this.length];
        for (int i = 0; i < this.length; i++) {
            this.x[i] = B[i];
        }
    }

    public VectorN(VectorN y) {
        this.length = y.length;
        this.x = new double[this.length];
        for (int i = 0; i < this.length; i++) {
            this.x[i] = y.x[i];
        }
    }

    public VectorN(double x, double y, double z) {
        this.length = 3;
        this.x = new double[3];
        this.x[0] = x;
        this.x[1] = y;
        this.x[2] = z;
    }

    public VectorN copy() {
        VectorN X = new VectorN(this);
        return X;
    }

    public Object clone() {
        return this.copy();
    }

    public double get(int i) {
        return x[i];
    }

    public int getLength() {
        return this.length;
    }

    public VectorN get(int i, int n) {
        VectorN out = new VectorN(n);
        for (int j = 0; j < n; j++) {
            out.x[j] = this.x[i + j];
        }
        return out;
    }

    public void set(int i, double s) {
        this.x[i] = s;
    }

    public void set(int i, VectorN in) {
        int end = i + in.length - 1;
        if (this.length < end) {
            throw new IllegalArgumentException(
                    "Vector dimensions must be equal or greater than "+end);
        }

        for (int j = 0; j < in.length; j++) {
            this.x[i + j] = in.x[j];
        }
    }

    public void set(double s) {
        for (int i = 0; i < this.length; i++) {
            this.x[i] = s;
        }
    }

    public double mag() {
        double out = 0.0;
        for (int i = 0; i < this.length; i++) {
            out = out + this.x[i] * this.x[i];
        }
        out = Math.sqrt(out);
        return out;
    }

    public VectorN minus(VectorN B) {
        checkVectorDimensions(B);
        VectorN out = new VectorN(this.length);

        for (int i = 0; i < this.length; i++) {
            out.x[i] = this.x[i] - B.x[i];
        }
        return out;
    }

    public double dotProduct(VectorN B)
    // computes the dot product of a * b
    {
        checkVectorDimensions(B);

        double out = 0.0;
        for (int i = 0; i < this.length; i++) {
            out = out + this.x[i] * B.x[i];
        }
        return out;
    }

    public boolean equals(Object o) {
        boolean equals = false;
        if (o instanceof VectorN) {
            VectorN v = (VectorN)o;
            if (this.length == v.length) {
                equals = true;
                for(int ctr=0; equals && (ctr<this.length); ++ctr) {
                    equals = (this.get(ctr) == v.get(ctr));
                }
            }
        }
        return equals;
    }

    public void print(String title) {
        System.out.println(" Vector " + title);
        for (int i = 0; i < this.length; i++) {
            System.out.println("  " + title + "[" + i + "] = " + this.x[i]);
        }
        System.out.println("-------------------");
    }

    public String toString() {
        String out = new String();
        for (int j = 0; j < this.length; j++) {
            String number = Double.toString(this.x[j]);
            out = out + number + "\t";
        }
        return out;
    }

    public void checkVectorDimensions(VectorN B) {
        if (B.length != this.length) {
            throw new IllegalArgumentException(
                    "Vector dimensions must equal " + this.length);
        }
    }
}
