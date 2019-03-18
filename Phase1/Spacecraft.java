package SolarSystem;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Spacecraft extends Mision {

    public double fx;
    public double fy;

    public double thrustersPowerN;
//    public double thrustersEfficiency;
    public double currentRotationAngle;
    public double thrusterAccX;
    public double thrusterAccY;
    public double oRatio;

//    public double resForceX;
//    public double resForceY;

    public Path2D pt;

    public boolean thrust;
    public boolean tracePrevision;

    public Point2D.Double[] pointTab;
    public Point.Double eCPoint;
    public Point.Double variation;

    public double gravityEffect;
    public double weight;

    public Spacecraft(String name,Color c,double radius,double mass,double radiusDisplay,double averageSpeed,
                     double fuelKg,double oRatio,double tpn, double bc,double rotation){

        super(name,c,radius,mass,radiusDisplay,averageSpeed,rotation);

        //public double fuelKg = 20000;
        super.mass = mass+fuelKg+(bc/100);
        this.thrustersPowerN = tpn/10;
        this.oRatio = oRatio;

        pt = new Path2D.Double();
        pt.moveTo(posX, -posY);

        pointTab = new Point2D.Double[3];
        variation = new Point.Double();

//        thrustersEfficiency = 0;
        currentRotationAngle = 0;
        thrust = false;
        tracePrevision = true;
    }

//    public void increaseThrustersEfficiency(){
//        thrustersEfficiency = thrustersEfficiency + 1;
//        if(thrustersEfficiency > 100)
//            thrustersEfficiency = 100;
//    }

//    public void decreaseThrustersEfficiency(){
//        thrustersEfficiency = thrustersEfficiency - 1;
//        if(thrustersEfficiency < 0)
//            thrustersEfficiency = 0;
//    }

    public void startEngines(){
        thrust = true;

    }

    public void cutEngines(){
        thrust = false;
        fx = 0;
        fy = 0;
    }

    public void ACWRotation(){
        currentRotationAngle+=oRatio;
        if( currentRotationAngle > 360)
            currentRotationAngle = 0;

//            mass = mass-0.5;
    }

    public void CWRotation(){

        currentRotationAngle-=oRatio;
        if(currentRotationAngle < 0)
            currentRotationAngle=360;

//        mass = mass-0.5;
    }


    public Spacecraft getCopy(){
        Spacecraft s = new Spacecraft("",null,0,0,0,0,0,0,0,0,0);
        double x = this.posX;
        s.posX = x;
        double y = this.posY;
        s.posY = y;
        double m = this.mass;
        s.mass = m;
        double sx = this.speedX;
        s.speedX = sx;
        double sy = this.speedY;
        s.speedY = sy;
//        double rx = this.resForceX;
//        s.resForceX = rx;
//        double ry = this.resForceY;
//        s.resForceY = ry;

        Point2D.Double[] pt = new Point2D.Double[3];
        s.pointTab = pt;

        eCPoint = new Point.Double();
        s.eCPoint = eCPoint;

        return s;
    }

    public void calculateInstantDirection(Model m){
        double t = 0;

        if(this.speedX > 0 && this.speedY > 0){
            t = (double) Math.atan((this.speedY/this.speedX));
        }

        else if(this.speedX < 0 && this.speedY > 0){
            t = Math.PI - (double) Math.atan((this.speedY/(this.speedX*-1)));
        }

        else if(this.speedX < 0 && this.speedY < 0){
            t = Math.PI + (double) Math.atan((-1*(this.speedY))/(-1*(this.speedX)));
        }

        else if(this.speedX > 0 && this.speedY < 0){
            t = (2 * Math.PI) - (double) Math.atan(((this.speedY*-1)/this.speedX));
        }

        //((thrustersEfficiency/5)+0.005)
        variation.x = (m.hyp*((double)Math.cos(t)));
        variation.y = (m.hyp*((double)Math.sin(t)));

    }

    public double setProportion(){
        double prop;
        double l;
        if(this.speedX < this.speedY){
            prop = Math.abs(speedY);
        }
        else{
            prop = Math.abs(speedX);
        }

        if(thrust){
            l = ((thrustersEfficiency/10)+1);
        }
        else{
            l = 1;
        }

        prop = prop/Math.abs(Math.round(prop))*l;

        return prop;
    }
}