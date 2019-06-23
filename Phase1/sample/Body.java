package sample;

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

    protected void updateVelocityAndLocationRK4(double timeSlice) {
    	Vector3D kOneLoc = this.velocity.copy();
    	Vector3D kOneVel = (this.acceleration.copy()).mulVec((this.location.copy()));
    	
    	Vector3D kTwoLoc = this.velocity.copy().mulVec(kOneVel.copy().mul(timeSlice / 2));
    	Vector3D kTwoVel = this.acceleration.copy().mulVec(this.location.copy().add(kOneLoc).copy().mul(timeSlice/2));
    	
    	Vector3D kThreeLoc = this.velocity.copy().mulVec(kTwoVel.copy().mul(timeSlice/2));
    	Vector3D kThreeVel = (this.acceleration.copy()).mulVec((this.location.copy()).add(kTwoLoc.copy().mul(timeSlice / 2)));
    	
    	Vector3D kFourLoc = this.velocity.copy().mulVec(kThreeVel.copy().mul(timeSlice));
    	Vector3D kFourVel = (this.acceleration.copy()).mulVec((this.location.copy()).add(kThreeLoc.copy().mul(timeSlice)));
    	
    	Vector3D kAllVel = kOneVel.add((kTwoVel.mul(2)).add((kThreeVel).mul(2)).add(kFourVel));
    	Vector3D kAllLoc = kOneLoc.add((kTwoLoc.mul(2)).add((kThreeLoc).mul(2)).add(kFourLoc));
    	this.velocity = this.velocity.add((kAllVel).mul((timeSlice / 6)));
    	this.location = this.location.add((kAllLoc).mul(timeSlice / 6));
    	System.out.println("loc: x " + location.x + " y " + location.y + " z " + location.z);
    	System.out.println("vel: x " + velocity.x + " y " + velocity.y + " z " + velocity.z);
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
