package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;

public class Subsystems {
    final Drivebase drivebase;
    final Elevator elevator;
    final Intake intake;
    final Gyroscope gyroscope;
    final AdditiveLogger logger;

    public Drivebase getDrivebase() {
        return drivebase;
    }

    public Elevator getElevator() {
        return elevator;
    }

    public Intake getIntake() {
        return intake;
    }

    public Gyroscope getGyroscope() {
        return gyroscope;
    }
    public AdditiveLogger getLogger() {return  logger;}

    public Subsystems(HardwareMap hardwareMap) {
        gyroscope = null;
        drivebase = new Drivebase(
            new Motor(hardwareMap, "front_left"),
            new Motor(hardwareMap, "front_right"),
            new Motor(hardwareMap, "back_right"),
            new Motor(hardwareMap, "back_left"),
            gyroscope
        );
        elevator = new Elevator(
            hardwareMap.get(DcMotor.class, "arm")
        );
        intake = new Intake(
            hardwareMap.get(Servo.class, "grab")
        );
        logger = new AdditiveLogger(11);
    }
}
