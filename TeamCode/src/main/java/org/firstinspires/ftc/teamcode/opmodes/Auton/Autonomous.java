package org.firstinspires.ftc.teamcode.opmodes.Auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Subsystems;
import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.MovementSequencer;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name="A_right", group="Mecanum")
public class Autonomous extends OpMode implements AutonMode {
    Subsystems systems;
    ToggleModifier grabMod;
    AdditiveLogger logger;

    private final MovementSequencer sequencer = new MovementSequencer();

    @Override
    public void init() {
        systems = new Subsystems(hardwareMap);

        // --- Set Custom stuff
        grabMod     = new ToggleModifier(0.001f, 0.006f, 0.0005f);
        logger      = new AdditiveLogger(15);
    }

    @Override
    public void loop() {
        sequencer.tick(this);
    }

    @Override
    public boolean requestedStop() {
        return false;
    }

    @Override
    public Subsystems getSubSystems() {
        return null;
    }
}
