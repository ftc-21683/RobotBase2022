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
        try {
            logs[logs.length - 1] = getCallerCallerClassName() + " - " + message;
        }catch(Exception ignored) {
            logs[logs.length - 1] = message;
        }
    }

    public void tickLogger(Telemetry tele) {
        for(String message : logs) {
            if(message == null) {
                continue;
            }
            if(message.isEmpty()) {
                continue;
            }
            tele.addData("", message);
        }
        tele.update();
    }

    public static String getCallerCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        String callerClassName = null;
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (ste.getClassName().indexOf("java.lang.Thread")!=0) {
                if (callerClassName==null) {
                    callerClassName = ste.getClassName();
                } else if (!callerClassName.equals(ste.getClassName())) {
                    return ste.getClassName();
                }
            }
        }
        return null;
    }
}
