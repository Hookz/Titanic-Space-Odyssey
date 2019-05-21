package Land;
public class Wind {

    private double wind; //km/s
    private final double airDensity = 1.23995; //kg/m3
    private final double area = 17*4.5;
    private double relativeWindSpeed;
    public double force;
    private double accByWind;
    private static double TIME_SLICE=200; //in seconds

    public Wind(){

        //wind = 0;
    }
    public void calcWindSpeed(double kmFromSurface) {
        //Linear formula based on two points we know (0,0.3) and (120,120), where x = km from surface and y = windspeed
        double windSpeed = (119.7 / 120) * kmFromSurface + 0.3;
        double range = (1.2 - 0.8);
        double randWindSpeed = windSpeed * ((Math.random() * range) + 0.8);

        double direction = Math.random();
        //Make it unlikely for the wind direction to change, otherwise it changes way too often (depending on how often
        //the wind speed is calculated
        //System.out.println(getWind());
        System.out.println("The old windspeed was: " + wind);
        System.out.println("The random wind is: " + randWindSpeed);
        System.out.println("The direction is: " + direction);
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
        System.out.println("The new windspeed is: " + wind);
        System.out.println(" ");

    }

    public double accByWind(SpaceShip s, double kmtosurface){
        calcWindSpeed(kmtosurface);
        calculateForce(s);
        accByWind = s.force/s.getMass();
        System.out.println(s.getAccByWind());
        return accByWind;
    }

    public void calculateForce(SpaceShip s){
        calculateRelativeWindSpeed(s);
        s.force = (area/2)*airDensity*(relativeWindSpeed*relativeWindSpeed);
        if (s.getRelativeWindSpeed()<0)
            s.force = -s.force;

    }

    public void calculateRelativeWindSpeed(SpaceShip s){
        relativeWindSpeed = s.getWind()-s.getXVelocity();
    }

    public double calcDisplacement(SpaceShip s, double kmtosurface){
        //System.out.println(accByWind(s, ));
        double displacement = s.getXVelocity()*TIME_SLICE + 0.5*(accByWind(s, kmtosurface)) * TIME_SLICE*TIME_SLICE;
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
