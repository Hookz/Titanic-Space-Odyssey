package SolarSystem;

import java.awt.Color;

public class Asteroid extends Mision
{
    public Asteroid(String name,Color c,double radius,double mass,double radiusDisplay,double averageSpeed,double rotation) {
        super(name,c,radius,mass,radiusDisplay,averageSpeed,rotation);
    }
    public Asteroid(String name,Color c,double posX,double posY,double mass, double radiusDisplay,double speedX,double speedY,double rotation) {
        super(name,c,posX,posY,mass,radiusDisplay,speedX,speedY,rotation);
    }
}