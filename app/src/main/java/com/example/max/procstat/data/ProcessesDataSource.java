package com.example.max.procstat.data;

import java.util.List;

import io.reactivex.Maybe;

public interface ProcessesDataSource {
    Maybe<List<ProcessInfo>> getProcesses();
    Maybe<ProcessInfo> getProcess(String packageName);
    Maybe<ProcessNetworkStat> getProcessNetworkStat(String packageName);
}
