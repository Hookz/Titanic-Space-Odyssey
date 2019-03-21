/*
Fixed earth leaving moon by modifying TIME_SlICE to be slower.  Our theory is there might be some calculation error happening in second due to double.
Maybe use float to gain better accuracy and convert it to double if we still want to use seconds as a TIME_SLICE.
 */

package sample;

//TODO Take advantage of celestistial body to see it could be used for rocket by using high rocket launch.
//TODO make an algorithm to compute missle trajectory.
//TODO make button change TIME_Slice for pause button
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;

public class Main extends Application {
    //second to update the model(deltaT)
    public final double TIME_SLICE = 200;

    //initial zoom
    public static final double INITIAL_SCALE = 6e9;

    public static final double BODY_RADIUS_GUI = 2;

    private static final int BOTTOM_AREA_HEIGHT = 100;

    private BodySystem bodySystem;

    private CoordinatesTransformer transformer = new CoordinatesTransformer();

    //fps
    private FPSCounter fps = new FPSCounter();

    private double canvasWidth = 0;
    private double canvasHeight = 0;
    private Vector3D dragPosStart;
    private Label timeLabel;
    private Label fpsLabel;
    private Label scaleLabel;
    private Button pauseButton;

    @Override
    public void start(Stage stage) {
        createBodies();
        transformer.setScale(INITIAL_SCALE);
        transformer.setOriginXForOther(500);
        transformer.setOriginYForOther(500);
        GraphicsContext gc = createGui(stage);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(0.1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent ae) {
                        updateFrame(gc);
                    }
                });
        timeline.getKeyFrames().add(kf);
        timeline.play();
        stage.show();
    }

    protected void updateFrame(GraphicsContext gc) {
        this.canvasWidth = gc.getCanvas().getWidth();
        this.canvasHeight = gc.getCanvas().getHeight();
        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        for (Body body : bodySystem.getBodies()) {

/*
            System.out.println(body.toString());
            System.out.println("px:" + transformer.modelToOtherX(body.location.x) + ", py:" + transformer.modelToOtherY(body.location.y));
            System.out.println();
*/

            double otherX = transformer.modelToOtherX(body.location.x);
            double otherY = transformer.modelToOtherY(body.location.y);

            // draw object circle
            gc.setFill(Color.BLACK);
            gc.fillOval(otherX - BODY_RADIUS_GUI, otherY - BODY_RADIUS_GUI, BODY_RADIUS_GUI * 2, BODY_RADIUS_GUI * 2);

            // draw eliptical path


            // draw label
            Text text = new Text(body.name);
            gc.fillText(body.name, otherX - (text.getLayoutBounds().getWidth() / 2), otherY - BODY_RADIUS_GUI - (text.getLayoutBounds().getHeight() / 2));
        }

        bodySystem.update(TIME_SLICE);
        timeLabel.setText(bodySystem.getElapsedTimeAsString());
        fpsLabel.setText("FPS: " + fps.countFrame());
        scaleLabel.setText(String.format("Scale: %d km/pixel", Math.round(transformer.getScale()/1000)));
    }

    protected void createBodies() {
        this.bodySystem = new SolarSystem();
    }

    private GraphicsContext createGui(Stage stage) {
        BorderPane border = new BorderPane();
        createTimeLabel();
        createFPSLabel();
        createScaleLabel();
        HBox hbox = createHBox();
        border.setBottom(hbox);
        Canvas canvas = createCanvas();
        border.setCenter(canvas);
        stage.setTitle("Solar System using N-Body");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setMaximized(true);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty().subtract(BOTTOM_AREA_HEIGHT));
        return canvas.getGraphicsContext2D();
    }

    private Canvas createCanvas() {
        Canvas canvas = new ResizableCanvas();

        // dragging of map
        canvas.setOnDragDetected((event) -> this.dragPosStart = new Vector3D(event.getX(), event.getY(), 0));
        canvas.setOnMouseDragged((event) -> {
            if (this.dragPosStart != null) {
                Vector3D dragPosCurrent = new Vector3D(event.getX(), event.getY(), 0);
                dragPosCurrent.sub(this.dragPosStart);
                dragPosStart = new Vector3D(event.getX(), event.getY(), 0);
                transformer.setOriginXForOther(transformer.getOriginXForOther() + dragPosCurrent.x);
                transformer.setOriginYForOther(transformer.getOriginYForOther() + dragPosCurrent.y);
            }
        });
        canvas.setOnMouseReleased((event) -> this.dragPosStart = null);

        // zooming (scaling)
        canvas.setOnScroll((event) -> {
            if (event.getDeltaY() > 0) {
                transformer.setScale(transformer.getScale() * 0.9);
            } else {
                transformer.setScale(transformer.getScale() * 1.1);
            }
        });
        return canvas;
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

    private void createPauseButton(){
        pauseButton = new Button("Pause the Universe");
    }

    public static void main(String[] args) {
        RocketModel r = new RocketModel(SpaceObject.EARTH, SpaceObject.TITAN, SpaceObject.SUN);
        System.out.println(r.aTranfer());
        System.out.println(r.peroidOfTransfer());
        System.out.println(r.earthVelocity());
        System.out.println(r.titanVelocity());
        System.out.println(r.velocityPerihelion());
        System.out.println(r.velocityAphelion());
        System.out.println(r.deltaV1());
        System.out.println(r.deltaV2());
        //scale this and comment on what the measurements are.  Even I'm confused.
        System.out.println(r.timeOfFlight()); //au to seconds.  Yikes.
        System.out.println(r.driftPeriod()); //au to seconds.  Yikes.
        System.out.println(r.degreeNeeded());
        launch(args);
    }

}