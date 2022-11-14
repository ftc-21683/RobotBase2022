package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;

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
    public AdditiveLogger logger;
    public ArrayList<ButtonEvent> events = new ArrayList<>();


    public ControllerInterface(Gamepad gamepad, AdditiveLogger logger) {
        this.gamepad = gamepad;
        this.logger = logger;
    }

    public void tick() {
        if(gamepad.a) {
            if(onPressA) {
                onPressA = false;
                for(ButtonEvent event : events) {
                    if(event.button == GamepadButton.A)
                        event.eventFunction.onPress();
                }
            }
        }else{
            onPressA = true;
        }

        if(gamepad.b) {
            if(onPressB) {
                onPressB = false;
                for(ButtonEvent event : events) {
                    if(event.button == GamepadButton.B)
                        event.eventFunction.onPress();
                }
            }
        }else{
            onPressB = true;
        }

        if(gamepad.x) {
            if(onPressX) {
                for(ButtonEvent event : events) {
                    if(event.button == GamepadButton.X) {
                        event.eventFunction.onPress();
                    }

                }
                onPressX = false;
            }
        }else{
            onPressX = true;
        }

        if(gamepad.y) {
            if(onPressY) {
                for(ButtonEvent event : events) {
                    if(event.button == GamepadButton.Y)
                        event.eventFunction.onPress();
                }
                logger.Log("Test");
                onPressY = false;
            }
        }else{
            onPressY = true;
        }



        if(gamepad.right_bumper) {
            if(onPressRB) {
                for(ButtonEvent event : events) {
                    if(event.button == GamepadButton.RB)
                        event.eventFunction.onPress();
                }
                onPressRB = false;
            }
        }else{
            onPressRB = true;
        }

        if(gamepad.left_bumper) {
            if(onPressLB) {
                for(ButtonEvent event : events) {
                    if(event.button == GamepadButton.LB)
                        event.eventFunction.onPress();
                }
                onPressLB = false;
            }
        }else{
            onPressLB = true;
        }
    }



    /*public boolean onAPress() {
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
    }*/
}
