package Land;


public class SpaceShip2 {

    public double mass; //in kilograms
    private Vector2D location; //in metres, at a certain point above titan. the zero point is where the rocket starts
    private Vector2D velocity; //m/s
    private Vector2D acceleration;// m/s^2
    private double length; //m
    private double width; //m
    
    //distance to titan is the y in the location vector
    //private static final double massTitan = 1.3452E+23; //kg
    ///private static final double G = 6.67E-11;
    private static final double g =  1.352; // m /s^2
    private static final double maxAcc = 9.6; // m / s^2 ??? or m/s???
    private static final double dragCo = 0.10; //assuming it's streamlined, it's an estimate
    private static final double density = 1.23995416; //density of Titan's atmosphere, kg / m^3
    
    
    private double tilt; //radians
    private double angularVelocity = 0; // rad/s
    protected static final double spinTolerance = 0.02; //radians/s
    protected static final double tiltTolerance = 0.01; //rad
    protected static final double landingXTolerance = 0.01; //m/s
    protected static final double landingYTolerance = 0.01; //m/s (for now)

    //constructors
    public SpaceShip2() {
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
    
    public SpaceShip2(double mass, Vector2D location, Vector2D velocity, double length, double width) {
        this.mass = mass;
        this.location = location;
        this.velocity = velocity;
        this.length = length;
        this.width = width;
    }

    //setters and getters
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
   
    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public double getTilt() {
        return tilt;
    }
    
    public void setLength(double length) {
		this.length = length;
	}
    
	public double getLength() {
		return length;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getWidth() {
		return width;
	}	
    
    //use the mass and distance to titan to calculate the acceleration
    public void addAccelerationByGravityForce() {
    	//Vector2D grav = new Vector2D(0, this.getGravity());
    	Vector2D grav = new Vector2D(0, g * mass); //the force
    	addAccelerationByForce(grav);
    }
    
    public void addAirResistance() {
    	//without using tilt or anything
    	double resisX = 0.5 * dragCo * density * (this.length * this.width) * this.velocity.x;
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
    
    public void checkAcceleration() {
    	if (acceleration.y > maxAcc) {
        	acceleration.y = maxAcc;
        }
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
    
    public SpaceShip2 copy() {
    	SpaceShip2 copy = new SpaceShip2();
    	copy.setMass(mass);
    	copy.setTilt(tilt);
    	copy.setAcceleration(acceleration.copy());
    	copy.setLocation(location.copy());
    	copy.setVelocity(velocity.copy());
    	copy.setLength(length);
    	copy.setWidth(width);
    	return copy;
    
    }

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
    

}
