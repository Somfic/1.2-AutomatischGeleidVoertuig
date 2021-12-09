package Behaviour.Movement;

import Behaviour.Behaviour;

public class Movement {
    float angle;
    float speed;
    int duration;
    int acceleration;

    String name;

    public Movement(String name, float speed, float angle, int acceleration, int duration) {
        this.name = name;
        this.angle = angle;
        this.speed = speed;
        this.acceleration = acceleration;
        this.duration = duration;
    }
}
