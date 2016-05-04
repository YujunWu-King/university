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
import com.tranzvision.gd.TZBatchBundle.dao.TzJcFwqdxTMapper;
import com.tranzvision.gd.TZBatchBundle.model.TzJcFwqdxT;
import com.tranzvision.gd.TZBatchBundle.model.TzJcFwqdxTKey;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 
 * @author TANG
 * 进程服务器定义列表
 */
@Service("com.tranzvision.gd.TZBatchBundle.service.impl.BatchServerDefnMngServiceImpl")
public class BatchServerDefnMngServiceImpl extends FrameworkImpl {
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TzJcFwqdxTMapper tzJcFwqdxTMapper;
	
	/* 查询进程服务器定义列表 */
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
			String[][] orderByArr = new String[][] { {"TZ_JCFWQ_MC","ASC"}};

			// json数据要的结果字段;
			String[] resultFldArray = { "TZ_JG_ID","TZ_JCFWQ_MC", "TZ_JCFWQ_MS","TZ_YXZT" };

			// 可配置搜索通用函数;
			Object[] obj = fliterForm.searchFilter(resultFldArray,orderByArr, comParams, numLimit, numStart, errorMsg);
			if (obj != null && obj.length > 0) {
				
				
				ArrayList<String[]> list = (ArrayList<String[]>) obj[1];

				for (int i = 0; i < list.size(); i++) {
					String[] rowList = list.get(i);
					Map<String, Object> mapList = new HashMap<String, Object>();
					
					mapList.put("orgId", rowList[0]);
					mapList.put("batchServerName", rowList[1]);
					mapList.put("batchServerDesc", rowList[2]);
					mapList.put("yxzt", rowList[3]);
					
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
			String batchServerName = jacksonUtil.getString("batchServerName");
			
			if(orgId == null || "".equals(orgId) || batchServerName == null || "".equals(batchServerName)){
				errMsg[0] = "1";
				errMsg[1] = "请选择查询的进程";
				return jacksonUtil.Map2json(returnJsonMap);
			}
			
			TzJcFwqdxTKey tzJcFwqdxTKey = new TzJcFwqdxTKey();
			tzJcFwqdxTKey.setTzJgId(orgId);
			tzJcFwqdxTKey.setTzJcfwqMc(batchServerName);
			
			TzJcFwqdxT tzJcFwqdxT = tzJcFwqdxTMapper.selectByPrimaryKey(tzJcFwqdxTKey);
			if(tzJcFwqdxT == null){
				errMsg[0] = "1";
				errMsg[1] = "查询的进程服务器不存在";
				return jacksonUtil.Map2json(returnJsonMap);
			}else{
				returnMap.put("orgId", tzJcFwqdxT.getTzJgId());
				returnMap.put("batchServerName", tzJcFwqdxT.getTzJcfwqMc());
				returnMap.put("batchServerIP", tzJcFwqdxT.getTzFwqIp());
				returnMap.put("batchServerDesc", tzJcFwqdxT.getTzJcfwqMs());
				returnMap.put("czxtLx", tzJcFwqdxT.getTzCzxtLx());
				returnMap.put("rwxhJg", tzJcFwqdxT.getTzRwxhJg());
				returnMap.put("zdbxRws", tzJcFwqdxT.getTzZdbxRws());
				returnMap.put("beiZhu", tzJcFwqdxT.getTzBeizhu());
				returnMap.put("yxzt", tzJcFwqdxT.getTzYxzt());
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
				String batchServerName = jacksonUtil.getString("batchServerName");
				batchServerName = (batchServerName == null ? "":batchServerName);
				String batchServerIP = jacksonUtil.getString("batchServerIP");
				batchServerIP = (batchServerIP == null ? "":batchServerIP);
				String batchServerDesc = jacksonUtil.getString("batchServerDesc");
				batchServerDesc = (batchServerDesc == null ? "":batchServerDesc);
				String czxtLx = jacksonUtil.getString("czxtLx");
				czxtLx = (czxtLx == null ? "":czxtLx);
				int rwxhJg = jacksonUtil.getInt("rwxhJg");
				
				int zdbxRws = jacksonUtil.getInt("zdbxRws");
				
				String beiZhu = jacksonUtil.getString("beiZhu");
				beiZhu = (beiZhu == null ? "":beiZhu);
				String yxzt = jacksonUtil.getString("yxzt");
				yxzt = (yxzt == null ? "":yxzt);
				if("STOPPED".equals(yxzt) || "STOPPING".equals(yxzt)){
					yxzt = "STOPPED";
				}
				
				if("RUNNING".equals(yxzt) || "STARTING".equals(yxzt)){
					yxzt = "RUNNING";
				}
				
				if(orgId == null || "".equals(orgId)){
					errMsg[0] = "1";
					errMsg[1] = "进程服务器所属机构不能为空";
					return strRet;
				}
				if(batchServerName == null || "".equals(batchServerName)){
					errMsg[0] = "1";
					errMsg[1] = "进程服务器名称不能为空";
					return strRet;
				}
				int count = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM TZ_JC_FWQDX_T WHERE TZ_JG_ID=? AND TZ_JCFWQ_MC=?",new Object[]{orgId,batchServerName},"Integer"); 
				if(count > 0){
					errMsg[0] = "1";
					errMsg[1] = "所属机构下进程服务器名称重复";
					return strRet;
				}
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				TzJcFwqdxT tzJcFwqdxT = new TzJcFwqdxT();
				tzJcFwqdxT.setTzJgId(orgId);
				tzJcFwqdxT.setTzJcfwqMc(batchServerName);
				tzJcFwqdxT.setTzFwqIp(batchServerIP);
				tzJcFwqdxT.setTzJcfwqMs(batchServerDesc);
				tzJcFwqdxT.setTzCzxtLx(czxtLx);
				tzJcFwqdxT.setTzRwxhJg(rwxhJg);
				tzJcFwqdxT.setTzZdbxRws(zdbxRws);
				tzJcFwqdxT.setTzBeizhu(beiZhu);
				tzJcFwqdxT.setTzYxzt(yxzt);
				tzJcFwqdxT.setTzZjxtsj(new Date());
				tzJcFwqdxT.setTzBsSlid("");
				
				tzJcFwqdxT.setTzTjr(oprid);
				tzJcFwqdxT.setTzTjsj(new Date());
				tzJcFwqdxT.setTzZhxgr(oprid);
				tzJcFwqdxT.setTzZhxgsj(new Date());
				int i = tzJcFwqdxTMapper.insert(tzJcFwqdxT);
				if(i == 0){
					errMsg[0] = "1";
					errMsg[1] = "添加进程服务器失败";
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
				String batchServerName = jacksonUtil.getString("batchServerName");
				batchServerName = (batchServerName == null ? "":batchServerName);
				String batchServerIP = jacksonUtil.getString("batchServerIP");
				batchServerIP = (batchServerIP == null ? "":batchServerIP);
				String batchServerDesc = jacksonUtil.getString("batchServerDesc");
				batchServerDesc = (batchServerDesc == null ? "":batchServerDesc);
				String czxtLx = jacksonUtil.getString("czxtLx");
				czxtLx = (czxtLx == null ? "":czxtLx);
				int rwxhJg = jacksonUtil.getInt("rwxhJg");
				
				int zdbxRws = jacksonUtil.getInt("zdbxRws");
				
				String beiZhu = jacksonUtil.getString("beiZhu");
				beiZhu = (beiZhu == null ? "":beiZhu);
				String yxzt = jacksonUtil.getString("yxzt");
				yxzt = (yxzt == null ? "":yxzt);
				if("STOPPED".equals(yxzt) || "STOPPING".equals(yxzt)){
					yxzt = "STOPPED";
				}
				
				if("RUNNING".equals(yxzt) || "STARTING".equals(yxzt)){
					yxzt = "RUNNING";
				}
				
				if(orgId == null || "".equals(orgId)){
					errMsg[0] = "1";
					errMsg[1] = "进程服务器所属机构不能为空";
					return strRet;
				}
				if(batchServerName == null || "".equals(batchServerName)){
					errMsg[0] = "1";
					errMsg[1] = "进程服务器名称不能为空";
					return strRet;
				}
				
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

				TzJcFwqdxTKey tzJcFwqdxTTKey = new TzJcFwqdxTKey();
				tzJcFwqdxTTKey.setTzJgId(orgId);
				tzJcFwqdxTTKey.setTzJcfwqMc(batchServerName);
				
				TzJcFwqdxT tzJcFwqdxT = tzJcFwqdxTMapper.selectByPrimaryKey(tzJcFwqdxTTKey);
				if(tzJcFwqdxT == null){
					errMsg[0] = "1";
					errMsg[1] = "更新的进程服务器不存在";
					return strRet;
				}else{
					tzJcFwqdxT.setTzFwqIp(batchServerIP);
					tzJcFwqdxT.setTzJcfwqMs(batchServerDesc);
					tzJcFwqdxT.setTzCzxtLx(czxtLx);
					tzJcFwqdxT.setTzRwxhJg(rwxhJg);
					tzJcFwqdxT.setTzZdbxRws(zdbxRws);
					tzJcFwqdxT.setTzBeizhu(beiZhu);
					tzJcFwqdxT.setTzYxzt(yxzt);
					
					tzJcFwqdxT.setTzZhxgr(oprid);
					tzJcFwqdxT.setTzZhxgsj(new Date());
					int i = tzJcFwqdxTMapper.updateByPrimaryKeySelective(tzJcFwqdxT);
					if(i == 0){
						errMsg[0] = "1";
						errMsg[1] = "更新进程服务器失败";
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
				// 所属机构，进程服务器名称;
				String orgId = jacksonUtil.getString("orgId");
				String batchServerName = jacksonUtil.getString("batchServerName");
				
				TzJcFwqdxTKey tzJcFwqdxTKey = new TzJcFwqdxTKey();
				tzJcFwqdxTKey.setTzJgId(orgId);
				tzJcFwqdxTKey.setTzJcfwqMc(batchServerName);
				tzJcFwqdxTMapper.deleteByPrimaryKey(tzJcFwqdxTKey);
				
			}
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
			return strRet;
		}

		return strRet;
	}
	
	
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		try{
			JacksonUtil jacksonUtil = new JacksonUtil();
			if("startSelBatchServer".equals(oprType)){
				// 所属机构，进程服务器名称;
				jacksonUtil.json2Map(strParams);
				String orgId = jacksonUtil.getString("orgId");
				String batchServerName = jacksonUtil.getString("batchServerName");
				
				TzJcFwqdxTKey tzJcFwqdxTKey = new TzJcFwqdxTKey();
				tzJcFwqdxTKey.setTzJgId(orgId);
				tzJcFwqdxTKey.setTzJcfwqMc(batchServerName);
				
				TzJcFwqdxT tzJcFwqdxT = tzJcFwqdxTMapper.selectByPrimaryKey(tzJcFwqdxTKey);
				if(tzJcFwqdxT != null){
					tzJcFwqdxT.setTzYxzt("RUNNING");
					tzJcFwqdxTMapper.updateByPrimaryKeySelective(tzJcFwqdxT);
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "启动的进程服务器不存在";
				}
			}
			
			if("stopSelBatchServer".equals(oprType)){
				// 所属机构，进程服务器名称;
				jacksonUtil.json2Map(strParams);
				String orgId = jacksonUtil.getString("orgId");
				String batchServerName = jacksonUtil.getString("batchServerName");
				
				TzJcFwqdxTKey tzJcFwqdxTKey = new TzJcFwqdxTKey();
				tzJcFwqdxTKey.setTzJgId(orgId);
				tzJcFwqdxTKey.setTzJcfwqMc(batchServerName);
				
				TzJcFwqdxT tzJcFwqdxT = tzJcFwqdxTMapper.selectByPrimaryKey(tzJcFwqdxTKey);
				if(tzJcFwqdxT != null){
					tzJcFwqdxT.setTzYxzt("STOPPED");
					tzJcFwqdxTMapper.updateByPrimaryKeySelective(tzJcFwqdxT);
				}else{
					errorMsg[0] = "1";
					errorMsg[1] = "停止的进程服务器不存在";
				}
			}
		}catch(Exception e){
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return "";
	}
}
