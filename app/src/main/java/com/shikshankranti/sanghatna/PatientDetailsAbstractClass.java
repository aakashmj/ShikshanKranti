package com.shikshankranti.sanghatna;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by aakash on 10/13/2017.
 */

public class PatientDetailsAbstractClass {

	public static String Name="";
	public static String Number="";
	public static String MemberID="";
	public static String DOB="";
	public static boolean Register;
	public static String Address="";
	public static String District="";
	public static String Taluka="";
	public static String PinCode="";
	public static Bitmap Photo=null ;
	public static Uri GalleryPhoto=null;
	public static String PhotoPath = "https://firebasestorage.googleapis.com/v0/b/shikshan-kranti.appspot.com/o/profile.jpg?alt=media&token=78c9ff6f-f845-4b9d-90e0-cc1aa3c813a8";
	public static String GalleryPath="";
	public static Uri CameraURI=null;

	public static boolean Gallery = false;




	public static String Gender = "MALE";




	public static void ClearAllElements() {
		Name = "";
		Number = "";
		DOB = "";
		String lang = "";
		Gender = "Male";
		Register = false;

	}
}