package OpenLoopAgain;
import Land.SpaceShip;
import Land.Wind;
import ControlSystem.Vector2D;
import java.util.ArrayList;

/*Quick explanation about this class
 * 
 * calculates the thrust (from both the main thruster and the side thrusters) beforehand
 * does NOT take wind into account - when wind is used, the x location and x velocity will almost always be incorrect
 * as the update method, when used, produces different output, an arraylist with locations is provided
 * */

public class OpenLoopV4 {
	private SpaceShip spaceship;
	
	private double timePassed = 0; //in seconds
	private final double g; //in m/s
	private boolean titan; //boolean, can be used for the state of the wind
	private ArrayList<Vector2D> estimatedLocations = new ArrayList<>(); //stores the different locations
	
	//a max thrust from the main thrusters and a maximum thrust from the side thruster
	public static double uMax;
	public static final double V_MAX = 0.25 * Math.PI; //m/s^2
	
	//to keep track of where the lunar lander is at this moment
	private double xTemp;
	private double yTemp;
	private double thetaTemp;
	
	private double xVelTemp = 0;
	private double yVelTemp = 0;
	private double thetaVelTemp = 0;
	
	//the y from which you start - the s your lunar lander needs to make
	private double yGoal;
	
	//to store the calculations done in the doCalculations() method
	public double timeToCorrectThetaFirst; 
	public double timeToCorrectThetaFirstVelocity;
	public double timeToSpeedUpX; 
	public double timeToCorrectThetaAgain; 
	public double timeToCorrectThetaAgainVelocity; 
	public double timeToSlowDownX;
	public double timeToCorrectThetaFinal;
	public double timeToCorrectThetaFinalVelocity;
	public double timeToLetYFall;
	public double timeToCorrectYVelocityBeforeLanding;
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
	//call the calculation method in the constructor already
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
		
		//set temporary coordinates
		xTemp = spaceship.getLocation().x;
		yTemp = spaceship.getLocation().y;
		yGoal = spaceship.getLocation().y;
		thetaTemp = spaceship.getTilt();
		
