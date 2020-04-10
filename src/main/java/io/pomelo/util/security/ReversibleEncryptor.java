package io.pomelo.util.security;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p>
 * 可逆加密类(DES|AES|DESede) BC秘钥库下载地址 http://www.bouncycastle.org/latest_releases.html<br>
 * 密钥256位解限制jar地址(下载后2个jar文件覆盖 JAVA_HOME/jre/lib/security/) http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * <br>
 * Reversible Encryption Class (DES | AES | DESede) BC keystore download address http://www.bouncycastle.org/latest_releases.html<br>
 * Key 256-bit solution to limit the jar address (after downloading 2 jar files covering JAVA_HOME/jre/lib/security/) http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
 * </p>
 * @ClassName ReversibleEncryptor.java
 * @author PomeloMan
 */
public class ReversibleEncryptor {

	/**
	 * <p>补码<br>Complement code</p>
	 */
	public final static String DEFAULT_IV = "aAbBcCdDeEfFgGhH";
	/**
	 * <p>秘钥库<br>Key storehouse</p>
	 */
	public final static String DEFAULT_SKL = "BC";
	/**
	 * <p>密钥长度<br>Key length</p>
	 */
	public final static int DEFAULT_KEYSIZE = 256;

	/**
	 * <p>算法 DES->DESede->AES<br>Algorithm DES-> DESede-> AES</p>
	 * @ClassName Algorithm.java
	 * @author PomeloMan
	 */
	public static enum Algorithm {

		DES("DES"), AES("AES/CBC/PKCS7Padding"/* 算法/模式/补码方式 */), DESede("DESede");

		private String alias;

		private Algorithm(String alias) {
			this.alias = alias;
		}

		public String getAlias() {
			return alias;
		}
	}

	static {
		/**
		 * <p>引入密钥库<br>Introduce the keystore</p>
		 */
		Security.addProvider(new BouncyCastleProvider()); // BC库
	}

