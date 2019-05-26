package Land;
public class SpaceShip extends Wind {

    protected double mass; //kg
    private Vector2D location; //in metres, at a certain point above titan. the zero point is where the rocket starts
    private Vector2D velocity; //m/s
    private Vector2D acceleration;// m/s^2
    private double height; //m
    private double width; //m
    
    private double tilt = 0; //radians
    private double angularVelocity; // rad/s
    private double torque; //provided by the side thrusters
    public double rotationAcceleration;
    
    private static final double maxAcc = 9.6; // m / s^2 ??? or m/s???
    private static final double dragCo = 0.10; //assuming it's streamlined, it's an estimate
    private static final double density = 1.23995416; //density of Titan's atmosphere, kg / m^3
    public static final double spinTolerance = 0.02; //radians/s
    public static final double tiltTolerance = 0.01; //rad
    public static final double landingXTolerance = 0.01; //m/s
    public static final double gravTitan = 1.352; //acceleration due to gravity on titan, in ms^2

    //TODO add ytolerance

    public SpaceShip() {
        if (acceleration == null) {
            acceleration = new Vector2D();
        }
        if (velocity == null) {
            velocity = new Vector2D();
        }
        if (location == null) {
            location = new Vector2D();
        }
    }

    public SpaceShip(double mass, Vector2D location, Vector2D velocity, double height, double width) {
        super();
        this.mass = mass;
        this.location=location;
        this.velocity=velocity;
        this.height = height;
        this.width = width;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }
    
    public void setAcceleration(Vector2D acceleration) {
    	this.acceleration = acceleration;
    }
    
    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void  setVelocity(Vector2D vel) {
        velocity = vel;
    }
    
    public Vector2D getVelocity() {
        return velocity;
    }
    
    public void setLocation(Vector2D loc) {
        location = loc;
    }

    public Vector2D getLocation() {
        return location;
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
   
    //These are formulae for the acceleration
    public double calculateXAcceleration(double rotationInRads, double accelerationThruster){
        this.acceleration.x = accelerationThruster*Math.sin(rotationInRads);
        return acceleration.x;
    }

    public double calculateYAcceleration(double rotationInRads, double accelerationThruster){
        this.acceleration.y = accelerationThruster*Math.cos(rotationInRads)-gravTitan;
        return acceleration.y;
    }

    public double calculateRotationAcceleration(){
        this.rotationAcceleration = torque;
        return rotationAcceleration;
    }
    //By choosing realistic values for the power of the thrusters we can now calculate the acceleration

  //use the mass and distance to titan to calculate the acceleration
    public void addAccelerationByGravityForce() {
    	//Vector2D grav = new Vector2D(0, this.getGravity());
    	Vector2D grav = new Vector2D(0, gravTitan * mass); //the force
    	addAccelerationByForce(grav);
    }
    
    public void addAirResistance() {
    	//without using tilt or anything
    	double resisX = 0.5 * dragCo * density * (this.height * this.width) * this.velocity.x;
    	double resisY = 0.5 * dragCo * density * (this.width * this.width) * this.velocity.y;
    	Vector2D resistance = new Vector2D(-resisX, -resisY);
    	addAccelerationByForce(resistance);
    	
    }
    
  //add a force vector on a body
    public void addAccelerationByForce(Vector2D force) {
        Vector2D accByForce = new Vector2D(force);
        accByForce.div(mass);
        acceleration.add(accByForce);
    }

    //reset accelation after time period to recalculate everything
    public void resetAcceleration() {
        acceleration = new Vector2D();
    }

    //calculates new velocity and update new location
    public void updateVelocityAndLocation(double timeSlice) {
        // caluclate final velocity when the time slice has occurred
        Vector2D oldVelocity = new Vector2D(this.velocity);
        updateVelocity(timeSlice);

        // updateVelocityAndLocation location using average velocity
        Vector2D changedVelocityAverage = new Vector2D(this.velocity).sub(oldVelocity).div(2.0);
        Vector2D averageVelocity = new Vector2D(oldVelocity).add(changedVelocityAverage);
        updateLocation(timeSlice, averageVelocity);
    }

    //calculates the last of the accumulated velocity.
    protected void updateVelocity(double timeSlice) {
        Vector2D velocityByAcc = new Vector2D(acceleration).mul(timeSlice);
        if (velocity.y > maxAcc) {
        	velocity.y = maxAcc;
        }
        velocity.add(velocityByAcc);
    }

    //updates location
    protected void updateLocation(double timeSlice, Vector2D averageVelocity) {
        Vector2D locationByVelocity = new Vector2D(averageVelocity).mul(timeSlice);
        location.add(locationByVelocity);
    }
    
    public SpaceShip copy() {
    	SpaceShip copy = new SpaceShip();
    	copy.setMass(mass);
    	copy.setTilt(tilt);
    	copy.setAcceleration(acceleration.copy());
    	copy.setLocation(location.copy());
    	copy.setVelocity(velocity.copy());
    	copy.setHeight(height);
    	copy.setWidth(width);
    	return copy;
    
    }
}

