package com.shikshankranti.sanghatna.database;

import android.graphics.Bitmap;
import android.net.Uri;

public class UsersDetails {
    String Name;
    String Address;
    String DOB;
    String PermenantAddress;
    String District;
    String Taluka;
    String PinCode;
    Uri Gallery;
    Bitmap Photo;
    String Id;

    public UsersDetails(String id, String name, String address, String dob, String permenantAddress, String district, String taluka, String pinCode, Uri gallery, Bitmap photo) {
        Id = id;
        Name = name;
        Address = address;
        DOB = dob;
        PermenantAddress = permenantAddress;
        District = district;
        Taluka = taluka;
        PinCode = pinCode;
        Gallery = gallery;
        Photo = photo;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPermenantAddress() {
        return PermenantAddress;
    }

    public void setPermenantAddress(String permenantAddress) {
        PermenantAddress = permenantAddress;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getTaluka() {
        return Taluka;
    }

    public void setTaluka(String taluka) {
        Taluka = taluka;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public Uri getGallery() {
        return Gallery;
    }

    public Bitmap getPhoto() {
        return Photo;
    }

    public void setGallery(Uri gallery) {
        Gallery = gallery;
    }

    public void setPhoto(Bitmap photo) {
        Photo = photo;
    }
}
