import Hardware.*;
import Logic.*;
import TI.BoeBot;


public class RobotTest {
    private Switch button1 = new Switch(0);
    private Switch button2 = new Switch(1);
    private GripperLogic gripperLogic = new GripperLogic(4);

    public RobotTest(){
    }

    public void run(){
        while (true){
            if (button1.getState()){
                gripperLogic.open();
            }
            if (button2.getState()){
                gripperLogic.close();
            }

            BoeBot.wait(1);
        }
    }
}
