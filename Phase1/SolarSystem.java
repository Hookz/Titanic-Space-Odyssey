package SolarSystem;

import java.util.ArrayList;

public class SolarSystem {
    public ArrayList<Mision> misionList = new ArrayList<Mision>();

    public ArrayList<Mision> misionListOptimized = new ArrayList<Mision>();

    public ArrayList<Star> star = new ArrayList<Star>();
    public ArrayList<Planet> planet = new ArrayList<Planet>();
    public ArrayList<Asteroid> astroids = new ArrayList<Asteroid>();

    public ArrayList<Satellite> satelliteGlobal = new ArrayList<Satellite>();
    public Mision defaultSystemSelectedMision;

    public SolarSystem()
    {}
    public void addStar(Star star) {
        star.type = 0;
        this.star.add(star);
        misionList.add(star);
        misionListOptimized.add(star);
    }

    public void addPlanet(Planet planet) {
        planet.type = 1;
        this.planet.add(planet);
        misionList.add(planet);
        misionListOptimized.add(planet);
    }

    public void addAstroid(Asteroid astroid) {
        astroid.type = 2;
        this.astroids.add(astroid);
        misionList.add(astroid);
    }

    public void addSatellite(Satellite satellite,Planet planet) {
        satellite.type = 4;
        this.satelliteGlobal.add(satellite);
        misionList.add(satellite);
        planet.addSatellite(satellite);
        misionListOptimized.add(satellite);
    }
}