package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Gyroscope {
    final IMU imu;

    public Gyroscope(IMU imu) {
        this.imu = imu;

        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
        );
        imu.initialize(parameters);
        imu.resetYaw();
    }

    public double getYaw() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    public Rotation2d getHeading() {
        return new Rotation2d(0, getYaw());
    }
}
