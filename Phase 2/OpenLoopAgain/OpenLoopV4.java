package OpenLoopAgain;

import java.util.ArrayList;

public class OpenLoopV4 {
	private SpaceShip spaceship;
	//private double freefall;
	private double timePassed = 0; //in seconds
	private final double g;
	private boolean titan;
	
	public static double uMax;
	public static final double V_MAX = 0.1 * Math.PI; //m/s^2
	
	//use these to calculate the times needed - so this can be used in the model
	//so the output is determined before - with times and which u and v to use at what time
	private double xTemp;
	private double yTemp;
	private double thetaTemp;
	
	private double xVelTemp = 0;
	private double yVelTemp = 0;
	private double thetaVelTemp = 0;
	
	private double xAccTemp = 0;
	private double yAccTemp = 0;
	
	private ArrayList<Double> wind;
	
	//everything we need to calculate for the different times at which stuff happens, then this can be used for the simulation
	private double timeToCorrectThetaFirst;
	private double timeToCorrectThetaFirstVelocity;
	private double timeToSpeedUpX;
	private double timeToSlowDownX; //make this private again later
	private double timeToCorrectThetaAgain;
	private double timeToCorrectThetaAgainVelocity; 
	private double timeToCorrectThetaFinal;
	private double timeToCorrectThetaFinalVelocity;
	private double timeToCorrectYVelocityBeforeLanding;
	public double finalTime;
	
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
		uMax = 30000 / spaceship.getMass(); //30 kN max thrust, then get the maximum acceleration - which we need - m/s^2
		System.out.println("uMax is " + uMax + " m/s");
		xTemp = spaceship.getLocation().x;
		yTemp = spaceship.getLocation().y;
		thetaTemp = spaceship.getTilt();
		
		System.out.println("x loc is " + xTemp + ", y loc is " + yTemp + ", theta is " + thetaTemp);
		
