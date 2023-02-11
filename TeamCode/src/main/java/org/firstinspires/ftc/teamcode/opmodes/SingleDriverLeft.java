package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems;
import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.subsystems.Elevator;
import org.firstinspires.ftc.teamcode.subsystems.Gyroscope;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.ControllerInterface;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

@TeleOp(name="SoloDriverLeft", group="Mecanum")
public class SingleDriverLeft extends OpMode {
    Subsystems subsystems;
    ToggleModifier driveMod = new ToggleModifier(0.2f, 1, 0.75f);
    ControllerInterface gp1ci;
    Elevator elevator;
    Intake intake;
    Drivebase drivebase;
    Gyroscope gyroscope;
    AdditiveLogger logger;
    GamepadEx driverOp;
    boolean showExtraInfo;

    @Override
    public void init() {
        subsystems = new Subsystems(hardwareMap);
        driverOp = new GamepadEx(gamepad1);
        // --- Set Custom stuff
        logger = subsystems.getLogger();
        gp1ci       = new ControllerInterface(gamepad1, logger);

        elevator = subsystems.getElevator();
        intake = subsystems.getIntake();
        drivebase = subsystems.getDrivebase();
        gyroscope = subsystems.getGyroscope();
    }

    @Override
    public void loop() {
        gp1ci.tick();

        //Drive
        double strafe = gamepad1.left_stick_x;
        double drivemod = (double) driveMod.getModifier(gamepad1, logger) * Drivebase.MAX_VELOCITY;

        drivebase.run(
            -driverOp.getRightX() * drivemod,
            driverOp.getLeftY() * drivemod,
            driverOp.getLeftX() * drivemod
        );

//        drivebase.updatePose(getRuntime());

        //---------- Elevator Manual Controls

        elevator.addPosition((driverOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0 ? 1 : 0));
        elevator.addPosition(-(driverOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0 ? 1 : 0));

        //---------- Elevator Macros

        if(driverOp.wasJustPressed(GamepadKeys.Button.X)) {
            intake.toggle();
        }

        if(driverOp.wasJustPressed(GamepadKeys.Button.A)) {
            elevator.runToPercentage(0);
        }

        if(driverOp.wasJustPressed(GamepadKeys.Button.B)) {
            elevator.runToPercentage(0.75);
        }

        if(driverOp.wasJustPressed(GamepadKeys.Button.Y)) {
            elevator.runToPercentage(1);
        }

        //---------- Intake
        if(driverOp.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
            intake.close();
        }
        if(driverOp.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
            intake.open();
        }
        if(driverOp.wasJustPressed(GamepadKeys.Button.X)) {
            intake.toggle();
        }


        if(driverOp.isDown(GamepadKeys.Button.A)) {
            if(driverOp.wasJustPressed(GamepadKeys.Button.X)) {

            }
        }


        telemetry.addData("Armheight", elevator.getPosition());
        telemetry.addData("Servo", intake.getPosition());
        telemetry.addData("Gyroscope", gyroscope.getYaw());

        telemetry.addData("Drive", driverOp.getLeftY());
        telemetry.addData("Strafe", driverOp.getRightX());
        telemetry.addData("Twist", driverOp.getLeftX());
        telemetry.addData("Position", String.format("x: %f, y: %f", drivebase.pose.getX(), drivebase.pose.getY()));

        driverOp.readButtons();

        logger.tickLogger(telemetry);
    }
}

