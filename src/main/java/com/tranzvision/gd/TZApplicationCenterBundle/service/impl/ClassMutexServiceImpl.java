package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_GD_BJXZQ_APP:ClassMutex
 * @author tang
 * 班级互斥
 */
@Service("com.tranzvision.gd.TZApplicationCenterBundle.service.impl.ClassMutexServiceImpl")
public class ClassMutexServiceImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	/**
	 * Parameter：&classId 当前班级ID	
	 * 描述：通过班级ID查询班级互斥规则ID，在通过互斥规则ID查询出所有班级ID	
	 * 如果存在互斥班级并提交过申请则返回true,否则返回false
	 */
	public boolean isClassMutex(String classid){
		//当前机构号，如果没有机构号获取超级机构ID;
		String str_curOrgIf = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		if(str_curOrgIf == null || "".equals(str_curOrgIf)){
			return false;
		}
		String oprId = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// 根据当前的班级编号查询出所有的规则ID;
		String sql_hcgz = "SELECT DISTINCT B.TZ_CLS_HCGZ_ID FROM PS_TZ_CLS_HCGZCS_T A,PS_TZ_CLS_HCGZ_T B WHERE A.TZ_CLS_HCGZ_ID=B.TZ_CLS_HCGZ_ID AND B.TZ_JG_ID=? AND A.TZ_CLASS_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql_hcgz,new Object[]{str_curOrgIf,classid});
		if(list != null && list.size()>0){
			for(int i=0; i < list.size();i++){
				String str_hcgz_id = (String)list.get(i).get("TZ_CLS_HCGZ_ID");
				/*除了当前班级，其他互斥班级id*/
				String sql_hcbj = "SELECT DISTINCT TZ_CLASS_ID FROM PS_TZ_CLS_HCGZCS_T WHERE TZ_CLS_HCGZ_ID=? AND TZ_CLASS_ID<>?";
				List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql_hcbj,new Object[]{str_hcgz_id,classid});
				if(list2 != null && list2.size()>0){
					for(int j=0; j < list2.size();j++){
						String str_hcbj_id = (String)list2.get(j).get("TZ_CLASS_ID");
						/*查询在互斥班级中是否申请过其他班级，如果有则返回true*/
				        int count = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=? AND OPRID=?", new Object[]{str_hcbj_id,oprId},"Integer");
				        if(count > 0){
				        	return true;
				        }
					}
				}
			}
		}
		return false;
	}
}
