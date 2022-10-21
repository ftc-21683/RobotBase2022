package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

public class ControllerInterface {
    Gamepad gamepad;
    // -- Face Buttons
    boolean onPressA = false;
    boolean onPressB = false;
    boolean onPressX = false;
    boolean onPressY = false;
    // -- L&R Bumpers
    boolean onPressLB = false;
    boolean onPressRB = false;


    public ControllerInterface(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void tick() {
        if(gamepad.a) {
            if(onPressA) {onPressA = false;}
        }else{
            onPressA = true;
        }

        if(gamepad.b) {
            if(onPressB) {onPressB = false;}
        }else{
            onPressB = true;
        }

        if(gamepad.x) {
            if(onPressX) {onPressX = false;}
        }else{
            onPressX = true;
        }

        if(gamepad.y) {
            if(onPressY) {onPressY = false;}
        }else{
            onPressY = true;
        }



        if(gamepad.right_bumper) {
            if(onPressRB) {onPressRB = false;}
        }else{
            onPressRB = true;
        }

        if(gamepad.left_bumper) {
            if(onPressLB) {onPressLB = false;}
        }else{
            onPressLB = true;
        }
    }

    public boolean onAPress() {
        if(gamepad.a) {
            return onPressA;
        }else{
            return false;
        }
    }

    public boolean onBPress() {
        if(gamepad.b) {
            return onPressB;
        }else{
            return false;
        }
    }

    public boolean onXPress() {
        if(gamepad.x) {
            return onPressX;
        }else{
            return false;
        }
    }

    public boolean onYPress() {
        if(gamepad.y) {
            return onPressY;
        }else{
            return false;
        }
    }

    public boolean onRBPress() {
        if(gamepad.right_bumper) {
            return onPressRB;
        }else{
            return false;
        }
    }

    public boolean onLBPress() {
        if(gamepad.right_bumper) {
            return onPressLB;
        }else{
            return false;
        }
    }
}
