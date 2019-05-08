public class SpaceShip {

    private double mass; //kg
    private double xVelocity;//
    private double yVelocity;//
    private double xLocation;//km
    private double yLocation;//km
    private double gravity;//acceleration of gravity
    private static final double massTitan = 1.3452E+23; //kg
    public static final double gravTitan = 1.352; //acceleration due to gravity on titan, in ms
    private static final double G = 6.67E-11;
    private double tilt; //radians
    private double angularVelocity; // rad/s
    private static final double spinTolerance = 0.02; //radians/s
    private static final double tiltTolerance = 0.01; //rad
    private static final double landingXTolerance = 0.01; //m/s
    private double xAcceleration;
    private double yAcceleration;
    private double torque; //provided by the side thrusters
    private double rotationAcceleration;
    //TODO add ytolerance


    public SpaceShip(double mass, double xLocation, double yLocation) {
        this.mass = mass;
        this.yLocation=yLocation;
        this.xLocation=xLocation;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMass() {
        return mass;
    }

    public void setXLocation(double xLocation) {
        this.xLocation = xLocation;
    }

    public double getXLocation() {
        return xLocation;
    }

    public void setYLocation(double yLocation) {
        this.yLocation = yLocation;
    }

    public double getYLocation() {
        return yLocation;
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

    public void setTilt(double tilt) {
        this.tilt = tilt;
    }

    public double getTilt() {
        return tilt;
    }

    public double getGravity() {
        double metersToSurface = getYLocation() * 1000;
        gravity = (getMass()*massTitan*G)/(metersToSurface*metersToSurface);
        return gravity;
    }

    //These are formulae for the acceleration
    public double calculateXAcceleration(double rotationInRads, double accelerationThruster){
        this.xAcceleration = accelerationThruster*Math.sin(rotationInRads);
        return xAcceleration;
    }

    public double calculateYAcceleration(double rotationInRads, double accelerationThruster){
        this.yAcceleration = accelerationThruster*Math.cos(rotationInRads)-gravTitan;
        return yAcceleration;
    }

    public double calculateRotationAcceleration(){
        this.rotationAcceleration = torque;
        return rotationAcceleration;
    }
    //By choosing realistic values for the power of the thrusters we can now calculate the acceleration


}

