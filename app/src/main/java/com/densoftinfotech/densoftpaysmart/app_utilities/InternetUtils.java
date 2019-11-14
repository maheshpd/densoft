package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetUtils {
	private static InternetUtils mInstance; 
	private static Context context;
	
	public InternetUtils(Context con){
		context = con;
	}
	
	public static synchronized InternetUtils getInstance(Context con){
		if(mInstance==null){
			mInstance = new InternetUtils(con);
		}
		return mInstance;
	}
	
	public boolean available(){
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info!=null && info.isConnected()); 
	}
	
}
