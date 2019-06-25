package sample;

import java.util.ArrayList;
import java.util.List;

public class Body {
    public Vector3D location;
    public Vector3D velocity;
    public Vector3D acceleration;

    // miss is in kg
    public double mass;

    //radius
    public double radius;

    public String name;

    public Body() {
        if (acceleration == null) {
            acceleration = new Vector3D();
        }
        if (velocity == null) {
            velocity = new Vector3D();
        }
        if (location == null) {
            location = new Vector3D();
        }
    }

    public Body(Vector3D location, Vector3D velocity, double radius, double mass) {
        this();
        this.location = location;
        this.velocity = velocity;
        this.radius = radius;
        this.mass = mass;
    }

    public double getRadius(){
        return radius;
    }
    public Vector3D getAcceleration() {
        return acceleration;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public Vector3D getLocation() {
        return location;
    }

    //add a force vector on a body
    public void addAccelerationByForce(Vector3D force) {
        Vector3D accByForce = new Vector3D(force);
        accByForce.div(mass);
        acceleration.add(accByForce);
    }



    //Calculates the gravitational force between this body and another body and accumulates the force.
    public void addAccelerationByGravityForce(Body other) {
        addAccelerationByForce(calculateGravitationalForce(other));
    }

    //reset accelation after time period to recalculate everything
    public void resetAcceleration() {
        acceleration = new Vector3D();
    }
    
    //calculates new velocity and update new location
    public void updateVelocityAndLocation(double timeSlice) {
        // caluclate final velocity when the time slice has occurred
        Vector3D oldVelocity = new Vector3D(this.velocity);
        updateVelocity(timeSlice);
    	
    	//updateVelocityAndLocationRK4(timeSlice); //when runga kutta is used
    	
        //updateVelocityAndLocation location using average velocity
        Vector3D changedVelocityAverage = new Vector3D(this.velocity).sub(oldVelocity).div(2.0);
        Vector3D averageVelocity = new Vector3D(oldVelocity).add(changedVelocityAverage);
        updateLocation(timeSlice, averageVelocity);
    }
	
    //gravity between this and another object
    protected Vector3D calculateGravitationalForce(Body other) {
        // get direction vector
        Vector3D directionVect = new Vector3D(this.location);
        directionVect.sub(other.location).normalize().mul(-1);

        // distance between bodies
        double r = this.location.distance(other.location);

        // calculate force
        Vector3D grativationalForce = new Vector3D(directionVect);
        grativationalForce.mul(Physics.G).mul(this.mass).mul(other.mass).div(r*r);

        return grativationalForce;
    }
    
     //does not use runga kutta
    //calculates the last of the accumilated velocity.
    protected void updateVelocity(double timeSlice) {
        Vector3D velocityByAcc = new Vector3D(acceleration).mul(timeSlice);
        velocity.add(velocityByAcc);
    }

    //updates location
    protected void updateLocation(double timeSlice, Vector3D averageVelocity) {
        Vector3D locationByVelocity = new Vector3D(averageVelocity).mul(timeSlice);
        location.add(locationByVelocity);
    }
    
    
    //uses rungaKuttaFourthOrder to calculate the acceleration
    //most of this code is based on the code retrievable on: http://www.cyber-omelette.com/2017/02/RK4.html
    public void calcAcc(List<Body> bodies, double timeSlice) {
    	this.acceleration = new Vector3D(0, 0, 0);
    	
    	Vector3D kOne = new Vector3D(0, 0, 0);
    	Vector3D kTwo = new Vector3D(0, 0, 0);
    	Vector3D kThree = new Vector3D(0, 0, 0);
    	Vector3D kFour = new Vector3D(0, 0, 0);
    	
    	Vector3D locationNow = new Vector3D(0, 0, 0);
    	Vector3D velocityNow = new Vector3D(0, 0, 0);
    	
    	for (int j = 0; bodies.size() > j; j++) {
    		if (!this.name.equalsIgnoreCase(bodies.get(j).name)) {
    			
    			double temp = Math.pow((this.location.x - bodies.get(j).location.x),2);
    			double secondTemp= Math.pow((this.location.y - bodies.get(j).location.y),2);
    			double thirdTemp = Math.pow((this.location.z - bodies.get(j).location.z),2);
    			double all = temp + secondTemp + thirdTemp;
    			double attr = Math.sqrt(all);
    			double attrPower = Math.pow(attr, 3);
    			double accl =  (Physics.G * bodies.get(j).mass / attrPower);
    			
    			kOne.x = accl * (bodies.get(j).location.x - this.location.x);
    			kOne.y = accl * (bodies.get(j).location.y - this.location.y);
    			kOne.z = accl * (bodies.get(j).location.z - this.location.z);
    			
    			velocityNow.x = this.velocity.x + kOne.x * 0.5;
    			velocityNow.y = this.velocity.y + kOne.y * 0.5;
    			velocityNow.z = this.velocity.z + kOne.z * 0.5;
    			
    			locationNow.x = this.location.x + velocityNow.x * 0.5 * timeSlice;
    			locationNow.y = this.location.y + velocityNow.y * 0.5 * timeSlice;
    			locationNow.z = this.location.z + velocityNow.z * 0.5 * timeSlice;
    			
    			kTwo.x = accl * (bodies.get(j).location.x - locationNow.x);
    			kTwo.y = accl * (bodies.get(j).location.y - locationNow.y);
    			kTwo.z = accl * (bodies.get(j).location.z - locationNow.z);
    			
    			velocityNow.x = this.velocity.x + kTwo.x * 0.5;
    			velocityNow.y = this.velocity.y + kTwo.y * 0.5;
    			velocityNow.z = this.velocity.z + kTwo.z * 0.5;
    					
    			locationNow.x = this.location.x + velocityNow.x * 0.5 * timeSlice;
    			locationNow.y = this.location.y + velocityNow.y * 0.5 * timeSlice;
    			locationNow.z = this.location.z + velocityNow.z * 0.5 * timeSlice;
    			
    			kThree.x = accl * (bodies.get(j).location.x - locationNow.x);
    			kThree.y = accl * (bodies.get(j).location.y - locationNow.y);
    			kThree.z = accl * (bodies.get(j).location.x - locationNow.z);
    			
    			velocityNow.x = this.velocity.x + kThree.x;
    			velocityNow.y = this.velocity.y + kThree.y;
    			velocityNow.z = this.velocity.z + kThree.z;
    			
    			locationNow.x = this.location.x + velocityNow.x * timeSlice;
    			locationNow.y = this.location.y + velocityNow.y * timeSlice;
    			locationNow.z = this.location.z + velocityNow.z * timeSlice;
    	
    			kFour.x = accl * (bodies.get(j).location.x - locationNow.x);
    			kFour.y = accl * (bodies.get(j).location.y - locationNow.y );
    			kFour.z = accl * (bodies.get(j).location.z - locationNow.z );
    			
    			this.acceleration.x = this.acceleration.x + ((kOne.x + 2 * kTwo.x + 2 * kThree.x + kFour.x) / 6);
    			this.acceleration.y = this.acceleration.y + ((kOne.y + 2 * kTwo.y + 2 * kThree.y + kFour.y) / 6);
    			this.acceleration.z = this.acceleration.z + ((kOne.z + 2 * kTwo.z + 2 * kThree.z + kFour.z) / 6);
    		}
    	}
    }
    
    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(String.format("-----------------------------------------------------------------------")).append('\n');
        buf.append(String.format("\n%s", name)).append('\n');
        buf.append(String.format("mass = %f", mass)).append('\n');
        buf.append(String.format("loc_x = %f, loc_y = %f, loc_z = %f", location.x, location.y, location.z)).append('\n');
        buf.append(String.format("vel_x = %f, vel_y = %f, vel_z = %f", velocity.x, velocity.y, velocity.z)).append('\n');
        buf.append(String.format("acc_x = %f, acc_y = %f, acc_z = %f", acceleration.x, acceleration.y, acceleration.z)).append('\n');
        return buf.toString();
    }
    
    public Body copy() {
    	Body copy = new Body();
    	copy.location = this.location.copy();
        copy.velocity = this.velocity.copy();
        copy.acceleration = this.acceleration.copy();
        copy.mass = this.mass;
        copy.radius = this.radius;
        copy.name = this.name;
        copy.acceleration = this.acceleration.copy();
        return copy;
    }
}
