package com.jctubino.itunessearch;

import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.lang.Object.*;
import android.os.Handler;

import androidx.annotation.NonNull;

//For background operations
public class AppExecutors {
    private static AppExecutors instance;
    public static AppExecutors getInstance(){
        if(instance==null){
            instance = new AppExecutors();
        }
        return instance;
    }

    //Responsible for updating, deleting, inserting, reading from cache
    private final Executor mDiskIO = Executors.newSingleThreadExecutor();
    private final Executor mMainThreadExecutor= new MainThreadExecutor();

    public Executor diskIO(){
        return mDiskIO;
    }

    public Executor mainThread(){
        return mMainThreadExecutor;
    }

    //post things to main thread
    private static class MainThreadExecutor implements Executor{
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
