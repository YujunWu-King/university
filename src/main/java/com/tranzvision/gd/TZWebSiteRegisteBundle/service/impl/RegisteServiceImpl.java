package com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.dao.PsTzSiteiDefnTMapper;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;
import com.tranzvision.gd.TZWebSiteUtilBundle.service.impl.SiteRepCssServiceImpl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.cfgdata.GetSysHardCodeVal;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 
 * @author tang
 * 发布注册页静态化相关页面
 *
 */
@Service("com.tranzvision.gd.TZWebSiteRegisteBundle.service.impl.RegisteServiceImpl")
public class RegisteServiceImpl {
	@Autowired
	private TZGDObject tzGdObject;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private SiteRepCssServiceImpl objRep;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private GetSysHardCodeVal getSysHardCodeVal;
	@Autowired
	private PsTzSiteiDefnTMapper psTzSiteiDefnTMapper;
	
	//生成用户注册页源代码，PS类：TZ_GD_USERMG_PKG:TZ_GD_USER_REG;
	public String userRegister(String strJgid, String strSiteId){
		String fields = "";
		try {
			String strLangSQL = "SELECT TZ_SITE_LANG FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=? AND TZ_SITEI_ENABLE='Y'";
			String strLang = jdbcTemplate.queryForObject(strLangSQL,new Object[]{strSiteId},"String");
			
			//获取要显示的字段;
			String sql = "SELECT TZ_REG_FIELD_ID,TZ_RED_FLD_YSMC,TZ_REG_FIELD_NAME,(SELECT TZ_REG_FIELD_NAME FROM PS_TZ_REGFIELD_ENG WHERE TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND LANGUAGE_CD=?) TZ_REG_FIELD_ENG_NAME,TZ_IS_REQUIRED,TZ_SYSFIELD_FLAG,TZ_FIELD_TYPE,TZ_DEF_VAL FROM PS_TZ_REG_FIELD_T PT WHERE TZ_ENABLE='Y' AND TZ_JG_ID=? ORDER BY TZ_ORDER ASC";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql,new Object[]{strLang,strJgid} );
			if(list!= null && list.size()>0){
				for(int i=0; i < list.size(); i++){
					Map<String, Object> map = list.get(i);
		
					String combox = "<option value =\"\">请选择</option>";
					String img = "";
					//名称是否修改;
					String regFldYsmc = (String)map.get("TZ_RED_FLD_YSMC"); 
				    String regFieldName = (String)map.get("TZ_REG_FIELD_NAME");
				    String regFieldEngName = (String)map.get("TZ_REG_FIELD_ENG_NAME");
				    if("ENG".equals(strLang)){
				    	if(regFieldEngName != null && !"".equals(regFieldEngName)){
					    	regFldYsmc = regFieldEngName;
					    }
				    }else{
				    	if(regFieldName != null && !"".equals(regFieldName)){
					    	regFldYsmc = regFieldName;
					    }
				    }
				    //是否必填;
				    String isRequired = (String)map.get("TZ_IS_REQUIRED"); 
				    if("Y".equals(isRequired)){
				    	isRequired = "*";
				    }else{
				    	isRequired = "";
				    }
				    
				    String regFieldId = (String)map.get("TZ_REG_FIELD_ID");
				    String regDefValue = (String)map.get("TZ_DEF_VAL");
				    if("TZ_PASSWORD".equals(regFieldId)){
				    	continue;
				    }
				    
				    ArrayList<String> fieldsArr = new ArrayList<>();
				    fieldsArr.add("TZ_REPASSWORD");
				    fieldsArr.add("TZ_GENDER");
				    fieldsArr.add("BIRTHDATE");
				    fieldsArr.add("TZ_COUNTRY");
				    fieldsArr.add("TZ_SCH_CNAME");
				    fieldsArr.add("TZ_LEN_PROID");
				    fieldsArr.add("TZ_LEN_CITY");
				    if(fieldsArr.contains(regFieldId)){
				    	//密码
				    	if("TZ_REPASSWORD".equals(regFieldId)){
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_PASSWORD_HTML", true, "*", regFldYsmc, regFieldId,regDefValue );
				    	}
				    	
				    	//性别;
				    	if("TZ_GENDER".equals(regFieldId)){
				    		String str1 = "";
				    		String str2 = "";
				    		if("M".equals(regDefValue)){
				    			str1 = "checked=\"checked\"";
				    		}
				    		
				    		if("F".equals(regDefValue)){
				    			str2 = "checked=\"checked\"";
				    		}
				    		
				    		if("ENG".equals(strLang)){
				    			fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_EN_HTML", true, isRequired, regFldYsmc, regFieldId,str1,str2 );
				    		}else{
				    			fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_SEX_HTML", true, isRequired, regFldYsmc, regFieldId,str1,str2 );
				    		}
				    	}
				    	
				    	//BIRTHDATE;
				    	if("BIRTHDATE".equals(regFieldId)){
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML", true, isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", "", regDefValue);
				    	}
				    	
				    	//TZ_COUNTRY;
				    	if("TZ_COUNTRY".equals(regFieldId)){
				    		img = "<img src=\"/tranzvision/images/chazhao.png\" class=\"serch-ico\" id=\"TZ_COUNTRY_click\"/>";
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML", true, isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue);
				    	}
				    	
				    	//TZ_SCH_CNAME;
				    	if("TZ_SCH_CNAME".equals(regFieldId)){
				    		img = "<img src=\"/tranzvision/images/chazhao.png\" class=\"serch-ico\" id=\"TZ_SCH_CNAME_click\"/>";
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML", true, isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue);
				    	}
				    	
				    	//TZ_LEN_PROID;
				    	if("TZ_LEN_PROID".equals(regFieldId)){
				    		img = "<img src=\"/tranzvision/images/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_PROID_click\"/>";
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML", true, isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue);
				    	}
				    	
				    	//TZ_LEN_CITY;
				    	if("TZ_LEN_CITY".equals(regFieldId)){
				    		img = "<img src=\"/tranzvision/images/chazhao.png\" class=\"serch-ico\" id=\"TZ_LEN_CITY_click\"/>";
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML", true, isRequired, regFldYsmc, regFieldId, "readonly=\"true\"", img, regDefValue);
				    	}
				    }else{
				    	//是否下拉框;
				    	String fieldType = (String)map.get("TZ_FIELD_TYPE");
				    	if("DROP".equals(fieldType)){
				    		String dropSQL = "SELECT TZ_OPT_ID,TZ_OPT_VALUE,(SELECT TZ_OPT_VALUE FROM PS_TZ_YHZC_XXZ_ENG WHERE TZ_JG_ID=PT.TZ_JG_ID AND TZ_REG_FIELD_ID=PT.TZ_REG_FIELD_ID AND TZ_OPT_ID=PT.TZ_OPT_ID AND LANGUAGE_CD=? ) TZ_OPT_EN_VALUE ,TZ_SELECT_FLG FROM PS_TZ_YHZC_XXZ_TBL PT WHERE TZ_JG_ID=? AND TZ_REG_FIELD_ID=? ORDER BY TZ_ORDER ASC";
				    		List<Map<String, Object>> dropList = jdbcTemplate.queryForList(dropSQL,new Object[]{strLang,strJgid,regFieldId});
				    		
				    		for(int j = 0; j<dropList.size(); j++ ){
				    			String optId = (String)dropList.get(j).get("TZ_OPT_ID");
				    			String optValue = (String)dropList.get(j).get("TZ_OPT_VALUE");
				    			String optEngValue = (String)dropList.get(j).get("TZ_OPT_EN_VALUE");
				    			if(optEngValue == null || "".equals(optEngValue)){
				    				optEngValue = optValue;
				    			}
				    			String selectFlg = (String)dropList.get(j).get("TZ_SELECT_FLG");
				    			if("ENG".equals(strLang)){
				    				if("Y".equals(selectFlg) || "1".equals(selectFlg)){
				    					combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">" + optEngValue + "</option>";
				    				}else{
				    					combox = combox + "<option value =\"" + optId + "\">" + optEngValue + "</option>";
				    				}
				    			}else{
				    				if("Y".equals(selectFlg) || "1".equals(selectFlg)){
				    					combox = combox + "<option value =\"" + optId + "\" selected=\"selected\">" + optValue + "</option>";
				    				}else{
				    					combox = combox + "<option value =\"" + optId + "\">" + optValue + "</option>";
				    				}
				    			}
				    		}
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_COMBOXR_HTML", true, isRequired, regFldYsmc, regFieldId,combox );
				    	}else{
				    		fields = fields + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_FIELD_HTML", true, isRequired, regFldYsmc, regFieldId, "", "", regDefValue);
				    	}
				    }
				}
				
			}
			
		
			String strActHtml = "";
			String regMbSQL = "SELECT TZ_ACTIVATE_TYPE FROM PS_TZ_USERREG_MB_T WHERE TZ_JG_ID=?";
			String strActType = jdbcTemplate.queryForObject(regMbSQL, new Object[]{strJgid},"String");
			if(strActType != null && !"".equals(strActType)){
				if(strActType.indexOf("MOBILE")>=0 && strActType.indexOf("EMAIL")>=0){
					if("ENG".equals(strLang)){
						 strActHtml = "<select name='yzfs' id='yzfs'  class='chosen-select combox_351px'><option value ='E'>Email</option><option value ='M'>Phone</option></select>";
				         strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_JHFS_ENG_HTML", true, strActHtml);
					}else{
						 strActHtml = "<select name='yzfs' id='yzfs'  class='chosen-select combox_351px'><option value ='E'>邮箱验证</option><option value ='M'>手机验证</option></select>";
				         strActHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_JHFS_ZHS_HTML", true, strActHtml);
					}
				}
			}else{
				if(strActType.indexOf("EMAIL")>=0){
					if("ENG".equals(strLang)){
						strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='Email' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
					}else{
						strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='邮箱验证' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
					}
				}else{
					if(strActType.indexOf("MOBILE")>=0){
						if("ENG".equals(strLang)){
							strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='Phone' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
						}else{
							strActHtml = "<input name='yzfs1' type='hidden' class='input_351px' id='yzfs1' value='手机验证' readonly='readonly'><input name='yzfs' type='hidden' class='input_351px' id='yzfs' value='E'>";
						}
					}
				}
			}
			
			if("ENG".equals(strLang)){
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_REG_EN_HTML", true, fields, strJgid, strActHtml);
			}else{
				fields = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_GD_REG_HTML", true, fields, strJgid, strActHtml);
			}
			
			//fields = tzGdObject.getHTMLText("HTML.test.test", true, "test111","test222");
		} catch (TzSystemException e) {
			e.printStackTrace();
			fields = "";
		}

		return fields;
	}
	
	
	//处理要发布的注册页内容,PS类：TZ_SITE_DECORATED_APP:TZ_SITE_MG_CLS;
	public String handleEnrollPage(String strSiteId){
		String strContent = "";
		try{
			String sql = "SELECT TZ_JG_ID FROM PS_TZ_SITEI_DEFN_T WHERE TZ_SITEI_ID=?";
			String strOrgId = jdbcTemplate.queryForObject(sql, new Object[]{strSiteId},"String");
			strContent = this.userRegister(strOrgId, strSiteId);
			strContent = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_SETREGISTEPAGE_HTML",true, strContent);
			//截取body内容
			int numCharstart = 0;
		    int numCharend = 0;
		    numCharstart = strContent.indexOf("<body");
		    numCharend = strContent.indexOf("</body>",numCharstart);
		    if(numCharstart >= 0 && numCharend > numCharstart){
		    	strContent = strContent.substring(numCharstart, numCharend + 7);
		    }else{
		    	strContent = "";
		    }
		}catch(Exception e){
			e.printStackTrace();
			strContent = "";
		}
		return strContent;
	}
	
	public boolean saveEnrollpage(String strReleasContent, String strSiteId, String[] errMsg){
		try{
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if(psTzSiteiDefnT != null){
				String jgid = psTzSiteiDefnT.getTzJgId();
		        String siteLang = psTzSiteiDefnT.getTzSiteLang();
		        
				String strReleasContent1 = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML",true)
						+ "<html>" + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_HEAD_HTML",true, 
								tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PANDUAN_LIULANQI_HTML",true)) 
						+ strReleasContent +  "</html>";
				
				strReleasContent1 = objRep.repWelcome(strReleasContent1, "");
		        strReleasContent1 = objRep.repSdkbar(strReleasContent1, "");
		        strReleasContent1 = objRep.repSiteid(strReleasContent1, strSiteId);
		        
		        if(jgid != null && !"".equals(jgid)){
		        	strReleasContent1 = objRep.repJgid(strReleasContent1, jgid.toUpperCase());
		        }else{
		        	strReleasContent1 = objRep.repJgid(strReleasContent1, "");
		        }
		        
		        if(siteLang != null && !"".equals(siteLang)){
		        	strReleasContent1 = objRep.repLang(strReleasContent1, siteLang.toUpperCase());
		        }else{
		        	strReleasContent1 = objRep.repLang(strReleasContent1, "");
		        }
		        
		        String strReleasContent2 = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML",true) + "<html>" 
		        + tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_SAVE_HEAD_HTML",true) + strReleasContent + "</html>";
		        
		        strReleasContent2 = objRep.repWelcome(strReleasContent2, "");
		        strReleasContent2 = objRep.repSiteid(strReleasContent2, strSiteId);
		        if(jgid != null && !"".equals(jgid)){
		        	strReleasContent2 = objRep.repJgid(strReleasContent2, jgid.toUpperCase());
		        }else{
		        	strReleasContent2 = objRep.repJgid(strReleasContent2, "");
		        }
		        
		        if(siteLang != null && !"".equals(siteLang)){
		        	strReleasContent2 = objRep.repLang(strReleasContent2, siteLang.toUpperCase());
		        }else{
		        	strReleasContent2 = objRep.repLang(strReleasContent2, "");
		        }
	        
		        psTzSiteiDefnT.setTzEnrollPrecode(strReleasContent1);
		        psTzSiteiDefnT.setTzEnrollSavecode(strReleasContent2);
		        int success = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
		        if(success > 0){
		        	return true;  
		        }else{
		        	errMsg[0] = "1";
					errMsg[1] = "更新失败！";
		        	return false;  
		        }
			}else{
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！";
				return false;  
			}
			
		}catch(Exception e){
			errMsg[0] = "2";
			errMsg[1] = "站点注册页保存异常！";
			return false; 
		}
	}
	
	public boolean releasEnrollpage(String strReleasContent, String strSiteId,String[] errMsg){
		try{
			PsTzSiteiDefnTWithBLOBs psTzSiteiDefnT = psTzSiteiDefnTMapper.selectByPrimaryKey(strSiteId);
			if(psTzSiteiDefnT != null){
				String jgid = psTzSiteiDefnT.getTzJgId();
		        String siteLang = psTzSiteiDefnT.getTzSiteLang();
		        String doctypeHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_DOCTYPE_HTML",true);
		        String explorerHtml = tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_PANDUAN_LIULANQI_HTML",true);
		        explorerHtml = explorerHtml.replaceAll("\\$",  "\\\\\\$");
		        
		        strReleasContent = doctypeHtml
		        	+ "<html>" +tzGdObject.getHTMLText("HTML.TZWebSiteRegisteBundle.TZ_REALEAS_HEAD_HTML",
		        			true,explorerHtml) 
		        	+ strReleasContent + "</html>";
		        strReleasContent = objRep.repTitle(strReleasContent, strSiteId);
		        strReleasContent = objRep.repCss(strReleasContent, strSiteId);
		        strReleasContent = objRep.repWelcome(strReleasContent, "");
		        strReleasContent = objRep.repSdkbar(strReleasContent, "");
		        strReleasContent = objRep.repSiteid(strReleasContent, strSiteId);
		        if(jgid != null && !"".equals(jgid)){
		        	strReleasContent = objRep.repJgid(strReleasContent, jgid.toUpperCase());
		        }else{
		        	strReleasContent = objRep.repJgid(strReleasContent, "");
		        }
		        
		        if(siteLang != null && !"".equals(siteLang)){
		        	strReleasContent = objRep.repLang(strReleasContent, siteLang.toUpperCase());
		        }else{
		        	strReleasContent = objRep.repLang(strReleasContent, "");
		        }
		        psTzSiteiDefnT.setTzEnrollPrecode(strReleasContent);
		        psTzSiteiDefnT.setTzEnrollPubcode(strReleasContent);
		        int success = psTzSiteiDefnTMapper.updateByPrimaryKeySelective(psTzSiteiDefnT);
		        if(success > 0){
		        	String dir = getSysHardCodeVal.getWebsiteEnrollPath();
		        	dir = request.getServletContext().getRealPath(dir);
		        	if(jgid != null && !"".equals(jgid)){
		        		dir = dir + File.separator + jgid.toLowerCase();
					}
		        	boolean bl = this.staticFile(strReleasContent, dir, "enroll.html", errMsg);
		        	return bl;  
		        }else{
		        	errMsg[0] = "1";
					errMsg[1] = "更新失败！";
		        	return false;  
		        }
			}else{
				errMsg[0] = "1";
				errMsg[1] = "站点不存在！！";
	        	return false;  
			}
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "2";
			errMsg[1] = "站点注册页发布异常！";
			return false; 
		}
	}
	
	public boolean staticFile(String strReleasContent, String dir, String fileName, String[] errMsg){
		try{
			System.out.println(dir);
			File fileDir = new File(dir);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			
			String filePath = "";
			if((dir.lastIndexOf(File.separator)+1) != dir.length()){
				filePath = dir + File.separator + fileName;
			}else{
				filePath = dir + fileName;
			}
			
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(strReleasContent);
			bw.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			errMsg[0] = "3";
			errMsg[1] = "静态化文件时异常！";
			return false; 
		}
	}
	
}