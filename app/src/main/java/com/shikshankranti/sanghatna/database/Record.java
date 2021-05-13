package com.shikshankranti.sanghatna.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = "record")
public class Record {
    private static final String PATIENT_ID = "PatientID";

    @DatabaseField(generatedId = true, columnName = "_id")
    public int ID;

    @DatabaseField(columnName = "deviceid")
    public String DEVICEID;

    @DatabaseField(columnName = "devicetype")
    public int DEVICE_TYPE;

    @DatabaseField(columnName = "recordID")
    public int RecordID;

    @DatabaseField(columnName = "ecgfile")
    public String ECGFILE;


   // @DatabaseField(columnName = "devicerecordeddt", dataType = DataType.DATE_STRING,
     //       format = "MM/dd/yyyy HH:mm:ss")
    //public Date DEVICE_RECORDED_DATETIME;
   @DatabaseField(columnName = "devicerecordeddt")
   public String DEVICE_RECORDED_DATETIME;

    @DatabaseField(columnName = "uploaded")
    public int ISUPLOADED;

    @DatabaseField(columnName = "corrupted")
    public int ISCORRUPTED;

    @DatabaseField(columnName = "temperature")
    public String TEMPRATURE;

    @DatabaseField(columnName = "isviewed") // as treated gain value plz make it sure while using this value
    public int ISVIEWED;

    @DatabaseField(columnName = "comments")
    public String COMMENTS;
    @DatabaseField(columnName = "interpretation")
    public String INTERPRETATION;

    @DatabaseField(columnName = "pulserate")
    public String PULSERATE;
    @DatabaseField(columnName = "spo2")
    public String SPO2;
    @DatabaseField(columnName = "systolic")
    public String SYSTOLIC;
    @DatabaseField(columnName = "diastolic")
    public String DIASTOLIC;

    @DatabaseField(columnName = "glucose")
    public String GLUCOSE;

    @DatabaseField(columnName = "bodyfat")
    public String BODYFAT;

    @DatabaseField(columnName = "bodywater")
    public String BODYWATER;

    @DatabaseField(columnName = "bonemass")
    public String BONEMASS;

    @DatabaseField(columnName = "musclemass")
    public String MUSCLEMASS;

    @DatabaseField(columnName = "analysis")
    public String ANALYSIS;

    @DatabaseField(columnName = "bmi")
    public String BMI;


    @DatabaseField(columnName = "Height")
    public String _height;

    @DatabaseField(columnName = "Weight")
    public String _weight;
    @DatabaseField(columnName = "CreatedDate", dataType = DataType.DATE_STRING, format = "MM/dd/yyyy HH:mm:ss")
    public Date _createddate;

    public String get_weight() {
        return _weight;
    }
    public String get_height() {
        return _height;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }
    public void set_height(String _height) {
        this._height = _height;
    }

    // Foreign Key Collector_ID
    @DatabaseField(foreign = true, columnName = PATIENT_ID, foreignAutoCreate = true, foreignAutoRefresh = true, columnDefinition = "integer references  PatientDetails(PatientID)")
    private PatientDetailsCL patientDetails;

    public Record() {

    }

    public Record(PatientDetailsCL patientDetails) {
        this.patientDetails = patientDetails;
    }

    public PatientDetailsCL getPatientDetails( ) {
        return this.patientDetails;
    }

    public void setPatientDetails(PatientDetailsCL patientDetails) {
        this.patientDetails = patientDetails;
    }

}

