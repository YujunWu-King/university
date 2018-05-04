package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormLabelTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormZlshTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFrmMorinfTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzInteGroupMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzIntervieweeMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormLabelTKey;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshTKey;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFrmMorinfT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFrmMorinfTKey;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzInteGroup;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzInterviewee;
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZLabelSetBundle.dao.PsTzLabelDfnTMapper;
import com.tranzvision.gd.TZLabelSetBundle.model.PsTzLabelDfnT;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzMszgTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTblKey;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzMszgT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhccTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcTKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccTKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkTKey;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.MessageTextServiceImpl;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.encrypt.Sha3DesMD5;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 原PS：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_AUDIT_CLS
 * 
 * @author tang 面试现场分组-报名表审核
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdMsglMSZServiceImpl")
public class TzGdMsglMSZServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private MessageTextServiceImpl messageTextServiceImpl;
	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;
	@Autowired
	private PsTzFormLabelTMapper psTzFormLabelTMapper;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzLabelDfnTMapper psTzLabelDfnTMapper;
	@Autowired
	private PsTzFormZlshTMapper psTzFormZlshTMapper;
	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	@Autowired
	private PsTzFrmMorinfTMapper psTzFrmMorinfTMapper;
	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;
	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;
	@Autowired
	private PsTzAppDhccTMapper psTzAppDhccTMapper;
	@Autowired
	private PsTzIntervieweeMapper pstzIntervieweeMapper;
	@Autowired
	private PsTzInteGroupMapper pstzInteGroupMapper;
	@Autowired
	private FliterForm fliterForm;

	// 获取面试组信息和评委组信息
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		// 排序字段如果没有不要赋值
		String[][] orderByArr = new String[][] { { "TZ_CLPS_GR_ID", "ASC" } };

		// json数据要的结果字段;
		//String[] resultFldArray = { "TZ_JG_ID", "TZ_CLPS_GR_ID", "TZ_CLPS_GR_NAME" };
		//String sql = "select * from PS_TZ_MSPS_GR_TBL";
		//查询所有的评委组
		List<PsTzInterviewee> list =  pstzIntervieweeMapper.findAll();
		
		for (int i = 0; i < list.size(); i++) {
			PsTzInterviewee ps = list.get(i);	
			Map<String, Object> mapList = new HashMap<>();
			mapList.put("jugGroupId", ps.getJugGroupId());
			mapList.put("jugGroupName", ps.getJugGroupName());
			listData.add(mapList);
		}
		mapRet.replace("total", list.size());
		mapRet.replace("root", listData);

		// 可配置搜索通用函数;
		//Object[] obj = fliterForm.searchFilter(resultFldArray, orderByArr, comParams, numLimit, numStart, errorMsg);

		/*if (obj != null && obj.length > 0) {
			ArrayList<String[]> list = (ArrayList<String[]>) obj[1];
			for (int i = 0; i < list.size(); i++) {
				String[] rowList = list.get(i);
				Map<String, Object> mapList = new HashMap<String, Object>();
				mapList.put("jgID", rowList[0]);
				mapList.put("jugGroupId", rowList[1]);
				mapList.put("jugGroupName", rowList[2]);
				listData.add(mapList);
			}
			mapRet.replace("total", obj[0]);
			mapRet.replace("root", listData);
		}*/

		return jacksonUtil.Map2json(mapRet);
	}

	// 获取评委组信息
	public String tzQueryinterviewee(String strParams, String[] errMsg) {
		// 返回值;
		Map<String, Object> returnJsonMap = new HashMap<String, Object>();
		returnJsonMap.put("formData", "");
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			jacksonUtil.json2Map(strParams);

			if (jacksonUtil.containsKey("classID") && jacksonUtil.containsKey("batchID")) {
				// 班级编号;
				String strClassID = jacksonUtil.getString("classID");
				// 批次编号;
				String strBatchID = jacksonUtil.getString("batchID");
				// 班级名称，报名表模板编号，批次名称;
				String strClassName = "", strAppModalID = "", strBatchName = "";

				// 获取班级名称，报名表模板ID，批次名称;
				String sql = "SELECT A.TZ_CLASS_NAME,A.TZ_APP_MODAL_ID,B.TZ_BATCH_NAME FROM PS_TZ_CLASS_INF_T A INNER JOIN PS_TZ_CLS_BATCH_T B ON(A.TZ_CLASS_ID=B.TZ_CLASS_ID AND B.TZ_BATCH_ID=?) WHERE A.TZ_CLASS_ID=?";
				Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[] { strBatchID, strClassID });
				if (map != null) {
					strClassName = (String) map.get("TZ_CLASS_NAME");
					strAppModalID = (String) map.get("TZ_APP_MODAL_ID");
					strBatchName = (String) map.get("TZ_BATCH_NAME");

					Map<String, Object> hMap = new HashMap<>();
					hMap.put("classID", strClassID);
					hMap.put("className", strClassName);
					hMap.put("modalID", strAppModalID);
					hMap.put("batchID", strBatchID);
					hMap.put("batchName", strBatchName);
					returnJsonMap.replace("formData", hMap);
				}

			} else {
				errMsg[0] = "1";
				errMsg[1] = "请选择报考方向和批次";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(returnJsonMap);
	}
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		// TODO Auto-generated method stub
		if(oprType.equals("queryGroups")) {
			System.out.println("++++++++++++++++");
		}
		return super.tzOther(oprType, strParams, errorMsg);
	}
}
