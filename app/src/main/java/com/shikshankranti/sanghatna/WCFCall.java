package com.shikshankranti.sanghatna;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shikshankranti.sanghatna.logger.Log4jHelper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class WCFCall {
	public static final String BASE_URL = "/WCFService.svc";
	SharedPreferences sharedpreferences;
	Context context;
	JSONObject json = new JSONObject();
	org.apache.log4j.Logger log = Log4jHelper.getLogger("WCFCall");
	boolean  resp = false;
  	public boolean InsertEcgData2(String Name, String Mobile,
								  String Age, String Gender, String Datetime, String Height,
								  String Weight, String BMI, String BP, String SPO2, String HR, String Temp, String Glucose, String Location, final Context context) {
			sharedpreferences = context.getSharedPreferences("carematepi", 0);
			sharedpreferences.getString("location", "caremate2");
		//String url = null;//http://13.235.130.113:8081";

		URL siteUrl;
		try {
			String	url = "http://13.235.130.113:8081/MobileDataService.svc/UploadCaremateData/?name="+URLEncoder.encode(Name, "UTF-8")+"&mobile="+Mobile+"&age="+URLEncoder.encode(Age, "UTF-8")+"&gender="+URLEncoder.encode(Gender, "UTF-8")+"&dttime="+ URLEncoder.encode(Datetime, "UTF-8")+"&height="+URLEncoder.encode(Height, "UTF-8")+"&weight="+URLEncoder.encode(Weight, "UTF-8")+"&BMI="+ URLEncoder.encode(BMI, "UTF-8")+"&BP="+BP+"&SPO2="+SPO2+"&HR="+HR+"&temp="+Temp+"&glucose="+Glucose+"&location=caremate2";

			siteUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) siteUrl
					.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.connect();
			/*SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			StringBuilder requestParams = new StringBuilder();
			Log.i("upload","WCF Call started");

			requestParams.append("?");
			requestParams.append(URLEncoder.encode("name", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Name, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("mobile", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Mobile, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("age", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(String.valueOf(Age), "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("gender", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Gender, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("dttime", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Datetime, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("height", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Height, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("weight", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Weight, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("BMI", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(BMI, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("BP", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(BP, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("SPO2", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(SPO2, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("HR", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(HR, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("temp", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Temp, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("glucose", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Glucose, "UTF-8"));
			requestParams.append("&");
			requestParams.append(URLEncoder.encode("location", "UTF-8"));
			requestParams.append("=").append(
					URLEncoder.encode(Location, "UTF-8"));
			requestParams.append("&");*/
			//Create a new InputStreamReader
			InputStreamReader streamReader = new
					InputStreamReader(conn.getInputStream());
			//Create a new buffered reader and String Builder
			BufferedReader reader = new BufferedReader(streamReader);
			StringBuilder stringBuilder = new StringBuilder();
			//Check if the line we are reading is not null
			String inputLine;
			while((inputLine = reader.readLine()) != null){
				stringBuilder.append(inputLine);
				if (inputLine.contains("Success")) {
					System.out.println(inputLine);
					resp = true;
					log.info("Upload"+ true);

					break;
				}
			}
			//Close our InputStream and Buffered reader
			reader.close();
			streamReader.close();

			Log.i("upload","WCF all parameters appeded");



		/*	// sends POST data
			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(requestParams.toString());
			out.flush();
			out.close();
			writer.write(requestParams.toString());
           // log.info(requestParams.toString());
			writer.flush();

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				String line = "";
				while ((line = in.readLine()) != null) {
					if (line.matches("Success")) {
						System.out.println(line);
						resp = true;
						log.info("Upload"+ true);

						break;
					}
				}
				in.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				log.info("Upload"+e.getMessage());

			}
	*/		conn.disconnect();

		} catch (Exception e) {
			resp = false;
		}

			Log.i("upload", "upload response");

			return resp;

		}
	}




