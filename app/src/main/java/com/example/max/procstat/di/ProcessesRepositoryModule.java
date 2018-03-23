package com.example.max.procstat.di;

import com.example.max.procstat.data.ProcessesDataRepository;
import com.example.max.procstat.data.ProcessesDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProcessesRepositoryModule {
    @Provides
    @Singleton
    public ProcessesDataSource providesProcessesDataSource(ProcessesDataRepository processesDataRepository){
        return processesDataRepository;
    }
}
