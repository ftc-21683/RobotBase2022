package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.ButtonEvent;
import org.firstinspires.ftc.teamcode.utils.ControllerInterface;
import org.firstinspires.ftc.teamcode.utils.GamepadButton;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

@TeleOp(name="MecanumSoloDriverTest", group="Mecanum")
public class MecanumSingleDriver extends OpMode {
    private int MAX_ARM_HEIGHT = 4000;
    private int MIN_ARM_HEIGHT = 0;
    public static final double MAX_GRAB_DIST = 0.0692;
    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;
    private DcMotor arm = null;
    private Servo grab = null;
    private int armHeight = 0;
    public static double grabPosition = 0;

    ToggleModifier driveMod = new ToggleModifier(0.2f, 1, 0.75f);
    AdditiveLogger logger;
    ControllerInterface gp1ci;

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
        logger      = new AdditiveLogger(15);
        gp1ci       = new ControllerInterface(gamepad1, logger);
    }

    @Override
    public void loop() {
        gp1ci.tick();

        //Drive
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.1;
        double twist = gamepad1.right_stick_x;

        //Arm
        if (armHeight < MAX_ARM_HEIGHT) {
            armHeight += gamepad1.right_trigger * 3;
        }
        if (armHeight > MIN_ARM_HEIGHT) {
            armHeight -= gamepad1.left_trigger * 2;
        }

        gp1ci.events.add(new ButtonEvent(GamepadButton.X, () -> {
            if(MecanumSingleDriver.grabPosition == MAX_GRAB_DIST) {
                MecanumSingleDriver.grabPosition = 0;
            }else {
                MecanumSingleDriver.grabPosition = MAX_GRAB_DIST;
            }
        }));

        gp1ci.events.add(new ButtonEvent(GamepadButton.A, () -> {
            armHeight = 0;
        }));

        gp1ci.events.add(new ButtonEvent(GamepadButton.B, () -> {
            armHeight = 2860;
        }));

        gp1ci.events.add(new ButtonEvent(GamepadButton.Y, () -> {
            armHeight = 3965;
        }));

        gp1ci.events.add(new ButtonEvent(GamepadButton.Y, () -> {
            if(MAX_ARM_HEIGHT == 3865){
                MAX_ARM_HEIGHT = 9999;
                MIN_ARM_HEIGHT = -9999;
                logger.Log("setting height to " + MAX_ARM_HEIGHT + " and bottom to " + MIN_ARM_HEIGHT);
            }else if(MAX_ARM_HEIGHT == 9999){
                MAX_ARM_HEIGHT = 3865;
                MIN_ARM_HEIGHT = 0;
                logger.Log("setting height to back to " + MAX_ARM_HEIGHT + " and bottom to 0");
            }
        }));

        //Grab
        grabPosition += (gamepad1.dpad_down ? -1 : 0) + (gamepad1.dpad_up ? 1 : 0) * 0.006f;

        // --------- Power Calculations

        double drivemod = (double) driveMod.getModifier(gamepad1, logger);

        double frontLeftPower = (drive + strafe + twist) * drivemod;
        double frontRightPower = (drive - strafe - twist) * drivemod;
        double backLeftPower = (drive - strafe + twist) * drivemod;
        double backRightPower = (drive + strafe - twist) * drivemod;

        // --------- Range Clipping

        frontLeftPower = Range.clip(frontLeftPower, -1, 1);
        frontRightPower = Range.clip(frontRightPower, -1, 1);
        backLeftPower = Range.clip(backLeftPower, -1, 1);
        backRightPower = Range.clip(backRightPower, -1, 1);
        grabPosition = Range.clip(grabPosition, 0, MAX_GRAB_DIST);

        // --------- Power Setting

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

