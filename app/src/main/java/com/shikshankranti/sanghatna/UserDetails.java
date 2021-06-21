package com.shikshankranti.sanghatna;

import com.google.firebase.database.IgnoreExtraProperties;
@IgnoreExtraProperties
public class UserDetails {

    public String UserID;
    public String MemberID;
    public String Name;
    public String Number;
    public String Address;
    public String DOB;
    public String Dist;
    public String Tal;
    public String PinCode;
    public String Photopath;


    public UserDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserDetails(String userid, String memberid,String name,String number,String address,String dob,String dist,String tal,String pincode,String photopth) {
        this.UserID = userid;
        this.MemberID = memberid;
        this.Name=name;
        this.Number=number;
        this.Address=address;
        this.DOB=dob;
        this.Dist=dist;
        this.Tal=tal;
        this.PinCode=pincode;
        this.Photopath=photopth;

    }

}