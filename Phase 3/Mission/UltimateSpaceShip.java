package Mission;

//the only spaceship class we'll need

public class UltimateSpaceShip extends Wind{
	//for the time:
	private static final int SEC_IN_MINUTE = 60;
	private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
	private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
	private static final int SEC_IN_YEAR = 31556926;
	private long elapsedSeconds = 0;
	private double timeSlice;

	//about the spaceship itself
	  public double mass; //kg
	  private double height; //m
    private double width; //m
    public final double MASS_EMPTY =130000; //mass w/out fuel kg
    public double massOfFuel;
    public boolean inSpace = true;
    public boolean thrusting = false;
    protected Vector3D velocity;
    protected Vector3D acceleration;
    protected Vector3D coordinates;
    private Vector2D location; //in metres, at a certain point above titan. the zero point is where the rocket starts
    private double tilt = 0; //radians

    public static final double THRUST_AT_SL = 6770; //kN thrust at sea level(Rocketdyne f-1: Saturn V's rocket engine calculated with specific Impulse)
    public static final double THRUST_AT_VACUUM = 7770; //kN thrust in a vacuum (Rocketdyne f-1: Saturn V's rocket engine calculated with specific Impulse)
    public static final double FUEL_MASS_FLOW_RATE = 788; //kg/s fuel mass depletion at maximum thrust per second
    public static final double FINAL_ANGULAR_VELOCITY = 0.02; //radians/s //-0.02 //
    public static final double TILT_TOLERANCE = 0.01; //rad
    public static final double LANDING_X_TOLERANCE = 0.01; //m/s
    public static final double FINAL_X_VELOCITY = 0.1;

    private static final double DRAG_CO = 0.10; //assuming it's streamlined, it's an estimate
    private double torque; //provided by the side thrusters
    public static final double spinTolerance = 0.02; //radians/s
    public static final double tiltTolerance = 0.01; //rad

    //about titan or space:
    private double gravity;//acceleration of gravity
    public static final double MASS_TITAN = 1.3452E+23; //kg
    public static final double GRAV_TITAN = 1.352; //acceleration due to gravity on titan, in ms^2
    public static final double G = 6.67E-11;
    private static final double DENSITY = 1.23995416; // density of Titan's atmosphere, kg/m^3
    public static final double gravTitan = 1.352; //acceleration due to gravity on titan, in ms^2
    private static final double maxAcc = 9.6; // m / s^2 ??? or m/s???

    private double rotationAcceleration;
    private double accByWind;
    private double relativeWindSpeed;
    public double force;

    //to control which update is used
	private boolean goingToTitan = true;
	private boolean landingOnTitan = false;
	private boolean goingToEarth = false;
	private boolean landingOnEarth = false;

	private boolean tries = true; //an arbitrary boolean for now

	public UltimateSpaceShip() {
		if (this.coordinates == null) {
			this.coordinates = new Vector3D();
		}
		if (this.velocity == null) {
			this.velocity = new Vector3D();
		}
		if (this.acceleration == null) {
			this.acceleration = new Vector3D();
		}
	}

	public UltimateSpaceShip(double mass, Vector3D coordinates, Vector3D velocity, double height, double width, double timeSlice) {
		this.mass = mass;
		this.coordinates = coordinates;
		this.velocity = velocity;
		this.height = height;
		this.width = width;
		this.timeSlice = timeSlice;

	}

