package Lambert;

import java.io.*;

public class Matrix implements Cloneable, Serializable {

    public double[][] A;

    public int m, n;

    public Matrix(int m, int n) {
        this.m = m;
        this.n = n;
        A = new double[m][n];
    }

    public Matrix copy() {
        Matrix X = new Matrix(m,n);
        double[][] C = X.getArray();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j];
            }
        }
        return X;
    }

    public Object clone() {
        return this.copy();
    }

    public double[][] getArray() {
        return A;
    }

    public double get(int i, int j) {
        return A[i][j];
    }

    public Matrix get(int[] I, int[] J) {
        checkIndicesLengths(I,J);
        Matrix X = new Matrix(I.length,1);
        double[][] B = X.getArray();
        for (int i = 0; i < I.length; i++) {
            B[i][0] = A[I[i]][J[i]];
        }
        return X;
    }

    public Matrix get(int[][] I, int[][] J) {
        checkIndicesDimensions(I,J);
        Matrix X = new Matrix(I.length,I[0].length);
        double[][] B = X.getArray();
        for (int i = 0; i < I.length; i++) {
            for (int j = 0; j < I[i].length; j++) {
                B[i][j] = A[I[i][j]][J[i][j]];
            }
        }
        return X;
    }

    public void set(int i, int j, double s) {
        A[i][j] = s;
    }

    public void set(int[] I, int[] J, double s) {
        checkIndicesLengths(I,J);
        for (int i = 0; i < I.length; i++) {
            A[I[i]][J[i]] = s;
        }
    }

    public VectorN times(VectorN B) {
        checkColumnDimension(B.length);
        double[] temp = new double[this.m];
        for (int i = 0; i < this.m; i++) {
            for (int j = 0; j < this.n; j++) {
                temp[i] = temp[i] + this.A[i][j]*B.get(j);
            }
        }
        VectorN out = new VectorN(temp);
        return out;
    }

    public static void checkIndicesLengths(int[] i, int[] j) {
        if (i.length != j.length) {
            throw new IllegalArgumentException("Indices lenghts must equals");
        }
    }

    public static void checkIndicesDimensions(int[][] i, int[][] j) {
        if ((i.length != j.length) || (i[0].length != j[0].length)) {
            throw new IllegalArgumentException("Indices dimensions must equals");
        }
    }

    public void checkColumnDimension(int column) {
        if (column != n) {
            throw new IllegalArgumentException("Matrix Columns dimensions must equals " + column);
        }
    }

}
