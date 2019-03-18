package SolarSystem;

import java.awt.*;

public class Satellite extends Mision {
    public Color color;
    public Planet relatedPlanet;
    public double initialDistanceWithRelatedPlanet;
    public double initialSpeed;

    public Satellite(String name,Color c,double radius,double mass,double radiusDisplay,double averageSpeed,
                     Planet relatedPlanet,double rotation)
    {
        super(name,c,radius + Math.sqrt(2* Math.pow(relatedPlanet.posX, 2)),mass,
                radiusDisplay,averageSpeed + Math.sqrt(2* Math.pow(relatedPlanet.speedX, 2)),rotation);
        this.relatedPlanet = relatedPlanet;
        initialDistanceWithRelatedPlanet = radius;
        initialSpeed = averageSpeed;
    }
}
