package com.tranzvision.gd.TZApplicationSurveyBundle.service.impl;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 调查问卷--新建问卷处理类
 */


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjDyTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjGzgxTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjLjgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjLjxsTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.dao.PsTzDcWjYbgzTMapper;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjLjxsTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjYbgzT;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZApplicationSurveyBundle.service.impl.QustionnaireMNGClsImpl")

public class QustionnaireMNGClsImpl extends FrameworkImpl{
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzDcWjDyTMapper psTzDcWjDyTMapper; 

	@Autowired
	private PsTzDcWjLjgzTMapper psTzDcWjLjgzTMapper;
	
	@Autowired
	private PsTzDcWjYbgzTMapper psTzDcWjYbgzTMapper;
	
	@Autowired
	private PsTzDcWjLjxsTMapper psTzDcWjLjxsTMapper;
	
	@Autowired
	private PsTzDcWjGzgxTMapper psTzDcWjGzgxTMapper;
	
	@Autowired
	private QuestionnaireEditorEngineImpl questionnaireEditorEngineImpl;
	//模块1：新建问卷<由于新建，则问卷模板要先进行创建>   如果actData的id为null则直接添加，否则actData的id不为Null则为从模板复制
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		for(int i=0;i<actData.length;i++){
			System.out.println("------新建问卷tzAdd-------actData:"+actData[i]);
		}
		//从actData中取出数据id
		//调查问卷定义表 对应实体类
		PsTzDcWjDyTWithBLOBs psTzDcWjDyTWithBLOBs=new PsTzDcWjDyTWithBLOBs();
		//JS中传递的数据为null直接跳出
		if(actData.length<=0)
			return null;
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(actData[0]);
		
