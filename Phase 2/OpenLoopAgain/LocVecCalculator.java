package OpenLoopAgain;

public class LocVecCalculator {
	//can be used to calculate different values according to data
	public double calculateTimeWhenSlowingDownWithConstantAcc(double needed, double veloc) {
		return needed / (0.5 * veloc);
	}

	public double calculateNewTheta(double oldLoc, double oldVeloc, double v, double timeSlice) {
		return oldLoc + calculateNewThetaVeloc(oldVeloc, v, timeSlice) * timeSlice;
	}
	
	public double calculateNewThetaVeloc(double oldVeloc, double v, double timeSlice) {
		return oldVeloc + v * timeSlice;
	}
	
	
	public double calculateNewX(double oldLoc, double oldVeloc, double u, double theta, double timeSlice) {
		return oldLoc + calculateNewXVeloc(oldVeloc, u, theta, timeSlice);
	}
	
	public double calculateNewXVeloc(double oldVeloc, double u, double theta, double timeSlice) {
		return oldVeloc + calculateNewXAcc(u, theta) * timeSlice;
	}
	
	public double calculateNewXAcc(double u, double theta) {
		return u*Math.sin(theta);
	}
	
	public double calculateNewY(double u, double theta, double g, double timeSlice, double oldVeloc, double oldLoc) {
		return oldLoc + calculateNewYVeloc(u, theta, g, timeSlice, oldVeloc) * timeSlice;
	}
	
	public double calculateNewYVeloc(double u, double theta, double g, double timeSlice, double oldVeloc) {
		return oldVeloc + timeSlice * calculateNewYAcc(u, theta, g);
	}
	
	public double calculateNewYAcc(double u, double theta, double g) {
		return u * Math.cos(theta) - g;
	}
}
