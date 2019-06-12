package Mission;

//the only spaceship class we'll need

public class UltimateSpaceShip {
	
	private boolean goingToTitan = true;
	private boolean landingOnTitan = false;
	private boolean goingToEarth = false;
	private boolean landingOnEarth = false;
	
	public void update(int timeSlice) {
		if(goingToTitan) {
			
			if() {
				goingToTitan = false;
				landingOnTitan = true;
			}
		}
		else if(landingOnTitan) {
			if() {
				landingOnTitan = false;
				goingToEarth = true;
			}
		}
		else if(goingToEarth) {
			if() {
				goingToEarth = false;
				landingOnEarth = true;
			}
			
		}
		else if(landingOnEarth) {
			if(){
				landingOnEarth = false;
			}
		}
		else {
			System.out.println("No more updates");
		}
		
	}
	

}