		String dataWjId=jacksonUtil.getString("id");
		String type=jacksonUtil.getString("type");
		System.out.println("====tzAdd===dataWjId:"+dataWjId+"====type:"+type);
		//情况1：问卷ID为空--->新建空白问卷
		String TZ_DC_WJ_ID=dataWjId;
		if(dataWjId==null||dataWjId.equals("")){
		//生成一个新的问卷ID(个人推荐方案)
		//UUID uuid=UUID.randomUUID();
		//psTzDcWjDyTWithBLOBs.setTzDcWjId(uuid.toString());
		
		//实际方案：
		TZ_DC_WJ_ID = "" + getSeqNum.getSeqNum("TZ_DC_WJ_DY_T", "TZ_DC_WJ_ID");
		psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
		//模板ID(新增空白问卷  模板ID为空)：
		//psTzDcWjDyTWithBLOBs.setTzAppTplId(null);
		//机构ID(不知如何获取 暂且固定为ADMIN)
		String TZ_JG_ID=tzLoginServiceImpl.getLoginedManagerOrgid(request);
		psTzDcWjDyTWithBLOBs.setTzJgId(TZ_JG_ID);
		
		//问卷标题
		 String TZ_DC_WJBT=jacksonUtil.getString("name");
		 psTzDcWjDyTWithBLOBs.setTzDcWjbt(TZ_DC_WJBT);
		//语言
		String TZ_APP_TPL_LAN=jacksonUtil.getString("language");
		psTzDcWjDyTWithBLOBs.setTzAppTplLan(TZ_APP_TPL_LAN);
		//问卷状态
		String TZ_DC_WJ_ZT="0";
		psTzDcWjDyTWithBLOBs.setTzDcWjZt(TZ_DC_WJ_ZT);
		//问卷发布
		String TZ_DC_WJ_FB="0";
		psTzDcWjDyTWithBLOBs.setTzDcWjFb(TZ_DC_WJ_FB);
		//
		String TZ_DC_WJ_DTGZ="0";
		psTzDcWjDyTWithBLOBs.setTzDcWjDtgz(TZ_DC_WJ_DTGZ);
		//
		String TZ_DC_WJ_IPGZ="3";
		psTzDcWjDyTWithBLOBs.setTzDcWjIpgz(TZ_DC_WJ_IPGZ);
		//
		String TZ_DC_WJ_JSGZ="1";
		psTzDcWjDyTWithBLOBs.setTzDcWjJsgz(TZ_DC_WJ_JSGZ);
		//添加时间
		try{
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowTimeStr=simpleDateFormat.format(new Date());
		Date nowTime=simpleDateFormat.parse(nowTimeStr);
		psTzDcWjDyTWithBLOBs.setRowAddedDttm(nowTime);
		}
		catch(Exception e)
		{
			System.out.println("====问卷添加==时间转换错误=");
		}
   		psTzDcWjDyTMapper.insert(psTzDcWjDyTWithBLOBs);
		}
		else{
			//情况2：type为copy复制调查问卷
			if(type.equals("copy")){
				//复制问卷除了 ID和名称 状态和发布状态不同外，其他数据保持一致
				psTzDcWjDyTWithBLOBs=psTzDcWjDyTMapper.selectByPrimaryKey(TZ_DC_WJ_ID);
				//复制问卷生成的一个新的ID
				TZ_DC_WJ_ID = "" + getSeqNum.getSeqNum("TZ_DC_WJ_DY_T", "TZ_DC_WJ_ID");
				psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
				System.out.println("===JSONSTR:"+psTzDcWjDyTWithBLOBs.getTzApptplJsonStr());
				//问卷标题
				 String TZ_DC_WJBT=jacksonUtil.getString("name");
				 psTzDcWjDyTWithBLOBs.setTzDcWjbt(TZ_DC_WJBT);
				//复制问卷 问卷开始日期和结束日期,其他相关日期要不要置空？ 若置空则加上psTzDcWjDyTWithBLOBs set相应的属性为null
				 
				//问卷状态
				 psTzDcWjDyTWithBLOBs.setTzDcWjZt("0");
				 
				 //发布状态
				 psTzDcWjDyTWithBLOBs.setTzDcWjFb("0");
				 
				 String TZ_APPTPL_JSON_STR=psTzDcWjDyTWithBLOBs.getTzApptplJsonStr();
				 //复制调查问卷主表信息 update
				 psTzDcWjDyTMapper.insertSelective(psTzDcWjDyTWithBLOBs);
				 
				 Map<String,Object>mapData=new HashMap<String,Object>();
				 JacksonUtil jsonUtil=new JacksonUtil();
				 jsonUtil.json2Map(TZ_APPTPL_JSON_STR);
				 mapData=jsonUtil.getMap();
				 
				 String userID=tzLoginServiceImpl.getLoginedManagerOprid(request);
				 /*保存信息配置项表*/
				 questionnaireEditorEngineImpl.saveSurvy(TZ_DC_WJ_ID, mapData, userID, new String[2]);
		         /*复制模板逻辑-在线调查问卷逻辑规则定义表*/
				 String TZ_DC_LJTJ_ID=null;
		         final String mbLJGZSqlCopy="select TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH from PS_TZ_DC_WJ_LJGZ_T where TZ_DC_WJ_ID=?";
		         List<Map<String,Object>>mbLJGZDataList=new ArrayList<Map<String,Object>>();
		         mbLJGZDataList=jdbcTemplate.queryForList(mbLJGZSqlCopy, new Object[]{dataWjId});
		         if(mbLJGZDataList!=null){
		        	 for(int i=0;i<mbLJGZDataList.size();i++){
		        		 Map<String,Object>mbLJGZMap=new HashMap<String,Object>();
		        		 mbLJGZMap=mbLJGZDataList.get(i);
		        		 TZ_DC_LJTJ_ID=mbLJGZMap.get("TZ_DC_LJTJ_ID")==null?null:mbLJGZMap.get("TZ_DC_LJTJ_ID").toString();
		        		 String logicalId="L" + getSeqNum.getSeqNum("TZ_DC_WJ_LJGZ_T", "TZ_DC_LJTJ_ID");
		        		 String TZ_XXX_BH=mbLJGZMap.get("TZ_XXX_BH")==null?null:mbLJGZMap.get("TZ_XXX_BH").toString();
		        		 String TZ_LJ_LX=mbLJGZMap.get("TZ_LJ_LX")==null?null:mbLJGZMap.get("TZ_LJ_LX").toString();
		        		 String TZ_PAGE_NO=mbLJGZMap.get("TZ_PAGE_NO")==null?null:mbLJGZMap.get("TZ_PAGE_NO").toString();
		        		 String TZ_LJTJ_XH=mbLJGZMap.get("TZ_LJTJ_XH")==null?null:mbLJGZMap.get("TZ_LJTJ_XH").toString();
		        			 
		        		 PsTzDcWjLjgzT psTzDcWjLjgzT=new PsTzDcWjLjgzT();
		        		 psTzDcWjLjgzT.setTzDcLjtjId(logicalId);
		        			
		        		 psTzDcWjLjgzT.setTzDcWjId(TZ_DC_WJ_ID);
		        		 psTzDcWjLjgzT.setTzXxxBh(TZ_XXX_BH);
		        		 psTzDcWjLjgzT.setTzLjLx(TZ_LJ_LX);
		        		 if(TZ_PAGE_NO!=null)
		        			 psTzDcWjLjgzT.setTzPageNo(Integer.valueOf(TZ_PAGE_NO));
		        		 if(TZ_LJTJ_XH!=null)
		        		 psTzDcWjLjgzT.setTzLjtjXh(Integer.valueOf(TZ_LJTJ_XH));
		        			 
		        		 psTzDcWjLjgzTMapper.insertSelective(psTzDcWjLjgzT);
		        		
		        		 /* 复制模板逻辑-在线调查问卷一般题型逻辑规则关系表*/
		        		 final String mbYBGZSqlCopy="select TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_WJ_YBGZ_T where TZ_DC_WJ_ID=? and TZ_DC_LJTJ_ID=?";
		        		 List<Map<String,Object>>mbYBGZDataList=new ArrayList<Map<String,Object>>();
		        		 mbYBGZDataList=jdbcTemplate.queryForList(mbYBGZSqlCopy, new Object[]{dataWjId,TZ_DC_LJTJ_ID});
		        		 
		        		 if(mbYBGZDataList!=null){
		        			 for(int j=0;j<mbYBGZDataList.size();j++){
		        				 Map<String,Object>mbYBGZMap=new HashMap<String,Object>();
		        				 mbYBGZMap=mbYBGZDataList.get(j);
		        				 
		        				 TZ_XXX_BH=mbYBGZMap.get("TZ_XXX_BH")==null?null:mbYBGZMap.get("TZ_XXX_BH").toString();
		        				 String TZ_XXXKXZ_MC=mbYBGZMap.get("TZ_XXXKXZ_MC")==null?null:mbYBGZMap.get("TZ_XXXKXZ_MC").toString();
		        				 String TZ_IS_SELECTED=mbYBGZMap.get("TZ_IS_SELECTED")==null?null:mbYBGZMap.get("TZ_IS_SELECTED").toString();
		        				 
		        				 PsTzDcWjYbgzT psTzDcWjYbgzT=new PsTzDcWjYbgzT();
		        				 psTzDcWjYbgzT.setTzDcLjtjId(TZ_DC_LJTJ_ID);
		        				 psTzDcWjYbgzT.setTzDcWjId(TZ_DC_WJ_ID);
		        				 psTzDcWjYbgzT.setTzXxxBh(TZ_XXX_BH);
		        				 psTzDcWjYbgzT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
		        				 psTzDcWjYbgzT.setTzIsSelected(TZ_IS_SELECTED);
		        				 
		        				 psTzDcWjYbgzTMapper.insert(psTzDcWjYbgzT);
		        			 }
		        		 }
		        		 
		        		 /*复制模板逻辑-在线调查问卷表格题逻辑规则关系表*/
		        		  final String mbGZGXSqlCopy="select TZ_XXX_BH,TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_WJ_GZGX_T where TZ_DC_WJ_ID=? and TZ_DC_LJTJ_ID=?";
		        		  List<Map<String,Object>>mbGZGXSDataList=new ArrayList<Map<String,Object>>();
		        		  mbGZGXSDataList=jdbcTemplate.queryForList(mbGZGXSqlCopy, new Object[]{dataWjId,TZ_DC_LJTJ_ID});
		        		  if(mbGZGXSDataList!=null){
		        			  for(int m=0;m<mbGZGXSDataList.size();m++){
		        				  Map<String,Object>mbGZGXSMap=new HashMap<String,Object>();
		        				  mbGZGXSMap=mbGZGXSDataList.get(m);
		        				  
		        				   TZ_XXX_BH=mbGZGXSMap.get("TZ_XXX_BH")==null?null:mbGZGXSMap.get("TZ_XXX_BH").toString();
		        				   String TZ_XXXZWT_MC=mbGZGXSMap.get("TZ_XXXZWT_MC")==null?null:mbGZGXSMap.get("TZ_XXXZWT_MC").toString();
		        				   String TZ_XXXKXZ_MC=mbGZGXSMap.get("TZ_XXXKXZ_MC")==null?null:mbGZGXSMap.get("TZ_XXXKXZ_MC").toString();
		        				   String TZ_IS_SELECTED=mbGZGXSMap.get("TZ_XXXKXZ_MC")==null?null:mbGZGXSMap.get("TZ_XXXKXZ_MC").toString();
		        				   
		        				   PsTzDcWjGzgxT psTzDcWjGzgxT=new PsTzDcWjGzgxT();
		        				   psTzDcWjGzgxT.setTzDcLjtjId(logicalId);
		        				   psTzDcWjGzgxT.setTzDcWjId(TZ_DC_LJTJ_ID);
		        				   psTzDcWjGzgxT.setTzXxxBh(TZ_XXX_BH);
		        				   psTzDcWjGzgxT.setTzXxxzwtMc(TZ_XXXZWT_MC);
		        				   psTzDcWjGzgxT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
		        				   psTzDcWjGzgxT.setTzIsSelected(TZ_IS_SELECTED);
		        				   
		        				   psTzDcWjGzgxTMapper.insert(psTzDcWjGzgxT);
		        			  }
		        		  }
		        	     /*复制逻辑-在线调查问卷关联显示逻辑关系表*/
		        		 final String mbLJXSSqlCopy="select TZ_XXX_BH from PS_TZ_DC_WJ_LJXS_T where TZ_DC_WJ_ID=? and TZ_DC_LJTJ_ID=?";
		        		 List<Map<String,Object>>mbLJXSDataList=new ArrayList<Map<String,Object>>();
		        		 mbLJXSDataList=jdbcTemplate.queryForList(mbLJXSSqlCopy, new Object[]{dataWjId,TZ_DC_LJTJ_ID});
		        		 if(mbLJXSDataList!=null){
		        			 for(int k=0;k<mbLJXSDataList.size();k++){
		        				Map<String,Object>mbLJXSMap=new HashMap<String,Object>();
		        				mbLJXSMap=mbLJXSDataList.get(k);
		        				TZ_XXX_BH=mbLJXSMap.get("TZ_XXX_BH")==null?null:mbLJXSMap.get("TZ_XXX_BH").toString();
		        				
		        				PsTzDcWjLjxsTKey psTzDcWjLjxsTKey=new PsTzDcWjLjxsTKey();
		        				psTzDcWjLjxsTKey.setTzDcLjtjId(logicalId);
		        				psTzDcWjLjxsTKey.setTzDcWjId(TZ_DC_WJ_ID);
		        				psTzDcWjLjxsTKey.setTzXxxBh(TZ_XXX_BH);
		        				
		        				psTzDcWjLjxsTMapper.insert(psTzDcWjLjxsTKey);
		        			 }
		        		 }
		         } 
		        }
			}
			//情况3：type为add，id不为空 从模板复制问卷
			else if(type.equals("add")){
				//PS_TZ_DC_DY_T 表中的SETID缺失？
				final String SQL="select TZ_APPTPL_JSON_STR,TZ_DC_JWNR,TZ_DC_JTNR,TZ_APP_TPL_LAN from PS_TZ_DC_DY_T where TZ_APP_TPL_ID=?";
				Map<String,Object>tplDataMap=new HashMap<String,Object>();
				//dataWjId是前台传入的id中的数据 这里传入的是模板ID
				tplDataMap=jdbcTemplate.queryForMap(SQL,new Object[]{dataWjId});
				if(tplDataMap!=null){		
					TZ_DC_WJ_ID = "" + getSeqNum.getSeqNum("TZ_DC_WJ_DY_T", "TZ_DC_WJ_ID");
					psTzDcWjDyTWithBLOBs.setTzDcWjId(TZ_DC_WJ_ID);
					//模板ID
					psTzDcWjDyTWithBLOBs.setTzAppTplId(dataWjId);
					//问卷标题
					String TZ_DC_WJBT=jacksonUtil.getString("name");
					psTzDcWjDyTWithBLOBs.setTzDcWjbt(TZ_DC_WJBT);
					//问卷状态
					psTzDcWjDyTWithBLOBs.setTzDcWjZt("0");
					//发布状态
					psTzDcWjDyTWithBLOBs.setTzDcWjFb("0");
					
					//机构ID
					String TZ_JG_ID=tzLoginServiceImpl.getLoginedManagerOrgid(request);
					psTzDcWjDyTWithBLOBs.setTzJgId(TZ_JG_ID);
					
					String TZ_APPTPL_JSON_STR=tplDataMap.get("TZ_APPTPL_JSON_STR")==null?null:tplDataMap.get("TZ_APPTPL_JSON_STR").toString();
					psTzDcWjDyTWithBLOBs.setTzApptplJsonStr(TZ_APPTPL_JSON_STR);
					
					String TZ_DC_JWNR=tplDataMap.get("TZ_DC_JWNR")==null?null:tplDataMap.get("TZ_DC_JWNR").toString();
					psTzDcWjDyTWithBLOBs.setTzDcJwnr(TZ_DC_JWNR);
					
					String TZ_DC_JTNR=tplDataMap.get("TZ_DC_JTNR")==null?null:tplDataMap.get("TZ_DC_JTNR").toString();
					psTzDcWjDyTWithBLOBs.setTzDcJtnr(TZ_DC_JTNR);
					
					String TZ_APP_TPL_LAN=tplDataMap.get("TZ_APP_TPL_LAN")==null?null:tplDataMap.get("TZ_APP_TPL_LAN").toString();
					psTzDcWjDyTWithBLOBs.setTzAppTplLan(TZ_APP_TPL_LAN);
					
					String TZ_DC_WJ_DTGZ="0";
					psTzDcWjDyTWithBLOBs.setTzDcWjDtgz(TZ_DC_WJ_DTGZ);
					//
					String TZ_DC_WJ_IPGZ="3";
					psTzDcWjDyTWithBLOBs.setTzDcWjIpgz(TZ_DC_WJ_IPGZ);
					//
					String TZ_DC_WJ_JSGZ="1";
					psTzDcWjDyTWithBLOBs.setTzDcWjJsgz(TZ_DC_WJ_JSGZ);
					//添加时间
					try{
					SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String nowTimeStr=simpleDateFormat.format(new Date());
					Date nowTime=simpleDateFormat.parse(nowTimeStr);
					psTzDcWjDyTWithBLOBs.setRowAddedDttm(nowTime);
					}
					catch(Exception e)
					{
						System.out.println("====问卷添加==时间转换错误=");
					}
					psTzDcWjDyTMapper.insert(psTzDcWjDyTWithBLOBs);
					
					Map<String,Object>mapData=new HashMap<String,Object>();
					JacksonUtil jsonUtil=new JacksonUtil();
					jsonUtil.json2Map(TZ_APPTPL_JSON_STR);
					mapData=jsonUtil.getMap();
					String userID=tzLoginServiceImpl.getLoginedManagerOprid(request);
					 /*保存信息配置项表*/
					questionnaireEditorEngineImpl.saveSurvy(TZ_DC_WJ_ID, mapData, userID, new String[2]);
					
		            /*复制模板逻辑-在线调查问卷逻辑规则定义表*/
					final String mbLJGZSql="select TZ_DC_LJTJ_ID,TZ_XXX_BH,TZ_LJ_LX,TZ_PAGE_NO,TZ_LJTJ_XH from PS_TZ_DC_MB_LJGZ_T where TZ_APP_TPL_ID=?";
					List<Map<String,Object>>mbLJGZDataList=new ArrayList<Map<String,Object>>();
					mbLJGZDataList=jdbcTemplate.queryForList(mbLJGZSql, new Object[]{dataWjId});
					String TZ_DC_LJTJ_ID=null;
					if(mbLJGZDataList!=null){
						for(int i=0;i<mbLJGZDataList.size();i++){
							Map<String,Object>mbLJGZMap=mbLJGZDataList.get(i);
							
							 TZ_DC_LJTJ_ID=mbLJGZMap.get("TZ_DC_LJTJ_ID")==null?null:mbLJGZMap.get("TZ_DC_LJTJ_ID").toString();
							 String logicalId="L" + getSeqNum.getSeqNum("TZ_DC_WJ_LJGZ_T", "TZ_DC_LJTJ_ID");
			        		 String TZ_XXX_BH=mbLJGZMap.get("TZ_XXX_BH")==null?null:mbLJGZMap.get("TZ_XXX_BH").toString();
			        		 String TZ_LJ_LX=mbLJGZMap.get("TZ_LJ_LX")==null?null:mbLJGZMap.get("TZ_LJ_LX").toString();
			        		 String TZ_PAGE_NO=mbLJGZMap.get("TZ_PAGE_NO")==null?null:mbLJGZMap.get("TZ_PAGE_NO").toString();
			        		 String TZ_LJTJ_XH=mbLJGZMap.get("TZ_LJTJ_XH")==null?null:mbLJGZMap.get("TZ_LJTJ_XH").toString();
			        			 
			        		 PsTzDcWjLjgzT psTzDcWjLjgzT=new PsTzDcWjLjgzT();
			        		 psTzDcWjLjgzT.setTzDcLjtjId(logicalId);
			        			
			        		 psTzDcWjLjgzT.setTzDcWjId(TZ_DC_WJ_ID);
			        		 psTzDcWjLjgzT.setTzXxxBh(TZ_XXX_BH);
			        		 psTzDcWjLjgzT.setTzLjLx(TZ_LJ_LX);
			        		 psTzDcWjLjgzT.setTzPageNo(Integer.valueOf(TZ_PAGE_NO));
			        		 psTzDcWjLjgzT.setTzLjtjXh(Integer.valueOf(TZ_LJTJ_XH));
			        			 
			        		 psTzDcWjLjgzTMapper.insert(psTzDcWjLjgzT);
			        		 //System.out.println("==执行==psTzDcWjLjgzTMapper.insert()");
			        		 /* 复制模板逻辑-在线调查问卷一般题型逻辑规则关系表*/
			        		 final String mbYBGZSql="select TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_YBGZ_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
			        		
			        		 List<Map<String,Object>>mbYBGZDataList=new ArrayList<Map<String,Object>>();
			        		 mbYBGZDataList=jdbcTemplate.queryForList(mbYBGZSql, new Object[]{dataWjId,TZ_DC_LJTJ_ID});
			        		 
			        		 if(mbYBGZDataList!=null){
			        			 for(int j=0;j<mbYBGZDataList.size();j++){
			        				 Map<String,Object>mbYBGZMap=new HashMap<String,Object>();
			        				 mbYBGZMap=mbYBGZDataList.get(j);
			        				 
			        				 TZ_XXX_BH=mbYBGZMap.get("TZ_XXX_BH")==null?null:mbYBGZMap.get("TZ_XXX_BH").toString();
			        				 String TZ_XXXKXZ_MC=mbYBGZMap.get("TZ_XXXKXZ_MC")==null?null:mbYBGZMap.get("TZ_XXXKXZ_MC").toString();
			        				 String TZ_IS_SELECTED=mbYBGZMap.get("TZ_IS_SELECTED")==null?null:mbYBGZMap.get("TZ_IS_SELECTED").toString();
			        				 
			        				 PsTzDcWjYbgzT psTzDcWjYbgzT=new PsTzDcWjYbgzT();
			        				 psTzDcWjYbgzT.setTzDcLjtjId(logicalId);
			        				 psTzDcWjYbgzT.setTzDcWjId(TZ_DC_WJ_ID);
			        				 psTzDcWjYbgzT.setTzXxxBh(TZ_XXX_BH);
			        				 psTzDcWjYbgzT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
			        				 psTzDcWjYbgzT.setTzIsSelected(TZ_IS_SELECTED);
			        				 
			        				 psTzDcWjYbgzTMapper.insert(psTzDcWjYbgzT);
			        				// System.out.println("==执行==psTzDcWjYbgzTMapper.insert()");
			        			 }
			        		 }
			        		 /*复制模板逻辑-在线调查问卷表格题逻辑规则关系表*/
			        		  final String mbGZGXSqlCopy="select TZ_XXX_BH,TZ_XXXZWT_MC,TZ_XXXKXZ_MC,TZ_IS_SELECTED from PS_TZ_DC_MB_GZGX_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
			        		  List<Map<String,Object>>mbGZGXSDataList=new ArrayList<Map<String,Object>>();
			        		  mbGZGXSDataList=jdbcTemplate.queryForList(mbGZGXSqlCopy, new Object[]{dataWjId,TZ_DC_LJTJ_ID});
			        		  if(mbGZGXSDataList!=null){
			        			  for(int m=0;m<mbGZGXSDataList.size();m++){
			        				  Map<String,Object>mbGZGXSMap=new HashMap<String,Object>();
			        				  mbGZGXSMap=mbGZGXSDataList.get(m);
			        				  
			        				   TZ_XXX_BH=mbGZGXSMap.get("TZ_XXX_BH")==null?null:mbGZGXSMap.get("TZ_XXX_BH").toString();
			        				   String TZ_XXXZWT_MC=mbGZGXSMap.get("TZ_XXXZWT_MC")==null?null:mbGZGXSMap.get("TZ_XXXZWT_MC").toString();
			        				   String TZ_XXXKXZ_MC=mbGZGXSMap.get("TZ_XXXKXZ_MC")==null?null:mbGZGXSMap.get("TZ_XXXKXZ_MC").toString();
			        				   String TZ_IS_SELECTED=mbGZGXSMap.get("TZ_IS_SELECTED")==null?null:mbGZGXSMap.get("TZ_IS_SELECTED").toString();
			        				   
			        				   PsTzDcWjGzgxT psTzDcWjGzgxT=new PsTzDcWjGzgxT();
			        				   psTzDcWjGzgxT.setTzDcLjtjId(logicalId);
			        				   psTzDcWjGzgxT.setTzDcWjId(TZ_DC_LJTJ_ID);
			        				   psTzDcWjGzgxT.setTzXxxBh(TZ_XXX_BH);
			        				   psTzDcWjGzgxT.setTzXxxzwtMc(TZ_XXXZWT_MC);
			        				   psTzDcWjGzgxT.setTzXxxkxzMc(TZ_XXXKXZ_MC);
			        				   psTzDcWjGzgxT.setTzIsSelected(TZ_IS_SELECTED);
			        				   
			        				   psTzDcWjGzgxTMapper.insert(psTzDcWjGzgxT);
			        				   
			        			  }
			        		  }
			        		  
			        	     /*复制逻辑-在线调查问卷关联显示逻辑关系表*/
			        		 final String mbGZGXSql="select TZ_XXX_BH from PS_TZ_DC_MB_LJXS_T where TZ_APP_TPL_ID=? and TZ_DC_LJTJ_ID=?";
			        		 List<Map<String,Object>>mbLJXSDataList=new ArrayList<Map<String,Object>>();
			        		 mbLJXSDataList=jdbcTemplate.queryForList(mbGZGXSql, new Object[]{dataWjId,TZ_DC_LJTJ_ID});
			        		 if(mbLJXSDataList!=null){
			        			 for(int k=0;k<mbLJXSDataList.size();k++){
			        				Map<String,Object>mbLJXSMap=new HashMap<String,Object>();
			        				mbLJXSMap=mbLJXSDataList.get(k);
			        				TZ_XXX_BH=mbLJXSMap.get("TZ_XXX_BH")==null?null:mbLJXSMap.get("TZ_XXX_BH").toString();
			        				
			        				PsTzDcWjLjxsTKey psTzDcWjLjxsTKey=new PsTzDcWjLjxsTKey();
			        				psTzDcWjLjxsTKey.setTzDcLjtjId(logicalId);
			        				psTzDcWjLjxsTKey.setTzDcWjId(TZ_DC_WJ_ID);
			        				psTzDcWjLjxsTKey.setTzXxxBh(TZ_XXX_BH);
			        				
			        				psTzDcWjLjxsTMapper.insert(psTzDcWjLjxsTKey);
			        				// System.out.println("==执行==psTzDcWjLjxsTMapper.insert()");

			        			 }
			        		 }
						}
					}
				}
			}
		}
   		return "{\"id\":\"" + TZ_DC_WJ_ID + "\"}";
	}


	@Override
	public String tzQueryList(String strParams, int numLimit, int numStart, String[] errorMsg) {
		System.out.println("====tzQueryList======strParams:"+strParams);
		String strComContent="{}";
		/*每次获取的记录数，开始记录*/ //分页的参数是如何获取？
		if(numLimit==0&&numStart==0){
		String strLimit=request.getParameter("limit");
		String strStart=request.getParameter("start");
		/*每次获取的记录数，开始记录*/
		 numLimit=Integer.valueOf(strLimit);
		 numStart=Integer.valueOf(strStart);
		}
		System.out.println("numLimit:"+numLimit);
		System.out.println("numStart:"+numStart);
		//错误码为0,则执行 
		if(!errorMsg[0].equals("1")){
			//解析json类
			JacksonUtil jsonUtil=new JacksonUtil();
			jsonUtil.json2Map(strParams);
			Map<String,Object>dataMap=jsonUtil.getMap();
			
			System.out.println("===问卷详情queryList==wjId:"+dataMap.get("wjId"));
			String wjId=dataMap.get("wjId").toString();
			final String SQL="SELECT COUNT(*) FROM PS_TZ_DC_INS_T WHERE TZ_DC_WJ_ID=?";
			int total=jdbcTemplate.queryForObject(SQL, new Object[]{wjId},"int");
			
			List<Map<String,Object>>resultList=new ArrayList<Map<String,Object>>();
			if(numLimit==0&&numStart==0){
				final String SQL1="select TZ_APP_INS_ID,TZ_DC_INS_IP,TZ_DC_WC_STA,date_format(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i:%s') KSSJ,date_format(ROW_LASTMANT_DTTM,'%Y-%m-%d %H:%i:%s') JSSJ,(select TZ_MSSQH from PS_TZ_REG_USER_T where OPRID=PERSON_ID) as TZ_MSSQH,(select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=PERSON_ID limit 1) as TZ_REALNAME from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=? order by ROW_ADDED_DTTM desc";
				resultList=jdbcTemplate.queryForList(SQL1);
			}
			else{
				final String SQL2="SELECT TZ_APP_INS_ID,TZ_DC_INS_IP,TZ_DC_WC_STA, date_format(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i:%s') KSSJ,date_format(ROW_LASTMANT_DTTM,'%Y-%m-%d %H:%i:%s') JSSJ,(select TZ_MSSQH from PS_TZ_REG_USER_T where OPRID=A.PERSON_ID) as TZ_MSSQH,(select TZ_REALNAME from PS_TZ_AQ_YHXX_TBL where OPRID=A.PERSON_ID limit 1) as TZ_REALNAME FROM PS_TZ_DC_INS_T A WHERE A.TZ_DC_WJ_ID=? limit ?,?";
				resultList=jdbcTemplate.queryForList(SQL2, new Object[]{wjId,numStart, numLimit});
			}
			
			//用来存储返回数据的Map,最终将returnMap转成json返回
			Map<String,Object>returnMap=new HashMap<String,Object>();
			returnMap.put("total",0);
			returnMap.put("root", "");
			
			if(resultList!=null){
				List<Map<String,Object>>infoList=new ArrayList<Map<String,Object>>();
				for(int i=0;i<resultList.size();i++){
					Map<String,Object>infoMap=new HashMap<String,Object>();
					Map<String,Object>resultMap=new HashMap<String,Object>();
					resultMap=resultList.get(i);
					
					String insId=resultMap.get("TZ_APP_INS_ID")==null?null:resultMap.get("TZ_APP_INS_ID").toString();
					String IPAddress=resultMap.get("TZ_DC_INS_IP")==null?null:resultMap.get("TZ_DC_INS_IP").toString();
					String state=resultMap.get("TZ_DC_WC_STA")==null?null:resultMap.get("TZ_DC_WC_STA").toString();
					String ksSj=resultMap.get("KSSJ")==null?null:resultMap.get("KSSJ").toString();
					String jsSj=resultMap.get("JSSJ")==null?null:resultMap.get("JSSJ").toString();
					//面试申请号、姓名
					String msSqh = resultMap.get("TZ_MSSQH")==null? "" :resultMap.get("TZ_MSSQH").toString();
					String name = resultMap.get("TZ_REALNAME")==null? "" :resultMap.get("TZ_REALNAME").toString();
				
					infoMap.put("order",i+1);
					infoMap.put("wjInsId",insId);
					infoMap.put("wjId", wjId);
					infoMap.put("IPAddress", IPAddress);
					infoMap.put("state", state);
					infoMap.put("ksSj", ksSj);
					infoMap.put("jsSj", jsSj);
					
					infoMap.put("msSqh", msSqh);
					infoMap.put("name", name);
					
					infoList.add(infoMap);
				}
				//将infoList封入返回Map中转json 后return;
				returnMap.replace("total", total);
				returnMap.replace("root", infoList);
				
				strComContent=new JacksonUtil().Map2json(returnMap);
				return strComContent;
			}
		}
		return null;
	}

	@Override
	public String tzQuery(String strParams,String[] errorMsg) {
		//用来存储返回数据的Map,最终将returnMap转成json返回
		Map<String,Object>returnMap=new HashMap<String,Object>();
		System.out.println("=====tzQuery====strParams:"+strParams);
		System.out.println("=====tzQuery======errorMsg[0]:"+errorMsg[0]);
		returnMap.put("formData","");
		returnMap.put("listData", "");
		String strComContent=null;
		if(!errorMsg[0].equals("1")){
			JacksonUtil jsonUtil=new JacksonUtil();
			//rem 将字符串转换成json
			Map<String,Object>paramsMap=new HashMap<String,Object>();
			jsonUtil.json2Map(strParams);
			paramsMap=jsonUtil.getMap();
			
			String wjId=paramsMap.get("wjId")==null?null:paramsMap.get("wjId").toString();
			String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);

			final String SQL="select date_format(TZ_DC_WJ_KSRQ,'%Y-%m-%d %H:%i:%s') TZ_DC_WJ_KSRQ,date_format(TZ_DC_WJ_JSRQ,'%Y-%m-%d %H:%i:%s') TZ_DC_WJ_JSRQ from PS_TZ_DC_WJ_DY_T where TZ_JG_ID=? and TZ_DC_WJ_ID=?";
			Map<String,Object>timeMap=new HashMap<String,Object>();
			timeMap=jdbcTemplate.queryForMap(SQL,new Object[]{jgId,wjId});
			String ksDate=null;
			String jsDate=null;
			if(timeMap!=null){
				ksDate=timeMap.get("TZ_DC_WJ_KSRQ")==null?null:timeMap.get("TZ_DC_WJ_KSRQ").toString();
				jsDate=timeMap.get("TZ_DC_WJ_JSRQ")==null?null:timeMap.get("TZ_DC_WJ_JSRQ").toString();
			}
			/*获得formData中的数据*/
			final String SQL1="select count(*) from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?";
				
				int joinPeople=jdbcTemplate.queryForObject(SQL1, new Object[]{wjId}, "int");
			final String SQL2="select count(*) from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=? and TZ_DC_WC_STA='0'";
				int comPeople=jdbcTemplate.queryForObject(SQL2, new Object[]{wjId},"int");
			final String SQL3="select count(*) from (select distinct(TZ_DC_INS_IP) from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?) x";
				int totalIP=jdbcTemplate.queryForObject(SQL3, new Object[]{wjId}, "int");
			double strPercent=0;
			DecimalFormat decimalFormat=new DecimalFormat("#.00");
			if(joinPeople!=0){
				strPercent=(double)comPeople/(double)joinPeople;
				strPercent=strPercent*100;
			}
			String percentStr=decimalFormat.format(strPercent)+"%";
			
			//将数据封装成 所需格式返回
			Map<String,Object>formDataMap=new HashMap<String,Object>();
			formDataMap.put("beginDate", ksDate);
			formDataMap.put("endDate", jsDate);
			formDataMap.put("joinPeople", joinPeople);
			formDataMap.put("comPeople", comPeople);
			formDataMap.put("totalIP", totalIP);
			formDataMap.put("percent", percentStr);
			
			returnMap.replace("formData", formDataMap);
			
		      /*获得表格中gridData中的数据*/
			final String SQL4="select TZ_APP_INS_ID,TZ_DC_INS_IP,TZ_DC_WC_STA,date_format(ROW_ADDED_DTTM,'%Y-%m-%d %H:%i:%s') KSSJ,date_format(ROW_LASTMANT_DTTM,'%Y-%m-%d %H:%i:%s') JSSJ from PS_TZ_DC_INS_T where TZ_DC_WJ_ID=?";
			List<Map<String,Object>>gridDataList=jdbcTemplate.queryForList(SQL4, new Object[]{wjId});
			List<Map<String,Object>>infoList=new ArrayList<Map<String,Object>>();
			
			if(gridDataList!=null){
				for(int i=0;i<gridDataList.size();i++){
					Map<String,Object>gridDataMap=gridDataList.get(i);
					String insId=gridDataMap.get("TZ_APP_INS_ID")==null?null:gridDataMap.get("TZ_APP_INS_ID").toString();
					String IPAddress=gridDataMap.get("TZ_DC_INS_IP")==null?null:gridDataMap.get("TZ_DC_INS_IP").toString();
					String state=gridDataMap.get("TZ_DC_WC_STA")==null?null:gridDataMap.get("TZ_DC_WC_STA").toString();
					String ksSj=gridDataMap.get("KSSJ")==null?null:gridDataMap.get("KSSJ").toString();
					String jsSj=gridDataMap.get("JSSJ")==null?null:gridDataMap.get("JSSJ").toString();
					
					Map<String,Object>infoMap=new HashMap<String,Object>();
					infoMap.put("order",i+1);
					infoMap.put("wjInsId",insId);
					infoMap.put("wjId", wjId);
					infoMap.put("IPAddress", IPAddress);
					infoMap.put("state", state);
					infoMap.put("ksSj", ksSj);
					infoMap.put("jsSj", jsSj);
					infoList.add(infoMap);
				}
				returnMap.replace("listData", infoList);
			}
			strComContent=new JacksonUtil().Map2json(returnMap);
			System.out.println("===strComContent==="+strComContent);
			return  strComContent;
		}
		return null;
	}

}
