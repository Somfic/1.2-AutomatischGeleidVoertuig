import Logic.BluetoothListener;
import Logic.BluetoothLogic;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class RobotGui extends Application implements BluetoothListener {

    private boolean isLineFollowingMode = true;
    private BluetoothLogic BLUETOOTH = new BluetoothLogic(this);

    private BorderPane mainPane;
    private Pane lineFollowerPane;
    private Pane manualPane;

    private Stage stage;

    private Button startStopButton;
    private Scene connectingScene;
    private Scene mainScene;


    public void run() {
        launch(RobotGui.class);
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        // Build the scenes
        mainScene = buildMainScene();
        connectingScene = buildConnectingScene();

        // Show the scene
        stage.setTitle("BoeBot");

        stage.setScene(connectingScene);

        //stage.setMaximized(true);
        stage.show();
    }

    public Scene buildMainScene() {
        Pane taskBar = buildTaskbar();

        lineFollowerPane = buildLineFollowerPane();
        manualPane = buildManualPane();

        // Build the main scene
        mainPane = new BorderPane();
        mainPane.setTop(taskBar);

        // Set the main component
        setMainPane();

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
        // naar voren
        // naar achteren
        // naar links
        // naar rechts
        Button forwards = new Button("\uD83E\uDC81");
        forwards.setOnAction(e -> {
            BLUETOOTH.send("move", "forwards");
        });

        Button left = new Button("\uD83E\uDC80");
        left.setOnAction(e -> {
            BLUETOOTH.send("move", "left");
        });

        Button right = new Button("\uD83E\uDC82");
        right.setOnAction(e -> {
            BLUETOOTH.send("move", "right");
        });

        Button backwards = new Button("\uD83E\uDC83");
        backwards.setOnAction(e -> {
            BLUETOOTH.send("move", "backwards");
        });

        HBox hbox = new HBox(left, new VBox(forwards, backwards), right);
        hbox.setAlignment(Pos.BOTTOM_CENTER);

        // logs
        VBox logger = new VBox();
        Label loggerName = new Label("LOGGER");
        TextArea loggerMessage = new TextArea();
        loggerMessage.setText("Hello world!");
        logger.getChildren().addAll(loggerName, loggerMessage);

        BorderPane pane = new BorderPane();
        pane.setBottom(hbox);
        pane.setTop(logger);

        return pane;
    }

    public Pane buildManualPane() {
        BorderPane pane = new BorderPane();

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

        VBox acceleration = new VBox();
        acceleration.getChildren().addAll(accelerationPlus, accelerationMin);
        pane.setBottom(acceleration);
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

            setMainPane();
        });

        return new HBox(startStopButton, changeMode);
    }

    public void setMainPane() {
        if(isLineFollowingMode) {
            mainPane.setCenter(lineFollowerPane);
        } else {
            mainPane.setCenter(manualPane);
        }
    }

    @Override
    public void onBluetoothOpenend() {
        stage.setScene(mainScene);
    }
}
