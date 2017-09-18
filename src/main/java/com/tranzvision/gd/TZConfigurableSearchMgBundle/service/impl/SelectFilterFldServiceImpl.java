package com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterFldTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.dao.PsTzFilterYsfTMapper;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterFldTKey;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterYsfT;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 可配置搜索添加搜索字段
 * 
 * @author tang 原PS类 TZ_GD_FILTERGL_PKG:TZ_XZ_FILTERFLD_CLS
 */
@Service("com.tranzvision.gd.TZConfigurableSearchMgBundle.service.impl.SelectFilterFldServiceImpl")
public class SelectFilterFldServiceImpl extends FrameworkImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private PsTzFilterFldTMapper psTzFilterFldTMapper;
	@Autowired
	private PsTzFilterYsfTMapper psTzFilterYsfTMapper;
	
	@Autowired
	private GetSeqNum getSeqNum;
	
	/* 查询可以添加的可配置搜索字段列表 */
	@Override
	public String tzQueryList(String comParams, int numLimit, int numStart, String[] errorMsg) {
		// 返回值;
		Map<String, Object> mapRet = new HashMap<String, Object>();
		mapRet.put("total", 0);
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
		mapRet.put("root", listData);
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {

			// 将字符串转换成json;
			jacksonUtil.json2Map(comParams);
			
			String str_com_id = jacksonUtil.getString("ComID");
			String str_page_id = jacksonUtil.getString("PageID");
			String str_view_name = jacksonUtil.getString("ViewMc");
			String str_field_name = jacksonUtil.getString("FieldMc");
			
			String str_field_view = jacksonUtil.getString("FieldView");
			
			if(str_field_name != null){
				str_field_name = str_field_name.trim().toUpperCase();
			}
			
			int tableNameCount = 0;
			String tableName = str_field_view;
			String tableNameSql = "select COUNT(1) from TZ_SCHEMA_TABLES_VW where TABLE_NAME=?";
			tableNameCount = jdbcTemplate.queryForObject(tableNameSql,new Object[]{str_field_view},"Integer");
			if(tableNameCount <= 0){
				tableName =  "PS_" + str_field_view;
			}
			
			int total = 0;
			// 查询总数;
			String totalSQL = "select COUNT(1) from TZ_SCHEMA_COLUMNS_VW where TABLE_NAME=? AND UPPER(COLUMN_NAME) LIKE ? ORDER BY ORDINAL_POSITION ASC";
			
			total = jdbcTemplate.queryForObject(totalSQL, new Object[] {tableName,"%"+str_field_name+"%" },
					"Integer");
			
			String sql = "select COLUMN_NAME,COLUMN_COMMENT from TZ_SCHEMA_COLUMNS_VW where TABLE_NAME=? AND UPPER(COLUMN_NAME) LIKE ? ORDER BY ORDINAL_POSITION ASC limit ?,? ";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,
					new Object[] { tableName,"%"+str_field_name+"%" ,numStart,numLimit});
			
			if (list != null) {
				
				for (int i = 0; i < list.size(); i++) {
					
					try{
						String str_field_mc = (String) list.get(i).get("COLUMN_NAME");
						String str_field_desc = (String) list.get(i).get("COLUMN_COMMENT");
						
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("orderNum", i + 1);
						map.put("ComID", str_com_id);
						map.put("PageID", str_page_id);
						map.put("ViewMc", str_view_name);
						map.put("FieldMc", str_field_mc);
						map.put("fieldDesc", str_field_desc);
						map.put("FieldView", str_field_view);
						
						listData.add(map);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				mapRet.replace("total", total);
				mapRet.replace("root", listData);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jacksonUtil.Map2json(mapRet);
	}
	
	@Override
	/*新增搜索字段*/
	public String tzAdd(String[] actData, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		try {
			int num = 0;
			for (num = 0; num < actData.length; num++) {
				// 表单内容;
				String strForm = actData[num];
				// 将字符串转换成json;
				jacksonUtil.json2Map(strForm);
				// 信息内容;
				String str_com_id = jacksonUtil.getString("ComID");
				String str_page_id = jacksonUtil.getString("PageID");
				String str_view_name = jacksonUtil.getString("ViewMc");
				String str_field_name = jacksonUtil.getString("FieldMc");
				
				String str_field_view = jacksonUtil.getString("FieldView");
				
				String strDeepQueryFlg="";
				if (str_field_view.equals(str_view_name)){
					strDeepQueryFlg="N";
				}else{
					strDeepQueryFlg="Y";
				}
				
				int tableNameCount = 0;
				String tableName = str_field_view;
				String tableNameSql = "select COUNT(1) from TZ_SCHEMA_TABLES_VW where TABLE_NAME=?";
				tableNameCount = jdbcTemplate.queryForObject(tableNameSql,new Object[]{str_field_view},"Integer");
				if(tableNameCount <= 0){
					tableName =  "PS_" + str_field_view;
				}
				
				String str_fieldgl_desc = "";
				
				PsTzFilterFldT psTzFilterFldT = new PsTzFilterFldT();
				PsTzFilterFldTKey psTzFilterFldTKey = new PsTzFilterFldTKey();
				psTzFilterFldTKey.setTzComId(str_com_id);
				psTzFilterFldTKey.setTzPageId(str_page_id);
				psTzFilterFldTKey.setTzViewName(str_view_name);
				psTzFilterFldTKey.setTzFilterFld(str_field_name);
				psTzFilterFldT = psTzFilterFldTMapper.selectByPrimaryKey(psTzFilterFldTKey);
				if(psTzFilterFldT == null || "".equals(psTzFilterFldT.getTzComId())){
					String maxSortSQL = "select Max(TZ_SORT_NUM) As TZ_SORT_NUM from PS_TZ_FILTER_FLD_T where TZ_COM_ID=? and TZ_PAGE_ID=? and TZ_VIEW_NAME=?";
					int numxh = 0;
					try{
						numxh= jdbcTemplate.queryForObject(maxSortSQL, new Object[]{str_com_id,str_page_id,str_view_name},"Integer");
					}catch(Exception e){
						numxh = 0;
					}
					String fieldNameSQL = "select DATA_TYPE,COLUMN_COMMENT from TZ_SCHEMA_COLUMNS_VW where TABLE_NAME=? AND COLUMN_NAME=?";
					Map<String, Object> map = jdbcTemplate.queryForMap(fieldNameSQL,new Object[]{tableName,str_field_name});
					
					if (map!=null){
						
						str_fieldgl_desc = (String) map.get("COLUMN_COMMENT");
						//String fieldType = (String) map.get("DATA_TYPE");
						String fieldType =  map.get("DATA_TYPE").toString();
						
						psTzFilterFldT = new PsTzFilterFldT();
						psTzFilterFldT.setTzComId(str_com_id);
						psTzFilterFldT.setTzPageId(str_page_id);
						psTzFilterFldT.setTzViewName(str_view_name);
						psTzFilterFldT.setTzFilterFld(str_field_name);
						psTzFilterFldT.setTzFilterFldDesc(str_fieldgl_desc);
						psTzFilterFldT.setTzSortNum(numxh +1);
						
						//增加DeepQuery字段
						psTzFilterFldT.setTzDeepqueryFlg(strDeepQueryFlg);
						if("Y".equals(strDeepQueryFlg)){
							psTzFilterFldT.setTzDeepqueryView(str_field_view);
							//psTzFilterFldT.setTzDeepqueryFld("");
						}
						psTzFilterFldTMapper.insertSelective(psTzFilterFldT);
	
						// 类型String值;
						String intTypeString = "VARCHAR,CHAR,LONGTEXT,TEXT";
						PsTzFilterYsfT psTzFilterYsfT = new PsTzFilterYsfT();
						psTzFilterYsfT.setTzComId(str_com_id);
						psTzFilterYsfT.setTzPageId(str_page_id);
						psTzFilterYsfT.setTzViewName(str_view_name);
						psTzFilterYsfT.setTzFilterFld(str_field_name);
						if(intTypeString.contains(fieldType.toUpperCase())){
							psTzFilterYsfT.setTzFilterYsf("07");
							psTzFilterYsfT.setTzFilterBdyQy("1");
							psTzFilterYsfT.setTzIsDefOprt("1");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
						
							psTzFilterYsfT.setTzFilterYsf("08");
							psTzFilterYsfT.setTzFilterBdyQy("1");
							psTzFilterYsfT.setTzIsDefOprt("0");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							
						}else{
							psTzFilterYsfT.setTzFilterYsf("01");
							psTzFilterYsfT.setTzFilterBdyQy("1");
							psTzFilterYsfT.setTzIsDefOprt("1");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							
							psTzFilterYsfT.setTzFilterYsf("02");
							psTzFilterYsfT.setTzIsDefOprt("0");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							
							psTzFilterYsfT.setTzFilterYsf("03");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							
							psTzFilterYsfT.setTzFilterYsf("04");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							
							psTzFilterYsfT.setTzFilterYsf("05");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
							
							psTzFilterYsfT.setTzFilterYsf("06");
							psTzFilterYsfTMapper.insert(psTzFilterYsfT);
						}
					}

				}else{
					errMsg[0]="1";
					errMsg[1]="试图添加的值已存在，请指定新值。";
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}
	
	
	
}
