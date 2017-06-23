package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;

//调查问卷--列表显示处理类

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcwjDcAetMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjDcAet;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDattTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzExcelDrxxTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDattT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzExcelDrxxT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.BatchProcessDetailsImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.batch.engine.base.BaseEngine;
import com.tranzvision.gd.batch.engine.base.EngineParameters;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QustionnaireListClsImpl")

public class QustionnaireListClsImpl extends FrameworkImpl{
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzDcWjDyTMapper psTzDcWjDyTMapper;
	
	@Autowired
	private TZGDObject tzGDObject;
	
	@Autowired
	private FliterForm fliterForm;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	
	@Autowired
	private PsTzExcelDattTMapper psTzExcelDattTMapper;

	@Autowired
	private PsTzExcelDrxxTMapper psTzExcelDrxxTMapper;
	
	@Autowired
	private BatchProcessDetailsImpl batchProcessDetailsImpl;
	
	@Autowired
	private PsTzDcwjDcAetMapper psTzDcwjDcAetMapper;

	/* 查询调查表列表 */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(comParams);
			if(jacksonUtil.containsKey("type") 
					&& "expHistory".equals(jacksonUtil.getString("type"))){ //调查结果导出历史
				// 排序字段
				String[][] orderByArr = new String[][] {{"TZ_STARTTIME","DESC"}};

				// json数据要的结果字段;
				String[] resultFldArray = { "TZ_DLZH_ID", "TZ_DR_TASK_DESC", "TZ_STARTTIME", "PROCESSINSTANCE",
						"PROCESS_STATUS","TZ_REALNAME"};

				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

				if (obj != null && obj.length > 0) {

					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);

						Map<String, Object> mapList = new HashMap<String, Object>();
						mapList.put("loginId", rowList[0]);
						mapList.put("fileName", rowList[1]);
						mapList.put("bgTime", rowList[2]);
						mapList.put("procInsId", rowList[3]);
						mapList.put("procState", rowList[4]);
						mapList.put("czPerName", rowList[5]);
						
						String stateDescr = batchProcessDetailsImpl.getBatchProcessStatusDescsr(rowList[4]);
						mapList.put("procStaDescr", stateDescr);
						
						listData.add(mapList);
					}

					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);

				}
			}else{
				// 排序字段如果没有不要赋值
				String[][] orderByArr = new String[][] {};
	
				// json数据要的结果字段;
				String[] resultFldArray = {"TZ_DC_WJ_ID","TZ_DC_WJBT","TZ_JG_ID","TZ_DC_WJ_ZT","TZ_DC_WJ_FB"};
	
				// 可配置搜索通用函数;
				Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);
	
				if (obj != null && obj.length > 0) {
					ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
	
					for (int i = 0; i < list.size(); i++) {
						String[] rowList = list.get(i);
						Map<String, Object> mapList = new HashMap<String, Object>();
						
						mapList.put("TZ_DC_WJ_ID", rowList[0]);
						mapList.put("TZ_DC_WJBT", rowList[1]);
						mapList.put("TZ_JG_ID", rowList[2]);
						mapList.put("TZ_DC_WJ_ZT", rowList[3]);
						mapList.put("TZ_DC_WJ_FB", rowList[4]);
						listData.add(mapList);
					}
	
					mapRet.replace("total", obj[0]);
					mapRet.replace("root", listData);
	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	 /*调查问卷设置保存*/
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		System.out.println("==设置===tzUpdate执行");
		String actResult=null;
		//参数为空直接跳出方法
		if(actData!=null)
		{
			//获取UserId 如何用户为“TZ_GUEST”->"请先登录再操作";
			String userId= tzLoginServiceImpl.getLoginedManagerOprid(request);
			if(userId.equals("TZ_GUEST"))
			{
				errMsg[0]="1";
				errMsg[1]="请先登录再操作";
				return null;
			}
			 for(int i=0;i<actData.length;i++)
			  {
				 System.out.println("====设置update===="+actData[i]);
				 //每次循环结果覆盖上一次结果？ 是否只有一条数据...
				 actResult= tzUpdateFattrInfo(actData[i],errMsg);
			  }
		}
		return actResult;
	}
	/*更新信息*/ 
	@SuppressWarnings("unchecked")
	private String tzUpdateFattrInfo(String strForm,String[] errMsg){
		System.out.println("==设置====tzUpdateFattrInfo执行");
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs=new PsTzDcWjDyTWithBLOBs();
		//机构ID
		String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		psTzDcWjDyTWithBLOBs.setTzJgId(jgId);
		//将String数据转换成Map
		JacksonUtil jacksonUtil=new JacksonUtil();
		jacksonUtil.json2Map(strForm);
		Map<String,Object>dataMap=jacksonUtil.getMap();
		if(dataMap.get("data")==null)
		{
			errMsg[0]="1";
			errMsg[1]="数据不正确";
			return null;
		}
		//System.out.println("==data==="+dataMap.get("data"));
		dataMap=(Map<String, Object>) dataMap.get("data");
		//主键不做非空判断 为空则数据错误
		// 问卷ID
		String TZ_DC_WJ_ID=null;
		if(dataMap.containsKey("TZ_DC_WJ_ID")){
			TZ_DC_WJ_ID=dataMap.get("TZ_DC_WJ_ID").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
		}
		//模板ID
		if(dataMap.containsKey("TZ_APP_TPL_ID")&&dataMap.get("TZ_APP_TPL_ID")!=null){
			String TZ_APP_TPL_ID=dataMap.get("TZ_APP_TPL_ID").toString();
			psTzDcWjDyTWithBLOBs.setTzAppTplId(TZ_APP_TPL_ID);
		}
		//问卷标题
		String TZ_DC_WJBT=null;
		if(dataMap.containsKey("TZ_DC_WJBT")){
			TZ_DC_WJBT=dataMap.get("TZ_DC_WJBT").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjbt(TZ_DC_WJBT);
		}
		//问卷语言 语言不会更新
		//if(dataMap.containsKey("TZ_APP_TPL_LAN"))...
		
		//状态
		String TZ_DC_WJ_ZT="0";
		if(dataMap.containsKey("TZ_DC_WJ_ZT")){
			TZ_DC_WJ_ZT=dataMap.get("TZ_DC_WJ_ZT").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjZt(TZ_DC_WJ_ZT);
		}
		//如果有PS_TZ_DC_WJ_DY_T移除
		if(dataMap.containsKey("TZ_APPTPL_JSON_STR"))
			dataMap.remove("TZ_APPTPL_JSON_STR");
		//发布状态
		String TZ_DC_WJ_FB="0";
		if(dataMap.containsKey("TZ_DC_WJ_FB")){
			TZ_DC_WJ_FB=dataMap.get("TZ_DC_WJ_FB").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjFb(TZ_DC_WJ_FB);
		}
		//卷头内容
		if(dataMap.containsKey("TZ_DC_JTNR")&&dataMap.get("TZ_DC_JTNR")!=null){
			String TZ_DC_JTNR=dataMap.get("TZ_DC_JTNR").toString();
			psTzDcWjDyTWithBLOBs.setTzDcJtnr(TZ_DC_JTNR);
		}
		//卷尾内容(保存的时候不转义，查找的时候在转义);
		if(dataMap.containsKey("TZ_DC_JWNR")&&dataMap.get("TZ_DC_JWNR")!=null)
		{
			String TZ_DC_JWNR=dataMap.get("TZ_DC_JWNR").toString();
			psTzDcWjDyTWithBLOBs.setTzDcJwnr(TZ_DC_JWNR);
		}
		//问卷PC版发布URL;
				if(dataMap.containsKey("TZ_DC_WJ_PC_URL")&&dataMap.get("TZ_DC_WJ_PC_URL")!=null){
					String TZ_DC_WJ_PC_URL=dataMap.get("TZ_DC_WJ_PC_URL").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjPcUrl(TZ_DC_WJ_PC_URL);
				}
				//问卷移动版发布URL
				if(dataMap.containsKey("TZ_DC_WJ_MB_URL")&&dataMap.get("TZ_DC_WJ_MB_URL")!=null){
					String TZ_DC_WJ_MB_URL=dataMap.get("TZ_DC_WJ_MB_URL").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjMbUrl(TZ_DC_WJ_MB_URL);
				}
				//问卷发布URL 字段缺失？
				if(dataMap.containsKey("TZ_DC_WJ_URL")&&dataMap.get("TZ_DC_WJ_URL")!=null){
					String TZ_DC_WJ_URL=dataMap.get("TZ_DC_WJ_URL").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjUrl(TZ_DC_WJ_URL);
				}
				//是否必须登录;
				String TZ_DC_WJ_DLZT="N";
				if(dataMap.containsKey("TZ_DC_WJ_DLZT")&&dataMap.get("TZ_DC_WJ_DLZT")!=null){
					dataMap.get("TZ_DC_WJ_DLZT").toString();
					if(TZ_DC_WJ_DLZT.equals("on"))
						TZ_DC_WJ_DLZT="Y";
					else
						TZ_DC_WJ_DLZT="N";
				}
				psTzDcWjDyTWithBLOBs.setTzDcWjDlzt(TZ_DC_WJ_DLZT);
				//答题规则
				if(dataMap.containsKey("TZ_DC_WJ_DTGZ")&&dataMap.get("TZ_DC_WJ_DTGZ")!=null){
					String TZ_DC_WJ_DTGZ=dataMap.get("TZ_DC_WJ_DTGZ").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjDtgz(TZ_DC_WJ_DTGZ);
				}
				//数据采集规则
				if(dataMap.containsKey("TZ_DC_WJ_IPGZ")&&dataMap.get("TZ_DC_WJ_IPGZ")!=null){
					String TZ_DC_WJ_IPGZ=dataMap.get("TZ_DC_WJ_IPGZ").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjIpgz(TZ_DC_WJ_IPGZ);
				}
				//问卷完成规则
				if(dataMap.containsKey("TZ_DC_WJ_JSGZ")&&dataMap.get("TZ_DC_WJ_JSGZ")!=null){
					String TZ_DC_WJ_JSGZ=dataMap.get("TZ_DC_WJ_JSGZ").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjJsgz(TZ_DC_WJ_JSGZ);
				}
				//问卷密码
				if(dataMap.containsKey("TZ_DC_WJ_PWD")&&dataMap.get("TZ_DC_WJ_PWD")!=null){
					String TZ_DC_WJ_PWD=dataMap.get("TZ_DC_WJ_PWD").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjPwd(TZ_DC_WJ_PWD);
					//是否需要密码
				}
					String TZ_DC_WJ_NEEDPWD=dataMap.get("TZ_DC_WJ_NEEDPWD")==null?"N":dataMap.get("TZ_DC_WJ_NEEDPWD").toString();
					if(TZ_DC_WJ_NEEDPWD!=null&&TZ_DC_WJ_NEEDPWD.equals("on"))
						TZ_DC_WJ_NEEDPWD="Y";
					else
						TZ_DC_WJ_NEEDPWD="N";
					
				psTzDcWjDyTWithBLOBs.setTzDcWjNeedpwd(TZ_DC_WJ_NEEDPWD);
				
				//是否使用前倒页 字段缺失？
				if(dataMap.containsKey("TZ_DC_WJ_QYQD")&&dataMap.get("TZ_DC_WJ_QYQD")!=null){
					String TZ_DC_WJ_QYQD=dataMap.get("TZ_DC_WJ_QYQD").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjQyqd(TZ_DC_WJ_QYQD);
				}
				//前导页内容    字段缺失？
				if(dataMap.containsKey("TZ_DC_WJ_QDNR")&&dataMap.get("TZ_DC_WJ_QDNR")!=null){
					String TZ_DC_WJ_QDNR=dataMap.get("TZ_DC_WJ_QDNR").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjQdnr(TZ_DC_WJ_QDNR);
				}
				//结果页内容 字段缺失？
				if(dataMap.containsKey("TZ_DC_WJ_JGNR")&&dataMap.get("TZ_DC_WJ_JGNR")!=null){
					String TZ_DC_WJ_JGNR=dataMap.get("TZ_DC_WJ_JGNR").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjJgnr(TZ_DC_WJ_JGNR);
				}
				//是否跳转 字段缺失？
				if(dataMap.containsKey("TZ_DC_WJ_SFTZ")&&dataMap.get("TZ_DC_WJ_SFTZ")!=null){
					String TZ_DC_WJ_SFTZ=dataMap.get("TZ_DC_WJ_SFTZ").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjSftz(TZ_DC_WJ_SFTZ);
				}
				//跳转地址
				if(dataMap.containsKey("TZ_DC_WJ_TZDZ")&&dataMap.get("TZ_DC_WJ_TZDZ")!=null){
					String TZ_DC_WJ_TZDZ=dataMap.get("TZ_DC_WJ_TZDZ").toString();
					psTzDcWjDyTWithBLOBs.setTzDcWjTzdz(TZ_DC_WJ_TZDZ);
				}
				//停留时间
				
				if(dataMap.containsKey("TZ_DC_WJ_TLSJ")&&dataMap.get("TZ_DC_WJ_TLSJ")!=null){
					String TZ_DC_WJ_TLSJ=dataMap.get("TZ_DC_WJ_TLSJ").toString();
					
					psTzDcWjDyTWithBLOBs.setTzDcWjTlsj(TZ_DC_WJ_TLSJ);
				}
				//听众列表 将听众ID分离成为一个字符串数组
				//if(dataMap.containsKey("AudList")&&dataMap.get("AudList")!=null){
				//	ArrayList<String> strListenersId=new ArrayList<String>();
				//	strListenersId=(ArrayList<String>) dataMap.get("AudList");
				//}
				
				/*去重判断*/ //听众列表 座位ID是什么鬼？
				//String isRepeatedSQL="SELECT 'Y' FROM PS_TZ_DC_WJ_DY_T WHERE TZ_JG_ID=? AND TZ_DC_WJBT=? AND TZ_DC_WJ_ID=? AND SETID=?";     
				//List result=jdbcTemplate.queryForList(isRepeatedSQL, new Object[]{jgId,TZ_DC_WJBT,TZ_DC_WJ_ID,tmpSetID}) ;     
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
		try{
		//开始日期
		String TZ_DC_WJ_KSRQ=null;	
		if(dataMap.containsKey("TZ_DC_WJ_KSRQ")&&dataMap.get("TZ_DC_WJ_KSRQ")!=null){
			TZ_DC_WJ_KSRQ=dataMap.get("TZ_DC_WJ_KSRQ").toString().trim();
			psTzDcWjDyTWithBLOBs.setTzDcWjKsrq(dateFormat.parse(TZ_DC_WJ_KSRQ));
		}
		//开始时间
		String TZ_DC_WJ_KSSJ=null;
		if(dataMap.containsKey("TZ_DC_WJ_KSSJ")&&dataMap.get("TZ_DC_WJ_KSSJ")!=null){
			TZ_DC_WJ_KSSJ=dataMap.get("TZ_DC_WJ_KSSJ").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjKssj(timeFormat.parse(TZ_DC_WJ_KSSJ));
		}
		//结束日期
		String TZ_DC_WJ_JSRQ=null;
		if(dataMap.containsKey("TZ_DC_WJ_JSRQ")&&dataMap.get("TZ_DC_WJ_JSRQ")!=null){
			TZ_DC_WJ_JSRQ=dataMap.get("TZ_DC_WJ_JSRQ").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjJsrq(dateFormat.parse(TZ_DC_WJ_JSRQ));
		}
		//结束时间
		String TZ_DC_WJ_JSSJ=null;
		if(dataMap.containsKey("TZ_DC_WJ_JSSJ")&&dataMap.get("TZ_DC_WJ_JSSJ")!=null){
			TZ_DC_WJ_JSSJ=dataMap.get("TZ_DC_WJ_JSSJ").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjJssj(timeFormat.parse(TZ_DC_WJ_JSSJ));
		}
		
		
		 //--------------------状态控制-------------
		if(TZ_DC_WJ_JSRQ!=null&&TZ_DC_WJ_KSRQ!=null&&TZ_DC_WJ_FB.equals("1")){
		 	Date nowDate=new Date();
		 	Date nowTime=nowDate;

		    nowDate=dateFormat.parse(dateFormat.format(nowDate));
		    nowTime=timeFormat.parse(timeFormat.format(nowTime));
			System.out.println("nowDate:"+dateFormat.format(nowDate)+"==nowDate.getTime():"+nowDate.getTime());
			System.out.println("nowTime:"+timeFormat.format(nowTime)+"==nowTime.getTime():"+nowTime.getTime());
				//结束时间重组为正确时间
			System.out.println("结束："+TZ_DC_WJ_JSRQ+" "+TZ_DC_WJ_JSSJ);
			System.out.println("Long值:"+dateFormat.parse(TZ_DC_WJ_JSRQ).getTime()+"=="+timeFormat.parse(TZ_DC_WJ_JSSJ).getTime());
			    //开始时间重组为正确时间
			System.out.println("开始："+TZ_DC_WJ_KSRQ+" "+TZ_DC_WJ_KSSJ);
			System.out.println("Long值:"+dateFormat.parse(TZ_DC_WJ_KSRQ).getTime()+"=="+timeFormat.parse(TZ_DC_WJ_KSSJ).getTime());
			    //+"=="+timeFormat.parse(TZ_DC_WJ_KSSJ).getTime()
			    //sruvyDataMap.replace("TZ_DC_WJ_KSSJ", TZ_DC_WJ_KSRQ+" "+TZ_DC_WJ_KSSJ);
			    //userId如何获取？
			if(!TZ_DC_WJ_ZT.equals("2")&&!TZ_DC_WJ_ZT.equals("3")) 
			if(!TZ_DC_WJ_JSRQ.equals("")){
			    	if(dateFormat.parse(TZ_DC_WJ_JSRQ).getTime()<nowDate.getTime()||dateFormat.parse(TZ_DC_WJ_JSRQ).getTime()==nowDate.getTime()&&timeFormat.parse(TZ_DC_WJ_JSSJ).getTime()<nowTime.getTime())
			    	{
			    		System.out.println("===问卷结束===");
			    		TZ_DC_WJ_ZT="3";
			    	}
			    	//状态为已发布  当前时间处于 开始时间和结束时间之间则将状态设置为"进行中"
			    	else if(nowDate.getTime()<dateFormat.parse(TZ_DC_WJ_JSRQ).getTime()&&dateFormat.parse(TZ_DC_WJ_KSRQ).getTime()<nowDate.getTime()){
			    		System.out.println("===问卷进行中 状况1===");
			    		TZ_DC_WJ_ZT="1";
			    	}
			    	else if(dateFormat.parse(TZ_DC_WJ_JSRQ).getTime()==nowDate.getTime()&&(nowTime.getTime()<timeFormat.parse(TZ_DC_WJ_JSSJ).getTime()||timeFormat.parse(TZ_DC_WJ_JSSJ).getTime()==nowTime.getTime())){
			    		System.out.println("===问卷进行中 状况2===");
			    		TZ_DC_WJ_ZT="1";
			    	}
			    	else if(dateFormat.parse(TZ_DC_WJ_KSRQ).getTime()==nowDate.getTime()&&(timeFormat.parse(TZ_DC_WJ_KSSJ).getTime()<nowTime.getTime()||timeFormat.parse(TZ_DC_WJ_KSSJ).getTime()==nowTime.getTime())){
			    		System.out.println("===问卷进行中 状况3===");
			    		TZ_DC_WJ_ZT="1";
			    	}
			    	else{
			    		System.out.println("===未开始===");
			    		TZ_DC_WJ_ZT="0";
			    	}
			    	psTzDcWjDyTWithBLOBs.setTzDcWjZt(TZ_DC_WJ_ZT);
			    	}
		}
		 //----------------------------------------------------------------------------
		}
		catch(Exception e){
			errMsg[0]="1";
			errMsg[1]="日期转换成错误";
					return null;
		}
		//保存数据 选择性      
		psTzDcWjDyTMapper.updateByPrimaryKeySelective(psTzDcWjDyTWithBLOBs);
		return new JacksonUtil().Map2json(dataMap);
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		System.out.println("===查找模板==strParams:"+strParams);
		String sql = "SELECT TZ_APP_TPL_MC,TZ_APP_TPL_ID FROM PS_TZ_DC_DY_T";
		List<HashMap<String,Object>>resultList=new ArrayList<HashMap<String,Object>>();
		List<HashMap<String,Object>>returnList=new ArrayList<HashMap<String,Object>>();
		resultList=jdbcTemplate.queryForList(sql);
		if(resultList!=null){
			for(int i=0;i<resultList.size();i++){
				HashMap<String,Object>returnMap=new HashMap<String,Object>();
				returnMap.put("tplid", resultList.get(i).get("TZ_APP_TPL_ID"));
				returnMap.put("tplname", resultList.get(i).get("TZ_APP_TPL_MC"));
				returnList.add(returnMap);
			}
			return new JacksonUtil().List2json((ArrayList<?>) returnList);
		}
		return null;
	}
	
	
	/**
	 * 下载导出
	 */
	@Override
	public String tzOther(String strType, String strParams, String[] errorMsg) {
		String strRet = "";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);
			
			if("DOWNLOAD".equals(strType)){
				//下载导出excel
				String filePath = "";
				String strProcInsId = jacksonUtil.getString("procInsId");
				if(!"".equals(strProcInsId)){
					int procInsId = Integer.parseInt(strProcInsId);
					
					PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
					if(psTzExcelDattT != null){
						filePath = psTzExcelDattT.getTzFwqFwlj();
						if(!"".equals(filePath) && filePath != null){
							filePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
							+ request.getContextPath() + filePath;
						}
					}
				}
				mapRet.put("filePath", filePath);
				strRet = jacksonUtil.Map2json(mapRet);
				
			}else if("EXPORT".equals(strType)){
				//确认导出

				String wjId = jacksonUtil.getString("wjId");
				String fileName = jacksonUtil.getString("fileName");
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				String downloadPath = getSysHardCodeVal.getDownloadPath();
				
				// 调查问卷导出excel存储路径
				String eventExcelPath = "/survey/xlsx";
				
				// 完整的存储路径
				String expDirPath = downloadPath + eventExcelPath + "/" + getDateNow();
				String absexpDirPath = request.getServletContext().getRealPath(expDirPath);
				
				/*生成运行控制ID*/
				SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMddHHmmss");
			    String s_dt = dateFormate.format(new Date());
				String runCntlId = "ZXDCJG" + s_dt + "_" + getSeqNum.getSeqNum("TZ_ZXDC_WJGL_COM", "DCJG_EXPORT");
				
				
				PsTzDcwjDcAet psTzDcwjDcAet = new PsTzDcwjDcAet();
				psTzDcwjDcAet.setRunCntlId(runCntlId);
				psTzDcwjDcAet.setTzDcWjId(wjId);
				psTzDcwjDcAet.setTzRelUrl(expDirPath);
				psTzDcwjDcAet.setTzJdUrl(absexpDirPath);
				psTzDcwjDcAetMapper.insert(psTzDcwjDcAet);
				
				
				String currentAccountId = tzLoginServiceImpl.getLoginedManagerDlzhid(request);
				String currentOrgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
				
				BaseEngine tmpEngine = tzGDObject.createEngineProcess(currentOrgId, "TZ_ZXDC_EXP_PROC");
				//指定调度作业的相关参数
				EngineParameters schdProcessParameters = new EngineParameters();

				schdProcessParameters.setBatchServer("");
				schdProcessParameters.setCycleExpression("");
				schdProcessParameters.setLoginUserAccount(currentAccountId);
				schdProcessParameters.setPlanExcuteDateTime(new Date());
				schdProcessParameters.setRunControlId(runCntlId);
				
				//调度作业
				tmpEngine.schedule(schdProcessParameters);
				
				// 进程实例id;
				int processinstance = tmpEngine.getProcessInstanceID();
				if(processinstance>0){
					PsTzExcelDrxxT psTzExcelDrxxT = new PsTzExcelDrxxT();
					psTzExcelDrxxT.setProcessinstance(processinstance);
					psTzExcelDrxxT.setTzComId("TZ_ZXDC_WJGL_COM");
					psTzExcelDrxxT.setTzPageId("TZ_ZXDC_WJGL_STD");
					//存放活动ID
					psTzExcelDrxxT.setTzDrLxbh(wjId);
					psTzExcelDrxxT.setTzDrTaskDesc(fileName); 
					psTzExcelDrxxT.setTzStartDtt(new Date());
					psTzExcelDrxxT.setOprid(oprid);
					psTzExcelDrxxT.setTzIsViewAtt("Y");
					psTzExcelDrxxTMapper.insert(psTzExcelDrxxT);
					
					
					// 生成本次导出的文件名
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					Random random = new Random();
					int max = 999999999;
					int min = 100000000;
					String sysFileName = simpleDateFormat.format(new Date()) + "_" + oprid.toUpperCase() + "_"
							+ String.valueOf(random.nextInt(max) % (max - min + 1) + min) + ".xlsx";
					
					PsTzExcelDattT psTzExcelDattT = new PsTzExcelDattT();
					psTzExcelDattT.setProcessinstance(processinstance);
					psTzExcelDattT.setTzSysfileName(sysFileName);
					psTzExcelDattT.setTzFileName(fileName);
					psTzExcelDattT.setTzCfLj("A");
					psTzExcelDattT.setTzFjRecName("");
					psTzExcelDattT.setTzFwqFwlj(""); 
					psTzExcelDattTMapper.insert(psTzExcelDattT);
					
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = "操作异常。" + e.getMessage();
		}
	
		return strRet;
	}
	
	
	/***
	 * 删除导出历史记录
	 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {

				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				//进程实例ID
				int procInsId = Integer.parseInt(jacksonUtil.getString("procInsId"));
				
				PsTzExcelDattT psTzExcelDattT = psTzExcelDattTMapper.selectByPrimaryKey(procInsId);
				if(psTzExcelDattT != null){
					String filePath = psTzExcelDattT.getTzFwqFwlj();
					if(!"".equals(filePath) && filePath != null){
						filePath = request.getServletContext().getRealPath(filePath);
						
						File file = new File(filePath);
						if(file.exists() && file.isFile()){
							file.delete();
						}
					}
				}
				
				psTzExcelDrxxTMapper.deleteByPrimaryKey(procInsId);
				psTzExcelDattTMapper.deleteByPrimaryKey(procInsId);
			}

			errMsg[0] = "0";
			errMsg[1] = "保存成功。";
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = "保存失败。" + e.getMessage();
		}

		return strRet;
	}
	
	
	/**
	 * 创建日期目录名
	 * 
	 * @return
	 */
	private String getDateNow() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		int day = cal.get(5);
		return (new StringBuilder()).append(year).append(month).append(day).toString();
	}
}
