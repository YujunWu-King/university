package com.tranzvision.gd.util.security;


import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Li.Wang
 * @version 1.0
 */

public class EncryptionForString {
  public EncryptionForString() {
  }

  // output encoding type
  public static final String ENCODING_BASE64 = "BASE64";
  public static final String ENCODING_RAW    = "RAW";

  public static final byte[] iv = {1,2,3,4,5,6,7,8};

  static
  {
      java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
  }



  /**
   * encrypt with specific key
   * @param strIn
   * @param key
   * @param strArithmetic
   * @param encoding
   * @return
   * @throws Exception
   */
  public static byte[] encrypt(byte[] keyIV,
                               String strIn,
                               Key key,
                               String strArithmetic,
                               String encoding
                               )
          throws Exception
  {
      Cipher cipher = Cipher.getInstance(strArithmetic + "/CBC/PKCS5Padding");
      //IvParameterSpec ips = new IvParameterSpec(iv);
      IvParameterSpec ips = (keyIV.length == 0)?new IvParameterSpec(iv):new IvParameterSpec(keyIV);
      cipher.init(Cipher.ENCRYPT_MODE,key,ips);
      byte[] bOut = cipher.doFinal(strIn.getBytes("UTF-8"));
      if (encoding.equalsIgnoreCase(ENCODING_BASE64))
      {
          bOut = EncodeUtil.encodeBase64(bOut);
      }

      return bOut;
  }




  /**
   * encrypt desede
   * @param strIn
   * @param key
   * @param encoding
   * @return
   * @throws Exception
   */
  public static byte[] encrypt(byte[] keyIV,
                               String strIn,
                               Key key,
                               String encoding
                               )
          throws Exception
  {
      return encrypt(keyIV,strIn,key,"desede",encoding);
  }


  /**
   * decrypt a message
   * @param bIn encrypted message
   * @param seed key seed
   * @param strKeyArithmetic key KeyArithmetic
   * @param strArithmetic decrypt KeyArithmetic
   * @param encoding output encoding
   * @return
   * @throws Exception
   */
  public static byte[] decrypt(byte[] keyIV,
                               byte[] bIn,
                               byte[] seed,
                               String strKeyArithmetic,
                               String strArithmetic,
                               String encoding
                               )
          throws Exception
  {
      SecureRandom sr = new SecureRandom(seed);
      KeyGenerator kGen = KeyGenerator.getInstance(strKeyArithmetic);
      if (strArithmetic.equalsIgnoreCase("desede"))
      {
          kGen.init(168,sr);
      }
      else
      {
          kGen.init(sr);
      }
      Key key = kGen.generateKey();
      return decrypt(keyIV,bIn,key,strArithmetic,encoding);
  }

  /**
   * decrypt with specific key
   * @param bIn
   * @param key
   * @param strArithmetic
   * @param encoding
   * @return
   * @throws Exception
   */
  public static byte[] decrypt(byte[] keyIV,
                               byte[] bIn,
                               Key key,
                               String strArithmetic,
                               String encoding
                               )
          throws Exception
  {
      Cipher cipher = Cipher.getInstance(strArithmetic + "/CBC/PKCS5Padding");
      IvParameterSpec ips = (keyIV.length == 0)?new IvParameterSpec(iv):new IvParameterSpec(keyIV);
      cipher.init(Cipher.DECRYPT_MODE,key,ips);
      byte[] bOut = cipher.doFinal(bIn);
      if (encoding.equalsIgnoreCase(ENCODING_BASE64))
      {
          bOut = EncodeUtil.encodeBase64(bOut);
      }
      return bOut;
  }

  /**
   * decrypt desede
   * @param bIn
   * @param key
   * @param encoding
   * @return
   * @throws Exception
   */
  public static byte[] decrypt(byte[] keyIV,
                               byte[] bIn,
                               Key key
                               )
          throws Exception
  {
      return decrypt(keyIV,bIn,key,"desede",ENCODING_RAW);
  }

  /**
   * decrypt desede
   * @param bIn
   * @param key
   * @param encoding
   * @return
   * @throws Exception
   */
  public static byte[] decrypt(byte[] keyIV,
                               byte[] bIn,
                               String strKey
                               )
          throws Exception
  {
      return decrypt(keyIV,bIn,strKey.getBytes("UTF-8"),"desede","desede",ENCODING_RAW);
  }



  static String bytesToString(byte[] bytes)
  {
      StringBuffer sb = new StringBuffer(bytes.length);
      for (int i=0;i<bytes.length;i++)
      {
          sb.append((char)bytes[i]);
      }
      return sb.toString();
  }

  static byte[] stringToBytes(String in)
  {
      byte[] bytes = new byte[in.length()];
      for (int i=0;i<bytes.length;i++)
      {
          bytes[i] = (byte)in.charAt(i);
      }
      return bytes;
  }

  public static String signFreeUser(int seed,String asc)
  {
      String str = "";

      // step 1
      // 将字符串转换成为ASCII串，格式为“ASCII位数”+“ASCII”+“ASCII位数”+“ASCII”……如a的ASCII是97，
      // b的ASCII是98,那么“ab”的ASCII串就是297298
      byte[] bytes = asc.getBytes();
      for (int i=0;i<bytes.length;i++)
      {
          String b = String.valueOf(bytes[i]);
          str += b.length() + b;
      }

      // step 2 将生成的ASCII串反向逆转，如297298就变成892792
      int size = str.length();
      byte[] bytes2 = str.getBytes();
      byte[] bytes1 = new byte[size];
      for (int i=0;i<size;i++)
      {
          bytes1[i] = bytes2[size - i - 1];
      }
      str = new String(bytes1);

      // step 3 将生成的反向ASCII串各位加上一个10以内的数，不进位，如892792各位加5，变成347247
      //bytes = str.getBytes();
      String str1 = "";
      for (int i=0;i<str.length();i++)
      {
          int num = Integer.parseInt(str.substring(i,i+1));
          String tmp = String.valueOf(num + seed);
          str1 += tmp.substring((tmp.length() - 1));
      }
      return str1;
  }

}