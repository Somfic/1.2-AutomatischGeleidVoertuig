import Hardware.LineFollower;
import Logic.LedLight;
import Logic.LedLogic;
import Logic.LineFollowerLogic;
import TI.BoeBot;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        Robot robot = new Robot();
        robot.run();
    }

//        LineFollowerLogic logic = new LineFollowerLogic(2, 0, 1);
//        LedLogic leds = new LedLogic();
//
//        logic.calibrate();
//
//
//        while(true) {
//            BoeBot.wait(0);
//
//            if(logic.getStateCenter()) {
//                leds.set(LedLight.FRONT_MIDDLE, Color.green);
//            } else {
//                leds.set(LedLight.FRONT_MIDDLE, Color.red);
//            }
//
//            if(logic.getStateLeft()) {
//                leds.set(LedLight.FRONT_LEFT, Color.green);
//            } else {
//                leds.set(LedLight.FRONT_LEFT, Color.red);
//            }
//
//            if(logic.getStateRight()) {
//                leds.set(LedLight.FRONT_RIGHT, Color.green);
//            } else {
//                leds.set(LedLight.FRONT_RIGHT, Color.red);
//            }
//
//            logic.process();
//            leds.process();
//        }
//    }
}
