package GUI;
import Data.Scaling;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
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
	//private Group group;
	private SolarSystem solarS;
	private Scene scene;
	private SubScene subScene;
	
	
	public void start(Stage primaryStage) throws Exception{
		
		solarS = new SolarSystem();
		createCamera();
		createLight();
		Group group = createGroup();
		
		
		subScene = new SubScene(group, Scaling.WIDTH - 100, Scaling.HEIGHT, true, SceneAntialiasing.BALANCED);
	    subScene.setCamera(camera);
	    createSubScene();
	    
	    // 2D
	    BorderPane pane = new BorderPane();
	    Button playPause = new Button("\u25B6");
	    Button forward6Button = new Button("\u25B6\u25B6");
	    Button forward12Button = new Button("\u25B6\u25B6\u25B6");
	    ToolBar toolBar = new ToolBar(playPause,forward6Button,forward12Button);
	    toolBar.setOrientation(Orientation.VERTICAL);
	    pane.setCenter(subScene);
	    pane.setPrefSize(300,300);
	    pane.setRight(toolBar);
	    
	    scene = new Scene(pane, Scaling.WIDTH, Scaling.HEIGHT);
	    
		primaryStage.setTitle("Solarsystem in 3D");
		primaryStage.setScene(scene);
		
		primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double diff = event.getDeltaY() * 50;
			camera.translateZProperty().set(camera.getTranslateZ() + diff);
	});
		
		primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			   switch (event.getCode()) {
			   	case D:
			   		camera.getTransforms().add(new Rotate(10, Rotate.Y_AXIS));
			   		break;
			   
			   	case A:
			   		camera.getTransforms().add(new Rotate(-10, Rotate.Y_AXIS));
			   		break;
			   
			   	case S:
			   		camera.getTransforms().add(new Rotate(-10, Rotate.X_AXIS));
			   		break;
				   
			   	case W:
			   		camera.getTransforms().add(new Rotate(10, Rotate.X_AXIS));
			   		break;
			   }
	  	});
		
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
	
	public void createCamera() {
		camera = new PerspectiveCamera();
		camera.translateZProperty().set(-1000);
		//camera.setNearClip(0);
		//camera.setFarClip(50);
		
		/*camera.getTransforms().addAll(
		new Rotate(20, Rotate.X_AXIS),
		new Rotate(20, Rotate.Y_AXIS),
		new Rotate(0, Rotate.Z_AXIS)
	);*/
	}
	
	public Group createGroup() {
		Group group = new Group();
		PointLight sun = createLight();
		for (Sphere s: solarS.getSpheres()) {
			group.getChildren().add(s);
			System.out.println("new planet added");
			System.out.println(s.getTranslateX());
			System.out.println(s.getTranslateY());
			System.out.println(s.getTranslateZ());
			System.out.println(s.getRadius());
			
		}
		group.getChildren().add(sun);
		group.getChildren().add(camera);
		return group;
	}
	
	public PointLight createLight() {
		PointLight sun = new PointLight();
		sun.setColor(Color.YELLOW);
		sun.getTransforms().add(new Translate(Scaling.WIDTH/2,Scaling.HEIGHT/2,0));
		return sun;
	}
	
	public void createSubScene() {
		subScene.setFill(new Color(0.0, 0.33, 0.66, 1));
		
		subScene.setOnMousePressed(event -> {
		     locX = event.getSceneX();
		     locY = event.getSceneY();
		   });
		 
		   subScene.setOnMouseDragged(event -> {
			 camera.translateXProperty().set(camera.getTranslateX() + (locX - event.getSceneX())/75);
			 camera.translateYProperty().set(camera.getTranslateY() + (locY - event.getSceneY())/75);
		   });
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

