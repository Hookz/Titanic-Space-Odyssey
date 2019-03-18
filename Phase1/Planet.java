package SolarSystem;

import java.awt.*;
import java.util.ArrayList;

public class Planet extends Mision {
    public Color color;
    public ArrayList<Satellite> satelliteOfThisPlanet = new ArrayList<Satellite>(); //point of optimization here.  We can use simple array.

    public Planet(String planetName, Color color,double radius, double mass, double raduisDisplay, double averageSpeed, double rotation) { //speed to velocity.
        super(planetName, color, radius, mass, raduisDisplay, averageSpeed, rotation);
    }

    public Planet(String name,Color c,double posX,double posY,double mass, double radiusDisplay,double speedX,double speedY,double rotation) {
        super(name,c,posX,posY,mass,radiusDisplay,speedX,speedY,rotation);
    }
    public void addSatellite(Satellite satellite) {
        satelliteOfThisPlanet.add(satellite);
    }
}
