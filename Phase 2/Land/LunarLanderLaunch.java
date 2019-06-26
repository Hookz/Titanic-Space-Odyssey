package Land;

import java.util.ArrayList;
import ControlSystem.Vector2D;

/*
 * Simple model of the launch of the lunar lander to the spaceship
 * 
 * launch to the spaceship
 * at the spaceship, it needs to be velocity close to zero (same as the landing)
 * the lander launches at (0,0)
 * leave the wind out
 */

public class LunarLanderLaunch {
	private static Vector2D locationNeeded;
	
	private double maxAcc;
	private final double maxAccSides = 0.10 * Math.PI;
	private double timeStep;
	private double timePassed = 0;
	
	private static SpaceShip spaceship;
	private ArrayList<Vector2D> locations = new ArrayList<>();
	private Wind wind = new Wind(true);
	
	private Vector2D tempLoc = new Vector2D();
	private Vector2D tempVeloc = new Vector2D();
	
	private double theta = 0;
	private double thetaVel = 0;
	
	
	public LunarLanderLaunch(Vector2D desiredLoc, SpaceShip spaceship, double timeStep) {
		this.locationNeeded = desiredLoc;
		this.spaceship = spaceship;
		this.timeStep = timeStep;
		maxAcc = 30000 / spaceship.getMass();
	}
	
	public ArrayList<Vector2D> fullLaunch(){
		estimateDisplacementAfterLaunching();
		correctX();
		enterSpaceShip();
		return locations;
	}
	
