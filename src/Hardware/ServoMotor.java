package Hardware;

import TI.Servo;

public class ServoMotor {

    private int pin;
    private int currentSpeed;
    private int targetSpeed;
    private boolean reverse;
    private Servo servo;

    public ServoMotor(int pin, boolean reverse) {
        this.pin = pin;
        this.currentSpeed = 1500;
        this.targetSpeed = 1500;
        this.reverse = reverse;
        this.servo = new Servo(pin);
    }

    public void setTargetSpeed(int targetSpeed){
        if(targetSpeed > 1700){
            this.targetSpeed = 1700;
        }
        else if(this.targetSpeed < 1300){
            this.targetSpeed = 1300;
        }
        else {
            this.targetSpeed = targetSpeed;
        }
    }


    // starts the motor
    public void start() {
        this.servo.start();
    }

    // stops the motor
    public void stop() {
        this.servo.stop();
    }

    //accelerates or decelerates by 10 depending on the boolean given.
    public void accelerate() {
        int speedIncrease = 10;

        if(this.targetSpeed < this.currentSpeed){
            speedIncrease = -10;
        }


        // check if the motor is in reverse and changes the speed.
        if (this.currentSpeed + speedIncrease <= 1700 && this.currentSpeed + speedIncrease >= 1300 && this.targetSpeed != this.currentSpeed) {
            this.servo.update(this.currentSpeed + speedIncrease);
            this.currentSpeed += speedIncrease;

        }
    }

    public boolean targetSpeedReached(){
        if(this.targetSpeed == this.currentSpeed){
            return true;
        }
        else{
            return false;
        }
    }

 }
