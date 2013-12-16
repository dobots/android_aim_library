package org.dobots.aim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

/**
 * Base class for an AIM UI module (activity).
 * 
 * @author dominik
 *
 */
public abstract class AimActivity extends Activity implements IAimModule {

	// TODO: adjustable id, multiple modules
	protected int mModuleId = 0; 

	protected AimConnectionHelper mAimConnectionHelper;

	@Override
	public int getModuleId() {
		// TODO Auto-generated method stub
		return mModuleId;
	}

	public String getTag() {
		return getModuleName();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		mModuleId = intent.getIntExtra("id", -1);
		
		mAimConnectionHelper = new AimConnectionHelper(this);
		mAimConnectionHelper.bindToMsgService();
		
		Log.i(getTag(),"onCreate");
	}
	
	@Override
	public void onAimStop() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.i(getTag(),"onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(getTag(),"onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(getTag(),"onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.i(getTag(),"onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();		
		mAimConnectionHelper.destroy();
		Log.i(getTag(), "onDestroy ");
	}
	
	public Messenger getOutMessenger(String port) {
		return mAimConnectionHelper.getOutMessenger(port);
	}

	public Messenger getInMessenger(String port) {
		return mAimConnectionHelper.getInMessenger(port);
	}

//	public void msgSend(Messenger messenger, Message msg) {
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

	public void sendData(Messenger messenger, int[] value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, int[][] value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, int[][][] value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, float[] value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, float[][] value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

	public void sendData(Messenger messenger, float[][][] value) {
		mAimConnectionHelper.sendData(messenger, value);
	}

}

