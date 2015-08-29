package com.smcl.test.movinglinetest;

import android.os.SystemClock;

import android.util.Log;

public class FPSCounter {
    private static int frames = 0;
    private static long startTimeMillis = 0;
    private static final long interval = 1000;
    private static int sumFrames = 0;
    private static long startTime = 0;

    public static void tick() {
        sumFrames++;
        if (startTime == 0) {
            startTime = SystemClock.uptimeMillis();
        }

        ++frames;
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTimeMillis >= interval) {
            int fps = (int) (frames * 1000 / interval);
            Log.v("FPSCount:LineTest", "FPS : " + fps);
            frames = 0;
            startTimeMillis = currentTime;
        }
    }

    public static void count() {
        long duration = SystemClock.uptimeMillis() - startTime;
        float avgFPS = sumFrames / (duration / 1000f);

        Log.v("FPSCounter",
                String.format("total frames = %d, total elapsed time = %d ms, average fps = %f",
                sumFrames, duration, avgFPS));
    }

    public static void pause() {
        startTime = 0;
        sumFrames = 0;
    }
}
