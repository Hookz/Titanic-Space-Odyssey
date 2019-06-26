package GUI;

import Data.Scaling;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Sphere;

/*
 * This class turns the bodySystem into another one made of spheres, which will provide an arraylist of spheres.
 * This arraylist can be used to display the gui in 3D.
 */

public class BodySystem3D {
	private List<Sphere> solarSystem = new ArrayList<Sphere>();
	
	public BodySystem3D(BodySystem bs) {
		for(Body b: bs.getBodies()) {
			if (b.name.equalsIgnoreCase("SUN")) {
				Sphere current = new Sphere(Scaling.SUN_SCALE * b.radius);
				current.translateXProperty().set((b.location.x * Scaling.SCALE_GUI) + Scaling.WIDTH / 2);
				current.translateYProperty().set((b.location.y * Scaling.SCALE_GUI) + Scaling.HEIGHT / 2);
				current.translateZProperty().set(b.location.z * Scaling.SCALE_GUI);
				solarSystem.add(current);
				System.out.println(b.name + " added");
			}
			else if (b.radius > 0.9E+7 ) {
				Sphere current = new Sphere(Scaling.PLANET_SCALE_LARGE * b.radius);
				current.translateXProperty().set((b.location.x * Scaling.SCALE_GUI) + Scaling.WIDTH / 2);
				current.translateYProperty().set((b.location.y * Scaling.SCALE_GUI) + Scaling.HEIGHT / 2);
				current.translateZProperty().set(b.location.z * Scaling.SCALE_GUI);
				solarSystem.add(current);
				System.out.println(b.name + " added large");
			}
			
			else {
				Sphere current = new Sphere(Scaling.PLANET_SCALE * b.radius);
				current.translateXProperty().set((b.location.x * Scaling.SCALE_GUI) + Scaling.WIDTH / 2);
				current.translateYProperty().set((b.location.y * Scaling.SCALE_GUI) + Scaling.HEIGHT / 2);
				current.translateZProperty().set(b.location.z * Scaling.SCALE_GUI);
				solarSystem.add(current);
				System.out.println(b.name + " added small");
			}
		}
	}
	
	public List<Sphere> getSphereSystem(){
		return solarSystem;
	}
}

