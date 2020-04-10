package io.pomelo.util.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * <p>
 * 非对称加密<br>
 * 公钥用于对数据进行加密，私钥对数据进行解密，两者不可逆。公钥和私钥是同时生成的，一一对应。<br>
 * 比如：A拥有公钥，B拥有公钥和私钥。A将数据通过公钥进行加密后，发送密文给B，B可以通过私钥进行解密。 <br>
 * Asymmetric encryption<br>
 * The public key is used to encrypt the data and the private key to decrypt the
 * data, both of which are irreversible. The public key and private key are
 * generated at the same time, one by one. <br>
 * For example: A has a public key, B has a public key and private key. A
 * encrypts the data with the public key, sends the ciphertext to B, and B
 * decrypts it with the private key.
 * </p>
 * 
 * @ClassName RSAUtil.java
 * @author PomeloMan
 */
public class RSAUtil {

	public final static String RSA_ALGORITHM = "RSA";
	public final static int DEFAULT_KEYSIZE = 1024;
	public final static String PRIVATE_KEY = "rsa_private_key.pem";
	public final static String PUBLIC_KEY = "rsa_public_key.pem";

	public enum Key {
		Public, Private
	}

	static {
		/**
		 * <p>
		 * 引入密钥库<br>
		 * Introduce the keystore
		 * </p>
		 */
		Security.addProvider(new BouncyCastleProvider()); // BC库
	}

	/**
	 * <p>
	 * 获取公钥和私钥的密钥对<br>
	 * Obtain the key pair of the public key and the private key
	 * </p>
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		keyPairGenerator.initialize(DEFAULT_KEYSIZE);
		return keyPairGenerator.generateKeyPair();
	}

	/**
	 * 密钥写入文件
	 * 
	 * @param keyPair
	 * @param filePath
	 * @throws IOException
	 */
	public static void generateRSAPrivateKey(KeyPair keyPair, String folder) throws IOException {
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		String privateKeyStr = EncryptHelper.encode(privateKey.getEncoded());
		try (FileWriter fileWriter = new FileWriter(new File(folder, PRIVATE_KEY));
				BufferedWriter writer = new BufferedWriter(fileWriter)) {
			writer.write(privateKeyStr);
		}
	}

	/**
	 * 公钥写入文件
	 * 
	 * @param keyPair
	 * @param filePath
	 * @throws IOException
	 */
	public static void generateRSAPublicKey(KeyPair keyPair, String folder) throws IOException {
		RSAPublicKey privateKey = (RSAPublicKey) keyPair.getPublic();
		String publicKeyStr = EncryptHelper.encode(privateKey.getEncoded());
		try (FileWriter fileWriter = new FileWriter(new File(folder, PUBLIC_KEY));
				BufferedWriter writer = new BufferedWriter(fileWriter);) {
			writer.write(publicKeyStr);
		}
	}

	/**
	 * 从文件中输入流中加载公钥/密钥
	 * 
	 * @param input
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static RSAKey loadKeyByFile(InputStream input, Key key) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(input));
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			String keyStr = sb.toString();
			return loadKeyByStr(keyStr, key);
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

	/**
	 * 从字符串中加载公钥/密钥
	 * 
	 * @param publicKeyStr
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static RSAKey loadKeyByStr(String publicKeyStr, Key key) throws Exception {
		byte[] buffer = EncryptHelper.decode(publicKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
		if (Key.Private == key) {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} else if (Key.Public == key) {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} else {
			return null;
		}
	}

	/**
	 * <p>
	 * 加密<br>
	 * Encrypt
	 * </p>
	 * 
	 * @param key(RSAPrivateKey、RSAPublicKey)
	 * @param src
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws GeneralSecurityException
	 */
	public static String encrypt(java.security.Key key, String src)
			throws UnsupportedEncodingException, GeneralSecurityException {
		return EncryptHelper.encode(encrypt(key, src.getBytes(EncryptHelper.DEFAULT_CHARSET)));
	}

	/**
	 * <p>
	 * 加密<br>
	 * Encrypt
	 * </p>
	 * 
	 * @param key(RSAPrivateKey、RSAPublicKey)
	 * @param srcBytes
	 * @return byte[]
	 * @throws GeneralSecurityException
	 */
	public static byte[] encrypt(java.security.Key key, byte[] srcBytes) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(srcBytes);
	}

	/**
	 * <p>
	 * 解密<br>
	 * Decrypt
	 * </p>
	 * 
	 * @param key(RSAPrivateKey、RSAPublicKey)
	 * @param cipher
	 * @return
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(java.security.Key key, String cipher)
			throws GeneralSecurityException, UnsupportedEncodingException {
		return new String(decrypt(key, EncryptHelper.decode(cipher)), EncryptHelper.DEFAULT_CHARSET);
	}

	/**
	 * <p>
	 * 解密<br>
	 * Decrypt
	 * </p>
	 * 
	 * @param key(RSAPrivateKey、RSAPublicKey)
	 * @param srcBytes
	 * @return byte[]
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(java.security.Key key, byte[] srcBytes) throws GeneralSecurityException {
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(srcBytes);
	}

}
