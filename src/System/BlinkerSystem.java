package System;

import Logic.LedLight;
import Logic.LedLogic;
import Logic.MotorLogic;
import TI.Timer;

import java.awt.*;

public class BlinkerSystem implements SystemInterface {

    private final LedLogic lights;
    private final MotorLogic motors;

    private boolean lightsAreOn;

    private float oldSpeed = 0;
    private float oldAngle = 0;

    private Timer timer = new Timer(300);

    public BlinkerSystem(LedLogic lights, MotorLogic motors) {

        this.lights = lights;
        this.motors = motors;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        float speed =  this.motors.getTargetSpeed();
        float angle = this.motors.getTargetAngle();

        if(timer.timeout() || speed != oldSpeed || angle != oldAngle) {
            timer.mark();

            this.oldSpeed = speed;
            this.oldAngle = angle;

            // Toggle whether the lights are on
            this.lightsAreOn = !this.lightsAreOn;


            Color frontColor = Color.white;
            Color backColor = new Color(64, 0, 0);

            Color reverseColor = Color.white;
            Color brakeColor = Color.red;

            Color blinkerColor = lightsAreOn ? Color.orange : new Color(64,42,0);

            // Reset everything
            this.lights.reset();

            // Default front lights
            this.lights.set(LedLight.FrontRight, frontColor);
            this.lights.set(LedLight.FrontLeft, frontColor);
            this.lights.set(LedLight.FrontMiddle, Color.black);

            // Default back lights
            this.lights.set(LedLight.BackLeft, backColor);
            this.lights.set(LedLight.BackRight, backColor);
            this.lights.set(LedLight.BackMiddle, Color.black);


            // Standing still
            if(speed == 0 && angle == 0) {
                // Brakes
                this.lights.set(LedLight.BackLeft, brakeColor);
                this.lights.set(LedLight.BackMiddle, brakeColor);
                this.lights.set(LedLight.BackRight, brakeColor);
            }

            // Reversing
            if(speed < 0) {
                // Reverse light
                this.lights.set(LedLight.BackRight, reverseColor);
            }

            // Going left
            if(angle > 0) {
                // Blink left
                this.lights.set(LedLight.FrontLeft, blinkerColor);
                this.lights.set(LedLight.BackLeft, blinkerColor);
            }

            // Going right
            else if(angle < 0) {
                // Blink right
                this.lights.set(LedLight.FrontRight, blinkerColor);
                this.lights.set(LedLight.BackRight, blinkerColor);
            }
        }
    }

    @Override
    public void reset() {
       lightsAreOn = false;
    }
}
