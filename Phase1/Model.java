package SolarSystem;

import java.awt.Color;
import java.util.ArrayList;

public class Model {

    //Math constant
    public double G = 6.67*Math.pow(10,-11); // not doing shit: F = (G * m1 * m2) / r^2.
    public double au = 1.49597*(double) Math.pow(10, 11);
    public SolarSystem ourSolarSystem = new SolarSystem();
    public SolarSystem currentSolarSystem;

    //Animation Shit
    public boolean timeOnPause = false;
    //Physics per seconds
    public int pps = 500;
    //FPS
    public int fps = 24;
    public int deltaT = 10;

    //UI Shit
    public int programWidth = 1440;
    public int programHeight = 900;
    public boolean windowedMode = false;
    public int pixelParAU = 8000;
    public int satelliteScaleFactor = 100;
    public double satelliteMaxDeltaT = 172;
    public double rotationMaxDeltaT = 300;
    public boolean corpLineDisplay = true;
    public boolean rotationDisplay = true;
    public double cameraZoom = 1;
    public int cameraXTranslation = 0;
    public int cameraYTranslation = 0;
    public int cameraTranslationTurn = 0;
    public boolean cameraIsOnZoom = false;
    public double cameraZoomConstantPerKey = 0.03;
    public double hyp = 0.1;
    public double maxZoomValue = 20;
    public double minZoomValue = 0.007;
    public boolean drawingOrbitstatus = true;
    public boolean showTrajectory = true;
    public boolean satelliteReset = false;
    public boolean rotationReset = false;
    public boolean freeViewMode = false;
    public boolean showSSPanel = true;


    //mission
    //empty mission
    public Mision emptyMision = new Mision("",Color.RED,0,0,0,0,0,0,0);

    public Mision selectedMision = emptyMision;
    public Spacecraft emptySpaceShip = new Spacecraft("",Color.WHITE,0,0,0,0,0,0,0,0,0);
    public Spacecraft selectedSpaceShip = emptySpaceShip;
    public ArrayList<Satellite> memorySatellite = new ArrayList<Satellite>();


//    //for zooming in and out
//    public final int KEY_ZOOM_IN_ID1 = 100;
//    public final int KEY_ZOOM_IN_ID2 = 50;
//    public final int KEY_ZOOM_OUT_ID1 = 150;
//    public final int KEY_ZOOM_OUT_ID2 = 200;

    //mission type
    public ArrayList<Integer> authorizedOrbitLineCorpID = new ArrayList<Integer>();
    public ArrayList<Integer> ppsRefreshPerCorpType = new ArrayList<Integer>();

    //collision.... for now we assume ship crashes and losses contact :(
    public double absorb = 1;

    //ERROR NOTICE
    public ArrayList<String> errorMessage = new ArrayList<String>();


    public Model()
    {
        //start state of solar system.
        SolarSystemDatabase ssdb = new SolarSystemDatabase();
        currentSolarSystem = ssdb.ssList.get(0);
        selectedMision = currentSolarSystem.defaultSystemSelectedMision;

        declareMisionRules();
        //error
        declareErrorsMessage();
    }
    void declareErrorsMessage()
    {
        ErrorMessage em = new ErrorMessage();
        errorMessage = em.errorMessage;
    }
    void declareMisionRules()
    {
      /*public int starID = 0;
        public int planetID = 1;
        public int astroidID = 2;
        public int spaceShipID = 3; 
        public int satellite = 4;*/
        //authorizedOrbitLineCorpID.add(0);
        authorizedOrbitLineCorpID.add(1);
        authorizedOrbitLineCorpID.add(3);
        authorizedOrbitLineCorpID.add(4);

        //mission physics per second
        ppsRefreshPerCorpType.add(10);
        ppsRefreshPerCorpType.add(1);
        ppsRefreshPerCorpType.add(20);
        ppsRefreshPerCorpType.add(1);
        ppsRefreshPerCorpType.add(1);
    }

}