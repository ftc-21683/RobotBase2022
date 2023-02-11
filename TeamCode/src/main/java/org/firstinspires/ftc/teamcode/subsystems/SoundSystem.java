package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.ftccommon.SoundPlayer;

import org.firstinspires.ftc.robotcore.external.android.AndroidSoundPool;
import org.firstinspires.ftc.teamcode.Subsystems;
import org.firstinspires.ftc.teamcode.utils.AdditiveLogger;

public class SoundSystem {
    final AndroidSoundPool asp;
    final SoundPlayer player;
    final AdditiveLogger logger;

    public SoundSystem(SoundPlayer player, Subsystems subsystems) {
        asp = new AndroidSoundPool();
        this.player = player;
        asp.initialize(player);
        logger = subsystems.getLogger();
        logger.Log("Soundsystem enabled");
    }

    public void setVolume(float vol) {
        asp.setVolume(vol);
    }

    public void play(String sound) {
        asp.play(sound);
        logger.Log("Sound played: " + sound);
    }
}
