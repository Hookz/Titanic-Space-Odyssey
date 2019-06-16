package OpenLoopAgain;

public class SpaceShip extends Wind {
	//needed for the measurement of the time
	private static final int SEC_IN_MINUTE = 60;
	private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
	private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
	private static final int SEC_IN_YEAR = 31556926;
	private long elapsedSeconds = 0;

	//variables
	private double mass; //kg
	private Vector2D velocity; //in m / sec
	private Vector2D acceleration; //in m/sec^2
	private Vector2D location; //in  meters away from the goal
	private double height; //m
	private double width; //m
	
	//all zero at the start
	private double tilt = 0; //radians
	private double angularVelocity = 0; // rad/s
	private double angularAcceleration = 0; //rad/sec^2
	private double torque = 0; //provided by the side thrusters - also known as v - in newton
	private double accelerationByMainThrusters = 0; //by the main - also known as u
	
	//constants
	private static final double GRAV_TITAN = 1.352; //acceleration due to gravity on titan, in m/s^2
	private static final double GRAV_EARTH = 9.81; //acceleration due to gravity on earth, in m/s^2
	private static final double DENSITY = 1.23995416; // density of Titan's atmosphere, kg/m^3
	private static final double DRAG_CO = 0.10; //assuming it's streamlined, it's an estimate
	
	//tolerance values for the landing
	private static final double FINAL_ANGLE = 0.02; //radians
	private static final double FINAL_ANGULAR_VELOCITY = 0.01;
	private static final double LANDING_X_TOLERANCE = 0.01; //m
	private static final double FINAL_X_VELOCITY = 0.1; //m/s
	private static final double FINAL_Y_VELOCITY = 0.1; //m/s

		//standard spaceship, for more advanced ones the set methods can be used
		public SpaceShip(double mass, Vector2D velocity, Vector2D acceleration, Vector2D location, double height, double width) {
			this.mass = mass;
			this.velocity = velocity;
			this.acceleration = acceleration;
			this.location = location;
			this.height = height;
			this.width = width;
		}
	
		//setters and getters
	    public void setMass(double mass) {
	        this.mass = mass;
	    }

	    public double getMass() {
	        return mass;
	    }

	    public void  setVelocity(Vector2D vel) {
	        velocity = vel;
	    }

	    public Vector2D getVelocity() {
	        return velocity;
	    }
	    
	    public void setAcceleration(Vector2D acceleration) {
	        this.acceleration = acceleration;
	    }

	    public Vector2D getAcceleration() {
	        return acceleration;
	    }

	    public void setLocation(Vector2D location) {
	    	this.location = location;
	    }
	    
	    public Vector2D getLocation () {
	    	return location;
	    }

	    public void setXLocation(double x) {
	        location.setX(x);
	    }

	    public double getXLocation() {
	    	return location.x;
	    }

	    public void setYLocation(double y) {
	    	location.setY(y);
	    }

	    public double getYLocation() {
	        return location.y;
	    }

	    public void setXVelocity(double xVelocity){
	    	this.velocity.setX(xVelocity); 
	    }

	    public double getXVelocity(){ 
	    	return this.velocity.getX(); 
	    }

	    public void setYVelocity(double yVelocity){
	        this.velocity.setY(yVelocity);
	    }

	    public double getYVelocity(){
	        return this.velocity.getY();
	    }

	    public void addTilt(double tilt) {
	        this.tilt = this.tilt + tilt;
	    }

	    public void setTilt(double tilt) {
	        this.tilt = tilt;
	    }

	    public double getTilt() {
	        return tilt;
	    }

	    public void setHeight(double length) {
	        this.height = length;
	    }

	    public double getHeight() {
	        return height;
	    }

	    public void setWidth(double width) {
	        this.width = width;
	    }

	    public double getWidth() {
	        return width;
	    }

	    public void setTorque(double torque) {
	    	this.torque = torque;
	    }
	    
	    public double getTorque(){
	        return torque;
	    }
	    
	    public void setAngularVelocity(double angVel) {
	    	this.angularVelocity = angVel;
	    }
	    
	    public double getAngularVelocity() {
	    	return angularVelocity;
	    }
	    
	    public void setAngularAcceleration(double angAcc) {
	    	this.angularAcceleration = angAcc;
	    }
	    
	    public double getAngularAcceleration() {
	    	return angularAcceleration;
	    }
	    
