package com.ryg.chapter_2.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ryg.chapter_2.utils.MyUtils;

/**
 * Created by Administrator on 2016/6/23.
 */
public class MessengerService extends Service {

    private static final String TAG="MessengerService";

    private static class  MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case MyUtils.MSG_FROM_CLIENT:
                    Log.i(TAG,"receive msg from client:"+msg.getData().getString("msg"));
                    Messenger replyTo=msg.replyTo;
                    Message replyMessage=Message.obtain(null,MyUtils.MSG_FROM_SERVICE);
                    Bundle bundle=new Bundle();
                    bundle.putString("reply","恩，你的消息我已经收到，稍后会回复你。");
                    replyMessage.setData(bundle);
                    try {
                        replyTo.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger=new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
}
