package com.example.max.procstat.di;

import android.app.Application;

import com.example.max.procstat.process_details.ProcessDetailsActivity;
import com.example.max.procstat.process_details.ProcessDetailsPresenter;
import com.example.max.procstat.processes_overview.ProcessesOverviewPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, ProcessesRepositoryModule.class})
public interface AppComponent {
    void inject(Application application);
    void inject(ProcessesOverviewPresenter presenter);
    void inject(ProcessDetailsPresenter presenter);
}
