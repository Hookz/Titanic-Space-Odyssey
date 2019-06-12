package GUI;
import Data.Scaling;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	public final double TIME_SLICE = 200;
	private double locX;
	private double locY;
	private Camera camera;
	
	
	public void start(Stage primaryStage) throws Exception{
		Sphere sphere = new Sphere(50);
		sphere.translateXProperty().set(Scaling.WIDTH/2);
		sphere.translateYProperty().set(Scaling.HEIGHT/2);
		
		SolarSystem solarS = new SolarSystem();
		
		camera = new PerspectiveCamera();
		//camera.setNearClip(0);
		//camera.setFarClip(50);
		
		Group group = new Group();
		for (Sphere s: solarS.getSpheres()) {
			group.getChildren().add(s);
			System.out.println("new planet added");
			System.out.println(s.getTranslateX());
			System.out.println(s.getTranslateY());
			System.out.println(s.getTranslateZ());
			System.out.println(s.getRadius());
		}
		
		PointLight sun = new PointLight();
		sun.setColor(Color.YELLOW);
		sun.getTransforms().add(new Translate(Scaling.WIDTH/2,Scaling.HEIGHT/2,0));
		
		
		Scene scene = new Scene(group, Scaling.WIDTH, Scaling.HEIGHT);
		scene.setFill(new Color(0.0, 0.33, 0.66, 1));
		camera.translateZProperty().set(-1000);
		scene.setCamera(camera);
		
		group.getChildren().add(sun);
		
		
		/*camera.getTransforms().addAll(
			new Rotate(20, Rotate.X_AXIS),
			new Rotate(20, Rotate.Y_AXIS),
			new Rotate(0, Rotate.Z_AXIS)
		);*/
		
		group.getTransforms().add(new Rotate(70, new Point3D(0,0,0)));
		
		
		   //for zooming
		primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double diff = event.getDeltaY() * 25;
			camera.translateZProperty().set(camera.getTranslateZ() + diff);
	});
		
		scene.setOnMousePressed(event -> {
		     locX = event.getSceneX();
		     locY = event.getSceneY();
		   });
		 
		   scene.setOnMouseDragged(event -> {
			 camera.translateXProperty().set(camera.getTranslateX() + (locX - event.getSceneX())/75);
			 camera.translateYProperty().set(camera.getTranslateY() + (locY - event.getSceneY())/75);
		   });
		
		
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			   switch (event.getCode()) {
			   	case Y:
				   camera.getTransforms().add(new Rotate(10, Rotate.Y_AXIS));
			   		break;
			   
			   	case H:
			   		camera.getTransforms().add(new Rotate(-10, Rotate.Y_AXIS));
			   		break;
			   
			   	case X:
			   		camera.getTransforms().add(new Rotate(-10, Rotate.X_AXIS));
			   		break;
				   
			   	case S:
			   		camera.getTransforms().add(new Rotate(10, Rotate.X_AXIS));
			   		break;
			   }
	  	});
		
		primaryStage.setTitle("Solarsystem in 3D");
		primaryStage.setScene(scene);
		
		Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(0.1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        solarS.update(TIME_SLICE);
                    }
                });
        timeline.getKeyFrames().add(kf);
        timeline.play();
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
