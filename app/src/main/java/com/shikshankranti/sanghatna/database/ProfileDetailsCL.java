package com.shikshankranti.sanghatna.database;

import androidx.annotation.NonNull;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by manishak on 8/spoinstruct/15.
 */

@DatabaseTable(tableName = "ProfileDetails")
public class ProfileDetailsCL implements Serializable {

    private long date;


    @DatabaseField(generatedId = true, columnName = "_id")
    private int ID;
    @DatabaseField(columnName = "PatientID")
    private String PATIENT_ID;

    @DatabaseField(columnName = "PatientName")
    private String _patientName;



    @DatabaseField(columnName = "Name")
    private String _name;

    @DatabaseField(columnName = "Mobile")
    public String _mobile;


    @DatabaseField(columnName = "Gender")
    private String _gender;

    @DatabaseField(columnName = "Age")
    private int _age;



    @DatabaseField(columnName = "Height")
    private String _height;

    @DatabaseField(columnName = "Weight")
    private String _weight;
    @DatabaseField(columnName = "Bmi")
    private String _bmi;

    @DatabaseField(columnName = "Systolic")
    private String _systolic;

    @DatabaseField(columnName = "Diastolic")
    private String _diastolic;

    @DatabaseField(columnName = "PulseRate")
    private String _pulaserate;

    @DatabaseField(columnName = "CreatedDate", dataType = DataType.DATE_STRING, format = "MM/dd/yyyy HH:mm:ss")
    private Date _createddate;

    @DatabaseField(columnName = "imageBytes", dataType = DataType.BYTE_ARRAY)
    public byte[] imageBytes;

    @DatabaseField(columnName = "modifieddate")
    public String ModifiedDate;


    public ProfileDetailsCL() {
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


    public String get_Name() {
        return _name;
    }
    public void set_name(String _lastName) {this._name = _lastName;
    }

    public void set_gender(String _gender) {
        this._gender = _gender;
    }
    public String get_gender() {
        return _gender;
    }

    public void set_weight(String _weight) {
        this._weight = _weight;
    }
    public String get_weight() {
        return _weight;
    }

    public void set_age(int _age) {
        this._age = _age;
    }
    public int get_age() {
        return _age;
    }

    public String get_height() {
        return _height;
    }
    public void set_height(String _height) {this._height = _height;}

    public String get_systolic() {
        return _systolic;
    }
    public void set_systolic(String _systolic) {
        this._systolic = _systolic;
    }

    public String get_diastolic() {return _diastolic;}
    public void set_diastolic(String _diastolic) {
        this._diastolic = _diastolic;
    }

    public String get_bmi() {return _bmi;}
    public void set_bmi(String _bmi) {
        this._bmi = _bmi;
    }

    public String get_pulaserate() {return _pulaserate;}

    public void set_pulserate(String _pulaserate) {
        this._pulaserate = _pulaserate;
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
        return "ProfileDetailsCL{" +

                "date=" + date +
                ", ID=" + ID +
                ", _patientName='" + _patientName + '\'' +
                ", PATIENT_ID='" + PATIENT_ID + '\'' +
                ", _name='" + _name + '\'' +
                ", _gender='" + _gender + '\'' +
                ", _age=" + _age +
                ", _height='" + _height + '\'' +
                ", _weight='" + _weight + '\'' +
                ", _bmi='" + _bmi + '\'' +
                ", _systolic='" + _systolic + '\'' +
                ", _diastolic='" + _diastolic + '\'' +
                ", _pulserate='" + _pulaserate + '\'' +
                ", _createddate=" + _createddate +
                '}';
    }
}
