package Hardware;

import TI.Servo;

public class ServoMotor {

    private int pin;
    private int currentSpeed;
    private int targetSpeed;
    private boolean reverse;
    private Servo servo;

    private int defaultSpeed = 1500;
    private int maxSpeed = 1700;
    private int minSpeed = 1300;

    private int timeInterval = 10;

    public ServoMotor(int pin, boolean reverse) {
        this.pin = pin;
        this.currentSpeed = defaultSpeed;
        this.targetSpeed = defaultSpeed;
        this.reverse = reverse;
        this.servo = new Servo(pin);
    }

    public void setTargetSpeed(int targetSpeed){
        if(targetSpeed > maxSpeed){
            this.targetSpeed = maxSpeed;
        }
        else if(this.targetSpeed < minSpeed){
            this.targetSpeed = minSpeed;
        }
        else {
            this.targetSpeed = targetSpeed;
        }
    }

    public boolean targetSpeedReached(){
        return this.targetSpeed == this.currentSpeed;
    }


    // starts the motor
    public void start() {
        this.servo.start();
    }

    // stops the motor
    public void stop() {
        this.servo.stop();
    }

    public void setTimeInterval(int time) {
        this.timeInterval = time;
    }

    //accelerates or decelerates by 10 depending on the boolean given.
    public void accelerate() {
        int speedIncrease = this.timeInterval;

        if(this.targetSpeed < this.currentSpeed){
            speedIncrease = -this.timeInterval;
        }


        // check if the motor is in reverse and changes the speed.
        if (this.currentSpeed + speedIncrease <= maxSpeed
                && this.currentSpeed + speedIncrease >= minSpeed
                && this.targetSpeed != this.currentSpeed) {

            this.servo.update(this.currentSpeed + speedIncrease);
            this.currentSpeed += speedIncrease;

        }
    }
 }
