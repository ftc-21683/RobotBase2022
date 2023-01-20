package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.opmodes.Auton.AutonMode;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.utils.movement.Movement;

import java.util.ArrayList;

public class MovementSequencer {
    ArrayList<Movement> movements = new ArrayList<>();
    public AdditiveLogger logger;
    public Drivebase drivebase;
    int progress = 0;


    public void AddMovement(Movement action) {
        this.movements.add(action);
    }

    public void tick(AutonMode opMode) {
        Movement movement = movements.get(progress);
        if(movement.pid.getPositionError() < 20) {
            if(((AutonMode) opMode).requestedStop()) {
                return;
            }
            //movement.pid.calculate(opMode.getSubSystems().getDrivebase());
        }
        ((OpMode) opMode).requestOpModeStop();
    }




}
