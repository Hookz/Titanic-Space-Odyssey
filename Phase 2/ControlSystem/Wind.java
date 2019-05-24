package ControlSystem;
public class Wind {

    private double wind; //km/s
    public final double airDensity = 1.23995; //kg/m3
    public final double area = 17*4.5;


    public Wind(){

        //wind = 0;
    }
    public void calcWindSpeed(double kmFromSurface) {

        //Linear formula based on two points we know (0,0.3) and (120,120), where x = km from surface and y = windspeed
        double windSpeed = (119.7 / 120) * kmFromSurface + 0.3;

        //Since the maximum windspeed according to California institute for technology was 120 m/s, we cap it there
        if (windSpeed>100) {
            windSpeed = 100;
        }

        double range = (1.2 - 0.8);
        double randWindSpeed = windSpeed * ((Math.random() * range) + 0.8);
        double direction = Math.random();
        //Make it unlikely for the wind direction to change, otherwise it changes way too often (depending on how often
        //the wind speed is calculated
        //System.out.println(getWind());
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

    }


    public double getWind() {
        return this.wind;
    }

    public double getAirDensity() {
        return airDensity;
    }

    public double getArea() {
        return area;
    }

    /*
    public static void main (String [] args){
        SpaceShip s = new SpaceShip(5000,0,0);
        System.out.println(s.calcDisplacement(400));
        System.out.print(s.getWind());
    }
    */
}