package io.pomelo.util.security;

import org.bouncycastle.util.encoders.Base64;

/**
 * <p>加密工具类<br>EncryptUtil</p>
 * Reference {@link DigestUtils.class}
 * @ClassName EncryptHelper.java
 * @author PomeloMan
 */
public class EncryptHelper {

	/**
	 * UTF-8
	 */
	public final static String DEFAULT_CHARSET = "UTF-8";
	/**
	 * <p>16进制字符集<br>Hexadecimal character set</p>
	 */
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

	/**
	 * <p>字节数组转16进制字符串<br>Byte array to hexadecimal string</p>
	 * @param bytes <p>字节数组<br>Byte array</p>
	 * @return String
	 */
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuffer hexStrBuff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			hexStrBuff.append(byteToHexString(bytes[i]));
		}
		return hexStrBuff.toString();
	}

	/**
	 * <p>字节转16进制字符<br>Byte to hexadecimal character</p>
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * <p>Base64编码<br>Base64 encoding</p>
	 * @param bytes
	 * @return
	 */
	public static String encode(byte[] bytes) {
		return Base64.toBase64String(bytes);
	}

	/**
	 * <p>Base64解码<br>Base64 decoding</p>
	 * @param str
	 * @return byte[]
	 */
	public static byte[] decode(String str) {
		return Base64.decode(str);
	}

	/**
	 * <p>转换十六进制字符串为字节数组<br>Convert hexadecimal string to byte array</p>
	 * @param hexstr <p>十六进制字符串<br>Hexadecimal string</p>
	 * @return
	 */
	public static byte[] hexStringToBytes(String hexStr) {
		byte[] b = new byte[hexStr.length() / 2];
		int j = 0;
		for (int i = 0; i < b.length; i++) {
			char c0 = hexStr.charAt(j++);
			char c1 = hexStr.charAt(j++);
			b[i] = (byte) ((parse(c0) << 4) | parse(c1));
		}
		return b;
	}

	/**
	 * <p>转换字符类型数据为整型数据<br>Convert character type data to integer data</p>
	 * @param c
	 * @return
	 */
	public static int parse(char c) {
		if (c >= 'a')
			return (c - 'a' + 10) & 0x0f; // 0x是16进制，a=10，b=11，c=12，d=13，e=14，f=15，所以0x0f等于十进制的15
		if (c >= 'A')
			return (c - 'A' + 10) & 0x0f;
		return (c - '0') & 0x0f;
	}

}
