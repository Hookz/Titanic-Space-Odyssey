/*
package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainSubScene extends Application {

	//second to update the model(deltaT)
    public static final double TIME_SLICE = 60 * 30;

    //initial zoom
    public static final double INITIAL_SCALE = 6e9;

    //public static final double BODY_RADIUS_GUI = 2;

    private static final int BOTTOM_AREA_HEIGHT = 100;

    private BodySystem bodySystem;

    private CoordinatesTransformer transformer = new CoordinatesTransformer();

    //fps
    private FPSCounter fps = new FPSCounter();

    private Vector3D dragPosStart;
    private Label timeLabel;
    private Label fpsLabel;
    private Label scaleLabel;
	
    private Group root3D;
    private Camera camera;
    
	public void start(Stage stage) throws Exception {
		createBodies();
        transformer.setScale(INITIAL_SCALE);
        transformer.setOriginXForOther(500);
        transformer.setOriginYForOther(500);
        SubScene sub = createGUI(stage);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(0.1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        updateFrame(sub);
                    }
                });
        timeline.getKeyFrames().add(kf);
        timeline.play();
        stage.show();
		
	}
	
	public SubScene createSubScene() {
		root3D = new Group();
		camera = new PerspectiveCamera(true);
		camera.setTranslateZ(-1000);
		camera.setNearClip(0.1);
		camera.setFarClip(3000.0);
		
		root3D.getChildren().add(camera);
		
		SubScene mySub = new SubScene(root3D, 100, 100);
		mySub.setFill(Color.LIGHTGREEN);
		
		//moving the camera -> but it firstly has to work
		mySub.setOnDragDetected((event) -> this.dragPosStart = new Vector3D(event.getX(), event.getY(), 0));
		mySub.setOnMouseDragged((event) -> {
			if (this.dragPosStart != null) {
				Vector3D dragPosCurrent = new Vector3D(event.getX(), event.getY(), 0);
				dragPosCurrent.sub(this.dragPosStart);
				dragPosStart = new Vector3D(event.getX(), event.getY(), 0);
				camera.setTranslateX(dragPosCurrent.xAxis);
				camera.setTranslateY(dragPosCurrent.yAxis);
			}
		});
		mySub.setOnMouseReleased((event) -> this.dragPosStart = null);
		
		
		return mySub;
		
	}
	public void updateFrame(SubScene sub) {
		root3D.getChildren().clear();
		root3D.getChildren().add(camera);
		sub.setCamera(camera);
		
		for (Body body : bodySystem.getBodies()) {

			            double otherX = transformer.modelToOtherX(body.location.xAxis);
			            double otherY = transformer.modelToOtherY(body.location.yAxis);

			            // draw object sphere
			            Sphere planet = new Sphere(body.radius / INITIAL_SCALE);
			            root3D.getChildren().add(planet);

			            // draw label
			            Text text = new Text(body.name);
			            root3D.getChildren().add(new Text(otherX - (text.getLayoutBounds().getWidth() / 2), otherY - body.radius/INITIAL_SCALE - (text.getLayoutBounds().getHeight() / 2),body.name));
			        }
					sub.setRoot(root3D);

			        bodySystem.update(TIME_SLICE);
			        timeLabel.setText(bodySystem.getElapsedTimeAsString());
			        fpsLabel.setText("FPS: " + fps.countFrame());
			        scaleLabel.setText(String.format("Scale: %d km/pixel", Math.round(transformer.getScale()/1000)));
			    }
	
	public SubScene createGUI(Stage stage) {
		BorderPane border = new BorderPane();
        createTimeLabel();
        createFPSLabel();
        createScaleLabel();
        HBox hbox = createHBox();
        border.setBottom(hbox);
        
        SubScene graphics = createSubScene();
        border.setCenter(graphics);
        stage.setTitle("Solar System");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setMaximized(true);
        
        //Bind subscene size to stack pane size
        graphics.widthProperty().bind(stage.widthProperty());
        graphics.heightProperty().bind(stage.heightProperty().subtract(BOTTOM_AREA_HEIGHT));
        return graphics;
	}
	
	protected void createBodies() {
        this.bodySystem = new SolarSystem();
    }
	
	
	private HBox createHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");
        hbox.setFillHeight(true);
        hbox.getChildren().add(this.timeLabel);
        hbox.getChildren().add(this.fpsLabel);
        hbox.getChildren().add(this.scaleLabel);
        return hbox;
    }

    private void createTimeLabel() {
        timeLabel = new Label();
        timeLabel.setPrefSize(500, 20);
    }

    private void createFPSLabel() {
        fpsLabel = new Label();
        fpsLabel.setPrefSize(100, 20);
    }

    private void createScaleLabel() {
        scaleLabel = new Label();
        scaleLabel.setPrefSize(300, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
*/
