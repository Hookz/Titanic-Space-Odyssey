package Mission;

//the only spaceship class we'll need

public class UltimateSpaceShip {
	
	private boolean goingToTitan = true;
	private boolean landingOnTitan = false;
	private boolean goingToEarth = false;
	private boolean landingOnEarth = false;
	
	private boolean tries = true; //an arbitrary boolean for now
	
	public void update(int timeSlice) {
		if(goingToTitan) {
			
			if(tries) {
				goingToTitan = false;
				landingOnTitan = true;
			}
		}
		else if(landingOnTitan) {
			if(tries) {
				landingOnTitan = false;
				goingToEarth = true;
			}
		}
		else if(goingToEarth) {
			if(tries) {
				goingToEarth = false;
				landingOnEarth = true;
			}
			
		}
		else if(landingOnEarth) {
			if(tries){
				landingOnEarth = false;
			}
		}
		else {
			System.out.println("No more updates");
		}
		
	}
	

}
