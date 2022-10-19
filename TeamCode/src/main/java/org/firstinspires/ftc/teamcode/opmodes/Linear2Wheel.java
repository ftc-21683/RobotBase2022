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
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.ValueBounce;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="2 Wheel OpMode", group="Linear Opmode")
//@Disabled
public class Linear2Wheel extends LinearOpMode {

    //Store Logger
    AdditiveLogger logger = new AdditiveLogger(15);
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    private DcMotor leftArm = null;
    //private DcMotor rightArm = null;

    @Override
    public void runOpMode() {
        //Preprogrammed Locations:
        //High Cone is 2421
        //Med Cone is 1392
        //Low Cone is 0
        ValueBounce bounce = new ValueBounce(2421, 1392, 0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        logger.Log("Initialized");

        leftDrive = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        leftArm = hardwareMap.get(DcMotor.class, "left_arm");
        //rightArm = hardwareMap.get(DcMotor.class, "right_arm");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        double armSpeed = 0.5;

        leftArm.setDirection(DcMotor.Direction.REVERSE);
        leftArm.setTargetPosition(0);
        int armHeight = 0;
        int leftArmHeightMax = 3625;

        //rightArm.setDirection(DcMotor.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double leftPower;
            double rightPower;


            float mod = getDriveMod(gamepad1);
            double drive = -gamepad1.left_stick_y * mod;
            double turn  =  gamepad1.left_stick_x * mod;

            leftPower    = Range.clip(drive + turn, -1.0, 1.0);
            rightPower   = Range.clip(drive - turn, -1.0, 1.0);

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            logger.tickLogger(telemetry);

            // Arm stuff
            if(gamepad1.dpad_up){
                if(armHeight < leftArmHeightMax){
                    armHeight++;
                    leftArm.setPower(armSpeed);

                }
            }
            if(gamepad1.dpad_down){
                if(armHeight > 0){
                    armHeight--;
                    leftArm.setPower(-armSpeed);
                }
            }
            logger.Log("pos: " + armHeight);
            leftArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftArm.setTargetPosition(armHeight);

            if(gamepad1.right_trigger > 0.8) {
                leftArm.setPower(1);
                leftArm.setTargetPosition(bounce.advance());
            }


            if(gamepad1.dpad_left){

                if(armSpeed < 1){

                    armSpeed+= 0.1;

                }

            }else if(gamepad1.dpad_right){
                if(armSpeed > 0){

                    armSpeed-= 0.1;

                }
            }
        }
    }
    boolean firstR = false;
    boolean firstL = false;

    boolean turbo = false;
    boolean detail = false;

    float detailspeed = 0.2f;
    float turbospeed = 1f;


    public float getDriveMod(Gamepad gamepad) {
        //Bumpers will set the mod to 0.1 and 1 ️
        //triggers will set the mod from .1 - .75 (dist 0.65)

        if(gamepad.left_bumper) {
            if(firstL) {
                firstL = false;
                detail = !detail;
                turbo = false;
                logger.Log("Detail Toggled");
            }
            return detailspeed;
        }
        firstL = true;
        if(gamepad.right_bumper) {
            if(firstR) {
                firstR = false;
                turbo = !turbo;
                detail = false;
                logger.Log("Turbo Toggled");
            }
            return turbospeed;
        }
        firstR = true;
        if(turbo)
            return turbospeed;
        if(detail)
            return detailspeed;
        return 0.5f;
    }
}
