package Land;
public class Wind {

    private double wind; //km/s
    private final double airDensity = 1.23995; //kg/m3
    private final double area = 17*4.5;
    private double relativeWindSpeed;
    public double force;
    private double accByWind;
    private static double TIME_SLICE=60; //in seconds

    public Wind(){
    }

    public void calcWindSpeed(double kmFromSurface) {
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
            //switch of sign
            wind = randWindSpeed;
        }
        else if (wind < 0 && direction<0.8){
            wind = -randWindSpeed;
        }

        else if (wind > 0 && direction>=0.8) {
            //switch of sign
            wind = -randWindSpeed;
        }
        else if (wind > 0 && direction<0.8){
            wind = randWindSpeed;
        }
    //For now, we put a limit on the maximum wind speed, since
    }

    public double accByWind(SpaceShip s, double kmtosurface){
        calcWindSpeed(kmtosurface);
        calculateForce(s);
        accByWind = s.force/s.getMass();
        return accByWind;
    }

    public void calculateForce(SpaceShip s){
        calculateRelativeWindSpeed(s);
        s.force = (area/2)*airDensity*(relativeWindSpeed*relativeWindSpeed);
        if (s.getRelativeWindSpeed()<0)
            s.force = -s.force;

    }

    public void calculateRelativeWindSpeed(SpaceShip s){
        relativeWindSpeed = s.getWind()-s.getVelocity().x;
    }

    //Call this method for the x displacement in METERS
    public double calcDisplacement(SpaceShip s, double kmtosurface){
        double displacement = s.getVelocity().y*TIME_SLICE + 0.5*(accByWind(s, kmtosurface)) * TIME_SLICE*TIME_SLICE;
        return displacement;
    }

    public double getWind() {
        return this.wind;
    }
    public double getAccByWind() {
        return this.accByWind;
    }

    public double getAirDensity() {
        return airDensity;
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

    /*
    public static void main (String [] args){
        SpaceShip s = new SpaceShip(5000,0,0);
        System.out.println(s.calcDisplacement(s, 400));
        System.out.print(s.getWind());
    }
    */
}
