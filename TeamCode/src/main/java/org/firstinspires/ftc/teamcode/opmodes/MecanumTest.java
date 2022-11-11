package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="MecanumTest", group="Mecanum")
public class MecanumTest extends OpMode {

    private DcMotor front_left  = null;
    private DcMotor front_right = null;
    private DcMotor back_left   = null;
    private DcMotor back_right  = null;

    @Override
    public void init() {
        front_left   = hardwareMap.get(DcMotor.class, "front_left");
        front_right  = hardwareMap.get(DcMotor.class, "front_right");
        back_left    = hardwareMap.get(DcMotor.class, "back_left");
        back_right   = hardwareMap.get(DcMotor.class, "back_right");

        front_right.setDirection(DcMotorSimple.Direction.REVERSE);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    @Override
    public void loop() {
        double drive  = -gamepad1.left_stick_y;
        double strafe = gamepad1.left_stick_x * 1.1;
        double twist  = gamepad1.right_stick_x;

        double frontLeftPower = drive+strafe+twist;
        double frontRightPower = drive - strafe - twist;
        double backLeftPower = drive - strafe - twist;
        double backRightPower = drive + strafe - twist;

        frontLeftPower = Range.clip(frontLeftPower, -1 , 1);
        frontRightPower = Range.clip(frontRightPower, -1 , 1);
        backLeftPower = Range.clip(backLeftPower, -1 , 1);
        backRightPower = Range.clip(backRightPower, -1 , 1);

        front_left.setPower(frontLeftPower);
        front_right.setPower(frontRightPower);
        back_left.setPower(backLeftPower);
        back_right.setPower(backRightPower);

    }
}
