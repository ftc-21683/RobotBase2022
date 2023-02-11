package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class Sensor {
    ColorSensor colorSensor;

    public Sensor(ColorSensor sensor) {
        colorSensor = sensor;
    }

    public int[] getColor() {
        colorSensor.enableLed(true);
        int[] colors = {colorSensor.red(), colorSensor.green(), colorSensor.blue()};
        colorSensor.enableLed(false);
        return colors;
    }
}
