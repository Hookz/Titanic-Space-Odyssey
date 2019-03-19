package sample;

public enum SpaceObject {

    //ephemerides coordinate taken from https://ssd.jpl.nasa.gov/horizons.cgi#results.
    //TimeSpan: 2019-03-18 on 00:00:00
    //(Name, Mass, Radius, X, Y, Z, VX, VY, VZ)
    //Units: km, km /sek
    SUN("Sun", 1.98855E+30, 695700000., 0, 0, 0, 0, 0, 0),

    MERCURY("Mercury", 0.33011E+24, 2439.7E+3, -5.892587635800256E+07, -4.674319444922816E+06, 4.906444768010203E+06, -5.356099326145262E+00, -4.638197158333085E+01, -3.299790129456486E+00),

    VENUS("Venus", 4.8675e24, 6051.8E+3, -1.476351077188876E+07, -1.065701113517407E+08, -6.438372850706205E+05, 3.445673023043100E+01, -4.825751980600542E+00, -2.055112226292678E+00),

    EARTH("Earth", 5.9723E+24, 6371.008E+3, -1.488388951139719E+08, 9.328713591537857E+06, -6.296315855979454E+03, -2.131735594074553E+00, -2.984407047225507E+01, 6.393357160394686E-04),

    MOON("Moon", 7.34767309E+22, 1.737E+6, -1.490893328388780E+08, 9.590111020782992E+06, 4.290510432549287E+03, -2.886604385220837E+00, -3.061754110279904E+01, 9.037989303802796E-02),

    MARS("Mars", 0.64171E+24, 3389.5E+3, 3.103733326694898E+07,2.309355414014468E+08 ,4.042431599711716E+06, -2.310739538513621E+01, 5.325092503684893E+00, 6.785415572203704E-01),

    JUPITER("Jupiter", 1898.19E+24, 69911E+3, -2.401367152604580E+08, -7.587357070445194E+08, 8.518379969878018E+06, 1.230001498250003E+01, -3.318726032931892E+00, -2.613332371146624E-01),

    SATURN("Saturn", 568.34E+24, 58232E+3, 3.514888790197405E+08,-1.461592185930664E+09, 1.142194480296618E+07, 8.860108133196917E+00, 2.229036831178442E+00, -3.918572780714782E-01),

    TITAN("Titan", 1.3452E+23, 5.15E+6, 3.507048448078138E+08, -1.460697245250129E+09, 1.103865383051020E+07, 4.588411437552161E+00, -5.813347988139380E-01, 1.481459966075253E+00),

    NEPTUNE("Neptune", 1.024E+26, 2.4622E+6, 4.344136042875575E+09, -1.084367906006034E+09, -7.778447579036146E+07, 1.280737523784774E+00,  5.305456072791291E+00, -1.394603100248213E-01),

    URANUS("Uranus", 8.681E+25, 2.5362E+6, 2.521773729382990E+09, 1.569507895069891E+09, -2.684071180565929E+07, -3.648355775991929E+00, 5.464235495333264E+00, 6.755880258598879E-02),

    PLUTO("Pluto", 1.309E+22, 1.1883E+6, 1.811625216524193E+09, -4.713290669446465E+09, -1.968051374977469E+07, 5.177185711936242E+00, 7.828947828931261E-01, -1.587053980082941E+00);


    public final String celestialName;
    public final double mass;   // kg
    public final double radius; // meters
    public Vector3D location; // meters
    public Vector3D velocity; // m/s


    SpaceObject(String celestialName, double mass, double radius, double x, double y, double z, double x_vel, double y_vel, double z_vel) {
        this.celestialName = celestialName;
        this.mass = mass;
        this.radius = radius;
        this.location = new Vector3D(x*1000, y*1000, z*1000);
        this.velocity = new Vector3D(x_vel*1000, y_vel*1000, z_vel*1000);
    }

    public Body getAsBody() {
        Body body = new Body();
        body.location = new Vector3D(this.location);
        body.mass = this.mass;
        body.name = this.celestialName;
        body.velocity = new Vector3D(this.velocity);
        body.radius = this.radius;
        return body;
    }
}
