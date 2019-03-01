package com.cui.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {
    @Override
    public void onCreate()
    {
        Log.d("aidl", "aidlService--------------onCreate");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub()
    {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getPid()
        {
            return this.getPid();
        }

        @Override
        public String getName() throws RemoteException
        {
            return "cuiqingchao";
        }

//        public Data getData() throws RemoteException
//        {
//            Data data = new Data();
//            data.id = Process.myPid();
//            data.name = "go or not go is a problem";
//            return data;
//        }
    };

}
