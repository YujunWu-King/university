package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjAppclsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzSureyAudTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjAppclsT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzSureyAudT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QuestionnaireSettingImpl")
public class QuestionnaireSettingImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzDcWjDyTMapper psTzDcWjDyTMapper;
	@Autowired
	private PsTzDcWjAppclsTMapper psTzDcWjAppclsTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzSureyAudTMapper PsTzSureyAudTMapper;
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String wjId = jacksonUtil.getString("wjId");
		ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
		try {
			int total = 0;
			// 查询总数;
			
			String totalSQL = "SELECT COUNT(1) FROM PS_TZ_DC_WJ_APPCLS_T WHERE TZ_DC_WJ_ID=?";
			total = jdbcTemplate.queryForObject(totalSQL, new Object[] { wjId },"Integer");
			String sql = "SELECT TZ_SEQNUM,TZ_APPCLS_ID,TZ_APPCLS_TYPE,TZ_QY_STATUS FROM PS_TZ_DC_WJ_APPCLS_T WHERE TZ_DC_WJ_ID=? LIMIT ?,?";
			List<?> listData = jdbcTemplate.queryForList(sql, new Object[] {wjId,numStart,numLimit });
			for (Object objData : listData) {

				Map<String, Object> mapData = (Map<String, Object>) objData;
				int TZ_SEQNUM=0;
				String TZ_APPCLS_ID = "";
				String TZ_APPCLS_TYPE = "";
				String TZ_QY_STATUS="";
				String TZ_APPCLS_TYPE_DESC="";
				Boolean tzIfUse=false;
				TZ_SEQNUM=Integer.valueOf(String.valueOf(mapData.get("TZ_SEQNUM")));
				if(mapData.get("TZ_APPCLS_ID")!=null){
					TZ_APPCLS_ID = String.valueOf(mapData.get("TZ_APPCLS_ID"));
				}
				if(mapData.get("TZ_APPCLS_TYPE")!=null){
					TZ_APPCLS_TYPE = String.valueOf(mapData.get("TZ_APPCLS_TYPE"));
					TZ_APPCLS_TYPE_DESC=jdbcTemplate.queryForObject("select TZ_ZHZ_CMS from PS_TZ_PT_ZHZXX_TBL where TZ_ZHZJH_ID='TZ_APP_CLS_TYPE' AND TZ_ZHZ_ID=? AND TZ_EFF_STATUS='A'", new Object[]{TZ_APPCLS_TYPE}, "String");
				}
				
				TZ_QY_STATUS = String.valueOf(mapData.get("TZ_QY_STATUS"));
				if("Y".equals(TZ_QY_STATUS)){
					tzIfUse=true;
				}
	            String TZ_APPCLS_NAME="";
	            if(null!=TZ_APPCLS_ID&&!"".equals(TZ_APPCLS_ID)){
	              TZ_APPCLS_NAME=jdbcTemplate.queryForObject("select TZ_DESCR100 from PS_TZ_APPCLS_TBL where TZ_APPCLS_ID=?", new Object[]{TZ_APPCLS_ID}, "String");
	            }
				Map<String, Object> mapJson = new HashMap<String, Object>();
				mapJson.put("tzSeqNum", TZ_SEQNUM);
				mapJson.put("wjId", wjId);
				mapJson.put("tzAppclsID", TZ_APPCLS_ID);
				mapJson.put("tzAppclsName", TZ_APPCLS_NAME);
				mapJson.put("tzAppclsType", TZ_APPCLS_TYPE);
				mapJson.put("tzAppclsTypeDesc", TZ_APPCLS_TYPE_DESC);
				mapJson.put("tzIfUse", tzIfUse);
			
				listJson.add(mapJson);
				mapRet.replace("total",total);
			}
			mapRet.replace("root", listJson);
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	@Override
	public String tzQuery(String strParams, String[] errMsg) {
			String strComContent="{}";
			
			for(int i=0;i<errMsg.length;i++)
			{
				System.out.println("====tzQuery====errMsg:");
				System.out.println(errMsg[i]);
			}
			//如果错误 则返回空--->什么错误？暂不处理
			System.out.println("=====tzQuery====strParams:"+strParams);
			JacksonUtil jacksonUtil=new JacksonUtil();
			jacksonUtil.json2Map(strParams);
			String wjId=jacksonUtil.getString("wjId");
			if(wjId==null||wjId.equals(""))
				return null;
			//机构Id怎么获取的？
			//机构ID
			String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
		    String TZ_APP_TPL_ID, TZ_DC_WJBT, TZ_DC_WJ_ZT, TZ_DC_WJ_FB, TZ_APPTPL_JSON_STR, TZ_APP_TPL_MC;
		    String TZ_DC_WJ_KSRQ, TZ_DC_WJ_KSSJ, TZ_DC_WJ_JSRQ, TZ_DC_WJ_JSSJ, TZ_DC_WJ_PC_URL, TZ_DC_WJ_MB_URL, TZ_DC_WJ_DLZT, TZ_DC_WJ_DTGZ, TZ_DC_WJ_IPGZ, TZ_DC_WJ_JSGZ, TZ_DC_WJ_NM, TZ_DC_WJ_NEEDPWD, TZ_DC_WJ_PWD, TZ_DC_JTNR, TZ_DC_JWNR, TZ_DC_WJ_URL, TZ_DC_WJ_QYQD, TZ_DC_WJ_QDNR, TZ_DC_WJ_JGNR, TZ_DC_WJ_SFTZ, TZ_DC_WJ_TZDZ, TZ_DC_WJ_TLSJ, TZ_APP_TPL_LAN,TZ_DC_WJ_WX;
		    //从问卷表中查询所有数据
		    String getSurvyInfoSQL="SELECT TZ_DC_WJ_ID,TZ_APP_TPL_ID,TZ_DC_WJBT,TZ_DC_WJ_ZT,TZ_DC_WJ_FB,date_format(TZ_DC_WJ_KSRQ,'%Y-%m-%d') TZ_DC_WJ_KSRQ,date_format(TZ_DC_WJ_KSSJ,'%H:%i:%s') TZ_DC_WJ_KSSJ,date_format(TZ_DC_WJ_JSRQ,'%Y-%m-%d') TZ_DC_WJ_JSRQ,date_format(TZ_DC_WJ_JSSJ,'%H:%i:%s') TZ_DC_WJ_JSSJ,TZ_DC_WJ_PC_URL,TZ_DC_WJ_MB_URL,TZ_DC_WJ_DLZT,TZ_DC_WJ_DTGZ,TZ_DC_WJ_IPGZ,TZ_DC_WJ_JSGZ,TZ_DC_WJ_NM,TZ_DC_WJ_NEEDPWD,TZ_DC_WJ_PWD,TZ_APPTPL_JSON_STR,TZ_DC_JTNR,TZ_DC_JWNR,TZ_DC_WJ_URL,TZ_DC_WJ_QYQD,TZ_DC_WJ_QDNR,TZ_DC_WJ_JGNR,TZ_DC_WJ_SFTZ,TZ_DC_WJ_TZDZ,TZ_DC_WJ_TLSJ,TZ_APP_TPL_LAN,TZ_DC_WJ_WX FROM PS_TZ_DC_WJ_DY_T WHERE TZ_DC_WJ_ID=? and TZ_JG_ID=?";
		   
		    Map<String,Object>sruvyDataMap=new HashMap<String,Object>();
		    sruvyDataMap=jdbcTemplate.queryForMap(getSurvyInfoSQL, new Object[]{wjId,jgId});
		    
		    TZ_DC_WJ_TLSJ=sruvyDataMap.get("TZ_DC_WJ_TLSJ")==null?"0":sruvyDataMap.get("TZ_DC_WJ_TLSJ").toString();
		    //从问卷模板中查询模板名称
		    TZ_APP_TPL_ID=sruvyDataMap.get("TZ_APP_TPL_ID")==null?"":sruvyDataMap.get("TZ_APP_TPL_ID").toString();
		    if(!TZ_APP_TPL_ID.equals("")){
		    String getTmplateInfoSQL="SELECT TZ_APP_TPL_MC FROM PS_TZ_DC_DY_T where TZ_APP_TPL_ID=? and  TZ_JG_ID=?";
		    TZ_APP_TPL_MC=jdbcTemplate.queryForObject(getTmplateInfoSQL, new Object[]{TZ_APP_TPL_ID,jgId},"String");
		    }
		    else{
		    	TZ_APP_TPL_MC="";
		    }
		    //发布
		    TZ_DC_WJ_FB=sruvyDataMap.get("TZ_DC_WJ_FB")==null?"0":sruvyDataMap.get("TZ_DC_WJ_FB").toString();
		    
		    if(TZ_DC_WJ_FB.equals("1")&&wjId!=null)
//		    	sruvyDataMap.replace("TZ_DC_WJ_URL", request.getContextPath()+"/dispatcher?classid=surveyapp&SURVEY_WJ_ID="+wjId);
		    	sruvyDataMap.replace("TZ_DC_WJ_URL", request.getContextPath()+"/s?SURVEY_WJ_ID="+wjId);
			   
		    //主要数据存储字符串
		    TZ_APPTPL_JSON_STR=sruvyDataMap.get("sruvyDataMap")==null?"":sruvyDataMap.get("sruvyDataMap").toString();
		    //问卷标题
		    TZ_DC_WJBT=sruvyDataMap.get("TZ_DC_WJBT")==null?"":sruvyDataMap.get("TZ_DC_WJBT").toString();
		    TZ_DC_WJ_KSSJ=sruvyDataMap.get("TZ_DC_WJ_KSSJ")==null?"09:00:00":sruvyDataMap.get("TZ_DC_WJ_KSSJ").toString();
		    //问卷开始时间
		    TZ_DC_WJ_JSSJ=sruvyDataMap.get("TZ_DC_WJ_JSSJ")==null?"18:00:00":sruvyDataMap.get("TZ_DC_WJ_JSSJ").toString();    
		    //给答题规则一个默认值     
		    TZ_DC_WJ_DTGZ=sruvyDataMap.get("TZ_DC_WJ_DTGZ")==null?"0":sruvyDataMap.get("TZ_DC_WJ_DTGZ").toString();
		    
		    TZ_DC_WJ_DLZT=sruvyDataMap.get("TZ_DC_WJ_DLZT")==null?"":sruvyDataMap.get("TZ_DC_WJ_DLZT").toString();
		    //数据采集规则TZ_DC_WJ_IPGZ
		    if(sruvyDataMap.get("TZ_DC_WJ_IPGZ")==null&&TZ_DC_WJ_DLZT.equals("Y"))
		    	TZ_DC_WJ_IPGZ="2";
		    if(sruvyDataMap.get("TZ_DC_WJ_IPGZ")==null&&TZ_DC_WJ_DLZT.equals("N"))
		    	TZ_DC_WJ_IPGZ="3";
		    //完成规则
		    TZ_DC_WJ_JSGZ=sruvyDataMap.get("TZ_DC_WJ_JSGZ")==null?"1":sruvyDataMap.get("TZ_DC_WJ_JSGZ").toString();
		    //微信
		    TZ_DC_WJ_WX=sruvyDataMap.get("TZ_DC_WJ_WX")==null?"N":sruvyDataMap.get("TZ_DC_WJ_WX").toString();
		    //听众列表 听众不唯一？
		    String strAudID,strAudName;
		    
		    //PS_TZ_SURVEY_AUD_T 数据表缺失？PS_TZ_AUDIENCE_VW?
		    String sqlTag="SELECT A.TZ_AUD_ID,B.TZ_AUD_NAM FROM PS_TZ_SURVEY_AUD_T A,PS_TZ_AUDIENCE_VW B WHERE  A.TZ_AUD_ID=B.TZ_AUD_ID AND TZ_DC_WJ_ID=?";
		    
    
		    List<Map<String, Object>>AudList=new ArrayList<Map<String, Object>>();
		    AudList=jdbcTemplate.queryForList(sqlTag, new Object[]{wjId});
		    List<String>AudNameList=new ArrayList<String>();
		    List<String>AudIDList=new ArrayList<String>();
		    
			if (AudList != null && AudList.size() > 0) {
				for (int i = 0; i < AudList.size(); i++) {
					//AudList.get(i).get("TZ_AUD_ID");
					//AudList.get(i).get("TZ_AUD_NAM");
					AudNameList.add((String) AudList.get(i).get("TZ_AUD_NAM"));
					AudIDList.add((String) AudList.get(i).get("TZ_AUD_ID"));
					
				}
			}
			sruvyDataMap.put("AudID",AudIDList);
			sruvyDataMap.put("AudName",AudNameList);
			System.out.println(sruvyDataMap.put("AudName",AudNameList));
			
			
		    
		    TZ_DC_WJ_JSRQ=sruvyDataMap.get("TZ_DC_WJ_JSRQ")==null?null:sruvyDataMap.get("TZ_DC_WJ_JSRQ").toString();
		    TZ_DC_WJ_ZT=sruvyDataMap.get("TZ_DC_WJ_ZT")==null?"0":sruvyDataMap.get("TZ_DC_WJ_ZT").toString();
		    TZ_DC_WJ_KSRQ=sruvyDataMap.get("TZ_DC_WJ_KSRQ")==null?null:sruvyDataMap.get("TZ_DC_WJ_KSRQ").toString();
		    
		    DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss"); 
		    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		    Date nowDate=new Date();
		    Date nowTime=nowDate;
		    try {
		    if(TZ_DC_WJ_JSRQ!=null&&TZ_DC_WJ_KSRQ!=null&&TZ_DC_WJ_FB.equals("1")){
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
			    String userId=tzLoginServiceImpl.getLoginedManagerOprid(request);
			    //结束日期和结束时间小于系统时间 则结束
			    if(!TZ_DC_WJ_ZT.equals("2")&&!TZ_DC_WJ_ZT.equals("3"))
			    if(!TZ_DC_WJ_JSRQ.equals("")){
			    	PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs=new PsTzDcWjDyTWithBLOBs();
			    	//根据问卷ID查询
			    	psTzDcWjDyTWithBLOBs=psTzDcWjDyTMapper.selectByPrimaryKey(wjId);
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
			    		//根据问卷ID查询
			    	}
			    	if(psTzDcWjDyTWithBLOBs!=null){
			    		psTzDcWjDyTWithBLOBs.setTzDcWjZt(TZ_DC_WJ_ZT);
			    		sruvyDataMap.replace("TZ_DC_WJ_ZT", TZ_DC_WJ_ZT);
			    		psTzDcWjDyTWithBLOBs.setRowLastmantDttm(new Date());
			    		//userId如何获取？
			    		psTzDcWjDyTWithBLOBs.setRowLastmantOprid(userId);
			    		psTzDcWjDyTMapper.updateByPrimaryKeyWithBLOBs(psTzDcWjDyTWithBLOBs);
			    	}
			    	}
		    	}
			    }
		    catch(Exception e){
		    	e.printStackTrace();
		    }
		   //TZ_DC_WJ_URL=sruvyDataMap.get("TZ_DC_WJ_URL")==null?"":sruvyDataMap.get("TZ_DC_WJ_URL").toString();
		   // TZ_APP_TPL_LAN=sruvyDataMap.get("TZ_APP_TPL_LAN")==null?"":sruvyDataMap.get("TZ_APP_TPL_LAN").toString();

		    
		    //TZ_APPTPL_JSON_STR没有用到暂时移除
		    sruvyDataMap.remove("TZ_APPTPL_JSON_STR");
		    
		    JacksonUtil josnUtil=new JacksonUtil();
		    strComContent=josnUtil.Map2json(sruvyDataMap);
		    strComContent="{\"formData\":"+strComContent+"}";
		    // &strComContent = GetHTMLText(HTML.TZ_GD_ZXDC_WJSZ_HTML, &wjId, &TZ_APP_TPL_ID, &TZ_APP_TPL_MC, &TZ_DC_WJBT, &TZ_DC_WJ_ZT, &TZ_DC_WJ_FB, &TZ_DC_WJ_KSRQ, &TZ_DC_WJ_KSSJ, &TZ_DC_WJ_JSRQ, &TZ_DC_WJ_JSSJ, &TZ_DC_WJ_PC_URL, &TZ_DC_WJ_MB_URL, &TZ_DC_WJ_DLZT, &TZ_DC_WJ_DTGZ, &TZ_DC_WJ_IPGZ, &TZ_DC_WJ_JSGZ, &TZ_DC_WJ_NM, &TZ_DC_WJ_NEEDPWD, &TZ_DC_WJ_PWD, &TZ_APPTPL_JSON_STR, %This.Transformchar(&TZ_DC_JTNR), %This.Transformchar(&TZ_DC_JWNR), &TZ_DC_WJ_URL, &TZ_DC_WJ_QYQD, %This.Transformchar(&TZ_DC_WJ_QDNR), %This.Transformchar(&TZ_DC_WJ_JGNR), &TZ_DC_WJ_SFTZ, &TZ_DC_WJ_TZDZ, &TZ_DC_WJ_TLSJ, &TZ_APP_TPL_LAN, &strAudIDList, &strAudNameList);
		    //&strComContent = "{""formData"":" | &strComContent | "}";
		    System.out.println("=====before======");
		    System.out.println(strComContent);
		    System.out.println("=====after=====");
		return strComContent;
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
		//System.out.println("strForm:"+strForm);
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs=new PsTzDcWjDyTWithBLOBs();
		//机构ID
		String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		psTzDcWjDyTWithBLOBs.setTzJgId(jgId);
		//将String数据转换成Map
		JacksonUtil jacksonUtil=new JacksonUtil();
		jacksonUtil.json2Map(strForm);
		
		String typeFlag = jacksonUtil.getString("typeFlag");
		Map<String, Object> dataMap = jacksonUtil.getMap("data");
	if("FORM".equals(typeFlag)){
		
		//主键不做非空判断 为空则数据错误
		// 问卷ID
		String TZ_DC_WJ_ID=null;
		if(dataMap.containsKey("TZ_DC_WJ_ID")){
			TZ_DC_WJ_ID=dataMap.get("TZ_DC_WJ_ID").toString();
			psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
		}
		//模板ID 不为null则用模板表中数据覆盖问卷表中数据
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
			//dataMap.remove("TZ_APPTPL_JSON_STR");
		{
			String TZ_APPTPL_JSON_STR=dataMap.get("TZ_APPTPL_JSON_STR").toString();
			psTzDcWjDyTWithBLOBs.setTzApptplJsonStr(TZ_APPTPL_JSON_STR);
		}
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
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat fullTimeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		String fullEndTimeStr=TZ_DC_WJ_JSRQ+" "+TZ_DC_WJ_JSSJ;
		String fullStartTimeStr=TZ_DC_WJ_KSRQ+" "+TZ_DC_WJ_KSSJ;
		if(fullTimeFormat.parse(fullEndTimeStr).getTime()<fullTimeFormat.parse(fullStartTimeStr).getTime()){
			errMsg[0]="1";
			errMsg[1]="问卷开始日期大于结束日期，请重新填写！";
			return "";
		}
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
			TZ_DC_WJ_DLZT=dataMap.get("TZ_DC_WJ_DLZT").toString();
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
		//获取微信授权
		if(dataMap.containsKey("TZ_DC_WJ_WX")&&dataMap.get("TZ_DC_WJ_WX")!=null){
			String TZ_DC_WJ_WX=dataMap.get("TZ_DC_WJ_WX").toString();
			
			psTzDcWjDyTWithBLOBs.setTzDcWjWx(TZ_DC_WJ_WX);
		}
		//听众列表 将听众ID分离成为一个字符串数组
		
		
		if(dataMap.containsKey("AudList")&& dataMap.get("AudList")!=null && !dataMap.get("AudList").toString().equals("")){
			ArrayList<String> strListenersId=new ArrayList<String>();
			strListenersId=(ArrayList<String>) dataMap.get("AudList");
			PsTzSureyAudT  PsTzSureyAudT=new PsTzSureyAudT();
			for (int i = 0; i < strListenersId.size(); i++) {
				
				String sql = "select COUNT(1) from PS_TZ_SURVEY_AUD_T WHERE TZ_DC_WJ_ID=? AND TZ_AUD_ID=?";
				int count = jdbcTemplate.queryForObject(sql, new Object[]{TZ_DC_WJ_ID,(String) strListenersId.get(i)},"Integer");
				if(count > 0){
					
				}else{
					PsTzSureyAudT.setTzAudId((String) strListenersId.get(i));
					PsTzSureyAudT.setTzDcWjId(TZ_DC_WJ_ID);
                    PsTzSureyAudTMapper.insert(PsTzSureyAudT);

				}
			}
				
		}
		//}
		
		/*去重判断*/ //听众列表 座位ID是什么鬼？
		//String isRepeatedSQL="SELECT 'Y' FROM PS_TZ_DC_WJ_DY_T WHERE TZ_JG_ID=? AND TZ_DC_WJBT=? AND TZ_DC_WJ_ID=? AND SETID=?";     
		//List result=jdbcTemplate.queryForList(isRepeatedSQL, new Object[]{jgId,TZ_DC_WJBT,TZ_DC_WJ_ID,tmpSetID}) ;  
	
		//保存数据 选择性      
		psTzDcWjDyTMapper.updateByPrimaryKeySelective(psTzDcWjDyTWithBLOBs);
	   }else if("GRID".equals(typeFlag)){
	       String strSeqNum=String.valueOf(dataMap.get("tzSeqNum"));
		   String wjId = String.valueOf(dataMap.get("wjId"));
		   String tzAppclsID = String.valueOf(dataMap.get("tzAppclsID"));
		   String tzAppclsType = String.valueOf(dataMap.get("tzAppclsType"));
		   Boolean tzIfUse =Boolean.parseBoolean(String.valueOf(dataMap.get("tzIfUse")));
		   if(dataMap.get("wjId")!=null){
			   if(dataMap.get("tzSeqNum")!=null){
				   PsTzDcWjAppclsT PsTzDcWjAppclsT=new PsTzDcWjAppclsT();
				   PsTzDcWjAppclsT.setTzSeqnum(Integer.valueOf(strSeqNum));
				   if(wjId!=null){
					   PsTzDcWjAppclsT.setTzDcWjId(wjId);
				   }
				   if(dataMap.get("tzAppclsID")!=null){
					   PsTzDcWjAppclsT.setTzAppclsId(tzAppclsID);
				   }
				   if(dataMap.get("tzAppclsType")!=null){
					   PsTzDcWjAppclsT.setTzAppclsType(tzAppclsType);
				   }
				   if(tzIfUse){
					   PsTzDcWjAppclsT.setTzQyStatus("Y");
				   }else{
					   PsTzDcWjAppclsT.setTzQyStatus("N");
				   }
				   
				   psTzDcWjAppclsTMapper.updateByPrimaryKeySelective(PsTzDcWjAppclsT);
			   }else{
				   int tzSeqNum=getSeqNum.getSeqNum("TZ_DC_WJ_APPCLS_T", "TZ_SEQNUM");
				   PsTzDcWjAppclsT PsTzDcWjAppclsT=new PsTzDcWjAppclsT();
				   PsTzDcWjAppclsT.setTzSeqnum(tzSeqNum);
				   if(wjId!=null){
					   PsTzDcWjAppclsT.setTzDcWjId(wjId);
				   }
				   if(dataMap.get("tzAppclsID")!=null){
					   PsTzDcWjAppclsT.setTzAppclsId(tzAppclsID);
				   }
				   if(dataMap.get("tzAppclsType")!=null){
					   PsTzDcWjAppclsT.setTzAppclsType(tzAppclsType);
				   }
				   if(tzIfUse){
					   PsTzDcWjAppclsT.setTzQyStatus("Y");
				   }else{
					   PsTzDcWjAppclsT.setTzQyStatus("N");
				   }
				   psTzDcWjAppclsTMapper.insert(PsTzDcWjAppclsT);
			   }
		  }
	  }
		return new JacksonUtil().Map2json(dataMap);
	}

	//点击重新 加载模版:当模板ID不为空的时候则复制模板表中信息到问卷表
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		
		JacksonUtil jacksonUtil=new JacksonUtil();
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs=new PsTzDcWjDyTWithBLOBs();
		String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if(oprType.equals("reloadTpl")){
			//将String数据转换成Map
			jacksonUtil.json2Map(strParams);
			Map<String,Object>paramMap=jacksonUtil.getMap();
			if(paramMap.get("tplId")!=null&&!paramMap.get("tplId").equals("")){
				//模版ID 问卷ID:
				String tplId=paramMap.get("tplId").toString();
				String wjId=paramMap.get("wjId").toString();
				//读取模版数据:
			    final String survyInfoFromTplSQL="SELECT * FROM PS_TZ_DC_DY_T WHERE TZ_APP_TPL_ID=? and TZ_JG_ID=?";
			    Map<String,Object>tplSurvyInfoMap=new HashMap<String,Object>();
			    tplSurvyInfoMap=jdbcTemplate.queryForMap(survyInfoFromTplSQL, new Object[]{tplId,jgId});
			    
			    psTzDcWjDyTWithBLOBs.setTzDcWjId(wjId);
			    psTzDcWjDyTWithBLOBs.setTzAppTplId(tplId);
			    
			    
				if(tplSurvyInfoMap!=null){
					//模版表中存在的数据 复制到当前问卷:复制卷头 卷尾内容
					if(tplSurvyInfoMap.containsKey("TZ_DC_JTNR")&&tplSurvyInfoMap.get("TZ_DC_JTNR")!=null){
						psTzDcWjDyTWithBLOBs.setTzDcJtnr(tplSurvyInfoMap.get("TZ_DC_JTNR").toString());
					}
					if(tplSurvyInfoMap.containsKey("TZ_DC_JWNR")&&tplSurvyInfoMap.get("TZ_DC_JWNR")!=null){
						psTzDcWjDyTWithBLOBs.setTzDcJwnr(tplSurvyInfoMap.get("TZ_DC_JWNR").toString());
					}
				}
				//选择性更新:只讲模版中的“卷头”和"卷尾"更新到当前问卷
				psTzDcWjDyTMapper.updateByPrimaryKeySelective(psTzDcWjDyTWithBLOBs);
				errorMsg[0]="0";
				return strParams;
		}else{
			errorMsg[0]="1";
			errorMsg[1]="请选择问卷模版";
		}
		}
		return null;
	}
	
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				int tzSeqNum=Integer.valueOf(jacksonUtil.getString("tzSeqNum"));
				
				if (tzSeqNum>0 ) {
					 PsTzDcWjAppclsT PsTzDcWjAppclsT=new PsTzDcWjAppclsT();
					 PsTzDcWjAppclsT.setTzSeqnum(tzSeqNum);
					 psTzDcWjAppclsTMapper.deleteByPrimaryKey(tzSeqNum);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return "{\"delete\":\"true\"}";
	}
	
}
