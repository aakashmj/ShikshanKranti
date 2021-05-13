package com.shikshankranti.sanghatna.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Created by manishak on 8/spoinstruct/15.
 */
public class ProfileDetailsAdapter {
    private Dao<ProfileDetailsCL, Integer> profiledetailsDao = null;
    private final Context context;
    private DatabaseHelper dbHelper;
    private static final String NAME_PATIENT = "PatientName";
    private static final String KEY_PATIENT_ID = "_id";
    private static final String KEY_CREATED_DATE = "CreatedDate";


    public ProfileDetailsAdapter(Context context) {
        this.context = context;
    }

    private void open() {
        try {
            profiledetailsDao = getHelper().getProfileDetailsDao();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }


    /**
     * You'll need this in your class to get the helper from the manager once
     * per class.
     */
    private DatabaseHelper getHelper() {
        if (dbHelper == null) {
            dbHelper = DatabaseHelper.getHelper(this.context);
        }
        return dbHelper;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }
    }


    public ArrayList<ProfileDetailsCL> GetAllProfileDetails() {
        ArrayList<ProfileDetailsCL> ProfileDetailsArrayList = null;
        try {
            if (profiledetailsDao == null)
                open();
            QueryBuilder<ProfileDetailsCL, Integer> qb = profiledetailsDao
                    .queryBuilder();


            // prepare our sql statement
            PreparedQuery<ProfileDetailsCL> preparedQuery;

            preparedQuery = qb.prepare();

            // query for all accounts that have "qwerty" as a password
            ProfileDetailsArrayList = (ArrayList<ProfileDetailsCL>) profiledetailsDao.query(preparedQuery);

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return ProfileDetailsArrayList;

    }

    public ArrayList<ProfileDetailsCL> GetAllProfileDetails_By_DateTime() {
        ArrayList<ProfileDetailsCL> ProfileDetailsArrayList = null;
        try {
            if (profiledetailsDao == null)
                open();
            QueryBuilder<ProfileDetailsCL, Integer> qb = profiledetailsDao
                    .queryBuilder();

            qb.orderBy(KEY_CREATED_DATE,false);

            // prepare our sql statement
            PreparedQuery<ProfileDetailsCL> preparedQuery;

            preparedQuery = qb.prepare();

            // query for all accounts that have "qwerty" as a password
            ProfileDetailsArrayList = (ArrayList<ProfileDetailsCL>) profiledetailsDao.query(preparedQuery);

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return ProfileDetailsArrayList;

    }

    public boolean InsertPatientDetails(ProfileDetailsCL profileDetailsCL) {
        boolean retval = false;
        try {
            if (profileDetailsCL == null)
                open();

            retval = profiledetailsDao.create(profileDetailsCL) > 0;
        } catch (SQLException e) {

            e.printStackTrace();

        }

        return retval;
    }

    public void DeletePatient(int rid) {
        DeleteBuilder<ProfileDetailsCL, Integer> deleteBuilder = profiledetailsDao
                .deleteBuilder();
        try {
            deleteBuilder.where().eq("_id", rid);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<ProfileDetailsCL> GetDetailsbyProfileIDBySearchLike(
            String name) {
        ArrayList<ProfileDetailsCL> ProfileDetailsArrayList = null;
        try {
            QueryBuilder<ProfileDetailsCL, Integer> qb = profiledetailsDao
                    .queryBuilder();
            qb.where().like(NAME_PATIENT, "%" + name + "%");
            qb.orderBy(KEY_CREATED_DATE,false);

            // prepare our sql statement
            PreparedQuery<ProfileDetailsCL> preparedQuery;

            preparedQuery = qb.prepare();

            // query for all accounts that have "qwerty" as a password
            ProfileDetailsArrayList = (ArrayList<ProfileDetailsCL>) profiledetailsDao
                    .query(preparedQuery);

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return ProfileDetailsArrayList;

    }


    public ProfileDetailsCL GetDetailsbyProfileDetailsID(int patientID) {
        ProfileDetailsCL profileDetailsCL = null;
        try {
            if (profiledetailsDao == null) open();
            QueryBuilder<ProfileDetailsCL, Integer> qb = profiledetailsDao
                    .queryBuilder();
            qb.where().eq(KEY_PATIENT_ID, patientID);

            // prepare our sql statement
            PreparedQuery<ProfileDetailsCL> preparedQuery;

            preparedQuery = qb.prepare();

            // query for all accounts that have "qwerty" as a password
            ArrayList<ProfileDetailsCL> ProfileDetailsArrayList = (ArrayList<ProfileDetailsCL>) profiledetailsDao
                    .query(preparedQuery);

            if (ProfileDetailsArrayList.size() > 0)
                profileDetailsCL = ProfileDetailsArrayList.get(0);



        } catch (SQLException e) {

            e.printStackTrace();
        }

        return profileDetailsCL;

    }

    public boolean UpdateImage(byte[] value, String patientdetailsid,
                               int isuploaded) {
        try {
            if (profiledetailsDao == null)
                open();

            UpdateBuilder<ProfileDetailsCL, Integer> updateBuilder = profiledetailsDao
                    .updateBuilder();
            // update the password to be "none"
            updateBuilder.updateColumnValue("imageBytes", value);
            updateBuilder.updateColumnValue("modifieddate",
                    Utility.currentUTCDateTime());
            // only update the rows where password is null
            updateBuilder.where().eq(KEY_PATIENT_ID, patientdetailsid);
            updateBuilder.update();
        } catch (Exception e) {

            return false;

        }
        return true;

    }

    // Update the PatientDetails
    public boolean UpdateProfileDetails_Details(ProfileDetailsCL profileDetailsCL) {
        int retval = -1;
        try {
            retval = profiledetailsDao.update(profileDetailsCL);
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return (retval > 0);
    }


}
