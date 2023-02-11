package org.firstinspires.ftc.teamcode.opmodes.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Subsystems;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.LegacyMoveSequencer;
import org.firstinspires.ftc.teamcode.utils.LegacyMovement;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

public class AutonomousGeneric extends OpMode implements AutonMode {
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
    private final LegacyMoveSequencer sequencer = new LegacyMoveSequencer();
    boolean left;


    @Override
    public void init() {
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
        int LowArmHeight = 1000;

        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(0, 0, 0, LowArmHeight, 0.0692);
        }, 1000));
        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(0.6, 0, 0., LowArmHeight, 0.0692);
        }, 4000/2));
        sequencer.addMovement(new LegacyMovement(() -> { //Wait so no slipping
            DriveWithSimController(0, 0, 0, LowArmHeight, 0.0692);
        }, 1000));
        sequencer.addMovement(new LegacyMovement(() -> { // Go back
            DriveWithSimController(-0.4, 0, 0., LowArmHeight, 0.0692);
        }, 800));
        sequencer.addMovement(new LegacyMovement(() -> { // move to side
            DriveWithSimController(0, 0.6, 0, 4000, 0.0692);
        }, 450));
        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(0.2, 0, 0, 4000, 0.0692);
        }, 3000/2));
        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(0, 0, 0, 4000, 0.0692);
        }, 1000));
        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(0, 0, 0, 3900, 0);
        }, 2000));
        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(-0.4, 0, 0, 0, 0);
        }, 500/2));
        sequencer.addMovement(new LegacyMovement(() -> {
            DriveWithSimController(0, 0, 0, 0, 0);
        }, 4000));
        //sequencer.ExecuteSequence(this, runtime, this);


    }

    @Override
    public boolean requestedStop() {
        return false;
    }

    @Override
    public Subsystems getSubSystems() {
        return null;
    }

    void DriveWithSimController(double gp1lsy, double gp1lsx, double gp1rsx, int armcontroll, double servo) {
        double drive = gp1lsy;
        double strafe = gp1lsx * 1.1 * (left ? 1 : -1);
        double twist = gp1rsx;
        armHeight = armcontroll;
        grabPosition = servo;

        double frontLeftPower = (drive + strafe + twist);
        double frontRightPower = (drive - strafe - twist);
        double backLeftPower = (drive - strafe + twist);
        double backRightPower = (drive + strafe - twist);

        frontLeftPower = Range.clip(frontLeftPower, -1, 1);
        frontRightPower = Range.clip(frontRightPower, -1, 1);
        backLeftPower = Range.clip(backLeftPower, -1, 1);
        backRightPower = Range.clip(backRightPower, -1, 1);
        grabPosition = Range.clip(grabPosition, 0, Intake.open);

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

    @Override
    public void loop() {
        sequencer.tick(this);
    }
}
