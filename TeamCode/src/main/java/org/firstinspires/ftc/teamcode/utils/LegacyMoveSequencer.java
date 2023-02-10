package org.firstinspires.ftc.teamcode.utils;

import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.opmodes.Auton.AutonMode;
import org.firstinspires.ftc.teamcode.utils.movement.Movement;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class LegacyMoveSequencer {
    ArrayList<LegacyMovement> movements = new ArrayList<>();
    int progress = 0;
    Timing.Timer timer = null;

    public void addMovement(LegacyMovement movement) {
        movements.add(movement);
    }

    public void tick(AutonMode opMode) {
        LegacyMovement movement = movements.get(progress);
        if(timer == null) {
            timer = new Timing.Timer(movement.time, TimeUnit.MILLISECONDS);
            timer.start();
        }
        if(!timer.done()) {
            if(((AutonMode) opMode).requestedStop()) {
                return;
            }
            movement.movement.whileMove();
            return;
        }
        progress++;
        if(progress == movements.size() - 1) {
            ((OpMode) opMode).requestOpModeStop();
        }
        timer = null;
    }
}
