package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Drivemod {
    static boolean firstR = false;
    static boolean firstL = false;

    static boolean turbo = false;
    static boolean detail = false;

    static float detailspeed = 0.2f;
    static float turbospeed = 1f;


    public static float getDriveMod(Gamepad gamepad, AdditiveLogger logger) {
        //Bumpers will set the mod to 0.1 and 1 ️
        //triggers will set the mod from .1 - .75 (dist 0.65)

        if(gamepad.left_bumper) {
            if(firstL) {
                firstL = false;
                detail = !detail;
                turbo = false;
                logger.Log("Detail Toggled");
            }
            return detailspeed;
        }
        firstL = true;
        if(gamepad.right_bumper) {
            if(firstR) {
                firstR = false;
                turbo = !turbo;
                detail = false;
                logger.Log("Turbo Toggled");
            }
            return turbospeed;
        }
        firstR = true;
        if(turbo)
            return turbospeed;
        if(detail)
            return detailspeed;
        return 0.5f;
    }
}
