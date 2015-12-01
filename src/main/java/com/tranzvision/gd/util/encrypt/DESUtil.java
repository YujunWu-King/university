package com.tranzvision.gd.util.encrypt;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DESUtil
{

	private static final String DES = "DES";

	public DESUtil()
	{
	}

	public static byte[] encrypt(byte src[], byte key[])
		throws Exception
	{
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(1, securekey, sr);
		return cipher.doFinal(src);
	}

	public static byte[] decrypt(byte src[], byte key[])
		throws Exception
	{
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(2, securekey, sr);
		return cipher.doFinal(src);
	}

	public static final String decrypt(String data, String key)
	{
		try{
			return new String(decrypt(hex2byte(data.getBytes()), key.getBytes()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static final String encrypt(String data, String key)
	{
		
		try{
			return byte2hex(encrypt(data.getBytes(), key.getBytes()));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String byte2hex(byte b[])
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}

		return hs.toUpperCase();
	}

	public static byte[] hex2byte(byte b[])
	{
		if (b.length % 2 != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte b2[] = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2)
		{
			String item = new String(b, n, 2);
			b2[n / 2] = (byte)Integer.parseInt(item, 16);
		}

		return b2;
	}

	public static void main(String args[])
	{
		String encode = encrypt("ni hao a ! *4586 ^#@#!@$!完全正确", "ACEMSSOKEY");
		//System.out.println("encode = " + encode);
		String decode = decrypt(encode, "ACEMSSOKEY");
		//System.out.println("decode = " + decode);
	}
}
