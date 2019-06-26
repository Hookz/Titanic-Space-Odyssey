package OpenLoopAgain;

import java.util.ArrayList;

import ControlSystem.Vector2D;
import Land.LunarLanderLaunch;
import Land.SpaceShip;

/* Perform tests for the OpenLoopV4 class
 * As there were differences found between the stored values in 
 * 
 * 
 */

public class Testing {
	public static void main(String[] args) {
		//testing whether the open loop works
		Vector2D velocity = new Vector2D(0, 0);
		Vector2D acceleration = new Vector2D(0,0);
		
		//three test cases
		Vector2D location = new Vector2D(100, 300*1000);
		Vector2D location2 = new Vector2D(-100, 300 * 1000);
		Vector2D location3 = new Vector2D(100, 100 * 1000);
		Vector2D location4 = new Vector2D(0, 1000 * 300);
		
		double height = 17.5;
		double width = 4;
		boolean titan = true;
		double g = SpaceShip.GRAV_TITAN;
		double timeSlice = 0.01;//sec
		double timePassed = 0;
		double mass = 10000;
		
		
		SpaceShip spaceShip = new SpaceShip(mass, velocity, acceleration, location, height, width, titan);
		SpaceShip spaceShip2 = new SpaceShip(mass, velocity.copy(), acceleration.copy(), location2, height, width, titan);
		SpaceShip spaceShip3 = new SpaceShip(mass, velocity.copy(), acceleration.copy(), location3, height, width, titan);
		SpaceShip spaceShip4 = new SpaceShip(mass, velocity.copy(), acceleration.copy(), location4, height, width, titan);
		OpenLoopV4 landingTitan = new OpenLoopV4(spaceShip, g);		
		OpenLoopV4 landingTitan2 = new OpenLoopV4(spaceShip2, g);
		OpenLoopV4 landingTitan3 = new OpenLoopV4(spaceShip3, g);
		OpenLoopV4 landingTitan4 = new OpenLoopV4(spaceShip4, g);
		
		//LunarLanderLaunch launch = new LunarLanderLaunch(new Vector2D(500, 300000), spaceShip.copy(), 0.1);
		//ArrayList<Vector2D> positions = launch.fullLaunch();
		
		
		while (timePassed < landingTitan4.timeToCorrectYVelocityBeforeLanding) {
				landingTitan4.update(timeSlice);
				
				System.out.println("location x is " + spaceShip4.getLocation().x);
				System.out.println("location y is " + spaceShip4.getLocation().y);
				System.out.println("the tilt is " + spaceShip4.getTilt());
				System.out.println("the angular vel is " + spaceShip.getAngularVelocity());
				System.out.println("velocity x is " + spaceShip4.getVelocity().x);
				System.out.println("velocity y is " + spaceShip4.getVelocity().y);
				
				System.out.println("the time passed is " + timePassed);
				timePassed = timePassed + timeSlice;
		}
	}
}
