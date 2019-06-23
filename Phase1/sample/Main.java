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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import java.util.ArrayList;

public class Main extends Application {
    //second to update the model(deltaT)
    public final long TIME_SLICE = 210;

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
    private Button fastForwardYearButton;
    private Button fastForwardHalfButton;
    private boolean paused = true;
    private ArrayList<BodySystem> oldSystems = new ArrayList<>();
    private int currentSystem = 0;
    private Button playBackButton;
    private boolean change = false;
    private RocketModel r;
    private Label launchLabel;
    private Label launchLabel2;
    private int i = 0;

    @Override
    public void start(Stage stage) {
        createBodies();
        Trajectory.pleaseJustRun();
        r = new RocketModel(null, Trajectory.earthToTitan, Trajectory.titanToEarth);
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
        gc.setFill(Color.LIGHTBLUE); //can be another color
        gc.fillRect(0, 0, canvasWidth, canvasHeight);

        if (change) {
            if (currentSystem>0) {
                int tryFor = bodySystem.getBodies().size();
                bodySystem.getBodies().clear();
                for (int i = 0; i <tryFor; i++) {
                    bodySystem.getBodies().add(oldSystems.get(currentSystem-1).getBodies().get(i).copy());
                    bodySystem.setSeconds(oldSystems.get(currentSystem-1).getSeconds());
                    timeLabel.setText(bodySystem.getElapsedTimeAsString());
                }
                currentSystem--;
            }
            change = false;
        }
        if (!paused) {
        	if(bodySystem.getSeconds()>Trajectory.launchToTitan && Trajectory.landOnTitan>bodySystem.getSeconds()){
        		if(i == 0) {
        			r.location =bodySystem.getBodies().get(4).getLocation().copy();
        			i++;
        		}
        		r.calculateVelocityFromThrust(r.earthToTitan, TIME_SLICE);
        		gc.setFill(Color.BLACK);
        		
        		double otherX = transformer.modelToOtherX(r.location.x);
        		double otherY = transformer.modelToOtherY(r.location.y);
            
        		//gc.fillOval(r.location.x - 3, r.location.y - 3, 3*2, 3* 2);
        		gc.fillOval(otherX, otherY, 6, 6);
        		//gc.fillText("Titanic", r.location.x + 10, r.location.y + 10);
        		gc.fillText("Titanic", otherX + 5, otherY+ 5);
        		System.out.println("yassss");
        	}else if(bodySystem.getSeconds()>Trajectory.launchToEarth && Trajectory.landOnEarth>bodySystem.getSeconds()){
        		if ( i ==1) {
        			r.location = bodySystem.getBodies().get(10).getLocation().copy();
        			i++;
        		}
        		double otherX = transformer.modelToOtherX(r.location.x);
        		double otherY = transformer.modelToOtherY(r.location.y);
        		
        		r.calculateVelocityFromThrust(r.titanToEarth, TIME_SLICE);
        		gc.setFill(Color.BLACK);
        		//gc.fillOval(r.location.x - 3, r.location.y - 3, 3*2, 3* 2);
        		gc.fillOval(otherX, otherY, 6, 6);
        		//gc.fillText("Earthanic", r.location.x + 10, r.location.y + 10);
        		gc.fillText("Earthanic", otherX + 5, otherY + 5);
        		System.out.print("woop");
        	}
        }

        for (Body body : bodySystem.getBodies()) {

/*
            System.out.println(body.toString());
            System.out.println("px:" + transformer.modelToOtherX(body.location.xAxis) + ", py:" + transformer.modelToOtherY(body.location.yAxis));
            System.out.println();
*/

            double otherX = transformer.modelToOtherX(body.location.x);
            double otherY = transformer.modelToOtherY(body.location.y);

            double BODY_RADIUS = Math.sqrt((body.getRadius()/4E+6)/(2*transformer.getScale()/6.0E9));
            // draw object circle

            gc.setFill(Color.BLACK);
            gc.fillOval(otherX - BODY_RADIUS, otherY - BODY_RADIUS, BODY_RADIUS * 2, BODY_RADIUS * 2);

            // draw label
            Text text = new Text(body.name);
            gc.fillText(body.name, otherX - (text.getLayoutBounds().getWidth() / 2), otherY - BODY_RADIUS - (text.getLayoutBounds().getHeight() / 2));
        }
        
        if (!paused) {
            bodySystem.update(TIME_SLICE);
            timeLabel.setText(bodySystem.getElapsedTimeAsString());
            if(bodySystem.getSeconds()% (60*60*24*365) == 0) {//every year
                oldSystems.add(bodySystem.copy());
                currentSystem++;
            }

        }
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
        createLaunchLabels();
        HBox hbox = createHBox();
        border.setBottom(hbox);
        HBox top = createHBoxTwo();
        border.setTop(top);

        Canvas canvas = createCanvas();
        border.setCenter(canvas);
        stage.setTitle("Solar System using N-Body");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setMaximized(true);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty().subtract(BOTTOM_AREA_HEIGHT * 2));
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
        HBox buttons = new HBox();

