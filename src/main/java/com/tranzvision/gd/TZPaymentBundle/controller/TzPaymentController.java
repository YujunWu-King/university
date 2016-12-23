package com.tranzvision.gd.TZPaymentBundle.controller;

import java.math.BigDecimal;

/**
 * @author raosheng
 *	
 */
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranzvision.gd.TZPaymentBundle.dao.PsTzPaymentInfoTMapper;
import com.tranzvision.gd.TZPaymentBundle.model.PsTzPaymentInfoT;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Controller
@RequestMapping("/pay")
public class TzPaymentController {
	//注入支付信息主表操作mapper
	@Autowired PsTzPaymentInfoTMapper psTzPaymentInfoTMapper;
	//注入主键生成类
	@Autowired GetSeqNum getSeqNum;
	//注入SQL执行类(SqlQuery是spring中JdbcTemplate类的封装类)
	@Autowired SqlQuery jdbcTemplate;
	
	//用来接收并处理前台传输的数据信息,存入数据库，最后转发到到三方支付界面
	public void  dealWithPaymentInfo(HttpServletRequest request,HttpServletResponse response ) throws Exception{
		
		//支付信息主表对象
		PsTzPaymentInfoT psTzPaymentInfoT=new PsTzPaymentInfoT();
		//*支付Id，必填，不超过16位的数字，规则可以自己定义，但应保持本应用系统不重复。*/
		String paymentId=null;
		//用户是否是同一个
		String UserId="zhangsan";
		//SQL查询用户ID为zhangsan的paymentId
		String sameUserSQL="select TZ_PAYMENTID from PS_TZ_PAYMENTINFO_T";
		paymentId=jdbcTemplate.queryForObject(sameUserSQL,"String");
		if(paymentId==null){
			paymentId="P"+getSeqNum.getSeqNum("PS_TZ_PAYMENTINFO_T", "TZ_PAYMENTID");
		}
		System.out.println("UserId:"+UserId+"paymentId:"+paymentId);
		
		psTzPaymentInfoT.setTzPaymentid(paymentId);
		String amount = request.getParameter("amount");	//金额
		String appOrderOwner = request.getParameter("username");	//支付者姓名，非必填
		psTzPaymentInfoT.setTzAmount(new BigDecimal(amount));
		
		//数据存入
		psTzPaymentInfoTMapper.insert(psTzPaymentInfoT);
		
		String projectId = "130002";		//项目编号
		//String amount = request.getParameter("amount");	//金额
		String currency = "CNY";			//币种
		String appPaymentId = "00000002";	//应用系统内的订单编号，长度16位以内	
		String directpay = "1";				//直接跳入支付页面
		String paymentCompanyId = "0101";	//第三方为首信内卡
		//String appOrderOwner = request.getParameter("username");	//支付者姓名，非必填
		appOrderOwner = new String(appOrderOwner.getBytes("iso-8859-1"), "UTF-8");
		String return_url="http://localhost:8080/receive1.jsp";	//支付完成后的实时返回地址
		String delayurl="http://localhost:8080/receive2.jsp";		//支付完成后的延迟返回地址

		String key = "chinapay";//项目在支付平台的密钥
		String data = projectId + amount + currency + appPaymentId + appOrderOwner + return_url + delayurl;//七个参数的拼串
		SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"),"HmacMD5");  
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());  
		mac.init(secretKey);  
		byte[] code = mac.doFinal(data.getBytes("UTF-8")); 
		
		StringBuffer hash = new StringBuffer();
		for (int i = 0; i < code.length; i++) {
			String hex = Integer.toHexString(0xFF & code[i]);
			if (hex.length() == 1) {
				hash.append('0');
			}
			hash.append(hex);
		}
		String sign = hash.toString(); 
		sign = sign.toUpperCase();
		//将数据放入reqeust中
		request.setAttribute("projectId", projectId);
		request.setAttribute("paymentCompanyId", paymentCompanyId);
		request.setAttribute("amount", amount);
		request.setAttribute("currency", currency);
		request.setAttribute("appPaymentId", appPaymentId);
		request.setAttribute("appOrderOwner", appOrderOwner);
		request.setAttribute("return_url", return_url);
		request.setAttribute("delayurl", delayurl);
		request.setAttribute("sign", sign);
		request.setAttribute("directpay", directpay);
		//转发到支付页面
		request.getRequestDispatcher("http://pay.pku.edu.cn/PayInterface/FromApp").forward(request, response);
	}
}
