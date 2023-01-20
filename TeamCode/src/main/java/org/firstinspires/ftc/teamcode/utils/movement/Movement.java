package org.firstinspires.ftc.teamcode.utils.movement;

import com.arcrobotics.ftclib.controller.PIDController;

public class Movement {
    int x;
    int y;
    boolean relative;
    String log;
    public PIDController pid;

    public Movement(int x, int y, boolean relative, PIDController pid, String log) {
        this.x = x;
        this.y = y;
        this.relative = relative;
        this.pid = pid;
        this.log = log;
    }
}
