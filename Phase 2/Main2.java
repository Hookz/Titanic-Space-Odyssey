package Land;

import java.util.ArrayList;
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
import sample.CoordinatesTransformer;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class Main2 extends Application {
    //second to update the model(deltaT)
    public final double TIME_SLICE = 1;

    public static final double SCALE = 2000;

    public static final double BODY_RADIUS_GUI = 2;

    private static final int BOTTOM_AREA_HEIGHT = 100;
    
    public static final double REAL_SCALE = 2.25 / 1000; //0.75 (on the screen) for 1 kilometer, although this is measured in meters

    private FPSCounter fps = new FPSCounter();

    private double canvasWidth = 0;
    private double canvasHeight = 0;
    private Label timeLabel;
    private Label fpsLabel;
    private Label landingLabel;
    private Label velocityLabelX;
    private Label velocityLabelY;
    private Label goalLabel;
    private Button pauseButton;
    private Button fastForwardTwentyMinButton;
    private Button fastForwardFiveMinButton;
    private boolean paused = true;
    private Button playBackButton;
    private boolean change = false;
    private double spaceshipMass = 1000; // kg
    public MovingSpaceShip spaceShip;
    private Vector2D locLanding;
    
    private int currentSpaceShip = 0;
    private ArrayList<MovingSpaceShip> oldSpaceShips = new ArrayList<>();
    
    @Override
    public void start(Stage stage) {
        createSpaceship();
        GraphicsContext gc = createGui(stage);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame kf = new KeyFrame(
                Duration.millis(1),
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
        
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvasWidth, canvasHeight);
        
        gc.setFill(Color.WHITE); 
        gc.setStroke(Color.WHITE);
        gc.strokeLine(0, 550, 1450, 550);//this is the ground
        
        gc.fillOval(locLanding.x, locLanding.y, 20, 5);
        gc.fillText("Landing location", locLanding.x - 40, locLanding.y + 30);
        
        if ((spaceShip.getLocation().y * REAL_SCALE) > 550 - spaceShip.getLength()) {
        	paused = true;
        	landingLabel.setText("Landed!");
        	pauseButton.setText("Play");
        	if (spaceShip.getLocation().x < locLanding.x + 30 && spaceShip.getLocation().x > locLanding.x - 30) {
        		goalLabel.setText("Landing succeeded");
        	}
        	else {
        		goalLabel.setText("Landing failed");
        	}
        }
        
        // when yAxis < 0, then stop -> it has hit titan
        
        if (change) {//to rewind - using the rewind buttons
        	if (currentSpaceShip>0) {	
        		spaceShip = oldSpaceShips.get(oldSpaceShips.size()-1).copyMSS();
        		oldSpaceShips.remove(oldSpaceShips.size()-1);
        		spaceShip.setSeconds(oldSpaceShips.get(oldSpaceShips.size()-1).getSeconds());
        		timeLabel.setText(spaceShip.getElapsedTimeAsString());
        		currentSpaceShip--;
        		landingLabel.setText("Landing...");
        	}
        	change = false;
        }
        
        for (MovingSpaceShip spaces : oldSpaceShips) {
        gc.fillOval(spaces.getLocation().x * REAL_SCALE, spaces.getLocation().y * REAL_SCALE, 5, 5 );
        }
        
        gc.fillRect(spaceShip.getLocation().x * REAL_SCALE, spaceShip.getLocation().y * REAL_SCALE , spaceShip.getWidth(), spaceShip.getLength());
        
        if (!paused) {//the normal updates
        	spaceShip.update(TIME_SLICE);
            timeLabel.setText(spaceShip.getElapsedTimeAsString());
            if(spaceShip.getSeconds()% (60*20) == 0 && spaceShip.getSeconds() != 0) {//every 10 min, save a copy
            	oldSpaceShips.add(spaceShip.copyMSS());
            	currentSpaceShip++;
            }
        }
        fpsLabel.setText("FPS: " + fps.countFrame());
        velocityLabelX.setText("Velocity xAxis =" + spaceShip.getVelocity().x);
        velocityLabelY.setText("Velocity yAxis = " + spaceShip.getVelocity().y);
    }
    
    private void createSpaceship() {
    	//arbitrary numbers:
    	Vector2D spaceshipLoc = new Vector2D(10, 20);
    	Vector2D spaceshipVeloc = new Vector2D(0, 0);
    	double length = 20;
    	double width = 10;
    	
    	spaceShip = new MovingSpaceShip(spaceshipMass, spaceshipLoc, spaceshipVeloc, length, width);
    	
    	locLanding = new Vector2D(200, 550); //marks the desired landing site
    	
    }


    private GraphicsContext createGui(Stage stage) {
        BorderPane border = new BorderPane();
        createTimeLabel();
        createFPSLabel();
        createLandingLabel();
        createVelocityLabels();
        createGoalLabel();
        
        HBox hbox = createHBox();
        border.setBottom(hbox);
        HBox top = createHBoxTwo();
        border.setTop(top);
        
        Canvas canvas = new Canvas();//createCanvas();
        border.setCenter(canvas);
        stage.setTitle("Moon Lander");
        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setMaximized(true);

        // Bind canvas size to stack pane size.
        canvas.widthProperty().bind(stage.widthProperty());
        canvas.heightProperty().bind(stage.heightProperty().subtract(BOTTOM_AREA_HEIGHT * 2));
        return canvas.getGraphicsContext2D();
    }
    
    private HBox createHBox() {
        HBox hbox = new HBox();
        HBox buttons = new HBox();
        
        createPauseButton();
        createFastForwardFiveMinButton();
        createFastForwardTwentyMinButton();
        createPlayBackButton();
        
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setSpacing(5);
        buttons.setStyle("-fx-background-color: #FFF44F;");
        buttons.setFillHeight(true);
        
        buttons.getChildren().add(playBackButton);
        buttons.getChildren().add(pauseButton);
        buttons.getChildren().add(fastForwardFiveMinButton);
        buttons.getChildren().add(fastForwardTwentyMinButton);
        
        VBox labels = new VBox();
        labels.getChildren().add(velocityLabelX);
        labels.getChildren().add(velocityLabelY);
        labels.getChildren().add(landingLabel);
        labels.setSpacing(10);
        labels.setStyle("-fx-background-color: #FFF44F;");
        labels.setFillWidth(true);
        
        VBox moreLabels = new VBox();
        moreLabels.getChildren().add(timeLabel);
        moreLabels.getChildren().add(goalLabel);
        moreLabels.setSpacing(10);   // Gap between nodes
        moreLabels.setStyle("-fx-background-color: #FFF44F;");
        moreLabels.setFillWidth(true);
        
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #FFF44F;");
        hbox.setFillHeight(true);
        hbox.getChildren().add(moreLabels);
        hbox.getChildren().add(this.fpsLabel);
        hbox.getChildren().add(labels);
        hbox.getChildren().add(buttons);
        return hbox;
    }

    private HBox createHBoxTwo() {
    	HBox hbox = new HBox();
    	
    	Label title = new Label();
    	title.setText("Landing on Titan");
    	title.setFont(new Font("Serif", 40));
    	title.setTextFill(Color.BLACK);
    	
    	hbox.getChildren().add(title);
    	hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #FFF44F;");
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
    
    private void createFastForwardFiveMinButton() {
    	fastForwardFiveMinButton = new Button();
    	fastForwardFiveMinButton.setText("Skip five min");
    	fastForwardFiveMinButton.setFont(new Font("Serif", 16));
    	fastForwardFiveMinButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            	for (int i = 0; i <(60*5)/TIME_SLICE ; i++) {
            		spaceShip.update(TIME_SLICE);
            		if (spaceShip.getLocation().y * REAL_SCALE > 550 - spaceShip.getLength()) {
                    	paused = true;
                    	landingLabel.setText("Landed!");
                    	pauseButton.setText("Play");
                    	if (spaceShip.getLocation().x < locLanding.x + 10 && spaceShip.getLocation().x > locLanding.x - 10 && spaceShip.getLocation().y < locLanding.y + 10 && spaceShip.getLocation().y > locLanding.y - 10) {
                    		goalLabel.setText("Landing succeeded");
                    	}
                    	else {
                    		goalLabel.setText("Landing failed");
                    	}
                    	break;
                    }
    				timeLabel.setText(spaceShip.getElapsedTimeAsString());
                    if (spaceShip.getSeconds() % (60*20) == 0) {
    	            	oldSpaceShips.add(spaceShip.copyMSS());
    	            	currentSpaceShip++;
    	            }
    			}
            }
        });
    }
    
    private void createFastForwardTwentyMinButton() {
    	fastForwardTwentyMinButton = new Button();
    	fastForwardTwentyMinButton.setText("Skip twenty min");
    	fastForwardTwentyMinButton.setFont(new Font("Serif", 16));
    	fastForwardTwentyMinButton.setOnAction(new EventHandler<ActionEvent>() {
    		public void handle(ActionEvent e) {
    			for (int i = 0; i <(60*20)/TIME_SLICE; i++) {
    				spaceShip.update(TIME_SLICE);
    				if (spaceShip.getLocation().y * REAL_SCALE > 550 - spaceShip.getLength()) {
    		        	paused = true;
    		        	landingLabel.setText("Landed!");
    		        	pauseButton.setText("Play");
    		        	if (spaceShip.getLocation().x < locLanding.x + 10 && spaceShip.getLocation().x > locLanding.x - 10 && spaceShip.getLocation().y < locLanding.y + 10 && spaceShip.getLocation().y > locLanding.y - 10) {
    		        		goalLabel.setText("Landing succeeded");
    		        	}
    		        	else {
    		        		goalLabel.setText("Landing failed");
    		        	}
    		        	break;
    		        }
    				timeLabel.setText(spaceShip.getElapsedTimeAsString());
                    if (spaceShip.getSeconds() % (60*20) == 0) {
    	            	oldSpaceShips.add(spaceShip.copyMSS());
    	            	currentSpaceShip++;
    	            }
    			}
    		}
    	});
    }
    
    private void createPlayBackButton() {
    	playBackButton = new Button();
    	playBackButton.setText("Rewind 20 min");
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
        timeLabel.setTextFill(Color.BLACK);
    }

    private void createFPSLabel() {
        fpsLabel = new Label();
        fpsLabel.setPrefSize(100, 20);
        fpsLabel.setFont(new Font("Serif", 16));
        fpsLabel.setTextFill(Color.BLACK);
    }

    private void createLandingLabel() {
        landingLabel = new Label();
        landingLabel.setPrefSize(300, 20);
        landingLabel.setFont(new Font("Serif", 16));
        landingLabel.setTextFill(Color.BLACK);
        landingLabel.setText("Landing...");
    }
    
    private void createVelocityLabels() {
    	velocityLabelX = new Label();
    	velocityLabelX.setPrefSize(300, 20);
    	velocityLabelX.setFont(new Font("Serif", 16));
    	velocityLabelX.setTextFill(Color.BLACK);
    	velocityLabelX.setText("Velocity xAxis");
    	
    	velocityLabelY = new Label();
    	velocityLabelY.setPrefSize(300, 20);
    	velocityLabelY.setFont(new Font("Serif", 16));
    	velocityLabelY.setTextFill(Color.BLACK);
    	velocityLabelY.setText("Velocity yAxis");
    	
    }
    
    private void createGoalLabel() {
    	goalLabel = new Label();
        goalLabel.setPrefSize(300, 20);
        goalLabel.setFont(new Font("Serif", 16));
        goalLabel.setTextFill(Color.BLACK);
        goalLabel.setText("Pending...");
    }

    public static void main(String[] args) {
        launch(args);
    }

}
