package OpenLoopAgain;

import java.util.ArrayList;

public class OpenLoopV4 {
	private SpaceShip spaceship;
	private double freefall;
	private double timePassed = 0; //in seconds
	private final double g;
	private boolean titan = true;
	private ArrayList<Double> wind;
	
	//the initial position is the location of the spaceship
	public OpenLoopV4(SpaceShip spaceship, double g) {
		this.spaceship = spaceship;
		this.g = g;
		if (this.g != spaceship.GRAV_TITAN) {
			this.titan = false;
		}
		doCalculations();
	}
	
	public void update(long timeSlice) {
		if (timePassed < freefall) {
			if (titan) {
				
			}
			else {
				
			}
			//only add gravitational force and air resistance
		}
		else {
			
		}
		
		spaceship.addPassedTime(timeSlice);
		timePassed =+ timeSlice;
	
	}
	
	public void doCalculations() {
		Wind wind = new Wind(titan);
		this.wind = wind.calculateForcesForWholeTraject(spaceship, spaceship.getYLocation());
		if (titan) {
			freefall = calculateFreeFallTimeTitan();
		}
		else {
			freefall = calculateFreeFallTimeEarth();
		}
	}
	
	//fall about 3/4th of the way through
	public double calculateFreeFallTimeTitan() {
		double time = 0;
		
		return time;
	}
	
	public double calculateFreeFallTimeEarth() {
		double time = 0;
		
		return time;
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
