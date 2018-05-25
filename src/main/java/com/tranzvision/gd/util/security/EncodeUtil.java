package com.tranzvision.gd.util.security;
/**
 * <p>Title: 编码类</p>
 * <p>Description: 提供BASE64,HEX的加解码</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * @version 1.0
 */

//import org.bouncycastle.util.encoders.Base64;
//import org.bouncycastle.util.encoders.Hex;

public class EncodeUtil
{

    public EncodeUtil()
    {
    }

    /**
     * decode base64
     * @param strIn
     * @return
     */
    public static byte[] decodeBase64(String strIn)
    {
        return Base64.decode(strIn);
    }

    /**
     * decode base64
     * @param bytes
     * @return
     */
    public static byte[] decodeBase64(byte[] bytes)
    {
        return Base64.decode(bytes);
    }

    /**
     * encode base64
     * @param bytes
     * @return
     */
    public static byte[] encodeBase64(byte[] bytes)
    {
        return Base64.encode(bytes);
    }

    /**
     * encode base64
     * @param str
     * @return
     */
    public static byte[] encodeBase64(String str)
    {
        return encodeBase64(str.getBytes());
    }

    public static String encodeBase64ToString(byte[] bytes)
    {
        return new String(encodeBase64(bytes));
    }


    /**
     * encode hex
     * @param bytes
     * @return
     */
     /*
    public static byte[] encodeHex(byte[] bytes)
    {
        return Hex.encode(bytes);
    }*/

    /**
     * encode hex
     * @param str
     * @return
     */
     /*
    public static byte[] encodeHex(String str)
    {
        return encodeHex(str.getBytes());
    }*/

    /**
     * decode hex
     * @param bytes
     * @return
     */
     /*
    public static byte[] decodeHex(byte[] bytes)
    {
        return Hex.decode(bytes);
    }*/

    /**
     * decode hex
     * @param str
     * @return
     */
     /*
    public static byte[] decodeHex(String str)
    {
        return Hex.decode(str);
    }*/
}
