import Logger.Logger;
import Logic.BluetoothListener;
import Logic.BluetoothLogic;
import Logic.BluetoothMessage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import Logger.LoggerListener;
import Logger.LogMessage;

public class RobotGui extends Application implements BluetoothListener, LoggerListener {

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

    private Logger logger = new Logger(this);

    private TextArea robotLogArea;
    private TextArea guiLogArea;

    private Button forwards;
    private Button backwards;
    private Button left;
    private Button right;

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

    public Pane buildLineFollowerPane() {
        BorderPane pane = new BorderPane();
        pane.setCenter(new Label("Empty ..."));

        pane.setPadding(new Insets(10, 10, 10, 10));

        return pane;
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
            if (startStopButton.getText().equals("Start")){
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

            if(isLineFollowingMode) {
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
        if(message.getType().equals("move-direction")) {
            if(message.getValue().equals("STATIONARY")) {
                backwards.setDisable(false);
                forwards.setDisable(false);
                left.setDisable(false);
                right.setDisable(false);
            }

            if(message.getValue().equals("LEFT")) {
                backwards.setDisable(false);
                forwards.setDisable(false);
                left.setDisable(true);
                right.setDisable(false);
            }

            if(message.getValue().equals("RIGHT")) {
                backwards.setDisable(false);
                forwards.setDisable(false);
                left.setDisable(false);
                right.setDisable(true);
            }

            if(message.getValue().equals("FORWARDS")) {
                backwards.setDisable(false);
                forwards.setDisable(true);
                left.setDisable(false);
                right.setDisable(false);
            }

            if(message.getValue().equals("BACKWARDS")) {
                backwards.setDisable(true);
                forwards.setDisable(false);
                left.setDisable(false);
                right.setDisable(false);
            }
        }
    }

    @Override
    public void onBluetoothOpened(boolean isOpen) {

        if(isOpen) {
            this.stage.setScene(mainScene);
        } else {
            this.stage.setScene(connectingScene);
        }
    }

    @Override
    public void onLogMessage(LogMessage logMessage) {
        if(logMessage.getSource().equals("Robot")) {
            robotLogArea.appendText(logMessage.getMessage() + "\n");
        }

        if(logMessage.getSource().equals("Gui")) {
            guiLogArea.appendText(logMessage.getMessage() + "\n");
        }
    }
}
