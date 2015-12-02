/**
 * 
 */
package com.tranzvision.gd.util.sequence;

import java.net.InetAddress;

/**
 * 生成随机唯一序列号
 * 
 * @author SHIHUA
 * @since 2015-11-03
 */
public class IdCreator {

  private static final long orderDataCenterId = 0;
  
  private IdWorker idWorker;
  
  public IdCreator() {
      long workerId = 0l;
      try {
          String ip = InetAddress.getLocalHost().getHostAddress();
          System.out.println(ip);
          workerId = Long.parseLong(ip.substring(ip.length() -1));
      } catch (Exception e) {
          e.printStackTrace();
          workerId = (long)(Math.random()*32);
      }
      idWorker = new IdWorker(workerId, orderDataCenterId);
  }
  
  /**
   * 生成id
   * @return
   */
  public final String createId() {
      return String.valueOf(idWorker.nextId());
  }

}
