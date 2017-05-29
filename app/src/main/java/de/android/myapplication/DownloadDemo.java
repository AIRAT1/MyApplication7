package de.android.myapplication;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

public class DownloadDemo extends AbstractPermissionActivity {

    @Override
    protected String[] getDesiredPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    protected void onPermissionDenied() {
        Toast.makeText(this, R.string.msg_sorry, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onReady(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectNetwork()
                .penaltyDeath()
                .build());
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new DownloadFragment()).commit();
        }
    }

    public void viewLog() {
        startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));
    }
}
