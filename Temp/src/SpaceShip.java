import Vector3D;
public class SpaceShip {
    public static final double MASS_EMPTY =130000; //mass w/out fuel kg
    public static double massOfFuel;
    public static final double THRUST_AT_SL = 6770; //kN thrust at sea level(Rocketdyne f-1: Saturn V's rocket engine calculated with specific Impulse)
    public static final double THRUST_AT_VACUUM = 7770; //kN thrust in a vacuum (Rocketdyne f-1: Saturn V's rocket engine calculated with specific Impulse)
    public static final double FUEL_MASS_FLOW_RATE = 788; //kg/s fuel mass depletion at maximum thrust per second
    public static boolean inSpace = false;
    public static boolean thrusting = false;


    /**
     *
     * @param directionVector vector of the direction in which thrust is to be applied
     * @param isUnitVector if it is not a unit vector, transform into unit vector
     * this method calculates the velocity by using the thrust for the implemented timestep
     * by transforming the directional vector into a unit vector the undirectional magnitude of the thrust
     * can be simply applied in the wanted direction
     *
     */
    public void calculateVelocityFromThrust(final Vector3D directionVector, boolean isUnitVector){
        Vector3D directionUnitVector = new Vector3D();
        if (isUnitVector) {
            directionUnitVector = directionVector;
        } else {
            directionUnitVector = directionVector.unitVector();
        }
        if (inSpace) {
            velocity += directionUnitVector.mul((THRUST_AT_VACUUM * TIME_SLICE));
            velocity += directionUnitVector.div((MASS_EMPTY + massOfFuel));
            massOfFuel -= (FUEL_MASS_FLOW_RATE * TIME_SLICE);
        } else {

            velocity += directionUnitVector.mul((THRUST_AT_SL * TIME_SLICE));
            velocity += directionUnitVector.div((MASS_EMPTY + massOfFuel));
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
    public double calcGeostationaryOrbitHeight(final Body body, final double rotationalPeriod){
        double r;
        double m2 = body.mass;
        r = Math.cbrt((G*m2*rotationalPeriod*rotationalPeriod)/(4*Math.pi*Math.pi));
        r -= body.radius; //we remove the radius of the body we are orbiting as the above equation gives us the distance from the centre of gravity
        return r;
    }
}
