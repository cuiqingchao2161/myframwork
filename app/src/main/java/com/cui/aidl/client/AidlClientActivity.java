package com.cui.aidl.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cui.aidl.service.IMyAidlInterface;
import com.cui.mvvmdemo.R;

public class AidlClientActivity extends Activity {
    IMyAidlInterface aidl;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);
        button = findViewById(R.id.aidl_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AidlClientActivity.this,"onClick",Toast.LENGTH_SHORT).show();
                if(aidl != null){
                    try {
                        Toast.makeText(AidlClientActivity.this,"name:"+aidl.getName(),Toast.LENGTH_SHORT).show();
                        Thread.sleep(2000);
                        Toast.makeText(AidlClientActivity.this,"pid:"+aidl.getPid(),Toast.LENGTH_SHORT).show();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        Intent intent = new Intent(this,IMyAidlInterface.class);
//        Intent intent = new Intent();
        bindService(intent, serviceConnection,BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidl = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidl = null;
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
