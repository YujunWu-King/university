package com.tranzvision.gd.util.security;



import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.bouncycastle.util.encoders.Hex;


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

public class Security {

	/**
	 * 完成数据的加密操作 参数说明 keyValue 密钥 For3DES 要加密的数据 ForDigest 要生成摘要的数据 keyIV 加密向量
	 * linkString 要加密的数据和加密向量之间的连接符
	 *
	 * 说明 若不需要做digest摘要，则传的参数 ForDigest 为null 若For3DES 和 ForDigest
	 * 之间的连接不需要任何连接符则传 "" keyIV 为byts[]类型
	 *
	 * @param keyValue
	 * @param For3DES
	 * @param ForDigest
	 * @param keyIV
	 * @return
	 * @throws java.lang.Exception
	 */
	public static String generalStringFor3DES(String keyValue, String For3DES, String ForDigest, byte[] keyIV,
			String linkString) throws Exception {
		String rtn = null;
		String tempcheck = null;
		String _For3DES = null;
		try {
			// 生成key
			java.security.Key key = Security.getKey(keyValue);

			if (ForDigest != null && ForDigest.length() > 0) {
				tempcheck = DigestForString.message(ForDigest, DigestForString.ENCODING_BASE64);
				_For3DES = For3DES + linkString + tempcheck;
			} else {
				_For3DES = For3DES;
			}
			byte[] encryptStr = Base64
					.encode(EncryptionForString.encrypt(keyIV, _For3DES, key, EncryptionForString.ENCODING_RAW));

			rtn = new String(encryptStr);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return rtn;
	}

	/**
	 * 完成用户的解密过程 参数说明： keyValue 密钥 DES2String 要解密的数据 keyIV 加密向量
	 * 
	 * @param keyValue
	 * @param DES2String
	 * @param keyIV
	 * @return
	 * @throws java.lang.Exception
	 */
	public static String Decrypt3DES2String(String keyValue, String DES2String, byte[] keyIV) throws Exception {
		String DecryptString = new String(Security.decrypt(DES2String.getBytes(), keyValue, keyIV), "UTF-8");
		return DecryptString;
	}

	/**
	 * 根据KeyValue生成Key
	 * 
	 * @param keyValue
	 * @return
	 * @throws java.lang.Exception
	 */
	public static java.security.Key getKey(String keyValue) throws Exception {
		java.security.Key key = null;
		key = DesEdeKeyTool.byte2Key(Hex.decode(keyValue));
		return key;
	}

	/**
	 * 对字符串进行解密，先对字符串用BASE64解码，然后对加密的字符串进行解密
	 * 
	 * @param encryptByBase64Str
	 *            需要解密的字符串
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] encryptByBase64Str, String keyValue, byte[] keyIV) throws Exception {
		// 对字符串用BASE64解码
		byte[] encryptStr = Base64.decode(encryptByBase64Str);
		java.security.Key key = getKey(keyValue);
		// 对加密的字符串进行解密
		byte[] decryptStr = EncryptionForString.decrypt(keyIV, encryptStr, key);
		return decryptStr;
	}



	/**
	 * 
	 * 使用gzip进行压缩
	 */
	public static String gzip(String primStr) {
		if (primStr == null || primStr.length() == 0) {
			return primStr;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		GZIPOutputStream gzip = null;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(primStr.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (gzip != null) {
				try {
					gzip.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new sun.misc.BASE64Encoder().encode(out.toByteArray());
//		try {
//			return  out.toString("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
//		return "";
	}

	public static String compactString(String tempString) {
		  StringBuffer sb = new StringBuffer();
		  byte[] tempBytes = tempString.getBytes();
		  for (int i = 0; i < tempBytes.length; i+=2) {
		   char firstCharacter = (char)tempBytes[i];
		   char secondCharacter = 0;
		   if(i+1<tempBytes.length)
		    secondCharacter = (char)tempBytes[i+1];
		   firstCharacter <<= 8;
		   sb.append((char)(firstCharacter+secondCharacter));
		  }
		  return sb.toString();
		}
		
	
	/**
	 *
	 * <p>
	 * Description:使用gzip进行解压缩
	 * </p>
	 * 
	 * @param compressedStr
	 * @return
	 */
	public static String gunzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = null;
		GZIPInputStream ginzip = null;
		byte[] compressed = null;
		String decompressed = null;
		try {
			compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
			in = new ByteArrayInputStream(compressed);
			ginzip = new GZIPInputStream(in);

			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = ginzip.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ginzip != null) {
				try {
					ginzip.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}

	/**
	 * 使用zip进行压缩
	 * 
	 * @param str
	 *            压缩前的文本
	 * @return 返回压缩后的文本
	 */
	public static final String zip(String str) {
		if (str == null)
			return null;
		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;
		String compressedStr = null;
		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
			compressedStr = new sun.misc.BASE64Encoder().encodeBuffer(compressed);
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return compressedStr;
	}

	/**
	 * 使用zip进行解压缩
	 * 
	 * @param compressed
	 *            压缩后的文本
	 * @return 解压后的字符串
	 */
	public static final String unzip(String compressedStr) {
		if (compressedStr == null) {
			return null;
		}

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {
			byte[] compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			zin.getNextEntry();
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return decompressed;
	}

	public static void main(String[] args) {
		String key = "161F3578E028C9E2F567A27EE5F2F8A2C9BDAEFE3DFDFABD";
		String For3DES = "2415225000000000408159$TZ_163024";
		//String For3DES = "abaedaaaeavvasevagoknfac";
		System.out.println(For3DES.length());
		byte[] IV_SECURITY = { 1, 2, 3, 4, 5, 6, 7, 8 };
		String linkString = "$";
		try {
			String aa = Security.generalStringFor3DES(key, For3DES, For3DES, IV_SECURITY, linkString);
			System.out.println("3DES:"+aa);
			System.out.println(aa.length());
//			String bb=Security.Decrypt3DES2String("A0DFDF28AEAB2F131327FEDE5C2D8A6D399E4F568A2D4EFD", "8XBuwicXEWfrijjaWPTaJSOCyuomYIZWoLnUEUmkQ0oK+1t3WG+Srap8MEQc6lPYtYPqSLxFr/9kr1F/YQ2OD69FH5YFnPaa", IV_SECURITY);
//			System.out.println("bb="+bb);
			MD5 md5 = new MD5();
			aa = md5.getMD5(For3DES.getBytes());
			System.out.println("MD5:"+aa);
			System.out.println(aa.length());
			
			//String gzip = Security.compactString(For3DES);
			
			//System.out.println("gzip:"+gzip);
			//System.out.println(For3DES.hashCode());

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}