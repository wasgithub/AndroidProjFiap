package br.com.washington.androidprojfiap;

import android.app.Application;
import android.util.Log;

import com.squareup.otto.Bus;

/**
 * Created by washington on 09/09/2017.
 */

public class DogsApplication extends Application {
    private static final String TAG = "DogsApplication";
    private static DogsApplication instance = null;
    private Bus bus = new Bus();

    public static DogsApplication getInstance() {
        return instance; // Singleton
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "DogsApplication.onCreate()");
        // Salva a instância para termos acesso como Singleton
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "DogsApplication.onTerminate()");
    }

    public Bus getBus() {
        return bus;
    }
}
