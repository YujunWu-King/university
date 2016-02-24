package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock.Limit;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_GD_LCFB_PKG:TZ_GD_FB_CLS
 * @author tang
 *
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzGdFvClsServiceImpl")
public class TzGdFvClsServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	
	
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {

			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String str_bj_id = jacksonUtil.getString("bj_id");
			//总数;
	        int total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID=?",new Object[]{str_bj_id},"Integer");
	        List<Map<String, Object>> list ;
	        
	        if(numLimit > 0){
	        	String sql = "SELECT OPRID,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? ORDER BY TZ_APP_INS_ID ASC limit ?,?";
	        	list = jdbcTemplate.queryForList(sql,new Object[]{str_bj_id,numStart,numLimit});
	        }else{
	        	String sql = "SELECT OPRID,TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? ORDER BY TZ_APP_INS_ID ASC";
	        	list = jdbcTemplate.queryForList(sql,new Object[]{str_bj_id});
	        }
	        if(list !=null && list.size()>0){
	        	for(int i = 0; i < list.size(); i++){
	        		int num = 0;
	        		
	        		Map<String, Object> map = new HashMap<>();
	        		
	        		String str_oprid = (String)list.get(i).get("OPRID");
	        		long str_app_ins_id = Long.valueOf(list.get(i).get("TZ_APP_INS_ID").toString());
	        	
	        		// 查人员的姓名;
	        		String str_name = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=? limit 0,1", new Object[]{str_oprid},"String");
	                // 查人员的国籍;
	        		String str_country = jdbcTemplate.queryForObject("SELECT TZ_COUNTRY FROM PS_TZ_REG_USER_T WHERE OPRID=? limit 0,1", new Object[]{str_oprid},"String");
	                
	        		map.put("bj_id",  str_bj_id );
	        		map.put("opr_id", str_oprid);
	        		map.put("bmb_id", String.valueOf(str_app_ins_id));
	        		map.put("ry_name", str_name);
	        		map.put("country", str_country);
	        		
	        		String sql_1 = "SELECT TZ_APPPRO_ID,TZ_APPPRO_NAME FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM ASC";
	        		List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql_1,new Object[]{str_bj_id});
	        		if(list2 != null && list2.size()>0){
	        			for(int j = 0; j < list2.size(); j ++){
	        				
	        				String str_lc_id = (String)list2.get(j).get("TZ_APPPRO_ID");
	        				String str_lc_name = (String)list2.get(j).get("TZ_APPPRO_NAME")==null?"":(String)list2.get(j).get("TZ_APPPRO_NAME");
	        				String str_hfy_bh = jdbcTemplate.queryForObject("SELECT TZ_APPPRO_HF_BH FROM PS_TZ_APPPRO_RST_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APP_INS_ID=?", new Object[]{str_bj_id, str_lc_id, str_app_ins_id},"String");
	        				String str_color = "",str_jg="";
	        				if(str_hfy_bh == null || "".equals(str_hfy_bh)){
	        					Map<String, Object> bmlcHfMap = jdbcTemplate.queryForMap("SELECT TZ_APPPRO_COLOR,TZ_CLS_RESULT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_WFB_DEFALT_BZ='on'", new Object[]{str_bj_id, str_lc_id});
	        					if(bmlcHfMap != null){
	        						str_color = (String)bmlcHfMap.get("TZ_APPPRO_COLOR")==null?"":(String)bmlcHfMap.get("TZ_APPPRO_COLOR");
	        						str_jg = (String)bmlcHfMap.get("TZ_CLS_RESULT")==null?"":(String)bmlcHfMap.get("TZ_CLS_RESULT");
	        					}
	        				}else{
	        					Map<String, Object> bmlcHfMap = jdbcTemplate.queryForMap("SELECT TZ_APPPRO_COLOR,TZ_CLS_RESULT FROM PS_TZ_CLS_BMLCHF_T WHERE TZ_CLASS_ID=? AND TZ_APPPRO_ID=? AND TZ_APPPRO_HF_BH=?", new Object[]{str_bj_id, str_lc_id, str_hfy_bh});
	        					if(bmlcHfMap != null){
	        						str_color = (String)bmlcHfMap.get("TZ_APPPRO_COLOR")==null?"":(String)bmlcHfMap.get("TZ_APPPRO_COLOR");
	        						str_jg = (String)bmlcHfMap.get("TZ_CLS_RESULT")==null?"":(String)bmlcHfMap.get("TZ_CLS_RESULT");
	        					}
	        				}
	        				
	        				map.put("bmlc_"+num, str_lc_id);
	        				map.put((str_lc_id + "#^" + str_lc_name), str_color + "," +str_jg );
	        				num = num + 1;
	        			}
	        		}
	        		listData.add(map);
	        	}
	        }
	        mapRet.replace("total", total);
	        mapRet.replace("root", listData);

		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		return jacksonUtil.Map2json(mapRet);
	}

	
	@Override
	public String tzOther(String oprType, String strParams, String[] errorMsg) {
		String reString = "";
		if("tzLoadGridColumns".equals(oprType)){
			reString = this.tzLoadGridColumns(strParams, errorMsg);
		}
		return reString;
	}
	
	
	 /*加载配置的列表项*/
	private String tzLoadGridColumns(String comParams, String[] errorMsg) {
		// 返回值;
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		Map<String, Object> returnMap = new HashMap<>();
		returnMap.put("bmlc_zd", returnMap);
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			jacksonUtil.json2Map(comParams);
			// 班级编号;
			String strClassID = jacksonUtil.getString("classID");
		      
		    String sqlAuditGridColumn = "SELECT TZ_APPPRO_ID,TZ_APPPRO_NAME FROM PS_TZ_CLS_BMLC_T WHERE TZ_CLASS_ID=? ORDER BY TZ_SORT_NUM ASC";
		    List<Map<String, Object>> list = jdbcTemplate.queryForList(sqlAuditGridColumn,
					new Object[] { strClassID });
		    if(list != null && list.size() > 0){
		    	for(int i = 0; i < list.size(); i++){
		    		String str_lc_id = (String)list.get(i).get("TZ_APPPRO_ID");
		    		String str_lc_name = (String)list.get(i).get("TZ_APPPRO_NAME")==null?"":(String)list.get(i).get("TZ_APPPRO_NAME");
		    		Map<String, Object> map = new HashMap<>();
		    		map.put("bmlc_id", str_lc_id);
		    		map.put("bmlc_name", str_lc_name);
		    		listData.add(map);
		    	}
		    }
		    returnMap.replace("bmlc_zd", listData);
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}

		return jacksonUtil.Map2json(returnMap);
	}
}
