package com.example.max.procstat.process_details;


import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.max.procstat.App;
import com.example.max.procstat.data.ProcessInfo;
import com.example.max.procstat.data.ProcessNetworkStat;
import com.example.max.procstat.data.ProcessesDataSource;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@InjectViewState
public class ProcessDetailsPresenter extends MvpPresenter<ProcessDetailsContract.View> implements ProcessDetailsContract.Presenter {
    @Inject
    ProcessesDataSource processesRepository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ProcessDetailsPresenter() {
        App.getAppComponent().inject(this);
    }

    @Override
    public void loadDetails(final String packageName) {
        Maybe<ProcessNetworkStat> networkStat = processesRepository.getProcessNetworkStat(packageName);
        Maybe<ProcessInfo> info = processesRepository.getProcess(packageName);
        disposables.add(Maybe.zip(
                networkStat, info,
                // get two different data items and convert them to a ViewModel
                (processNetworkStat, processInfo) -> new ViewModel(
                        processInfo.getIconPath(),
                        processInfo.getName(),
                        processNetworkStat.getMobileWifiRx(),
                        processNetworkStat.getMobileWifiTx()))

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .doOnSubscribe(__ -> getViewState().showProgress())
                .subscribe(
                        // success
                        viewModel -> {
                            getViewState().hideProgress();
                            getViewState().showDetails(viewModel);
                        },
                        // error
                        error -> {
                            Timber.e(error);
                            getViewState().hideProgress();
                            getViewState().showError();
                        },
                        // complete
                        () -> Timber.d("done")

                ));
    }

    @Override
    public void unsubscribe() {
        disposables.clear();
    }
}
