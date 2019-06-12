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
     * by transforming the directional vector into a unit vector the undirectional magnitude of the thrust can be simply applied in the wanted direction
     *
     */
    public void calculateVelocity(final Vector3D directionVector, boolean isUnitVector){
        if(thrusting) {
            if (isUnitVector) {
                Vector3D directionUnitVector = new Vector3D(directionVector);
            } else {
                Vector3D directionUnitVector = new Vector3D(directionVector.unitVector());
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
    }
}
