package com.example.max.procstat.process_details;

import com.arellomobile.mvp.MvpView;

public interface ProcessDetailsContract {
    interface Presenter {
        void loadDetails(String packageName);
        void unsubscribe();
    }

    interface View extends MvpView {
        void showDetails(ViewModel viewModel);
        void showProgress();
        void hideProgress();
        void showError();
    }
}