        createPauseButton();
        createFastForwardHalfButton();
        createFastForwardYearButton();
        createPlayBackButton();

        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(5);
        buttons.setStyle("-fx-background-color: #336699;");
        buttons.setFillHeight(true);

        buttons.getChildren().add(playBackButton);
        buttons.getChildren().add(pauseButton);
        buttons.getChildren().add(fastForwardHalfButton);
        buttons.getChildren().add(fastForwardYearButton);

        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");
        hbox.setFillHeight(true);
        VBox newBox = new VBox();
        newBox.setPadding(new Insets(0,0, 0, 0));
        newBox.setSpacing(1);
        newBox.setStyle("- fx-background-color: #336699");
        newBox.setFillWidth(true);
        newBox.getChildren().add(this.timeLabel);
        newBox.getChildren().add(this.launchLabel);
        
        hbox.getChildren().add(newBox);
        hbox.getChildren().add(this.fpsLabel);
        hbox.getChildren().add(this.scaleLabel);
        hbox.getChildren().add(buttons);
        return hbox;
    }

    private HBox createHBoxTwo() {
        HBox hbox = new HBox();

        Label title = new Label();
        title.setText("Model of the Solar System");
        title.setFont(new Font("Serif", 40));
        title.setTextFill(Color.WHITE);

        hbox.getChildren().add(title);
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");
        hbox.setFillHeight(true);
        return hbox;
    }

    private void createPauseButton() {
        pauseButton = new Button();
        pauseButton.setText("Play");
        pauseButton.setFont(new Font("Serif", 16));
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if (paused) {
                    paused = false;
                    pauseButton.setText("Pause");
                }
                else {
                    paused = true;
                    pauseButton.setText("Play");
                }
            }
        });

    }

    private void createFastForwardHalfButton() {
        fastForwardHalfButton = new Button();
        fastForwardHalfButton.setText("Skip 1/2 year");
        fastForwardHalfButton.setFont(new Font("Serif", 16));
        fastForwardHalfButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for (int i = 0; i < (60*60*24*365*0.5)/TIME_SLICE; i++) {
                    bodySystem.update(TIME_SLICE);
                    timeLabel.setText(bodySystem.getElapsedTimeAsString());
                    if (bodySystem.getSeconds() % (60*60*24*365) == 0) {
                        oldSystems.add(bodySystem.copy());
                        currentSystem++;
                    }
                }
            }
        });
    }

    private void createFastForwardYearButton() {
        fastForwardYearButton = new Button();
        fastForwardYearButton.setText("Skip 3 years");
        fastForwardYearButton.setFont(new Font("Serif", 16));
        fastForwardYearButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                for (int i = 0; i <(60*60*24*365*3)/TIME_SLICE; i++) {
                    bodySystem.update(TIME_SLICE);
                    timeLabel.setText(bodySystem.getElapsedTimeAsString());
                    if (bodySystem.getSeconds() % (60*60*24*365) == 0) {
                        oldSystems.add(bodySystem.copy());
                        currentSystem++;
                    }
                }
            }
        });
    }

    private void createPlayBackButton() {
        playBackButton = new Button();
        playBackButton.setText("Rewind 1 year");
        playBackButton.setFont(new Font("Serif", 16));
        playBackButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                change = true;
            }
        });
    }

    private void createTimeLabel() {
        timeLabel = new Label();
        timeLabel.setPrefSize(500, 20);
        timeLabel.setFont(new Font("Serif", 16));
        timeLabel.setTextFill(Color.WHITE);
    }

    private void createFPSLabel() {
        fpsLabel = new Label();
        fpsLabel.setPrefSize(100, 20);
        fpsLabel.setFont(new Font("Serif", 16));
        fpsLabel.setTextFill(Color.WHITE);
    }

    private void createScaleLabel() {
        scaleLabel = new Label();
        scaleLabel.setPrefSize(300, 20);
        scaleLabel.setFont(new Font("Serif", 16));
        scaleLabel.setTextFill(Color.WHITE);
    }
    
    private void createLaunchLabels() {
    	launchLabel = new Label();
    	launchLabel.setPrefSize(300, 200);
    	launchLabel.setFont(new Font("Serif", 16));
    	launchLabel.setTextFill(Color.WHITE);
    	launchLabel.setText("The launch to titan is at: " + Trajectory.launchToTitan + " seconds.");
    	
    	launchLabel2 = new Label();
    	launchLabel2.setPrefSize(300, 200);
    	launchLabel2.setFont(new Font("Serif", 16));
    	launchLabel2.setTextFill(Color.WHITE);
    	launchLabel2.setText("The launch to earth is at: " + Trajectory.launchToEarth + " seconds.");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
