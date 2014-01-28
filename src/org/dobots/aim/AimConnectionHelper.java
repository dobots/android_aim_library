package org.dobots.aim;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * The AIM connection helper provides the communication interface with the
 * dodedodo app. It connects (binds) to the MsgService, registers messengers 
 * for the outgoing ports and receives messengers from incoming ports.
 * 
 * @author dominik
 *
 */
public class AimConnectionHelper {

	private IAimModule mModule;
	
	protected Messenger mToMsgService = null;
	protected final Messenger mFromMsgService = new Messenger(new IncomingMsgHandler());
	protected boolean mMsgServiceIsBound;
	
	Timer mHeartBeatTimer;
	HeartBeatTimerTask mHeartBeatTimerTask;
	
	// key is the name of the port, value is the messenger assigned to that port
	protected HashMap<String, Messenger> mInMessenger = new HashMap<String, Messenger>();
	protected HashMap<String, Messenger> mOutMessenger = new HashMap<String, Messenger>();

	private ServiceConnection mMsgServiceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been established, giving us the service object
			// we can use to interact with the service.  We are communicating with our service through an IDL
			// interface, so get a client-side representation of that from the raw service object.
			mToMsgService = new Messenger(service);
			Message msg = Message.obtain(null, AimProtocol.MSG_REGISTER);
			Bundle bundle = new Bundle();
			bundle.putString("module", mModule.getModuleName());
			bundle.putString("package", mModule.getPackageName());
			bundle.putInt("id", mModule.getModuleId()); // TODO: adjustable id, multiple modules
			msg.setData(bundle);
			msgSend(msg);
			
			for (Map.Entry<String, Messenger> entry : mInMessenger.entrySet()) {
				Message msgPort = Message.obtain(null, AimProtocol.MSG_SET_MESSENGER);
				msgPort.replyTo = entry.getValue();
				Bundle bundlePort = new Bundle();
				bundlePort.putString("module", mModule.getModuleName());
				bundlePort.putString("package", mModule.getPackageName());
				bundlePort.putInt("id", mModule.getModuleId()); 
				bundlePort.putString("port", entry.getKey());
				msgPort.setData(bundlePort);
				msgSend(mToMsgService, msgPort);
			}


			Log.i(mModule.getModuleName(), "Connected to MsgService: " + mToMsgService.toString());
		}
		
		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been unexpectedly disconnected: its process crashed.
			mToMsgService = null;
			Log.i(mModule.getModuleName(), "Disconnected from MsgService");
		}
	};

	// Handle messages from MsgService
	class IncomingMsgHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AimProtocol.MSG_SET_MESSENGER:
				Log.i(mModule.getModuleName(), "set messenger");
				String port = msg.getData().getString("port");
				if (mOutMessenger.containsKey(port)) {
					mOutMessenger.put(port, msg.replyTo);
				}
				break;
			case AimProtocol.MSG_STOP:
				Log.i(mModule.getModuleName(), "stopping");
				mModule.onAimStop();
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}
	
	private class HeartBeatTimerTask extends TimerTask {
		@Override
		public void run() {
			if (mToMsgService != null) {
				Message msg = Message.obtain(null, AimProtocol.MSG_PONG);
				Bundle b = new Bundle();
				b.putString("package", mModule.getPackageName());
				b.putString("module", mModule.getModuleName());
				b.putInt("id", mModule.getModuleId());
				msg.setData(b);
				msgSend(msg);
			}
		}
	}
	
	public AimConnectionHelper(IAimModule module) {
		mModule = module;
		
		mModule.defineInMessenger(mInMessenger);
		mModule.defineOutMessenger(mOutMessenger);
		
		mHeartBeatTimer = new Timer();
		mHeartBeatTimerTask = new HeartBeatTimerTask();
		mHeartBeatTimer.schedule(mHeartBeatTimerTask ,0, 1000);
	}
	
	public void destroy() {
		mHeartBeatTimer.cancel();
		unbindFromMsgService();
	}
	
	public void bindToMsgService() {
		if (!mMsgServiceIsBound) {
			// Establish a connection with the service.  We use an explicit class name because there is no reason to be 
			// able to let other applications replace our component.
			Intent intent = new Intent();
			intent.setClassName("org.dobots.dodedodo", "org.dobots.dodedodo.MsgService");
			mModule.bindService(intent, mMsgServiceConnection, Context.BIND_AUTO_CREATE);
			mMsgServiceIsBound = true;
			Log.i(mModule.getModuleName(), "Binding to msgService");
		}
	}

	public void unbindFromMsgService() {
		if (mMsgServiceIsBound) {
			// If we have received the service, and registered with it, then now is the time to unregister.
			if (mToMsgService != null) {
				Message msg = Message.obtain(null, AimProtocol.MSG_UNREGISTER);
				Bundle bundle = new Bundle();
				bundle.putString("module", mModule.getModuleName());
				bundle.putString("package", mModule.getPackageName());
				bundle.putInt("id", mModule.getModuleId());
				msg.setData(bundle);
				msgSend(msg);
			}
			// Detach our existing connection.
			mModule.unbindService(mMsgServiceConnection);
			mMsgServiceIsBound = false;
			Log.i(mModule.getModuleName(), "Unbinding from msgService");
		}
	}

	// Send a msg to the msgService
	public void msgSend(Message msg) {
		if (!mMsgServiceIsBound) {
			Log.i(mModule.getModuleName(), "Can't send message to service: not bound");
			return;
		}
		try {
			msg.replyTo = mFromMsgService;
			mToMsgService.send(msg);
		} catch (RemoteException e) {
			Log.e(mModule.getModuleName(), "Failed to send msg to service. " + e);
			// There is nothing special we need to do if the service has crashed.
		}
	}

	// Send a msg to some messenger
	public void msgSend(Messenger messenger, Message msg) {
		if (messenger == null || msg == null)
			return;
		try {
			//msg.replyTo = mFromMsgService;
			messenger.send(msg);
		} catch (RemoteException e) {
			Log.e(mModule.getModuleName(), "failed to send msg to service. " + e);
			// There is nothing special we need to do if the service has crashed.
		}
	}

	public Messenger getOutMessenger(String port) {
		// TODO Auto-generated method stub
		return mOutMessenger.get(port);
	}

	public Messenger getInMessenger(String port) {
		// TODO Auto-generated method stub
		return mInMessenger.get(port);
	}
	
	public void sendData(Messenger messenger, Bundle data) {
		Message msgOut = Message.obtain(null, AimProtocol.MSG_PORT_DATA);
		msgOut.setData(data);
		msgSend(messenger, msgOut);
	}

	public void sendData(Messenger messenger, String data) {
		Bundle bundle = AimUtils.encode(data);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, int value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, float value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, int[] value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, int[][] value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, int[][][] value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, float[] value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, float[][] value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}

	public void sendData(Messenger messenger, float[][][] value) {
		Bundle bundle = AimUtils.encode(value);
		sendData(messenger, bundle);
	}
	
}
