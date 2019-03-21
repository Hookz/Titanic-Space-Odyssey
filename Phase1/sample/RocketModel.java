package sample;

import static sample.SpaceObject.SUN;
//Earth to Saturn
//Saturn to Titan
//just use: https://trajbrowser.arc.nasa.gov/traj_browser.php?NEOs=on&NEAs=on&NECs=on&chk_maxMag=on&maxMag=25&chk_maxOCC=on&maxOCC=4&chk_target_list=on&target_list=Titan&mission_class=oneway&mission_type=rendezvous&LD1=2014&LD2=2016&maxDT=2.0&DTunit=yrs&maxDV=7.0&min=DV&wdw_width=0&submit=Search
//theorems used: Vis-viva equation(extention of Kepler's 2nd law), Kepler's third law,
public class RocketModel {

    public double deltaV;
    public double earthToSun;
    public double titanToSun;
    public double earthVelocity;
    public double titanVelocity;
    public final double earthOrbitalPeriod = 31.6E+6;
    public final double titanOrbitalPeriod = 93E+7; //using saturn orbit around sun for now.
    public Vector3D Earth, Titan, Sun;

    //data for Rocket

    public RocketModel(SpaceObject Earth, SpaceObject Titan, SpaceObject Sun) {
        //get the distances.
        this.earthToSun = Earth.location.distance(Sun.location);
        this.titanToSun = Titan.location.distance(Sun.location);
        this.earthVelocity = earthVelocity();
        this.titanVelocity = titanVelocity();
    }

    public double aTranfer(){
        return (earthToSun + titanToSun) / 2;
    }

    public double peroidOfTransfer(){
        return Math.sqrt((4 * Math.pow(Math.PI,2) * aTranfer())/Physics.G*SUN.mass);
    }

    public double earthVelocity(){
        return 2*Math.PI*earthToSun/earthOrbitalPeriod;
    }

    public double titanVelocity(){
        return 2*Math.PI*titanToSun/titanOrbitalPeriod;
    }

    public double velocityPerihelion() {
        return ((2*Math.PI * aTranfer())/peroidOfTransfer())*Math.sqrt((2*aTranfer()/earthToSun) -1);
    }

    public double velocityAphelion() {
        return 2*Math.PI*aTranfer()/peroidOfTransfer()*Math.sqrt((2*aTranfer()/titanToSun)-1);
    }
    public double deltaV1(){
        return velocityPerihelion() - earthVelocity;
    }
    public double deltaV2(){
        return titanVelocity - velocityAphelion();
    }

    public double timeOfFlight(){
        return ((1.0 / 2 * peroidOfTransfer())/86400)/365; //au to seconds.  Yikes.
    }

    //kepler's third law p^2=a^3
    public double driftPeriod() {
        return Math.sqrt(Math.pow(aTranfer(), 3))/3.154E+7; //au to seconds.  Yikes.
    }

    //angle need to attain launch
    public double degreeNeeded() {
        return -1 * (driftPeriod()/titanOrbitalPeriod * 360 - 180);
    }

//    public final double MARS_ORBITAL_PERIOD = 59.4E+6;
//    public final double JUPITER_ORBITAL_PERIOD = 370E+6;
//    public Vector3D Mars, Jupiter;
}
