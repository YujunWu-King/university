package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzFormZlshTMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.dao.PsTzInteGroupMapper;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshTKey;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzInteGroup;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZJudgesTypeBundle.dao.PsTzClpsGrTblMapper;
import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzClpsGrTbl;
import com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao.psTzClpsPwTblMapper;
import com.tranzvision.gd.TZMbaPwClpsBundle.dao.PsTzMsPskshTblMapper;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 原PS：TZ_GD_BMGL_BMBSH_PKG:TZ_GD_BMGL_AUDIT_CLS
 * 
 * @author tang 面试现场分组-报名表审核
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdMsglClassServiceImpl")
public class TzGdMsglClassServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzFormZlshTMapper psTzFormZlshTMapper;
	@Autowired
	private PsTzInteGroupMapper pstzInteGroupMapper;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private PsTzMsPskshTblMapper psTzMsPskshTblMapper;
	@Autowired
	private PsTzClpsGrTblMapper psTzClpsGrTblMapper;

	// 获取评委组信息
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(comParams);
		
		//查询所有的评委组
		List<PsTzClpsGrTbl> list = psTzClpsGrTblMapper.findAll();
		
		//
		for (int i = 0; i < list.size(); i++) {
			PsTzClpsGrTbl pcgt = list.get(i);	
			Map<String, Object> mapList = new HashMap<>();
			mapList.put("jugGroupId", pcgt.getTzClpsGrId());
			mapList.put("jugGroupName", pcgt.getTzClpsGrName());
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
	
	//获取面试组信息 
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		jacksonUtil.json2Map(strParams);
		
		// TODO Auto-generated method stub
		if(oprType.equals("queryGroups")) {
			
			// 根据评委组查询面试组信息
			String jugGroupId = jacksonUtil.getString("jugGroupId");
			List<PsTzInteGroup> list = pstzInteGroupMapper.findByPwId(jugGroupId);
			
			for (int i = 0; i < list.size(); i++) {
				PsTzInteGroup pg = list.get(i);
				Map<String, Object> mapList = new HashMap<>();
				mapList.put("tz_group_name", pg.getTz_group_name());
				mapList.put("tz_group_id", pg.getTz_group_id());
				
				
				//查询该面试组已有多少面试人
				String sql = "select * from PS_TZ_MSPS_KSH_TBL where TZ_GROUP_ID =?";
				List<Object> list2 = jdbcTemplate.queryForList(sql, new Object[] {pg.getTz_group_id()});
				
				mapList.put("interviewers", list2.size());
				listData.add(mapList);
			}
			mapRet.replace("total", list.size());
			mapRet.replace("root", listData);
		}else if("updateStu".equals(oprType)) {
			System.out.println("----------");
			
			try {
				// 当前面试组;
				Integer tz_group_id = jacksonUtil.getInt("tz_group_id");
				// 报名表编号;
				Long tz_app_ins_id = jacksonUtil.getLong("tz_app_ins_id");
				
				//班级ID
				String classID = jacksonUtil.getString("classID");
				
				//根据班级id和报名表编号查询学生
				//PsTzMspsKshTbl pmskt = psTzMspsKshTblMapper.findById(classID, tz_app_ins_id);
				//设置面试组
				//psi.setGroup_id(tz_group_id);
				//设置更改时间
				//psi.setGroup_date(new Date());
				//设置面试序号
				//首先查询该组已经分配了多少个学生
				//List<PsTzStuInfo> list = pstStuMapper.findByGroupID(tz_group_id);
				//根据list集合的大小给该考生分配序号
				//if(list != null)
					//psi.setOrder(list.size() + 1);
				//更新
				//pstStuMapper.updateStu(psi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}
		String str = jacksonUtil.Map2json(mapRet);
		return str;
	}
	
	/* 将学生进行面试分组 */
	public String tzUpdateStu(Map<String, Object> infoData, String[] errMsg) {
		// 返回值;
		String strRet = "{}";
		System.out.println("-------------------------");
		try {
			// 班级编号;
			Integer tz_group_id = (Integer) infoData.get("tz_group_id");
			System.out.println(tz_group_id);
			// tz_app_ins_id;
			Long tz_app_ins_id = (Long) infoData.get("tz_app_ins_id");
			System.out.println(tz_app_ins_id);
			
			//根据报名表编号查询报名表
//			PsTzStuInfo psi = pstzStuMapper.findById(tz_app_ins_id);
//			if(psi != null) {
//				psi.setGroup_id(tz_group_id);
//				psi.setGroup_date(new Date());
//				pstzStuMapper.updateStu(psi);
//			}
			
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}

}
