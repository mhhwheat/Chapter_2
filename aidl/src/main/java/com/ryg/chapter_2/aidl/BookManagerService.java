package com.ryg.chapter_2.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/6/25.
 */
public class BookManagerService extends Service {
    private static final String TAG="BMS";

    private AtomicBoolean mIsServiceDestroyed=new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList=new CopyOnWriteArrayList<Book>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList=new RemoteCallbackList<IOnNewBookArrivedListener>();

    private Binder mBinder=new IBookManager.Stub(){

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.unregister(listener);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android"));
        mBookList.add(new Book(2,"Ios"));

        new Thread(new ServiceWorker()).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestroyed.set(true);
        super.onDestroy();
    }

    private void onNewBookArrived(Book book) throws RemoteException{
        mBookList.add(book);
        final int N=mListenerList.beginBroadcast();
        for(int i=0;i<N;i++){
            IOnNewBookArrivedListener l=mListenerList.getBroadcastItem(i);
            if(l!=null){
                try {
                    l.onNewBookArrived(book);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }

        mListenerList.finishBroadcast();
    }

    private class ServiceWorker implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestroyed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                int bookId=mBookList.size()+1;
                Book newBook=new Book(bookId,"new book#"+bookId);
                try {
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
