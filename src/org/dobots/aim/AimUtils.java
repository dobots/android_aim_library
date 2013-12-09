package org.dobots.aim;

import org.dobots.aim.AimProtocol.AimArrayDimensionException;
import org.dobots.aim.AimProtocol.AimDataTypeException;

import android.os.Bundle;
import android.os.Message;

public class AimUtils {

	// STRING
	
	public static String getStringData(Message msg) throws AimDataTypeException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_STRING) {
			throw new AimDataTypeException();
		}
		return bundle.getString("data");
	}
	
	public static void setData(Message msg, String data) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_STRING);
		bundle.putString("data", data);
		msg.setData(bundle);
	}

	// FLOAT
	
	public static void setData(Message msg, float value) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT);
		bundle.putFloat("data", value);
		msg.setData(bundle);
	}
	
	public static float getFloatData(Message msg) throws AimDataTypeException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_FLOAT) {
			throw new AimDataTypeException();
		}
		return bundle.getFloat("data");
	}
	
	// FLOAT ARRAY

	/// 1 DIMENSION
	public static void setData(Message msg, float[] value) {
		
		float[] aimArray = new float[value.length + 2];
		aimArray[0] = 1; // 1 dimension
		aimArray[1] = value.length; // length of dimension

		int offset = 2;
		System.arraycopy(value, 0, aimArray, offset, value.length);

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		msg.setData(bundle);
	}

	public static float[] get1DFloatArrayData(Message msg) throws AimDataTypeException, AimArrayDimensionException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_FLOAT_ARRAY) {
			throw new AimDataTypeException();
		}

		float[] aimArray = bundle.getFloatArray("data");
		if (aimArray[0] != 1) {
			throw new AimArrayDimensionException();
		}
		
		float[] result = new float[(int)aimArray[1]];
		
		int offset = 2;
		System.arraycopy(aimArray, offset, result, 0, result.length);
		return result;
	}
	
	/// 2 DIMENSIONS
	public static void setData(Message msg, float[][] value) {
		
		float[] aimArray = new float[value.length * value[0].length + 3];
		aimArray[0] = 2; // 1 dimension
		aimArray[1] = value.length; // length of first dimension
		aimArray[2] = value[0].length; // length of second dimension

		int index = 3;
		for (int i = 0; i < value.length; ++i) {
			for (int j = 0; j < value[0].length; ++j) {
				aimArray[index++] = value[i][j];
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		msg.setData(bundle);
	}

	public static float[][] get2DFloatArrayData(Message msg) throws AimDataTypeException, AimArrayDimensionException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_FLOAT_ARRAY) {
			throw new AimDataTypeException();
		}

		float[] aimArray = bundle.getFloatArray("data");
		if (aimArray[0] != 2) {
			throw new AimArrayDimensionException();
		}
		
		float[][] result = new float[(int)aimArray[1]][(int)aimArray[2]];
		
		int index = 3;
		for (int i = 0; i < result.length; ++i) {
			for (int j = 0; j < result[0].length; ++j) {
				result[i][j] = aimArray[index++];
			}
		}
		
		return result;
	}
	
	/// 3 DIMENSIONS
	public static void setData(Message msg, float[][][] value) {
		
		float[] aimArray = new float[value.length * value[0].length * value[0][0].length + 4];
		aimArray[0] = 3; // 1 dimension
		aimArray[1] = value.length; // length of first dimension
		aimArray[2] = value[0].length; // length of second dimension
		aimArray[3] = value[0][0].length; // length of third dimension

		int index = 4;
		for (int i = 0; i < value.length; ++i) {
			for (int j = 0; j < value[0].length; ++j) {
				for (int k = 0; k < value[0][0].length; ++k) {
					aimArray[index++] = value[i][j][k];
				}
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		msg.setData(bundle);
	}

	public static float[][][] get3DFloatArrayData(Message msg) throws AimDataTypeException, AimArrayDimensionException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_FLOAT_ARRAY) {
			throw new AimDataTypeException();
		}

		float[] aimArray = bundle.getFloatArray("data");
		if (aimArray[0] != 3) {
			throw new AimArrayDimensionException();
		}
		
		float[][][] result = new float[(int)aimArray[1]][(int)aimArray[2]][(int)aimArray[3]];
		
		int index = 4;
		for (int i = 0; i < result.length; ++i) {
			for (int j = 0; j < result[0].length; ++j) {
				for (int k = 0; k < result[0][0].length; ++k) {
					result[i][j][k] = aimArray[index++];
				}
			}
		}
		
		return result;
	}
	
	// INTEGER
	
	public static void setData(Message msg, int value) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT);
		bundle.putInt("data", value);
		msg.setData(bundle);
	}
	
	public static int getIntData(Message msg) throws AimDataTypeException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_INT) {
			throw new AimDataTypeException();
		}

		return bundle.getInt("data");
	}
	
	// INTEGER ARRAY
	
	/// 1 DIMENSION
	public static void setData(Message msg, int[] value) {
		
		int[] aimArray = new int[value.length + 2];
		aimArray[0] = 1; // 1 dimension
		aimArray[1] = value.length; // length of dimension
		System.arraycopy(value, 0, aimArray, 2, value.length);

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		msg.setData(bundle);
		
	}

	public static int[] get1DIntArrayData(Message msg) throws AimDataTypeException, AimArrayDimensionException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_INT_ARRAY) {
			throw new AimDataTypeException();
		}

		int[] aimArray = bundle.getIntArray("data");
		if (aimArray[0] != 1) {
			throw new AimArrayDimensionException();
		}
		
		int[] result = new int[aimArray[1]];
		
		int offset = 2;
		System.arraycopy(aimArray, offset, result, 0, result.length);
		return result;
	}
	
	/// 2 DIMENSIONS
	public static void setData(Message msg, int[][] value) {
		
		int[] aimArray = new int[value.length * value[0].length + 3];
		aimArray[0] = 2; // 1 dimension
		aimArray[1] = value.length; // length of first dimension
		aimArray[2] = value[0].length; // length of second dimension

		int index = 3;
		for (int i = 0; i < value.length; ++i) {
			for (int j = 0; j < value[0].length; ++j) {
				aimArray[index++] = value[i][j];
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		msg.setData(bundle);
	}

	public static int[][] get2DIntArrayData(Message msg) throws AimDataTypeException, AimArrayDimensionException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_INT_ARRAY) {
			throw new AimDataTypeException();
		}

		int[] aimArray = bundle.getIntArray("data");
		if (aimArray[0] != 2) {
			throw new AimArrayDimensionException();
		}
		
		int[][] result = new int[aimArray[1]][aimArray[2]];
		
		int index = 3;
		for (int i = 0; i < result.length; ++i) {
			for (int j = 0; j < result[0].length; ++j) {
				result[i][j] = aimArray[index++];
			}
		}
		
		return result;
	}
	
	/// 3 DIMENSIONS
	public static void setData(Message msg, int[][][] value) {
		
		int[] aimArray = new int[value.length * value[0].length * value[0][0].length + 4];
		aimArray[0] = 3; // 1 dimension
		aimArray[1] = value.length; // length of first dimension
		aimArray[2] = value[0].length; // length of second dimension
		aimArray[3] = value[0][0].length; // length of third dimension

		int index = 4;
		for (int i = 0; i < value.length; ++i) {
			for (int j = 0; j < value[0].length; ++j) {
				for (int k = 0; k < value[0][0].length; ++k) {
					aimArray[index++] = value[i][j][k];
				}
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		msg.setData(bundle);
	}

	public static int[][][] get3DIntArrayData(Message msg) throws AimDataTypeException, AimArrayDimensionException {
		Bundle bundle = msg.getData();
		if (bundle.getInt("datatype") != AimProtocol.DATATYPE_FLOAT_ARRAY) {
			throw new AimDataTypeException();
		}

		int[] aimArray = bundle.getIntArray("data");
		if (aimArray[0] != 3) {
			throw new AimArrayDimensionException();
		}
		
		int[][][] result = new int[aimArray[1]][aimArray[2]][aimArray[3]];
		
		int index = 4;
		for (int i = 0; i < result.length; ++i) {
			for (int j = 0; j < result[0].length; ++j) {
				for (int k = 0; k < result[0][0].length; ++k) {
					result[i][j][k] = aimArray[index++];
				}
			}
		}
		
		return result;
	}
	
	//
	
}
