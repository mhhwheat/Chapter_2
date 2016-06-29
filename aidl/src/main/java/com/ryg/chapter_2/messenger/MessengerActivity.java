package com.ryg.chapter_2.messenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.ryg.chapter_2.aidl.R;
import com.ryg.chapter_2.utils.MyUtils;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MessengerActivity extends Activity {
    private static final String TAG=MessengerActivity.class.getSimpleName();

    private Messenger mService;

    private ServiceConnection mConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService=new Messenger(service);
            Message msg=Message.obtain(null, MyUtils.MSG_FROM_CLIENT);
            Bundle data=new Bundle();
            data.putString("msg","hello, this is client");
            msg.setData(data);
            msg.replyTo=mGetReplyMessenger;

            try {
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Intent intent=new Intent(this,MessengerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    private Messenger mGetReplyMessenger=new Messenger(new MessengerHander());

    private static class MessengerHander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MyUtils.MSG_FROM_SERVICE:
                    Log.i(TAG,"receive msg from service"+msg.getData().getString("reply"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

}
