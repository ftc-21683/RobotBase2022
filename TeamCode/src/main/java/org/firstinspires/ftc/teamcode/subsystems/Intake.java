package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    final Servo intake;

    double closed = 0;
    public static final double open = 0.0692;
    boolean check = true;

    public Intake(Servo intake) {
        this.intake = intake;
        intake.setDirection(Servo.Direction.REVERSE);
    }

    public double getPosition() {
        return intake.getPosition();
    }

    public boolean isOpen() {
        return intake.getPosition() - open < 0.05;
    }

    public void toggle() {
        System.out.println("toggled");
        if(isOpen()) {
            close();
            return;
        }
        open();
    }

    public void open() {
        intake.setPosition(open);
    }

    public void close() {
        intake.setPosition(closed);
    }
}
