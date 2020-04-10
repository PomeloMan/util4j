package io.pomelo.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>加密工具类(MD5|SHA&Salt)<br>Encryption Tools (MD5 | SHA & Salt)</p>
 * @ClassName IrreversibleEncryptor.java
 * @author PomeloMan
 */
public class IrreversibleEncryptor {

	/**
	 * <p>不可逆密码算法MD5-> SHA<br>Algorithm For IrreversibleEncryptor MD5->SHA</p>
	 * @ClassName Algorithm.java
	 * @author PomeloMan
	 */
	public static enum Algorithm {
		MD5, SHA
	}

	/**
	 * <p>加密<br>Encrypt</p>
	 * @param cleartext <p>明文<br>cleartext</p>
	 * @param algorithm <p>算法(MD5|SHA)<br>algorithm(MD5|SHA)</p>
	 * @param salt <p>盐值<br>salt</p>
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String cleartext, Algorithm algorithm,
			String salt) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		return EncryptHelper.byteArrayToHexString(MessageDigest.getInstance(algorithm.toString()).digest(mergeIntoPassword(cleartext, salt).getBytes(EncryptHelper.DEFAULT_CHARSET)));
	}

	/**
	 * <p>加密<br>Encrypt</p>
	 * @param cleartext <p>明文<br>cleartext</p>
	 * @param algorithm <p>算法(MD5|SHA)<br>algorithm(MD5|SHA)</p>
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String cleartext, Algorithm algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return encrypt(cleartext, algorithm, null);
	}

	/**
	 * <p>将明文与盐值合并<br>Merge the plaintext with the salt value</p>
	 * @param cleartext <p>明文<br>cleartext</p>
	 * @param salt <p>盐值<br>salt</p>
	 * @return
	 */
	private static String mergeIntoPassword(String cleartext, String salt) {
		if (salt == null)
			return cleartext;
		return cleartext + "{" + salt + "}";
	}

}