	/**
	 * <p>生成密钥<br>Generate the key</p>
	 * @param algorithm
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static SecretKey generateKey(Algorithm algorithm)
			throws NoSuchAlgorithmException {
		return generateKey(algorithm, null);
	}

	/**
	 * <p>
	 * 	生成密钥<br>
	 *	使用seed初始化密钥生成器 种子一定，生成密钥的值一定，更换种子后生成的密钥值才会不同
	 * <br>
	 * 	Generate the key<br>
	 * 	Use seed to initialize the key generator seed must generate the value of the key must be generated after replacing the seed key value will be different
	 * </p>
	 * @param algorithm
	 * @param seed
	 * @return SecretKey
	 * @throws NoSuchAlgorithmException
	 */
	public static SecretKey generateKey(Algorithm algorithm, String seed)
			throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm.toString());
		SecureRandom sr = new SecureRandom();
		if (StringUtils.isNotEmpty(seed))
			sr.setSeed(seed.getBytes());
		if (algorithm.equals(Algorithm.AES))
			keyGenerator.init(DEFAULT_KEYSIZE, sr);
		else
			keyGenerator.init(sr);
		return keyGenerator.generateKey();
	}

	/**
	 * <p>获取密钥16进制字符串<br>Get the key hexadecimal string</p>
	 * @param algorithm
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSecretKeyHexString(Algorithm algorithm)
			throws NoSuchAlgorithmException {
		return getSecretKeyHexString(algorithm, null);
	}

	/**
	 * <p>获取密钥16进制字符串<br>Get the key hexadecimal string</p>
	 * @param algorithm
	 * @param seed
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getSecretKeyHexString(Algorithm algorithm, String seed)
			throws NoSuchAlgorithmException {
		return EncryptHelper.byteArrayToHexString(generateKey(algorithm, seed).getEncoded());
	}

	/**
	 * <p>加密<br>Encrypt</p>
	 * @param cleartext
	 * @param keyStr
	 * @param algorithm
	 * @return String Base64
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String cleartext, String keyStr,
			Algorithm algorithm) throws UnsupportedEncodingException,
			GeneralSecurityException {
		return encrypt(cleartext, keyStr, algorithm, DEFAULT_SKL, DEFAULT_IV);
	}

	/**
	 * <p>加密<br>Encrypt</p>
	 * @param cleartext
	 * @param keyStr
	 * @param algorithm
	 * @param secretKeyLibrary
	 * @param iv
	 * @return String Base64
	 * @throws UnsupportedEncodingException
	 * @throws GeneralSecurityException
	 */
	public static String encrypt(String cleartext, String keyStr,
			Algorithm algorithm, String secretKeyLibrary, String iv)
			throws GeneralSecurityException, UnsupportedEncodingException {
		return EncryptHelper.encode(encrypt(cleartext.getBytes(EncryptHelper.DEFAULT_CHARSET), keyStr, algorithm, secretKeyLibrary, iv));
	}

	/**
	 * <p>
	 * 按指定加密规则(algorithm)密钥库(secretKeyLibrary)加密数据(data)。<br>
	 * 加密模式默认ECB,无需设置初始化向量IV(Initialization vector),若加密模式为CBC则需要设置IV,不设置,使用随机IV,可通过cipher.getIV获取加密使用的IV
	 * <br>
	 * Encrypt data (data) by the specified encryption key algorithm (secretKeyLibrary). <br>
	 * Encryption mode Default ECB, no need to set Initialization vector IV, if the encryption mode is CBC you need to set IV, do not set, use a random IV, cipher.getIV can be used to obtain the IV
	 * <p>
	 * @param data
	 * @param secretKey
	 * @param algorithm
	 * @return byte[]
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(byte[] data, String keyStr, Algorithm algorithm)
			throws GeneralSecurityException {
		return encrypt(data, keyStr, algorithm, DEFAULT_SKL, DEFAULT_IV);
	}

	/**
	 * <p>
	 * 按指定加密规则(algorithm)密钥库(secretKeyLibrary)加密数据(data)。<br>
	 * 加密模式默认ECB,无需设置初始化向量IV(Initialization vector),若加密模式为CBC则需要设置IV,不设置,使用随机IV,可通过cipher.getIV获取加密使用的IV
	 * <br>
	 * Encrypt data (data) by the specified encryption key algorithm (secretKeyLibrary). <br>
	 * Encryption mode Default ECB, no need to set Initialization vector IV, if the encryption mode is CBC you need to set IV, do not set, use a random IV, cipher.getIV can be used to obtain the IV
	 * <p>
	 * @param data
	 * @param secretKey
	 * @param algorithm
	 * @param secretKeyLibrary
	 * @param iv
	 * @return byte[] 密文字节数组
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(byte[] data, String keyStr,
			Algorithm algorithm, String secretKeyLibrary, String iv)
			throws GeneralSecurityException {
		Cipher cipher = null;
		if (StringUtils.isEmpty(keyStr))
			throw new NullPointerException();
		SecretKey secretKey = getKey(EncryptHelper.hexStringToBytes(keyStr),
				algorithm);
		if (StringUtils.isEmpty(secretKeyLibrary))
			cipher = Cipher.getInstance(algorithm.getAlias());
		else
			cipher = Cipher.getInstance(algorithm.getAlias(), secretKeyLibrary);
		if (StringUtils.isEmpty(iv))
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		else
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,
					new IvParameterSpec(iv.getBytes()));
		return cipher.doFinal(data);
	}

	/**
	 * <p>解密<br>Decrypt</p>
	 * @param ciphertext
	 * @param keyStr
	 * @param algorithm
	 * @return String
	 * @throws GeneralSecurityException
	 */
	public static String decrypt(String ciphertext, String keyStr,
			Algorithm algorithm) throws GeneralSecurityException {
		return decrypt(ciphertext, keyStr, algorithm, DEFAULT_SKL, DEFAULT_IV);
	}

	/**
	 * <p>解密<br>Decrypt</p>
	 * @param ciphertext
	 * @param keyStr
	 * @param algorithm
	 * @param secretKeyLibrary
	 * @param iv
	 * @return String
	 * @throws GeneralSecurityException
	 */
	public static String decrypt(String ciphertext, String keyStr,
			Algorithm algorithm, String secretKeyLibrary, String iv)
			throws GeneralSecurityException {
		return new String(decrypt(EncryptHelper.decode(ciphertext), keyStr, algorithm, secretKeyLibrary, iv));
	}

	/**
	 * <p>解密<br>Decrypt</p>
	 * @param data
	 * @param secretKey
	 * @param algorithm
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String keyStr, Algorithm algorithm)
			throws Exception {
		return decrypt(data, keyStr, algorithm, DEFAULT_SKL, DEFAULT_IV);
	}

	/**
	 * <p>解密<br>Decrypt</p>
	 * @param data
	 * @param secretKey
	 * @param algorithm
	 * @param secretKeyLibrary
	 * @param iv
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(byte[] data, String keyStr,
			Algorithm algorithm, String secretKeyLibrary, String iv)
			throws GeneralSecurityException {
		Cipher cipher = null;
		if (StringUtils.isEmpty(keyStr))
			throw new NullPointerException();
		SecretKey secretKey = getKey(EncryptHelper.hexStringToBytes(keyStr),
				algorithm);
		if (StringUtils.isEmpty(secretKeyLibrary))
			cipher = Cipher.getInstance(algorithm.getAlias());
		else
			cipher = Cipher.getInstance(algorithm.getAlias(), secretKeyLibrary);
		if (StringUtils.isEmpty(iv))
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
		else
			cipher.init(Cipher.DECRYPT_MODE, secretKey,
					new IvParameterSpec(iv.getBytes()));
		return cipher.doFinal(data);
	}

	/**
	 * <p>获取密钥<br>Get the key</p>
	 * @param keyBytes
	 * @param algorithm
	 * @return SecretKey
	 */
	public static SecretKey getKey(byte[] keyBytes, Algorithm algorithm) {
		return new SecretKeySpec(keyBytes, algorithm.toString());
	}

	/**
	 * <p>引入密钥库<br>Introduce the keystore</p>
	 */
	public static void addProvider(Provider provider) {
		Security.addProvider(provider);
	}

}
