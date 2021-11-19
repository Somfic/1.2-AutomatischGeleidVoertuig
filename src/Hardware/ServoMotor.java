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


    // starts the motor
    public void start() {
        this.servo.start();
    }

    // stops the motor
    public void stop() {
        this.servo.stop();
    }

    //accelerates or decelerates by 10 depending on the boolean given.
    public void accelerate(int milliseconds, boolean accelerate) {

        int speedIncrease = 1000 / milliseconds;

        if (accelerate){
            speedIncrease = speedIncrease * 1;
        } else {
            speedIncrease = speedIncrease * -1;
        }

        // check if the motor is in reverse and changes the speed.
        if (this.reverse == true) {
            if (this.currentSpeed < this.targetSpeed && this.currentSpeed > 1300) {
                this.servo.update(this.currentSpeed - speedIncrease);
                this.currentSpeed -= speedIncrease;
                if(this.currentSpeed < 1300){
                    this.currentSpeed = 1300;
                    this.servo.update(1300);
                }
            }
        } else {
            if (this.currentSpeed < this.targetSpeed && this.currentSpeed < 1700) {
                this.servo.update(this.currentSpeed + speedIncrease);
                this.currentSpeed += speedIncrease;
                if(this.currentSpeed > 1700){
                    this.currentSpeed = 1700;
                    this.servo.update(1700);
                }
            }
        }
    }

 }
