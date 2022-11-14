package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

public class MovementSequencer {
    ArrayList<Action> eventFunction = new ArrayList<>();
    ArrayList<Double> eventTime = new ArrayList<>();
    ArrayList<String> logmsgs = new ArrayList<>();
    public AdditiveLogger logger;

    public void AddMovement(Action action, double actionTime) {
        this.eventFunction.add(action);
        eventTime.add(actionTime);
        logmsgs.add(null);
    }
    public void AddMovement(Action action, double actionTime, String logmsg) {
        AddMovement(action,actionTime);
        logmsgs.set(logmsg.length() - 1, logmsg);
    }

    public interface Action {
        public void execute();
    }

    public void ExecuteSequence(ElapsedTime runtime, OpMode opMode) {
        for(int i = 0; i < eventFunction.size(); i++) {
            runtime.reset();
            if(!logmsgs.get(i).isEmpty() || logmsgs.get(i) != null) {
                logger.Log(logmsgs.get(i));
            }
            while (runtime.seconds() < (double) eventTime.get(i)) {
                eventFunction.get(i).execute();
            }
        }
        opMode.requestOpModeStop();
    }
}
