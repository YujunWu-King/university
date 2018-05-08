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
import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzMsPskshTbl;
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
			
			try {
				// 当前面试组;
				String tz_group_name = jacksonUtil.getString("tz_group_name");
				// 报名表编号;
				Long tz_app_ins_id = jacksonUtil.getLong("tz_app_ins_id");
				//评委组编号
				String inteGroup_id = jacksonUtil.getString("inteGroup_id");
				//班级ID
				String classID = jacksonUtil.getString("classID");
				//批次id
				String batchID = jacksonUtil.getString("batchID");
				
				
				//根据面试组名称和面试评委组查询对象
				PsTzInteGroup ptig = pstzInteGroupMapper.findByNameAndCid(tz_group_name, inteGroup_id);
				//根据班级id和报名表编号查询学生
				
				
				PsTzMsPskshTbl ptmpkt = psTzMsPskshTblMapper.selectByCidAndAid(classID, tz_app_ins_id);
				if(ptmpkt != null){
					Integer tz_group_id = null;
					if(ptig != null) {
						tz_group_id = ptig.getTz_group_id();
					}
					//设置面试组
					ptmpkt.setTzGroupId(tz_group_id);
					//设置更改时间
					ptmpkt.setTzGroupDate(new Date());
					//设置面试序号
					//首先查询该组已经分配了多少个学生
					List<PsTzMsPskshTbl> list = psTzMsPskshTblMapper.findByGroupID(tz_group_id);
					List<Long> list2 = new ArrayList<>();
					for (PsTzMsPskshTbl psTzMsPskshTbl : list) {
						list2.add(psTzMsPskshTbl.getTzAppInsId());
					}
					
					//根据list集合的大小给该考生分配序号
					if(list != null)
						//这里需要判断当前对象是否在list中
						if(list2.contains(ptmpkt.getTzAppInsId())) {
							ptmpkt.setTzOrder(list.size());
						}else {
							ptmpkt.setTzOrder(list.size() + 1);
						}
					//更新
					psTzMsPskshTblMapper.updateByPrimaryKey(ptmpkt);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String str = jacksonUtil.Map2json(mapRet);
		return str;
	}
	

}
