package org.firstinspires.ftc.teamcode.utils;

import java.util.function.Function;

public class ButtonEvent {
    PressAction eventFunction;
    GamepadButton button;

    public ButtonEvent(GamepadButton button, PressAction action) {
        this.eventFunction = action;
        this.button = button;
    }

    public interface PressAction {
        public void onPress();
    }
}
