package org.firstinspires.ftc.teamcode.opmodes.Auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="2 Wheel OpModehehehe", group="Linear Opmode")
public class ApriltagDetection extends LinearOpMode {
    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("MainCamera", "id", hardwareMap.appContext.getPackageName());

    @Override
    public void runOpMode() throws InterruptedException {
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK);
        //Camera opening is done Sync, and in future this should be changed to be async
        camera.openCameraDevice();

        camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);

        //camera.setPipeline(new AprilTagDetectionPipeline(8, ));
    }
}
