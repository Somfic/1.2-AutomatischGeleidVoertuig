package Behaviour.Movement;

import Behaviour.Behaviour;
import Behaviour.Bluetooth.BluetoothListener;
import Behaviour.Distance.DistanceBehaviour;
import Behaviour.Remote.RemoteListener;
import Configuration.Config;
import Logger.Logger;
import Logic.*;
import TI.Timer;

import java.awt.*;
import java.util.ArrayList;

public class MovementBehaviour implements Behaviour, RemoteListener, BluetoothListener {

    private final Logger LOGGER = new Logger(this);

    private final MotorLogic MOTOR;
    private final DistanceBehaviour DISTANCE;
    private final LineFollowerLogic LINEFOLLOWER;
    private final BuzzerLogic BUZZER;
    private BluetoothLogic BLUETOOTH;
    private final ArrayList<Movement> MOVEMENT_QUEUE = new ArrayList<Movement>();
    private WhiskerLogic whiskers;
    private Timer timer;
    private boolean isExecutingMovement;

    private boolean isFollowingPath = false;
    private boolean isTurningOnCrossing = false;
    private boolean wantsToTurnLeft = true;

    private MoveDirection moveDirection = MoveDirection.STATIONARY;
    private float acceleration = 5;

    private Point position = new Point(0, 0);
    private LookDirection lookDirection = LookDirection.SOUTH;

    private Point[] path;
    private ArrayList<CrossDecision> crossDecisions = new ArrayList<CrossDecision>();
    private MoveDirection lastMoveDirection;
    private MoveDirection lastLastMoveDirection;

    public MovementBehaviour(MotorLogic motorLogic, DistanceBehaviour distance, LineFollowerLogic lineFollower, BuzzerLogic buzzerLogic, BluetoothLogic bluetooth) {
        this.DISTANCE = distance;
        this.MOTOR = motorLogic;
        this.LINEFOLLOWER = lineFollower;
        this.BUZZER = buzzerLogic;
        this.BLUETOOTH = bluetooth;
    }

    @Override
    public void initialise() {
        this.timer = new Timer(100);
    }

