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

    Motor.Encoder front_left_encoder;
    Motor.Encoder front_right_encoder;
    Motor.Encoder back_left_encoder;
    Motor.Encoder back_right_encoder;

    // Gyroscope gyro;
    MecanumDriveKinematics m_kinematics = new MecanumDriveKinematics
            (
                    m_frontLeftLocation, m_frontRightLocation,
                    m_backLeftLocation, m_backRightLocation
            );
    MecanumDriveOdometry m_odometry;
    public Pose2d pose = new Pose2d();
    MecanumDrive mecanumController;

    public Drivebase(Motor front_left, Motor front_right, Motor back_left, Motor back_right, Gyroscope gyro) {
        this.front_left = front_left.motor;
        this.front_right = front_right.motor;
        this.back_left = back_left.motor;
        this.back_right = back_right.motor;
        //this.gyro = gyro;

        // -- Set Directions
        this.front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        this.front_left.setDirection(DcMotorSimple.Direction.FORWARD);
        this.back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        this.back_right.setDirection(DcMotorSimple.Direction.FORWARD);


        front_left_encoder = front_left.encoder;
        front_right_encoder = front_right.encoder;

        back_left_encoder = back_left.encoder;
        back_right_encoder = back_right.encoder;

//        m_odometry = new MecanumDriveOdometry (
//            m_kinematics, gyro.getHeading(),
//            new Pose2d(0.0, 0.0, new Rotation2d())
//        );

        mecanumController = new MecanumDrive(front_left, front_right, back_left, back_right);
    }



    /**
     * @param drive Forwards and backwards movement
     * @param strafe Left and right movement
     * @param twist Rotational movement
     */
    public void run(double drive, double strafe, double twist) {
        this.front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        this.front_left.setDirection(DcMotorSimple.Direction.FORWARD);
        this.back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        this.back_right.setDirection(DcMotorSimple.Direction.FORWARD);


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

    /**
     * @param drive Forwards and backwards movement
     * @param strafe Left and right movement
     * @param twist Rotational movement
     */
    public void runFTCLIB(double drive, double strafe, double twist) {
        this.front_right.setDirection(DcMotorSimple.Direction.REVERSE);
        this.front_left.setDirection(DcMotorSimple.Direction.FORWARD);
        this.back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        this.back_right.setDirection(DcMotorSimple.Direction.REVERSE);

        mecanumController.driveRobotCentric(strafe, drive, twist);
    }

//    public void runFieldOriented(double drive, double strafe, double twist) {
//        mecanumController.driveFieldCentric(strafe, drive, twist, gyro.getYaw());
//    }

//    public void updatePose(double elapsedTime) {
//        MecanumDriveWheelSpeeds wheelSpeeds = new MecanumDriveWheelSpeeds (
//            front_left_encoder.getRate(), front_right_encoder.getRate(),
//            back_left_encoder.getRate(), back_right_encoder.getRate()
//        );
//
//        Rotation2d gyroAngle = Rotation2d.fromDegrees(gyro.getYaw());
//
//
//        pose = m_odometry.updateWithTime(elapsedTime, gyroAngle, wheelSpeeds);
//    }
}
