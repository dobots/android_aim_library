# Optional Android Library to develop AIM modules

The AimProtocol.java defines the protocol of the messages send between MsgService and AIM modules.

The main elements of this Android library are the following two templates:

- AimService
- AimActivity

these two classes can be used to create AIM modules. They set up the connections to the DoDeDoDo app (MsgService) and handle "port creation"

To create a module, extend one of these two classes (for UI extend AimActivity, for background extend AimService). In addition, the following functions will have to be implemented:

- String getModuleName();
	returns the name used for the AIM module
- void defineInMessenger(HashMap<String, Messenger> list);
	this defines the input port names and a messenger assigned to that port, e.g.
	protected void defineInMessenger(HashMap<String, Messenger> list) {
		list.put("bmp", mPortBmpInMessenger);
	}
- void defineOutMessenger(HashMap<String, Messenger> list);
	this defines the output port names. as messenger a null value can be provided because the messenger will get assigned from the MsgService, e.g.
	protected void defineOutMessenger(HashMap<String, Messenger> list) {
		list.put("jpg", null);
	}