    @Override
    public void process() {
        if (timer.timeout()) {
            timer.mark();

            // Previous command finished executing, remove it from the commands list
            if (isExecutingMovement && this.MOVEMENT_QUEUE.size() > 0) {
                Movement movement = this.MOVEMENT_QUEUE.get(0);

                LOGGER.debug("Finished " + movement.name);

                this.MOVEMENT_QUEUE.remove(0);
                isExecutingMovement = false;
            }

            // If there are more commands to run, run those first
            if (this.MOVEMENT_QUEUE.size() > 0) {
                Movement movement = this.MOVEMENT_QUEUE.get(0);

                MOTOR.setAcceleration(movement.acceleration);
                MOTOR.setMove(movement.speed, movement.angle);

                timer.setInterval(movement.duration);
                timer.mark();

                isExecutingMovement = true;
                LOGGER.debug("Starting " + movement.name + " (" + movement.duration + "ms)");

                return;
            }

            // All queued commands are executed, find a new command
            timer.setInterval(100);

            /*
             * The code below is used for the whiskers
             */

            // if (this.whiskers.hasObstacleLeft() && this.whiskers.hasObstacleRight()) {
            // addMovementToQueue("Braking", 0, 0, 100, 500);
            // addMovementToQueue("Backing up", -0.5f, 0, 5, 1500);
            // addMovementToQueue("Stopping", 0, 0, 5,500);
            // return;
            // }
            //
            // if (this.whiskers.hasObstacleRight()) {
            // addMovementToQueue("Braking", 0, 0, 100,500);
            // addMovementToQueue("Backing up for left turn", -0.5f, 0, 5,3000);
            // addMovementToQueue("Turn left", 0, 0.5f, 10,1700);
            // addMovementToQueue("Stopping", 0, 0, 5,500);
            //
            // return;
            //
            // }
            //
            // if (this.whiskers.hasObstacleLeft()) {
            // addMovementToQueue("Braking", 0, 0, 100,500);
            // addMovementToQueue("Backing up for right turn", -0.5f, 0, 5,3000);
            // addMovementToQueue("Turn right", 0, -0.5f, 10,1700);
            // addMovementToQueue("Stopping", 0, 0, 5,500);
            // return;
            // }

            /*
             * The code below is used for the ultrasone sensors
             */

            if (isFollowingPath) {
                boolean left = this.LINEFOLLOWER.getStateLeft();
                boolean center = this.LINEFOLLOWER.getStateCenter();
                boolean right = this.LINEFOLLOWER.getStateRight();

                // Print the status of the linefollowers
                //this.LOGGER.info((left ? 1 : 0) + " " + (center ? 1 : 0) + " " + (right ? 1 : 0) + " ");

                if (left && center && right && this.moveDirection != MoveDirection.FORWARDS) {
                    // Everything is on the line, drive straight
                    this.moveDirection = MoveDirection.FORWARDS;
                    this.LOGGER.info("Everything ok, following line");
                }
                else if(!left && center && !right) {
                    this.LOGGER.info("Detected crossing");

                    this.moveDirection = MoveDirection.STATIONARY;

                    if(crossDecisions.size() == 0) {
                        this.LOGGER.info("No route!");
                        this.isFollowingPath = false;
                        this.isTurningOnCrossing = false;
                        return;
                    }

                    CrossDecision decision = crossDecisions.remove(0);
                    this.LOGGER.info("New decision: " + decision.toString());

                    if(decision == CrossDecision.Left) {
                        if(lookDirection == LookDirection.SOUTH) {
                            this.position.translate(1, 0);
                            this.lookDirection = LookDirection.EAST;
                        }

                        else if(lookDirection == LookDirection.NORTH) {
                            this.position.translate(-1, 0);
                            this.lookDirection = LookDirection.WEST;
                        }

                        else if(lookDirection == LookDirection.EAST) {
                            this.position.translate(0, -1);
                            this.lookDirection = LookDirection.NORTH;
                        }

                        else if(lookDirection == LookDirection.WEST) {
                            this.position.translate(0, 1);
                            this.lookDirection = LookDirection.SOUTH;
                        }

                        this.LOGGER.info("Turning left on crossing");
                        this.moveDirection = MoveDirection.LEFT;
                        isTurningOnCrossing = true;

                        broadcastPosition();

                        addMovementToQueue("Break", 0f, 0, 10, 200);
                        addMovementToQueue("Get into position", 0.8f, 0, 2, 1300);
                        addMovementToQueue("Break", 0f, 0, 10, 200);
                        addMovementToQueue("Turn left", 0, 1f, 10, 1500);
                    }

                    else if(decision == CrossDecision.Right) {
                        if(lookDirection == LookDirection.SOUTH) {
                            this.position.translate(-1, 0);
                            this.lookDirection = LookDirection.WEST;
                        }

                        else if(lookDirection == LookDirection.NORTH) {
                            this.position.translate(1, 0);
                            this.lookDirection = LookDirection.EAST;
                        }

                        else if(lookDirection == LookDirection.EAST) {
                            this.position.translate(0, 1);
                            this.lookDirection = LookDirection.SOUTH;
                        }

                        else if(lookDirection == LookDirection.WEST) {
                            this.position.translate(0, -1);
                            this.lookDirection = LookDirection.NORTH;
                        }

                        this.LOGGER.info("Turning right crossing");
                        isTurningOnCrossing = true;
                        this.moveDirection = MoveDirection.RIGHT;

                        broadcastPosition();

                        addMovementToQueue("Break", 0f, 0, 10, 200);
                        addMovementToQueue("Get into position", 0.8f, 0, 2, 1300);
                        addMovementToQueue("Break", 0f, 0, 10, 200);
                        addMovementToQueue("Turn right", 0, -1f, 10, 1500);
                    }

                    else if(decision == CrossDecision.Stop) {
                        this.LOGGER.info("Arrived at destination");
                        this.moveDirection = MoveDirection.STATIONARY;
                        this.isFollowingPath = false;
                        this.isTurningOnCrossing = false;

                        broadcastPosition();
                    }

                    else if(decision == CrossDecision.Turn) {
                        this.LOGGER.info("Turning at crossing");

                        isTurningOnCrossing = true;
                        this.moveDirection = MoveDirection.RIGHT;

                        broadcastPosition();

                        addMovementToQueue("Break", 0f, 0, 10, 200);
                        addMovementToQueue("Get into position", 0.8f, 0, 2, 1300);
                        addMovementToQueue("Break", 0f, 0, 10, 200);
                        addMovementToQueue("Turn", 0, -1f, 10, 3200);

                        if(lookDirection == LookDirection.SOUTH) {
                            this.position.translate(0, -1);
                            this.lookDirection = LookDirection.NORTH;
                        }

                        else if(lookDirection == LookDirection.NORTH) {
                            this.position.translate(0, 1);
                            this.lookDirection = LookDirection.SOUTH;
                        }

                        else if(lookDirection == LookDirection.EAST) {
                            this.position.translate(-1, 0);
                            this.lookDirection = LookDirection.WEST;
                        }

                        else if(lookDirection == LookDirection.WEST) {
                            this.position.translate(1, 0);
                            this.lookDirection = LookDirection.EAST;
                        }
                    }

                    else if(decision == CrossDecision.Skip) {
                        if(lookDirection == LookDirection.SOUTH) {
                            this.position.translate(0, 1);
                        }

                        else if(lookDirection == LookDirection.NORTH) {
                            this.position.translate(0, -1);
                        }

                        else if(lookDirection == LookDirection.EAST) {
                            this.position.translate(1, 0);
                        }

                        else if(lookDirection == LookDirection.WEST) {
                            this.position.translate(-1, 0);
                        }

                        this.LOGGER.info("Skipping crossing");
                        this.isTurningOnCrossing = false;

                        broadcastPosition();

                        this.moveDirection = MoveDirection.FORWARDS;
                        addMovementToQueue("Skip crossing", 1, 0, 5, 1000);
                    }
                }
                else if (!left && (center || right) && this.moveDirection != MoveDirection.RIGHT) {
                    // Left is off the line, turn right

                    this.acceleration = 20;
                    this.moveDirection = MoveDirection.LEFT;

                    this.LOGGER.info("Left is on the line, turning left");
                } else if ((left || center) && !right && this.moveDirection != MoveDirection.LEFT) {
                    // Right is off the line, turn left

                    this.acceleration = 20;
                    this.moveDirection = MoveDirection.RIGHT;

                    this.LOGGER.info("Right is on the line, turning right");
                } else if (!left && !center && !right && this.moveDirection != MoveDirection.STATIONARY) {
                    // Everything is off the line, stop!
                    //this.moveDirection = MoveDirection.STATIONARY;

                    this.LOGGER.info("Everything is off the line, stopping");
                } else {
                    // Something is off the line, but not all, so drive straight
                    //this.moveDirection = MoveDirection.FORWARDS;
                }
            }

//            if (this.DISTANCE.getDistance() < 20 &&
//                    this.DISTANCE.getPulse() > 0
//                    && this.moveDirection == MoveDirection.FORWARDS) {
//                // calculate braking speed depending on DISTANCE to obstacle
//                int brakingSpeed = (int) ((1500 - this.DISTANCE.getPulse()) / 50);
//
//                this.acceleration = brakingSpeed;
//                this.moveDirection = MoveDirection.STATIONARY;
//
//                //addMovementToQueue("Braking", 0, 0, brakingSpeed, 500);
//                return;
//            }
            // Determines the speed of the robot
            if (this.DISTANCE.getPulse() < 1000 && this.DISTANCE.getPulse() != -2){

                if (this.DISTANCE.getDistance() <= 0){
                    MOTOR.setMaxSafeSpeed(0);
                } else {
                    MOTOR.setMaxSafeSpeed((float)MOTOR.getMAX_SPEED() * (this.DISTANCE.getDistance() * 0.05f));
                }
            } else {
                MOTOR.setMaxSafeSpeed(MOTOR.getMAX_SPEED());
            }

            // Clamp acceleration between 1 and 30
            this.acceleration = Math.max(1, this.acceleration);
            this.acceleration = Math.min(30, this.acceleration);

            // Send the direction
            if(lastMoveDirection != moveDirection) {
                this.BLUETOOTH.send("move-direction", this.moveDirection.toString());
                lastMoveDirection = moveDirection;
            }


            // Set the motor speed and acceleration
            MOTOR.setAcceleration(this.acceleration);
            if (this.moveDirection == MoveDirection.FORWARDS) {
                MOTOR.setMove(1, 0);
            } else if (this.moveDirection == MoveDirection.BACKWARDS) {
                MOTOR.setMove(-1, 0);
            } else if (this.moveDirection == MoveDirection.LEFT) {
                MOTOR.setMove(0, 1f);
            } else if (this.moveDirection == MoveDirection.RIGHT) {
                MOTOR.setMove(0, -1f);
            } else if (this.moveDirection == MoveDirection.STATIONARY) {
                MOTOR.setMove(0, 0);
            }
        }
    }

