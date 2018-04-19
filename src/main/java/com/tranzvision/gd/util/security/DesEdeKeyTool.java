package com.tranzvision.gd.util.security;

import javax.crypto.*;
import java.security.*;
import java.io.*;
import javax.crypto.spec.*;

import org.bouncycastle.util.encoders.Hex;


/**
 * <p>Title: 3DES密钥工具类</p>
 * <p>Description: 生成密钥，取得密钥，对密钥文件的操作</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: 联创</p>
 * @author Li.Wang
 * @version 1.0
 */

public class DesEdeKeyTool
{
  /*
  private static String m_propFilePath = "csp.interfaces.crypt.keystore";
      private static String m_keyStorePath = "";
      private static KeyStore m_keyStore = null;
      private static String m_keyStorePass = "";
      private static java.security.cert.Certificate[] m_Certificate;*/
  private static String m_strKeyArithmetic = "DESEDE";
  private static int m_nKeyLength = 168;


  static
  {
    java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
    /*
    try
    {
        // read keystore to memory
        m_keyStore = KeyStore.getInstance("JKS", "SUN");
        ResourceBundle prb = ResourceBundle.getBundle(m_propFilePath);
        String keyStorePath = prb.getString("KEYSTORE_PATH");
        char[] m_keyStorePass = prb.getString("KEYSTORE_PASS").toCharArray();
        File file = new File(keyStorePath);
        if (file.exists())
        {
            FileInputStream fis = new FileInputStream(keyStorePath);
            m_keyStore.load(fis,m_keyStorePass);
        }
        else
        {
            // a empty key store
            m_keyStore.load(null,m_keyStorePass);
        }
        m_Certificate = m_keyStore.getCertificateChain("aaa");
    }
    catch (Exception e)
    {
        System.out.println("Init keystore error!\n" + e);
    }*/
  }

  public DesEdeKeyTool()
  {
  }

  /**
   * 生成密钥
   * @param seed
   * @return
   * @throws Exception
   */
  public static Key generateKey(byte[] seed) throws Exception
  {
    SecureRandom sr = new SecureRandom(seed);
    KeyGenerator kGen = KeyGenerator.getInstance(m_strKeyArithmetic);
    kGen.init(m_nKeyLength,sr);
    Key key = kGen.generateKey();
    return key;
  }

  /**
   * 生成密钥
   * @param seed
   * @return
   * @throws Exception
   */
  public static byte[] generateKeyBytes(byte[] seed) throws Exception
  {
    Key key = generateKey(seed);
    return key.getEncoded();
  }

  /**
   * 生成密钥到文件
   * @param fileName
   * @param seed
   * @throws Exception
   */
  public static void generateKeyFile(String fileName,byte[] seed) throws Exception
  {
    Key key = generateKey(seed);
    FileOutputStream out = new FileOutputStream(fileName);
    out.write(Hex.encode(key.getEncoded()));
    out.close();
  }

  /**
   * 把一个字节数组转换成密钥对象
   * @param keyBytes
   * @return
   * @throws Exception
   */
  public static Key byte2Key(byte[] keyBytes) throws Exception
  {
    DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(m_strKeyArithmetic);
    return keyfactory.generateSecret(spec);
  }

  /**
   * 从文件读入密钥
   * @param fileName
   * @return
   * @throws Exception
   */
  public static Key getKeyFromFile(String fileName) throws Exception
  {
    //key.getEncoded();
    FileInputStream in = new FileInputStream(fileName);
    byte[] keybytes = new byte[in.available()];
    in.read(keybytes);
    in.close();
    return byte2Key(Hex.decode(new String(keybytes)));
  }

}
