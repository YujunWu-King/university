package com.tranzvision.gd.util.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 主要包含常用的消息摘要算法SHA-1和MD5， 对称加密算法3DES(DESede)
 * 
 * 消息摘要算法利用JDK提供的MessageDigest工具类实现
 * SHA-1：从byte[]直接做Base64 encode，然后new String
 * MD5：将byte转成hex String然后拼接，默认32位
 * 注意这两种是不一样的
 * 
 * 3DES(DESede)加密，解密
 * 
 * 另外Base64使用自己实现的工具类，替换sun的jdk中未公开提供的
 * sun.misc.BASE64Encoder
 * sun.misc.BASE64Decoder
 * 因为在非Sun提供的JDK，例如IBM的JDK中可能找不到相关类，不利于部署
 *
 */
public class Sha3DesMD5 {

	/*********************************************************************************************************
	 * 
	 *   MessageDigest 消息摘要算法包含常用的SHA-1, MD5
	 * 
	 *********************************************************************************************************/
	public static final String MESSAGE_DIGECT_SHA1 = "SHA-1";
	public static final String MESSAGE_DIGECT_MD5 = "MD5";
	
	/**
	 * 摘要算法
	 * @param input 待做摘要明文字符串
	 * @param algorithm 摘要算法，例如 SHA-1, MD5
	 * @return digest string 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public static byte[] digest(String input, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		if (algorithm == null || "".equals(algorithm) || input == null) {
			throw new IllegalArgumentException("must have message content and arithmetic!");
		}
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(input.getBytes("UTF-8"));
		return md.digest();
	}

	/**
	 * SHA1摘要算法
	 * 
	 * @param input 待摘要明文字符串
	 * @param encoding 是否做Base64转码
	 * @return
	 * @throws Exception
	 */
	public static String sha1(String input, boolean encoding)
			throws Exception {
		byte[] out = digest(input, MESSAGE_DIGECT_SHA1);
		if	(encoding) {
			out = Base64.encode(out);
		}
		return new String(out, "UTF-8");
	}

	/**
	 * SHA1摘要算法,byte[]使用Base64转码
	 * @param input
	 * @return
	 * @throws Exception
	 */
	public static String sha1(String input) throws Exception {
		return sha1(input, true);
	}
	
	/**
	 * 32位MD5摘要算法
	 * 
	 * @param input 明文字符串
	 * @return
	 * @throws Exception
	 */
	public static String md5(String input) {
		try {
            return byte2Hex(digest(input, MESSAGE_DIGECT_MD5));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
	}
	
	/**
	 * 16位MD5摘要算法
	 * 
	 * @param input 明文字符串
	 * @return
	 * @throws Exception
	 */
	public static String md5_16(String input) {
		return md5(input).substring(8,24);
	}
	
	/*********************************************************************************************************
	 * 
	 *   加密算法
	 *   
	 *   加密算法包括DES,3DES,Blowfish等
	 *   运算模式包括ECB,CBC,CFB,OFB等(CBC需要加密向量，常用)
	 *   与其它语言通用的填充模式包括NoPadding,ISO10126Padding,
	 *   PKCS5Padding(对应.Net中默认设置的PKCS7Padding)
	 *   
	 *********************************************************************************************************/
	
	public static final String SYMMETRICENCRYPTION_DES = "DES";
	public static final String SYMMETRICENCRYPTION_3DES = "DESede";
	public static final byte[] DEFAULT_IV = {1,3,5,7,9,0,8,6};//默认的加密向量
	
    /**
     * 加密方法
     * @param key
     * @param iv
     * @param algorithm
     * @param source
     * @return
     * @throws NoSuchPaddingException 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidAlgorithmParameterException 
     * @throws InvalidKeyException 
     * @throws BadPaddingException 
     * @throws IllegalBlockSizeException 
     * @throws Exception
     */
    public static byte[] encrypt(String key, byte[] iv, String algorithm, byte[] source, boolean encoding) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
    	SecretKey secretKey = new SecretKeySpec(hex2byte(key), algorithm);
    	IvParameterSpec ips =  new IvParameterSpec(iv);
    	cipher.init(Cipher.ENCRYPT_MODE, secretKey, ips);
    	byte[] bOut =  cipher.doFinal(source);
    	if (encoding) {
            bOut = Base64.encode(bOut);
        }
    	return bOut;
    }
    
