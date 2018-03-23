package com.example.max.procstat.processes_overview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.max.procstat.R;
import com.example.max.procstat.data.ProcessInfo;
import com.example.max.procstat.process_details.ProcessDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProcessesOverviewActivity extends MvpAppCompatActivity implements ProcessesOverviewContract.View{

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    @InjectPresenter
    ProcessesOverviewPresenter processesOverviewPresenter;
    private ProcessesOverviewAdapter processesOverviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processes_everview);
        ButterKnife.bind(this);
        RecyclerView recyclerView = findViewById(R.id.list);
        processesOverviewAdapter = new ProcessesOverviewAdapter(this::navigateToDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(processesOverviewAdapter);
        processesOverviewPresenter.loadProcesses();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        processesOverviewPresenter.unsubscribe();
    }

    private void navigateToDetails(String packageName) {
        Intent i = new Intent(this, ProcessDetailsActivity.class);
        i.putExtra(ProcessDetailsActivity.PACKAGE_NAME_EXTRA, packageName);
        startActivity(i);
    }

    @Override
    public void showProgress() {
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.cannot_load_data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProcesses(List<ProcessInfo> processes) {
        processesOverviewAdapter.setData(processes);
    }

}
