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
/*
    public static void main(String[] args) {
        SpaceShip s = new SpaceShip(5000, 0, 0);
        s.calcWindSpeed(10);
        s.calculateRelativeWindSpeed(s);
        s.calculateForce(s);

        System.out.println(s.getArea());
        System.out.println(s.getAirDensity());
        System.out.println(s.getRelativeWindSpeed());
        System.out.println(s.getWind());
        System.out.println(s.getXVelocity());
        System.out.println(s.getForce());
    }
*/
    public double calcDisplacement(SpaceShip s, double kmtosurface){
        //System.out.println(accByWind(s, ));
        double displacement = accByWind(s, kmtosurface) * TIME_SLICE;
        return displacement;
    }

/*
    public void normalWind(SpaceShip s){


        //Determine direction
        double d = Math.random();
        if (d>=0.5){
            tilt = -tilt;
        }
        s.addTilt(tilt);
    }
    */

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
}