    /**
     * 使用3DES(DESede)方式加密
     * @param key
     * @param iv
     * @param source
     * @return
     * @throws Exception
     */
    public static byte[] tripleDES(String key, byte[] iv, byte[] source,boolean encoding) throws Exception{
        return encrypt(key, iv, SYMMETRICENCRYPTION_3DES, source, encoding);
	}
    
    /**
     * 使用默认的加密向量
     * @param key
     * @param source
     * @return 加密后的字符串数组
     * @throws Exception
     */
    public static byte[] tripleDES(String key, byte[] source) throws Exception{
    	return tripleDES(key, DEFAULT_IV, source, true);
    }
    
    public static String tripleDES(String key, String source) throws Exception{
        return new String(tripleDES(key, source.getBytes("utf-8")),"utf-8");
    }
    
    /**
     * 解密方法
     * @param key
     * @param iv
     * @param algorithm
     * @param source
     * @param hasEncoding
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(String key, byte[] iv, String algorithm, byte[] source, boolean hasEncoding) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm + "/CBC/PKCS5Padding");
        SecretKey secretKey = new SecretKeySpec(hex2byte(key), algorithm);
        IvParameterSpec ips =  new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ips);
        if (hasEncoding) {
            source = Base64.decode(source);
        }
        return cipher.doFinal(source);
    }
    
    public static byte[] decryptTripleDES(String key, byte[] iv, byte[] source,boolean hasEncoding) throws Exception{
        return decrypt(key, iv, SYMMETRICENCRYPTION_3DES, source, hasEncoding);
    }
    
    public static byte[] decryptTripleDES(String key, byte[] source) throws Exception{
        return decryptTripleDES(key, DEFAULT_IV, source, true);
    }
    
    public static String decryptTripleDES(String key, String source) throws Exception{
           return new String(decryptTripleDES(key, source.getBytes("utf-8")), "utf-8");
    }
    
    /**
     * 用时间戳做种子生成3DES算法密钥
     * 
     * @throws Exception
     */
    public static void generateKey() {
        try {
            SecureRandom sr = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes("utf-8"));
            KeyGenerator kGen = KeyGenerator.getInstance(Sha3DesMD5.SYMMETRICENCRYPTION_3DES);
            kGen.init(168, sr);
            Key key = kGen.generateKey();
            //System.out.println(byte2Hex(key.getEncoded()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    
	/*********************************************************************************************************
	 * 
	 *   其它
	 * 
	 *********************************************************************************************************/
	/**
	 * 校验两个byte数组是否匹配
	 * 
	 * @param aDigest
	 * @param anotherDigest
	 * @return false not equal,else true
	 */
	public static boolean isEqual(byte[] aDigest, byte[] anotherDigest) {
		int len = aDigest.length;
		if (len != anotherDigest.length) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (anotherDigest[i] != aDigest[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 校验两个摘要字符串是否匹配
	 * 
	 * @param aDigest
	 * @param anotherDigest
	 * @return
	 * @throws Exception 
	 */
	public static boolean isEqual(String aDigest, String anotherDigest) throws Exception {
		return isEqual(aDigest.getBytes("UTF-8"),
				anotherDigest.getBytes("UTF-8"));
	}

	/**
	 * 二进制数组转字符串
	 * @param b
	 * @return
	 */
	public static String byte2Hex(byte[] b)  { 
		return byte2Hex(b, null);
	}
	
	/**
	 * 字符串转二进制数组
	 * @param s
	 * @return
	 */
	public static byte[] hex2byte(String s) {
		byte[] b = s.getBytes();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
	
	/**
	 * 二进制制转字符串
	 * @param b
	 * @param splitter 拼接字符串的分隔符
	 * @return
	 */
    public static String byte2Hex(byte[] b, String splitter) {
        StringBuilder hs = new StringBuilder("");
        String tmpStr = "";
        for (int n = 0; n < b.length; n++) {
            tmpStr = (Integer.toHexString(b[n] & 0XFF));
            if (tmpStr.length() == 1)
                hs.append("0").append(tmpStr);
            else
                hs.append(tmpStr);
            if (splitter != null && !"".equals(splitter)) {
                if (n < b.length - 1)
                    hs.append(splitter);
            }
        }
        return hs.toString().toUpperCase();
    }
	
}
