package io.pomelo.util;

import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;

import org.junit.Test;

import io.pomelo.util.common.BinaryUtil;
import io.pomelo.util.common.FileUtil;
import io.pomelo.util.common.MathUtil;
import io.pomelo.util.security.IrreversibleEncryptor;
import io.pomelo.util.security.IrreversibleEncryptor.Algorithm;
import io.pomelo.util.security.RSAUtil;
import io.pomelo.util.security.ReversibleEncryptor;

public class UtilTest {

	/******************************** ↓文件工具↓ ********************************/
	@Test
	public void basic() {
//		FileUtil.getFile("doc", "README.txt");
		System.out.println(FileUtil.getFile(""));
	}

	@Test
	public void decimal() {
//		System.out.println(DecimalUtil.format("0.00%", 0.999956));
		System.out.println(MathUtil.subtract(1, 2));
		System.out.println(MathUtil.divide(3, 5));
		System.out.println(MathUtil.round(3.1415926, 4));
	}

	@Test
	public void zip() throws IOException {
	}

	@Test
	public void unzip() throws IOException {
	}

	/******************************** ↑文件工具↑ ********************************/
	/******************************** ↓加密解密↓ ********************************/
	private String securityString = "pomeloman*@%";

	@Test
	public void MD5Test() throws Exception {
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + IrreversibleEncryptor.encrypt(securityString, Algorithm.MD5));
	}

	@Test
	public void SHATest() throws Exception {
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + IrreversibleEncryptor.encrypt(securityString, Algorithm.SHA));
	}

	@Test
	public void DESTest() throws Exception {
		String key = ReversibleEncryptor.getSecretKeyHexString(ReversibleEncryptor.Algorithm.DES);
		String cipher = ReversibleEncryptor.encrypt(securityString, key, ReversibleEncryptor.Algorithm.DES);
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + cipher);
		System.out.println(
				"After decryption: " + ReversibleEncryptor.decrypt(cipher, key, ReversibleEncryptor.Algorithm.DES));
	}

	@Test
	public void AESTest() throws Exception {
		String key = ReversibleEncryptor.getSecretKeyHexString(ReversibleEncryptor.Algorithm.AES);
		String cipher = ReversibleEncryptor.encrypt(securityString, key, ReversibleEncryptor.Algorithm.AES);
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + cipher);
		System.out.println(
				"After decryption: " + ReversibleEncryptor.decrypt(cipher, key, ReversibleEncryptor.Algorithm.AES));
	}

	@Test
	public void DESedeTest() throws Exception {
		String key = ReversibleEncryptor.getSecretKeyHexString(ReversibleEncryptor.Algorithm.DESede);
		String cipher = ReversibleEncryptor.encrypt(securityString, key, ReversibleEncryptor.Algorithm.DESede);
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + cipher);
		System.out.println(
				"After decryption: " + ReversibleEncryptor.decrypt(cipher, key, ReversibleEncryptor.Algorithm.DESede));
	}

	@Test
	public void RSATest() throws Exception {
		String cipher = null;
		KeyPair pair = RSAUtil.generateKeyPair();
		cipher = RSAUtil.encrypt((RSAPublicKey) pair.getPublic(), securityString);
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + cipher);
		System.out.println("After decryption: " + RSAUtil.decrypt((RSAPrivateKey) pair.getPrivate(), cipher));

		RSAPrivateKey prikey = (RSAPrivateKey) RSAUtil
				.loadKeyByFile(RSAUtil.class.getResourceAsStream(RSAUtil.PRIVATE_KEY), RSAUtil.Key.Private);
		RSAPublicKey pubkey = (RSAPublicKey) RSAUtil
				.loadKeyByFile(RSAUtil.class.getResourceAsStream(RSAUtil.PUBLIC_KEY), RSAUtil.Key.Public);
		cipher = RSAUtil.encrypt(prikey, securityString);
		System.out.println("Before encryption: " + securityString);
		System.out.println("After encryption: " + cipher);
		System.out.println("After decryption: " + RSAUtil.decrypt(pubkey, cipher));
	}

	/******************************** ↑加密解密↑ ********************************/
	/******************************** ↓二进制工具↓ ********************************/
	@Test
	public void StringUtilTest() {
		System.out.println(Arrays.toString(BinaryUtil.toIndexArray(5)));
		System.out.println(Arrays.toString(BinaryUtil.toBinaryArray(5)));
	}
	/******************************** ↑二进制工具↑ ********************************/
}
