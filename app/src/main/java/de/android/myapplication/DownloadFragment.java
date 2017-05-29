package de.android.myapplication;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DownloadFragment extends Fragment implements View.OnClickListener {
    private DownloadManager mgr = null;
    private long lastDownload = 1L;
    private View query, start = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        View result = inflater.inflate(R.layout.main, parent, false);
        query = result.findViewById(R.id.query);
        query.setOnClickListener(this);
        start = result.findViewById(R.id.start);
        start.setOnClickListener(this);
        result.findViewById(R.id.view).setOnClickListener(this);
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter f = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        f.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        getActivity().registerReceiver(onEvent, f);
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(onEvent);

        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v == query) {
            queryStatus(v);
        } else if (v == start) {
            startDownload(v);
        } else {
            ((DownloadDemo) getActivity()).viewLog();
        }
    }

    private void startDownload(View v) {
        Uri uri = Uri.parse("https://commonsware.com/misc/test.mp4");
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
        DownloadManager.Request req = new DownloadManager.Request(uri);
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Demo")
                .setDescription("Something useful. No really.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "test.mp4");
        lastDownload = mgr.enqueue(req);
        v.setEnabled(false);
        query.setEnabled(true);
    }
}
