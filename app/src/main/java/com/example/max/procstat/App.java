package com.example.max.procstat;

import android.app.Application;

import com.example.max.procstat.di.AppComponent;
import com.example.max.procstat.di.AppModule;
import com.example.max.procstat.di.DaggerAppComponent;

import timber.log.Timber;

public class App extends Application {
    private static AppComponent sAppComponent;

    public static AppComponent getAppComponent(){
        return sAppComponent;
    }

    @Override
    public void onCreate() {
        sAppComponent  = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            Timber.d("debugging mode");
        }
    }
}
