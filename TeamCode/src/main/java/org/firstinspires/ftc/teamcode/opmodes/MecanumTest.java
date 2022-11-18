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

@TeleOp(name="MecanumTest", group="Mecanum")
public class MecanumTest extends OpMode {
    private int MAX_ARM_HEIGHT = 4000;
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
        // --------- Control Area

        //Drive
        double drive = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.1;
        double twist = gamepad1.right_stick_x;

        //Arm
        if (gamepad2.dpad_up && armHeight < MAX_ARM_HEIGHT) {
            armHeight += 3;
        }
        if (gamepad2.dpad_down && armHeight > MIN_ARM_HEIGHT) {
            armHeight -= 2;
        }

        gp2ci.events.add(new ButtonEvent(GamepadButton.X, () -> {
            if(MecanumTest.grabPosition == MAX_GRAB_DIST) {
                MecanumTest.grabPosition = 0;
            }else {
                MecanumTest.grabPosition = MAX_GRAB_DIST;
            }
        }));

        gp2ci.events.add(new ButtonEvent(GamepadButton.Y, () -> {
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
        grabPosition += gamepad2.left_stick_y * grabMod.getModifier(gamepad2, logger);

        // --------- Power Calculations

        double drivemod = (double) driveMod.getModifier(gamepad1, logger);

        double frontLeftPower = (drive + strafe);
        double frontRightPower = (drive - strafe);
        double backLeftPower = (drive - strafe);
        double backRightPower = (drive + strafe);

        // --------- Rotation Calculating

        frontLeftPower = (frontLeftPower + (MAX_CLIP_HEIGHT - frontLeftPower)) * drivemod; //this should make rotation equal; rotation is weird because the rotation is value is static for each wheel, so if the left wheel is 0.5 power and the right is 0.75, and we add 0.25, the right wheel will be more powerful therefore breaking the rotation
        frontRightPower = frontRightPower - ((MAX_CLIP_HEIGHT - frontLeftPower)) * drivemod;
        backLeftPower = backLeftPower - ((MAX_CLIP_HEIGHT - frontLeftPower)) * drivemod;
        backRightPower = backRightPower - ((MAX_CLIP_HEIGHT - frontLeftPower)) * drivemod;

        // --------- Range Clipping

        frontLeftPower = Range.clip(frontLeftPower, MIN_CLIP_HEIGHT, MAX_CLIP_HEIGHT);
        frontRightPower = Range.clip(frontRightPower, MIN_CLIP_HEIGHT, MAX_CLIP_HEIGHT);
        backLeftPower = Range.clip(backLeftPower, MIN_CLIP_HEIGHT, MAX_CLIP_HEIGHT);
        backRightPower = Range.clip(backRightPower, MIN_CLIP_HEIGHT, MAX_CLIP_HEIGHT);
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