    private void broadcastPosition() {
        this.LOGGER.info("Position: " + this.position.x + ", " + this.position.y);
        this.LOGGER.info("Move direction: " + this.moveDirection.toString());
        this.LOGGER.info("Look direction: " + this.lookDirection.toString());

        this.BLUETOOTH.send("position", this.position.x + "," + this.position.y);
        this.BLUETOOTH.send("direction", this.lookDirection.toString());
    }

    private void addMovementToQueue(String name, float targetSpeed, float targetAngle, int acceleration, int duration) {
        Movement movement = new Movement(name, targetSpeed, targetAngle, acceleration, duration);
        MOVEMENT_QUEUE.add(movement);
    }

    @Override
    public void reset() {
        MOVEMENT_QUEUE.clear();
        isExecutingMovement = false;

        this.moveDirection = MoveDirection.STATIONARY;
        this.acceleration = 5;
    }

    @Override
    public void onRemoteButtonPressed(int code) {

        if (code == -1)
            return;

        boolean wantsToGoToLineFollowerMode = code == Config.REMOTE_CD_ENTER;

        if (wantsToGoToLineFollowerMode && !isFollowingPath) {
            this.LOGGER.info("Switched to line follower mode");
            this.isFollowingPath = true;
            this.moveDirection = MoveDirection.FORWARDS;

            return;
        }

        if (isFollowingPath && !wantsToGoToLineFollowerMode) {
            this.LOGGER.info("Switched to manual mode (remote)");
            this.moveDirection = MoveDirection.STATIONARY;
            this.isFollowingPath = false;
            return;
        }

        if (!isFollowingPath) {
            if (code == Config.REMOTE_CHANNEL_PLUS) {
                // Move forwards
                // If we are moving backwards, stop
                if (this.moveDirection == MoveDirection.BACKWARDS) {
                    this.moveDirection = MoveDirection.STATIONARY;
                } else {
                    this.moveDirection = MoveDirection.FORWARDS;
                }

            } else if (code == Config.REMOTE_CHANNEL_MIN) {

                // Move backwards
                // If we are moving forwards, stop
                if (this.moveDirection == MoveDirection.FORWARDS) {
                    this.moveDirection = MoveDirection.STATIONARY;
                } else {
                    this.moveDirection = MoveDirection.BACKWARDS;
                }

            } else if (code == Config.REMOTE_VOLUME_PLUS) {

                // Move to the right
                // If we are moving to the left, stop
                if (this.moveDirection == MoveDirection.LEFT) {
                    this.moveDirection = MoveDirection.STATIONARY;
                } else {
                    this.moveDirection = MoveDirection.RIGHT;
                }

            } else if (code == Config.REMOTE_VOLUME_MIN) {

                // Move to the left
                // If we are moving to the right, stop
                if (this.moveDirection == MoveDirection.RIGHT) {
                    this.moveDirection = MoveDirection.STATIONARY;
                } else {
                    this.moveDirection = MoveDirection.LEFT;
                }
            }
        }

        if (code == Config.REMOTE_STOP) {
            this.moveDirection = MoveDirection.STATIONARY;
        } else if (code == Config.REMOTE_FORWARDS) {
            this.MOTOR.MAX_SPEED += 5;
        } else if (code == Config.REMOTE_BACKWARDS) {
            this.MOTOR.MAX_SPEED -= 5;

            if(this.MOTOR.MAX_SPEED < 20) {
                this.MOTOR.MAX_SPEED = 20;
            }
        }

        if (code == Config.REMOTE_RECORD_BUTTON){
            this.LINEFOLLOWER.calibrate();
            this.BUZZER.buzzOnce(200);
            this.LOGGER.debug("Calibrating line followers");
        }
    }

