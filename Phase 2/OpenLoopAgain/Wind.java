package OpenLoopAgain;

import java.util.ArrayList;

public class Wind {

    private double wind; //km/s
    public final double airDensityTitan = 1.23995; //kg/m3
    public final double airDensityEarth = 1.2041; //kg/m3 (for 20 degrees celsius)
    public final double area = 17*4.5;
    private double relativeWindSpeed;
    public double force;
    private double accByWind;
    private static double TIME_SLICE=1; //in seconds
    private boolean titan; //whether it's landing on earth or titan- true = titan
  
    public Wind(boolean titan){
        wind = 0;
        this.titan = titan;
        
    }

    public double calcWindSpeed(double kmFromSurface) {
        //Linear formula based on two points we know (0,0.3) and (120,120), where x = km from surface and y = windspeed
        //Calc windspeed based on function
        double windSpeed = (119.7 / 120) * kmFromSurface + 0.3;

        //Since the maximum windspeed according to California institute for technology was 120 m/s, we cap it there
        if (windSpeed>100) {
            windSpeed = 100;
        }

        //Add a 20% difference, so we have a stochastic model
        double range = (1.2 - 0.8);
        double randWindSpeed = windSpeed * ((Math.random() * range) + 0.8);

        double direction = Math.random();
        //Make it unlikely for the wind direction to change, otherwise it changes way too often (depending on how often
        //the wind speed is calculated

        if (wind == 0 && direction >=0.5) {
            randWindSpeed = -randWindSpeed;
            wind = randWindSpeed;
        }

        else if (wind == 0 && direction <0.5) {
            wind = randWindSpeed;
        }

        else if (wind < 0 && direction>=0.8) {
            wind = randWindSpeed;
        }
        else if (wind < 0 && direction<0.8){
            //switch of sign
        	wind = -randWindSpeed;
        }

        else if (wind > 0 && direction>=0.8) {
            //switch of sign
            wind = -randWindSpeed;
        }
        else if (wind > 0 && direction<0.8){
            wind = randWindSpeed;
        }
        return wind;
    }

    public double accByWind(SpaceShip s, double kmtosurface){
        calcWindSpeed(kmtosurface);
        calculateForce(s);
        accByWind = s.force/s.getMass();
        return accByWind;
    }

    public void calculateForce(SpaceShip s){
        calculateRelativeWindSpeed(s);
        if (titan) {
        	s.force = (area/2)*airDensityTitan*(relativeWindSpeed*relativeWindSpeed);
    	}
        else {
        	s.force = (area/2) * airDensityEarth*(relativeWindSpeed*relativeWindSpeed);
        }
        
        if (s.getRelativeWindSpeed()<0) {
            s.force = -s.force;
        }
    }
    
    public ArrayList<Double> calculateForcesForWholeTraject(SpaceShip spaceship, double kilometresStart) {
    	//an arraylist of the wind force every km
    	ArrayList<Double> forces = new ArrayList(); 
    	
    	//loop through every kilometre
    	while(kilometresStart> -1) { 
    		calcWindSpeed(kilometresStart);
    		calculateForce(spaceship);
    		forces.add(this.getForce());
    		kilometresStart = kilometresStart - 1;
    	}
    	
    	return forces;
    }
    
    
    public void calculateRelativeWindSpeed(SpaceShip s){
        relativeWindSpeed = s.getWind()-s.getVelocity().getX();
    }

    //Call this method for the x displacement in METERS
    public double calcDisplacement(SpaceShip s, double kmtosurface){
        double displacement = s.getVelocity().getX()*TIME_SLICE + 0.5*(accByWind(s, kmtosurface)) * TIME_SLICE*TIME_SLICE;
        return displacement;
    }
    
    //getters:

    public double getWind() {
        return this.wind;
    }
    public double getAccByWind() {
        return this.accByWind;
    }

    public double getAirDensityTitan() {
        return airDensityTitan;
    }
    
    public double getAirDensityEarth() {
    	return airDensityEarth;
    }

    public double getArea() {
        return area;
    }

    public double getRelativeWindSpeed() {
        return relativeWindSpeed;
    }

    public double getForce() {
        return force;
    }
    

    //Call this method to get the tilt in radians (positive wind is negative tilt)
    //this method adjusts tilt in SpaceShip class
    public double calcTilt(SpaceShip s, double kmToSurface){
        double randTilt = (Math.random()*10);
        //Make zones, such that the further away from titan, the higher the tilt can be
        if (kmToSurface<=24)
            randTilt = randTilt;
        else if (kmToSurface <= 48)
            randTilt = 3* randTilt;
        else if (kmToSurface <= 72)
            randTilt = 4.5* randTilt;
        else if (kmToSurface <= 96)
            randTilt = 6* randTilt;
        else
            randTilt = 9* randTilt;

        double tiltInRadians = Math.toRadians(randTilt);

        if (s.getWind()<0)
            s.addTilt(tiltInRadians);
        else
            s.addTilt(-tiltInRadians);

        return tiltInRadians;
    }
}
