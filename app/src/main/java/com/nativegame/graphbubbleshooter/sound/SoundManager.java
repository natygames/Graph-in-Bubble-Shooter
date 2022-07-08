package com.nativegame.graphbubbleshooter.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.nativegame.graphbubbleshooter.R;

import java.util.HashMap;

public class SoundManager {

    private static final int MAX_STREAMS = 8;

    private final Context mContext;
    private SoundPool mSoundPool;
    private HashMap<SoundEvent, Integer> mSoundsMap;

    public SoundManager(Context context) {
        mContext = context;
        loadSounds();
    }

    private void loadSounds() {
        createSoundPool();
        mSoundsMap = new HashMap<>();
        // loadEventSound(mContext, SoundEvent.PASS_PIPE, R.raw.sfx);
    }

    private void createSoundPool() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        } else {
            // Use SoundPool.Builder on API 21
            // http://developer.android.com/reference/android/media/SoundPool.Builder.html
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(MAX_STREAMS)
                    .build();
        }
    }

    private void loadEventSound(Context context, SoundEvent event, int fileID) {
        int soundId = mSoundPool.load(context, fileID, 1);
        mSoundsMap.put(event, soundId);
    }

    public void playSoundForSoundEvent(SoundEvent event) {
        Integer soundId = mSoundsMap.get(event);
        if (soundId != null) {
            // Left Volume, Right Volume, priority (0 == lowest), loop (0 == no) and rate (1.0 normal playback rate)
            mSoundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void unloadSounds() {
        if (mSoundPool != null) {
            mSoundPool.release();
            mSoundPool = null;
            mSoundsMap.clear();
        }
    }

}
