package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * 流程中发布结果的系统变量对应的类; Parameter: 流程的类方法的参数固定，为一个数字，数组第一个值为页面类型：
 * "A":表示报名中心传入解析，"B"表示面试申请页面，"C"表示历史报名页面 数组的第二个参数为报名表id字符串； 数组的第三个参数为项目的根目录；
 *
 * 返回的值也是固定的数组， 数组的第一个参数为：是否发布， 数组的第二个参数为 系统变量显示的内容；
 */
public class LcSysvarClass {
	// table 允许的列数量；
	private static int TABLE_COLUM_NUM = 6;
	@Autowired
	private SqlQuery sqlQuery;	
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzAppAdGenQrcodeServiceImpl TzAppAdGenQrcodeServiceImpl;

	// 报名表提交状态;
	public String[] getAppSubmitSatus(String[] para) {
		String type = para[0];
		long appIns = Long.parseLong(para[1]);
		String rootPath = para[2];
		String[] result = { "", "" };
		try {
			String isPublish = "";
			String content = "";

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");

			// 查看报名表是不是已经提交了;
			String appInsStatus = "", appTplId = "";
			Map<String, Object> appinsMap = jdbcTemplate.queryForMap(
					"select TZ_APP_FORM_STA,TZ_APP_TPL_ID from PS_TZ_APP_INS_T where TZ_APP_INS_ID=?",
					new Object[] { appIns });
			if (appinsMap != null) {
				appInsStatus = (String) appinsMap.get("TZ_APP_FORM_STA");
				appTplId = (String) appinsMap.get("TZ_APP_TPL_ID");
			}
			// 已经提交;
			if ("U".equals(appInsStatus)) {
				isPublish = "Y";
			} else {
				isPublish = "N";
			}

			String th = "";
			String td = "";
			String tableHtml = "";
			String thead = "";
			String tbody = "";
			int totalnum = jdbcTemplate.queryForObject(
					"SELECT count(1) FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? and TZ_PAGE_NO > 0",
					Integer.class, new Object[] { appTplId });
			if (totalnum > 0) {
				List<Map<String, Object>> list = jdbcTemplate.queryForList(
						"SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? and TZ_PAGE_NO > 0 ORDER BY TZ_ORDER ASC",
						new Object[] { appTplId });
				if (list != null && list.size() > 0) {
					int count = 0;
					// 行数;
					int rows = 0;
					String TZ_XXX_BH = "", TZ_XXX_MC = "";
					for (int i = 0; i < list.size(); i++) {
						count = count + 1;
						TZ_XXX_BH = (String) list.get(i).get("TZ_XXX_BH");
						TZ_XXX_MC = (String) list.get(i).get("TZ_XXX_MC");
						String isComplete = "";
						try {
							isComplete = jdbcTemplate.queryForObject(
									"SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?",
									String.class, new Object[] { appIns, TZ_XXX_BH });
						} catch (Exception e1) {
							isComplete = "";
						}
						if ("".equals(th)) {
							th = "<td>" + TZ_XXX_MC + "</td>";
						} else {
							th = th + "<td>" + TZ_XXX_MC + "</td>";
						}

						String td1 = "";
						if ("Y".equals(isComplete)) {
							td1 = "<td><img src=\"" + rootPath
									+ "/statics/css/website/images/table_check.png\">已完成</td>";
						} else {
							td1 = "<td><img src=\"" + rootPath + "/statics/css/website/images/alert.png\">未完成</td>";
						}
						if ("".equals(td)) {
							td = td1;
						} else {
							td = td + td1;
						}

						int yuShu = count % TABLE_COLUM_NUM;
						if (count == totalnum || yuShu == 0) {
							rows = rows + 1;
							// 如果大于等于2行，则把第一行开始小于总列数的补全;
							if (rows > 1 && yuShu != 0) {
								th = th + "<td colspan=\"" + (TABLE_COLUM_NUM - yuShu) + "\"></td>";
								td = td + "<td colspan=\"" + (TABLE_COLUM_NUM - yuShu) + "\"></td>";
							}
							thead = "<thead><tr>" + th + "</tr></thead>";
							tbody = "<tbody><tr>" + td + "</tr></tbody>";
							if ("".equals(tableHtml)) {
								tableHtml = thead + tbody;
							} else {
								tableHtml = tableHtml + thead + tbody;
							}
							th = "";
							td = "";
						}

					}
				}
			}

			String tableHtmlStart = "<table class=\"table_style1\">";

			String tableHtmlEnd = "</table>";

			content = tableHtmlStart + tableHtml + tableHtmlEnd;

			result[0] = isPublish;
			result[1] = content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	// 解析材料评审
	public String[] getLcpsStatus(String[] para) {
		String type = para[0];
		long appIns = Long.parseLong(para[1]);
		String rootPath = para[2];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_LCPS"});
			if(lcName != null && !"".equals(lcName)){
				result = this.analyLcDrInfo(lcName, type, appIns, rootPath, defalutString);
			}
		}catch(Exception e){
			
		}
		return result;
	}

	// 解析面试结果
	public String[] getMsjgStatus(String[] para) {
		String type = para[0];
		long appIns = Long.parseLong(para[1]);
		String rootPath = para[2];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_MSJG"});
			if(lcName != null && !"".equals(lcName)){
				
				result = this.analyLcDrInfo(lcName, type, appIns, rootPath, defalutString); String QrcodeHtml="";
				
				 /* 
				 * @author YTT
				 * @since 2017-02-20
				 * @desc 查询录取状态，如果状态为已录取，则显示查看电子录取通知书按钮
				 * 
				 * */
				//录取状态
					String tzLuquStaSql="SELECT TZ_LUQU_ZT FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_APP_INS_ID=?";
					String tzLuquSta= sqlQuery.queryForObject(tzLuquStaSql, new Object[] {appIns}, "String");
					if (tzLuquSta=="A"){
						//录取通知书二维码地址
						String Qrcodefilepath=TzAppAdGenQrcodeServiceImpl.genQrcode(appIns);
						//查看录取通知书按钮html
						QrcodeHtml=tzGDObject.getHTMLText("HTML.TZApplicationCenterBundle.TZ_GD_GENQRCODE_HTML",Qrcodefilepath);
						//将查看录取通知书按钮的html放在拼接面试结果html的开头
						result[1] = QrcodeHtml+result[1];
					};						
			}
		}catch(Exception e){
			
		}
		return result;
	}

