package GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Data.Scaling;
import javafx.scene.shape.Sphere;

/*
 * Different class than the one in the ControlSystem, this one doesn't have implementation for the RK4.
 */

public class BodySystem {
    private static final int SEC_IN_MINUTE = 60;
    private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
    private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
    private static final int SEC_IN_YEAR = 31556926;
    private long elapsedSeconds = 0;
    private List<Sphere> solarSystem;

    private List<Body> bodies;

    public BodySystem() {
        bodies = new ArrayList<>();
    }

    public List<Body> getBodies() {
        return bodies;
    }

    public void addBody(Body body) {
        bodies.add(body);
    }

    public double update(final double timeSlice) {
        // reset acceleration so we can accumulate acceleration due to gravitation from all bodies
        bodies.stream().forEach(i -> i.resetAcceleration()) ;

        // add gravitation force to each body from each body
        for (int i = 0; i < bodies.size(); i++) {
            Body current = bodies.get(i);
            for (int j = i+1; j < bodies.size(); j++) {
                Body other = bodies.get(j);
                current.addAccelerationByGravityForce(other);
                other.addAccelerationByGravityForce(current);
            }
        }
        // update velocity and location for each body
        bodies.stream().forEach(i -> i.updateVelocityAndLocation(timeSlice)) ;
        
        boolean sun = true;
        int i = 0;
        for(Sphere current: solarSystem) {
        	
			if (sun) {
				current.translateXProperty().set((bodies.get(i).location.x * Scaling.SCALE_GUI) + Scaling.WIDTH / 2);
				current.translateYProperty().set((bodies.get(i).location.y * Scaling.SCALE_GUI) + Scaling.HEIGHT / 2);
				current.translateZProperty().set(bodies.get(i).location.z * Scaling.SCALE_GUI);
				sun = false;
				i++;
			}
			else {
				current.translateXProperty().set((bodies.get(i).location.x * Scaling.SCALE_GUI) + Scaling.WIDTH / 2);
				current.translateYProperty().set((bodies.get(i).location.y * Scaling.SCALE_GUI) + Scaling.HEIGHT / 2);
				current.translateZProperty().set(bodies.get(i).location.z * Scaling.SCALE_GUI);
				i++;
			}
			
		}
        
        elapsedSeconds += timeSlice;
        return timeSlice;
    }

    public Optional<Body> getBody(String name) {
        return bodies.stream().filter(i -> i.name.equals(name)).findFirst();
    }

    public String getElapsedTimeAsString() {
        long years = elapsedSeconds / SEC_IN_YEAR;
        long days = (elapsedSeconds % SEC_IN_YEAR) / SEC_IN_DAY;
        long hours = ( (elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) / SEC_IN_HOUR;
        long minutes = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) / SEC_IN_MINUTE;
        long seconds = ( ((elapsedSeconds % SEC_IN_YEAR) % SEC_IN_DAY) % SEC_IN_HOUR) % SEC_IN_MINUTE;
        return String.format("Years:%08d, Days:%03d, Hours:%02d, Minutes:%02d, Seconds:%02d", years, days, hours, minutes, seconds);
    }
    
    public BodySystem copy() {
    	BodySystem copy = new BodySystem();
    	copy.elapsedSeconds = this.elapsedSeconds;
    	for (int i = 0; i < this.bodies.size(); i++) {
    		copy.bodies.add(this.bodies.get(i).copy());
    	}
    	return copy;
    }
    
    public void createSpheres() {
    	BodySystem3D spheres = new BodySystem3D(this);
    	solarSystem = spheres.getSphereSystem();
    }
    
    public List<Sphere> getSpheres() {
    	return solarSystem;
    }
    
    public long getSeconds() {
    	return elapsedSeconds;
    }

    public void setSeconds(long seconds) {
    	this.elapsedSeconds = seconds;
    }

}
