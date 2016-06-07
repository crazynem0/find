package com.typartner.find.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Encoder;


public class Encoding {
	private static final byte[] keyBytes = "1q@W3e$5".getBytes();
	private static final String ALGORITHM = "DES/ECB/NoPadding";
	private static MessageDigest MD5 = null;
	static {
		
	    try {
	    	MD5 = MessageDigest.getInstance("MD5");
	    } catch (NoSuchAlgorithmException ex) {}
	 }
	
	/**
	 * 
	 * 功能描述:md5加密后，再base64编码
	 * @param encodeText
	 */
	 public static String md5Encoding(String value){
		 String result = "";
		 if (value == null) {
			 return result;
		 }
		 BASE64Encoder baseEncoder = new BASE64Encoder();
		 try {
			  result = baseEncoder.encode(MD5.digest(value.getBytes("utf-8")));
		 }catch (Exception ex) {}
		 return result;
	 }
	 
	/**
	 * DES加密
	 * @param encodeText 加密原文
	 * @return 加密密文(转换为16进制字符串),出错为null.
	 */
	public static String encoding(String encodeText) {
		try {
			byte[] encodeBytes = padding(encodeText);
			byte[] bytes =encrypt(encodeBytes, keyBytes);
			String hex = bytes2hex(bytes);
			return hex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * DES加密
	 * @param keyBytes 密钥
	 * @param encodeText 加密原文
	 * @return 加密密文(转换为16进制字符串),出错为null.
	 */
	public static String encoding(String key, String encodeText) {
		try {
			byte[] encodeBytes = padding(encodeText);
			byte[] bytes = encrypt(encodeBytes, key.getBytes());
			String hex = bytes2hex(bytes);
			return hex;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * DES解密
	 * @param decodedText 密文原文（加密后16进制字符串）
	 * @return 明文,出错为null.
	 */
	public static String decoding(String decodedText) {
		try {
			byte[] decodeBytes = hex2bytes(decodedText);
			byte[] bytes = decrypt(decodeBytes, keyBytes);
			return new String(truncate(bytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * DES解密
	 * @param keyBytes 密钥
	 * @param decodedText 密文原文（加密后16进制字符串）
	 * @return 明文,出错为null.
	 */
	public static String decoding(String key, String decodedText) {
		try {
			byte[] decodeBytes = hex2bytes(decodedText);
			byte[] bytes = decrypt(decodeBytes, key.getBytes());
			return new String(truncate(bytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字节数组转换为16进制字符串
	 * @param bytes
	 * @return
	 */
	private static String bytes2hex(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i]);
			sb.append(hex.length() == 1 ? ("0" + hex) : (hex.length() > 2 ? hex.substring(6, 8) : hex));
		}
		return sb.toString().toLowerCase();
	}
	
	/**
	 * 16进制字符串转换为字节数组
	 * @param hex
	 * @return
	 */
	private static byte[] hex2bytes(String hex) {
		if(hex == null || hex.trim().length() < 2 || hex.trim().length() % 2 != 0) {
			return null;
		}
		String tmp = hex.trim().toUpperCase();
		int len = tmp.length() / 2;
		byte[] bytes = new byte[len];
		int i = 0;
		int k = 0;
		while(i < tmp.length()) {
			int h = (getInt(tmp.charAt(i++))) << 4;
			int l = (getInt(tmp.charAt(i++)));
			bytes[k++] = (byte)(h + l);
		}
		return bytes;
	}
	
	/**
	 * 16进制字符对应的数值
	 * @param c
	 * @return
	 */
	private static int getInt(char c) {
		int value = 0;
		if(c >= '0' && c <= '9') {
			value = c - '0';
		}else if(c >= 'A' && c <= 'F') {
			value = c - 'A' + 10;
		}else if(c >= 'a' && c <= 'f') {
			value = c - 'a' + 10;
		}else {
			throw new IllegalArgumentException("参数错误，可接受范围[0-9A-Za-z]");
		}
		return value;
	}
	
	/**
	 * MD5摘要
	 * @param input 输入字符数组
	 * @return 摘要
	 */
    private static String md5(byte[] input) {
		MessageDigest alg = null;
		try {
			alg = MessageDigest.getInstance("MD5");// or "SHA-1"
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		alg.update(input);
		byte[] digest = alg.digest();
		return bytes2hex(digest);
	}

    /**
     * MD5摘要
     * @param encodeText 输入字符串
     * @return 摘要
     */
    public static String md5(String encodeText) {
		try {
			String encodedText = md5(encodeText.getBytes());
			return encodedText;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    
	/**
	 * 对要加密的字符串进行填充
	 * @param src 要加密的字符串（明文）
	 * @return 填充后的字节数组
	 */
	private static byte[] padding(String src) throws Exception {
		byte[] srcBytes = src.getBytes();
		int len = srcBytes.length;
		int mod = len % 8;
		if (mod == 0) {
			return srcBytes;
		}
		byte[] newBytes = new byte[len + (8 - mod)];
		System.arraycopy(srcBytes, 0, newBytes, 0, len);
		for (int i = len; i < len + (8 - mod); i++) {
			newBytes[i] = (byte) (8 - mod);
		}
		return newBytes;
	}

	/**
	 * 截断加密前填充字符
	 * @param encodeBytes 解密后字节数组
	 * @return 解密后的字符数组
	 */
	private static byte[] truncate(byte[] encodeBytes) {
		int len = encodeBytes.length;
		int index = len - 1 ;
		int tag = encodeBytes[index];
		for(int i = 1; i < 8; i++) {
			if(tag == encodeBytes[--index]) {
				len = (i == 1) ? (len - 2) : (len -1);
				continue;
			}else {
				break;
			}
		}
		if(!(tag == encodeBytes.length - len)) {
			len = encodeBytes.length;
		}
		byte[] newByte = new byte[len];
		System.arraycopy(encodeBytes, 0, newByte, 0, len);
		return newByte;
	}
	
	/**
	 * 加密
	 * @param cleartext byte[]
	 * @param key byte[]
	 * @throws Exception
	 * @return byte[]
	 */
	private static byte[] encrypt(byte[] cleartext, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		SecretKey deskey = getSecretKey(key);
		cipher.init(Cipher.ENCRYPT_MODE, deskey);

		byte[] ciphertext = cipher.doFinal(cleartext);
		return ciphertext;
	}
	
	/**
	 *<p>根据提供的密钥内容产生安全密钥</p>
	 *
	 * @param key byte[]
	 * @throws Exception
	 * @return SecretKey
	 */
	private static SecretKey getSecretKey(byte[] key) throws Exception {
		KeySpec spec = null;
		String theAlgorithm = null;
		if (ALGORITHM.startsWith("DES")) {
			spec = new DESKeySpec(key);
			theAlgorithm = "DES";
		}
		if (ALGORITHM.startsWith("DESede")) {
			spec = new DESedeKeySpec(key);
			theAlgorithm = "DESede";
		}
		SecretKeyFactory factory = SecretKeyFactory.getInstance(theAlgorithm);
		SecretKey secretKey = factory.generateSecret(spec);

		return secretKey;
	}
	
	/**
	 * 解密
	 * @param encrypttext byte[]
	 * @param key byte[]
	 * @throws Exception
	 * @return byte[]
	 */
	private static byte[] decrypt(byte[] encrypttext, byte[] key) throws Exception {
		try {
			// DES算法要求有一个可信任的随机数源
			//SecureRandom sr = new SecureRandom();

			Cipher cipher = Cipher.getInstance(ALGORITHM);
			SecretKey deskey = getSecretKey(key);
			//cipher.init(Cipher.DECRYPT_MODE, deskey, sr);
			cipher.init(Cipher.DECRYPT_MODE, deskey);

			byte[] ciphertext = cipher.doFinal(encrypttext);
			return ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
			throw new SecurityException("密钥不正确!");
		}
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		System.out.println(Encoding.encoding("fangrui"));
		
	}
}