	    public void setAccelerationByMainThrusters(double accMa) {
	    	this.accelerationByMainThrusters = accMa;
	    }
	    
	    public double getAccelerationByMainThrusters() {
	    	return accelerationByMainThrusters;
	    }
	    
	    public void setSeconds(long seconds) {
			elapsedSeconds = seconds;
		}
		
		public long getSeconds() {
			return elapsedSeconds;
		}
	    
	    //end of setters and getters
		
		
	    //after this, methods to calculate different forces/accelerations
	    

	    //These are formulae for the acceleration
	    public double calculateXAcceleration(/*double rotationInRads, double accelerationThruster*/){
	        //this.acceleration.setX(accelerationThruster*Math.sin(rotationInRads));
	    	this.acceleration.setX(accelerationByMainThrusters * Math.sin(tilt));
	        return this.acceleration.getX();
	    }

	    public double calculateYAcceleration(/*double rotationInRads, double accelerationThruster*/){
	        //this.acceleration.setY(accelerationThruster*Math.cos(rotationInRads)- GRAV_TITAN);
	    	this.acceleration.setY(accelerationByMainThrusters*Math.cos(tilt) - GRAV_TITAN);
	        return this.acceleration.getY();
	    }

	    public double calculateRotationAcceleration(){
	        this.angularAcceleration = torque;
	        return angularAcceleration;
	    }

	    //By choosing realistic values for the power of the thrusters we can now calculate the acceleration

	    //the wind needs to be added in the openloop, as it needs to be the same for the timesteps
	    
	    //use the mass and distance to titan to calculate the acceleration
	    public void addAccelerationByGravityForceTitan() {
	        //Vector2D grav = new Vector2D(0, this.getGravity());
	        Vector2D grav = new Vector2D(0, GRAV_TITAN * mass); //the force
	        addAccelerationByForce(grav);
	    }
	    
	    public void addAccelerationByGravityForceEarth() {
	    	Vector2D grav = new Vector2D(0, GRAV_EARTH * mass);
	    	addAccelerationByForce(grav);
	    }

	    //add the air resistance to this spaceship
	    public void addAirResistance() {
	        //without using tilt or anything
	        double resisX = 0.5 * DRAG_CO * DENSITY * (this.height * this.width) * this.velocity.getX();
	        double resisY = 0.5 * DRAG_CO * DENSITY * (this.width * this.width) * this.velocity.getY();
	        Vector2D resistance = new Vector2D(-resisX, -resisY);
	        addAccelerationByForce(resistance);

	    }

	    //add a force vector on the spaceship
	    public void addAccelerationByForce(Vector2D force) {
	        Vector2D accByForce = new Vector2D(force);
	        accByForce.div(mass);
	        acceleration.add(accByForce);
	    }	     

	    //calculate whether the landing was successful
	    public boolean successfuLanding() {
	    	if (location.y <= 0) {
	    		System.out.println("y = 0");
	    		if(FINAL_ANGLE <= this.getTilt() && FINAL_ANGULAR_VELOCITY <= this.getAngularVelocity()) {
	    			System.out.println("alse: tilt is within bounds");
	    			if (Math.abs(this.location.x) <= LANDING_X_TOLERANCE) {
	    				System.out.print("also: x location is good");
	    				if (Math.abs(this.velocity.x) <= FINAL_X_VELOCITY && Math.abs(this.velocity.y) <= FINAL_Y_VELOCITY) {
	    					System.out.println("also: velocity is small enough");
	    					System.out.println("landing succeeded");
	    					return true;
	    				}
	    			}
	    		}
	    	}
	    	return false;
	    }
	    
	    public String toString() {
	        return String.format("xAxis = %f, yAxis = %f, theta = %f", location.getX(), location.getY(), this.tilt);
	    }

	    public String getElapsedTimeAsString() {
	    	long years = elapsedSeconds / SEC_IN_YEAR;
	    	long days = (elapsedSeconds % SEC_IN_YEAR) / SEC_IN_DAY;
	    	long hours = ( (elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) / SEC_IN_HOUR;
	    	long minutes = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) / SEC_IN_MINUTE;
	    	long seconds = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) % SEC_IN_MINUTE;
	    	return String.format("Years:%08d, Days:%03d, Hours:%02d, Minutes:%02d, Seconds:%02d", years, days, hours, minutes, seconds);
	    }

	    public void addPassedTime(long timeSlice) {
	    	elapsedSeconds =+ timeSlice;
	    }
}
