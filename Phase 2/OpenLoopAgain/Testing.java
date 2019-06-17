package OpenLoopAgain;

public class Testing {
	public static void main(String[] args) {
		//testing whether the open loop works
		Vector2D velocity = new Vector2D(0.1, 0.1);
		Vector2D acceleration = new Vector2D(0,0);
		Vector2D location = new Vector2D(10, 300*1000);
		double height = 17.5;
		double width = 4;
		boolean titan = true;
		double g = SpaceShip.GRAV_TITAN;
		long timeSlice = 1;
		
		SpaceShip spaceShip = new SpaceShip(5000, velocity, acceleration, location, height, width, titan );
		OpenLoopV4 landingTitan = new OpenLoopV4(spaceShip, g);
		
		while (location.y > 0) {
				landingTitan.update(timeSlice);
				System.out.println("location x is " + spaceShip.getLocation().x);
				System.out.println("location y is " + spaceShip.getLocation().y);
				System.out.println("velocity x is " + spaceShip.getVelocity().x);
				System.out.println("velocity y is " + spaceShip.getVelocity().y);
		}
	}
}
