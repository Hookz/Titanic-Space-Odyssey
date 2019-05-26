package Land;
import ControlSystem.Vector2D;
public class SpaceShip extends ControlSystem.SpaceShip {

    protected double mass; //kg
    private Vector2D location; //in metres, at a certain point above titan. the zero point is where the rocket starts
    
    private static final double maxAcc = 9.6; // m / s^2 ??? or m/s???
    private static final double dragCo = 0.10; //assuming it's streamlined, it's an estimate
    private static final double density = 1.23995416; //density of Titan's atmosphere, kg / m^3
    public static final double spinTolerance = 0.02; //radians/s
    public static final double tiltTolerance = 0.01; //rad
    public static final double landingXTolerance = 0.01; //m/s
    public static final double gravTitan = 1.352; //acceleration due to gravity on titan, in ms^2

    //TODO add ytolerance

    public SpaceShip() {
        if (getAcceleration() == null) {
            setAcceleration(new Vector2D());
        }
        if (getVelocity() == null) {
            setVelocity(new Vector2D());
        }
        if (location == null) {
            location = new Vector2D();
        }
    }

    public SpaceShip(double mass, Vector2D location, Vector2D velocity, double height, double width) {
        super(mass,location.getX(),location.getY(),0,height,width);
        this.location=location;
    }
   
    //These are formulae for the acceleration

    public double calculateRotationAcceleration(){
        setRotationAcceleration(getTorque());
        return getRotationAcceleration();
    }
    //By choosing realistic values for the power of the thrusters we can now calculate the acceleration

    //reset accelation after time period to recalculate everything
    public void resetAcceleration() {
        setAcceleration(new Vector2D());
    }

    //calculates new velocity and update new location
    public void updateVelocityAndLocation(double timeSlice) {
        // caluclate final velocity when the time slice has occurred
        Vector2D oldVelocity = new Vector2D(this.getVelocity());
        updateVelocity(timeSlice);

        // updateVelocityAndLocation location using average velocity
        Vector2D changedVelocityAverage = new Vector2D(this.getVelocity()).sub(oldVelocity).div(2.0);
        Vector2D averageVelocity = new Vector2D(oldVelocity).add(changedVelocityAverage);
        updateLocation(timeSlice, averageVelocity);
    }

    //calculates the last of the accumulated velocity.
    protected void updateVelocity(double timeSlice) {
        Vector2D velocityByAcc = new Vector2D(getAcceleration()).mul(timeSlice);
        if (getVelocity().getY() > maxAcc) {
        	getVelocity().setY(maxAcc);
        }
        setVelocity(getVelocity().add(velocityByAcc));
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

    public SpaceShip copy() {
    	SpaceShip copy = new SpaceShip();
    	copy.setMass(mass);
    	copy.setTilt(getTilt());
    	copy.setAcceleration(getAcceleration().copy());
    	copy.setLocation(location);
    	copy.setVelocity(getVelocity().copy());
    	copy.setHeight(getHeight());
    	copy.setWidth(getWidth());
    	return copy;
    
    }
}

