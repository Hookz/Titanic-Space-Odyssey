package Land;
import ControlSystem.Vector2D;
public class MovingSpaceShip extends SpaceShip{
//to calculate how much time has passed
private static final int SEC_IN_MINUTE = 60;
private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
private static final int SEC_IN_YEAR = 31556926;
private long elapsedSeconds = 0;

private Wind wind = new Wind();

public MovingSpaceShip() {
	super();
}

public MovingSpaceShip(double mass, Vector2D location, Vector2D velocity, double height, double width) {
	super(mass, location, velocity, height, width);
	
}

public double update(double timeSlice) {
	elapsedSeconds = (long) (elapsedSeconds + timeSlice);
	// reset acceleration, as it has to be calculated again
	wind.calcDisplacement(this, this.getLocation().getY());
	this.getVelocity().setX(this.getVelocity().getX() + wind.getWind()/1000);
    this.resetAcceleration();
   
    //use the gravitational force, the wind and the tilt to calculate the acceleration for this spaceship
    this.addAccelerationByGravityForce(); //for now it's only the gravitational force - add wind and tilt when those classes are done
    this.addAirResistance();
    
    //add the open loop system here - OpenLoop version 3
    //BUT the coordinates for this and the OpenLoop are a bit different
    //for this one, the lower you get, the bigger the y value gets, for the other one the smaller the y value gets!!!!
    //so fix this issue!!
    
    
    
    // update velocity and location for each body
    this.updateVelocityAndLocation(timeSlice);
    return timeSlice;
}

public String getElapsedTimeAsString() {
    long years = elapsedSeconds / SEC_IN_YEAR;
    long days = (elapsedSeconds % SEC_IN_YEAR) / SEC_IN_DAY;
    long hours = ( (elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) / SEC_IN_HOUR;
    long minutes = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) / SEC_IN_MINUTE;
    long seconds = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) % SEC_IN_MINUTE;
    return String.format("Years:%08d, Days:%03d, Hours:%02d, Minutes:%02d, Seconds:%02d", years, days, hours, minutes, seconds);
}

//to determine whether it was a successful landing
public boolean landingSucceeded() {
	if (this.getTilt() < this.tiltTolerance) {
		return true;
	}
	else {
		return false;
	}
}

public long getSeconds() {
	return elapsedSeconds;
}

public void setSeconds(long seconds) {
	this.elapsedSeconds = seconds;
}
public MovingSpaceShip copyMSS() {
	MovingSpaceShip copy = new MovingSpaceShip();
	copy.setMass(mass);
	copy.setSeconds(getSeconds());
	copy.setTilt(getTilt());
	copy.setAcceleration(getAcceleration().copy());
	copy.setLocation(getLocation().copy());
	copy.setVelocity(getVelocity().copy());
	copy.setHeight(this.getHeight());
	copy.setWidth(this.getWidth());
	return copy;

}



}