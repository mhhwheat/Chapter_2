package com.styleflying.AIDL;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.styleflying.R;


/**
 * Created by Administrator on 2016/6/19.
 */
public class mAIDLActivity extends Activity {
    private static final String TAG = "AIDLActivity";
    private Button btnOK;
    private Button btnCancel;
    private Button btnCallBack;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private void Log(String str) {
        Log.d(TAG, "--------------" + str + "------------");
    }

    mInterface mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log("connect service");
            mService = mInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log("disconnect service");
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOK = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCallBack = (Button) findViewById(R.id.btn_callback);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Intent intent = new Intent("com.styleflying.AIDL.service");
                intent.putExtras(args);
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                Log("OK button click");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(mConnection);
            }
        });

        btnCallBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(TAG, "current Thread Id =" + Thread.currentThread().getId());
                    mService.invokTest();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
