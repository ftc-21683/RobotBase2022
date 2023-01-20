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

@TeleOp(name="MecanumDualDriverRotated", group="Mecanum")
public class MecanumDualDriverRotated extends OpMode {
    private int MAX_ARM_HEIGHT = 4100;
    private final int MAX_CLIP_HEIGHT = 1;
    private final int MIN_CLIP_HEIGHT = -1;
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

    ToggleModifier grabMod;
    ToggleModifier driveMod = new ToggleModifier(0.2f, 1, 0.75f);
    AdditiveLogger logger;
    ControllerInterface gp1ci;
    ControllerInterface gp2ci;

    private static void onPress() {
        if (grabPosition == MAX_GRAB_DIST) {
            grabPosition = 0;
            return;
        }
        grabPosition = MAX_GRAB_DIST;
    }

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
        gp1ci       = new ControllerInterface(gamepad1, logger);
        gp2ci       = new ControllerInterface(gamepad2, logger);
    }

    @Override
    public void loop() {
        gp1ci.tick();
        gp2ci.tick();

        //Drive
        double drive = gamepad1.left_stick_x;
        double strafe = gamepad1.left_stick_y * 1.1;
        double twist = gamepad1.right_stick_x;

        //Arm
        if (armHeight < MAX_ARM_HEIGHT) {
            armHeight += (gamepad2.dpad_up ? 1 : 0) * 3;
        }
        if (armHeight > MIN_ARM_HEIGHT) {
            armHeight -= (gamepad2.dpad_down ? 1 : 0) * 3;
        }

        gp2ci.events.add(new ButtonEvent(GamepadButton.X, MecanumDualDriverRotated::onPress));

        gp2ci.events.add(new ButtonEvent(GamepadButton.A, () -> {
            armHeight = MIN_ARM_HEIGHT;
            if(gamepad2.right_bumper) {
                MAX_ARM_HEIGHT = Integer.MAX_VALUE;
            }
        }));

        gp2ci.events.add(new ButtonEvent(GamepadButton.B, () -> {
            if(MAX_ARM_HEIGHT > 7000) {
                return;
            }
            armHeight = (int) Math.ceil(MAX_ARM_HEIGHT * 0.75);
        }));

        gp2ci.events.add(new ButtonEvent(GamepadButton.Y, () -> {
            if(MAX_ARM_HEIGHT > 7000) {
                return;
            }
            armHeight = MAX_ARM_HEIGHT;
            
        }));

        //Grab
        grabPosition += gamepad2.left_stick_y * grabMod.getModifier(gamepad2, logger);

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

