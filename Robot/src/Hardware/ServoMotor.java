package Hardware;

import TI.Servo;

public class ServoMotor {

    private final Servo SERVO;

    public ServoMotor(int pin) {
        this.SERVO = new Servo(pin);
    }

    // starts the motor
    public void start() {
        this.SERVO.start();
    }

    // stops the motor
    public void stop() {
        this.SERVO.stop();
    }

    public void set(int pulse) {
        this.SERVO.update(pulse);
    }
}
