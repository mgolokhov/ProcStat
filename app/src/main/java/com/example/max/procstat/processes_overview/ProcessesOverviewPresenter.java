package com.example.max.procstat.processes_overview;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.max.procstat.App;
import com.example.max.procstat.data.ProcessesDataSource;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


@InjectViewState
public class ProcessesOverviewPresenter extends MvpPresenter<ProcessesOverviewContract.View> implements ProcessesOverviewContract.Presenter {
    @Inject
    ProcessesDataSource processesRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ProcessesOverviewPresenter(){
        App.getAppComponent().inject(this);
    }

    @Override
    public void loadProcesses() {
        disposables.add(processesRepository
                .getProcesses()
                .doOnSubscribe(__ -> getViewState().showProgress())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        // success
                        processes -> {
                            // convert to ViewModel
                            getViewState().hideProgress();
                            getViewState().showProcesses(processes);
                        },
                        // error
                        error -> {
                            Timber.e(error);
                            getViewState().hideProgress();
                            getViewState().showError();
                        },
                        // complete
                        () -> {
                            getViewState().hideProgress();
                            Timber.d("completed");
                        }
                ));
    }

    @Override
    public void unsubscribe(){
        disposables.clear();
    }

}
