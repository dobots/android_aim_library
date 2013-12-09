package org.dobots.aim;

import java.util.HashMap;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Messenger;

/**
 * Interface defining an AIM module, which can be a service (background module)
 * or an activity (UI module). 
 * 
 * 
 * @author dominik
 *
 */
public interface IAimModule {

	String getModuleName();
	
	int getModuleId();

	String getTag();
	
	/*
	 * TO BE DEFINED IN THE SUB-CLASSES
	 * 
	 * E.g.
	 * protected void defineInMessenger(HashMap<String, Messenger> list) {
	 * 		list.put("bmp", mPortBmpInMessenger);
	 * }
	 */
	void defineInMessenger(HashMap<String, Messenger> list);

	/*
	 * TO BE DEFINED IN THE SUB-CLASSES
	 * 
	 * E.g.
	 * protected void defineOutMessenger(HashMap<String, Messenger> list) {
	 * 		list.put("jpg", null);
	 * }
	 */
	void defineOutMessenger(HashMap<String, Messenger> list);
	
	// called when the module is stopped
	void onAimStop();
	
	// 
	boolean bindService (Intent service, ServiceConnection conn, int flags);
	
	//
	void unbindService (ServiceConnection conn);

	String getPackageName();
}