package com.tranzvision.gd.util.security.collectsecurity;

import java.security.*;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Linkage </p>
 * @author Li.Wang 2005-07-08
 * @version 1.0
 */

public class DigestForCollectFee {
  // encoding
  public static final String ENCODING_BASE64 = "BASE64";
  public static final String ENCODING_RAW = "RAW";

  public DigestForCollectFee() {
  }

  /**
   * make a digest using arithmetic like sha-1 or md5...
   * @param strInput
   * @param strArithmetic
   * @param encoding
   * @return digest string
   * @throws Exception
   */
  public static byte[] message(String strInput, String strArithmetic,
                               String encoding) throws Exception {
    if (strArithmetic == null || strArithmetic.equals("") ||
        strInput == null) {
      throw new Exception("must have message content and arithmetic!\n");
    }

    if (encoding == null || encoding.equals("")) {
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

    return bOut;
  }

  /**
   * gen a digest use SHA-1
   * @param strInput
   * @param encoding
   * @return
   * @throws Exception
   */
  public static byte[] message(String strInput, String encoding) throws
      Exception {
    return message(strInput, "SHA-1", encoding);
  }
}
