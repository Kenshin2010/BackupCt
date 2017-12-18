package vn.manroid.backupcontact;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by manro on 18/12/2017.
 */

public class App extends Application {
    private static App instance;
    public static Context mContext;

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
//        SugarContext.init(this);
        instance = this;
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        }
//        Timber.i("Creating our Application");
        mContext = getApplicationContext();
    }

    public static App getInstance() {
        return instance;
    }

    public static Context getContext() {
        return mContext;
    }

//    public static boolean hasNetwork() {
//        //error         ========================
//        return instance.checkIfHasNetwork();
//    }

//    public boolean checkIfHasNetwork() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
//        return networkInfo != null && networkInfo.isConnected();
//    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        SugarContext.terminate();
    }
}