	public void estimateDisplacementAfterLaunching() { // this step is done!!
		
		//thrust used is two times the grav acc from titan - so we never have to slow down 
		double t = timeStep;
		double sSpeedUp = 0;
		double sNeeded = 0;
		double velFinal = 0;
		while (sNeeded < locationNeeded.y) {
			sSpeedUp = SpaceShip.GRAV_TITAN * t * t * 0.5;
			double vNow = SpaceShip.GRAV_TITAN * t;
			double sSlowDown = vNow * t - 0.5 * SpaceShip.GRAV_TITAN * t * t;
			velFinal = vNow - SpaceShip.GRAV_TITAN * t;
			sNeeded = sSpeedUp + sSlowDown;
			
			t = t + timeStep;
		}
		System.out.println(sNeeded);
		System.out.println(velFinal);
		
		
		//store the locations in an arraylist
		while (tempLoc.y < sSpeedUp){
			tempLoc.y = 0.5 * timePassed * timePassed * (SpaceShip.GRAV_TITAN);
			tempVeloc.y = timePassed * (SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed +  timeStep;
		}
		
		System.out.println("needed to reach: " + sSpeedUp + " reached: " + tempLoc.y);
		System.out.println("current yvel is " + tempVeloc.y);
		System.out.println("current time is " + timePassed);
		System.out.println("current x is " + tempLoc.x);
		System.out.println();
	}
	
	public void correctX() {
		//first, correct theta such that yAcc = 0, similar to open loop!
		
		double thetaNeeded = 0.5 * Math.PI; //the y acc will be equal to g for this
		double timeNeeded = timeStep;
		double temporaryV = 1000;
		double tempMax;
		double tempNeededVel = 0;
				
		//calculate how long it takes to reach half of the desired theta with maximum thrust
		if (tempLoc.x > 0) {
			thetaNeeded = -thetaNeeded;
			temporaryV = -temporaryV;
			tempMax = -maxAccSides;
					
			while (temporaryV < tempMax) {
				tempNeededVel = thetaNeeded / timeNeeded;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + timeStep;
			}
		}
		else {
			while (temporaryV > maxAccSides) {
				tempNeededVel = thetaNeeded / timeNeeded;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + timeStep;
			}
		}
		
		//from this moment on, the wind DOES NOT AFFECT the spaceship anymore
		//calculate the time needed to get to sNeeded with max thrust
		theta = timeNeeded * 0.5 * timeNeeded * temporaryV;
		thetaVel = timeNeeded * temporaryV;
		
		System.out.println("theta needed: " + thetaNeeded);
		System.out.println("theta after first corr " + theta);
		System.out.println("theta vel after first corr " + thetaVel);
		System.out.println();
		
		//change the thetavel such that it's zero when the theta is at the theta needed for the correction of x
		
		double secondTimeNeeded= (thetaNeeded - theta) / (0.5 * thetaVel); //assuming constant acceleration (so thrust)
		temporaryV = -(thetaVel / secondTimeNeeded);
		
		theta = theta + secondTimeNeeded * 0.5 * secondTimeNeeded * temporaryV + thetaVel * secondTimeNeeded;
		thetaVel = thetaVel + secondTimeNeeded * temporaryV;
		
		
		double timeTemp = timePassed + timeNeeded + secondTimeNeeded;
		double startVelY = tempVeloc.y;
		double startY = tempLoc.y;
		double time = timeStep;
		//then calculate the y at the point it is corrected and the x at the time it's corrected - no thrust is used in the main direction at the moment
		while (timePassed< (timeTemp)){
			tempLoc.x = tempLoc.x + wind.calcDisplacement(spaceship, tempLoc.y);
			tempLoc.y = startY + startVelY * time +  0.5 * time * time * (-SpaceShip.GRAV_TITAN);
			tempVeloc.y = startVelY + time * (-SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed +  timeStep;
			time = time + timeStep;
		}
		
		System.out.println("theta after second correction is " + theta);
		System.out.println("theta vel after second correction is " + thetaVel);
		System.out.println("x after this correction is " + tempLoc.x);
		System.out.println("y after this correction is " + tempLoc.y);
		System.out.println("y vel after this correction is " + tempVeloc.y);
		System.out.println();

		
		//until here, everything is perfectly fine!!
		//from this point on, don't use the wind anymore!! displacement is done for now
		
		double uTemp = 1000;
		double sNeeded = (locationNeeded.x)/ 5; //get to one third with maximum speed
		time = timeStep;
		double maxVel, aNeeded;
		
		
		System.out.println("needed s is " + sNeeded);
		
		while (uTemp > maxAcc) {
			maxVel = 2 * (sNeeded / time); //average vel is sNeeded / time, max is two times the other one in case of constant acc
			aNeeded =(maxVel)/ (time);
			uTemp = (aNeeded / (Math.sin(theta)));
			time = time + timeStep;
		} 
		
		timeTemp = timePassed + time;
		double extraTime = 0;
		double storeY = tempLoc.y;
		double storeYVel = tempVeloc.y;
		//double xStart = tempLoc.x;
		while (timePassed < timeTemp) {
			tempLoc.x = 0.5 * uTemp *  Math.sin(theta) * extraTime * extraTime;
			tempVeloc.x = tempVeloc.x + timeStep  * uTemp * Math.sin(theta);
			tempLoc.y = storeY + storeYVel * extraTime +  0.5 * extraTime * extraTime * (-SpaceShip.GRAV_TITAN);
			tempVeloc.y = storeYVel + extraTime * (-SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed +  timeStep;
			extraTime = extraTime + timeStep;
		}
		
		System.out.println("x after this correction is " + tempLoc.x);
		System.out.println("y after this correction is " + tempLoc.y);
		System.out.println("y vel after this correction is " + tempVeloc.y);
		System.out.println("x vel is " + tempVeloc.x);
		System.out.println();
		
		//then use the same technique to change the theta in such a way it's the same but then * -1
		
		thetaNeeded = - 0.5 * Math.PI;
		timeNeeded = timeStep;
		temporaryV = 1000;
		tempMax = 0;
		tempNeededVel = 0;
				
		//calculate how long it takes to reach half of the desired theta with maximum thrust
		if (tempLoc.x > 0) {
			temporaryV = -temporaryV;
			tempMax = -maxAccSides;
					
			while (temporaryV < tempMax) {
				tempNeededVel = thetaNeeded / timeNeeded;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + timeStep;
			}
		}
		else {
			while (temporaryV > maxAccSides) {
				tempNeededVel = thetaNeeded / timeNeeded;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + timeStep;
			}
		}
		
		theta = theta + (timeNeeded * 0.5 * timeNeeded * temporaryV);
		thetaVel = thetaVel + timeNeeded * temporaryV;
		
		//then, again, correct the theta such that the velocity is zero at the point the theta you need is reached
		secondTimeNeeded= Math.abs((thetaNeeded - theta) / (0.5 * thetaVel)); //assuming constant acceleration (so thrust)
		temporaryV = -(thetaVel / secondTimeNeeded);
		
		theta = theta + secondTimeNeeded * 0.5 * secondTimeNeeded * temporaryV + thetaVel * secondTimeNeeded;
		thetaVel = thetaVel + secondTimeNeeded * temporaryV;
		System.out.println("time needed for theta corr " + (timeNeeded + secondTimeNeeded));
		System.out.println("double checking x: " + tempLoc.x + " and velX: " + tempVeloc.x);
		
		timeTemp = timePassed + timeNeeded + secondTimeNeeded;
		
		storeY = tempLoc.y;
		storeYVel = tempVeloc.y;
		time = timeStep;
		while (timePassed < (timeTemp)) {
			tempLoc.x = tempLoc.x + tempVeloc.x * timeStep;
			tempLoc.y = storeY + storeYVel * time +  0.5 * time * time * (-SpaceShip.GRAV_TITAN);
			tempVeloc.y = storeYVel + time * (-SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed  + timeStep;
			time = time + timeStep;
		}
		
		System.out.println("x after this correction is " + tempLoc.x);
		System.out.println("y after this correction is " + tempLoc.y);
		System.out.println("y vel after this correction is " + tempVeloc.y);
		System.out.println("tilt after this correction is " + theta);
		System.out.println("tilt vel after this corr is " + thetaVel);
		System.out.println();
		
		//now slow down the x
		System.out.println("needed s is " + (locationNeeded.x - tempLoc.x));
		timeNeeded = Math.abs((locationNeeded.x - tempLoc.x) / (0.5 * tempVeloc.x)); //slow down at the same rate
		System.out.println("time needed to slow down is " + timeNeeded);
		
		//calculate which u is needed to slow down
		double accNeeded = (tempVeloc.x / timeNeeded);
		uTemp = Math.abs((accNeeded / Math.sin(theta)));
		System.out.println("u needed is " + uTemp);
		
		timeTemp = timePassed + timeNeeded;
		double startVeloc = tempVeloc.x;
		double startVelocY = tempVeloc.y;
		double timePassedNow = timeStep;
		double startX = tempLoc.x;
		while (timePassed < (timeTemp)) {
			tempLoc.x = startX + startVeloc * timePassedNow + ( 0.5 * uTemp * Math.sin(theta) * timePassedNow * timePassedNow);
			tempVeloc.x = startVeloc+ timePassedNow * Math.sin(theta) * uTemp;
			tempLoc.y = tempLoc.y + startVelocY * timeStep +  0.5 * timeStep * timeStep * (-SpaceShip.GRAV_TITAN);
			tempVeloc.y = tempVeloc.y + timeStep * (-SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed +  timeStep;
			timePassedNow = timePassedNow + timeStep;
		}
		
		System.out.println("x after this correction is " + tempLoc.x);
		System.out.println("y after this correction is " + tempLoc.y);
		System.out.println("y vel after this correction is " + tempVeloc.y);
		System.out.println("x vel is " + tempVeloc.x);
		System.out.println();
	}
	
	public void enterSpaceShip() {
		//firstly, correct the theta
		//after that, make sure the velocity of the lander is zero when it reaches the spaceship
		//which should be implicitly done
	
		double sTheta = -theta;
		double timeNeeded = timeStep;
		double temporaryV = 1000;
		double tempMax = 0;
		double tempNeededVel = 0;
		
		System.out.println(sTheta);
				
		//calculate how long it takes to reach half of the desired theta with maximum thrust
		if (sTheta < 0) {
			temporaryV = -temporaryV;
			tempMax = -maxAccSides;
					
			while (temporaryV < tempMax) {
				tempNeededVel = sTheta / timeNeeded;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + timeStep;
			}
		}
		else {
			while (temporaryV > maxAccSides) {
				tempNeededVel = sTheta / timeNeeded;
				temporaryV = tempNeededVel / timeNeeded;
				timeNeeded = timeNeeded + timeStep;
			}
		}
		
		//from this moment on, the wind DOES NOT AFFECT the spaceship anymore
		//calculate the time needed to get to sNeeded with max thrust
		theta = theta + timeNeeded * 0.5 * timeNeeded * temporaryV;
		thetaVel = timeNeeded * temporaryV; //assume the thetaVel after the last time was 0
		System.out.println("theta now " + theta + " thetavel is " + thetaVel);
		
		//change the thetavel such that it's zero when the theta is at the theta needed for the correction of x
		double secondTimeNeeded = timeStep;
		
		secondTimeNeeded= (0.5 * sTheta) / (0.5 * thetaVel); //assuming constant acceleration (so thrust)
		temporaryV = -(thetaVel / secondTimeNeeded);
		
		theta = theta + secondTimeNeeded * 0.5 * secondTimeNeeded * temporaryV + thetaVel * secondTimeNeeded;
		thetaVel = thetaVel + secondTimeNeeded * temporaryV;
		
		//then calculate the y at the point it is corrected and the x at the time it's corrected - no thrust is used in the main direction at the moment
		//x will stay the same
		double timeTemp = timePassed;
		double storeYVel = tempVeloc.y;
		double timeNow = timeStep;
		double storeY = tempLoc.y;
		while (timePassed< (timeTemp + timeNeeded + secondTimeNeeded)){
			tempLoc.y = storeY + storeYVel * timeNow +  0.5 * timeNow * timeNow * (-SpaceShip.GRAV_TITAN);
			tempVeloc.y = storeYVel + timeNow * (-SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed +  timeStep;
			timeNow = timeNow + timeStep;
		}
		
		System.out.println("x after this correction is " + tempLoc.x);
		System.out.println("y after this correction is " + tempLoc.y);
		System.out.println("y vel after this correction is " + tempVeloc.y);
		System.out.println("x vel after this correction is " + tempVeloc.x);
		System.out.println("tilt after this correction is " + theta);
		System.out.println("tilt vel after this corr is " + thetaVel);
		System.out.println();
		
		
		timeTemp = timePassed;
		storeYVel = tempVeloc.y;
		timeNow = timeStep;
		storeY = tempLoc.y;
		while (tempLoc.y < (locationNeeded.y)){
			tempLoc.y = storeY + storeYVel * timeNow +  0.5 * timeNow * timeNow * (-SpaceShip.GRAV_TITAN);
			tempVeloc.y = storeYVel + timeNow * (-SpaceShip.GRAV_TITAN);
			
			locations.add(tempLoc.copy());
			timePassed = timePassed +  timeStep;
			timeNow = timeNow + timeStep;
		 }
		
		System.out.println("y after this correction is " + tempLoc.y);
		System.out.println("y vel after this correction is " + tempVeloc.y);
	}
}