	// 解析联考报名
	public String[] getLkbmStatus(String[] para) {
		String type = para[0];
		long appIns = Long.parseLong(para[1]);
		String rootPath = para[2];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_LKBM"});
			if(lcName != null && !"".equals(lcName)){
				result = this.analyLcDrInfo(lcName, type, appIns, rootPath, defalutString);
			}
		}catch(Exception e){
			
		}
		return result;
	}

	// 解析预录取
	public String[] getYlqStatus(String[] para) {
		String type = para[0];
		long appIns = Long.parseLong(para[1]);
		String rootPath = para[2];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_YLQ"});
			if(lcName != null && !"".equals(lcName)){
				result = this.analyLcDrInfo(lcName, type, appIns, rootPath, defalutString);
			}
		}catch(Exception e){
			
		}
		return result;
	}

	// 流程导入表信息;
	private String[] analyLcDrInfo(String lcName, String type, long appIns, String rootPath, String defalutString) {
		String[] result = { "", defalutString };
		// 查询表明;
		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		try {
			String tableName = jdbcTemplate.queryForObject(
					"select TZ_TARGET_TBL from TZ_IMP_TPL_DFN_T where TZ_TPL_ID=?", String.class,
					new Object[] { lcName });
			if (tableName == null || "".equals(tableName)) {
				return result;
			}

			// 是否发布;
			String isPublish = "";
			// 查询显示的字段;
			String th = "";
			String td = "";
			String tableHtml = "";
			String thead = "";
			String tbody = "";
			String content = "";
			int totalnum = jdbcTemplate.queryForObject(
					"select count(1) from TZ_IMP_TPL_FLD_T where TZ_TPL_ID=? and TZ_DISPLAY='Y'", Integer.class,
					new Object[] { lcName });
			if (totalnum > 0) {
				// 字段id;
				ArrayList<String> fieldIdList = new ArrayList<>();
				// 名称;
				ArrayList<String> fieldNameList = new ArrayList<>();
				// 查询的表字段;
				String fieldSelectSQL = "";
				String sql = "select TZ_FIELD,TZ_FIELD_NAME from TZ_IMP_TPL_FLD_T where TZ_TPL_ID=? and TZ_DISPLAY='Y' order by TZ_SEQ ASC";
				List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] { lcName });
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String fieldId = (String) list.get(i).get("TZ_FIELD");
						String fieldName = (String) list.get(i).get("TZ_FIELD_NAME");
						if (fieldName == null) {
							fieldName = "";
						}
						if ("".equals(fieldSelectSQL)) {
							fieldSelectSQL = fieldId;
						} else {
							fieldSelectSQL = fieldSelectSQL + " , " + fieldId;
						}
						fieldIdList.add(fieldId);
						fieldNameList.add(fieldName);
					}

					Map<String, Object> valueMap = null;
					try {
						fieldSelectSQL = " select " + fieldSelectSQL + " from " + tableName
								+ " where TZ_APP_INS_ID = ? ";
					
						valueMap = jdbcTemplate.queryForMap(fieldSelectSQL, new Object[] { appIns });
					} catch (Exception e1) {
						valueMap = null;
					}

					// 没值表示未发布;
					if (valueMap == null) {
						return result;
					}
					int count = 0;
					// 行数;
					int rows = 0;
					for (int i = 0; i < fieldNameList.size(); i++) {
						count = count + 1;
						String xxxId = fieldIdList.get(i);
						String xxxmc = fieldNameList.get(i);
						String xxxValue = "";
						if (valueMap != null) {
							isPublish = "Y";
							xxxValue = valueMap.get(xxxId) == null ? "" : String.valueOf(valueMap.get(xxxId));
						}
						if (xxxValue == null) {
							xxxValue = "";
						}

						if ("".equals(th)) {
							th = "<td>" + xxxmc + "</td>";
						} else {
							th = th + "<td>" + xxxmc + "</td>";
						}

						if ("".equals(td)) {
							td = "<td>" + xxxValue + "</td>";
						} else {
							td = td + "<td>" + xxxValue + "</td>";
						}

						int yuShu = count % TABLE_COLUM_NUM;
						if (count == totalnum || yuShu == 0) {
							rows = rows + 1;
							// 如果大于等于2行，则把第一行开始小于总列数的补全;
							if (rows > 1 && yuShu != 0) {
								th = th + "<td colspan=\"" + (TABLE_COLUM_NUM - yuShu) + "\"></td>";
								td = td + "<td colspan=\"" + (TABLE_COLUM_NUM - yuShu) + "\"></td>";
							}
							thead = "<thead><tr>" + th + "</tr></thead>";
							tbody = "<tbody><tr>" + td + "</tr></tbody>";
							if ("".equals(tableHtml)) {
								tableHtml = thead + tbody;
							} else {
								tableHtml = tableHtml + thead + tbody;
							}
							th = "";
							td = "";
						}
					}
				}
				String tableHtmlStart = "<table class=\"table_style1\">";

				String tableHtmlEnd = "</table>";

				content = tableHtmlStart + tableHtml + tableHtmlEnd;
				result[0] = isPublish;
				result[1] = content;
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
