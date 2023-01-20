package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Translation2d;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.ChassisSpeeds;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveKinematics;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveOdometry;
import com.arcrobotics.ftclib.kinematics.wpilibkinematics.MecanumDriveWheelSpeeds;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class Drivebase {
    public static final double MAX_VELOCITY = 2.5;

    private final DcMotor front_left;
    Translation2d m_frontLeftLocation = new Translation2d(0.192, 0.12);
    private final DcMotor front_right;
    Translation2d m_frontRightLocation = new Translation2d(-0.192, 0.12);
    private final DcMotor back_left;
    Translation2d m_backLeftLocation = new Translation2d(0.192, -0.12);
    private final DcMotor back_right;
    Translation2d m_backRightLocation = new Translation2d(-0.192, -0.12);
    Gyroscope gyro;
    MecanumDriveKinematics m_kinematics = new MecanumDriveKinematics
            (
                    m_frontLeftLocation, m_frontRightLocation,
                    m_backLeftLocation, m_backRightLocation
            );
    MecanumDriveOdometry m_odometry;
    int x = 0;
    int y = 0;
    MecanumDrive mecanumController;

    public Drivebase(Motor front_left, Motor front_right, Motor back_left, Motor back_right, Gyroscope gyro) {
        this.front_left = front_left.motor;
        this.front_right = front_right.motor;
        this.back_left = back_left.motor;
        this.back_right = back_right.motor;
        this.gyro = gyro;

        // -- Set Directions
        this.front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        this.front_left.setDirection(DcMotorSimple.Direction.FORWARD);
        this.back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        this.back_right.setDirection(DcMotorSimple.Direction.FORWARD);

        m_odometry = new MecanumDriveOdometry
        (
            m_kinematics, gyro.getHeading(),
            new Pose2d(0.0, 0.0, new Rotation2d())
        );

        mecanumController = new MecanumDrive(front_left, front_right, back_left, back_right);
    }

    /**
     * @param drive Forwards and backwards movement
     * @param strafe Left and right movement
     * @param twist Rotational movement
     */
    public void run(double drive, double strafe, double twist) {
        double frontLeftPower = (drive + strafe + twist);
        double frontRightPower = (drive - strafe - twist);
        double backLeftPower = (drive - strafe + twist);
        double backRightPower = (drive + strafe - twist);

        frontLeftPower = Range.clip(frontLeftPower, -1, 1);
        frontRightPower = Range.clip(frontRightPower, -1, 1);
        backLeftPower = Range.clip(backLeftPower, -1, 1);
        backRightPower = Range.clip(backRightPower, -1, 1);

        front_left.setPower(frontLeftPower);
        front_right.setPower(frontRightPower);
        back_left.setPower(backLeftPower);
        back_right.setPower(backRightPower);
    }

    public void runFieldOriented(double drive, double strafe, double twist) {
        mecanumController.driveFieldCentric(strafe, drive, twist, gyro.getYaw());
    }

    public void runFieldOriented(double elapsedTime, double drive, double strafe, double twist) {
        MecanumDriveWheelSpeeds wheelSpeeds = new MecanumDriveWheelSpeeds
                (
                        front_left.getPower() * MAX_VELOCITY, front_right.getPower() * MAX_VELOCITY,
                        back_left.getPower() * MAX_VELOCITY, back_right.getPower() * MAX_VELOCITY
                );
        Rotation2d gyroAngle = Rotation2d.fromDegrees(gyro.getYaw());

        m_odometry.updateWithTime(elapsedTime, gyroAngle, wheelSpeeds);

        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(drive, strafe, twist, gyroAngle);

        wheelSpeeds = m_kinematics.toWheelSpeeds(speeds);

        double frontLeft = wheelSpeeds.frontLeftMetersPerSecond / MAX_VELOCITY;
        double frontRight = wheelSpeeds.frontRightMetersPerSecond / MAX_VELOCITY;
        double backLeft = wheelSpeeds.rearLeftMetersPerSecond / MAX_VELOCITY;
        double backRight = wheelSpeeds.rearRightMetersPerSecond / MAX_VELOCITY;

        frontLeft = Range.clip(frontLeft, -1, 1);
        frontRight = Range.clip(frontRight, -1, 1);
        backLeft = Range.clip(backLeft, -1, 1);
        backRight = Range.clip(backRight, -1, 1);

        front_left.setPower(frontLeft);
        front_right.setPower(frontRight);
        back_left.setPower(backLeft);
        back_right.setPower(backRight);
    }
}
