package com.example.max.procstat.process_details;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.max.procstat.App;
import com.example.max.procstat.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class ProcessDetailsActivity extends MvpAppCompatActivity implements ProcessDetailsContract.View {
    public static final String PACKAGE_NAME_EXTRA = "package_name_extra";
    private static final int READ_PHONE_STATE_REQUEST = 42;
    @InjectPresenter
    ProcessDetailsPresenter processDetailsPresenter;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    @BindView(R.id.icon)
    AppCompatImageView icon;
    @BindView(R.id.appName)
    TextView appName;
    @BindView(R.id.networkStatistics)
    TextView networkStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_details);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissions()) {
            requestPermissions();
        } else {
            processDetailsPresenter.loadDetails(getIntent().getStringExtra(PACKAGE_NAME_EXTRA));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        processDetailsPresenter.unsubscribe();
    }

    private boolean hasPermissions() {
        return hasPermissionToReadNetworkHistory() && hasPermissionToReadPhoneStats();
    }

    private void requestPermissions() {
        if (!hasPermissionToReadNetworkHistory()) {
            return;
        }
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
        }
    }


    private boolean hasPermissionToReadPhoneStats() {
        return (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED);
    }


    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                READ_PHONE_STATE_REQUEST);
    }


    private boolean isAppOpsManagerAllowed(String permission){
        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.checkOpNoThrow(
                permission,
                android.os.Process.myUid(),
                getPackageName()
        );
        return (mode == AppOpsManager.MODE_ALLOWED);
    }

    private boolean hasPermissionToReadNetworkHistory() {
        if (isAppOpsManagerAllowed(AppOpsManager.OPSTR_GET_USAGE_STATS)){
            return true;
        }
        requestReadNetworkHistoryAccess();
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }


    @Override
    public void showDetails(ViewModel viewModel) {
        icon.setImageURI(Uri.parse(viewModel.getIconPath()));
        appName.setText(viewModel.getAppName());
        String net_stat_template = getResources().getString(R.string.network_stat_template);
        networkStatistics.setText(String.format(net_stat_template, viewModel.getRx(), viewModel.getTx()));
    }

    @OnClick(R.id.button)
    void openProcessSystemInfo(){
        String packageName = getIntent().getStringExtra(PACKAGE_NAME_EXTRA);
        try {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Timber.e(e);
            // Open a generic Apps page
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);
        }
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
}