	public void update(int timeSlice) {
		if(goingToTitan) {
			//trajectory to titan


			if(tries) {
				goingToTitan = false;
				landingOnTitan = true;
			}
		}
		else if(landingOnTitan) {
			//add the phase 2 stuff here - so it updates every time slice


			if(tries) {
				landingOnTitan = false;
				goingToEarth = true;
			}
		}
		else if(goingToEarth) {
			//trajectory back to earth


			if(tries) {
				goingToEarth = false;
				landingOnEarth = true;
			}

		}
		else if(landingOnEarth) {
			//also phase 2, yet now it has to be changed for earth


			if(tries){
				landingOnEarth = false;
			}
		}
		else {
			System.out.println("No more updates");
		}
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

	public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public void setAcceleration(Vector3D acceleration) {
        this.acceleration = acceleration;
    }

    public Vector3D getAcceleration() {
        return acceleration;
    }

    public void  setVelocity(Vector3D vel) {
        velocity = vel;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    //change the coordinates at only one point
    public void setXLocation(double x) {
        setCoordinates(x,getCoordinates().getY(),getCoordinates().getZ());
    }

    public double getXLocation() { return getCoordinates().getX(); }

    public void setZLocation(double z) {
        setCoordinates(getCoordinates().getX(),getCoordinates().getY(),z);
    }

    public double getZLocation() {
        return getCoordinates().getZ();
    }

    public void setYLocation(double y) {
        setCoordinates(getCoordinates().getX(),y,getCoordinates().getZ());
    }

    public double getYLocation() {
        return getCoordinates().getY();
    }

    //setter and getter for coordinates
    public void setCoordinates(double x, double y, double z){
        this.coordinates = new Vector3D(x,y,z);
    }

    public Vector3D getCoordinates(){
        return coordinates;
    }

    //more setters and getters
    public void setXVelocity(double xVelocity){ this.velocity.setX(xVelocity); }

    public double getXVelocity(){ return this.velocity.getX(); }

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

    public double getRotationAcceleration(){
        return rotationAcceleration;
    }
    public void setRotationAcceleration(double rA){
        this.rotationAcceleration = rA;
    }
    public double getTorque(){
        return torque;
    }
    public double getWidth() {
        return width;
    }



    //calculate - to get the forces and accelerations - for the landing?
    public double getGravity() {
        double metersToSurface = getYLocation() * 1000;
        gravity = (getMass()* MASS_TITAN *G)/(metersToSurface*metersToSurface);
        return gravity;
    }

    //These are formulae for the acceleration
    public double calculateXAcceleration(double rotationInRads, double accelerationThruster){
        this.acceleration.setX(accelerationThruster*Math.sin(rotationInRads));
        return this.acceleration.getX();
    }

    public double calculateYAcceleration(double rotationInRads, double accelerationThruster){
        this.acceleration.setY(accelerationThruster*Math.cos(rotationInRads)- GRAV_TITAN);
        return this.acceleration.getY();
    }

    public double calculateRotationAcceleration(){
        this.rotationAcceleration = torque;
        return rotationAcceleration;
    }


    //By choosing realistic values for the power of the thrusters we can now calculate the acceleration

    public double accByWind(double kmtosurface){
        calcWindSpeed(kmtosurface);
        calculateForce();
        accByWind = this.force/this.getMass();
        System.out.println(this.getAccByWind());
        return accByWind;
    }


    //use the mass and distance to titan to calculate the acceleration
    public void addAccelerationByGravityForce() {
        //Vector2D grav = new Vector2D(0, this.getGravity());
        Vector3D grav = new Vector3D(0, GRAV_TITAN * mass ,0); //the force
        addAccelerationByForce(grav);
    }

    public void addAirResistance() {
        //without using tilt or anything
        double resisX = 0.5 * DRAG_CO * DENSITY * (this.height * this.width) * this.velocity.getX();
        double resisY = 0.5 * DRAG_CO * DENSITY * (this.width * this.width) * this.velocity.getY();
        Vector3D resistance = new Vector3D(-resisX, -resisY, 0);
        addAccelerationByForce(resistance);

    }

    //add a force vector on a body
    public void addAccelerationByForce(Vector3D force) {
        Vector3D accByForce = new Vector3D(force);
        accByForce.div(mass);
        acceleration.add(accByForce);
    }

    public void calculateForce(){
        calculateRelativeWindSpeed();
        this.force = (area/2)*airDensity*(relativeWindSpeed*relativeWindSpeed);
        if (this.getRelativeWindSpeed()<0)
            this.force = -this.force;

    }

    public void calculateRelativeWindSpeed(){
        relativeWindSpeed = this.getWind()-this.getXVelocity();
    }

    public double calcDisplacement(double kmtosurface){
        //System.out.println(accByWind(s, ));
        double displacement = this.getXVelocity()*timeSlice + 0.5*(accByWind(kmtosurface)) * timeSlice*timeSlice;
        return displacement;
    }

    public double calcTilt(double kmToSurface){
        double randTilt = (Math.random()*10);
        //Make zones, such that the further away from titan, the higher the tilt can be
        if (kmToSurface <= 48)
            randTilt = 3* randTilt;
        else if (kmToSurface <= 72)
            randTilt = 4.5* randTilt;
        else if (kmToSurface <= 96)
            randTilt = 6* randTilt;
        else
            randTilt = 9* randTilt;

        double tiltInRadians = Math.toRadians(randTilt);

        if (this.getWind()<0)
            this.addTilt(tiltInRadians);
        else
            this.addTilt(-tiltInRadians);

        return tiltInRadians;
    }

    public double getAccByWind() {
        return this.accByWind;
    }
    public double getRelativeWindSpeed(){
        return relativeWindSpeed;
    }
    //tolerance getters
    public double getFinalAngularVelocity() {
        return FINAL_ANGULAR_VELOCITY;
    }

     public double getTiltTolerance() {
        return TILT_TOLERANCE;
     }

     public double getLandingXTolerance() {
        return LANDING_X_TOLERANCE;
     }

     public double getFinalXVelocity() {
        return FINAL_X_VELOCITY;
     }

    public double getForce() {
        return force;
    }

    //By choosing realistic values for the power of the thrusters we can now calculate the acceleration

    //reset accelation after time period to recalculate everything
    public void resetAcceleration() {
        setAcceleration(new Vector3D());
    }

    //calculates new velocity and update new location
    public void updateVelocityAndLocation(double timeSlice) {
        // caluclate final velocity when the time slice has occurred
        Vector3D oldVelocity = new Vector3D(this.getVelocity());
        updateVelocity(timeSlice);

        // updateVelocityAndLocation location using average velocity
        Vector3D changedVelocityAverage = new Vector3D(this.getVelocity()).sub(oldVelocity).div(2.0);
        Vector3D averageVelocity = new Vector3D(oldVelocity).add(changedVelocityAverage);

        Vector2D removeZ = new Vector2D(averageVelocity.getX(), averageVelocity.getY());
        updateLocation(timeSlice, removeZ);
    }

    //calculates the last of the accumulated velocity.
    //TODO add directional vector towards titan so thrust in is the proper direction
    protected void updateVelocity(double timeSlice) {
        Vector3D velocityByAcc = new Vector3D(getAcceleration()).mul(timeSlice);
        if (getVelocity().getY() > maxAcc) {
        	getVelocity().setY(maxAcc);
        }
        setVelocity(getVelocity().add(velocityByAcc));
//        if(thrusting){
//            calculateVelocityFromThrust(direcitonVec, true/false);
//        }
    }

    //updates location
    protected void updateLocation(double timeSlice, Vector2D averageVelocity) {
        Vector2D locationByVelocity = new Vector2D(averageVelocity).mul(timeSlice);
        location.add(locationByVelocity);
    }

    public Vector2D getLocation() {
        return location;
    }

    public void setLocation(Vector2D loc) {
        location = new Vector2D(loc);
    }

    public String toString() {
        return String.format("xAxis = %f, yAxis = %f, theta = %f", coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }

    //needed to store multiple versions of the spaceship
	public UltimateSpaceShip copyUSS() {
		UltimateSpaceShip copy = new UltimateSpaceShip();
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
    /**
     *
     * @param directionVector vector of the direction in which thrust is to be applied
     * @param isUnitVector if it is not a unit vector, transform into unit vector
     * this method calculates the velocity by using the thrust for the implemented timestep
     * by transforming the directional vector into a unit vector the undirectional magnitude of the thrust
     * can be simply applied in the wanted direction
     *
     */
    public void calculateVelocityFromThrust(final Vector3D directionVector, boolean isUnitVector) {
        Vector3D directionUnitVector;
        if (isUnitVector) {
            directionUnitVector = directionVector;
        } else {
            directionUnitVector = directionVector.unitVector();
        }
        if (inSpace) {
            velocity.add(directionUnitVector.mul(THRUST_AT_VACUUM * TIME_SLICE));
            velocity.add(directionUnitVector.div(MASS_EMPTY + massOfFuel));
            massOfFuel -= (FUEL_MASS_FLOW_RATE * TIME_SLICE);
        } else {

            velocity.add(directionUnitVector.mul(THRUST_AT_SL * TIME_SLICE));
            velocity.add(directionUnitVector.div(MASS_EMPTY + massOfFuel));
            massOfFuel -= (FUEL_MASS_FLOW_RATE * TIME_SLICE);
        }
    }
    /**
     *
     * @param body the planet for which we will be in geostationary orbit. From this we will require mass and radius of the planet
     * @param rotationalPeriod we want this to be the same as the body's rotation on its axis. This effectively removes relative movement  of our spaceShip to the surface of the body
     * @return the height from the surface of the planet required for geostationary orbit
     * this equation is derived from centripetal force acceleration: m1(v^2)/r and the gravitational force on a satellite: G*m1*m2/r^2
     * this can be used to find the starting height for the lunar landing module.
     */

    //TODO import Body somehow as we need it for the trajectory to titan
    /*
    public double calcGeostationaryOrbitHeight(final Body body, final double rotationalPeriod){
        double r;
        double m2 = body.mass;
        r = Math.cbrt((G*m2*rotationalPeriod*rotationalPeriod)/(4*Math.PI*Math.PI));
        r -= body.radius; //we remove the radius of the body we are orbiting as the above equation gives us the distance from the centre of gravity
        return r;
    }
    */
}
