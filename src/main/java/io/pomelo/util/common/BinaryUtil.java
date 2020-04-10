package io.pomelo.util.common;

import java.util.Arrays;

/**
 * <p>二进制工具<br>Binary util</p>
 * @ClassName BinaryUtil.java
 * @author PomeloMan
 */
public class BinaryUtil {

	/**
	 * <p>
	 * 获取参数二进制位值=1的索引
	 * <br>
	 * Gets the index of the parameter binary bit value = 1
	 * </p>
	 * <pre>
	 * 1 -> 1 -> [0]
	 * 2 -> 10 -> [1]
	 * 3 -> 11 -> [0,1]
	 * 4 -> 100 -> [2]
	 * 5 -> 101 -> [0,2]
	 * </pre>
	 * @param num
	 * @return
	 */
	public static int[] toIndexArray(int num) {
		String binaryString = Integer.toString(num, 2);
		int[] indexArray = new int[binaryString.length()];
		int j = 0;
		for (int i = 0; i < binaryString.length(); i++) {
			if (((num >> i) & 1) == 1) {
				indexArray[j++] = i;
			}
		}
		return Arrays.copyOf(indexArray, j);
	}

	/**
	 * <p>
	 * 获取二进制和为输入值的集合
	 * <br>
	 * Get a collection of binary sum = input
	 * </p>
	 * <pre>
	 * 1 -> 2º -> [1]
	 * 2 -> 2¹ -> [2]
	 * 3 -> 2º+2¹ -> [1,2]
	 * 4 -> 2² -> [4]
	 * 5 -> 2º+2² -> [1,4]
	 * 6 -> 2¹+2² -> [2,4]
	 * </pre>
	 * @param num
	 * @return
	 */
	public static int[] toBinaryArray(int num) {
		String binaryString = Integer.toString(num, 2);
		int[] indexArray = new int[binaryString.length()];
		int j = 0;
		for (int i = 0; i < binaryString.length(); i++) {
			if (((num >> i) & 1) == 1) {
				indexArray[j++] = Double.valueOf(Math.pow(2, i)).intValue();
			}
		}
		return Arrays.copyOf(indexArray, j);
	}

}
