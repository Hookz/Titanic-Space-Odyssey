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
import javafx.scene.control.Label;
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
	private boolean change = false;
	
	
	public void start(Stage primaryStage) throws Exception{
		
		solarS = new SolarSystem();
		createCamera();
		createLight();
		Group group = createGroup();
		
		
		subScene = new SubScene(group, Scaling.WIDTH - 300, Scaling.HEIGHT, true, SceneAntialiasing.BALANCED);
	    subScene.setCamera(camera);
	    createSubScene();
	    
	    // 2D
	    BorderPane pane = new BorderPane();
	    
	    
	    Button playPause = new Button("\u25B6");
	    //Button forward6Button = new Button("\u25B6\u25B6");
	    //Button forward12Button = new Button("\u25B6\u25B6\u25B6");
	    //ToolBar toolBar = new ToolBar(playPause,forward6Button,forward12Button);
	    
	    playPause.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (change) {
                    change = false;
                }
                else {
                    change = true;
                }
            }
        });
	    
	    
	    Label instructionLabelOne = new Label("Y - rotate y: 10 degrees");
	    Label instructionLabelTwo = new Label("H - rotate y: -10 degrees");
	    Label instructionLabelThree = new Label("X - rotate x: 10 degrees");
	    Label instructionLabelFour = new Label("S - rotate x: -10 degrees");
	    Label instructionLabelFive = new Label("Z - rotate z: 10 degrees");
	    Label instructionLabelSix = new Label("A - rotate z: - 10 degrees");
	    Label instructionLabelSeven = new Label("Scroll - move in z direction");
	    Label instructionLabelEight = new Label("Drag - move in x and y direction");
	    Label timeLabel = new Label("Time");
	   
	    ToolBar toolbar = new ToolBar(playPause,instructionLabelOne, instructionLabelTwo, instructionLabelThree, instructionLabelFour, instructionLabelFive, instructionLabelSix, instructionLabelSeven, instructionLabelEight, timeLabel);
	    toolbar.setOrientation(Orientation.VERTICAL);
	    toolbar.setPrefWidth(300);
	    pane.setCenter(subScene);
	    pane.setPrefSize(300,300);
	    pane.setRight(toolbar);
	    
	    scene = new Scene(pane, Scaling.WIDTH, Scaling.HEIGHT);
	    
		primaryStage.setTitle("Solarsystem in 3D");
		primaryStage.setScene(scene);
		
		primaryStage.addEventHandler(ScrollEvent.SCROLL, event -> {
			double diff = event.getDeltaY() * 50;
			camera.translateZProperty().set(camera.getTranslateZ() + diff);
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
			   		
			   	case Z:
			   		camera.getTransforms().add(new Rotate(10, Rotate.Z_AXIS));
			   		break;
			   		
			   	case A:
			   		camera.getTransforms().add(new Rotate(-0, Rotate.Z_AXIS));
			   }
	  	});
		
		Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(0.1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        if (change) {
                        	solarS.update(TIME_SLICE);
                        	timeLabel.setText(solarS.getElapsedTimeAsString());
                        }
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

