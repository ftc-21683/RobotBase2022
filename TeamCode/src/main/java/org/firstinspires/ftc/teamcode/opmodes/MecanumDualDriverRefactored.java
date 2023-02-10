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

@TeleOp(name="MecanumDualDriverRefactored", group="Mecanum")
public class MecanumDualDriverRefactored extends OpMode {
    ToggleModifier grabMod;
    ToggleModifier driveMod = new ToggleModifier(0.2f, 1, 0.75f);
    AdditiveLogger logger;
    ControllerInterface gp1ci;
    ControllerInterface gp2ci;

    Subsystems subsystems;
    Elevator elevator;
    Intake intake;
    Drivebase drivebase;
    Gyroscope gyroscope;
    GamepadEx driver;
    GamepadEx operator;

    @Override
    public void init() {
        subsystems = new Subsystems(hardwareMap);

        // --- Set Custom stuff
        logger      = new AdditiveLogger(15);
        gp1ci       = new ControllerInterface(gamepad1, logger);
        gp2ci       = new ControllerInterface(gamepad2, logger);

        // --- Declare Subsystems
        driver = new GamepadEx(gamepad1);
        operator = new GamepadEx(gamepad2);
        elevator = subsystems.getElevator();
        intake = subsystems.getIntake();
        drivebase = subsystems.getDrivebase();
        gyroscope = subsystems.getGyroscope();
    }

    @Override
    public void loop() {
        gp1ci.tick();
        gp2ci.tick();

        //Drive
        double strafe = gamepad1.left_stick_x * 1.1;
        double drivemod = (double) driveMod.getModifier(gamepad1, logger) * Drivebase.MAX_VELOCITY;

        drivebase.run(
        -gamepad1.left_stick_y * drivemod ,
        gamepad1.right_stick_x * drivemod,
        strafe * drivemod
        );


        //---------- Elevator Manual Controls

        elevator.addPosition((gamepad2.dpad_up ? 1 : 0));
        elevator.addPosition(-(gamepad2.dpad_down ? 1 : 0));

        //---------- Elevator Macros

        if(operator.wasJustPressed(GamepadKeys.Button.X)) {
            intake.toggle();
        }

        if(gamepad2.a) {
            elevator.runToPercentage(0);
        }

        if(gamepad2.b) {
            elevator.runToPercentage(0.75);
        }

        if(gamepad2.y) {
            elevator.runToPercentage(1);
        }

        //---------- Intake
        if(gamepad2.left_stick_y < 0) {
            intake.close();
        }
        if(gamepad2.left_stick_y > 0) {
            intake.open();
        }

        // --------- Power Calculations
        telemetry.addData("Armheight", elevator.getPosition());
        telemetry.addData("Servo", intake.getPosition());
        telemetry.addData("Gyroscope", gyroscope.getYaw());
        operator.readButtons();
        driver.readButtons();
        logger.tickLogger(telemetry);

    }
}

