package OpenLoopAgain;

import java.util.ArrayList;

public class ClosedLoopV3 {
	private SpaceShip spaceship;
	private double timePassed = 0; //in seconds
	private final double g;
	private boolean titan = true;
	private ArrayList<Double> wind;
	private int kmToGo;
	
	private final double X_TOLERATE = 0.1;
	private final double TILT_TOLERATE = 0.02;
	private double beginSlowDownLoc;
	private double heavySlowDownLoc;
	
	
	//the initial position is the location of the spaceship
	public ClosedLoopV3(SpaceShip spaceship, double g) {
		this.spaceship = spaceship;
		this.g = g;
		if (this.g != spaceship.GRAV_TITAN) {
			this.titan = false;
		}
		kmToGo = (int) spaceship.getYLocation() / 1000;
		System.out.println("The amount of km the spaceship needs to travel is " + kmToGo);
		doCalculations();
	}
	
	public void update(long timeSlice) {
		spaceship.setAcceleration(new Vector2D());
		
		if (Math.abs(spaceship.getLocation().x) > X_TOLERATE) {
			if (spaceship.getLocation().x > 0) {
				spaceship.setTilt(-45 * 2* Math.PI);
			}
			else {
				spaceship.setTilt(45 * 2 * Math.PI);
			}
			spaceship.setAccelerationByMainThrusters(0.1);
			System.out.println("added acc by main thrusters");
		}
		else if (Math.abs(spaceship.getTilt()) > TILT_TOLERATE) {
			spaceship.setTilt(0);
		}
		if (beginSlowDownLoc > spaceship.getLocation().getY() && heavySlowDownLoc > spaceship.getLocation().getY()) {
			spaceship.setAccelerationByMainThrusters(1);
		}
		else if (heavySlowDownLoc < spaceship.getLocation().getY()) {
			spaceship.setAccelerationByMainThrusters(2);
		}
		
		if(titan) {
			spaceship.addAccelerationByGravityForceTitan();
		}
		else {
			spaceship.addAccelerationByGravityForceEarth();
		}
		
		spaceship.addAccelerationByMainThrusters();
		if (spaceship.getYLocation() > 0) {
		spaceship.addAccelerationByForce(new Vector2D(wind.get((int) (kmToGo - spaceship.getYLocation() / 1000 + 1)), 0));
		}
		
		spaceship.recalculateLocation(timeSlice);
		
		spaceship.addPassedTime(timeSlice);
		timePassed =+ timeSlice;
	
	}
	
	public void doCalculations() {
		Wind wind = new Wind(titan);
		this.wind = wind.calculateForcesForWholeTraject(spaceship, spaceship.getYLocation());
		
		beginSlowDownLoc = spaceship.getLocation().y * 0.5; //the spaceship should start to slow down a little bit
		heavySlowDownLoc = spaceship.getLocation().y * 0.9; //the spaceship should slow down a lot
	}
	
	public void setTimePassed(double sec) {
		this.timePassed = sec;
	}
	
	public double getTimePassed() {
		return timePassed;
	}
	
}

