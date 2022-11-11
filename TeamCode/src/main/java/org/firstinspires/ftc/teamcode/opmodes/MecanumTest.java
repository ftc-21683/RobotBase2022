package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

@TeleOp(name="MecanumTest", group="Mecanum")
public class MecanumTest extends OpMode {
    private final int MAX_ARM_HEIGHT = 3600;
    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;
    private DcMotor arm = null;
    private Servo grab = null;
    private int armHeight = 0;
    private double grabPosition = 0;

    ToggleModifier grabMod;
    AdditiveLogger logger;

    @Override
    public void init() {
        // --- Register Drive Motors
        front_left   = hardwareMap.get(DcMotor.class, "front_left");
        front_right  = hardwareMap.get(DcMotor.class, "front_right");
        back_left    = hardwareMap.get(DcMotor.class, "back_left");
        back_right   = hardwareMap.get(DcMotor.class, "back_right");

        // --- Register Arm Motor
        arm = hardwareMap.get(DcMotor.class, "arm");

        // --- Register Arm Servo
        grab = hardwareMap.get(Servo.class, "grab");

        // --- Set Motor Direction
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);
        // --- Set Servo Direction
        arm.setDirection(DcMotor.Direction.REVERSE);

        // --- Set Modes
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // --- Set Custom stuff
        grabMod = new ToggleModifier(0.001f, 0.006f, 0.0005f);
        logger = new AdditiveLogger(15);
    }

    @Override
    public void loop() {
        // --------- Control Area

        //Drive
        double drive  = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.1;
        double twist  = gamepad1.right_stick_x;

        //Arm
        if(gamepad2.dpad_up && armHeight < MAX_ARM_HEIGHT){
            armHeight += 3;
        }
        if(gamepad2.dpad_down && armHeight > 0){
            armHeight -= 2;
        }

        //Grab
        grabPosition += gamepad2.left_stick_y * grabMod.getModifier(gamepad2, logger);

        // --------- Power Calculations

        double frontLeftPower = drive+strafe+twist;
        double frontRightPower = drive - strafe - twist;
        double backLeftPower = drive - strafe - twist;
        double backRightPower = drive + strafe - twist;

        // --------- Range Clipping

        frontLeftPower = Range.clip(frontLeftPower, -1 , 1);
        frontRightPower = Range.clip(frontRightPower, -1 , 1);
        backLeftPower = Range.clip(backLeftPower, -1 , 1);
        backRightPower = Range.clip(backRightPower, -1 , 1);
        grabPosition = Range.clip(grabPosition, 0, 0.65);

        // --------- Power Setting

        front_left.setPower(frontLeftPower);
        front_right.setPower(frontRightPower);
        back_left.setPower(backLeftPower);
        back_right.setPower(backRightPower);

        arm.setPower(0.5);
        arm.setTargetPosition(armHeight);

        grab.setPosition(grabPosition);
    }
}
