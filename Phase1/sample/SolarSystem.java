package sample;

import java.util.Arrays;
import java.util.Random;
import static sample.SpaceObject.*;

public class SolarSystem extends BodySystem {

    private static SpaceObject[] CELESTIAL_BODIES_IN_SYSTEM = new SpaceObject[] {SUN, MERCURY, VENUS, EARTH, MOON, MARS, JUPITER, SATURN, TITAN, URANUS, NEPTUNE, PLUTO};


    public SolarSystem() {
        super();
        createSolarSystem();
    }

    private  void createSolarSystem() {
        Random rand = new Random();

        Arrays.stream(CELESTIAL_BODIES_IN_SYSTEM).forEach((spaceObject) -> {
            final Body body = spaceObject.getAsBody();

            addBody(body);
        });
    }

}
