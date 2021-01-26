package com.skullzbones.novelnation.Utility;

import android.util.Log;

import java.util.concurrent.Executor;

public class SeriesExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        Log.i("TASK R","One Task");
        command.run();
    }
}
