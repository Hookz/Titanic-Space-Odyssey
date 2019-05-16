package Lambert;

import java.io.*;

public class LinePrinter implements Printable {

    private final int[] indices;
    private PrintWriter pw;
    private final boolean printall;
    private double multiple = 1;
    private int counter = 0;
    private boolean isadaptive = false;

    public LinePrinter() {
        this(null, true, null);
    }

    private LinePrinter(String fname, boolean printAllIndexes, int[] i) {
        this.printall = printAllIndexes;
        this.indices = (printAllIndexes ? null : new int[i.length]);
        if (!printAllIndexes) {
            System.arraycopy(i, 0, this.indices, 0, i.length);
        }

        try {
            if (fname == null) {
                this.pw = new PrintWriter(System.out, true);
            }
            else {
                OutputStream ostrm = FileUtil.openOutputFile(fname);
                this.pw = new PrintWriter(ostrm);
            }
        } catch (IOException e) {
            System.err.println("LinePrinter error opening file: " + e);
            System.err.println("All output to line printer will be lost.");
        }
    }

    public void print(double t, double[] y) {
        if (pw != null) {
            double tprint = counter * multiple;
            if (t == tprint || isadaptive) {

                pw.print(t + "\t");

                // print the state array
                if (printall) {
                    // print all of the yAxis array
                    for (int j = 0; j < y.length; j++) {
                        pw.print(y[j] + "\t");
                    }
                } else {
                    // check to make sure there is enough to print
                    if (indices.length > y.length) {
                        System.out
                                .println("LinePrinter: too many elements to print");
                        return;
                    }
                    // print the requested parts of the yAxis array
                    for (int j = 0; j < indices.length; j++) {
                        pw.print(y[indices[j]] + "\t");
                    }
                }

                // add the linefeed for the next line
                pw.println();
                counter = counter + 1;
            }
        }
    }


}
