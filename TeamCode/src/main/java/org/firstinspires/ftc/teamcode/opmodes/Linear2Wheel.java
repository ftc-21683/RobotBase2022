/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.ButtonEvent;
import org.firstinspires.ftc.teamcode.utils.ControllerInterface;
import org.firstinspires.ftc.teamcode.utils.GamepadButton;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;
import org.firstinspires.ftc.teamcode.utils.ValueBounce;


/**
 * @author Rudy Soliz
 * @author Nicholas Vettor
 */

@TeleOp(name="2 Wheel OpMode", group="Linear Opmode")
//@Disabled
public class Linear2Wheel extends LinearOpMode {
    public static Linear2Wheel active2Wheel;

    static double armSpeed = 0.5;
    static boolean inverted_turn = false;

    //Store Logger
    AdditiveLogger logger = new AdditiveLogger(10);
    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();

    ToggleModifier driveMod = new ToggleModifier(0.2f, 1);
    ToggleModifier grabMod = new ToggleModifier(0.001f, 0.006f, 0.0005f);
    //private DcMotor rightArm = null;

    @Override
    public void runOpMode() {
        active2Wheel = this;
        //Preprogrammed Locations:
        //High Cone is 2421
        //Med Cone is 1392
        //Low Cone is 0
        ValueBounce bounce = new ValueBounce(2818, 1392, 0);


        // --- Add Controller Interfaces
        ControllerInterface interface2 = null; //new ControllerInterface(gamepad2);
        ControllerInterface interface1 = null; //new ControllerInterface(gamepad1);
        interface2.logger = logger;

        // --- Send Initialized Status
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        logger.Log("Initialized");

        // --- Register Drive Motors
        DcMotor leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        DcMotor rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // --- Register Arm Motors
        DcMotor leftArm = hardwareMap.get(DcMotor.class, "left_arm");
        DcMotor rightArm = hardwareMap.get(DcMotor.class, "right_arm");

        // --- Register Grab Servos
        Servo leftGrab = hardwareMap.get(Servo.class, "left_grab");
        Servo rightGrab = hardwareMap.get(Servo.class, "right_grab");

        // --- Set Motor Directions
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        leftArm.setDirection(DcMotor.Direction.REVERSE);
        rightArm.setDirection(DcMotor.Direction.REVERSE);
        leftGrab.setDirection(Servo.Direction.REVERSE);
        rightGrab.setDirection(Servo.Direction.FORWARD);

        // --- Set Default Target Positions
        leftArm.setTargetPosition(0);
        rightArm.setTargetPosition(0);



        // --- Set Default Speed
        int armHeight = 0;
        int maxArmHeight = 3600; // 4206 // 2818
        // -- Set Default Servo Positions
        double leftGrabPosition = 0;
        double rightGrabPosition = 0;


        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            interface2.tick();
            double leftPower;
            double rightPower;

            // --- Get the modifier
            float mod = driveMod.getModifier(gamepad1, logger);

            // --- Get Input Data for drive
            double drive = gamepad1.left_stick_y * mod;
            double turn = (inverted_turn ? 1 : -1) * gamepad1.left_stick_x * mod;

            leftPower = Range.clip(drive + turn, -1, 1.0);
            rightPower = Range.clip(drive - turn, -1, 1.0);

            // --- Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // --- Get Input for arms
            if(gamepad2.dpad_up){
                if(armHeight < maxArmHeight){
                    armHeight += 3;
                    leftArm.setPower(armSpeed);
                    rightArm.setPower(armSpeed);
                }
            }
            if(gamepad2.dpad_down){
                if(armHeight > 0){
                    armHeight -= 2;
                    leftArm.setPower(-armSpeed * 0.75);
                    rightArm.setPower(-armSpeed * 0.75);
                }
            }
            leftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftArm.setTargetPosition(armHeight);
            rightArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightArm.setTargetPosition(armHeight);

            // --- Set Position Levels
            if(gamepad2.right_trigger > 0.8) {
                int pos = bounce.advance();
                leftArm.setTargetPosition(pos);
                rightArm.setTargetPosition(pos);
            }

            // --- Experimental Arm Speed Control
            interface2.events.add(new ButtonEvent(GamepadButton.Y, () -> {
                Linear2Wheel.armSpeed += 0.1;
            }));
            interface2.events.add(new ButtonEvent(GamepadButton.A, () -> {
                Linear2Wheel.armSpeed -= 0.1;
            }));

            // --- Experimental Drive Inversion
            interface1.events.add(new ButtonEvent(GamepadButton.X, () -> {
                logger.Log("inverted");
                Linear2Wheel.inverted_turn = !Linear2Wheel.inverted_turn;
            }));

            // --- Grab Controllers
            leftGrabPosition += gamepad2.left_stick_y * grabMod.getModifier(gamepad2, logger);
            leftGrabPosition = Range.clip(leftGrabPosition, 0, 0.65);

            rightGrabPosition += gamepad2.left_stick_y * grabMod.getModifier(gamepad2, logger);
            rightGrabPosition = Range.clip(rightGrabPosition, 0, 0.65);

            leftGrab.setPosition(leftGrabPosition);
            rightGrab.setPosition(rightGrabPosition);

            // -- Show the elapsed game time and wheel power.
            telemetry.addData("Armheight", armHeight);
            telemetry.addData("servoMotion", "(" + leftGrabPosition + ", " +rightGrabPosition + ")");
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
        }
    }
}