		System.out.println("x loc is " + xTemp + ", y loc is " + yTemp + ", theta is " + thetaTemp);
		
		
		doCalculations();
		System.out.println();
		System.out.println();
	}
	
	//will update the spaceship object itself - uses the thrust needed to find accelerations
	//unfortunately, when the passed time gets bigger, so do the differences between the previously calculated values and the calculated values
	public void update(double timeSlice) {
		if (spaceship.getLocation().y > 0) {
			double uNeeded = u();
			double vNeeded = v();
		
			spaceship.setAccelerationByMainThrusters(uNeeded);
			spaceship.setTorque(vNeeded);	
			spaceship.addAccelerationBySideThrusters(timeSlice);
		
			spaceship.recalculateLocation(timeSlice);
		
			spaceship.addPassedTime(timeSlice);
			this.timePassed = timePassed + timeSlice;
		}
		if(spaceship.getLocation().y <= 0) {
			spaceship.successfuLanding();
		}
	}
	
	//getter
	public double getTimePassed() {
		return timePassed;
	}
	
	//call the other methods that do the calculations in the right order
	public void doCalculations() {
		correctThetaFirst();
		correctXSpeedUp();
		correctThetaForSlowingDown();
		correctXSlowDown();
		correctThetaAgain();
		correctYVelocity();
		adjustTime();
		//add the calculations for every variable inhere
		
		//when needed, this can be used
		Wind wind = new Wind(titan);
		ArrayList<Double> windspeeds = wind.calculateForcesForWholeTraject(spaceship, spaceship.getYLocation());
	}
	
	//such that, as the update methods runs through a while loop, the timestamps will be okay
	public void adjustTime() { 
		timeToCorrectThetaFirst = this.timeToCorrectThetaFirst;
		timeToCorrectThetaFirstVelocity = this.timeToCorrectThetaFirstVelocity + this.timeToCorrectThetaFirst;
		timeToSpeedUpX = this.timeToCorrectThetaFirstVelocity + this.timeToSpeedUpX;
		timeToCorrectThetaAgain = this.timeToCorrectThetaAgain + this.timeToSpeedUpX;
		timeToCorrectThetaAgainVelocity = this.timeToCorrectThetaAgainVelocity + this.timeToCorrectThetaAgain;
		timeToSlowDownX = this.timeToCorrectThetaAgainVelocity + this.timeToSlowDownX;
		timeToCorrectThetaFinal = this.timeToCorrectThetaFinal + this.timeToSlowDownX;
		timeToCorrectThetaFinalVelocity = this.timeToCorrectThetaFinalVelocity + timeToCorrectThetaFinal;
		timeToLetYFall = this.timeToCorrectThetaFinalVelocity + this.timeToLetYFall;
		timeToCorrectYVelocityBeforeLanding = this.timeToLetYFall + this.timeToCorrectYVelocityBeforeLanding;
		System.out.println("final time should be " + timeToCorrectYVelocityBeforeLanding);
		finalTime = timeToCorrectYVelocityBeforeLanding;
	}
	
	//determines which u to use at which time
	public double u() {
		double u = 0;
		
		if (timePassed <= timeToSpeedUpX && timePassed > timeToCorrectThetaFirstVelocity) {
			u = uToSpeedUpX;
			System.out.println("speeding up x");
		}
		else if (timePassed <= timeToSlowDownX && timePassed > timeToCorrectThetaAgainVelocity) {
			u = uToSlowDownX; 
			System.out.println("slowing down x");
		}
		//if the time is smaller than timeToLetYFall, and bigger than timeToCorrectThetaFinalVelocity, no thrust is applied
		else if(timePassed <= timeToCorrectYVelocityBeforeLanding && timePassed> timeToLetYFall) {
			u = uToCorrectYVelocityBeforeLanding;
			System.out.println("slowing down y");
		}
		return u;
	}
	
	//determines which v to use at which time
	public double v() {
		double v = 0;
		if (timePassed <= timeToCorrectThetaFirst) {
			v = vToCorrectThetaFirst;
			System.out.println("correcting theta first");
		}
		else if(timePassed >timeToCorrectThetaFirst && timePassed <= timeToCorrectThetaFirstVelocity) {
			v = vToCorrectThetaFirstVelocity;
			System.out.println("correcting theta vel after that");
		}
		else if (timePassed> timeToSpeedUpX && timePassed <= timeToCorrectThetaAgain) {
			v = vToCorrectThetaAgain;
			System.out.println("correcting theta again");
		}
		else if (timePassed > timeToCorrectThetaAgain && timePassed <= timeToCorrectThetaAgainVelocity) {
			v = vToCorrectThetaAgainVelocity;
			System.out.println("correcting theta vel again");
		}
		else if(timePassed > timeToSlowDownX && timePassed <= timeToCorrectThetaFinal) {
			v = vToCorrectThetaFinal;
			System.out.println("correcting theta final");
		}
		else if(timePassed > timeToCorrectThetaFinal && timePassed <= timeToCorrectThetaFinalVelocity) {
			v = vToCorrectThetaFinalVelocity;
			System.out.println("correcting theta vel final");
		}
		return v;
	}
	
	//will correct theta so y acc will be zero / close to zero, and x can be corrected
	//uses maximum side thrust
	public void correctThetaFirst() { 
		
		//calculate the theta needed such that y acc will be minimised
		double tempThetaNeeded = Math.acos(g / uMax); 
	
		double timeNeeded = 0.0001;
		double temporaryV = 1000;
		double tempMax;
		double tempNeededVel = 0;
		
		//calculate how long it takes to reach half of the desired theta with maximum thrust
		if (xTemp > 0) {
			tempThetaNeeded = -tempThetaNeeded;
			temporaryV = -temporaryV;
			tempMax = -V_MAX;
			
			while (temporaryV < tempMax) {
				tempNeededVel = ((tempThetaNeeded - thetaTemp) / timeNeeded) - thetaVelTemp;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + 0.000001;
			}
		}
		else {
			while (temporaryV > V_MAX) {
				tempNeededVel = ((tempThetaNeeded - thetaTemp) / timeNeeded) - thetaVelTemp;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + 0.000001;
			}
		}
		timeToCorrectThetaFirst = timeNeeded;
		vToCorrectThetaFirst = temporaryV;
		
		//calculate the current values
		thetaTemp = thetaTemp + thetaVelTemp * timeToCorrectThetaFirst + 0.5 * timeToCorrectThetaFirst * timeToCorrectThetaFirst * vToCorrectThetaFirst;
			
		double t = 0;
		while (t < timeToCorrectThetaFirst) {
			double yNow = yTemp + 0.5 * -g * t * t + yVelTemp * t;
			estimatedLocations.add(new Vector2D(xTemp, yNow));
			t = t + 0.1;
			
		}
		
		yTemp = yTemp + 0.5 * -g * timeToCorrectThetaFirst * timeToCorrectThetaFirst;
		yVelTemp = yVelTemp + -g * timeToCorrectThetaFirst;
		
		//now slow down the velocity so the theta is constant
		thetaVelTemp = (thetaVelTemp + vToCorrectThetaFirst * timeToCorrectThetaFirst);
		
		//assuming constant acceleration / thrust
		timeToCorrectThetaFirstVelocity = Math.abs((tempThetaNeeded - thetaTemp) / (0.5 * thetaVelTemp)); 
		vToCorrectThetaFirstVelocity = -(thetaVelTemp / timeToCorrectThetaFirstVelocity); 
		
		System.out.println("desired angle is " + tempThetaNeeded);
		System.out.println("time to correct theta first is " + timeToCorrectThetaFirst);
		System.out.println("v needed to correct theta is " + vToCorrectThetaFirst);
		System.out.println("thetaNow is " + thetaTemp);
		System.out.println("y after first theta correction is " + yTemp);
		System.out.println("yvel after first theta correction is " + yVelTemp);
		System.out.println("angularvel is " + thetaVelTemp);
		System.out.println("time to correct angular vel " + timeToCorrectThetaFirstVelocity);
		System.out.println("thrust needed to correct angular vel " + vToCorrectThetaFirstVelocity);
		
		//calculate current values
		thetaTemp = thetaTemp + 0.5 * vToCorrectThetaFirstVelocity * timeToCorrectThetaFirstVelocity *timeToCorrectThetaFirstVelocity + thetaVelTemp * timeToCorrectThetaFirstVelocity;//tempThetaNeeded;
		thetaVelTemp = thetaVelTemp + timeToCorrectThetaFirstVelocity * vToCorrectThetaFirstVelocity;
		
		t = 0;
		while (t < timeToCorrectThetaFirstVelocity) {
			double yNow = yTemp + 0.5 * -g * t * t + yVelTemp * t;
			estimatedLocations.add(new Vector2D(xTemp, yNow));
			t = t + 0.1;
			
		}
		
		yTemp = yTemp + 0.5 * -g * timeToCorrectThetaFirstVelocity * timeToCorrectThetaFirstVelocity;
		yVelTemp = yVelTemp + -g * timeToCorrectThetaFirstVelocity;
		
		System.out.println("theta at the end of the correction is " + thetaTemp);
		System.out.println("theta vel after the first complete correction " + thetaVelTemp);
		System.out.println("y after first theta vel correction is " + yTemp);
		System.out.println("yvel after first theta velcorrection is " + yVelTemp);
		System.out.println();
		System.out.println();
	}
	
	//speeds up until x has reaches 1/4th of the distance to point 0
	public void correctXSpeedUp() {
		double sNeeded = xTemp * 1/4;
		System.out.println("needed s to travel is " + sNeeded);
		
		double time = 0.0001;
		double uTemp = uMax + 10;
		double aNeeded = 0;
		double maxVel = 0;
		
		//calculate the time needed to get to sNeeded with max thrust
		while (uTemp > uMax) {
			maxVel = 2 * (sNeeded / time); //average vel is sNeeded / time, max is two times the other one in case of constant acc
			aNeeded =(maxVel)/ (time);
			uTemp = Math.abs(aNeeded / (Math.sin(thetaTemp)));
			time = time + 0.000001;
		} 
		
		timeToSpeedUpX = time;
		uToSpeedUpX = uTemp;
		xVelTemp = xVelTemp + (timeToSpeedUpX * uToSpeedUpX * Math.sin(thetaTemp));
		
		double t = 0;
		while (t < timeToSpeedUpX) {
			double yNow = yTemp + 0.5 * (uToSpeedUpX * Math.cos(thetaTemp)-g) * t * t + yVelTemp * t;
			double xNow = xTemp + 0.5 * t * t * uToSpeedUpX  * Math.sin(thetaTemp);
			estimatedLocations.add(new Vector2D(xNow, yNow));
			t = t + 0.1;
		}
		
		double tempS = 0.5 * timeToSpeedUpX * timeToSpeedUpX * uToSpeedUpX  * Math.sin(thetaTemp);
		xTemp = (xTemp + tempS);
		
		//calculate current values
		double yAcc = (uToSpeedUpX * Math.cos(thetaTemp)) - g;
		double yVelAverage = yVelTemp + 0.5 * (yAcc * timeToSpeedUpX);
		yTemp = yTemp + timeToSpeedUpX * yVelAverage;
		yVelTemp = yVelTemp + yAcc * timeToSpeedUpX;
		
		//print our requirements
		System.out.println("time to speed up x is " + timeToSpeedUpX);
		System.out.println("the s reached is " + tempS);
		System.out.println("u needed to speed up x is " + uToSpeedUpX);
		System.out.println("xTemp is " + xTemp);
		System.out.println("y after first x corr is " + yTemp);
		System.out.println("yveltemp after first x corr is " + yVelTemp);
		System.out.println();
		System.out.println();
	}
	
	//corrects the theta so it's the exact opposite as the one in the speeding up the x
	public void correctThetaForSlowingDown() { 
		//turn so it has the same angle, but faces the other way
		
		double thetaNeeded = -thetaTemp; //facing exactly the other way
		System.out.println("theta needed for slowing down is " + thetaNeeded);
		System.out.println("theta from which we start " + thetaTemp);
		
		double timeNeeded = 0.0001;
		double temporaryV = 1000;
		double tempMax;
		double tempMaxVel = 0;

		//using maximum thrust again, get to half of the desired theta distance
		if (thetaTemp >= 0) {
			temporaryV = -temporaryV;
			tempMax = -V_MAX;
			
			//there's no starting rotation velocity, so leave this out
			while (temporaryV < tempMax) {
				tempMaxVel = (((thetaNeeded - thetaTemp)/timeNeeded -thetaVelTemp));
				temporaryV = tempMaxVel / timeNeeded;
				timeNeeded = timeNeeded + 0.0000001;
			}
		}
		else {
			while (temporaryV > V_MAX) {
				tempMaxVel = (((thetaNeeded - thetaTemp)/timeNeeded) -thetaVelTemp);
				temporaryV = tempMaxVel / timeNeeded;
				timeNeeded = timeNeeded + 0.0000001;
			}
		}
		
		timeToCorrectThetaAgain = timeNeeded;
		vToCorrectThetaAgain = temporaryV;
		
		//calculate current values and the time needed to slow down
		thetaTemp = thetaTemp + thetaVelTemp * timeToCorrectThetaAgain + 0.5 * timeToCorrectThetaAgain * timeToCorrectThetaAgain * vToCorrectThetaAgain;
		thetaVelTemp = thetaVelTemp + (vToCorrectThetaAgain * timeToCorrectThetaAgain);		
		timeToCorrectThetaAgainVelocity = (thetaNeeded) / (0.5 * thetaVelTemp); //assuming constant acceleration (so thrust)
		vToCorrectThetaAgainVelocity = -(thetaVelTemp / timeToCorrectThetaAgainVelocity);
		
		double t = 0;
		while (t < (timeToCorrectThetaAgain + timeToCorrectThetaAgainVelocity)) {
			double yNow = yTemp + 0.5 * (-g) * t * t + yVelTemp * t;
			double xNow = xTemp + xVelTemp * t;
			estimatedLocations.add(new Vector2D(xNow, yNow));
			t = t + 0.1;
		}
		
		yTemp = yTemp + yVelTemp * timeToCorrectThetaAgain + 0.5 *timeToCorrectThetaAgain * timeToCorrectThetaAgain * (-g);
		yVelTemp = yVelTemp + timeToCorrectThetaAgain * -g; //as there is no thrust applied
		System.out.println("y after first half of theta corr " + yTemp);
		System.out.println("yVel after first half of theta corr + yVelTemp");
		
		System.out.println("time needed to correct theta for slowing down " + timeToCorrectThetaAgain);
		System.out.println("v needed to correct the theta of the lunar lander in this time " + vToCorrectThetaAgain);
		System.out.println("theta after correcting this again " + thetaTemp);
		System.out.println("angularvel is " + thetaVelTemp);
		System.out.println("time to correct angular vel " + timeToCorrectThetaAgainVelocity);
		System.out.println("thrust needed to correct angular vel " + vToCorrectThetaAgainVelocity);
		
		//calculate current values
		thetaTemp = thetaTemp + 0.5 * timeToCorrectThetaAgainVelocity * timeToCorrectThetaAgainVelocity *vToCorrectThetaAgainVelocity + thetaVelTemp * timeToCorrectThetaAgainVelocity;
		thetaVelTemp = (thetaVelTemp + vToCorrectThetaAgainVelocity * timeToCorrectThetaAgainVelocity);
		yTemp = yTemp + yVelTemp * timeToCorrectThetaAgainVelocity + 0.5 *timeToCorrectThetaAgainVelocity * timeToCorrectThetaAgainVelocity * (-g);
		yVelTemp = yVelTemp + timeToCorrectThetaAgainVelocity * -g; //as there is no thrust applied
		System.out.println("y after first half of theta corr " + yTemp);
		System.out.println("yVel after first half of theta corr + yVelTemp");
		System.out.println("the theta after full correction again " + thetaTemp);
		System.out.println("new angular velocity is " + thetaVelTemp);
		System.out.println();
		System.out.println();
	}
	
	//slows down until x has reached the point 0, and the velocity is 0 as well
	public void correctXSlowDown() {
		//find the current x value - the x needed to correct
		xTemp = xTemp + xVelTemp * (timeToCorrectThetaAgainVelocity + timeToCorrectThetaAgain);
		System.out.println("the velocity in the x direction is " + xVelTemp);
		System.out.println("the x location after correcting is " + xTemp);
		
		//how long is needed to slow down with constant acc
		timeToSlowDownX = Math.abs(xTemp / (0.5 * xVelTemp)); //slow down at the same rate
		System.out.println("time to slow x down such that at 0.0 it will be zero as well: "+ timeToSlowDownX);
		
		//calculate which u is needed to slow down
		double accNeeded = xVelTemp / timeToSlowDownX;
		uToSlowDownX = Math.abs(accNeeded / Math.sin(thetaTemp));
		
		double t = 0;
		while (t < timeToSlowDownX) {
			double yNow = yTemp + 0.5 * (uToSlowDownX * Math.cos(thetaTemp)-g) * t * t + yVelTemp * t;
			double xNow = xTemp + 0.5 * t * t * uToSlowDownX  * Math.sin(thetaTemp) + t * xVelTemp * t;
			estimatedLocations.add(new Vector2D(xNow, yNow));
			t = t + 0.1;
		}
		
		//calculate new values
		xTemp = xTemp + xVelTemp * timeToSlowDownX + 0.5 * timeToSlowDownX * timeToSlowDownX * uToSlowDownX *Math.sin(thetaTemp);
		xVelTemp = xVelTemp + timeToSlowDownX * uToSlowDownX * Math.sin(thetaTemp);	
		yTemp = yTemp + timeToSlowDownX * yVelTemp + 0.5 * timeToSlowDownX * timeToSlowDownX * (uToSlowDownX * Math.cos(thetaTemp) - g);
		yVelTemp = yVelTemp + timeToSlowDownX * (uToSlowDownX * Math.cos(thetaTemp) - g);
		
		System.out.println("the u needed to slow down x well enough is: " + uToSlowDownX);
		System.out.println("remaining x velocity is " + xVelTemp);
		System.out.println("remaining x is " + xTemp);
		System.out.println("y after correcting x is " + yTemp);
		System.out.println("y vel after correcting x is " + yTemp);
		System.out.println();
		System.out.println();
	}
	
	//correct theta so it's 0, and the theta velocity is 0
	public void correctThetaAgain() { 
		double thetaNeeded = 0;
		double timeNeeded = 0.001;
		double temporaryV = 1000;
		double tempMax;
		double tempMaxVel;
		
		//use maximum thrust, works the same way as the other theta corrections
		if (thetaTemp >= 0) {
			temporaryV = -temporaryV;
			tempMax = -V_MAX;
			
			//there's no starting rotation velocity, so leave this out
			while (temporaryV < tempMax) {
				tempMaxVel = (thetaNeeded - thetaTemp)/timeNeeded -thetaVelTemp;
				temporaryV = tempMaxVel / timeNeeded;
				timeNeeded = timeNeeded + 0.0000001;
			}
		}
		else {
			while (temporaryV > V_MAX) {
				tempMaxVel = (thetaNeeded - thetaTemp)/timeNeeded -thetaVelTemp;
				temporaryV = tempMaxVel / timeNeeded;
				timeNeeded = timeNeeded + 0.0000001;
			}
		}
		timeToCorrectThetaFinal = timeNeeded;
		vToCorrectThetaFinal = temporaryV;
		
		yTemp = yTemp + timeToCorrectThetaFinal * yVelTemp + 0.5 * timeToCorrectThetaFinal * timeToCorrectThetaFinal * -g;
		yVelTemp = yVelTemp + -g * timeToCorrectThetaFinal;
		
		System.out.println("theta before correcting is " + thetaTemp);
		System.out.println("time to correct theta final is " + timeToCorrectThetaFinal);
		System.out.println("v needed to correct theta final is " + vToCorrectThetaFinal);
		System.out.println("y after first part of theta correcting " + yVelTemp);
		System.out.println("y vel after first part of theta correcting " + yVelTemp);
		
		//calculate new values for the first half of the correction
		thetaTemp = thetaTemp+ thetaVelTemp *timeToCorrectThetaFinal + 0.5 * timeToCorrectThetaFinal * timeToCorrectThetaFinal * vToCorrectThetaFinal;
		thetaVelTemp = thetaVelTemp + vToCorrectThetaFinal * timeToCorrectThetaFinal;
		
		//calculates the v needed and the t needed for the last correction
		timeToCorrectThetaFinalVelocity = Math.abs((thetaTemp) / (0.5 * thetaVelTemp)); //assuming constant acceleration (so thrust)
		vToCorrectThetaFinalVelocity = -(thetaVelTemp / timeToCorrectThetaFinalVelocity);
		
		System.out.println("theta after first correction " + thetaTemp);
		System.out.println("angularvel is " + thetaVelTemp);
		System.out.println("time to correct angular vel final " + timeToCorrectThetaFirstVelocity);
		System.out.println("thrust needed to correct angular vel final " + vToCorrectThetaFirstVelocity);
		
		double t = 0;
		while (t < (timeToCorrectThetaFinal + timeToCorrectThetaFinalVelocity)) {
			double yNow = yTemp + 0.5 * (-g) * t * t + yVelTemp * t;
			double xNow = xTemp;
			estimatedLocations.add(new Vector2D(xNow, yNow));
			t = t + 0.1;
		}
		
		//calculate new values according to the last correction
		yTemp = yTemp + timeToCorrectThetaFinalVelocity * yVelTemp + 0.5 * timeToCorrectThetaFinalVelocity * timeToCorrectThetaFinalVelocity * -g;
		yVelTemp = yVelTemp + -g * timeToCorrectThetaFinalVelocity;
		thetaTemp = thetaTemp + thetaVelTemp * timeToCorrectThetaFinalVelocity + 0.5 * timeToCorrectThetaFinalVelocity * timeToCorrectThetaFinalVelocity * vToCorrectThetaFinalVelocity;
		thetaVelTemp = thetaVelTemp + vToCorrectThetaFinalVelocity * timeToCorrectThetaFinalVelocity;
		
		System.out.println("final theta is " + thetaTemp);
		System.out.println("final angular vel is "+ thetaVelTemp);
		System.out.println("y before correcting it is " + yTemp);
		System.out.println("y vel before correcting it is " + yVelTemp);
		System.out.println();
		System.out.println();
	}
	
	//freefalls for half the way through, then starts correcting so at y =0, yvel = 0 as well
	public void correctYVelocity() {	
		double sNeeded = -(yGoal / 2);
		double sNow = 0;
		timeToLetYFall = 0.000001;
		
		//find our which time is needed for the freefall
		while (sNow > sNeeded) {
			sNow = 0.5 * -g * timeToLetYFall * timeToLetYFall + yVelTemp * timeToLetYFall;
			timeToLetYFall = timeToLetYFall + 0.0001;
		}
		
		double t = 0;
		while (t < timeToLetYFall) {
			double yNow = yTemp + 0.5 * (-g) * t * t + yVelTemp * t;
			double xNow = xTemp;
			estimatedLocations.add(new Vector2D(xNow, yNow));
			t = t + 0.1;
		}
		
		//final theta is 0.0, final x is 0.0
		yTemp = yTemp + sNow; //timeToLetYFall * 0.5 * timeToLetYFall * g - yVelTemp * timeToLetYFall;
		yVelTemp = yVelTemp - g * timeToLetYFall; 
		
		System.out.println("freefall time is " + timeToLetYFall);
		System.out.println("yTemp left after freefall " + yTemp);
		System.out.println("yVelTemp after freefall " + yVelTemp);
		
		
		//calculate in which time an with which u, the y vel can be slowed down (with constant thrust)
		timeToCorrectYVelocityBeforeLanding = Math.abs((yTemp) / (0.5 * yVelTemp));
		uToCorrectYVelocityBeforeLanding = Math.abs((((yVelTemp)/ timeToCorrectYVelocityBeforeLanding))) + g;
		
		t = 0;
		while (t < (timeToCorrectYVelocityBeforeLanding + 0.1)) {
			double yNow = yTemp + 0.5 * (uToCorrectYVelocityBeforeLanding - g) * t * t + yVelTemp * t;
			double xNow = xTemp;
			estimatedLocations.add(new Vector2D(xNow, yNow));
			t = t + 0.1;
		}
		
		yTemp = yTemp + yVelTemp * timeToCorrectYVelocityBeforeLanding + timeToCorrectYVelocityBeforeLanding * 0.5 * timeToCorrectYVelocityBeforeLanding * (uToCorrectYVelocityBeforeLanding - g);
		yVelTemp = yVelTemp + timeToCorrectYVelocityBeforeLanding * (uToCorrectYVelocityBeforeLanding-g);
	
		System.out.println("thrust needed to correct y " + uToCorrectYVelocityBeforeLanding);
		System.out.println("time needed to correct y vel is " + timeToCorrectYVelocityBeforeLanding);
		System.out.println("final y velocity is " + yVelTemp);
		System.out.println("final y is " + yTemp);
		System.out.println();
		System.out.println();
	}
	
	public ArrayList<Vector2D> getLocations(){
		return estimatedLocations;
	}
	
}
