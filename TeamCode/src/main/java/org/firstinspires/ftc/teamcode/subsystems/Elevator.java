package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.wpilibcontroller.ElevatorFeedforward;
import com.qualcomm.robotcore.hardware.DcMotor;

public class Elevator {
    public static final int MAX_ARM_HEIGHT = 4000;
    final int MIN_ARM_HEIGHT = 0;

    final DcMotor arm;
    final double ARM_SPEED = 0.7;
    int position = 0;
    int multiplier = 18;

    public int getPosition() {
        return position;
    }

    public Elevator(DcMotor arm) {
        this.arm = arm;
        arm.setDirection(DcMotor.Direction.REVERSE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void runToPosition(int position) {
        runToPosition(position, false);
    }

    public void runToPosition(int position, boolean ignoreMinMax) {
        if(!ignoreMinMax) {
            if(isPastHeight(position)) {
                return;
            }
        }
        this.position = position;
        runToPosition();
    }

    boolean isPastHeight(int position) {
        return position < MIN_ARM_HEIGHT || position > MAX_ARM_HEIGHT;
    }

    void runToPosition() {
        if(position == arm.getCurrentPosition()) {
            arm.setPower(0);
            return;
        }
        arm.setPower(ARM_SPEED);
        arm.setTargetPosition(this.position);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void addPosition(int addition) {
        if(isPastHeight(position + addition * multiplier)) {
            return;
        }
        position += addition * multiplier;
        runToPosition();
    }

    public void resetZero() {

    }


    //Utility Methods


    public void floor() {
        runToPosition(MIN_ARM_HEIGHT);
    }

    public void ceiling() {
        runToPosition(MAX_ARM_HEIGHT);
    }

    public void runToPercentage(double percentage) {
        if(percentage < 0 || percentage > 1) {
            throw new IllegalArgumentException("percentage was not between 0 and 1");
        }
        runToPosition((int) Math.round(MAX_ARM_HEIGHT * percentage));
    }
}
