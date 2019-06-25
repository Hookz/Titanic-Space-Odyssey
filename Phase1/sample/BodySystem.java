package sample;

import ControlSystem.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BodySystem {
    private static final int SEC_IN_MINUTE = 60;
    private static final int SEC_IN_HOUR = SEC_IN_MINUTE * 60;
    private static final int SEC_IN_DAY = SEC_IN_HOUR * 24;
    private static final int SEC_IN_YEAR = 31556926;
    private long elapsedSeconds = 0;

    private List<Body> bodies;

    public BodySystem() {
        this.bodies = new ArrayList<>();
    }

    public List<Body> getBodies() {
        return this.bodies;
    }

    public void addBody(Body body) {
        this.bodies.add(body);
    }

    public double update(double timeSlice) {
        // reset acceleration so we can accumulate acceleration due to gravitation from all this.bodies
        this.bodies.stream().forEach(i -> i.resetAcceleration()) ;
        
        // add gravitation force to each body from each body
        for (int i = 0; i < this.bodies.size(); i++) {
            Body current = this.bodies.get(i);
            for (int j = i+1; j < this.bodies.size(); j++) {
                Body other = this.bodies.get(j);
                current.addAccelerationByGravityForce(other);
                other.addAccelerationByGravityForce(current);
            }
        }
        // update velocity and location for each body
        this.bodies.stream().forEach(i -> i.updateVelocityAndLocation(timeSlice)) ;
        
        //findVelocity(timeSlice);
        //findLocation(timeSlice);
        
        elapsedSeconds += timeSlice;
        return timeSlice;
    }

    //TODO: Check if this can be fixed in any way as for some reason both this.bodies.get(i).location and this.bodies.get(j).location return the same numbers (?)
    //SOURCE: https://digitalcommons.calpoly.edu/cgi/viewcontent.cgi?article=1096&context=aerosp, PAGE 10-11 Hermite Integration
    /*public double update(double timeSlice){

        Vector3D acc = new Vector3D();
        Vector3D jerk = new Vector3D();
        for (int i = 0; i < this.bodies.size(); i++) {
            this.bodies.get(i).calcAcc(this.bodies, timeSlice);
            Vector3D ai = new Vector3D();
            Vector3D ji = new Vector3D();
            for(int j = 0; j < this.bodies.size(); j++){
                if(i!=j){
                    Vector3D rji = new Vector3D(this.bodies.get(j).location.copy());
                    System.out.println(rji);
                    System.out.println(this.bodies.get(i).location);
                    rji.sub(this.bodies.get(i).location);
                    System.out.println(rji);
                    Vector3D vji = new Vector3D(this.bodies.get(j).velocity.copy().sub(this.bodies.get(i).velocity.copy()));
                    Vector3D aji = new Vector3D(rji.copy().mul(this.bodies.get(j).mass / (rji.copy().mulVec(rji.copy()).scalarProduct(rji))));
                    Vector3D jji = new Vector3D((vji.copy().sub(rji.copy().mul(3*vji.copy().scalarProduct(rji.copy())/(rji.copy().scalarProduct(vji.copy()))))).mul(rji.copy().mulVec(rji.copy()).scalarProduct(rji)));
                    ai.add(aji.copy());
                    ji.add(jji.copy());
                }
            }
            acc.add(ai.copy());
            jerk.add(ji.copy());
        }
        for(int i=0;i<this.bodies.size();i++){
            this.bodies.get(i).acceleration = new Vector3D(acc);

            //adding temporary variable for constructing complex Vectors
            Vector3D temp = new Vector3D();

            //creating the new location vector
            Vector3D newLocation = new Vector3D(this.bodies.get(i).location);
            newLocation.add(this.bodies.get(i).velocity.copy().mul(timeSlice));
            temp = this.bodies.get(i).acceleration.copy().div(2);
            newLocation.add(temp.copy().mul(timeSlice).mul(timeSlice));
            temp = jerk.copy().div(2);
            temp.mul(timeSlice);
            temp.mul(timeSlice);
            temp.mul(timeSlice);
            newLocation.add(temp.copy());
            this.bodies.get(i).location = new Vector3D(newLocation);

            //creating the new velocity vector
            Vector3D newVelocity = new Vector3D(this.bodies.get(i).velocity);
            newVelocity.add(acc.copy().mul(timeSlice));
            temp = jerk.copy().div(2);
            temp.mul(timeSlice);
            temp.mul(timeSlice);
            newVelocity.add(temp.copy());
            this.bodies.get(i).velocity = new Vector3D(newVelocity);
            //if(i==1) System.out.println(bodies.get(i));
            System.out.println(acc);
        }
        elapsedSeconds += timeSlice;
        return timeSlice;
        }*/

    //the next two methods are similar to the ones in the body, and are used to calculate the location and the velocity used when there is an already calculated acceleration
    public void findVelocity(double timeSlice) {
    	for (int i = 0; i < this.bodies.size(); i++) {
    		this.bodies.get(i).calcAcc(this.bodies, timeSlice);
    		System.out.println(this.bodies.get(i).acceleration);
    		this.bodies.get(i).velocity.x = this.bodies.get(i).velocity.x + this.bodies.get(i).acceleration.x * timeSlice;
    		this.bodies.get(i).velocity.y = this.bodies.get(i).velocity.y + this.bodies.get(i).acceleration.y * timeSlice;
    		this.bodies.get(i).velocity.z = this.bodies.get(i).velocity.z + this.bodies.get(i).acceleration.z * timeSlice;
    	}
    }
    
    public void findLocation(double timeSlice) {
    	for(int i = 0; i < this.bodies.size(); i++) {
    		this.bodies.get(i).location.x = this.bodies.get(i).location.x +  this.bodies.get(i).velocity.x * timeSlice;
    		this.bodies.get(i).location.y = this.bodies.get(i).location.y + this.bodies.get(i).velocity.y * timeSlice;
    		this.bodies.get(i).location.z = this.bodies.get(i).location.z + this.bodies.get(i).velocity.z * timeSlice;
    	}
    }

    public Optional<Body> getBody(String name) {
        return this.bodies.stream().filter(i -> i.name.equals(name)).findFirst();
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
    
    public long getSeconds() {
    	return elapsedSeconds;
    }

    public void setSeconds(long seconds) {
    	this.elapsedSeconds = seconds;
    }

}
