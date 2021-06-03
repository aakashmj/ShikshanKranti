package com.shikshankranti.sanghatna.database;

public class UsersDetails {
    String Name;
    String Address;
    String DOB;
    String PermenantAddress;
    String District;
    String Taluka;
    String PinCode;
    String PhotoPath;
    String Id;
    String Number;
    String MemberID;

    public UsersDetails(String id,String memberID, String name,String number, String address, String dob, String district, String taluka, String pinCode,String photopath) {
        Id = id;
        MemberID = memberID;
        Name = name;
        Address = address;
        Number = number;
        DOB = dob;
        District = district;
        Taluka = taluka;
        PinCode = pinCode;
        PhotoPath = photopath;
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

    public String getPhotoPath() {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String memberID) {
        MemberID = memberID;
    }
}