		doCalculations();
	}
	
	public void update(double timeSlice) {
		spaceship.setAccelerationByMainThrusters(u());
		spaceship.setTorque(v());	
		
		//spaceship.addAccelerationByMainThrusters(timeSlice);
		spaceship.addAccelerationBySideThrusters(timeSlice);
		
		spaceship.recalculateLocation(timeSlice);
		
		//if testing: add wind eventually
		
		spaceship.addPassedTime(timeSlice);
		timePassed =  (timePassed +  timeSlice);
		
		//System.out.println("passed time " + this.timePassed);
		
		if(spaceship.getLocation().y <= 0) {
			spaceship.successfuLanding();
		}
	}
	
	public double getTimePassed() {
		return timePassed;
	}
	
	public void doCalculations() {
		correctThetaFirst();
		correctXSpeedUp();
		correctXSlowDown();
		correctThetaAgain();
		correctYVelocity();
		adjustTime();
		//add the calculations for every variable inhere
		
		Wind wind = new Wind(titan);
		this.wind = wind.calculateForcesForWholeTraject(spaceship, spaceship.getYLocation());
	}
	
	
	public void adjustTime() {
		timeToCorrectThetaFirstVelocity = this.timeToCorrectThetaFirstVelocity + this.timeToCorrectThetaFirst;
		timeToSpeedUpX = this.timeToCorrectThetaAgainVelocity + this.timeToSpeedUpX;
		timeToCorrectThetaAgain = this.timeToCorrectThetaAgain + this.timeToSpeedUpX;
		timeToCorrectThetaAgainVelocity = this.timeToCorrectThetaAgainVelocity + this.timeToCorrectThetaAgain;
		timeToSlowDownX = this.timeToCorrectThetaAgainVelocity + this.timeToSlowDownX;
		timeToCorrectThetaFinal = this.timeToCorrectThetaFinal + this.timeToSlowDownX;
		timeToCorrectThetaFinalVelocity = this.timeToCorrectThetaFinalVelocity + timeToCorrectThetaFinal;
		timeToCorrectYVelocityBeforeLanding = this.timeToCorrectYVelocityBeforeLanding + this.timeToCorrectThetaAgainVelocity;
		System.out.println("final time should be " + timeToCorrectYVelocityBeforeLanding);
		finalTime = timeToCorrectYVelocityBeforeLanding;
	}
	
	
	public double u() {
		double u = 0;
		
		if (timePassed <= timeToSpeedUpX && timePassed > timeToCorrectThetaFirstVelocity) {
			u = uToSpeedUpX;
			//System.out.println("speeding up x");
		}
		else if (timePassed <= timeToSlowDownX && timePassed > timeToCorrectThetaAgainVelocity) {
			u = uToSlowDownX; 
			//System.out.println("slowing down x");
		}
		else if(timePassed <= timeToCorrectYVelocityBeforeLanding && timePassed> timeToCorrectThetaFinalVelocity) {
			u = uToCorrectYVelocityBeforeLanding;
			//System.out.println("slowing down y");
		}
		return u;
	}
	
	public double v() {
		double v = 0;
		if (timePassed <= timeToCorrectThetaFirst) {
			v = vToCorrectThetaFirst;
			//System.out.println("correcting theta first");
		}
		else if(timePassed >timeToCorrectThetaFirst && timePassed <= timeToCorrectThetaFirstVelocity) {
			v = vToCorrectThetaFirstVelocity;
			//System.out.println("correcting theta vel after that");
		}
		else if (timePassed> timeToSpeedUpX && timePassed <= timeToCorrectThetaAgain) {
			v = vToCorrectThetaAgain;
			//System.out.println("correcting theta again");
		}
		else if (timePassed > timeToCorrectThetaAgain && timePassed <= timeToCorrectThetaAgainVelocity) {
			v = vToCorrectThetaAgainVelocity;
			//System.out.println("correcting theta vel again");
		}
		else if(timePassed > timeToCorrectThetaAgainVelocity && timePassed <= timeToSlowDownX) {
			v = vToCorrectThetaFinal;
			//System.out.println("correcting theta final");
		}
		else if(timePassed > timeToCorrectThetaFinal && timePassed <= timeToCorrectThetaFinalVelocity) {
			v = vToCorrectThetaFinalVelocity;
			//System.out.println("correcting theta vel final");
		}
		return v;
	}
	
	public void correctThetaFirst() {
		//first get it to half the theta needed
		
		double tempThetaNeeded = Math.acos(g / uMax);
	
		double timeNeeded = 0.001;
		double temporaryV = 1000;
		double tempMax;
		
		if (Math.abs(xTemp) > 0) {
			tempThetaNeeded = -tempThetaNeeded;
			temporaryV = -temporaryV;
			tempMax = -V_MAX;
			
			while (temporaryV < tempMax) {
				temporaryV =  ((((0.5 * tempThetaNeeded) - thetaTemp)/timeNeeded) - thetaVelTemp)/timeNeeded;
				timeNeeded = timeNeeded + 0.00001;
				//System.out.println(temporaryV);
			}
		}
		else {
		
			while (temporaryV > V_MAX) {
				temporaryV =  ((((0.5 * tempThetaNeeded) - thetaTemp)/timeNeeded) - thetaVelTemp)/timeNeeded;
				timeNeeded = timeNeeded + 0.00001;
				//System.out.println(temporaryV);
			}
		}
		System.out.println("desired angle is " + tempThetaNeeded);
		
		timeToCorrectThetaFirst = timeNeeded;
		vToCorrectThetaFirst = temporaryV;
		System.out.println("time to correct theta first is " + timeToCorrectThetaFirst);
		System.out.println("v needed to correct theta is " + vToCorrectThetaFirst);
		
		thetaTemp = 0.5 * tempThetaNeeded;
		
		//now slow down the velocity so the theta is constant
		thetaVelTemp = thetaVelTemp + vToCorrectThetaFirst * timeToCorrectThetaFirst;
		System.out.println("angularvel is " + thetaVelTemp);
		
		timeToCorrectThetaFirstVelocity = (0.5 * tempThetaNeeded) / (0.5 * thetaVelTemp); //assuming constant acceleration (so thrust)
		vToCorrectThetaFirstVelocity = thetaVelTemp / timeToCorrectThetaFirstVelocity;
		System.out.println("time to correct angular vel " + timeToCorrectThetaFirstVelocity);
		System.out.println("thrust needed to correct angular vel " + vToCorrectThetaFirstVelocity);
		thetaTemp = tempThetaNeeded;
	}
	
	public void correctXSpeedUp() {
		uToSpeedUpX = uMax;//for the first part of the correction
		double xNeeded = xTemp * 1/2;
		System.out.println("needed x is " + xNeeded);
		
		double time = 0.0001;
		double uTemp = uMax + 10;
		
		while (uTemp > uMax) {
			uTemp = ((((xNeeded - xTemp))/time - xVelTemp)/time)/ Math.sin(thetaTemp);
			//System.out.println(uTemp);
			time = time + 0.00001;
		} 
		timeToSpeedUpX = time;
		System.out.println("time to speed up x is " + timeToSpeedUpX);
		uToSpeedUpX = uTemp;
		System.out.println("u needed to speed up x is " + uToSpeedUpX);
		System.out.println();
		
	}
	
	
	public void correctXSlowDown() {
		
		//turn one half again
		double thetaNeeded = -thetaTemp; //facing exactly the other way
		System.out.println("theta needed for slowing down is " + thetaNeeded);
		
		double timeNeeded = 0.001;
		double temporaryV = 1000;
		double tempMax;
		
		if (Math.abs(xTemp) < 0) {
			temporaryV = -temporaryV;
			tempMax = -V_MAX;
			
			while (temporaryV < tempMax) {
				temporaryV =  ((((0.5 * thetaNeeded) - thetaTemp)/timeNeeded) - thetaVelTemp)/timeNeeded;
				timeNeeded = timeNeeded + 0.00001;
				//System.out.println(temporaryV);
			}
		}
		else {
		
			while (temporaryV > V_MAX) {
				temporaryV =  ((((0.5 * thetaNeeded) - thetaTemp)/timeNeeded) - thetaVelTemp)/timeNeeded;
				timeNeeded = timeNeeded + 0.00001;
				//System.out.println(temporaryV);
			}
		}
		timeToCorrectThetaAgain = timeNeeded;
		System.out.println("time needed to correct theta for slowing down " + timeNeeded);
		vToCorrectThetaAgain = temporaryV;
		System.out.println("v needed to correct the theta of the lunar lander in this time " + temporaryV);
		//then slow down the theta again
		
		thetaVelTemp = thetaVelTemp + vToCorrectThetaAgain * timeToCorrectThetaAgain;
		System.out.println("angularvel is " + thetaVelTemp);
				
		timeToCorrectThetaAgainVelocity = (0.666666666666666666 * thetaNeeded) / (0.5 * thetaVelTemp); //assuming constant acceleration (so thrust)
		vToCorrectThetaAgainVelocity = thetaVelTemp / timeToCorrectThetaAgainVelocity;
		System.out.println("time to correct angular vel " + timeToCorrectThetaAgainVelocity);
		System.out.println("thrust needed to correct angular vel " + vToCorrectThetaAgainVelocity);
		
		thetaTemp = thetaNeeded;
		
		//two third of this is left - find velocity when x = 1/3 of x itself
		xVelTemp = xVelTemp + timeToSpeedUpX *  uToSpeedUpX * Math.cos(thetaNeeded);
		System.out.println("the velocity in the x direction is " + xVelTemp);
		xTemp = xTemp - xVelTemp * timeToCorrectThetaAgainVelocity - 0.3333333333 *xTemp;
		System.out.println("the x location after correcting is " + xTemp);
		
		//this isn't right: find another time, using uMax!!
		timeToSlowDownX = xTemp / (0.5 * xVelTemp);
		System.out.println("time to slow x down such that at 0.0 it will be zero as well: "+ timeToSlowDownX);
		uToSlowDownX = xTemp / (Math.sin(thetaNeeded) * timeToSlowDownX);
		System.out.println("the u needed to slow down x well enough is: " + uToSlowDownX);
	}
	
	/*
	private long timeToCorrectThetaFinal;
	private long timeToCorrectThetaFinalVelocity;
	private long timeToCorrectYVelocityBeforeLanding;
	
	private double vToCorrectThetaFinal;
	private double vToCorrectThetaFinalVelocity;
	
	private double uToCorrectYVelocityBeforeLanding;
	 */
	
	public void correctThetaAgain() {
		double thetaNeeded = 0;
		double timeNeeded = 0.001;
		double temporaryV = 1000;
		double tempMax;
		
		if (Math.abs(xTemp) > 0) {
			temporaryV = -temporaryV;
			tempMax = -V_MAX;
			
			while (temporaryV < tempMax) {
				temporaryV =  (((0.5 * (thetaNeeded - thetaTemp))/timeNeeded) - thetaVelTemp)/timeNeeded;
				timeNeeded = timeNeeded + 0.00001;
				//System.out.println(temporaryV);
			}
		}
		else {
		
			while (temporaryV > V_MAX) {
				temporaryV =  ((((0.5 * (thetaNeeded) - thetaTemp))/timeNeeded) - thetaVelTemp)/timeNeeded;
				timeNeeded = timeNeeded + 0.00001;
				//System.out.println(temporaryV);
			}
		}
		
		timeToCorrectThetaFinal = timeNeeded;
		vToCorrectThetaFinal = temporaryV;
		System.out.println("time to correct theta final is " + timeToCorrectThetaFinal);
		System.out.println("v needed to correct theta final is " + vToCorrectThetaFinal);
		
		thetaTemp = 0.5 * thetaTemp;
		
		//now slow down the velocity so the theta is constant
		thetaVelTemp = thetaVelTemp + vToCorrectThetaFinal * timeToCorrectThetaFinal;
		System.out.println("angularvel is " + thetaVelTemp);
		
		timeToCorrectThetaFinalVelocity = (0.5 * thetaTemp) / (0.5 * thetaVelTemp); //assuming constant acceleration (so thrust)
		vToCorrectThetaFinalVelocity = thetaVelTemp / timeToCorrectThetaFinalVelocity;
		System.out.println("time to correct angular vel final " + timeToCorrectThetaFirstVelocity);
		System.out.println("thrust needed to correct angular vel final " + vToCorrectThetaFirstVelocity);
		thetaTemp = thetaNeeded;
	}
	
	public void correctYVelocity() {
		//first find the y location at this point, and the yvelocity
		//calculate this in the methods needed
		yVelTemp = 3;

		
		timeToCorrectYVelocityBeforeLanding = yTemp / (0.5 * yVelTemp);
		uToCorrectYVelocityBeforeLanding = ((yVelTemp / timeToCorrectYVelocityBeforeLanding) + g);
		
		System.out.println("thrust needed to correct y " + uToCorrectYVelocityBeforeLanding);
		/*
		double timeSlice = 10;
		while (yTemp > 0) {
			double toPrintYVel = yVelTemp + (uToCorrectYVelocityBeforeLanding * Math.cos(thetaTemp)) * timeSlice;
			double toPrintY = yTemp - yVelTemp * timeSlice;
			System.out.println("vel " + yVelTemp);
			System.out.println("loc " + yTemp);
		}	
		*/
		
	}
	
}
