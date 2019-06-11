package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 招生线索管理
 * @author LuYan 2017-10-09
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzSupplierClueServiceImpl")
public class TzSupplierClueServiceImpl extends FrameworkImpl {

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;

	
	@Override
	@SuppressWarnings("unchecked")
	public String tzQueryList(String strParams,int numLimit,int numStart,String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		mapRet.put("root", "[]");
        String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] {{ "ROW_ADDED_DTTM", "DESC" }};
			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_LEAD_ID","TZ_JG_ID","TZ_REALNAME", "TZ_LEAD_STATUS","TZ_LEAD_STATUS_DESC", "TZ_COMP_CNAME", 
					"TZ_MOBILE","TZ_POSITION", "TZ_BMR_STATUS_DESC","TZ_BZ","ROW_ADDED_DTTM","TZ_RSFCREATE_WAY_DESC","TZ_ZR_OPRID",
					"TZ_ZRR_NAME","TZ_COLOUR_SORT_ID","TZ_LABEL_NAME","TZ_EMAIL"};
			// 可配置搜索通用函数;
			//后台增加可配置搜索条件,责任人是"我"的销售线索
			String subStr=strParams.substring(1, strParams.length()-2);
			String condition=subStr+ ",\"ROW_ADDED_OPRID-operator\":\"01\",\"ROW_ADDED_OPRID-value\":\"" + oprid + "\"}";
			strParams="{"+condition+"}";
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, strParams, numLimit, numStart, errorMsg);

			if (obj != null && obj.length > 0) {

				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("clueId", rowList[0]);
					mapList.put("orgID", rowList[1]);
					mapList.put("cusName", rowList[2]);
					mapList.put("clueState", rowList[3]);
					mapList.put("clueStateDesc", rowList[4]);
					mapList.put("comName", rowList[5]);
					mapList.put("cusMobile", rowList[6]);
					mapList.put("cusPos", rowList[7]);
					mapList.put("bmStateDesc", rowList[8]);
					mapList.put("cusBz", rowList[9]);
					mapList.put("createDate", rowList[10]);
					mapList.put("createWayDesc", rowList[11]);
					mapList.put("chargeOprid", rowList[12]);
					mapList.put("zrPer", rowList[13]);
					mapList.put("colorType", rowList[14]);
					mapList.put("gbyy", rowList[15]);
					mapList.put("email", rowList[16]);
							
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jacksonUtil.Map2json(mapRet);
	}
	
	
	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String strReturn= "";
		try{
			if("saveClueInfo".equals(oprType)){
				strReturn = this.tzSaveClueInfo(strParams, errorMsg);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return strReturn;
	}
	
	

	
	private String tzSaveClueInfo(String strParams, String[] errorMsg) {

		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			String oprid=tzLoginServiceImpl.getLoginedManagerOprid(request);
			String jgId=tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			jacksonUtil.json2Map(strParams);
		
			String clueId = jacksonUtil.getString("clueId");
			String cusName = jacksonUtil.getString("cusName");
			String cusMobile = jacksonUtil.getString("cusMobile");
			String companyName = jacksonUtil.getString("companyName");
			String position = jacksonUtil.getString("position");
			String phone = jacksonUtil.getString("phone");
			String cusEmail = jacksonUtil.getString("cusEmail");
			String memo = jacksonUtil.getString("memo");
			
			String age = jacksonUtil.getString("age");
			String sex = jacksonUtil.getString("sex");
			String tjr = jacksonUtil.getString("tjr");
			String fdb = jacksonUtil.getString("fdb");
			String zgxl = jacksonUtil.getString("zgxl");
			String gznx = jacksonUtil.getString("gznx");
			String glnx = jacksonUtil.getString("glnx");
		
			if("".equals(clueId)||clueId==null){
				//根据手机或邮箱查询是否已存在线索	
				String sql = "select count(*) from PS_TZ_XSXS_INFO_T where TZ_JG_ID=? and TZ_LEAD_STATUS<>'G' and ((TZ_MOBILE<>' ' and TZ_MOBILE=?) or (TZ_EMAIL<>' '  and TZ_EMAIL=?))";
				int existsCount = sqlQuery.queryForObject(sql, new Object[]{ jgId,cusMobile,cusEmail }, "int");
				if(existsCount > 0){
					errorMsg[0] = "1";
					errorMsg[1] = "保存失败，手机或邮箱已存在对应线索";
				}else{
					clueId=String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
					
					PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
					PsTzXsxsInfoT.setTzLeadId(clueId);
					PsTzXsxsInfoT.setTzJgId(jgId);
					PsTzXsxsInfoT.setTzLeadStatus("A");
					//线索创建方式
					PsTzXsxsInfoT.setTzRsfcreateWay("G"); /*手工创建*/
					PsTzXsxsInfoT.setTzRealname(cusName);
					PsTzXsxsInfoT.setTzCompCname(companyName);
					PsTzXsxsInfoT.setTzMobile(cusMobile);
					PsTzXsxsInfoT.setTzPhone(phone);
					PsTzXsxsInfoT.setTzEmail(cusEmail);
					PsTzXsxsInfoT.setTzPosition(position);
					PsTzXsxsInfoT.setTzBz(memo);
					
					PsTzXsxsInfoT.setTzAge(age);
					PsTzXsxsInfoT.setTzSex(sex);
					PsTzXsxsInfoT.setTzTjr(tjr);
					PsTzXsxsInfoT.setTzFdb(fdb);
					PsTzXsxsInfoT.setTzZgxl(zgxl);
					PsTzXsxsInfoT.setTzGznx(gznx);
					PsTzXsxsInfoT.setTzGlnx(glnx);

					PsTzXsxsInfoT.setRowAddedOprid(oprid);
					PsTzXsxsInfoT.setRowLastmantOprid(oprid);
					PsTzXsxsInfoT.setRowAddedDttm(new Date());
					PsTzXsxsInfoT.setRowLastmantDttm(new Date());
					psTzXsxsInfoTMapper.insert(PsTzXsxsInfoT);
					map.put("clueId", clueId);
					
					/*查询是否存在姓名相同未关闭的线索*/
					Integer existName = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID<>? AND TZ_LEAD_STATUS<>'G' AND TZ_REALNAME=?", new Object[]{clueId,cusName},"Integer");
					if(existName>0) {
						map.put("existName", "Y");
					} else {
						map.put("existName", "");
					}
				}
			}else{
				PsTzXsxsInfoTWithBLOBs PsTzXsxsInfoT=new PsTzXsxsInfoTWithBLOBs();
				PsTzXsxsInfoT.setTzLeadId(clueId);
				PsTzXsxsInfoT.setTzJgId(jgId);
				
				PsTzXsxsInfoT.setTzRealname(cusName);
				PsTzXsxsInfoT.setTzCompCname(companyName);
				PsTzXsxsInfoT.setTzMobile(cusMobile);
				PsTzXsxsInfoT.setTzPhone(phone);
				PsTzXsxsInfoT.setTzEmail(cusEmail);
				PsTzXsxsInfoT.setTzPosition(position);
				PsTzXsxsInfoT.setTzBz(memo);
				
				PsTzXsxsInfoT.setTzAge(age);
				PsTzXsxsInfoT.setTzSex(sex);
				PsTzXsxsInfoT.setTzTjr(tjr);
				PsTzXsxsInfoT.setTzFdb(fdb);
				PsTzXsxsInfoT.setTzZgxl(zgxl);
				PsTzXsxsInfoT.setTzGznx(gznx);
				PsTzXsxsInfoT.setTzGlnx(glnx);
				
				PsTzXsxsInfoT.setRowLastmantOprid(oprid);
				PsTzXsxsInfoT.setRowLastmantDttm(new Date());
				psTzXsxsInfoTMapper.updateByPrimaryKeySelective(PsTzXsxsInfoT);
				map.put("clueId", clueId);
				
				map.put("existName", "");
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(map);
	}
}
