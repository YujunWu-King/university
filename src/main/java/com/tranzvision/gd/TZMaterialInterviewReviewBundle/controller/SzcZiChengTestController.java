package com.tranzvision.gd.TZMaterialInterviewReviewBundle.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzClpyDataServiceImpl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.TzMspyDataServiceImpl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.service.impl.XmlToWord;


import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.SqlQuery;



/**
 * 宋子成测试控制器
 * 
 * @author 宋子成
 * @since 2017-01-21
 */
@Service
@Controller
public class SzcZiChengTestController {


	
	@Autowired
	private TzClpyDataServiceImpl tzClpyDataServiceImpl;

	@Autowired
	private TzMspyDataServiceImpl tzmspyDataServiceImpl;

	@Autowired
	private XmlToWord xmlToWord;
	@Autowired
	private SqlQuery jdbcTemplate;

	@RequestMapping("/song")
	public String testUpdate(HttpServletRequest request, HttpServletResponse response) {

		/*测试
		String tstsql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID = ? ORDER BY ROW_ADDED_DTTM";
		List<Map<String, Object>> pw_ks_list = jdbcTemplate.queryForList(tstsql,new Object[] {"ADMIN"});
	

		Map<String, Object> tstAap =new HashMap();
		String tstoprid = "";
		for(int i= 0;i<pw_ks_list.size();i++){
			 tstoprid = pw_ks_list.get(i).get("OPRID").toString();
			System.out.println("tstoprid="+tstoprid);
			tstAap.put(tstoprid,i);
		}
		
		//2 , 39  
		tstAap.put("TZ_39",22222223);
		
		System.out.println("TZ_39="+tstAap.get("TZ_39"));
		System.out.println("TZ_14072="+tstAap.get("TZ_14072"));
		
		return "";
		*/
		//面试
		String oprType = "DCPY";
		String arr[] = {"a","b"};
		String strParams = "{\"TZ_CLASS_ID\":\"105\",\"TZ_APPLY_PC_ID\":\"2\",\"TZ_DQPY_LUNC\":\"02\",\"TZ_PWEI_OPRIDS\":\"MBA_48946=MBA_48991\"}";
		//String strParams = "{\"TZ_CLASS_ID\":\"106\",\"TZ_APPLY_PC_ID\":\"1\",\"TZ_DQPY_LUNC\":\"02\",\"TZ_PWEI_OPRIDS\":\"clpw6\"}";
		String url = tzmspyDataServiceImpl.tzOther(oprType,strParams, arr);
		
		
		System.out.println("url="+url);
		
		return "C:/tstcreateword2/dc_pysj_2017-02-09 01-31-17.doc";
		
	
		/* 材料
		String oprType = "DCPY";
		String arr[] = {"a","b"};
		String strParams = "{\"TZ_CLASS_ID\":\"106\",\"TZ_APPLY_PC_ID\":\"1\",\"TZ_DQPY_LUNC\":\"02\",\"TZ_PWEI_OPRIDS\":\"clpw06=clpw07=clpw08=clpw09\"}";
		//String strParams = "{\"TZ_CLASS_ID\":\"106\",\"TZ_APPLY_PC_ID\":\"1\",\"TZ_DQPY_LUNC\":\"02\",\"TZ_PWEI_OPRIDS\":\"clpw6\"}";
		String url = tzClpyDataServiceImpl.tzOther(oprType,strParams, arr);
		
		
		System.out.println("url="+url);
		
		return "C:/tstcreateword2/dc_pysj_2017-02-09 01-31-17.doc";
		*/
		/*
		try {
			String filepath = "C:/tstcreateword2";
			String fangxiang = "fangxiang";
			String pici = "pici";

			String pws = "clpw6=clpw7=clpw8=clpw9";

			String url = xmlToWord.createWord(filepath, fangxiang, pici, pws);
			System.out.println("--return--" + url);

		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		// testSzcServiceImpl.tzUpdate();
		// System.out.println("88889999");

		// Freemark freemark = new Freemark();
		// System.out.println("freemark-->"+freemark);
		/*
		 * freemark.setTemplateName("wordTemplate-tst.ftl");
		 * freemark.setFileName("tz_doc_" + new SimpleDateFormat(
		 * "yyyy-MM-dd hh-mm-ss").format(new Date()) + ".doc"); //
		 * freemark.setFilePath("bin\\doc\\");
		 * freemark.setFilePath("C:\\tstcreateword\\"); // 生成word
		 * 
		 * try { freemark.createWord_tst3(); } catch (DocumentException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		// File file = new File(DEST);
		// file.getParentFile().mkdirs();
		// new HtmlToFdf().createPdf(DEST);
		/*
		 * try { htmlToFdf.createPdf(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (com.itextpdf.text.DocumentException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

	}

}
