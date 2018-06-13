package com.tranzvision.gd.util.security.collectsecurity;

import com.tranzvision.gd.util.security.Base64;
import com.tranzvision.gd.util.security.EncryptionForString;
import com.tranzvision.gd.util.security.Security;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class SecurityForCollectFee {

	/**
	 * 
	 * 
	 * @param Authenticator
	 * @param keyvalue
	 * @param dev
	 * @return
	 * @throws java.lang.Exception
	 */
	public static int checkAuthenticator(String Authenticator, String keyvalue, String dev) {
		int rc = 0;
		try {
			java.security.Key key = Security.getKey(keyvalue);
			//System.out.println("dev=" + dev);
			byte[] tempcheck = DigestForCollectFee.message(dev, null);
			byte[] encryptStr = Base64
					.encode(EncryptionForCollect.encrypt(tempcheck, key, EncryptionForString.ENCODING_RAW));
			String checker = new String(encryptStr);
			System.out.println("checker=" + checker);
			System.out.println("Authenticator=" + Authenticator);
			if (!checker.equals(Authenticator)) {
				rc = -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			rc = -1;
		}
		return rc;
	}

	/**
	 * 生成返回给SP或全国中心的摘要
	 * 
	 * @param SPID
	 * @param TimeStamp
	 * @return
	 * @throws java.lang.Exception
	 */
	public static String generalAuthenticator(String keyvalue, String dev) throws Exception {
		String rtn = "";
		try {
			java.security.Key key = Security.getKey(keyvalue);
			byte[] tempcheck = DigestForCollectFee.message(dev, null);
			pring(tempcheck);
			
			pring(EncryptionForCollect.encrypt(tempcheck, key, EncryptionForString.ENCODING_RAW));
			byte[] encryptStr = Base64
					.encode(EncryptionForCollect.encrypt(tempcheck, key, EncryptionForString.ENCODING_RAW));
			rtn = new String(encryptStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	private static void pring(byte[] tempcheck) {
		System.out.println("===================");
		for (int i=0;i<tempcheck.length;i++) {
			System.out.println(toHexString1(tempcheck[i]));
		}
		System.out.println("===================");
	}
	
	public static String toHexString1(byte[] b){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i){
            buffer.append(toHexString1(b[i]));
        }
        return buffer.toString();
    }
	
	public static String toHexString1(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		//System.out.println(String.valueOf(null));
//		try {
//			java.security.Key key = Security.getKey("A0DFDF28AEAB2F131327FEDE5C2D8A6D399E4F568A2D4EFD");
	String dev = "111222221525396905558";
	String key="A0DFDF28AEAB2F131327FEDE5C2D8A6D399E4F568A2D4EFD";
	System.out.println("key="+key);
	System.out.println("dev="+dev);
	try {
		String a = SecurityForCollectFee.generalAuthenticator(key, dev);
		System.out.println(a);
		int b=SecurityForCollectFee.checkAuthenticator(a, key, dev);
		System.out.println(b);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//			byte[] tempcheck = DigestForCollectFee.message(dev, null);
//			// System.out.println("tempcheck=" + new String(tempcheck));
//			byte[] encryptStr = EncryptionForCollect.encrypt(tempcheck, key, EncryptionForString.ENCODING_RAW);
//			byte[] aa = Base64.encode(encryptStr);
//			// String rtn = new String(encryptStr);
//			System.out.println("rtn=" + new String(tempcheck));
//			System.out.println("rtn=" + new String(encryptStr));
//			System.out.println("rtn=" + new String(aa));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}