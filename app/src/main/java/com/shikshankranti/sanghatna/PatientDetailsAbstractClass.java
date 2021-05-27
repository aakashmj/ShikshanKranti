package com.shikshankranti.sanghatna;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by aakash on 10/13/2017.
 */

public class PatientDetailsAbstractClass {

	public static String Name="Aakash Jagtap";
	public static String Number="9960201203";
	public static String DOB="04/05/1989";
	public static boolean Register;
	public static String Address="Swami Villa";
	public static String District="Thane";
	public static String Taluka="Shahapur";
	public static String PinCode="421601";
	public static Bitmap Photo ;
	public static Uri GalleryPhoto;
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
