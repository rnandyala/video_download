package com.example.downloadvideopoc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.IntentService;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Config;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnaltics;

    private Button mDownloadVideo;
    private Button mException;
    DownloadManager.Request request;

    private long mDownloadID;
    DownloadManager mDownloadManager;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mException = findViewById(R.id.RTE);

        mException.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
int i = 1/0;
                }
                catch (Exception ex){
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(Build.VERSION.SDK_INT));
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,Build.MODEL);
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "stack message: "+ex.getMessage());
                    mFirebaseAnaltics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                }
            }
        });

        mFirebaseAnaltics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Integer.toString(Build.VERSION.SDK_INT));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,Build.MODEL);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "video download");
        mFirebaseAnaltics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        final String videoUrl = "https://mliquidate.com/pmc2.1qa/portalAPiQA/Uploads/Sample100.mp4";

        mDownloadVideo = findViewById(R.id.mDownloadVideo);

        mDownloadVideo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this,"click event triggered",Toast.LENGTH_SHORT).show();

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "about to start download");
                        mFirebaseAnaltics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

initDownload(videoUrl);
                    }
                }

        );


    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initDownload(String videoUrl) {




File mFile =         new File(
                Environment.getExternalStorageDirectory()
                        + File.separator,
                "VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4");



        Toast.makeText(MainActivity.this,mFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();





  request = new DownloadManager.Request(Uri.parse(videoUrl))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                // Uri of the destination file
                .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,"new.mp4")
                .setTitle("video mliquidate")// Title of the Download Notification
                .setDescription("Downloading")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)// Description of the Download Notification
                .setRequiresCharging(false)// Set if charging is required to begin the download
                .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                .setAllowedOverRoaming(true);// Set if download is allowed on roaming network


        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            execAsync();
            }}, 5000
        );
//                mDownloadID = mDownloadManager.enqueue(request);




        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, String.valueOf(System.currentTimeMillis()));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "call executed");
        mFirebaseAnaltics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    public void execAsync(){

        Toast.makeText(this, "async download manager executed", Toast.LENGTH_LONG).show();

        try {
            mDownloadID = mDownloadManager.enqueue(request);
        }
        catch (Exception ex){

            AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                    .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                    .setTitle("async exception")
//set message
                    .setMessage(ex.getMessage())
//set positive button
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what would happen when positive button is clicked
                            finish();
                        }
                    }).show();
//set negative button

        }

    }



}

