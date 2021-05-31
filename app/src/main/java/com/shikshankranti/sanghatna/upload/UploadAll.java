package com.shikshankranti.sanghatna.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shikshankranti.sanghatna.WCFCall;
import com.shikshankranti.sanghatna.database.PatientDetailsAdapter;
import com.shikshankranti.sanghatna.database.PatientDetailsCL;
import com.shikshankranti.sanghatna.database.Record;
import com.shikshankranti.sanghatna.database.RecordDbAdapter;
import com.shikshankranti.sanghatna.logger.Log4jHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class UploadAll {
    int actualframcount = 0;
    final org.apache.log4j.Logger log = Log4jHelper.getLogger("UploadALL");
    SharedPreferences sharedpreferences;
    public final static String ACTION_MESSAGE_UPLOADALL = "metsl.unowifi.upload.UPLOADALL";
    public static final String MyPREFERENCES = "Caremate Mini";
    String patient_id, patientHeight, patientWeight, patage, patname,fullname,mobile,lastname, local_devicesID ,date_rec,gender,uploadgender;
    String spo2 ,systolic,diastolic,temperature,pulserate,bodyfat,bonemaass,bmi,musclemass,glucose,bodywater;
    int totalframecount = 0;
    PatientDetailsCL patientDetailsCL;
    PatientDetailsAdapter patientDetailsAdapter;

    String device_id;

    String _sp_name;


    public boolean uploadALL(Context context, String deviceid) {

        try {
            RecordDbAdapter recordDbAdapter = new RecordDbAdapter(context);
            recordDbAdapter.open();
            ArrayList<Record> records = recordDbAdapter.getUnUploadedECG();
            recordDbAdapter.close();

            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            if (records != null) {

                for (int i = 0; i < records.size(); i++) {
                    log.info("Uploading " + records.get(i));
                    Date date =new Date();//records.get(i)._createddate;
                    date_rec =  df.format(date) ;
                    patient_id = records.
                            get(i).getPatientDetails().PATIENT_ID;
                    patname = records.get(i).getPatientDetails()._patientName;
                    lastname= records.get(i).getPatientDetails().get_lastName();
                    patage = records.get(i).getPatientDetails().get_age();
                    mobile=records.get(i).getPatientDetails()._mobile;
                    patientHeight=records.get(i)._height;
                    patientWeight=records.get(i)._weight;
                    systolic=records.get(i).SYSTOLIC;
                    diastolic=records.get(i).DIASTOLIC;
                    bmi=records.get(i).BMI;
                    bodyfat=records.get(i).SPO2;
                    spo2=records.get(i).SPO2;
                    if(bodyfat==null){
                        bodyfat="NA";
                    }
                    bodywater=records.get(i).BODYWATER;
                    bonemaass=records.get(i).BONEMASS;
                    glucose=records.get(i).GLUCOSE;
                    pulserate=records.get(i).PULSERATE;
                    temperature=records.get(i).TEMPRATURE;
                    musclemass=records.get(i).MUSCLEMASS;
                    fullname = patname;
                    gender =  String.valueOf(records.get(i).getPatientDetails()._gender);

                    if(gender.contains("FE")) {
                        uploadgender = "F";
                    }else {
                        uploadgender = "M";
                                          }

                    local_devicesID = records.get(i).DEVICEID;
                    boolean result = Uplaodto24X7(context, records.get(i).ECGFILE,date_rec
                    );   // deviceid
                    Log.i("upload","Uploadto24x7 done result is "+result);

                    if (result) {
                        recordDbAdapter.open();
                        recordDbAdapter.SetUploaded(records.get(i).ID);
                        recordDbAdapter.close();

                    }
                }
              return false;
            }else
                return true;

        } catch (Exception e) {
            log.error("exception occured "+e.toString());
          //  Log.e("Error: ", e.getMessage());
             return false;
        }
    }

    public boolean get_UploadStatus(Context context)
    {

        return false;
    }

    private boolean Uplaodto24X7(Context context, String filename1, String record_dt) {

     /*   if (deviceId != null) {
            DevicesDbAdapter adapter = new DevicesDbAdapter(context);
            adapter.open();
            Devices devices = adapter.GetSpBYDeviceID(deviceId);
            _sp_name = devices.SPID;
            adapter.close();

        }
*/
        boolean result = false;
        try {
            // byte[] array = new byte[1024];
            sharedpreferences = context.getSharedPreferences(MyPREFERENCES,
                    Context.MODE_PRIVATE);
          //  device_id = deviceId;
                WCFCall _call = new WCFCall();
                String BP=systolic+diastolic;
                if(systolic.length()==0&diastolic.length()==0){
                    systolic="";
                    diastolic="";
                    BP="";
                }
                result = _call.InsertEcgData2(fullname, mobile,patage,uploadgender, date_rec,patientHeight, patientWeight, bmi,BP,pulserate,pulserate,temperature,glucose,"caremate2", context);

        }catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.e("Error: ", e.getMessage());
        }
        return result;
    }

}
