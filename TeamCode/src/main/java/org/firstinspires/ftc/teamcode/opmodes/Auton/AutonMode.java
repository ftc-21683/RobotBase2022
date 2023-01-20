package org.firstinspires.ftc.teamcode.opmodes.Auton;

import org.firstinspires.ftc.teamcode.Subsystems;

public interface AutonMode {
    boolean requestedStop();

    Subsystems getSubSystems();
}
