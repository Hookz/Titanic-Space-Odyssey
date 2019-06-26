package ControlSystem;
/*
 * A class that solves ODE's - can use four different ones, depending on which you choose.
 * Equation to be solved is in the velocity method.
 * The velocity method can be changed - depending on which equation needs to be solved.
 */

public class ODESolvers {
	private static SpaceShip spaceShip;
	
	public ODESolvers(SpaceShip spaceShip) {
		this.spaceShip = spaceShip;
	}
	
	//for the half rotation
	//this equation can be changed!!! depends on which calculation you need to do
	public static double velocity(double t, double y){
		double temporary = ((y - spaceShip.coordinates.z )/t);
        return temporary; //t*t - equation that we need to solve
    }

	public double eulersMethod(double t, double tEnd, double h, double y) {
		double oldY = y; //needs to be changed
		
		while (tEnd > t) {
			y = y + h * velocity(t, y); //calculation for euler's method
			t =+ h;
			oldY = y;
		}
		
		return y;
	}
	
	public double ralstonsMethod(double t, double tEnd, double h, double yZero) {
		double y = yZero;
		
		double kOne = 0;
		double kTwo = 0;
		
		while ( t < tEnd) {
			kOne = h*velocity(t, y);
			kTwo = h*velocity(t+((2*h)/3), y + ((2*kOne)/3));
			y = y + (kOne + 3 * kTwo)/4;
			t=+ h;
		}
		return y;
		
	}
	
	public double heuns3rdOrder(double t, double tEnd, double h, double yZero) {
		double y = yZero;
		double kOne = 0;
		double kTwo = 0;
		double kThree = 0;
		
		while (t < tEnd) {
			kOne = h * velocity(t, y);
			kTwo = h * velocity(t + h/3, y + kOne/3);
			kThree = h * velocity(t + (2*h)/3, y + (2*kTwo)/3);
			y = y + (kOne + 3*kThree)/4;
		}
		
		return y;
				
	}
	
	public double rungaKutta4thOrder(double t, double tEnd, double h, double yZero) {
        double kOne = 0;
        double kTwo = 0;
        double kThree= 0;
        double kFour = 0;

        double y = yZero;
        while (t < tEnd) {
            kOne = h * velocity(t, y);
            kTwo = h * velocity(t + 0.5*h, y + 0.5*kOne);
            kThree = h * velocity(t + 0.5*h, y + 0.5*kTwo);
            kFour = h * velocity(t + h, y + kThree);

            //update the current y (wi+1)
            y = y + (1/6.0) * (kOne + 2*kTwo + 2*kThree + kFour);
            t += h;
        }

        return y;
	}
	
}

