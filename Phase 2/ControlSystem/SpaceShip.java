package ControlSystem;

import java.sql.Time;

public class SpaceShip extends Wind {

    public double mass; //kg
    protected Vector2D velocity;
    protected Vector2D acceleration;
    protected Vector3D coordinates;
    private double gravity;//acceleration of gravity
    public static final double MASS_TITAN = 1.3452E+23; //kg
    public static final double GRAV_TITAN = 1.352; //acceleration due to gravity on titan, in ms^2
    public static final double GRAV_EARTH = 9.81;
    public static final double MASS_EARTH = 5.972E+24;
    public static final double G = 6.67E-11;
    private static final double DENSITY_TITAN = 1.23995416; // density of Titan's atmosphere, kg/m^3
    private static final double DENSITY_EARTH = 1.2041;
    private static final double MAX_ACCELERATION = 9.6; // m/s^2
    private double tilt = 0; //radians
    private double angularVelocity; // rad/s
    public static final double FINAL_ANGULAR_VELOCITY = 0.02; //radians/s //-0.02 //
    public static final double TILT_TOLERANCE = 0.01; //rad
    public static final double LANDING_X_TOLERANCE = 0.01; //m/s
    public static final double FINAL_X_VELOCITY = 0.1;
    private static final double DRAG_CO = 0.10; //assuming it's streamlined, it's an estimate
    private double torque; //provided by the side thrusters
    private double rotationAcceleration;
    private boolean landed = false;
    private double accByWind;
    private double relativeWindSpeed;
    public double force;
    public static double TIME_SLICE= 0.1;
    private double height; //m
    private double width; //m
    private boolean titan;
    //TODO add ytolerance


    public SpaceShip(boolean titan){
    	super(titan);
        this.mass = 0;
        this.coordinates = new Vector3D(0,0,0);
        if (acceleration == null) {
            acceleration = new Vector2D();
        }
        if (velocity == null) {
            velocity = new Vector2D();
        }
        if (coordinates == null) {
            coordinates = new Vector3D();
        }
        this.height = 0;
        this.width = 0;
    }
    public SpaceShip(double mass, double xLocation, double yLocation, double zLocation, double height, double width, boolean titan) {
        super(titan);
        this.mass = mass;
        this.coordinates = new Vector3D(xLocation,yLocation,zLocation);
        this.height = height;
        this.width = width;
        this.acceleration = new Vector2D();
        this.velocity = new Vector2D();
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

    public void setCoordinates(double x, double y, double z){
        this.coordinates = new Vector3D(x,y,z);
    }

    public Vector3D getCoordinates(){
        return coordinates;
    }

    public Vector2D getLocation() {
        return new Vector2D(coordinates.getX(),coordinates.getY());
    }

    public void setLocation(Vector2D loc) {
        setCoordinates(loc.getX(),loc.getY(),coordinates.getZ());
    }

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
        Vector2D grav = new Vector2D(0, GRAV_TITAN * mass); //the force
        addAccelerationByForce(grav);
    }

    public void addAirResistance() {
        //without using tilt or anything
    	double resisX, resisY;
    	if (titan) {
    		resisX = 0.5 * DRAG_CO * DENSITY_TITAN * (this.height * this.width) * this.velocity.getX();
    		resisY = 0.5 * DRAG_CO * DENSITY_TITAN * (this.width * this.width) * this.velocity.getY();
    	}
    	else {
    		resisX = 0.5 * DRAG_CO * DENSITY_EARTH * (this.height * this.width) * this.velocity.getX();
    		resisY = 0.5 * DRAG_CO * DENSITY_EARTH * (this.width * this.width) * this.velocity.getY();
    	}
        Vector2D resistance = new Vector2D(-resisX, -resisY);
        addAccelerationByForce(resistance);

    }

    //add a force vector on a body
    public void addAccelerationByForce(Vector2D force) {
        Vector2D accByForce = new Vector2D(force);
        accByForce.div(mass);
        acceleration.add(accByForce);
    }

    public void calculateForce(){
        calculateRelativeWindSpeed();
        if (titan) {
        	this.force = (area/2)*DENSITY_TITAN*(relativeWindSpeed*relativeWindSpeed);
        }
        else {
        	this.force = (area/2)*DENSITY_EARTH*(relativeWindSpeed*relativeWindSpeed);
        }
        
        if (this.getRelativeWindSpeed()<0)
            this.force = -this.force;

    }

    public void calculateRelativeWindSpeed(){
        relativeWindSpeed = this.getWind()-this.getXVelocity();
    }

    public double calcDisplacement(double kmtosurface){
        //System.out.println(accByWind(s, ));
        double displacement = this.getXVelocity()*TIME_SLICE + 0.5*(accByWind(kmtosurface)) * TIME_SLICE*TIME_SLICE;
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


    public String toString() {

        return String.format("xAxis = %f, yAxis = %f, theta = %f", coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }
	public SpaceShip copy() {
		SpaceShip copy = new SpaceShip(this.mass, this.getXLocation(), this.getYLocation(), this.getTilt(), this.height, this.width, this.titan);
		copy.setAcceleration(acceleration.copy());
		copy.setVelocity(this.velocity.copy());
		copy.setRotationAcceleration(this.rotationAcceleration);
		return copy;
	}
}

