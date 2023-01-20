package org.firstinspires.ftc.teamcode.utils.movement;


import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.teamcode.utils.MovementSequencer;

public class MovementBuilder {
    int x;
    int y;
    boolean relative;
    String log;
    PIDController pid;

    public MovementBuilder(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void relative(boolean val) {
        relative = val;
    }

    void log(String log) {
        this.log = log;
    }

    void pid(PIDController pid) {
        this.pid = pid;
    }

    Movement build() {
        return new Movement(x, y, relative, pid, log);
    }
}
