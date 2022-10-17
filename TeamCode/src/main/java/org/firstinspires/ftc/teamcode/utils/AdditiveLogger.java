package org.firstinspires.ftc.teamcode.utils;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.List;

public class AdditiveLogger {
    String[] logs;
    int logCount = 0;


    /**
     *
     * @param maxLogs How many logs will be saved at one time.
     */
    public AdditiveLogger(int maxLogs) {
        logs = new String[maxLogs + 1];

    }

    public void Log(String message) {
        for(int i = 0; i < logs.length - 1; i++) {
            if(i == 0) {
                continue;
            }
            logs[i] = logs[i+1];
        }
        logs[logs.length - 1] = message;
    }

    public void tickLogger(Telemetry tele) {
        for(String message : logs) {
            if(message == null) {
                continue;
            }
            if(message.isEmpty()) {
                continue;
            }
            tele.addData("LOG", message);
        }
        tele.update();
    }
}
