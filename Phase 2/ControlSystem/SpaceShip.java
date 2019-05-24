package ControlSystem;

import java.sql.Time;

public class SpaceShip extends Wind {

    public double mass; //kg
    private double xVelocity;//
    private double yVelocity;//
    public Vector3D coordinates;
    private double gravity;//acceleration of gravity
    public static final double MASS_TITAN = 1.3452E+23; //kg
    public static final double GRAV_TITAN = 1.352; //acceleration due to gravity on titan, in ms^2
    public static final double G = 6.67E-11;
    private double tilt = 0; //radians
    private double angularVelocity; // rad/s
    public static final double FINAL_ANGULAR_VELOCITY = 0.02; //radians/s //-0.02 //
    public static final double TILT_TOLERANCE = 0.01; //rad
    public static final double LANDING_X_TOLERANCE = 0.01; //m/s
    public static final double FINAL_X_VELOCITY = 0.1;
    private double xAcceleration;
    private double yAcceleration;
    private double torque; //provided by the side thrusters
    private double rotationAcceleration;
    private boolean landed = false;
    private double accByWind;
    private double relativeWindSpeed;
    public double force;
    public static double TIME_SLICE= 0.1;
    //TODO add ytolerance


    public SpaceShip(double mass, double xLocation, double yLocation, double zLocation) {
        super();
        this.mass = mass;
        this.coordinates = new Vector3D(xLocation,yLocation,zLocation);
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public void setXLocation(double x) {
        setCoordinates(x,getCoordinates().y,getCoordinates().z);
    }

    public double getXLocation() {
        return getCoordinates().x;
    }

    public void setZLocation(double z) {
        setCoordinates(getCoordinates().x,getCoordinates().y,z);
    }

    public double getZLocation() {
        return getCoordinates().z;
    }

    public void setYLocation(double y) {
        setCoordinates(getCoordinates().x,y,getCoordinates().z);
    }

    public double getYLocation() {
        return getCoordinates().y;
    }

    public void setCoordinates(double x, double y, double z){
        this.coordinates = new Vector3D(x,y,z);
    }

    public Vector3D getCoordinates(){
        return coordinates;
    }

    public void setXVelocity(double xVelocity){
        this.xVelocity = xVelocity;
    }

    public double getXVelocity(){
        return xVelocity;
    }

    public void setYVelocity(double yVelocity){
        this.yVelocity = yVelocity;
    }

    public double getYVelocity(){
        return yVelocity;
    }

    public void addTilt(double tilt) {
        this.tilt = this.tilt + tilt;
    }

    public double getTilt() {
        return tilt;
    }

    public double getGravity() {
        double metersToSurface = getYLocation() * 1000;
        gravity = (getMass()* MASS_TITAN *G)/(metersToSurface*metersToSurface);
        return gravity;
    }

    //These are formulae for the acceleration
    public double calculateXAcceleration(double rotationInRads, double accelerationThruster){
        this.xAcceleration = accelerationThruster*Math.sin(rotationInRads);
        return xAcceleration;
    }

    public double calculateYAcceleration(double rotationInRads, double accelerationThruster){
        this.yAcceleration = accelerationThruster*Math.cos(rotationInRads)- GRAV_TITAN;
        return yAcceleration;
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

        return String.format("xAxis = %f, yAxis = %f, theta = %f", coordinates.x, coordinates.y, coordinates.z);
    }
}

