package OpenLoopAgain;

import ControlSystem.Vector2D;
import Land.SpaceShip;

//this class is done - just for the test

public class Testing {
	public static void main(String[] args) {
		//testing whether the open loop works
		Vector2D velocity = new Vector2D(0, 0);
		Vector2D acceleration = new Vector2D(0,0);
		Vector2D location = new Vector2D(100, 300*1000);
		double height = 17.5;
		double width = 4;
		boolean titan = true;
		double g = SpaceShip.GRAV_TITAN;
		double timeSlice = 0.01;//sec
		double timePassed = 0;
		
		
		SpaceShip spaceShip = new SpaceShip(10000, velocity, acceleration, location, height, width, titan );
		OpenLoopV4 landingTitan = new OpenLoopV4(spaceShip, g);		
		
		/*
		while (timePassed < landingTitan.timeToCorrectThetaFinalVelocity) {
				landingTitan.update(timeSlice);
				
				System.out.println("location x is " + spaceShip.getLocation().x);
				System.out.println("location y is " + spaceShip.getLocation().y);
				System.out.println("the tilt is " + spaceShip.getTilt());
				System.out.println("the angular vel is " + spaceShip.getAngularVelocity());
				System.out.println("velocity x is " + spaceShip.getVelocity().x);
				System.out.println("velocity y is " + spaceShip.getVelocity().y);
				System.out.println("the time passed is " + timePassed);
				timePassed = timePassed + timeSlice;
		}
		*/
	}
}
