package com.shikshankranti.sanghatna.application;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.shikshankranti.sanghatna.utils.CrashHandler;


/**application class
 *
 * Created by jianddongguo on 2017/7/20.
 */

public class MyApplication extends MultiDexApplication {
    // File Directory in sd card
    public static final String DIRECTORY_NAME = "USBCamera";

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.init(getApplicationContext(), getClass());
        MultiDex.install(this);
    }
}
