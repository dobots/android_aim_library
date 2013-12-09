package org.dobots.aim;

import org.dobots.aimlibrary.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Provides a default UI for an AIM module that only runs in the background
 * Includes:
 * 		- status (running / stopped)
 * 		- button start / stop
 * 
 * @author dominik
 *
 */
public class SimpleAimServiceUI extends Activity {

	private static final int WATCHDOG_INTERVAL = 500;

	private static final String TAG = SimpleAimServiceUI.class.getSimpleName();

    private Handler mUiHandler = new Handler();

	private ToggleButton btnStartStop;
	private TextView txtMessageStatus;
	
	private Class<?> mServiceClass;

	/**
	 * Use protected void onCreate(Bundle savedInstanceState, Class<?> serviceClass)
	 */
	@Deprecated
	protected void onCreate(Bundle savedInstanceState) {};
	
	/**
	 * uses the default layout
	 * @param savedInstanceState
	 * @param serviceClass
	 */
	protected void onCreate(Bundle savedInstanceState, Class<?> serviceClass) {
		onCreate(savedInstanceState, serviceClass, R.layout.simple_aim_service);
	}
	
	/**
	 * provide a custom layout. has to include the elements of res/layout/main.xml
	 * @param savedInstanceState
	 * @param serviceClass
	 * @param layoutResID
	 */
	protected void onCreate(Bundle savedInstanceState, Class<?> serviceClass, int layoutResID) {
		super.onCreate(savedInstanceState);

		mServiceClass = serviceClass;

		setLayout(layoutResID);
				
		mUiHandler.postDelayed(mWatchdog, WATCHDOG_INTERVAL);
	}

	protected void setLayout(int layoutResID) {
		setContentView(layoutResID);
		
		btnStartStop = (ToggleButton) findViewById(R.id.btnStartStop);
		btnStartStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!btnStartStop.isChecked()) {
					stopService();
				} else {
					startService();
				}
			}
		});
		
		txtMessageStatus = (TextView) findViewById(R.id.txtModuleStatus);
	}
	

	protected void startService() {
		Intent intent = new Intent(this, mServiceClass);
		intent.putExtra("id", 0); // Default id
		
		// first start to the service ...
		startService(intent);
		Log.i(TAG, "Starting: " + intent.toString());
		
	}

    protected void stopService() {
		// ... then stop the service
		Intent intent = new Intent(this, mServiceClass);
		stopService(intent);
		
		Log.i(TAG, "Stopping service: " + intent.toString());
	}

    private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (mServiceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
    
    private Runnable mWatchdog = new Runnable() {
		
		@Override
		public void run() {
			if (isServiceRunning()){
				txtMessageStatus.setText("Module running");
				btnStartStop.setChecked(true);
			} else {
				txtMessageStatus.setText("Module stopped");
				btnStartStop.setChecked(false);
			}
			mUiHandler.postDelayed(this, WATCHDOG_INTERVAL);
		}
	};

}
