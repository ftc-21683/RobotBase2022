package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ToggleModifier {
    boolean firstR = false;
    boolean firstL = false;

    boolean turbo = false;
    boolean detail = false;

    public ToggleModifier(float detailSpeed, float turboSpeed) {
        this.detailSpeed = detailSpeed;
        this.turboSpeed = turboSpeed;
    }

    public ToggleModifier(float detailSpeed, float turboSpeed, float defaultSpeed) {
        this(detailSpeed, turboSpeed);
        this.defaultSpeed = defaultSpeed;
    }

    public float detailSpeed = 0.2f;
    public float turboSpeed = 1f;
    public float defaultSpeed = 0.5f;


    public float getModifier(Gamepad gamepad, AdditiveLogger logger) {
        //Bumpers will set the mod to 0.1 and 1 ️
        //triggers will set the mod from .1 - .75 (dist 0.65)

        if(gamepad.left_bumper) {
            if(firstL) {
                firstL = false;
                detail = !detail;
                turbo = false;
                logger.Log((gamepad.id == 1004 ? "C1" : "C2") + ": Detail Toggled");
            }
            return detailSpeed;
        }
        firstL = true;
        if(gamepad.right_bumper) {
            if(firstR) {
                firstR = false;
                turbo = !turbo;
                detail = false;
                logger.Log((gamepad.id == 1004 ? "C1" : "C2") + ": Turbo Toggled");
            }
            return turboSpeed;
        }
        firstR = true;
        if(turbo)
            return turboSpeed;
        if(detail)
            return detailSpeed;
        return defaultSpeed;
    }
}
