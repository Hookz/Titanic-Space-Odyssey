package SolarSystem;

import java.awt.*;
import java.awt.geom.Path2D;

public class Mision {
    public int type;
    public String name;

    public double posX;
    public double posY;

    public double speedX;
    public double speedY;

    public double direction;

    public double mass;
    public double angle;
    public double massPerInitialPixelCube;

    public double initialRadiusDisplay, currentRadiusDisplay;

    public double rotationPeriodHour = 0;
    public double currentRotationRadians = 0;

    public double widthToHeightProportion = 1;

    public String description = null;

    public Color color;

    public Path2D path;

    public Mision(String planetName, Color c, double posX, double posY, double mass, double radiusDisplay, double speedX, double speedY, double rotation) {
        this.name = planetName;
        this.color = c;
        this.mass = mass;
        this.initialRadiusDisplay = radiusDisplay;
        this.currentRadiusDisplay = initialRadiusDisplay;
        this.speedX = speedX;
        this.speedY = speedY;
        this.rotationPeriodHour = rotation;
        this.massPerInitialPixelCube = mass/((4/3)*Math.PI*Math.pow(radiusDisplay, 3));

    }

    public Mision(String name, Color c, double radians, double mass, double radiusDisplay, double averageSpeed, double rotation) {
        this.name = name;
        this.posX = radians * Math.sin(Math.toRadians(45)) * -1;
        this.posY = posX;
        this.mass = mass;
        this.initialRadiusDisplay = radiusDisplay;
        this.currentRadiusDisplay = initialRadiusDisplay;
        this.speedX = averageSpeed * Math.sin(Math.toRadians(45));
        this.speedY = speedX * -1;
        this.color = c;
        this.rotationPeriodHour = rotation;
        this.massPerInitialPixelCube = mass/((4/3)*Math.PI*Math.pow(radiusDisplay, 3));

        path = new Path2D.Double();
        path.moveTo(posX, posY);

    }

    public Double getSpeed() {
        return Math.sqrt(Math.pow(this.speedX,2) + Math.pow(this.speedY,2));
    }

    public void setSpeed(double newSpeed) {
        this.speedX = newSpeed * Math.sin(angle) * -1;
        this.speedY = newSpeed * Math.cos(angle);
    }

    public void setMass(Double valueD,double currentZoom) {
        this.mass = valueD;
        this.initialRadiusDisplay = Math.cbrt((3 * mass) / (4 * Math.PI * massPerInitialPixelCube));
    }


    public double getDirection(){
        if((speedX > 0)&&(speedY > 0)) {
            //FRAMING 1 X:+Y:+
            direction = Math.atan(Math.abs(speedY/speedX));
        }
        else if((speedX < 0)&&(speedY > 0)) {
            //FRAMING 2 X:-Y:+
            direction = Math.PI/2 + Math.atan(Math.abs(speedX/speedY));
        }
        else if((speedX < 0)&&(speedY < 0)) {
            //FRAMING 3 X:-Y:-
            direction = Math.PI + Math.atan(Math.abs(speedY/speedX));
        }
        else if((speedX > 0)&&(speedY < 0)) {
            //FRAMING 4 X:+Y:-
            direction = (3 * Math.PI/ 2) + Math.atan(Math.abs(speedX/speedY));
        }

        return direction;
    }

    public void setSpeed(double s, double a){
        setSpeedX(s,a);
        setSpeedY(s,a);
    }

    public void setSpeedX(double speed, double angle){
        speedX = speed*Math.cos(angle);
    }

    public void setSpeedY(double speed, double angle){
        speedY = speed*Math.sin(angle);
    }

}
