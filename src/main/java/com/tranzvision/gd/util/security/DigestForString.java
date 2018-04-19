package com.tranzvision.gd.util.security;

import java.security.MessageDigest;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Li.Wang
 * @version 1.0
 */

public class DigestForString {
  public DigestForString() {
  }

  // encoding
  public static final String ENCODING_BASE64 = "BASE64";
  public static final String ENCODING_RAW    = "RAW";

  /**
   * make a digest using arithmetic like sha-1 or md5...
   * @param strInput
   * @param strArithmetic
   * @param encoding
   * @return digest string
   * @throws Exception
   */
  public static String message(String strInput,String strArithmetic,String encoding) throws Exception
  {
      if (strArithmetic == null || strArithmetic.equals("") ||
          strInput == null)
      {
          throw new Exception ("must have message content and arithmetic!\n");
      }

      if (encoding == null || encoding.equals(""))
      {
          encoding = ENCODING_RAW;
      }

      String strOut = "";
      byte[] bOut = null;
      byte[] bIn = strInput.getBytes("UTF-8");

      // arithmetic, md5,sha-1
      MessageDigest md = MessageDigest.getInstance(strArithmetic);
      //System.out.println(md.getProvider().getName());
      //byte[] bytes = Hex.decode(strInput);
      md.update(bIn);
      bOut = md.digest();

      if (encoding.equalsIgnoreCase(ENCODING_BASE64))
      {
          bOut = EncodeUtil.encodeBase64(bOut);
      }
      strOut = new String(bOut);
      //System.out.println(strOut);
      return strOut;
  }

  /**
   * gen a digest use SHA-1
   * @param strInput
   * @param encoding
   * @return
   * @throws Exception
   */
  public static String message(String strInput,String encoding) throws Exception
  {
      return message(strInput,"SHA-1",encoding);
  }


  /**
   * verify that the two message digests match
   * @param newMD
   * @param oldMD
   * @return false not equal,else true
   */
  public static boolean verify(byte[] newMD,byte[] oldMD)
  {
      boolean bResult = true;
      // verify that the two message digests match
      int len = newMD.length;
      if (len != oldMD.length)
      {
          bResult = false;
      }
      else
      {
          for (int i = 0; i < len; i++)
          {
              if (oldMD[i] != newMD[i])
              {
              bResult = false;
              break;
          }
          }
      }
      return bResult;
  }

  /**
   * verify that the two message digests match
   * @param strNewDigest
   * @param strOldDigest
   * @return
   */
  public static boolean verify(String strNewDigest,String strOldDigest) throws Exception
  {
      return verify(strNewDigest.getBytes("UTF-8"),strOldDigest.getBytes("UTF-8"));
  }

}