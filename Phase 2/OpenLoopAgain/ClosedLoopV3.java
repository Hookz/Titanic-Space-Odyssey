package OpenLoopAgain;

import java.util.ArrayList;

public class ClosedLoopV3 {
	private SpaceShip spaceship;
	private double timePassed = 0; //in seconds
	private final double g;
	private boolean titan = true;
	private ArrayList<Double> wind;
	
	//the initial position is the location of the spaceship
	public ClosedLoopV3(SpaceShip spaceship, double g) {
		this.spaceship = spaceship;
		this.g = g;
		if (this.g != spaceship.GRAV_TITAN) {
			this.titan = false;
		}
		doCalculations();
	}
	
	public void update(long timeSlice) {
		
		
		spaceship.addPassedTime(timeSlice);
		timePassed =+ timeSlice;
	
	}
	
	public void doCalculations() {
		Wind wind = new Wind(titan);
		this.wind = wind.calculateForcesForWholeTraject(spaceship, spaceship.getYLocation());
	}
	
	
	public double u(long timeSlice) {
		double u = 0;
		
		return u;
	}
	
	public double v(long timeSlice) {
		double v = 0;
		
		
		return v;
	}
	
}

