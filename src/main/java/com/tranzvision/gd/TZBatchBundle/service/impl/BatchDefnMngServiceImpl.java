package com.tranzvision.gd.TZBatchBundle.service.impl;

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
import com.tranzvision.gd.TZBatchBundle.dao.TzJincDyTMapper;
import com.tranzvision.gd.TZBatchBundle.model.TzJincDyT;
import com.tranzvision.gd.TZBatchBundle.model.TzJincDyTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author TANG
 * 进程定义列表
 */
@Service("com.tranzvision.gd.TZBatchBundle.service.impl.BatchDefnMngServiceImpl")
public class BatchDefnMngServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzJincDyTMapper tzJincDyTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery jdbcTemplate;
	
	/* 查询进程定义列表 */
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
			// 排序字段如果没有不要赋值
			String[][] orderByArr = new String[][] { {"TZ_JC_MC","ASC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID","TZ_JC_MC", "TZ_JC_MS" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("orgId", rowList[0]);
					mapList.put("batchName", rowList[1]);
					mapList.put("batchDecs", rowList[2]);
					
					listData.add(mapList);
				}

				mapRet.replace("total", obj[0]);
				mapRet.replace("root", listData);

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return jacksonUtil.Map2json(mapRet);
	}
	
	/*查询进程定义查询*/
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		Map<String, Object> returnMap = new HashMap<>();
		Map<String, Object> returnJsonMap  = new HashMap<>();
		returnJsonMap.put("formData", returnMap);
		JacksonUtil jacksonUtil = new JacksonUtil();
		try{
			jacksonUtil.json2Map(strParams);
			
			String orgId = jacksonUtil.getString("orgId");
			String batchName = jacksonUtil.getString("batchName");
			
			if(orgId == null || "".equals(orgId) || batchName == null || "".equals(batchName)){
				errMsg[0] = "1";
				errMsg[1] = "请选择查询的进程";
				return jacksonUtil.Map2json(returnJsonMap);
			}
			
			TzJincDyTKey tzJincDyTKey = new TzJincDyTKey();
			tzJincDyTKey.setTzJgId(orgId);
			tzJincDyTKey.setTzJcMc(batchName);
			
			TzJincDyT tzJincDyT = tzJincDyTMapper.selectByPrimaryKey(tzJincDyTKey);
			if(tzJincDyT == null){
				errMsg[0] = "1";
				errMsg[1] = "查询的进程不存在";
				return jacksonUtil.Map2json(returnJsonMap);
			}else{
				returnMap.put("orgId", tzJincDyT.getTzJgId());
				returnMap.put("batchName", tzJincDyT.getTzJcMc());
				returnMap.put("batchDecs", tzJincDyT.getTzJcMs());
				returnMap.put("yxptLx", tzJincDyT.getTzYxptLx());
				returnMap.put("beiZhu", tzJincDyT.getTzBeizhu());
				returnMap.put("javaClass", tzJincDyT.getTzJavaClass());
				returnMap.put("comId", tzJincDyT.getTzZczjId());
				returnJsonMap.replace("formData", returnMap);
			}
			
		}catch(Exception e){
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return jacksonUtil.Map2json(returnJsonMap);
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	
	/* 新增进程定义 */
	@Override
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				String orgId = jacksonUtil.getString("orgId");
				orgId = (orgId == null ? "":orgId);
				String batchName = jacksonUtil.getString("batchName");
				batchName = (batchName == null ? "":batchName);
				String batchDecs = jacksonUtil.getString("batchDecs");
				batchDecs = (batchDecs == null ? "":batchDecs);
				String yxptLx = jacksonUtil.getString("yxptLx");
				yxptLx = (yxptLx == null ? "":yxptLx);
				String beiZhu = jacksonUtil.getString("beiZhu");
				beiZhu = (beiZhu == null ? "":beiZhu);
				String comId = jacksonUtil.getString("comId");
				comId = (comId == null ? "":comId);
				String javaClass = jacksonUtil.getString("javaClass");
				javaClass = (javaClass == null ? "":javaClass);
				if(orgId == null || "".equals(orgId)){
					errMsg[0] = "1";
					errMsg[1] = "进程所属机构不能为空";
					return strRet;
				}
				if(batchName == null || "".equals(batchName)){
					errMsg[0] = "1";
					errMsg[1] = "进程名称不能为空";
					return strRet;
				}
				int count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TZ_JINC_DY_T WHERE TZ_JG_ID=? AND TZ_JC_MC=?",new Object[]{orgId,batchName},"Integer"); 
				if(count > 0){
					errMsg[0] = "1";
					errMsg[1] = "所属机构下进程名称重复";
					return strRet;
				}
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				TzJincDyT tzJincDyT = new TzJincDyT();
				tzJincDyT.setTzJgId(orgId);
				tzJincDyT.setTzJcMc(batchName);
				tzJincDyT.setTzJcMs(batchDecs);
				tzJincDyT.setTzYxptLx(yxptLx);
				tzJincDyT.setTzBeizhu(beiZhu);
				tzJincDyT.setTzJavaClass(javaClass);
				tzJincDyT.setTzZczjId(comId);
				tzJincDyT.setTzTjr(oprid);
				tzJincDyT.setTzTjsj(new Date());
				tzJincDyT.setTzZhxgr(oprid);
				tzJincDyT.setTzZhxgsj(new Date());
				int i = tzJincDyTMapper.insert(tzJincDyT);
				if(i == 0){
					errMsg[0] = "1";
					errMsg[1] = "添加进程失败";
				}

			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	
	/* 修改进程定义 */
	@Override
	public String tzUpdate(String[] actData, String[] errMsg) {
		String strRet = "{}";
		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}

		try {
			JacksonUtil jacksonUtil = new JacksonUtil();
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				String orgId = jacksonUtil.getString("orgId");
				orgId = (orgId == null ? "":orgId);
				String batchName = jacksonUtil.getString("batchName");
				batchName = (batchName == null ? "":batchName);
				String batchDecs = jacksonUtil.getString("batchDecs");
				batchDecs = (batchDecs == null ? "":batchDecs);
				String yxptLx = jacksonUtil.getString("yxptLx");
				yxptLx = (yxptLx == null ? "":yxptLx);
				String beiZhu = jacksonUtil.getString("beiZhu");
				beiZhu = (beiZhu == null ? "":beiZhu);
				String comId = jacksonUtil.getString("comId");
				comId = (comId == null ? "":comId);
				String javaClass = jacksonUtil.getString("javaClass");
				javaClass = (javaClass == null ? "":javaClass);
				if(orgId == null || "".equals(orgId)){
					errMsg[0] = "1";
					errMsg[1] = "进程所属机构不能为空";
					return strRet;
				}
				if(batchName == null || "".equals(batchName)){
					errMsg[0] = "1";
					errMsg[1] = "进程名称不能为空";
					return strRet;
				}
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				TzJincDyTKey tzJincDyTKey = new TzJincDyTKey();
				tzJincDyTKey.setTzJgId(orgId);
				tzJincDyTKey.setTzJcMc(batchName);
				
				TzJincDyT tzJincDyT = tzJincDyTMapper.selectByPrimaryKey(tzJincDyTKey);
				if(tzJincDyT == null){
					errMsg[0] = "1";
					errMsg[1] = "更新的进程不存在";
					return strRet;
				}else{
					tzJincDyT.setTzJcMs(batchDecs);
					tzJincDyT.setTzYxptLx(yxptLx);
					tzJincDyT.setTzBeizhu(beiZhu);
					tzJincDyT.setTzJavaClass(javaClass);
					tzJincDyT.setTzZczjId(comId);
					tzJincDyT.setTzZhxgr(oprid);
					tzJincDyT.setTzZhxgsj(new Date());
					int i = tzJincDyTMapper.updateByPrimaryKeySelective(tzJincDyT);
					if(i == 0){
						errMsg[0] = "1";
						errMsg[1] = "更新进程失败";
					}
				}
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	/* 删除进程信息 */
	@Override
	public String tzDelete(String[] actData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";

		// 若参数为空，直接返回;
		if (actData == null || actData.length == 0) {
			return strRet;
		}
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {

			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 所属机构，进程名称;
				String orgId = jacksonUtil.getString("orgId");
				String batchName = jacksonUtil.getString("batchName");
				
				TzJincDyTKey tzJincDyTKey = new TzJincDyTKey();
				tzJincDyTKey.setTzJgId(orgId);
				tzJincDyTKey.setTzJcMc(batchName);
				tzJincDyTMapper.deleteByPrimaryKey(tzJincDyTKey);
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
}
