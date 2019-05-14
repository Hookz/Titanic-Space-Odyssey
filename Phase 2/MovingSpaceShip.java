package Land;

public class MovingSpaceShip extends SpaceShip2{
//to calculate how much time has passed
private static final int SEC_IN_MINUTE = 60;
private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
private static final int SEC_IN_YEAR = 31556926;
private long elapsedSeconds = 0;

public MovingSpaceShip() {
	super();
}

public MovingSpaceShip(double mass, Vector2D location, Vector2D velocity, double length, double width) {
	super(mass, location, velocity, length, width);
	
}

public double update(double timeSlice) {
     elapsedSeconds = (long) (elapsedSeconds + timeSlice);
	// reset acceleration, as it has to be calculated again
    this.resetAcceleration();

    //use the gravitational force, the wind and the tilt to calculate the acceleration for this spaceship
    this.addAccelerationByGravityForce(); //for now it's only the gravitational force - add wind and tilt when those classes are done
    
    //hardcoded for now
    Vector2D wind = new Vector2D(300, 0);
    Vector2D friction = new Vector2D(-100, -200); //can add the real air friction to this later
    this.addAccelerationByForce(wind); 
    this.addAccelerationByForce(friction);
    
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
	return copy;

}



}