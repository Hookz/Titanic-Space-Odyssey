package sample;


import java.sql.Time;
//TODO
/**
 * info for adding to GUI:
 * time of launch from EarthToTitan and TitanToEarth given by Trajectory.launchToTitan and Trajectory.launchToEarth, respectively
 * both above given in seconds and are public in Trajectory class
 * elapsedSeconds in main or of current bodysystem gives time of the system.
 * IDEA: while time is between launch and land use calculateVelocityFromThrust
 * POSSIBLE ISSUE: Velocity does not reach 0 when approaching titan(doesn't slow down at all)
 * POSSIBLE ISSUE: overlap with land on titan and launch from titan times as they follow each other instantly
 */
public class RocketModel {
    public static final double MASS_EMPTY =100000; //mass w/out fuel kg
    public static final double THRUST_AT_SL = 6770000; //N thrust at sea level(Rocketdyne f-1: Saturn V's rocket engine calculated with specific Impulse)
    public static final double THRUST_AT_VACUUM = 7770000; //N thrust in a vacuum (Rocketdyne f-1: Saturn V's rocket engine calculated with specific Impulse)
    public static final double FUEL_MASS_FLOW_RATE = 788; //kg/s fuel mass depletion at maximum thrust per second
    public boolean inSpace = true;
    public double massOfFuel = 4000000; // in kg
    public Vector3D velocity;
    public Vector3D location;
    public Vector3D earthToTitan;
    public Vector3D titanToEarth;
    public long launchToTitan;
    public long landOnTitan;
    public long launchToEarth;
    public long landOnEarth;
    public long secondsOfThrust = 0;
    public long timeToTitan, timeToEarth;

    public RocketModel(Vector3D location, Vector3D finalEarth, Vector3D finalTitan) {
        this.location = location;
        this.earthToTitan = finalEarth;
        this.titanToEarth = finalTitan;
        this.inSpace = true;
        this.velocity = new Vector3D(0,0,0);
    }

//    public static void main(String[] args){
//        Vector3D v = new Vector3D(0,0,0);
//        Vector3D d = new Vector3D(1,0,0);
//        RocketModel r = new RocketModel(v,v,v);
//        for(int i = 1000000; i<6000000; i+=20000){
//            System.out.println(/*(i+MASS_EMPTY)+"      "+*/r.velocity.x);
//            r.velocity.x = 0;
//            r.location.x = 0;
//            r.massOfFuel = i;
//            r.calculateVelocityFromThrust(d,100,0);
//        }
//    }

    /**
     *
     * @param directionVector vector of the direction in which thrust is to be applied.
     * @param timeSlice this is the timeslice of the calling class. Allows for the hruster to be applied in smaller increments than whatever timeSlice the
     *                  other class decides to use
     * @param time is a placeholder for the amount of time available to reach target.
     * This method calculates the velocity by using the thrust for the implemented timestep
     * by transforming the directional vector into a unit vector the undirectional magnitude of the thrust
     * can be simply applied in the wanted direction
     *
     */

    public void calculateVelocityFromThrust(final Vector3D directionVector, final long timeSlice, long time){
        Vector3D directionUnitVector;
        double thrustSlice = 1;
        double speedToReach = speedToReach(directionVector.length(), time);
        while ((velocity.length()<speedToReach || secondsOfThrust<=timeSlice) &&  massOfFuel>=0) {
            directionUnitVector = directionVector.normalize();
            if (inSpace) {
                directionUnitVector.mul((THRUST_AT_VACUUM*thrustSlice)/(massOfFuel+MASS_EMPTY));
                velocity.add(directionUnitVector);
                massOfFuel =(massOfFuel - (FUEL_MASS_FLOW_RATE * thrustSlice));
            } else {
                directionUnitVector.mul((THRUST_AT_SL * thrustSlice) / (massOfFuel + MASS_EMPTY));
                velocity.add(directionUnitVector);
                massOfFuel =(massOfFuel - (FUEL_MASS_FLOW_RATE * thrustSlice));
            }
//           if((((int)(thrustSlice*10))%10)!= 0){
                secondsOfThrust++;
//           }
        } System.out.println("fuel used: "+ (4000000-massOfFuel));
        updateLocation(velocity, timeSlice);

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

        r = Math.cbrt((Physics.G*m2*rotationalPeriod*rotationalPeriod)/(4*Math.PI*Math.PI));
        r -= body.radius; //we remove the radius of the body we are orbiting as the above equation gives us the distance from the centre of gravity
        return r;
    }

    public void updateLocation(Vector3D averageVelocity, final long timeSlice) {
        Vector3D locationByVelocity = new Vector3D(averageVelocity).mul(timeSlice);
        location.add(locationByVelocity);
    }

    //TODO implement required speed. S=v/t from SUVAT equations
    public double speedToReach(final double distance, long time){
        double velocity;
        double timeInSeconds = 60*60*24*365*2.35;
        velocity = distance/(timeInSeconds);
        return velocity;
    }
}