    @Override
    public void onBluetoothMessage(String input) {

        input = input.toLowerCase();

        if (input.equals("start-stop")) {
            if(this.moveDirection != MoveDirection.STATIONARY) {
                this.lastLastMoveDirection = this.moveDirection;
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = this.lastLastMoveDirection;
            }
        }

        if (input.equals("w") || input.equals("move:forwards")) {

            // Move forwards
            // If we are moving backwards, stop
            if (this.moveDirection == MoveDirection.BACKWARDS) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.FORWARDS;
            }

        } else if (input.equals("s") || input.equals("move:backwards")) {

            // Move backwards
            // If we are moving forwards, stop
            if (this.moveDirection == MoveDirection.FORWARDS) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.BACKWARDS;
            }

        } else if (input.equals("d") || input.equals("move:right")) {

            // Move to the right
            // If we are moving to the left, stop
            if (this.moveDirection == MoveDirection.LEFT) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.RIGHT;
            }

        } else if (input.equals("a") || input.equals("move:left")) {

            // Move to the left
            // If we are moving to the right, stop
            if (this.moveDirection == MoveDirection.RIGHT) {
                this.moveDirection = MoveDirection.STATIONARY;
            } else {
                this.moveDirection = MoveDirection.LEFT;
            }
        } else if(input.equals("mode:line-following")) {
            this.isFollowingPath = true;
            this.moveDirection = MoveDirection.FORWARDS;
        } else if(input.equals("mode:manual")) {
            this.moveDirection = MoveDirection.STATIONARY;
            this.isFollowingPath = false;
        } else if(input.equals("calibrate")) {
            this.LINEFOLLOWER.calibrate();
        } else if(input.startsWith("route")) {
            String rawPath = input.replace("route:", "");
            String[] points = rawPath.split(":");

            path = new Point[points.length];

            for (int i = 0; i < points.length; i++) {
                String point = points[i];

                int x = Integer.parseInt(point.split(",")[0]);
                int y = Integer.parseInt(point.split(",")[1]);

                path[i] = new Point(x, y);
            }

            LookDirection oldLookDirection = lookDirection;

            position = path[0];

            Point currentPoint = position;

            crossDecisions.clear();
            for (Point wayPoint : path) {
                crossDecisions.addAll(calculateRoute(currentPoint, wayPoint));
                currentPoint = wayPoint;
            }
            crossDecisions.add(CrossDecision.Stop);

            StringBuilder route = new StringBuilder();
            for (CrossDecision decision : crossDecisions) {
                route.append(decision).append(", ");
            }

            this.BLUETOOTH.send("Calculated route", route.toString());

            lookDirection = oldLookDirection;
        }
        else if (input.equals(" ") || input.equals("move:stop")) {
            this.moveDirection = MoveDirection.STATIONARY;
        } else if (input.equals("+")) {
            this.MOTOR.MAX_SPEED += 5;
        } else if (input.equals("-")) {
            this.MOTOR.MAX_SPEED -= 5;

            if(this.MOTOR.MAX_SPEED < 20) {
                this.MOTOR.MAX_SPEED = 20;
            }
        }
    }

    private ArrayList<CrossDecision> calculateRoute(Point from, Point to) {
        ArrayList<CrossDecision> decisions = new ArrayList<>();

        if(from.x > to.x) {
            if(lookDirection == LookDirection.SOUTH) {
                decisions.add(CrossDecision.Right);
            }

            if(lookDirection == LookDirection.NORTH) {
                decisions.add(CrossDecision.Left);
            }

            if(lookDirection == LookDirection.EAST) {
                decisions.add(CrossDecision.Turn);
            }

            if(lookDirection == LookDirection.WEST) {
                decisions.add(CrossDecision.Skip);
            }

            lookDirection = LookDirection.WEST;

            for (int i = 0; i < Math.abs(to.x - from.x) - 1; i++) {
                decisions.add(CrossDecision.Skip);
            }
        }

         if(from.x < to.x) {
            if(lookDirection == LookDirection.SOUTH) {
                decisions.add(CrossDecision.Left);
            }

            if(lookDirection == LookDirection.NORTH) {
                decisions.add(CrossDecision.Right);
            }

            if(lookDirection == LookDirection.EAST) {
                decisions.add(CrossDecision.Skip);
            }

            if(lookDirection == LookDirection.WEST) {
                decisions.add(CrossDecision.Turn);
            }

            lookDirection = LookDirection.EAST;

            for (int i = 0; i < Math.abs(to.x - from.x) - 1; i++) {
                decisions.add(CrossDecision.Skip);
            }
        }

        if(from.y > to.y) {
            if(lookDirection == LookDirection.SOUTH) {
                decisions.add(CrossDecision.Turn);
            }

            if(lookDirection == LookDirection.NORTH) {
                decisions.add(CrossDecision.Skip);
            }

            if(lookDirection == LookDirection.EAST) {
                decisions.add(CrossDecision.Left);
            }

            if(lookDirection == LookDirection.WEST) {
                decisions.add(CrossDecision.Right);
            }

            lookDirection = LookDirection.NORTH;

            for (int i = 0; i < Math.abs(to.y - from.y) - 1; i++) {
                decisions.add(CrossDecision.Skip);
            }
        }

        if(from.y < to.y) {
            if(lookDirection == LookDirection.SOUTH) {
                decisions.add(CrossDecision.Skip);
            }

            if(lookDirection == LookDirection.NORTH) {
                decisions.add(CrossDecision.Turn);
            }

            if(lookDirection == LookDirection.EAST) {
                decisions.add(CrossDecision.Right);
            }

            if(lookDirection == LookDirection.WEST) {
                decisions.add(CrossDecision.Left);
            }

            lookDirection = LookDirection.SOUTH;

            for (int i = 0; i < Math.abs(to.y - from.y) - 1; i++) {
                decisions.add(CrossDecision.Skip);
            }
        }

        return decisions;
    }
}
