package com.styleflying.AIDL;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2016/6/19.
 */
public class mAIDLService extends Service{

    private static final String TAG="AIDLService";
    private void Log(String str){
        Log.i(TAG,"----------" + str + "----------");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log("service create");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }


    private final mInterface.Stub mBinder= new mInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void invokTest() throws RemoteException {
            Log.e(TAG,"remote call from client!current thread ID="+Thread.currentThread().getId());

        }
    };
}
