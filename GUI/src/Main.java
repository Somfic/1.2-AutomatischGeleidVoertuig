import Logger.LogMessage;
import Logger.Logger;
import Logger.LoggerListener;
import Logic.BluetoothListener;
import Logic.BluetoothLogic;
import Logic.BluetoothMessage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Application implements BluetoothListener, LoggerListener {

    private boolean isLineFollowingMode = true;
    private final BluetoothLogic BLUETOOTH = new BluetoothLogic(this);

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

    private Point startPoint = new Point(0, 0);
    private final ArrayList<Point> waypoints = new ArrayList<>();
    private Point endPoint = new Point(0, 0);

    private TextArea waypointsArea;
    private Label position;
    private Label direction;
    private String lookDirection = "";

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
        VBox pane = new VBox();

        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(10);

        javafx.scene.text.Font font = new javafx.scene.text.Font("Comic Sans MS", 20);

        Text text = new Text("BoeBot");
        text.setFont(font);

        pane.getChildren().add(text);

        ComboBox portInput = new ComboBox();
        portInput.getItems().addAll("COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "COM10");

        portInput.setPlaceholder(new Text("Select a port"));

        Button connectButton = new Button("Connect");
        connectButton.setDisable(true);
        connectButton.setOnAction(e -> {
            BLUETOOTH.open();
        });

        portInput.setOnAction(event -> {
            BLUETOOTH.setPort(portInput.getValue().toString());
            connectButton.setDisable(false);
        });


        pane.getChildren().add(portInput);
        pane.getChildren().add(connectButton);

        return new Scene(pane);
    }

    public String mode = "";

    public static ArrayList<ArrayList<Button>> buttons = new ArrayList<ArrayList<Button>>();

    public Pane buildLineFollowerPane() {
        BorderPane pane = new BorderPane();

        GridPane grid = new GridPane();
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

            StringBuilder pathString = new StringBuilder();
            for (Point p : path) {
                pathString.append(p.x).append(",").append(p.y).append(":");
            }

            this.BLUETOOTH.send("route", pathString.toString());
        });

        int width = 5;
        int height = 7;

        for (int x = 0; x < height; x++) {
            Main.buttons.add(new ArrayList<>());

            for (int y = 0; y < width; y++) {
                Button button = new Button();
                button.setPrefSize(50, 50);

                Image image = new Image(getClass().getResourceAsStream("crossing.png"), 50, 50, true, false);
                BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));

                Background backGround = new Background(bImage);
                button.setBackground(backGround);

                //button.setGraphic(image);

                int finalY = y;
                int finalX = x;
                button.setOnAction(e -> {
                    LOGGER.info("Pressed: " + finalX + "," + finalY);

                    processGridPress(new Point(finalX, finalY));
                });
                grid.add(button, x, y);

                Main.buttons.get(x).add(button);
            }
        }

        pane.setRight(buttons);
        pane.setCenter(grid);

        HBox debug = new HBox();


        Button calibrate = new Button("Calibrate");
        calibrate.setOnAction(e -> {
            BLUETOOTH.send("calibrate");
        });

        position = new Label();
        direction = new Label();

        debug.getChildren().add(calibrate);
        debug.getChildren().add(position);
        debug.getChildren().add(direction);

        pane.setBottom(debug);

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

                // Reset button
                changeImage(button, "crossing.png");
            }
        }

        waypointsArea.clear();
        for (Point waypoint : waypoints) {
            waypointsArea.appendText(waypoint.x + ", " + waypoint.y + "\n");
        }

        int startX = startPoint.x;
        int startY = startPoint.y;

        ArrayList<Point> path = new ArrayList<>();
        path.addAll(waypoints);
        path.add(endPoint);

        for (Point waypoint : path) {
            this.LOGGER.info("Calculating path from " + startX + "," + startY + " to " + waypoint.x + "," + waypoint.y);

            while (startX != waypoint.x || startY != waypoint.y) {
                if (startX < waypoint.x) {
                    startX++;
                } else if (startX > waypoint.x) {
                    startX--;
                } else {
                    if (startY < waypoint.y) {
                        startY++;
                    } else {
                        startY--;
                    }
                }

                Button button = buttons.get(startX).get(startY);
                changeImage(button, "crossing-route.png");
            }
        }

        for (int x = 0; x < buttons.size(); x++) {
            for (int y = 0; y < buttons.get(0).size(); y++) {
                Button button = buttons.get(x).get(y);

                if(waypoints.contains(new Point(x, y))) {
                    changeImage(button, "waypoint.png");
                }

                if (x == startPoint.x && y == startPoint.y) {
                    // Start button
                    if(lookDirection.equals("NORTH")) {
                        changeImage(button, "bot-north.png");
                    } else if(lookDirection.equals("SOUTH")) {
                        changeImage(button, "bot-south.png");
                    } else if(lookDirection.equals("EAST")) {
                        changeImage(button, "bot-east.png");
                    } else if(lookDirection.equals("WEST")) {
                        changeImage(button, "bot-west.png");
                    } else {
                        changeImage(button, "start.png");
                    }
                }

                if (x == endPoint.x && y == endPoint.y) {
                    // End button
                    changeImage(button, "end.png");
                }
            }
        }
    }

    public Pane buildManualPane() {
        BorderPane pane = new BorderPane();

        VBox acceleration = new VBox();

        // acceleratie omhoog
        Button accelerationPlus = new Button("Speed +");
        accelerationPlus.setOnAction(event -> {
            BLUETOOTH.send("acceleration", "increase");
        });

        // acceleratie omlaag
        Button accelerationMin = new Button("Speed -");
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
        startStopButton = new Button("Start/Stop");
        startStopButton.setOnAction(e -> {
            BLUETOOTH.send("start-stop");
        });

        // change mode
        Button changeMode = new Button("Verander modus");
        changeMode.setOnAction(e -> {
            isLineFollowingMode = !isLineFollowingMode;

            if (isLineFollowingMode) {
                BLUETOOTH.send("mode", "line-following");
                changeMode.setText("Line Following");
            } else {
                BLUETOOTH.send("mode", "manual");
                changeMode.setText("Manual");
            }

            //todo: change to correct tab
        });

        return new HBox(startStopButton, changeMode);
    }

    @Override
    public void onBluetoothMessage(BluetoothMessage message) {

        if (message.getType().equals("position")) {
            String[] values = message.getValue().split(",");
            Point position = new Point(Integer.parseInt(values[0]), Integer.parseInt(values[1]));

            startPoint = position;

            if(waypoints.size() > 0) {
                if (waypoints.get(0).x == position.x && waypoints.get(0).y == position.y) {
                    this.LOGGER.info("Removing waypoint");
                    waypoints.remove(0);
                }
            }

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateGrid();
                }
            });
        }

        if(message.getType().equals("direction")) {
            this.lookDirection = message.getValue();
        }

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

    private void changeImage(Button button, String path) {
        Image image = new Image(getClass().getResourceAsStream(path), 50, 50, true, false);
        BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(button.getWidth(), button.getHeight(), true, true, true, false));

        Background backGround = new Background(bImage);
        button.setBackground(backGround);
    }
}
