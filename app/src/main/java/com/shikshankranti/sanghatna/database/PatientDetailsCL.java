package com.shikshankranti.sanghatna.database;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by aakashj on 8/spoinstruct/15.
 */

@DatabaseTable(tableName = "PatientDetails")
public class PatientDetailsCL implements Serializable {

    private long date;
    @DatabaseField(generatedId = true, columnName = "_id")
    private int ID;
    @DatabaseField(columnName = "PatientID")
    public String PATIENT_ID;

    @DatabaseField(columnName = "PatientName")
    public String _patientName;
    @DatabaseField(columnName = "LastName")
    private String _lastName;
    @DatabaseField(columnName = "Gender")
    public String _gender;
    @DatabaseField(columnName = "Age")
    public String _age;
    @DatabaseField(columnName = "RegistrationID")
    private String _registrationID;
    @DatabaseField(columnName = "Referencedoctor")
    private String _referenceDoctor;

    @DatabaseField(columnName = "Consultantdoctor")
    private String _consultantdoctor;

    @DatabaseField(columnName = "HospitalName")
    private String _HospitalName;

    @DatabaseField(columnName = "Mobile")
    public String _mobile;


    @DatabaseField(columnName = "History")
    private String _history;

    @DatabaseField(columnName = "Symptoms")
    private String _symptoms;


    @DatabaseField(columnName = "CreatedDate", dataType = DataType.DATE_STRING, format = "MM/dd/yyyy HH:mm:ss")
    private Date _createddate;

    @DatabaseField(columnName = "imageBytes", dataType = DataType.BYTE_ARRAY)
    public byte[] imageBytes;

    @DatabaseField(columnName = "modifieddate")
    public String ModifiedDate;

    @DatabaseField(columnName = "uploaded")
    public int ISUPLOADED;

    @DatabaseField(columnName = "corrupted")
    public int ISCORRUPTED;

 /*   @DatabaseField(columnName = "temperature")
    public String TEMPRATURE;

    @DatabaseField(columnName = "bodywater")
    public String BODYWATER;

    @DatabaseField(columnName = "isviewed") // as treated gain value plz make it sure while using this value
    public int ISVIEWED;

    @DatabaseField(columnName = "comments")
    public String COMMENTS;
    @DatabaseField(columnName = "interpretation")
    public String INTERPRETATION;

    @DatabaseField(columnName = "pulserate")
    public String PULSERATE;
    @DatabaseField(columnName = "systolic")
    public String SYSTOLIC;
    @DatabaseField(columnName = "diastolic")
    public String DIASTOLIC;

    @DatabaseField(columnName = "glucose")
    public String GLUCOSE;

    @DatabaseField(columnName = "bodyfat")
    public String BODYFAT;

    @DatabaseField(columnName = "bonemass")
    public String BONEMASS;

    @DatabaseField(columnName = "musclemass")
    public String MUSCLEMASS;

    @DatabaseField(columnName = "analysis")
    public String ANALYSIS;

    @DatabaseField(columnName = "bmi")
    public String BMI;
*/

    @DatabaseField(columnName = "Height")
    private String _height;

    @DatabaseField(columnName = "Weight")
    private String _weight;

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


    public PatientDetailsCL() {
    }

    public void setChecked(boolean isChecked) {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String get_patientName() {
        return _patientName;
    }

    public void set_patientName(String _patientName) {
        this._patientName = _patientName;
    }

    public String getPATIENT_ID() {

        return PATIENT_ID;
    }

    public void setPATIENT_ID(String PATIENT_ID) {
        this.PATIENT_ID = PATIENT_ID;
    }


    public String get_lastName() {
        return _lastName;
    }

    public void set_lastName(String _lastName) {
        this._lastName = _lastName;
    }

    public String get_gender() {
        return _gender;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }

    public String get_age() {
        return _age;
    }


    public void set_age(String _age) {
        this._age = _age;
    }

    public String get_registrationID() {
        return _registrationID;
    }

    public void set_registrationID(String _registrationID) {
        this._registrationID = _registrationID;
    }

    public String get_referenceDoctor() {
        return _referenceDoctor;
    }

    public void set_referenceDoctor(String _referenceDoctor) {
        this._referenceDoctor = _referenceDoctor;
    }

    public String get_consultantdoctor() {
        return _consultantdoctor;
    }

    public void set_consultantdoctor(String _consultantdoctor) {
        this._consultantdoctor = _consultantdoctor;
    }

    public String get_HospitalName() {
        return _HospitalName;
    }

    public void set_HospitalName(String _HospitalName) {
        this._HospitalName = _HospitalName;
    }

    public String get_history() {
        return _history;
    }

    public void set_history(String _history) {
        this._history = _history;
    }


    public String get_symptoms() {
        return _symptoms;
    }

    public void set_symptoms(String _symptoms) {
        this._symptoms = _symptoms;
    }


    public Date get_createddate() {
        return _createddate;
    }

    public void set_createddate(Date _createddate) {
        this._createddate = _createddate;
    }


    @NonNull
    @Override
    public String toString() {
        return "PatientDetailsCL{" +

                "date=" + date +
                ", ID=" + ID +
                ", _patientName='" + _patientName + '\'' +
                ", PATIENT_ID='" + PATIENT_ID + '\'' +
                ", _lastName='" + _lastName + '\'' +
                ", _gender='" + _gender + '\'' +
                ", _age=" + _age +
                ", _registrationID='" + _registrationID + '\'' +
                ", _referenceDoctor='" + _referenceDoctor + '\'' +
                ", _consultantdoctor='" + _consultantdoctor + '\'' +
                ", _HospitalName='" + _HospitalName + '\'' +
                ", _history='" + _history + '\'' +
                ", _symptoms='" + _symptoms + '\'' +
                ", _createddate=" + _createddate +
                '}';
    }
}
