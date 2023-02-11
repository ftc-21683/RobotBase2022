package org.firstinspires.ftc.teamcode.opmodes.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.subsystems.Intake;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;
import org.firstinspires.ftc.teamcode.utils.MovementSequencer;
import org.firstinspires.ftc.teamcode.utils.ToggleModifier;

@Autonomous(name="High_J_Right_Turbo", group="Mecanum")
public class AutonomousRight extends AutonomousGeneric {
    @Override
    public void init() {
        super.init();
        left = false;
    }
}