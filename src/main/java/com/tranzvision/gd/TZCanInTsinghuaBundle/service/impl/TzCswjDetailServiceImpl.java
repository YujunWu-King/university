package com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl;

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

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.dao.PsTzCswjDcxTblMapper;
import com.tranzvision.gd.TZCanInTsinghuaBundle.dao.PsTzCswjPctTblMapper;
import com.tranzvision.gd.TZCanInTsinghuaBundle.dao.PsTzCswjTblMapper;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjDcxTbl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjPctTbl;
import com.tranzvision.gd.TZCanInTsinghuaBundle.model.PsTzCswjTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZCanInTsinghuaBundle.service.impl.TzCswjDetailServiceImpl")
public class TzCswjDetailServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private PsTzCswjDcxTblMapper PsTzCswjDcxTblMapper;
	@Autowired
	private PsTzCswjTblMapper PsTzCswjTblMapper;
	@Autowired
	private PsTzCswjPctTblMapper PsTzCswjPctTblMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	
	/*调查问卷信息项配置列表查询
	 * 
	 * 需要分两种情况，情况一：开通调查问卷的时候，自动加载问卷中的信息项(下拉框,单选题多选题);
	 * */
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "");
		
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);

		String strCswjId = jacksonUtil.getString("TZ_CS_WJ_ID");
	    String flag=jacksonUtil.getString("FLAG");
		
			ArrayList<Map<String, Object>> listJson = new ArrayList<Map<String, Object>>();
			try{
			  //情况二：从测试问卷中获取信息项详情
			  if("A".equals(flag)){
				int total = 0;
				// 查询总数;
				String  strWjId=sqlQuery.queryForObject("select TZ_DC_WJ_ID from PS_TZ_CSWJ_TBL where TZ_CS_WJ_ID=?", new Object[] { strCswjId },"String");
				String totalSQL = "SELECT COUNT(1) FROM PS_TZ_CSWJ_DCX_TBL WHERE TZ_CS_WJ_ID=? AND TZ_DC_WJ_ID=?";
				total = sqlQuery.queryForObject(totalSQL, new Object[] { strCswjId,strWjId },"Integer");
				String sql = "select TZ_XXX_BH,TZ_XXX_MC,TZ_XXX_DESC,TZ_ORDER from PS_TZ_CSWJ_DCX_TBL WHERE TZ_CS_WJ_ID=? AND TZ_DC_WJ_ID=? order by TZ_ORDER LIMIT ?,?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strCswjId,strWjId,numStart,numLimit });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strXXXId = "";
					String strXXXMc = "";
					String strXXXDesc="";
					Integer numOrder;

					strXXXId = String.valueOf(mapData.get("TZ_XXX_BH"));
					strXXXMc = String.valueOf(mapData.get("TZ_XXX_MC"));
					strXXXDesc = String.valueOf(mapData.get("TZ_XXX_DESC"));
					numOrder = Integer.parseInt(String.valueOf(mapData.get("TZ_ORDER")));
					//信息项题型
					String strComMc=sqlQuery.queryForObject("select TZ_COM_LMC from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? AND TZ_XXX_BH=?", new Object[]{strWjId,strXXXId}, "String");
					
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("TZ_CS_WJ_ID", strCswjId);
					mapJson.put("TZ_DC_WJ_ID", strWjId);
					mapJson.put("TZ_ORDER", numOrder);
					mapJson.put("TZ_XXX_BH", strXXXId);
					mapJson.put("TZ_XXX_MC", strXXXMc);
					mapJson.put("TZ_XXX_DESC", strXXXDesc);
					mapJson.put("TZ_COM_LMC", strComMc);
					listJson.add(mapJson);
					mapRet.replace("total",total);
				}
				mapRet.replace("root", listJson);
				}
				
			//情况一：从调查问卷中加载信息项(只加载下拉框，多选题等)
			/*if("B".equals(flag)){
				int total = 0;
				String strWjId="";
				// 查询总数;
				String strWjIdSql="select TZ_DC_WJ_ID from PS_TZ_CSWJ_TBL where TZ_CS_WJ_ID=?";
				strWjId=sqlQuery.queryForObject(strWjIdSql, new Object[] { strCswjId },"String");
				String totalSQL = "select count(*) from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and  TZ_COM_LMC in('RadioBox','ComboBox')";
				total = sqlQuery.queryForObject(totalSQL, new Object[] { strCswjId },"Integer");
				String sql = "select TZ_XXX_BH,TZ_XXX_MC,TZ_TITLE,TZ_ORDER from PS_TZ_DCWJ_XXXPZ_T where TZ_DC_WJ_ID=? and  TZ_COM_LMC in('RadioBox','ComboBox')  order by TZ_ORDER LIMIT ?,?";
				List<?> listData = sqlQuery.queryForList(sql, new Object[] {strWjId,numStart,numLimit });
				for (Object objData : listData) {

					Map<String, Object> mapData = (Map<String, Object>) objData;
					String strXXXId = "";
					String strXXXMc = "";
					String strXXXDesc="";
					Integer numOrder;

					strXXXId = String.valueOf(mapData.get("TZ_XXX_BH"));
					strXXXMc = String.valueOf(mapData.get("TZ_XXX_MC"));
					strXXXDesc = String.valueOf(mapData.get("TZ_TITLE"));
					numOrder = Integer.parseInt(String.valueOf(mapData.get("TZ_ORDER")));
					
					Map<String, Object> mapJson = new HashMap<String, Object>();
					mapJson.put("TZ_CS_WJ_ID", strCswjId);
					mapJson.put("TZ_DC_WJ_ID", strWjId);
					mapJson.put("TZ_ORDER", numOrder);
					mapJson.put("TZ_XXX_BH", strXXXId);
					mapJson.put("TZ_XXX_MC", strXXXMc);
					mapJson.put("TZ_XXX_DESC", strXXXDesc);
					listJson.add(mapJson);
					mapRet.replace("total",total);
				}
				mapRet.replace("root", listJson);
			}*/
				
			} catch (Exception e) {
				e.printStackTrace();
				errorMsg[0] = "1";
				errorMsg[1] = e.toString();
			}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	/**
	 * 测试问卷新增
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		String TZ_CS_WJ_ID = null;
		Map<String, Object> returnJson = new HashMap<String, Object>();
		returnJson.put("formData", "{}");
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss"); 
			    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			    
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("ORG".equals(typeFlag)) {

					 TZ_CS_WJ_ID = String.valueOf(mapData.get("TZ_CS_WJ_ID"));
					String TZ_CS_WJ_NAME = String.valueOf(mapData.get("TZ_CS_WJ_NAME"));
					String TZ_CLASS_ID =mapData.get("TZ_CLASS_ID")==null?"": String.valueOf(mapData.get("TZ_CLASS_ID"));
					String TZ_DC_WJ_ZT = mapData.get("TZ_DC_WJ_ZT")==null?"":String.valueOf(mapData.get("TZ_DC_WJ_ZT"));
					Date TZ_DC_WJ_KSRQ = dateFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_KSRQ")));
					Date TZ_DC_WJ_JSRQ = dateFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_JSRQ")));
					Date TZ_DC_WJ_KSSJ = timeFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_KSSJ")));
					Date TZ_DC_WJ_JSSJ = timeFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_JSSJ")));
					String TZ_STATE = mapData.get("TZ_STATE")==null?"":String.valueOf(mapData.get("TZ_STATE"));
					String TZ_APP_TPL_ID = mapData.get("TZ_APP_TPL_ID")==null?"":String.valueOf(mapData.get("TZ_APP_TPL_ID"));
					String TZ_DC_WJ_ID = mapData.get("TZ_DC_WJ_ID")==null?"":String.valueOf(mapData.get("TZ_DC_WJ_ID"));
					String strPreset=String.valueOf(mapData.get("TZ_PRESET_NUM"));
					int TZ_PRESET_NUM=0;
					if (!"".equals(strPreset)){
						TZ_PRESET_NUM=Integer.valueOf(strPreset);
					}
				
					int isExistAppProcessTmpNum = sqlQuery.queryForObject("select count(*) from PS_TZ_CSWJ_TBL where TZ_CS_WJ_NAME=? and TZ_JG_ID=?", new Object[] {TZ_CS_WJ_NAME,orgid}, "Integer");
					
					if (isExistAppProcessTmpNum == 0) {
						
						if("".equals(TZ_CS_WJ_ID) ||  "NEXT".equals(TZ_CS_WJ_ID.toUpperCase())){
							TZ_CS_WJ_ID = String.valueOf(getSeqNum.getSeqNum("TZ_CSWJ_TBL", "TZ_CS_WJ_ID"));
						}
						
						PsTzCswjTbl PsTzCswjTbl = new PsTzCswjTbl();
						PsTzCswjTbl.setTzCsWjId(TZ_CS_WJ_ID);
						PsTzCswjTbl.setTzCsWjName(TZ_CS_WJ_NAME);
						PsTzCswjTbl.setTzClassId(TZ_CLASS_ID);
						PsTzCswjTbl.setTzDcWjZt(TZ_DC_WJ_ZT);
						PsTzCswjTbl.setTzDcWjKsrq(TZ_DC_WJ_KSRQ);
						PsTzCswjTbl.setTzDcWjKssj(TZ_DC_WJ_KSSJ);
						PsTzCswjTbl.setTzDcWjJsrq(TZ_DC_WJ_JSRQ);
						PsTzCswjTbl.setTzDcWjJssj(TZ_DC_WJ_JSSJ);
						PsTzCswjTbl.setTzPresetNum(TZ_PRESET_NUM);
						PsTzCswjTbl.setTzState(TZ_STATE);
						PsTzCswjTbl.setTzAppTplId(TZ_APP_TPL_ID);
						PsTzCswjTbl.setTzDcWjId(TZ_DC_WJ_ID);
						PsTzCswjTbl.setTzJgId(orgid);
						PsTzCswjTbl.setRowAddedDttm(new Date());
						PsTzCswjTbl.setRowAddedOprid(oprid);
						PsTzCswjTbl.setRowLastmantDttm(new Date());
						PsTzCswjTbl.setRowLastmantOprid(oprid);
						
						PsTzCswjTblMapper.insert(PsTzCswjTbl);

					}else{
						errMsg[0] = "1";
						errMsg[1] = "测试问卷名称不能重复！";
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		//strRet = jacksonUtil.Map2json(returnJson);
		return "{\"id\":\"" + TZ_CS_WJ_ID + "\"}";
	}
	
	/**
	 * 保存测试问卷详情
	 * 
	 * @param actData
	 * @param errMsg
	 * @return String
	 */
	@Override
	@Transactional
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "";
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss"); 
	    DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		JacksonUtil jacksonUtil = new JacksonUtil();
		String TZ_CS_WJ_ID="";
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);
				String typeFlag = jacksonUtil.getString("typeFlag");
				Map<String, Object> mapData = jacksonUtil.getMap("data");

				if ("ORG".equals(typeFlag)) {

					    TZ_CS_WJ_ID = String.valueOf(mapData.get("TZ_CS_WJ_ID"));
						String TZ_CS_WJ_NAME = String.valueOf(mapData.get("TZ_CS_WJ_NAME"));
						String TZ_CLASS_ID = mapData.get("TZ_CLASS_ID")==null?"":String.valueOf(mapData.get("TZ_CLASS_ID"));
						String TZ_DC_WJ_ZT = mapData.get("TZ_DC_WJ_ZT")==null?"":String.valueOf(mapData.get("TZ_DC_WJ_ZT"));
						Date TZ_DC_WJ_KSRQ = dateFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_KSRQ")));
						Date TZ_DC_WJ_JSRQ = dateFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_JSRQ")));
						Date TZ_DC_WJ_KSSJ = timeFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_KSSJ")));
						Date TZ_DC_WJ_JSSJ = timeFormat.parse(String.valueOf(mapData.get("TZ_DC_WJ_JSSJ")));
						String TZ_STATE = mapData.get("TZ_STATE")==null?"":String.valueOf(mapData.get("TZ_STATE"));
						String TZ_APP_TPL_ID = mapData.get("TZ_APP_TPL_ID")==null?"":String.valueOf(mapData.get("TZ_APP_TPL_ID"));
						String TZ_DC_WJ_ID = mapData.get("TZ_DC_WJ_ID")==null?"":String.valueOf(mapData.get("TZ_DC_WJ_ID"));
						String strPreset = String.valueOf(mapData.get("TZ_PRESET_NUM"));
						int TZ_PRESET_NUM=0;
						if (!"".equals(strPreset)){
							TZ_PRESET_NUM=Integer.valueOf(strPreset);
						}
						TZ_CS_WJ_NAME = TZ_CS_WJ_NAME.trim();
					/*模版名称是否已经存在*/
					String sql = "select count(*) from PS_TZ_CSWJ_TBL where TZ_CS_WJ_NAME=? and TZ_CS_WJ_ID<>?";
					int isExistAppProcessTmpNum = sqlQuery.queryForObject(sql, new Object[] { TZ_CS_WJ_NAME,TZ_CS_WJ_ID }, "Integer");
					
					if (isExistAppProcessTmpNum == 0) {
					
						String sqlGetAppProcessTmp = "select COUNT(1) from PS_TZ_CSWJ_TBL WHERE TZ_CS_WJ_ID=?";
						int count = sqlQuery.queryForObject(sqlGetAppProcessTmp, new Object[] { TZ_CS_WJ_ID }, "Integer");
						if (count > 0) {
							PsTzCswjTbl PsTzCswjTbl = new PsTzCswjTbl();
							PsTzCswjTbl.setTzCsWjId(TZ_CS_WJ_ID);
							PsTzCswjTbl.setTzCsWjName(TZ_CS_WJ_NAME);
							PsTzCswjTbl.setTzClassId(TZ_CLASS_ID);
							PsTzCswjTbl.setTzDcWjZt(TZ_DC_WJ_ZT);
							PsTzCswjTbl.setTzDcWjKsrq(TZ_DC_WJ_KSRQ);
							PsTzCswjTbl.setTzDcWjKssj(TZ_DC_WJ_KSSJ);
							PsTzCswjTbl.setTzDcWjJsrq(TZ_DC_WJ_JSRQ);
							PsTzCswjTbl.setTzDcWjJssj(TZ_DC_WJ_JSSJ);
							PsTzCswjTbl.setTzPresetNum(TZ_PRESET_NUM);
							PsTzCswjTbl.setTzState(TZ_STATE);
							PsTzCswjTbl.setTzAppTplId(TZ_APP_TPL_ID);
							PsTzCswjTbl.setTzDcWjId(TZ_DC_WJ_ID);
							PsTzCswjTbl.setTzJgId(orgid);
							PsTzCswjTbl.setRowAddedDttm(new Date());
							PsTzCswjTbl.setRowAddedOprid(oprid);
							PsTzCswjTbl.setRowLastmantDttm(new Date());
							PsTzCswjTbl.setRowLastmantOprid(oprid);
							
							PsTzCswjTblMapper.updateByPrimaryKeySelective(PsTzCswjTbl);
							
						}else{
							errMsg[0] = "1";
							errMsg[1] = "测试问卷"+ TZ_CS_WJ_NAME +"不存在！";
						}	
					}else{
						errMsg[0] = "1";
						errMsg[1] = "测试问卷名称不能重复！";
					}
				}else if("MEM".equals(typeFlag)){
					/*测试问卷编号*/
					 TZ_CS_WJ_ID = String.valueOf(mapData.get("TZ_CS_WJ_ID"));
					/*调查问卷编号*/
					String TZ_DC_WJ_ID =  String.valueOf(mapData.get("TZ_DC_WJ_ID")); 
					/*信息项编号*/
					String TZ_XXX_BH = String.valueOf(mapData.get("TZ_XXX_BH"));
					/*信息项名称*/
					String TZ_XXX_MC = String.valueOf(mapData.get("TZ_XXX_MC"));
					/*信息项描述*/
					String TZ_XXX_DESC = String.valueOf(mapData.get("TZ_XXX_DESC"));
					/*排序序号*/
					String TZ_ORDER = String.valueOf(mapData.get("TZ_ORDER")); 
					
					TZ_XXX_DESC = TZ_XXX_DESC.trim();
						
					String sqlGetAppProcess = "select COUNT(1) from PS_TZ_CSWJ_DCX_TBL WHERE TZ_CS_WJ_ID=? AND TZ_DC_WJ_ID=? and TZ_XXX_BH=?";
					int count = sqlQuery.queryForObject(sqlGetAppProcess, new Object[] { TZ_CS_WJ_ID,TZ_DC_WJ_ID,TZ_XXX_BH }, "Integer");
					if(count>0){
						PsTzCswjDcxTbl PsTzCswjDcxTbl = new PsTzCswjDcxTbl();
						PsTzCswjDcxTbl.setTzCsWjId(TZ_CS_WJ_ID);
						PsTzCswjDcxTbl.setTzDcWjId(TZ_DC_WJ_ID);
						PsTzCswjDcxTbl.setTzXxxBh(TZ_XXX_BH);
						PsTzCswjDcxTbl.setTzXxxMc(TZ_XXX_MC);
						PsTzCswjDcxTbl.setTzXxxDesc(TZ_XXX_DESC);
						PsTzCswjDcxTbl.setTzOrder(Integer.parseInt(TZ_ORDER));
						PsTzCswjDcxTblMapper.updateByPrimaryKeySelective(PsTzCswjDcxTbl);
					}else{
						PsTzCswjDcxTbl PsTzCswjDcxTbl = new PsTzCswjDcxTbl();
						PsTzCswjDcxTbl.setTzCsWjId(TZ_CS_WJ_ID);
						PsTzCswjDcxTbl.setTzDcWjId(TZ_DC_WJ_ID);
						PsTzCswjDcxTbl.setTzXxxBh(TZ_XXX_BH);
						PsTzCswjDcxTbl.setTzXxxMc(TZ_XXX_MC);
						PsTzCswjDcxTbl.setTzXxxDesc(TZ_XXX_DESC);
						PsTzCswjDcxTbl.setTzOrder(Integer.parseInt(TZ_ORDER));
						PsTzCswjDcxTblMapper.insertSelective(PsTzCswjDcxTbl);
						//信息项重新排序
						final String cswjDcxSql = "select TZ_XXX_BH from PS_TZ_DCWJ_XXX_VW where TZ_DC_WJ_ID=? ORDER BY TZ_ORDER";
						List<Map<String, Object>> cswjDcxDataList = new ArrayList<Map<String, Object>>();
						cswjDcxDataList = sqlQuery.queryForList(cswjDcxSql, new Object[] { TZ_DC_WJ_ID });
						if (cswjDcxDataList != null) {
							int j = 0;
							for (int k = 0; k < cswjDcxDataList.size(); k++) {
								Map<String, Object> cswjDcxMap = new HashMap<String, Object>();
								cswjDcxMap = cswjDcxDataList.get(k);
								String strXxxBh = cswjDcxMap.get("TZ_XXX_BH") == null ? null: cswjDcxMap.get("TZ_XXX_BH").toString();
								String exist=sqlQuery.queryForObject("select 'Y' from PS_TZ_CSWJ_DCX_TBL where TZ_CS_WJ_ID=? and TZ_DC_WJ_ID=? and TZ_XXX_BH=?", new Object[]{TZ_CS_WJ_ID,TZ_DC_WJ_ID,strXxxBh}, "String");
							  if("Y".equals(exist)){
							   j = j + 1;
							   PsTzCswjDcxTbl PsTzCswjDcx2Tbl = new PsTzCswjDcxTbl();
							   PsTzCswjDcx2Tbl.setTzCsWjId(TZ_CS_WJ_ID);
							   PsTzCswjDcx2Tbl.setTzDcWjId(TZ_DC_WJ_ID);
							   PsTzCswjDcx2Tbl.setTzXxxBh(TZ_XXX_BH);
							   PsTzCswjDcx2Tbl.setTzXxxMc(TZ_XXX_MC);
							   PsTzCswjDcx2Tbl.setTzXxxDesc(TZ_XXX_DESC);
							   PsTzCswjDcx2Tbl.setTzOrder(j);
								PsTzCswjDcxTblMapper.updateByPrimaryKeySelective(PsTzCswjDcx2Tbl);
							   }
							}
						}

						//插入信息项可选值
						final String cswjPctSql = "select a.TZ_XXXKXZ_MC from PS_TZ_DCWJ_XXKXZ_T a where  a.TZ_DC_WJ_ID=? and a.TZ_XXX_BH=? ORDER BY a.TZ_ORDER";
						List<Map<String, Object>> cswjPctDataList = new ArrayList<Map<String, Object>>();
						cswjPctDataList = sqlQuery.queryForList(cswjPctSql, new Object[] { TZ_DC_WJ_ID ,TZ_XXX_BH});
						if (cswjPctDataList != null) {
							int j = 0;
							for (int k = 0; k < cswjPctDataList.size(); k++) {
								j = j + 1;
								Map<String, Object> mbLJXSMap = new HashMap<String, Object>();
								mbLJXSMap = cswjPctDataList.get(k);
								String TZ_XXXKXZ_MC = mbLJXSMap.get("TZ_XXXKXZ_MC") == null ? null: mbLJXSMap.get("TZ_XXXKXZ_MC").toString();
								PsTzCswjPctTbl PsTzCswjPctTbl = new PsTzCswjPctTbl();
								PsTzCswjPctTbl.setTzCsWjId(TZ_CS_WJ_ID);
								PsTzCswjPctTbl.setTzDcWjId(TZ_DC_WJ_ID);
								PsTzCswjPctTbl.setTzXxxBh(TZ_XXX_BH);
								PsTzCswjPctTbl.setTzXxxkxzMc(TZ_XXXKXZ_MC);
								PsTzCswjPctTbl.setTzOrder(j);
								PsTzCswjPctTblMapper.insert(PsTzCswjPctTbl);
							}
						}
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		//strRet = jacksonUtil.Map2json(returnJson);
		//保存成功后，返回測試問卷的編號
		strRet=(TZ_CS_WJ_ID==null?"":TZ_CS_WJ_ID.toString());
		return "{\"id\":\"" +strRet+"\"}";
	}
	
	
	@Override
	/*测试问卷编辑前的查询*/
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "{}");
		JacksonUtil jacksonUtil = new JacksonUtil();
	    DateFormat timeFormat=new SimpleDateFormat("HH:mm:ss"); 
		DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		    
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("TZ_CS_WJ_ID")) {
				// 流程模版编号;
				String tzCsWjId = jacksonUtil.getString("TZ_CS_WJ_ID");
				PsTzCswjTbl PStzCsWjTbl = PsTzCswjTblMapper.selectByPrimaryKey(tzCsWjId);
				if (PStzCsWjTbl != null) {
					Map<String, Object> retMap = new HashMap<String, Object>();
					retMap.put("TZ_CS_WJ_ID", PStzCsWjTbl.getTzCsWjId().toString());
					retMap.put("TZ_CS_WJ_NAME", PStzCsWjTbl.getTzCsWjName());
					retMap.put("TZ_STATE", PStzCsWjTbl.getTzState());
					retMap.put("TZ_PRESET_NUM", PStzCsWjTbl.getTzPresetNum());
					retMap.put("TZ_APP_TPL_ID", PStzCsWjTbl.getTzAppTplId());
					retMap.put("TZ_DC_WJ_ID", PStzCsWjTbl.getTzDcWjId().toString());
					retMap.put("TZ_DC_WJ_ZT", PStzCsWjTbl.getTzDcWjZt());
					retMap.put("TZ_DC_WJ_KSRQ", dateFormat.format(PStzCsWjTbl.getTzDcWjKsrq()));
					retMap.put("TZ_DC_WJ_KSSJ", timeFormat.format(PStzCsWjTbl.getTzDcWjKssj()));
					retMap.put("TZ_DC_WJ_JSRQ",dateFormat.format(PStzCsWjTbl.getTzDcWjJsrq()));
					retMap.put("TZ_DC_WJ_JSSJ", timeFormat.format(PStzCsWjTbl.getTzDcWjJssj()));
					retMap.put("TZ_CLASS_ID", PStzCsWjTbl.getTzClassId());
					returnJsonMap.replace("formData",retMap);
				} else {
					errMsg[0] = "1";
					errMsg[1] = "测试问卷数据不存在！";
				}
			} else {
				errMsg[0] = "1";
				errMsg[1] = "测试问卷数据不存在！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	@Override
	@Transactional
	public String tzDelete(String[] actData, String[] errMsg) {
		String strRet = "{}";
		JacksonUtil jacksonUtil = new JacksonUtil();
		String strCswjID="";
		try {

			int dataLength = actData.length;
			for (int num = 0; num < dataLength; num++) {
				// 表单内容
				String strForm = actData[num];
				// 解析json
				jacksonUtil.json2Map(strForm);

				// 流程模版编号;
				 strCswjID = jacksonUtil.getString("TZ_CS_WJ_ID");
				/*流程编号*/
				String strWjID = jacksonUtil.getString("TZ_DC_WJ_ID");
				String strXxBh = jacksonUtil.getString("TZ_XXX_BH");
				
				if (strCswjID != null && !"".equals(strCswjID) && strWjID != null && !"".equals(strWjID)&&strXxBh != null && !"".equals(strXxBh)) {
					PsTzCswjDcxTbl PsTzCswjDcxTbl = new PsTzCswjDcxTbl();
					PsTzCswjDcxTbl.setTzCsWjId(strCswjID);
					PsTzCswjDcxTbl.setTzDcWjId(strWjID);
					PsTzCswjDcxTbl.setTzXxxBh(strXxBh);
					PsTzCswjDcxTblMapper.deleteByPrimaryKey(PsTzCswjDcxTbl);
					/*同时删除流程对应的常用回复短语信息*/
					Object[] args = new Object[] { strCswjID,strWjID, strXxBh};
					sqlQuery.update("DELETE FROM PS_TZ_CSWJ_PCT_TBL WHERE TZ_CS_WJ_ID = ? AND TZ_DC_WJ_ID=? and TZ_XXX_BH=?", args);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return "{\"id\":\"" + strCswjID + "\"}";
	}
	
}

