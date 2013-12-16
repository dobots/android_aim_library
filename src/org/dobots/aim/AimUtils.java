package org.dobots.aim;

import org.dobots.aim.AimProtocol.AimArrayDimensionException;
import org.dobots.aim.AimProtocol.AimDataTypeException;
import org.dobots.utilities.log.Loggable;

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
	
	public static Bundle encode(String data) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_STRING);
		bundle.putString("data", data);
		return bundle;
	}

	// FLOAT
	
	public static Bundle encode(float value) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT);
		bundle.putFloat("data", value);
		return bundle;
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
	public static Bundle encode(float[] value) {
		
		float[] aimArray = new float[value.length + 2];
		aimArray[0] = 1; // 1 dimension
		aimArray[1] = value.length; // length of dimension

		int offset = 2;
		System.arraycopy(value, 0, aimArray, offset, value.length);

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		return bundle;
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
	public static Bundle encode(float[][] value) {
		
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
		return bundle;
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
	public static Bundle encode(float[][][] value) {
		
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
		return bundle;
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
	
	public static Bundle encode(int value) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT);
		bundle.putInt("data", value);
		return bundle;
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
	public static Bundle encode(int[] value) {
		
		int[] aimArray = new int[value.length + 2];
		aimArray[0] = 1; // 1 dimension
		aimArray[1] = value.length; // length of dimension
		System.arraycopy(value, 0, aimArray, 2, value.length);

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		return bundle;
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
	public static Bundle encode(int[][] value) {
		
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
		return bundle;
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
	public static Bundle encode(int[][][] value) {
		
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
		return bundle;
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
	
	public static Bundle encode(Integer... values) {

		int length = 1; // 1 field for the number of structures
		length += 2 * values.length; // 2 fields per structure, 1 for number of dim, 1 for length of dim
		length += values.length; // number of elements
		
		int[] aimArray = new int[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 1; // number of dimensions of 'structure'
			aimArray[index++] = 1; // length of the dimension
			
			aimArray[index++] = values[i];
		}
		
//		for (int j = 0; j < value.length; ++j) {
//			for (int k = 0; k < value[0].length; ++k) {
//				for (int l = 0; l < value[0][0].length; ++l) {
//					aimArray[index++] = value[j][k][l];
//				}
//			}
//		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		return bundle;
	}
	
	public static Bundle encode(Integer[]... values) {

		int length = 1; // 1 field for the number of structures
		length += 2 * values.length; // 2 fields per structure, 1 for number of dim, 1 for length of dim
		for (int i = 0; i < values.length; ++i) { // for each structure ...
			length += values[i].length; // .. number of elements
		}
		
		int[] aimArray = new int[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 1; // number of dimensions of 'structure'
			aimArray[index++] = values[i].length; // length of the dimension
			
			for (int j = 0; j < values[i].length; ++j) {
				aimArray[index++] = values[i][j];
			}
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		return bundle;
	}
	
	public static Bundle encode(Integer[][]... values) {

		int length = 1; // 1 field for the number of structures
		length += 3 * values.length; // 2 fields per structure, 1 for number of dim, 1 for each dim
		for (int i = 0; i < values.length; ++i) { // for each structure ...
			length += values[i].length * values[i][0].length; // .. number of elements
		}
		
		int[] aimArray = new int[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 2; // number of dimensions of 'structure'
			aimArray[index++] = values[i].length; // length of the first dimension
			aimArray[index++] = values[i][0].length; // length of the second dimension
			
			for (int j = 0; j < values[i].length; ++j) {
				for (int k = 0; k < values[i][j].length; ++k) {
					aimArray[index++] = values[i][j][k];
				}
			}
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		return bundle;
	}
	
	public static Bundle encode(Integer[][][]... values) {

		int length = 1; // 1 field for the number of structures
		length += 4 * values.length; // 2 fields per structure, 1 for number of dim, 1 for each dim
		for (int i = 0; i < values.length; ++i) { // for each structure ...
			length += values[i].length * values[i][0].length * values[i][0][0].length; // .. number of elements
		}
		
		int[] aimArray = new int[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 3; // number of dimensions of 'structure'
			aimArray[index++] = values[i].length; // length of the first dimension
			aimArray[index++] = values[i][0].length; // length of the second dimension
			aimArray[index++] = values[i][0][0].length; // length of the second dimension
			
			for (int j = 0; j < values[i].length; ++j) {
				for (int k = 0; k < values[i][j].length; ++k) {
					for (int l = 0; k < values[i][j][k].length; ++l) {
						aimArray[index++] = values[i][j][k][l];
					}
				}
			}
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		return bundle;
	}

	//
	
	public static Bundle encode(Float... values) {

		int length = 1; // 1 field for the number of structures
		length += 2 * values.length; // 2 fields per structure, 1 for number of dim, 1 for length of dim
		length += values.length; // number of elements
		
		float[] aimArray = new float[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 1; // number of dimensions of 'structure'
			aimArray[index++] = 1; // length of the dimension
			
			aimArray[index++] = values[i];
		}
		
//		for (int j = 0; j < value.length; ++j) {
//			for (int k = 0; k < value[0].length; ++k) {
//				for (int l = 0; l < value[0][0].length; ++l) {
//					aimArray[index++] = value[j][k][l];
//				}
//			}
//		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		return bundle;
	}
	
	public static Bundle encode(Float[]... values) {

		int length = 1; // 1 field for the number of structures
		length += 2 * values.length; // 2 fields per structure, 1 for number of dim, 1 for length of dim
		for (int i = 0; i < values.length; ++i) { // for each structure ...
			length += values[i].length; // .. number of elements
		}
		
		float[] aimArray = new float[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 1; // number of dimensions of 'structure'
			aimArray[index++] = values[i].length; // length of the dimension
			
			for (int j = 0; j < values[i].length; ++j) {
				aimArray[index++] = values[i][j];
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		return bundle;
	}
	
	public static Bundle encode(Float[][]... values) {

		int length = 1; // 1 field for the number of structures
		length += 3 * values.length; // 2 fields per structure, 1 for number of dim, 1 for each dim
		for (int i = 0; i < values.length; ++i) { // for each structure ...
			length += values[i].length * values[i][0].length; // .. number of elements
		}
		
		float[] aimArray = new float[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 2; // number of dimensions of 'structure'
			aimArray[index++] = values[i].length; // length of the first dimension
			aimArray[index++] = values[i][0].length; // length of the second dimension
			
			for (int j = 0; j < values[i].length; ++j) {
				for (int k = 0; k < values[i][j].length; ++k) {
					aimArray[index++] = values[i][j][k];
				}
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		return bundle;
	}
	
	public static Bundle encode(Float[][][]... values) {

		int length = 1; // 1 field for the number of structures
		length += 4 * values.length; // 2 fields per structure, 1 for number of dim, 1 for each dim
		for (int i = 0; i < values.length; ++i) { // for each structure ...
			length += values[i].length * values[i][0].length * values[i][0][0].length; // .. number of elements
		}
		
		float[] aimArray = new float[length];
		aimArray[0] = values.length; // number of 'structures'
		
		int index = 1;
		for (int i = 0; i < values.length; ++i) {
			aimArray[index++] = 3; // number of dimensions of 'structure'
			aimArray[index++] = values[i].length; // length of the first dimension
			aimArray[index++] = values[i][0].length; // length of the second dimension
			aimArray[index++] = values[i][0][0].length; // length of the second dimension
			
			for (int j = 0; j < values[i].length; ++j) {
				for (int k = 0; k < values[i][j].length; ++k) {
					for (int l = 0; k < values[i][j][k].length; ++l) {
						aimArray[index++] = values[i][j][k][l];
					}
				}
			}
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", aimArray);
		return bundle;
	}

	private static int id = 0;
//	public static Bundle encode(byte[] value) {
//		
//		int[] aimArray = new int[value.length * value[0].length + 3];
//		aimArray[0] = 2; // 1 dimension
//		aimArray[1] = value.length; // length of first dimension
//		aimArray[2] = value[0].length; // length of second dimension
//
//		int index = 3;
//		for (int i = 0; i < value.length; ++i) {
//			for (int j = 0; j < value[0].length; ++j) {
//				aimArray[index++] = value[i][j];
//			}
////			System.arraycopy(value[i], 0, aimArray, index, value[i].length);
////			index += value[i].length;
//		}
//
//		Bundle bundle = new Bundle();
//		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
//		bundle.putIntArray("data", aimArray);
//		return bundle;
//	}
	
	/// 1 DIMENSION
	public static Bundle encode(byte[] value) {
		
		int[] aimArray = new int[value.length + 2];
		aimArray[0] = 1; // 1 dimension
		aimArray[1] = value.length; // length of dimension
		for (int i = 0; i < value.length; ++i) {
			aimArray[i + 2] = value[i];
		}

		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", aimArray);
		return bundle;
	}

}
