package org.firstinspires.ftc.teamcode.utils;

public class LegacyMovement {
    public Movement movement;
    public long time;

    public LegacyMovement(Movement movement, long miliseconds) {
        this.movement = movement;
        time = miliseconds;
    }

    public interface Movement {
        void whileMove();
    }
}
