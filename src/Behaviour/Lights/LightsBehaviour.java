package Behaviour.Lights;

import Behaviour.Behaviour;
import Configuration.Config;
import Logger.Logger;
import Logic.LedLight;
import Logic.LedLogic;
import Logic.MotorLogic;
import TI.Timer;

import java.awt.*;

public class LightsBehaviour implements Behaviour {

    private final Logger LOGGER = new Logger(this);

    private final LedLogic LIGHTS;
    private final MotorLogic MOTORS;
    private final Timer TIMER = new Timer(300);
    private boolean lightsAreOn;
    private float oldSpeed = 0;
    private float oldAngle = 0;

    public LightsBehaviour(LedLogic lights, MotorLogic motors) {

        this.LIGHTS = lights;
        this.MOTORS = motors;
    }

    @Override
    public void initialise() {

    }

    @Override
    public void process() {
        float speed = this.MOTORS.getTargetSpeed();
        float angle = this.MOTORS.getTargetAngle();

        if (TIMER.timeout() || speed != oldSpeed || angle != oldAngle) {
            TIMER.mark();

            this.oldSpeed = speed;
            this.oldAngle = angle;

            // Toggle whether the LIGHTS are on
            this.lightsAreOn = !this.lightsAreOn;

            Color frontColor = Color.white;
            Color backColor = new Color(64, 0, 0);

            Color reverseColor = Color.white;
            Color brakeColor = Color.red;

            Color blinkerColor = Config.LED_BLINKER_COLOR_OFF;

            if (lightsAreOn) {
                blinkerColor = Config.LED_BLINKER_COLOR_ON;
            }

            // Default front LIGHTS
            this.LIGHTS.set(LedLight.FRONT_RIGHT, frontColor);
            this.LIGHTS.set(LedLight.FRONT_LEFT, frontColor);
            this.LIGHTS.clear(LedLight.FRONT_MIDDLE);

            // Default back LIGHTS
            this.LIGHTS.set(LedLight.BACK_LEFT, backColor);
            this.LIGHTS.set(LedLight.BACK_RIGHT, backColor);
            this.LIGHTS.clear(LedLight.BACK_MIDDLE);


            // Standing still
            if (speed == 0 && angle == 0 && MOTORS.isAccelerating()) {
                // Brakes
                this.LIGHTS.set(LedLight.BACK_LEFT, brakeColor);
                this.LIGHTS.set(LedLight.BACK_MIDDLE, brakeColor);
                this.LIGHTS.set(LedLight.BACK_RIGHT, brakeColor);
            }

            // Reversing
            if (speed < 0) {
                // Reverse light
                this.LIGHTS.set(LedLight.BACK_RIGHT, reverseColor);
            }

            // Going left
            if (angle > 0) {
                // Blink left
                this.LIGHTS.set(LedLight.FRONT_LEFT, blinkerColor);
                this.LIGHTS.set(LedLight.BACK_LEFT, blinkerColor);
            }

            // Going right
            else if (angle < 0) {
                // Blink right
                this.LIGHTS.set(LedLight.FRONT_RIGHT, blinkerColor);
                this.LIGHTS.set(LedLight.BACK_RIGHT, blinkerColor);
            }
        }
    }

    @Override
    public void reset() {
        lightsAreOn = false;
    }
}
