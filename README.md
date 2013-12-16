# Optional Android Library to develop AIM modules

The main elements of this Android library are the following two templates:

- AimService
- AimActivity

these two classes can be used to create AIM modules. They set up the connections to the Dodedodo app (MsgService) and handle "port creation"

To create a module, extend one of these two classes (for UI extend AimActivity, for background extend AimService). In addition, the following functions will have to be implemented:

- `String getModuleName();` returns the name used for the AIM module
- `void defineInMessenger(HashMap<String, Messenger> list);` this defines the input port names and a messenger assigned to that port, e.g.

			protected void defineInMessenger(HashMap<String, Messenger> list) {
				list.put("bmp", mPortBmpInMessenger);
			}

- `void defineOutMessenger(HashMap<String, Messenger> list);`
	this defines the output port names. as messenger a null value can be provided because the messenger will get assigned from the MsgService, e.g.

			protected void defineOutMessenger(HashMap<String, Messenger> list) {
				list.put("jpg", null);
			}

In addition, both classes provide various functions `void sendData(...)` to simplify sending messages, and the two functions `Messenger getOutMessenger(String port)` and `Messenger getInMessenger(String port)` which take the port name and give back the respective messenger to which the messages should be sent.

## Additional Classes

The AimProtocol.java defines the protocol of the messages sent between MsgService and AIM modules.

AimUtils.java provides functions to encode/extract data in/from a Bundle. 

SimpleAimServiceUI.java provides a simple UI for background modules. The module status is shown (running/stopped) and a button is provided to start/stop the module.