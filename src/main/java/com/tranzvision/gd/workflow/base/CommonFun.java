package com.tranzvision.gd.workflow.base;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 公共函数功能
 * @author 张浪	2019-01-09
 *
 */
public class CommonFun {

	
	private GetSeqNum getSeqNum;
	
	
	public CommonFun(){
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		getSeqNum = (GetSeqNum) getSpringBeanUtil.getAutowiredSpringBean("GetSeqNum");
	}
	
	
	/**
	 * 全局唯一识别标识生成函数
	 * @param preGUID	前缀
	 * @return
	 */
	public String GenerateGUID(String preGUID) {
		String GUID = "";
		Random random = new Random();
		
		String randomStr = String.valueOf(random.nextInt(4899)) 
				+ String.valueOf(random.nextInt(1276))
				+ String.valueOf(random.nextInt(579))
				+ String.valueOf(random.nextInt(199))
				+ String.valueOf(random.nextInt(89))
				+ String.valueOf(random.nextInt(7921)) 
				+ String.valueOf(random.nextInt(1767919)) + "0000000000000000";
		randomStr = randomStr.substring(0, 16);
		
		String timeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		GUID = preGUID + timeStr + randomStr;
 		
		return GUID;
	}
	
	
	
	
	/**
	 * 全局唯一识别标识生成函数
	 * @param preGUID	前缀
	 * @param len	长度
	 * @return
	 */
	public String GenerateGUID_1(String preGUID, int len) {
		String GUID = "";
		Random random = new Random();
		
		String randomStr = String.valueOf(random.nextInt(4899)) 
				+ String.valueOf(random.nextInt(1276))
				+ String.valueOf(random.nextInt(579))
				+ String.valueOf(random.nextInt(199))
				+ String.valueOf(random.nextInt(89))
				+ String.valueOf(random.nextInt(7921)) 
				+ String.valueOf(random.nextInt(1767919)) + "0000000000000000";
		//randomStr = randomStr.substring(0,16);
		
		String timeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		GUID = preGUID + timeStr + randomStr;
		
		if(len + preGUID.length() < 20){
			len = 20 + preGUID.length();
		}
		GUID = GUID.substring(0, len);
		
		return GUID;
	}
	
	
	
	
	/**
	 * 全局唯一顺序序列号生成函数
	 * @param s_Type	唯一属性类型，字符串
	 * @param len		长度
	 * @return
	 */
	public String GenerateSeqGUID(String s_Type, int len){
		String GUID = "";
		//获取自增序列
		int seqNum = getSeqNum.getSeqNum("TZ_WFL_UNIQSEQ", "WFL_"+s_Type);
		
		String seqNumStr = "0000000000000000000000000000000000000000" + seqNum;
		String timeStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
		
		s_Type += timeStr;
		
		GUID = s_Type + seqNumStr.substring(seqNumStr.length() - (len - s_Type.length()));

		return GUID;
	}
	
}
