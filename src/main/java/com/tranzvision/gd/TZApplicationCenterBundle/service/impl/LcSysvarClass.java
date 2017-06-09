package com.tranzvision.gd.TZApplicationCenterBundle.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tranzvision.gd.util.base.GetSpringBeanUtil;

/**
 * 
 * 流程中发布结果的系统变量对应的类; Parameter: 流程的类方法的参数固定，为一个数字，数组第一个值为页面类型：
 * "A":表示报名中心传入解析，"B"表示面试申请页面，"C"表示历史报名页面 数组的第二个参数为报名表id字符串； 数组的第三个参数为项目的根目录； 数组的第四个参数为是否是手机
 * 数组的第五个参数为站点id;
 * 返回的值也是固定的数组， 数组的第一个参数为：是否发布， 数组的第二个参数为 系统变量显示的内容；
 */
public class LcSysvarClass {
	// table 允许的列数量；
	private static int TABLE_COLUM_NUM = 6;
	// 系统变量不可使用注入
	/*
	@Autowired
	private SqlQuery sqlQuery;	
	@Autowired
	private TZGDObject tzGDObject;
	@Autowired
	private TzAppAdGenQrcodeServiceImpl TzAppAdGenQrcodeServiceImpl;
*/
	// 报名表提交状态;
	public String[] getAppSubmitSatus(String[] para) {
		String type = para[0];
		long appIns = Long.parseLong(para[1]);
		String rootPath = para[2];
		String isMobile = para[3];
		String siteId = para[4];
		String[] result = { "", "" };
		try {
			String isPublish = "";
			String content = "";

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			
			// 查看报名表是不是已经提交了;
			String appInsStatus = "", appTplId = "",classId="";
			Map<String, Object> appinsMap = jdbcTemplate.queryForMap(
					"select A.TZ_APP_FORM_STA,A.TZ_APP_TPL_ID,B.TZ_CLASS_ID from PS_TZ_APP_INS_T A,PS_TZ_FORM_WRK_T B where A.TZ_APP_INS_ID=B.TZ_APP_INS_ID AND A.TZ_APP_INS_ID=?",
					new Object[] { appIns });
			if (appinsMap != null) {
				appInsStatus = (String) appinsMap.get("TZ_APP_FORM_STA");
				appTplId = (String) appinsMap.get("TZ_APP_TPL_ID");
				classId = (String) appinsMap.get("TZ_CLASS_ID");
			}
			// 已经提交;
			if ("U".equals(appInsStatus)) {
				isPublish = "Y";
			} else {
				isPublish = "N";
			}
			//报名表提交状态;
			String bmbTjStatusDesc = jdbcTemplate.queryForObject("select TZ_ZHZ_DMS from  PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_APPFORM_STATE' AND TZ_EFF_STATUS='A' AND TZ_ZHZ_ID=?", new Object[]{appInsStatus},String.class);
			if(bmbTjStatusDesc == null){
				bmbTjStatusDesc = "";
			}
			
			// 报名表链接;
			String applyFromUrl = rootPath + "/dispatcher?classid=appId&TZ_CLASS_ID=" + classId + "&SITE_ID=" + siteId + "&TZ_PAGE_ID=" ;

			String th = "";
			String td = "";
			String tableHtml = "";
			String thead = "";
			String tbody = "";
			
			int totalnum = jdbcTemplate.queryForObject(
					"SELECT count(1) FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? and TZ_PAGE_NO > 0",
					Integer.class, new Object[] { appTplId });
			//是否是手机访问;
			if("Y".equals(isMobile)){
				if (totalnum > 0) {
					List<Map<String, Object>> list = jdbcTemplate.queryForList(
							"SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? and TZ_PAGE_NO > 0 ORDER BY TZ_ORDER ASC",
							new Object[] { appTplId });
					if (list != null && list.size() > 0) {
						String TZ_XXX_BH = "", TZ_XXX_MC = "";
						for (int i = 0; i < list.size(); i++) {
							String span = "";
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
							if(i ==  list.size() -1){
								span = "<span class=\"fl width_40\">报名表提交状态</span><span class=\"fl\">"+bmbTjStatusDesc+"</span>";
								
							}else{
								span = "<span class=\"fl width_40\">" + TZ_XXX_MC + "</span>";
								
								//完成;
								if ("Y".equals(isComplete)) {
									/*span = span + "<span class=\"fl\"><img src=\"" + rootPath
											+ "/statics/css/website/m/images/reg_right.png\"></span>";*/
									span = span + "<span class=\"fl\">已完成</span>";
								} else {
									//无
									if("B".equals(isComplete)){
										span = span + "<span class=\"fl\">无</span>";
									}else{
										/*span = span + "<span class=\"fl\"><img src=\"" + rootPath
										+ "/statics/css/website/m/images/reg_warm.png\"></span>";*/
										span = span + "<span class=\"fl\">未完成</span>";
									}
									
								}
							}
							
							
							content = content +  "<div class=\"overhidden\">" + span + "</div>";
							
						}
					}
				}
			}else{
				if (totalnum > 0) {
					List<Map<String, Object>> list = jdbcTemplate.queryForList(
							"SELECT TZ_XXX_BH,TZ_XXX_MC FROM PS_TZ_APP_XXXPZ_T WHERE TZ_COM_LMC = 'Page' AND TZ_APP_TPL_ID = ? and TZ_PAGE_NO > 0 ORDER BY TZ_ORDER ASC",
							new Object[] { appTplId });
					
					if (list != null && list.size() > 0) {
						
						//每列的宽度;
						double width = 100L;
						if(list.size() >= TABLE_COLUM_NUM){
							width = Math.floor(width/TABLE_COLUM_NUM*10);
							
						}else{
							width = Math.floor(width/list.size()*10);
						}
						width = width/10;
						 
						 
						int count = 0;
						// 行数;
						int rows = 0;
						String TZ_XXX_BH = "", TZ_XXX_MC = "";
						for (int i = 0; i < list.size(); i++) {
							count = count + 1;
							TZ_XXX_BH = (String) list.get(i).get("TZ_XXX_BH");
							TZ_XXX_MC = (String) list.get(i).get("TZ_XXX_MC");
							String td1 = "";
							if(count == totalnum){
								//最后一列不显示报名表的正式提交列, 直接显示报名表的状态;
								TZ_XXX_MC = "报名表提交状态";
								// 已经提交;
								td1 = "<td>" + bmbTjStatusDesc + "</td>";
							}else{
								String isComplete = "";
								try {
									isComplete = jdbcTemplate.queryForObject(
											"SELECT TZ_HAS_COMPLETE FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?",
											String.class, new Object[] { appIns, TZ_XXX_BH });
								} catch (Exception e1) {
									isComplete = "";
								}
								
	
								//每页对应额链接;
								String everyApplyFromUrl = applyFromUrl + TZ_XXX_BH;
								if ("Y".equals(isComplete)) {
									td1 = "<td><img src=\"" + rootPath
											+ "/statics/css/website/images/table_check.png\"><a href=\""+everyApplyFromUrl+"\" style=\"text-decoration:underline;\">已完成</a></td>";
								} else if("B".equals(isComplete)){
									td1 = "<td><img src=\"" + rootPath + "/statics/css/website/images/alert.png\"><a href=\""+everyApplyFromUrl+"\" style=\"text-decoration:underline;\">无</a></td>";
								}else {
									td1 = "<td><img src=\"" + rootPath + "/statics/css/website/images/alert.png\"><a href=\""+everyApplyFromUrl+"\" style=\"text-decoration:underline;\">未完成</a></td>";
								}
							}
							if ("".equals(th)) {
								if(i < TABLE_COLUM_NUM){
									th = "<td width=\"" + width + "%\">" + TZ_XXX_MC + "</td>";
								}else{
									th = "<td>" + TZ_XXX_MC + "</td>";
								}
								
							} else {
								if(i < TABLE_COLUM_NUM){
									th = th + "<td width=\"" + width + "%\">" + TZ_XXX_MC + "</td>";
								}else{
									th = th + "<td>" + TZ_XXX_MC + "</td>";
								}
								
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
					
					tableHtml = tableHtml + "<thead><tr><td colspan=\"3\">推荐人提交推荐信情况</td><td colspan=\"3\">打印PDF报名表</td></tr></thead>";
					
					String tjrqkxx = "";
					String tjxSql = "select TZ_REFERRER_NAME,TZ_TJX_APP_INS_ID,TZ_REF_LETTER_ID,TZ_REFLETTERTYPE from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID=? and TZ_MBA_TJX_YX='Y' order by TZ_TJR_ID asc";
					List<Map<String, Object>> tjxList = jdbcTemplate.queryForList(tjxSql,new Object[]{appIns});
					if(tjxList != null && tjxList.size() > 0){
			        	for(int i = 0 ; i < tjxList.size(); i++){
			        		String tjrxx = (String)tjxList.get(i).get("TZ_REFERRER_NAME")==null?"":(String)tjxList.get(i).get("TZ_REFERRER_NAME");
			        		Long tjxInstId = 0L;
			        		try{
			        			tjxInstId = Long.parseLong(tjxList.get(i).get("TZ_TJX_APP_INS_ID").toString());
			        		}catch(Exception e){
			        			tjxInstId = 0L;
			        		}
			        		String TZ_REF_LETTER_ID = (String)tjxList.get(i).get("TZ_REF_LETTER_ID")==null?"":(String)tjxList.get(i).get("TZ_REF_LETTER_ID"); 
			        		//String TZ_REFLETTERTYPE = (String)tjxList.get(i).get("TZ_REFLETTERTYPE")==null?"":(String)tjxList.get(i).get("TZ_REFLETTERTYPE"); 
			        		
			        		// 是否提交;
			                int isTj = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> ' ' AND A.ATTACHUSERFILE <> ' ') OR  EXISTS (SELECT 'Y' FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U' AND A.TZ_TJX_APP_INS_ID > 0)) AND A.TZ_APP_INS_ID = ? and A.TZ_REF_LETTER_ID=? and A.TZ_MBA_TJX_YX='Y'", new Object[]{appIns, TZ_REF_LETTER_ID},Integer.class);
			                if(isTj > 0){
			                	if("".equals(tjrqkxx)){
			                		tjrqkxx =  "推荐人" + (i+1) + "[" + tjrxx + "]:已提交";
			                	}else{
			                		tjrqkxx = tjrqkxx + "<br>推荐人" + (i+1) + "[" + tjrxx + "]:已提交";
			                	}
			                	
			                }else{
			                	if("".equals(tjrqkxx)){
			                		tjrqkxx = "推荐人" + (i+1) + "[" + tjrxx + "]:未提交";
			                	}else{
			                		tjrqkxx = tjrqkxx + "<br>推荐人" + (i+1) + "[" + tjrxx + "]:未提交";
			                	}
			                	
			                }
			                
			        	}
			         }
					
					tjrqkxx = "<td colspan=\"3\">" + tjrqkxx + "</td>";
					
					//打印报名表;
					String applyFormPrint = rootPath + "/PrintPdfServlet?instanceID=" + appIns;
					//未提交也可以打印;
					tjrqkxx = tjrqkxx + "<td colspan=\"3\">" + "<a target='_blank' href='"+applyFormPrint+"'>打印报名表</a>" + "</td>";
					/*
					if ("U".equals(appInsStatus)) {
						tjrqkxx = tjrqkxx + "<td colspan=\"3\">" + "<a target='_blank' href='"+applyFormPrint+"'>打印报名表</a>" + "</td>";
					}else{
						tjrqkxx = tjrqkxx + "<td colspan=\"3\">需要先提交报名表</td>";
					}
					*/
					tableHtml = tableHtml + "<tbody><tr>" + tjrqkxx + "</tr></tbody>";
				}

				String tableHtmlStart = "<table class=\"table_style1\">";

				String tableHtmlEnd = "</table>";

				content = tableHtmlStart + tableHtml + tableHtmlEnd;
			}
			

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
		String isMobile = para[3];
		String siteId = para[4];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_LCPS"});
			if(lcName != null && !"".equals(lcName)){
				result = this.analyLcDrInfo(siteId,lcName, type, appIns, rootPath,isMobile, defalutString);
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
		String isMobile = para[3];
		String siteId = para[4];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_MSJG"});
			if(lcName != null && !"".equals(lcName)){
				
				result = this.analyLcDrInfo(siteId,lcName, type, appIns, rootPath, isMobile,defalutString); 
				
				String QrcodeHtml="";
				//录取状态
				String tzLuquStaSql="SELECT TZ_LUQU_ZT FROM PS_TZ_MSPS_KSH_TBL WHERE TZ_APP_INS_ID=?";
				String tzLuquSta="";
				try{
					tzLuquSta = jdbcTemplate.queryForObject(tzLuquStaSql, new Object[] {appIns},String.class);
				}catch(Exception e){
					tzLuquSta = "";
				}
				if ("LQ".equals(tzLuquSta)){
					if("Y".equals(isMobile)){
						QrcodeHtml = "<div class=\"overhidden\" onclick='openRqQrcode(\""+appIns+"\")'><i class=\"add_icon\"></i><span class=\"fl\" style=\"color:#666;\">查看电子版条件录取通知书</span></div>";
					}else{
						QrcodeHtml = "<div class=\"overhidden\"><a onclick='openRqQrcode(\""+appIns+"\")' href=\"javascript:void(0);\"><img class=\"fl table_ck\" src=\"" + rootPath + "/statics/css/website/images/table_search.png\"></a><p>查看电子版条件录取通知书</p></div>";
					}
					
				}
				
				result[1] = QrcodeHtml + result[1];
				//录取状态
				/*
				   
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
					*/						
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
		String isMobile = para[3];
		String siteId = para[4];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_LKBM"});
			if(lcName != null && !"".equals(lcName)){
				result = this.analyLcDrInfo(siteId,lcName, type, appIns, rootPath,isMobile, defalutString);
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
		String isMobile = para[3];
		String siteId = para[4];

		GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
		
		String defalutString = "<span>未发布</span>";
		String[] result = {"",defalutString};
		String sql ="select TZ_HARDCODE_VAL from PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT=?";
		try{
			String lcName = jdbcTemplate.queryForObject(sql, String.class, new Object[]{"TZ_YLQ"});
			if(lcName != null && !"".equals(lcName)){
				result = this.analyLcDrInfo(siteId,lcName, type, appIns, rootPath,isMobile, defalutString);
			}
		}catch(Exception e){
			
		}
		return result;
	}

	// 流程导入表信息;
	private String[] analyLcDrInfo(String siteId,String lcName, String type, long appIns, String rootPath,String isMobile, String defalutString) {
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
			if("Y".equals(isMobile)){
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
						
						for (int i = 0; i < fieldNameList.size(); i++) {
							String xxxId = fieldIdList.get(i);
							String xxxmc = fieldNameList.get(i);
							String xxxValue = "";
							
							if (valueMap != null) {
								xxxValue = valueMap.get(xxxId) == null ? "" : String.valueOf(valueMap.get(xxxId));
							}
							if (xxxValue == null) {
								xxxValue = "";
							}
							
							//至少要有一个有值才表示已经发布并显示;
							xxxValue = xxxValue.trim();
							if(!"".equals(xxxValue)){
								isPublish = "Y";
							}

							content = content +  "<div class=\"overhidden\"><span class=\"fl width_40\">" + xxxmc + "</span><span class=\"fl\">" + xxxValue + "</span></div>";

						}
					}
					if("Y".equals(isPublish)){
						result[0] = isPublish;
						result[1] = content;
					}
					
				}
			}else{
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
						int listSize = list.size();
						//每列的宽度;
						double width = 100L;
						if(listSize >= TABLE_COLUM_NUM){
							width = Math.floor(width/TABLE_COLUM_NUM*10);
							
						}else{
							width = Math.floor(width/list.size()*10);
						}
						width = width/10;
						
						//最后一行要不要合并;
						//是否每列合并；
						boolean colspanBl = false;
						int colspanNum = 1;
						//大于多少列考虑合并;
						int colspanStartNum = TABLE_COLUM_NUM;
						
						if(listSize > TABLE_COLUM_NUM && (listSize%TABLE_COLUM_NUM) != 0){
							//最后一行有多少列;
							int lastColumnNum = listSize % TABLE_COLUM_NUM;
							colspanBl = true;
							//能除尽则每列合并,不能最后一列合并;
							if(TABLE_COLUM_NUM % lastColumnNum == 0){
								colspanNum = TABLE_COLUM_NUM / lastColumnNum;
								colspanStartNum = listSize - (listSize%TABLE_COLUM_NUM);
							}else{
								colspanNum = TABLE_COLUM_NUM - (listSize%TABLE_COLUM_NUM) + 1;
								colspanStartNum = listSize -1;
							}
						}
						
						
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
								xxxValue = valueMap.get(xxxId) == null ? "" : String.valueOf(valueMap.get(xxxId));
							}
							if (xxxValue == null) {
								xxxValue = "";
							}
							
							xxxValue = xxxValue.trim();
							

							if ("".equals(th)) {
								if(i < TABLE_COLUM_NUM){
									th = "<td width=\"" + width + "%\">" + xxxmc + "</td>";
								}else{
									if(colspanBl == true && i >= colspanStartNum){
										th = "<td colspan=\"" + colspanNum + "\">" + xxxmc + "</td>";
									}else{
										th = "<td >" + xxxmc + "</td>";
									}
									
								}
							} else {
								if(i < TABLE_COLUM_NUM){
									th = th + "<td width=\"" + width + "%\">" + xxxmc + "</td>";
								}else{
									if(colspanBl == true && i >= colspanStartNum){
										th =  th + "<td colspan=\"" + colspanNum + "\">" + xxxmc + "</td>";
									}else{
										th = th + "<td>" + xxxmc + "</td>";
									}
									
								}
								
							}
							
							//如果是材料评审且面试的资格为："有"则显示
							String msRsl = "";
							if("TZ_RESULT".equals(xxxId) && "TZ_LCPS".equals(lcName)){
								msRsl = jdbcTemplate.queryForObject("select TZ_RESULT_CODE from TZ_IMP_CLPS_TBL WHERE TZ_APP_INS_ID=?",new Object[]{appIns},String.class);
								if("有".equals(msRsl)){
									xxxValue = xxxValue + "，面试预约请<a href=\"" + rootPath + "/dispatcher?classid=Interview&siteId="+siteId+"\">点击此处</a>";
								}
							}
							
							//至少要有一个有值才表示已经发布并显示;
							if(!"".equals(xxxValue)){
								isPublish = "Y";
							}
							
							if ("".equals(td)) {
								if(colspanBl == true && i >= colspanStartNum){
									td = "<td style=\"word-break:break-all;\" colspan=\"" + colspanNum + "\">" + xxxValue + "</td>";
								}else{
									td = "<td style=\"word-break:break-all;\">" + xxxValue + "</td>";
								}
								
							} else {
								if(colspanBl == true && i >= colspanStartNum){
									td = td + "<td style=\"word-break:break-all;\" colspan=\"" + colspanNum + "\">" + xxxValue + "</td>";
								}else{
									td = td + "<td style=\"word-break:break-all;\">" + xxxValue + "</td>";
								}
								
							}

							int yuShu = count % TABLE_COLUM_NUM;
							if (count == totalnum || yuShu == 0) {
								rows = rows + 1;
								// 如果大于等于2行，则把第一行开始小于总列数的补全;
								/*
								if (rows > 1 && yuShu != 0) {
									th = th + "<td colspan=\"" + (TABLE_COLUM_NUM - yuShu) + "\"></td>";
									td = td + "<td colspan=\"" + (TABLE_COLUM_NUM - yuShu) + "\"></td>";
								}
								*/
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
					if("Y".equals(isPublish)){
						result[0] = isPublish;
						result[1] = content;
					}
					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
