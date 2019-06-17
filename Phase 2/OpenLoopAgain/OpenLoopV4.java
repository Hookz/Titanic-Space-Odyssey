package OpenLoopAgain;

import java.util.ArrayList;

public class OpenLoopV4 {
	private SpaceShip spaceship;
	private double freefall;
	private long timePassed = 0; //in seconds
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
		spaceship.setTilt(0); //actually need to calculate this at every timeslice
		
		if (titan) {
			spaceship.addAccelerationByGravityForceTitan();
		}
		else {
			spaceship.addAccelerationByGravityForceEarth();
		}
		
		spaceship.addAirResistance();
		
		if( timePassed > freefall) {
			spaceship.setAccelerationByMainThrusters(1000);
			spaceship.calculateXAcceleration();
			spaceship.calculateYAcceleration();
		}
		
		spaceship.recalculateLocation(timeSlice);
		
		spaceship.addPassedTime(timeSlice);
		timePassed = timePassed +  timeSlice;
		
		System.out.println("passed time " + this.timePassed);
		
		if(spaceship.getLocation().y <= 0) {
			spaceship.successfuLanding();
		}
	
	}
	
	public double getTimePassed() {
		return timePassed;
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
	
	//fall about 3/4th of the way through - doesn't use air resistance or the v at the start (yet)
	public double calculateFreeFallTimeTitan() {
		double sNeeded = spaceship.getLocation().y * 0.75;
		double time = Math.sqrt((2 * sNeeded) / spaceship.GRAV_TITAN);
		System.out.println("freefall time is " + time);
		return time;
	}
	
	public double calculateFreeFallTimeEarth() {
		double sNeeded = spaceship.getLocation().y * 0.75;
		double time = Math.sqrt((2 *sNeeded) / spaceship.GRAV_EARTH);
		System.out.println("freefall time is " + time);
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
