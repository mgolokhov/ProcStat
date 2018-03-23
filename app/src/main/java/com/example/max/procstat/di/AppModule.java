package com.example.max.procstat.di;

import android.app.AppOpsManager;
import android.app.Application;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Application app) {
        context = app;
    }

    @Provides
    @Singleton
    public Context provideAppContext() {
        return context;
    }

    @Provides
    @Singleton
    public PackageManager providePackageManager(Context context){
        return context.getPackageManager();
    }

    @Provides
    @Singleton
    public NetworkStatsManager provideNetworkStatsManager(Context context){
        return (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
    }
}
