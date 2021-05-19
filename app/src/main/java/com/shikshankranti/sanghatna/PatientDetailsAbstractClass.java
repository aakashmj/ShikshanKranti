package com.shikshankranti.sanghatna;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;

/**
 * Created by aakash on 10/13/2017.
 */

public class PatientDetailsAbstractClass {

	public static String Name="";
	public static String Number="";
	public static String Age="9";
	public static boolean Register;
	public static String Address="";
	public static String District="";
	public static String Taluka="";
	public static String PinCode="";
	public static Bitmap Photo ;
	public static Uri GalleryPhoto;
	public static boolean Gallery = false;



	public static String Gender = "MALE";




	public static void ClearAllElements() {
		Name = "";
		Number = "";
		Age = "";
		String lang = "";
		Gender = "Male";
		Register = false;

    }
}
