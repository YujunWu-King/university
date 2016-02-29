package com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl;

import java.io.File;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * PS:TZ_GD_PRINTWORD_PKG:TZ_DEALWITH_XML
 * @author Administrator
 *
 */
@Service("com.tranzvision.gd.TZApplicationVerifiedBundle.service.impl.TzDealWithXMLServiceImpl")
public class TzDealWithXMLServiceImpl {
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	/*
	 * 参数：&app_ins_id :报名表编号;&OPRID :打印当前行personid;&str_app_modal_id :报名表模板编号;
	 */
	/* 功能：生成存储报名表信息项名称及属性ID的数组一维数组; */
	public ArrayList<String> getData(String app_ins_id, String OPRID, String str_app_modal_id, String[] errMsg) {
		if(app_ins_id == null || "".equals(app_ins_id)
				|| str_app_modal_id == null || "".equals(str_app_modal_id)){
			errMsg[0] = "1";
			errMsg[1] = "参数不够，必须传入报名表实例编号、报名表模板编号！";
		}
		
		long int_app_ins_id = 0;
		try {
			int_app_ins_id = Long.parseLong(app_ins_id);
		} catch (Exception e) {
			errMsg[0] = "1";
			errMsg[1] = "非法的报名表实例编号，报名表实例编号必须为数字！";
		}

		int str_is_hava_ins = jdbcTemplate.queryForObject("SELECT count(1) FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?",
				new Object[] { int_app_ins_id }, "Integer");
		if (str_is_hava_ins == 0) {
			errMsg[0] = "1";
			errMsg[1] = "没有该报名表实例编号对应的报名数据！";
		}

		if (str_app_modal_id == null || "".equals(str_app_modal_id)) {
			errMsg[0] = "1";
			errMsg[1] = "报名表模板编号不能为空！";
		} else {
			int str_is_have_modal = jdbcTemplate.queryForObject(
					"SELECT count(1) FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ?", new Object[] { str_app_modal_id },
					"Integer");
			if (str_is_have_modal == 0) {
				errMsg[0] = "1";
				errMsg[1] = "该报名表模版编号不是有效的模板编号！";
			}
		}
		
		
		ArrayList<String> appins_data = new ArrayList<>();

		/* 到这里，应该需要的参数都已经初始化，可以判断当前人是否有查看他人报名表的角色 */
		/*
		 * 1.传入的OPRID是否是当前人 2.是--有权限打印报名表 3.不是--是否是材料评委、面试评委、班级管理员
		 */
		String currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		if (currentOprid != null && !currentOprid.equals(OPRID)) {
			int haveQx = jdbcTemplate.queryForObject(
					"SELECT COUNT(1) FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID = ( SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ? limit 0,1) AND OPRID = ?",
					new Object[] { app_ins_id, currentOprid }, "Integer");
			if (haveQx == 0) {
				haveQx = jdbcTemplate.queryForObject(
						"SELECT COUNT(1) FROM PS_TZ_MSPS_PW_TBL WHERE TZ_CLASS_ID = ( SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ? limit 0,1) AND TZ_PWEI_OPRID = ?",
						new Object[] { app_ins_id, currentOprid }, "Integer");
				if (haveQx == 0) {
					haveQx = jdbcTemplate.queryForObject(
							"SELECT COUNT(1) FROM PS_TZ_CLPS_PW_TBL WHERE TZ_CLASS_ID = ( SELECT TZ_CLASS_ID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ? limit 0,1) AND TZ_PWEI_OPRID = ?",
							new Object[] { app_ins_id, currentOprid }, "Integer");
				}
			}
			if (haveQx == 0) {
				errMsg[0] = "1";
				errMsg[1] = "没有权限！";
			}
		}

		// 编号，类型，名称;
		String TZ_XXX_NO = "", TZ_XXX_BH = "", TZ_XXX_CCLX = "", TZ_COM_LMC = "";
		String str_modal_field_value = "";
		String TZ_APP_S_TEXT = "", TZ_KXX_QTZ = "", TZ_KXZ_QT_BZ = "";

		/* TZ_APP_XXXPZ_T表为【报名表模板管理】页面配置的所有信息项（包含非下拉框和下拉框） */
		List<Map<String, Object>> allFieldsList = jdbcTemplate.queryForList(
				"SELECT TZ_XXX_BH,TZ_XXX_CCLX,TZ_XXX_MC,TZ_COM_LMC,TZ_XXX_NO FROM PS_TZ_TEMP_FIELD_V WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_CCLX IN ('S','D','L')",
				new Object[] { str_app_modal_id });
		if (allFieldsList != null && allFieldsList.size() > 0) {
			/* step1:循环所有模版字段，拼装字段和值的数组 */
			for (int i = 0; i < allFieldsList.size(); i++) {
				TZ_XXX_BH = (String) allFieldsList.get(i).get("TZ_XXX_BH");
				TZ_XXX_CCLX = (String) allFieldsList.get(i).get("TZ_XXX_CCLX");
				//TZ_XXX_MC = (String) allFieldsList.get(i).get("TZ_XXX_MC");
				TZ_COM_LMC = (String) allFieldsList.get(i).get("TZ_COM_LMC");
				TZ_XXX_NO = (String) allFieldsList.get(i).get("TZ_XXX_NO");

				str_modal_field_value = "";
				/* 循环存储类型 */
				if ("D".equals(TZ_XXX_CCLX)) {
					if("check".equals(TZ_COM_LMC)){
						/*多选*/
			            String sqlD = "SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ,(SELECT TZ_KXZ_QT_BZ FROM PS_TZ_APPXXX_KXZ_T B WHERE A.TZ_XXX_BH = B.TZ_XXX_BH AND B.TZ_APP_TPL_ID=? limit 0,1) TZ_KXZ_QT_BZ FROM PS_TZ_APP_DHCC_T A WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? AND TZ_IS_CHECKED='Y'";
			            List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sqlD, new Object[]{str_app_modal_id,int_app_ins_id,TZ_XXX_BH});
			            if(list2 != null  && list2.size() > 0){
			            	for(int j = 0; j < list2.size(); j++){
			            		TZ_APP_S_TEXT = (String)list2.get(j).get("TZ_APP_S_TEXT");
			            		TZ_KXX_QTZ = (String)list2.get(j).get("TZ_KXX_QTZ");
			            		TZ_KXZ_QT_BZ = (String)list2.get(j).get("TZ_KXZ_QT_BZ");
			            		
			            		if("Y".equals(TZ_KXZ_QT_BZ)){
			            			str_modal_field_value = TZ_KXX_QTZ;
			            		}else{
			            			str_modal_field_value = TZ_APP_S_TEXT;
			            		}
			            		
			            		/*拼装：属性ID、字段值、报名表是否checkbox显示*/
			            		appins_data.add(TZ_XXX_BH);
			            		appins_data.add(str_modal_field_value);
			            		appins_data.add("Y");
			            	}
			            }
					}else{
						/*单选,Radio*/
						Map<String, Object> radioMap = jdbcTemplate.queryForMap("SELECT TZ_APP_S_TEXT,TZ_KXX_QTZ,(SELECT TZ_KXZ_QT_BZ FROM PS_TZ_APPXXX_KXZ_T B WHERE A.TZ_XXX_BH = B.TZ_XXX_BH AND B.TZ_APP_TPL_ID=? limit 0,1) TZ_KXZ_QT_BZ FROM PS_TZ_APP_DHCC_T A WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? AND TZ_IS_CHECKED='Y' LIMIT 0,1",new Object[]{str_app_modal_id,int_app_ins_id, TZ_XXX_BH});
						if(radioMap != null){
							TZ_APP_S_TEXT = (String)radioMap.get("TZ_APP_S_TEXT");
							TZ_KXX_QTZ = (String)radioMap.get("TZ_KXX_QTZ");
							TZ_KXZ_QT_BZ = (String)radioMap.get("TZ_KXZ_QT_BZ");
							
							if("Y".equals(TZ_KXZ_QT_BZ)){
								str_modal_field_value = TZ_KXX_QTZ;
		            		}else{
		            			str_modal_field_value = TZ_APP_S_TEXT;
		            		}
		            		
		            		/*拼装：属性ID、字段值、报名表是否checkbox显示*/
		            		appins_data.add(TZ_XXX_BH);
		                    appins_data.add(str_modal_field_value);
		                    appins_data.add("NOT");
						}
						
					}
				}
				
				/*长文本*/
				if("L".equals(TZ_XXX_CCLX)){
					str_modal_field_value = jdbcTemplate.queryForObject("SELECT TZ_APP_L_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ? limit 0,1",new Object[]{int_app_ins_id, TZ_XXX_BH},"String");
			        if(str_modal_field_value == null){
			        	str_modal_field_value = "";
			        }
					/*拼装：属性ID、字段值、报名表是否checkbox显示*/
					appins_data.add(TZ_XXX_BH);
			        appins_data.add(str_modal_field_value);
			        appins_data.add("NOT");
				}
				
				/*短文本或下拉框或至今*/
				if("S".equals(TZ_XXX_CCLX)){
					String str_modal_field_ms_value = "";
					Map<String, Object> textMap = jdbcTemplate.queryForMap("SELECT TZ_APP_S_TEXT,TZ_APP_L_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?",new Object[]{int_app_ins_id, TZ_XXX_BH});
					if( textMap != null){
						str_modal_field_value = (String)textMap.get("TZ_APP_S_TEXT");
						str_modal_field_ms_value = (String)textMap.get("TZ_APP_L_TEXT");
						if(str_modal_field_value == null){
							str_modal_field_value = "";
						}
						if(str_modal_field_ms_value == null){
							str_modal_field_ms_value = "";
						}
					}
					
					if("Select".equals(TZ_COM_LMC)
							|| "CompanyNature".equals(TZ_COM_LMC)
							|| "Degree".equals(TZ_COM_LMC)
							|| "Diploma".equals(TZ_COM_LMC)){
						str_modal_field_value = jdbcTemplate.queryForObject("SELECT TZ_XXXKXZ_MS FROM PS_TZ_APPXXX_KXZ_T WHERE TZ_APP_TPL_ID =? AND TZ_XXX_BH = ? AND TZ_XXXKXZ_MC =?", new Object[]{str_app_modal_id, TZ_XXX_NO, str_modal_field_value},"String");
						if(str_modal_field_value == null){
							str_modal_field_value = "";
						}
					}
					
					if(TZ_COM_LMC.length() > 3 && "bmr".equals(TZ_COM_LMC.substring(0, 3))){
						str_modal_field_value = str_modal_field_ms_value;
					}
					
					String comTodate = "com_todate";
					if(TZ_XXX_BH.length() >= comTodate.length()
							&& "com_todate".equals(TZ_XXX_BH.substring(TZ_XXX_BH.length()-comTodate.length(), TZ_XXX_BH.length()))){
						if("Y".equals(str_modal_field_value)){
							str_modal_field_value = "是";
						}else{
							str_modal_field_value = "";
						}
					}
					
					/*拼装：属性ID、字段值、报名表是否checkbox显示*/
		            appins_data.add(TZ_XXX_BH);
		            appins_data.add(str_modal_field_value);
		            appins_data.add("NOT");
					
				}
			}
		}
		return appins_data;
	}
	
	
	 /*参数：&app_ins_id :报名表编号;&OPRID :打印当前行personid;&str_app_modal_id :报名表模板编号;&component_flag:是否在组件中调用;&str_genxmlstring：false返回内容，true返回url地址:非组件调用时将xml内容以字符串形式传出*/
	/*filePath: 生产xml的路径*/ 
	/*功能：获得最后打印的报名表;*/
	 public String replaceXMLPulish(String app_ins_id, String OPRID, String str_app_modal_id, boolean component_flag, String filePath, String[] errMsg){
		 try{
			 SAXReader saxReader = new SAXReader();
			 //读取模板文件地址;
			 Map<String, Object> mbFileMap = jdbcTemplate.queryForMap("SELECT  TZ_ATTACHFILE_NAME,TZ_ATTSYSFILENAME,TZ_ATT_A_URL FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID=?",new Object[]{str_app_modal_id});
			 if(mbFileMap != null){
				 String mbFileName = (String)mbFileMap.get("TZ_ATTACHFILE_NAME");
				 String mbSysFileName = (String)mbFileMap.get("TZ_ATTSYSFILENAME");
				 String mbFileUrl = (String)mbFileMap.get("TZ_ATT_A_URL");
				 if(mbFileName == null || "".equals(mbFileName)
						 || mbSysFileName == null || "".equals(mbSysFileName)
						 || mbFileUrl == null || "".equals(mbFileUrl)){
					 errMsg[0] = "1";
					 errMsg[1] = "报名表打印模板不存在";
					 return "";
				 }
				 
				 String separator = File.separator;
				 String sourceStr = request.getServletContext().getRealPath(mbFileUrl);
				 if((sourceStr.lastIndexOf(separator) + 1) == sourceStr.length()){
					 sourceStr = sourceStr  + mbSysFileName;
			     }else{
			    	 sourceStr = sourceStr  + File.separator + mbSysFileName;
			     }
				 
				 
				 File file = new File(sourceStr);
				 if (!file.exists() || !file.isFile()) {
					 errMsg[0] = "1";
					 errMsg[1] = "报名表打印模板不存在";
					 return "";
				 }
				 
				 Document document = saxReader.read(file);
		    	 
		    	 ArrayList<String> appins_data = this.getData(app_ins_id, OPRID, str_app_modal_id, errMsg);
		
			     // 获取根元素
			     Element root = document.getRootElement();
			     this.replaceElement(root,appins_data);
			     
			     
			     if(component_flag){
			    	 OutputFormat format = OutputFormat.createPrettyPrint();
				     //利用格式化类对编码进行设置
				     format.setEncoding("UTF-8");
				     //得到生成的文件路径；
				     if(filePath == null || "".equals(filePath)){
				    	 errMsg[0] = "1";
						 errMsg[1] = "未定义报名表生成路径";
						 return "";
				     }
				     
				     /*
				     String s_dt = "";
				     String currentOprid = "";
				     
				     if(filePath != null || "".equals(filePath)){
				    	 dirStr = request.getServletContext().getRealPath(mbFileUrl);
					     SimpleDateFormat dateFormate = new SimpleDateFormat("yyyyMMdd");
					     s_dt = dateFormate.format(new Date());
					     currentOprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
					     
					     if((dirStr.lastIndexOf(separator) + 1) == dirStr.length()){
					    	 dirStr = dirStr + s_dt + File.separator + currentOprid;
					     }else{
					    	 dirStr = dirStr + File.separator + s_dt + File.separator + currentOprid;
					     }
				     }else{
				    	 dirStr = request.getServletContext().getRealPath(filePath);
				     }

				     dirStr = request.getServletContext().getRealPath(filePath);
				     File dir = new File(dirStr);
				     
					 if (!dir.exists()) {
							dir.mkdirs();
					 }
					 */
				     String dirStr = request.getServletContext().getRealPath(filePath);
				     if(dirStr.lastIndexOf(separator) + 1 == dirStr.length()){
				    	  
				     }else{
				    	 dirStr = dirStr + separator; 
				     }
				     File dir = new File(dirStr);
				     
					 if (!dir.exists()) {
							dir.mkdirs();
					 }
					 
					 String name = jdbcTemplate.queryForObject("SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL B WHERE B.OPRID = (SELECT OPRID FROM PS_TZ_FORM_WRK_T A WHERE A.TZ_APP_INS_ID = ?)", new Object[]{Long.parseLong(app_ins_id)},"String");
					 if(name == null || "".equals(name)){
						 name = ""; 
					 }else{
						 name = name.replaceAll(" ", "_");
					 }
					 String mbType = jdbcTemplate.queryForObject("select TZ_USE_TYPE from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID=?", new Object[]{Long.parseLong(app_ins_id)},"String");
					 if(mbType == null || "".equals(mbType)){
						 mbType = "BMB"; 
					 }
					 
					 String newFileName = "";
					 if("TJX".equals(mbType)){
						 //如果是推荐信，查看推荐人的姓名;
						 String tzReferrer = jdbcTemplate.queryForObject("select TZ_REFERRER_NAME FROM PS_TZ_KS_TJX_TBL where TZ_TJX_APP_INS_ID=?", new Object[]{Long.parseLong(app_ins_id)},"String");
						 newFileName = name + "_" + tzReferrer + "_推荐信.xml";
					 }else{
						 newFileName = name + "_报名表.xml";
					 }
				     /*
					 SimpleDateFormat datetimeFormate = new SimpleDateFormat("yyyyMMddHHmmss");
				     String s_dtm = datetimeFormate.format(new Date());
					 String newFileName = s_dtm + String.valueOf((int)(10*(Math.random())));
					 */
					 
				     FileOutputStream output = new FileOutputStream(new File(
				    		 dirStr + newFileName));
				     XMLWriter writer = new XMLWriter(output, format);
				     
				     writer.write(document);
				     writer.flush();
				     writer.close();
				     output.flush();
				     output.close();
				     
				     
				     String viewLoadUrl = "";
				     if((filePath.lastIndexOf("/") + 1) == filePath.length()){
				    	 viewLoadUrl = "http://"+ request.getServerName() + ":"+ request.getServerPort() + request.getContextPath() + filePath + newFileName+".xml";
				     }else{
				    	 viewLoadUrl = "http://"+ request.getServerName() + ":"+ request.getServerPort() + request.getContextPath() + filePath + "/" + newFileName+".xml"; 
				     }
				     
				     /*
				     if((mbFileUrl.lastIndexOf("/") + 1) == mbFileUrl.length()){
				    	 viewLoadUrl = "http://"+ request.getServerName() + ":"+ request.getServerPort() + request.getContextPath() + mbFileUrl +  s_dt + "/" + currentOprid +"/" +newFileName+".xml";
				     }else{
				    	 viewLoadUrl = "http://"+ request.getServerName() + ":"+ request.getServerPort() + request.getContextPath() + mbFileUrl + "/" +  s_dt + "/" + currentOprid +"/" + newFileName + ".xml";
				     }
				     */
				     return viewLoadUrl;
			     }else{
			    	 String xmlString = document.asXML();  
				     return xmlString;
			     }
			     
			    
			 }else{
				 errMsg[0] = "1";
				 errMsg[1] = "打印报名表模板不存在";
			 }
	    	 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return "";
	 }
	 
	 
	 public void replaceElement(Element e, ArrayList<String> appins_data){
		 	
		@SuppressWarnings("unchecked")
		List<Element> list = e.elements();
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					Element child = list.get(i);
					this.replaceElement(child,appins_data);
	    		}
			}else{
				String elementName = e.getName();
				if("t".equals(elementName)){
					String nodeValue = e.getTextTrim();
					for(int j = 0; j < appins_data.size(); j = j + 3){
						if(appins_data.get(j).equals(nodeValue)){
							String newValue = appins_data.get(j+1);
							newValue = newValue.replaceAll("\r\n", "\n");
							newValue = newValue.replaceAll("\r", "\n");
							String[] newValueArr = newValue.split("\n");
							if(newValueArr.length > 1){
								for(int k = 0; k < newValueArr.length ; k++){
									if(k == 0){
										e.setText(newValueArr[k]);
									}else{
										//添加回车;
										e.getParent().addElement("w:br");
										Element addt = e.getParent().addElement("w:t");
										addt.setText(newValueArr[k]);
									}
								}
							}else{
								e.setText(newValue);
							}
							
							
						}
					}
				}
			}

	    }
}
