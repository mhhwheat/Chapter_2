package com.ryg.chapter_2.aidl.manualbinder;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.ryg.chapter_2.aidl.Book;

import java.util.List;

/**
 * Created by Administrator on 2016/6/20.
 */
public class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl(){
        this.attachInterface(this,DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj){
        if(obj==null){
            return null;
        }
        android.os.IInterface iin=obj.queryLocalInterface(DESCRIPTOR);
        if((iin!=null)&&(iin instanceof IBookManager)){
            return ((IBookManager)iin);
        }

        return new BookManagerImpl.Proxy(obj);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return null;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code){
            case INTERFACE_TRANSACTION:{
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList:{
                data.enforceInterface(DESCRIPTOR);
                List<Book> result=this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook:{
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if(0!=data.readInt()){
                    arg0=Book.CREATOR.createFromParcel(data);
                }else{
                    arg0=null;
                }
                this.addBook(arg0);
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IBookManager{
        private IBinder mRemote;

        Proxy(IBinder remote){
            mRemote=remote;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data=Parcel.obtain();
            Parcel replay=Parcel.obtain();
            List<Book> result;
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList,data,replay,0);
                replay.readException();;
                result=replay.createTypedArrayList(Book.CREATOR);
            }finally {
                replay.recycle();
                data.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data=Parcel.obtain();
            Parcel reply=Parcel.obtain();
            try{
                data.writeInterfaceToken(DESCRIPTOR);
                if(book!=null){
                    data.writeInt(1);
                    book.writeToParcel(data,0);
                }else{
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook,data,reply,0);
                reply.readException();
            }finally {
                reply.recycle();
                data.recycle();
            }
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }
    }
}
