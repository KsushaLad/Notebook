package com.ksusha.coopybook.database;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecuter {
    private static AppExecuter instance;
    private final Executor mainIO; //главный поток
    private final Executor subIO; //второстепенный поток

    public AppExecuter(Executor mainIO, Executor subIO) {
        this.mainIO = mainIO;
        this.subIO = subIO;
    }

    public static AppExecuter getInstance() { //для создания только второго потока
        if (instance == null)
            instance = new AppExecuter(new MainThreadHandler(), Executors.newSingleThreadExecutor()); //создание единичного потока главного и второстепенного
        return instance;
    }

    public static class MainThreadHandler implements Executor { //перенос из второстепенного потока на основной
        private Handler mainHandler = new Handler(Looper.getMainLooper()); //взятие главного потока

        @Override
        public void execute(Runnable command) {
            mainHandler.post(command); //возможность запуска
        }
    }

    public Executor getMainIO() {
        return mainIO;
    }

    public Executor getSubIO() {
        return subIO;
    }
}
