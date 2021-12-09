package Hardware;

import TI.Servo;

public class ServoMotor {

    private final Servo servo;

    public ServoMotor(int pin) {
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

    public void set(int pulse) {
        this.servo.update(pulse);
    }
}
