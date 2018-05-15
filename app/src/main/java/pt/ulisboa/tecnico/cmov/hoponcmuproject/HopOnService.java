package pt.ulisboa.tecnico.cmov.hoponcmuproject;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.Serializable;

public class HopOnService extends Service {

    HopOnCMUApplication mHopOnApp;
    IBinder mBinder;

    private MainActivity mActivity;
    private String mUsername;

    public HopOnService() {
    }
    public void setUsername(String u) {

        this.mUsername = u;
    }
    public MainActivity getMainActivity() {
        return mActivity;
    }

    public void setMainActivity(MainActivity mActivity) {

        this.mActivity = mActivity;
    }
    @Override
    public void onCreate() {
        mHopOnApp = (HopOnCMUApplication) getApplicationContext();
        mBinder = new WifiBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {

        this.mUsername = mHopOnApp.getUsername();

        return mBinder;
    }


    public class WifiBinder extends Binder implements Serializable {

        public HopOnService getService() {

            return HopOnService.this;
        }
    }

}
