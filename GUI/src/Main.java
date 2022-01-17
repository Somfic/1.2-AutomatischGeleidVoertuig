import Logger.LogMessage;
import Logger.Logger;
import Logger.LoggerListener;
import Logic.BluetoothListener;
import Logic.BluetoothLogic;
import Logic.BluetoothMessage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application implements BluetoothListener, LoggerListener {

    private boolean isLineFollowingMode = true;
    private BluetoothLogic BLUETOOTH = new BluetoothLogic(this);

    private BorderPane mainPane;

    private Pane adminPane;
    private Pane lineFollowerPane;
    private Pane manualPane;

    private Stage stage;

    private Button startStopButton;
    private Scene connectingScene;
    private Scene mainScene;

    private final Logger LOGGER = new Logger(this);

    private TextArea robotLogArea;
    private TextArea guiLogArea;

    private Button forwards;
    private Button backwards;
    private Button left;
    private Button right;

   private Point startPoint = new Point(0,0);
   private ArrayList<Point> waypoints = new ArrayList<>();
   private Point endPoint = new Point(0,0);

    private TextArea waypointsArea;

    @Override
    public void start(Stage stage) {

        Logger.setSource("Gui");
        Logger.setListener(this);

        this.stage = stage;

        // Build the panes
        lineFollowerPane = buildLineFollowerPane();
        manualPane = buildManualPane();
        adminPane = buildAdminPane();

        // Build the scenes
        mainScene = buildMainScene();
        connectingScene = buildConnectingScene();

        // Show the scene
        stage.setTitle("BoeBot");

        stage.setScene(connectingScene);

        //stage.setMaximized(true);
        stage.show();
    }

    private Pane buildAdminPane() {

        HBox logs = new HBox();

        VBox guiLogs = new VBox();
        Label guiLabel = new Label("Gui");
        guiLogArea = new TextArea();
        guiLogArea.setEditable(false);
        guiLogs.getChildren().addAll(guiLabel, guiLogArea);

        VBox robotLogs = new VBox();
        Label robotLabel = new Label("Robot");
        robotLogArea = new TextArea();
        robotLogArea.setEditable(false);
        robotLogs.getChildren().addAll(robotLabel, robotLogArea);

        logs.getChildren().addAll(guiLogs, robotLogs);

        BorderPane pane = new BorderPane();

        pane.setCenter(logs);
        pane.setPadding(new Insets(10, 10, 10, 10));

        return pane;
    }

    public Scene buildMainScene() {
        Pane taskBar = buildTaskbar();

        // Build the main scene
        mainPane = new BorderPane();
        mainPane.setTop(taskBar);

        // Set the main component
        TabPane tabs = new TabPane();

        Tab manualTab = new Tab("Handmatige controle", manualPane);
        manualTab.setClosable(false);

        Tab lineFollowerTab = new Tab("Lijnvolger", lineFollowerPane);
        lineFollowerTab.setClosable(false);

        Tab adminTab = new Tab("Admin", adminPane);
        adminTab.setClosable(false);

        tabs.getTabs().add(manualTab);
        tabs.getTabs().add(lineFollowerTab);
        tabs.getTabs().add(adminTab);

        mainPane.setCenter(tabs);

        // Build the main scene
        return new Scene(mainPane);
    }

    public Scene buildConnectingScene() {
        // Build the connecting scene
        BorderPane pane = new BorderPane();
        pane.setCenter(new Text("Connecting to BoeBot ..."));

        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> {
            BLUETOOTH.setPort(4);
            BLUETOOTH.open();
        });

        pane.setBottom(connectButton);

        return new Scene(pane);
    }

    public String mode = "";

    public ArrayList<ArrayList<Button>> buttons = new ArrayList<ArrayList<Button>>();

    public Pane buildLineFollowerPane() {
        BorderPane pane = new BorderPane();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        waypointsArea = new TextArea();

        VBox buttons = new VBox();
        Button startPositionButton = new Button("Set start position");
        Button addWaypointButton = new Button("Add waypoint");
        Button clearWaypointButton = new Button("Clear waypoints");
        Button endPositionButton = new Button("Set end position");
        Button executeButton = new Button("Upload route");
        Button cancelButton = new Button("Clear route");


        buttons.getChildren().add(startPositionButton);
        buttons.getChildren().add(addWaypointButton);
        buttons.getChildren().add(waypointsArea);
        buttons.getChildren().add(clearWaypointButton);
        buttons.getChildren().add(endPositionButton);
        buttons.getChildren().add(executeButton);
        buttons.getChildren().add(cancelButton);

        startPositionButton.setOnAction(e -> {
            mode = "start";
            updateGrid();
        });

        addWaypointButton.setOnAction(e -> {
            mode = "waypoint";
            updateGrid();
        });

        clearWaypointButton.setOnAction(e -> {
            waypoints.clear();
            updateGrid();
        });

        endPositionButton.setOnAction(e -> {
            mode = "end";
            updateGrid();
        });

        executeButton.setOnAction(e -> {
            ArrayList<Point> path = new ArrayList<>();
            path.add(startPoint);
            path.addAll(waypoints);
            path.add(endPoint);

            String pathString = "";
            for (Point p : path) {
                pathString += p.x + "," + p.y + "-";
            }

            this.BLUETOOTH.send("route", pathString);
        });

        int width = 5;
        int height = 7;

        for (int x = 0; x < height; x++) {
            this.buttons.add(new ArrayList<>());

            for (int y = 0; y < width; y++) {
                Button button = new Button();
                button.setPrefSize(50, 50);

                int finalY = y;
                int finalX = x;
                button.setOnAction(e -> {
                    LOGGER.info("Pressed: " + finalX + "," + finalY);

                    processGridPress(new Point(finalX, finalY));
                });
                grid.add(button, x, y);

                this.buttons.get(x).add(button);
            }
        }

        pane.setRight(buttons);
        pane.setCenter(grid);

        Button calibrate = new Button("Calibrate");
        calibrate.setOnAction(e -> {
            BLUETOOTH.send("calibrate");
        });

        pane.setBottom(calibrate);

        pane.setPadding(new Insets(10, 10, 10, 10));

        return pane;
    }

    private void processGridPress(Point pressedPoint) {
        if (mode.equals("start")) {
            startPoint = pressedPoint;
        }

        if (mode.equals("waypoint")) {
            waypoints.add(pressedPoint);
        }


        if (mode.equals("end")) {
           endPoint = pressedPoint;
        }

        updateGrid();
    }

    public void updateGrid() {
        for (int x = 0; x < buttons.size(); x++) {
            for (int y = 0; y < buttons.get(0).size(); y++) {
                Button button = buttons.get(x).get(y);

                if (x == startPoint.x && y == startPoint.y) {
                    // Start button
                    button.setText("Start");
                } else if (x == endPoint.x && y == endPoint.y) {
                    // End button
                    button.setText("End");
                } else {
                    // Normal button
                    button.setText("");
                    button.setDisable(false);
                }
            }
        }

        waypointsArea.clear();
        for (Point waypoint : waypoints) {
            waypointsArea.appendText(waypoint.x + ", " + waypoint.y + "\n");
        }

        int x = startPoint.x;
        int y = startPoint.y;

        ArrayList<Point> path = new ArrayList<>();
        path.addAll(waypoints);
        path.add(endPoint);

        for(Point waypoint : path) {
            this.LOGGER.info("Calculating path from " + x + "," + y + " to " + waypoint.x + "," + waypoint.y);

            while (x != waypoint.x || y != waypoint.y) {
                if (x < waypoint.x) {
                    x++;
                } else if (x > waypoint.x) {
                    x--;
                } else {
                    if (y < waypoint.y) {
                        y++;
                    } else {
                        y--;
                    }
                }

                buttons.get(x).get(y).setText("X");
            }
        }
    }

    public Pane buildManualPane() {
        BorderPane pane = new BorderPane();

        VBox acceleration = new VBox();

        // acceleratie omhoog
        Button accelerationPlus = new Button("acceleration +");
        accelerationPlus.setOnAction(event -> {
            BLUETOOTH.send("acceleration", "increase");
        });

        // acceleratie omlaag
        Button accelerationMin = new Button("acceleration -");
        accelerationMin.setOnAction(event -> {
            BLUETOOTH.send("acceleration", "decrease");
        });

        acceleration.getChildren().addAll(accelerationPlus, accelerationMin);
        pane.setBottom(acceleration);

        HBox controls = new HBox();
        forwards = new Button("\uD83E\uDC81");
        left = new Button("\uD83E\uDC80");
        right = new Button("\uD83E\uDC82");
        backwards = new Button("\uD83E\uDC83");

        forwards.setOnAction(e -> {
            BLUETOOTH.send("move", "forwards");
        });

        backwards.setOnAction(e -> {
            BLUETOOTH.send("move", "backwards");
        });

        left.setOnAction(e -> {
            BLUETOOTH.send("move", "left");
        });

        right.setOnAction(e -> {
            BLUETOOTH.send("move", "right");
        });

        controls.setAlignment(Pos.BOTTOM_CENTER);
        controls.getChildren().addAll(left, new VBox(forwards, backwards), right);

        pane.setCenter(new Group(controls));

        pane.setPadding(new Insets(10, 10, 10, 10));

        return pane;
    }

    public Pane buildTaskbar() {
        startStopButton = new Button("Start");
        startStopButton.setOnAction(e -> {
            BLUETOOTH.send("start-stop", "toggle");
            if (startStopButton.getText().equals("Start")) {
                startStopButton.setText("Stop");
            } else {
                startStopButton.setText("Start");
            }

            // todo: automatically change text of startStopButton to Start/Stop depending on current state
        });

        // change mode
        Button changeMode = new Button("Verander modus");
        changeMode.setOnAction(e -> {
            isLineFollowingMode = !isLineFollowingMode;

            if (isLineFollowingMode) {
                BLUETOOTH.send("mode", "line-following");
            } else {
                BLUETOOTH.send("mode", "manual");
            }

            //todo: change to correct tab
        });

        return new HBox(startStopButton, changeMode);
    }

    @Override
    public void onBluetoothMessage(BluetoothMessage message) {
        if (message.getType().equals("move-direction")) {
            if (message.getValue().equals("STATIONARY")) {
                backwards.setDisable(false);
                forwards.setDisable(false);
                left.setDisable(false);
                right.setDisable(false);
            }

            if (message.getValue().equals("LEFT")) {
                backwards.setDisable(false);
                forwards.setDisable(false);
                left.setDisable(true);
                right.setDisable(false);
            }

            if (message.getValue().equals("RIGHT")) {
                backwards.setDisable(false);
                forwards.setDisable(false);
                left.setDisable(false);
                right.setDisable(true);
            }

            if (message.getValue().equals("FORWARDS")) {
                backwards.setDisable(false);
                forwards.setDisable(true);
                left.setDisable(false);
                right.setDisable(false);
            }

            if (message.getValue().equals("BACKWARDS")) {
                backwards.setDisable(true);
                forwards.setDisable(false);
                left.setDisable(false);
                right.setDisable(false);
            }
        }
    }

    @Override
    public void onBluetoothOpened(boolean isOpen) {

        if (isOpen) {
            this.stage.setScene(mainScene);
        } else {
            this.stage.setScene(connectingScene);
        }
    }

    @Override
    public void onLogMessage(LogMessage logMessage) {
        if (logMessage.getSource().equals("Robot")) {
            robotLogArea.appendText(logMessage.getMessage() + "\n");
        }

        if (logMessage.getSource().equals("Gui")) {
            guiLogArea.appendText(logMessage.getMessage() + "\n");
        }
    }
}
