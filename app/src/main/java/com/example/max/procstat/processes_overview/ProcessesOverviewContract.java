package com.example.max.procstat.processes_overview;

import com.arellomobile.mvp.MvpView;
import com.example.max.procstat.data.ProcessInfo;

import java.util.List;

public interface ProcessesOverviewContract{
    interface Presenter {
        void loadProcesses();
        void unsubscribe();
    }

    interface View extends MvpView{
        void showProgress();
        void hideProgress();
        void showError();
        void showProcesses(List<ProcessInfo> processes);
    }
}
