package GUI;

import java.util.Arrays;
import java.util.Random;
import Data.SpaceObject;

import static Data.SpaceObject.*;

/*
 * The same class as the one in the Phase1/sample package
 */

public class SolarSystem extends BodySystem {

    private static SpaceObject[] CELESTIAL_BODIES_IN_SYSTEM = new SpaceObject[] {SUN, MERCURY, VENUS, EARTH, MOON, MARS,PHOBOS , JUPITER, SATURN, TITAN, URANUS, NEPTUNE, PLUTO};


    public SolarSystem() {
        super();
        createSolarSystem();
        this.createSpheres();
    }

    private  void createSolarSystem() {
        Random rand = new Random();

        Arrays.stream(CELESTIAL_BODIES_IN_SYSTEM).forEach((spaceObject) -> {
            final Body body = spaceObject.getAsBody();

            addBody(body);
        });
    }

}
