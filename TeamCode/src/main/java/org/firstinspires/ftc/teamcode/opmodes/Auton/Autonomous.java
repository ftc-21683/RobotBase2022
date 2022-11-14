package org.firstinspires.ftc.teamcode.opmodes.Auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.opmodes.MecanumTest;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.ControllerInterface;
import org.firstinspires.ftc.teamcode.utils.MovementSequencer;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="MecanumAuton", group="Mecanum")
public class Autonomous extends LinearOpMode {
    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;
    private DcMotor arm = null;
    private Servo grab = null;
    double grabPosition = 0;
    int armHeight = 0;

    ToggleModifier grabMod;
    AdditiveLogger logger;

    private ElapsedTime runtime = new ElapsedTime();
    private final MovementSequencer sequencer = new MovementSequencer();


    @Override
    public void runOpMode() throws InterruptedException {
        // --- Register Drive Motors
        front_left   = hardwareMap.get(DcMotor.class, "front_left");
        front_right  = hardwareMap.get(DcMotor.class, "front_right");
        back_left    = hardwareMap.get(DcMotor.class, "back_left");
        back_right   = hardwareMap.get(DcMotor.class, "back_right");

        // --- Register Arm Motor
        arm          = hardwareMap.get(DcMotor.class, "arm");

        // --- Register Arm Servo
        grab         = hardwareMap.get(Servo.class, "grab");

        // --- Set Motor Direction
        front_right.setDirection(DcMotorSimple.Direction.FORWARD);
        back_left.setDirection(DcMotorSimple.Direction.FORWARD);
        back_right.setDirection(DcMotorSimple.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.REVERSE);

        // --- Set Servo Direction
        grab.setDirection(Servo.Direction.REVERSE);
        // --- Set Custom stuff
        grabMod     = new ToggleModifier(0.001f, 0.006f, 0.0005f);
        logger      = new AdditiveLogger(15);

        waitForStart();
        sequencer.AddMovement(() -> {
            DriveWithSimController(1, 0, 0, 0, 0);
        }, 0.5, "Moving Forward!");
        sequencer.AddMovement(() -> {
            DriveWithSimController(0, 1, 0, 0, 0);
        }, 0.5, "Done!");

        sequencer.ExecuteSequence(runtime, this);


    }

    void DriveWithSimController(double gp1lsy, double gp1lsx, double gp1rsx, int armcontroll, double servo) {
        double drive = gp1lsy;
        double strafe = gp1lsx * 1.1;
        double twist = gp1rsx;
        armHeight = armcontroll;
        grabPosition = servo;

        double frontLeftPower = (drive + strafe + twist);
        double frontRightPower = (drive - strafe - twist);
        double backLeftPower = (drive - strafe - twist);
        double backRightPower = (drive + strafe - twist);

        frontLeftPower = Range.clip(frontLeftPower, -1, 1);
        frontRightPower = Range.clip(frontRightPower, -1, 1);
        backLeftPower = Range.clip(backLeftPower, -1, 1);
        backRightPower = Range.clip(backRightPower, -1, 1);
        grabPosition = Range.clip(grabPosition, 0, MecanumTest.MAX_GRAB_DIST);

        front_left.setPower(frontLeftPower);
        front_right.setPower(frontRightPower);
        back_left.setPower(backLeftPower);
        back_right.setPower(backRightPower);

        arm.setPower(0.5);
        arm.setTargetPosition(armHeight);

        grab.setPosition(grabPosition);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        telemetry.addData("Armheight", armHeight);
        telemetry.addData("Servo", grabPosition);

        logger.tickLogger(telemetry);
    }

}
