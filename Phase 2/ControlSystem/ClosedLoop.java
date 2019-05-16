package ControlSystem;

import java.util.ArrayList;

public class ClosedLoop {

    /**
     *
     * @param x1 position of lander
     * @param x0 goal
     */
    public static void rotateTo(double x1, double x0) {
        if (x1 > x0) {
            //rotate left
        } else if(x1 < x0) {
            //rotate right
        } else {
            //fall
            fall();
        }
    }


    public static void fall() {
        //fall
    }
}
