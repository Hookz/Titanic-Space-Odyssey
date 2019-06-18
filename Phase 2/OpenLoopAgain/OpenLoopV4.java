package OpenLoopAgain;

import java.util.ArrayList;

public class OpenLoopV4 {
	private SpaceShip spaceship;
	//private double freefall;
	private long timePassed = 0; //in seconds
	private final double g;
	private boolean titan;
	
	//use these to calculate the times needed
	private double xTemp;
	private double yTemp;
	private double thetaTemp;
	
	private double xVelTemp = 0;
	private double yVelTemp = 0;
	private double thetaVelTemp = 0;
	
	private double xAccTemp = 0;
	private double yAccTemp = 0;
	private double thetaAccTemp = 0;
	
	//private ArrayList<Double> wind;
	
	//everything we need to calculate for the different times at which stuff happens, then this can be used for the simulation
	private long timeToCorrectThetaFirst;
	private long timeToCorrectThetaFirstVelocity;
	private long timeToSpeedUpX;
	private long timeToSlowDownX;
	private long timeToCorrectThetaAgain;
	private long timeToCorrectThetaAgainVelocity;
	private long timeToCorrectThetaFinal;
	private long timeToCorrectThetaFinalVelocity;
	private long timeToCorrectYVelocityBeforeLanding;
	
	private double vToCorrectThetaFirst;
	private double vToCorrectThetaFirstVelocity;
	private double vToCorrectThetaAgain;
	private double vToCorrectThetaAgainVelocity;
	private double vToCorrectThetaFinal;
	private double vToCorrectThetaFinalVelocity;
	
	private double uToSpeedUpX;
	private double uToSlowDownX;
	private double uToCorrectYVelocityBeforeLanding;
	
	//the initial position is the location of the spaceship
	public OpenLoopV4(SpaceShip spaceship, double g) {
		this.spaceship = spaceship;
		this.g = g;
		if (this.g != spaceship.GRAV_TITAN) {
			this.titan = false;
		}
		else {
			this.titan = true;
		}
		doCalculations();
	}
	
	public void update(long timeSlice) {
		u();
		v();
		
		spaceship.addAccelerationByMainThrusters(timeSlice);
		spaceship.addAccelerationBySideThrusters(timeSlice);
		
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
		//add the calculations for every variable inhere
		
		/*Wind wind = new Wind(titan);
		this.wind = wind.calculateForcesForWholeTraject(spaceship, spaceship.getYLocation());
		if (titan) {
			freefall = calculateFreeFallTimeTitan();
		}
		else {
			freefall = calculateFreeFallTimeEarth();
		}*/
	}
	
	/*
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
	*/
	
	
	
	public double u() {
		double u = 0;
		
		if (timePassed >= timeToSpeedUpX && timePassed < timeToCorrectThetaAgain) {
			spaceship.setAccelerationByMainThrusters(uToSpeedUpX);
		}
		else if (timePassed >= timeToSlowDownX && timePassed < timeToCorrectThetaFinal) {
			spaceship.setAccelerationByMainThrusters(uToSlowDownX);
		}
		else if(timePassed >= timeToCorrectYVelocityBeforeLanding) {
			spaceship.setAccelerationByMainThrusters(uToCorrectYVelocityBeforeLanding);
		}
		
		return u;
	}
	
	public double v() {
		double v = 0;
		if (timePassed >= timeToCorrectThetaFirst && timePassed < timeToCorrectThetaFirstVelocity) {
			spaceship.setTorque(vToCorrectThetaFirst);
		}
		else if(timePassed >= timeToCorrectThetaFirstVelocity && timePassed < timeToSpeedUpX) {
			spaceship.setTorque(vToCorrectThetaFirstVelocity);
		}
		else if (timePassed>= timeToCorrectThetaAgain && timePassed < timeToCorrectThetaAgainVelocity) {
			spaceship.setTorque(vToCorrectThetaAgain);
		}
		else if (timePassed >= timeToCorrectThetaAgainVelocity && timePassed < timeToSlowDownX) {
			spaceship.setTorque(vToCorrectThetaAgainVelocity);
		}
		else if(timePassed >= timeToCorrectThetaFinal && timePassed < timeToCorrectThetaFinalVelocity) {
			spaceship.setTorque(vToCorrectThetaFinal);
		}
		else if(timePassed >=timeToCorrectThetaFinalVelocity && timePassed < timeToCorrectYVelocityBeforeLanding) {
			spaceship.setTorque(vToCorrectThetaFinalVelocity);
		}
		
		
		return v;
	}
	
}
