package org.dobots.aim;

import org.dobots.aim.AimProtocol.AimDataTypeException;

import android.os.Bundle;
import android.os.Message;

/**
 * 
 * E.g. 
 * 
 * 4 Tensors, 1x 0-order, 1x 1-order, 1x 2-order, 1x 3-order
 * 	0				// single value
 *  1     5			// 1D array / vector
 *  2     2x2		// 2D array / matrix
 *  3     2x3x2		// 3D array / 
 *  
 *  int 		zeroOrder 	= 0;
 *  int[] 		firstOrder 	= { 1, 2, 3, 4, 5};
 *  int[][]		secondOrder = { {1, 2}, {3, 4} };
 *  int[][][]	thirdOrder 	= { { {1, 2}, {3, 4}, {5, 6} }, { {7, 8}, {9, 10}, {11, 12} } };
 *    
 *  int[] aimArray;
 *  int offset = AimUtils.createAimArray(aimArray, [], [5], [2, 2], [2, 3, 2]);
 *  
 *  // add data of 0-order tensor
 *  aimArray[++offset] = zeroOrder;
 *  
 *  // add data of 1-order tensor
 *  for (int i = 0; i < firstOrder.length; ++i) {
 *  	aimArray[++offset] = firstOrder[i];
 *  }
 *  
 *  // add data of 2-order tensor
 *  for (int i = 0; i < secondOrder.length; ++i) {
 *  	for (int j = 0; j < secondOrder[0].length; ++j) {
 *  		aimArray[++offset] = secondOrder[i][j];
 *  	}
 *  }
 *  
 *  // add data of 3-order tensor
 *  for (int i = 0; i < thirdOrder.length; ++i) {
 *  	for (int j = 0; j < thirdOrder[0].length; ++j) {
 *  		for (int k = 0; k < thirdOrder[0][0].length; ++k) {
 *  	  		aimArray[++offset] = thirdOrder[i][j][k];
 *  		}
 *  	}
 *  }
 *  
 * 	Bundle bundle = AimUtils.encode(aimArray);
 * 
 *  sendData(bundle);
 *  
 * 
 * @author Dominik Egger
 *
 */
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
	
	// N DIM ARRAYS
	
	//	ndim  size
	//	0				// single value
	//	1     5			// 1D array / vector
	//	2     2x2		// 2D array / matrix
	//	3     3x4x3		// 3D array / 
	//	
	//	4    0    1 5    2 2 2    3 3 4 3 data
	
	//	public static int getArrayLength(Integer... dimensionSizes) {
	//		
	//		int dimensions = dimensionSizes.length;
	//		int headerLength = 	1 + // 1 array position for the number of tensorsDimensions, here only one tensor possible
	//							1 + // 1 array position for the number of dimensions
	//							dimensions; // 1 array position for each dimension for the size of that dimension;
	//		int dataLength = 1;
	//		for (Integer dimSize : dimensionSizes) {
	//			dataLength *= dimSize;
	//		}
	//		
	//		return headerLength + dataLength;
	//	}
	
	/**
	 * Use this function to create the aim array. it returns the length neccessary for the header
	 * and data elements based on the dimensions and number of tensorsDimensions.
	 * 		The number of arrays defines the number of tensors
	 * 		The number of values inside each array defines the dimension of the tensor
	 * 		The values define the size of the dimension
	 * 
	 * E.g.
	 *  0				// single value
	 * 	1     5			// 1D array / vector
	 * 	2     2x2		// 2D array / matrix
	 * 	3     3x4x3		// 3D array /
	 *  
	 *  getArrayLength([], [5], [2, 2], [3, 4, 3]);
	 *  
	 * @param tensorsDimensions
	 * @return
	 */
	public static int getArrayLength(Integer[]... tensorsDimensions) {
	
		int numOftensorsDimensions = tensorsDimensions.length;
		int headerLength = 	1 + // 1 array position for the number of tensorsDimensions
							numOftensorsDimensions; // 1 array position for the number of dimensions for each tensor
		int dimensions = 0;
		int dataLength = 0;
		for (Integer[] tensor : tensorsDimensions) {
			dimensions += tensor.length;
			
			int localDataLength = 1;
			for (int i = 0; i < tensor.length; ++i) {
				localDataLength *= tensor[i];
			}
			dataLength += localDataLength;
		}
		
		headerLength += dimensions; // 1 array position for each dimension for each tensor for the size of that dimension
		
		return headerLength + dataLength;
		
	}
	
	//	public static void addHeader(float[] array, Integer... dimensionSizes) {
	//		
	//		array[0] = 1;
	//		array[1] = dimensionSizes.length;
	//		
	//		int index = 2;
	//		for (Integer dimSize : dimensionSizes) {
	//			array[index++] = dimSize;
	//		}
	//		
	//	}
	
	/**
	 * Use this function to add the header information necessary for aim arrays
	 * to the given array. create the array using the length returned by
	 * getArrayLength(Integer[]... tensorsDimensions)
	 * 		The number of arrays defines the number of tensors
	 * 		The number of values inside each array defines the dimension of the tensor
	 * 		The values define the size of the dimension
	 * returns the length of the header. start putting value at return value + 1
	 *  
	 * E.g.
	 *  0				// single value
	 * 	1     5			// 1D array / vector
	 * 	2     2x2		// 2D array / matrix
	 * 	3     3x4x3		// 3D array /
	 *  
	 *  addHeader(array, [], [5], [2, 2], [3, 4, 3]);
	 * 
	 * @param array
	 * @param tensorsDimensions
	 * @return
	 */
	public static int addHeader(float[] array, Integer[]... tensorsDimensions) {
		
		array[0] = tensorsDimensions.length; // number of tensorsDimensions
		 
		int index = 1;
		for (Integer[] tensor : tensorsDimensions) {
			array[index++] = tensor.length;	// number of dimensions for this tensor
			
			for (int i = 0; i < tensor.length; ++i) {
				array[index++] = tensor[i]; // size of this dimension
			}
		}
		return index - 1;
	}

	/**
	 * Use this function to add the header information necessary for aim arrays
	 * to the given array. create the array using the length returned by
	 * getArrayLength(Integer[]... tensorsDimensions)
	 * 		The number of arrays defines the number of tensors
	 * 		The number of values inside each array defines the dimension of the tensor
	 * 		The values define the size of the dimension
	 * returns the length of the header. start putting value at return value + 1
	 *  
	 * E.g.
	 *  0				// single value
	 * 	1     5			// 1D array / vector
	 * 	2     2x2		// 2D array / matrix
	 * 	3     3x4x3		// 3D array /
	 *  
	 *  addHeader(array, [], [5], [2, 2], [3, 4, 3]);
	 * 
	 * @param array
	 * @param tensorsDimensions
	 * @return
	 */
	public static int addHeader(int[] array, Integer[]... tensorsDimensions) {

		array[0] = tensorsDimensions.length; // number of tensorsDimensions
		 
		int index = 1;
		for (Integer[] tensor : tensorsDimensions) {
			array[index++] = tensor.length;	// number of dimensions for this tensor
			
			for (int i = 0; i < tensor.length; ++i) {
				array[index++] = tensor[i]; // size of this dimension
			}
		}
		return index - 1;
	}
	
	/**
	 * create an aim array and add the header. returns the length of the header. 
	 * start filling the array with your data at return value + 1
	 * @param array
	 * @param tensorsDimensions
	 * @return
	 */
	public static int createAimArray(int[] array, Integer[]... tensorsDimensions) {
		array = new int[getArrayLength(tensorsDimensions)];
		return addHeader(array, tensorsDimensions);
	}

	/**
	 * create an aim array and add the header. returns the length of the header. 
	 * start filling the array with your data at return value + 1
	 * @param array
	 * @param tensorsDimensions
	 * @return
	 */
	public static int createAimArray(float[] array, Integer[]... tensorsDimensions) {
		array = new float[getArrayLength(tensorsDimensions)];
		return addHeader(array, tensorsDimensions);
	}

	/**
	 * only use arrays created with createAimArray(...), or using the functions
	 * getArrayLength(...) and addHeader(...)
	 * @param array
	 * @return
	 */
	public static Bundle encode(int[] array) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_INT_ARRAY);
		bundle.putIntArray("data", array);
		return bundle;
	}

	/**
	 * only use arrays created with createAimArray(...), or using the functions
	 * getArrayLength(...) and addHeader(...)
	 * @param array
	 * @return
	 */
	public static Bundle encode(float[] array) {
		Bundle bundle = new Bundle();
		bundle.putInt("datatype", AimProtocol.DATATYPE_FLOAT_ARRAY);
		bundle.putFloatArray("data", array);
		return bundle;
	}
		
	
	

}
