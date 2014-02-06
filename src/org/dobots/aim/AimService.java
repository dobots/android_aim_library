package org.dobots.aim;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Base class for an AIM background module (service).
 * 
 * @author dominik
 *
 */
public abstract class AimService extends Service implements IAimModule {

	// TODO: adjustable id, multiple modules
	protected int mModuleId = -1; 
	
	protected AimConnectionHelper mAimConnectionHelper;

	@Override
	public int getModuleId() {
		return mModuleId;
	}

	public String getTag() {
		return getModuleName();
	}
	
	public void onCreate() {
		super.onCreate();
		mAimConnectionHelper = new AimConnectionHelper(this);
		Log.i(getTag(), "onCreate");
	}

	public void onDestroy() {
		super.onDestroy();
		mAimConnectionHelper.destroy();
		Log.i(getTag(), "onDestroy");
	}

	@Override
	public void onAimStop() {
		stopSelf();
	}

	// Called when all clients have disconnected from a particular interface of this service.
	@Override
	public boolean onUnbind(final Intent intent) {
		return super.onUnbind(intent);
	}

	// Deprecated since API level 5 (android 2.0)
	@Override
	public void onStart(Intent intent, int startId) {
		//		handleStartCommand(intent);
		mModuleId = intent.getIntExtra("id", -1);
		Log.d(getTag(), "onStart " + mModuleId);
		start();
	}

	// Called each time a client uses startService()
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//	    handleStartCommand(intent);
		// We want this service to continue running until it is explicitly stopped, so return sticky.
		mModuleId = intent.getIntExtra("id", -1);
		Log.d(getTag(), "onStartCommand " + mModuleId);
		start();
		return START_STICKY;
	}

	public void start() {
		if (mModuleId < 0)
			return;
		
		mAimConnectionHelper.bindToMsgService();
	}
	
	protected Messenger getOutMessenger(String port) {
		return mAimConnectionHelper.getOutMessenger(port);
	}

	protected Messenger getInMessenger(String port) {
		return mAimConnectionHelper.getInMessenger(port);
	}

//	protected void msgSend(Messenger messenger, Message msg) {
//		mAimConnectionHelper.msgSend(messenger, msg);
//	}
	
	public void sendData(Messenger messenger, Bundle data) {
		mAimConnectionHelper.sendData(messenger, data);
	}

	public void sendData(Messenger messenger, String value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, int value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, float value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

}
