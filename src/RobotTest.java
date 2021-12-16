import Hardware.*;
import Logic.*;
import TI.BoeBot;


public class RobotTest {
    private Switch button1 = new Switch(0);
    private Switch button2 = new Switch(1);
    private GripperLogic gripperLogic = new GripperLogic(14);

    public RobotTest(){
    }

    public void run(){
        while (true){
            if (button1.getState()){
                System.out.println("opening");
                gripperLogic.open();
            }
            if (button2.getState()){
                System.out.println("closing");
                gripperLogic.close();
            }

            gripperLogic.process();

            BoeBot.wait(1);
        }
    }
}
