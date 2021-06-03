package com.shikshankranti.sanghatna.upload;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.shikshankranti.sanghatna.logger.Log4jHelper;


public class UploderService extends Service {

    public final static String ACTION_UPLOADALL = "com.elr.uploader.action.UPLOADALL";
    public final static String ACTION_UPLOADALL_SERVER =  "com.elr.uploader.action.UPLOADALL.SERVER";
    public final static String ACTION_UPLOADALL_SERVER_ALREADY = "com.elr.uploader.action.UPLOADALL.T0.SERVER.ALREADY";

    // This is the object that receives interactions from clients. See
    // RemoteService for a more complete example.
    private final IBinder _binder = new LocalBinder();


    private long _handlerThreadId;

    // some stuff for the async service implementation - borrowed heavily from
    // the standard IntentService, but that class doesn't offer fine enough
    // control for "foreground" services.
    private volatile Looper _serviceLooper;
    private volatile ServiceHandler _serviceHandler;

    // A bit of a hack to allow global receivers to know whether or not
    // the service is running, and therefore whether to tell the service
    // about some events
    public static boolean IsRunning = false;
    public static UploderService uploderService;
    org.apache.log4j.Logger log = Log4jHelper.getLogger("UploaderService");

    String device_name;
    Handler handler = new Handler();


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                onHandleIntent((Intent) msg.obj, msg.arg1);
            } catch (Exception e) {
                log.error("Error " + e.getMessage());
            }
        }
    }

    // The IntentService(-like) implementation manages taking the
    // intents passed to startService and delivering them to
    // this function which runs in its own thread (so can block
    // Pretty-much everything using the _xmppMgr is here...
    protected void onHandleIntent(final Intent intent, int id) {

        Log.e("getDevice","dID"+device_name);

		/*
         * if (!CoddleOnline.isDatabaseExists()) { //
		 * BluetoothService.stopAllServices(); return; }
		 */

        if (intent == null) {
            log.error("onHandleIntent: Intent null");
            return;
        }
        if (Thread.currentThread().getId() != _handlerThreadId)
            throw new IllegalThreadStateException();
        String a = intent.getAction();

       final Runnable loadRunnable = () -> {

     /*      Navigation_Activity.Upload_PD.setMessage("Uploading to server.....");
           Navigation_Activity.Upload_PD.setCancelable(true);
           Navigation_Activity.Upload_PD.show();
*/
       };

        if (a.equals(ACTION_UPLOADALL)) {

            // ******************first check internet connection then do upload****************************//

            boolean interstatus;
            interstatus = checkInternetconn();
            if (interstatus) {

                handler.post(loadRunnable);
                new Thread(() -> {
                    UploadAll uploadAll = new UploadAll();
                    log.info("UploadAll Function Is Started");
                        boolean status = uploadAll.uploadALL(UploderService.this, device_name);

                    log.info("UploadAll Finished");
                    Intent intent1 = new Intent(ACTION_UPLOADALL_SERVER);
                    sendBroadcast(intent1);
                }).start();   // end of thread

            }

        }
    }
    public boolean checkInternetconn(){

        ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            NetworkInfo netInfo = conMgr != null ? conMgr.getActiveNetworkInfo() : null;

            return netInfo != null;
        }
    }
   /* MaterialDialog mMaterialDialog;

    public void material_dialog() {
        mMaterialDialog = new MaterialDialog(this)
                .setMessage("Please Check internet Connection!!!")
                .setNegativeButton("Exit", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();

                    }
                });

        mMaterialDialog.show();
    }*/

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public UploderService getService() {
            return UploderService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return _binder;
    }

    int statusUploadCounter = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = this.getApplicationContext();
     //   SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
      //  device_name = sharedPref.getString(getString(R.string.Get_DeviceId), "");

        uploderService = this;
        HandlerThread thread = new HandlerThread("MasterCaution.UploderService");
        thread.start();
        _handlerThreadId = thread.getId();
        _serviceLooper = thread.getLooper();
        _serviceHandler = new ServiceHandler(_serviceLooper);
        log.info("Service Created");
        Log.d("Uploadservice", "service created");
        IsRunning = true;

    }

    @Override
    public void onStart(Intent intent, int startId) {

        Context context = this.getApplicationContext();
        //SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //device_name = sharedPref.getString(getString(R.string.Get_DeviceId), "");

        if (intent == null) { // The application has been killed by Android and
            // we try to restart the connection
            try {
                getApplicationContext().startService(new Intent(UploderService.ACTION_UPLOADALL));
                Log.d("Uploadservice", "start call");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        Message msg = _serviceHandler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        _serviceHandler.sendMessage(msg);
        Log.d("Uploadservice", "HAndler called");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        log.info("Service Destroyed");
        IsRunning = false;
        _serviceLooper.quit();

		/*
		 * if (!(MainService.IsRunning)) { CoddleOnline.releaseHelper();
		 * 
		 * }
		 */
        super.onDestroy();
    }

    /**
     * Intent helper functions. As many of our intent objects use a 'message'
     * extra, we have a helper that allows you to provide that too. Any other
     * extras must be set manually
     */
    public static Intent newSvcIntent(Context ctx) {
        return newSvcIntent(ctx, null);
    }

    public static Intent newSvcIntent(Context ctx, String message) {
        Intent i = new Intent(UploderService.ACTION_UPLOADALL, null, ctx, UploderService.class);
        if (message != null) {
            i.putExtra("message", message);
        }
        return i;
    }

    public static UploderService getInstance() {

        if (uploderService == null) {
            return new UploderService();
        } else {
            return uploderService;
        }
    }

}
