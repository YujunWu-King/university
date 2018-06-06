package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

//import org.apache.jasper.el.JasperELResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 招生线索导入
 * @author LuYan 2017-10-17
 *
 */
@Service("com.tranzvision.gd.TZEnrollmentClueBundle.service.impl.TzClueImportServiceImpl")
public class TzClueImportServiceImpl extends FrameworkImpl {
	
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	@Autowired
	private GetSeqNum getSeqNum;

	
	@Override
	public String tzAdd(String[] strData,String[] errMsg) {
		String strRet = "" ;
		Map<String, Object> mapRet = new HashMap<String,Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前用户编号
			String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			int insertNum = 0,unInsertNum = 0;
			String unFindChargeClue = "";
			String completeSameClue = "";
			String unAssignZrrClue = "";
			String existNameClue = "";
			String existNameMobileClue = "";
			
			String unFindFlag = "";
			
			int num = 0;
			for(num=0;num<strData.length;num++) {
				unFindFlag = "";
				
				String strParams = strData[num];
				jacksonUtil.json2Map(strParams);
				String importFrom = jacksonUtil.getString("importFrom");
				Map<String, Object> mapData = jacksonUtil.getMap("data");
				
				String name = mapData.get("name") == null ? "" : mapData.get("name").toString().trim();
				String mobile = mapData.get("mobile") == null ? "" : mapData.get("mobile").toString().trim();
				String companyName = mapData.get("companyName") == null ? "" : mapData.get("companyName").toString().trim();
				String position = mapData.get("position") == null ? "" : mapData.get("position").toString().trim();
				String localName = mapData.get("localName") == null ? "" : mapData.get("localName").toString().trim();
				String memo = mapData.get("memo") == null ? "" : mapData.get("memo").toString().trim();
				
				//常住地
				String localId = "";
				if(!"".equals(localName)) {
					localId = sqlQuery.queryForObject("SELECT TZ_LABEL_NAME FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_JG_ID=? AND (TZ_LABEL_DESC=? OR TZ_LABEL_NAME=?) AND TZ_LABEL_STATUS='Y'", new Object[]{orgId,localName,localName},"String");
				}
			
				
				/*招生线索管理导入有责任人、创建方式、创建时间*/
				String chargeOprid = "",chargeName = "",createWayId = "",createWay ="",createDttm = "";
				if("ZSXS".equals(importFrom)) {
					chargeOprid = mapData.get("chargeOprid") == null ? "" : mapData.get("chargeOprid").toString();
					chargeName = mapData.get("chargeName") == null ? "" : mapData.get("chargeName").toString();
					createWay = mapData.get("createWay") == null ? "" : mapData.get("createWay").toString();
					createDttm = mapData.get("createDttm") == null ? "" : mapData.get("createDttm").toString().trim();
					
					if(!"".equals(chargeOprid)) {
						
					} else {
						if(!"".equals(chargeName)) {
							chargeOprid = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_XSXS_ZRR_VW WHERE TZ_JG_ID=? AND TZ_REALNAME=?",new Object[]{orgId,chargeName},"String");
							if(chargeOprid!=null && !"".equals(chargeOprid)) {

							} else {
								unFindFlag = "Y";
							}
						}
					}
					
					if(!"".equals(createWay)) {
						createWayId = sqlQuery.queryForObject("SELECT TZ_ZHZ_ID FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_RSFCREATE_WAY' AND (TZ_ZHZ_DMS=? OR TZ_ZHZ_ID=?)", new Object[]{createWay,createWay},"String");
					} 
				}
					
				Date dateCreateDttm = new Date();
				if(createDttm!=null && !"".equals(createDttm)) {
					dateCreateDttm = sdf.parse(createDttm);
				}
				
				//我的招生线索导入
				if("MYXS".equals(importFrom)) {
					//创建方式为自主开发
					createWayId = "I";
					//责任人为当前登录人
					chargeOprid = oprid;
				}
				
				if("Y".equals(unFindFlag)) {
					//输入了责任人，但是找不到OPRID，不能导入，并提示
					if(!"".equals(unFindChargeClue)) {
						unFindChargeClue = unFindChargeClue + "<br>" + "姓名：" + name + "，手机：" + mobile;
					} else {
						unFindChargeClue = "姓名：" + name + "，手机：" + mobile;
					}
					unInsertNum ++;
				} else {
				/*
					//如果姓名、公司、手机完全一样，不插入
					Integer countRepeat = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_REALNAME=? AND TZ_COMP_CNAME=? AND TZ_MOBILE=?", new Object[]{name,companyName,mobile},"Integer");
					if(countRepeat>0) {
						if(!"".equals(completeSameClue)) {
							completeSameClue = completeSameClue + "<br>" + "姓名：" + name + "，手机：" + mobile + "，公司：" + companyName;
						} else {
							completeSameClue = "姓名：" + name + "，手机：" + mobile + "，公司：" + companyName;
						}
						unInsertNum ++;
					} else {
				*/
						insertNum ++;
						
						String existNameMobile = "";
						String existZrrOprid = "";
						Integer existMobile = 0;
						Integer existName = 0;
						//根据姓名手机查询是否存在重复的未关闭的线索
						Map<String,Object> mapNameMobile = sqlQuery.queryForMap("SELECT 'Y' TZ_FLAG,TZ_ZR_OPRID FROM PS_TZ_XSXS_INFO_T WHERE TZ_REALNAME=? AND TZ_MOBILE=? AND TZ_REALNAME<>' ' AND TZ_MOBILE<>' ' AND TZ_LEAD_STATUS<>'G' LIMIT 0,1", new Object[]{name,mobile});	
						if(mapNameMobile!=null) {
							existNameMobile = mapNameMobile.get("TZ_FLAG") == null ? "" : mapNameMobile.get("TZ_FLAG").toString();
							existZrrOprid = mapNameMobile.get("TZ_ZR_OPRID") == null ? "" : mapNameMobile.get("TZ_ZR_OPRID").toString();
						}
						
						if("Y".equals(existNameMobile)) {
							if(!"".equals(existNameMobileClue)) {
								existNameMobileClue = existNameMobileClue + "<br>" + "姓名：" + name + "，手机：" + mobile;
							} else {
								existNameMobileClue = "姓名：" + name + "，手机：" + mobile;
							}
						} else {
	
							//根据手机查询是否存在重复的未关闭的线索，如果存在则不分配责任人，2017-11-28
							if(mobile!=null && !"".equals(mobile)) {
								existMobile = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_MOBILE=? AND TZ_LEAD_STATUS<>'G'", new Object[]{mobile},"Integer");
								if(existMobile>0) {
									if(!"".equals(unAssignZrrClue)) {
										unAssignZrrClue = unAssignZrrClue + "<br>" + "姓名：" + name + "，手机：" + mobile;
									} else {
										unAssignZrrClue = "姓名：" + name + "，手机：" + mobile;
									}
								}
							}
					
							//根据姓名查询是否存在重复的未关闭的线索，进行提示
							existName = sqlQuery.queryForObject("SELECT COUNT(1) FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_STATUS<>'G' AND TZ_REALNAME=?", new Object[]{name},"Integer");
							if(existName>0) {
								if(!"".equals(existNameClue)) {
									existNameClue = existNameClue + "<br>" + "姓名：" + name + "，手机：" + mobile;
								} else {
									existNameClue = "姓名：" + name + "，手机：" + mobile;
								}
							} 
						}
						
						if("MYXS".equals(importFrom)) {
							unAssignZrrClue = "";
						}
					
						//新增线索
						String clueId = String.valueOf(getSeqNum.getSeqNum("TZ_XSXS_INFO_T", "TZ_LEAD_ID"));
						PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = new PsTzXsxsInfoTWithBLOBs();
						psTzXsxsInfoT.setTzLeadId(clueId);
						psTzXsxsInfoT.setTzJgId(orgId);
						psTzXsxsInfoT.setTzRsfcreateWay(createWayId);
						psTzXsxsInfoT.setTzRealname(name);
						psTzXsxsInfoT.setTzCompCname(companyName);
						psTzXsxsInfoT.setTzMobile(mobile);
						psTzXsxsInfoT.setTzPosition(position);
						psTzXsxsInfoT.setTzXsquId(localId);
						psTzXsxsInfoT.setTzBz(memo);
						if("MYXS".equals(importFrom)) {
							//我的招生线索导入，责任人为当前登录人
							psTzXsxsInfoT.setTzLeadStatus("C");
							psTzXsxsInfoT.setTzZrOprid(chargeOprid);
						} else {
							if("ZSXS".equals(importFrom)) {
								//招生线索管理导入
								if("Y".equals(existNameMobile)) {
									//存在姓名手机相同未关闭的线索
									if(!"".equals(chargeOprid)) {
									    //指定了责任人
										psTzXsxsInfoT.setTzLeadStatus("C");
										psTzXsxsInfoT.setTzZrOprid(chargeOprid);
									} else {
										//没有指定责任人
										if(!"".equals(existZrrOprid)) {
											psTzXsxsInfoT.setTzLeadStatus("C");
											psTzXsxsInfoT.setTzZrOprid(existZrrOprid);
											chargeOprid = existZrrOprid;
										} else  {
											psTzXsxsInfoT.setTzLeadStatus("A");
										}
									}
								} else {
									if(existMobile>0) {
										//存在手机相同未关闭的线索，不分配责任人
										psTzXsxsInfoT.setTzLeadStatus("A");
									} else {
										if(existName>0) {
											//存在姓名相同未关闭的线索
											if(!"".equals(chargeOprid)) {
												psTzXsxsInfoT.setTzLeadStatus("C");
												psTzXsxsInfoT.setTzZrOprid(chargeOprid);
											} else {
												//没有责任人，责任人为当前登录人
												psTzXsxsInfoT.setTzLeadStatus("C");
												psTzXsxsInfoT.setTzZrOprid(oprid);
												chargeOprid=oprid;
											}
										} else {
											if(!"".equals(chargeOprid)) {
												psTzXsxsInfoT.setTzLeadStatus("C");
												psTzXsxsInfoT.setTzZrOprid(chargeOprid);
											} else {
												psTzXsxsInfoT.setTzLeadStatus("A");
											}
										}
									}
								}
							}
						}
						
						psTzXsxsInfoT.setRowAddedOprid(oprid);
						psTzXsxsInfoT.setRowAddedDttm(dateCreateDttm);
						psTzXsxsInfoT.setRowLastmantOprid(oprid);
						psTzXsxsInfoT.setRowLastmantDttm(new Date());
						psTzXsxsInfoTMapper.insert(psTzXsxsInfoT);
						
					/*}*/
				}
			}
			
			mapRet.put("insertNum", insertNum);
			mapRet.put("unInsertNum", unInsertNum);
			mapRet.put("unAssignZrrClue", unAssignZrrClue);
			mapRet.put("existNameClue", existNameClue);
			mapRet.put("unFindChargeClue", unFindChargeClue);
			mapRet.put("completeSameClue", completeSameClue);
			mapRet.put("existNameMobileClue", existNameMobileClue);
			
			strRet = jacksonUtil.Map2json(mapRet);
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	public String tzOther(String operateType, String strParams, String[] errMsg) {
		String strRet = "";
		JacksonUtil jacksonUtil = new JacksonUtil();

		try {
			//校验线索信息
			if("tzValidate".equals(operateType)){
				try {
					jacksonUtil.json2Map(strParams);
					
					String importFrom = jacksonUtil.getString("importFrom");
					List<Map<String,Object>> listRecord = (List<Map<String,Object>>) jacksonUtil.getList("validate");
					if(listRecord!=null && listRecord.size()>0) {
						String[] strData = new String[listRecord.size()];
						for(int i=0;i<listRecord.size();i++) {
							strData[i] = jacksonUtil.Map2json(listRecord.get(i));
						}
						strRet = this.validateData(importFrom,strData,errMsg);
					}
				} catch (Exception e) {
					errMsg[0] = "1";
					errMsg[1] = e.toString();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}

		return strRet;
	}
	
	
	/*校验线索信息*/
	public String validateData(String importFrom,String[] strData,String[] errMsg) {
		String strRet = "";
		ArrayList<Map<String, Object>> listData = new ArrayList<Map<String,Object>>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		try {
			
			//当前机构
			String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
			
			int num = 0;
			for(num=0;num<strData.length;num++) {
				String strParams = strData[num];
				
				jacksonUtil.json2Map(strParams);
				Map<String, Object> mapData = jacksonUtil.getMap("data");
				
				String modelId = mapData.get("id") == null ? "" : mapData.get("id").toString();
				String name = mapData.get("name") == null ? "" : mapData.get("name").toString();
				String mobile = mapData.get("mobile") == null ? "" : mapData.get("mobile").toString();
				String companyName = mapData.get("companyName") == null ? "" : mapData.get("companyName").toString();
				String position = mapData.get("position") == null ? "" : mapData.get("position").toString();
				String localName = mapData.get("localName") == null ? "" : mapData.get("localName").toString();
				String memo = mapData.get("memo") == null ? "" : mapData.get("memo").toString();
				String createWay = mapData.get("createWay") == null ? "" : mapData.get("createWay").toString();
				String createDttm = mapData.get("createDttm") == null ? "" : mapData.get("createDttm").toString();
				
				//姓名必填
				Boolean nameRequired = false;
				if(name!=null && !"".equals(name)) {
					nameRequired = true;
				}
				
				//手机必填
				/*
				Boolean mobileRequired = false;
				if(mobile!=null && !"".equals(mobile)) {
					mobileRequired = true;
				}
				*/
				
				//验证手机有效性
				/*
				Boolean isMobile = (mobile==null || "".equals(mobile)) ? true : this.isMobile(mobile);
				*/
			
				
				//常住地有效性
				//Boolean localRequire = false;
				Boolean localValidate = true;
				if(localName!=null && !"".equals(localName)) {
					//localRequire = true;
					//可能为描述信息或者值
					String localId = sqlQuery.queryForObject("SELECT TZ_LABEL_NAME FROM PS_TZ_XSXS_DQBQ_T WHERE TZ_JG_ID=? AND (TZ_LABEL_DESC=? OR TZ_LABEL_NAME=?) AND TZ_LABEL_STATUS='Y'", new Object[]{orgId,localName,localName},"String");
					if(localId!=null && !"".equals(localId)) {
						localValidate = true;
					} else {
						localValidate = false;
					}
				} 
				
				//创建方式必填、有效性
				Boolean createWayRequire = false;
				Boolean createWayValidate = true;
				
				//创建时间必填、有效性
				Boolean createDttmRequire = false;
				Boolean createDttmValidate = true;
				
				if("ZSXS".equals(importFrom)) {
					//招生线索才导入创建方式
					if(createWay!=null && !"".equals(createWay)) {
						createWayRequire = true;
						//可能为描述信息或者值
						String createWayId = sqlQuery.queryForObject("SELECT TZ_ZHZ_ID FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID='TZ_RSFCREATE_WAY' AND (TZ_ZHZ_DMS=? OR TZ_ZHZ_ID=?)", new Object[]{createWay,createWay},"String");
						if(createWayId!=null && !"".equals(createWayId)) {
							createWayValidate = true;
						} else {
							createWayValidate = false;
						}
					} 
					
					//招生线索导入创建时间必填
					if(createDttm!=null && !"".equals(createDttm)) {
						createDttmRequire = true;
						if("invalid".equals(createDttm)) {
							createDttmValidate=false;
						} else {
							createDttmValidate=this.isValidDate(createDttm);
						}
					}
					
				} else {
					createWayRequire = true;
					createWayValidate = true;
					createDttmRequire = true;
					createDttmValidate = true;
				}
				
				Boolean validationResult = nameRequired  && localValidate && createWayRequire && createWayValidate && createDttmRequire && createDttmValidate ;
				
				ArrayList<String> validationMsgList = new ArrayList<String>();
				
				if(!nameRequired) {
					validationMsgList.add("姓名必填");
				}
				/*
				if(!mobileRequired) {
					validationMsgList.add("手机必填");
				}
				if(!isMobile) {
					validationMsgList.add("手机格式不正确");
				}
				if(!localRequire) {
					validationMsgList.add("常住地必填");
				}
				*/
				if(!localValidate) {
					validationMsgList.add("常住地找不到");
				}
				if(!createWayRequire) {
					validationMsgList.add("线索来源必填");
				}
				if(!createWayValidate) {
					validationMsgList.add("线索来源不正确");
				}
				if(!createDttmRequire) {
					validationMsgList.add("创建时间必填");
				}
				if(!createDttmValidate) {
					validationMsgList.add("创建时间格式不正确");
				}

				
				Map<String, Object> mapList = new HashMap<String,Object>();
				mapList.put("id", modelId);
				mapList.put("validationResult", validationResult);
				mapList.put("validationMsg", validationMsgList);
				listData.add(mapList);
			}
			
			strRet = jacksonUtil.List2json(listData);
				
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/*验证手机有效性*/
	public boolean isMobile(String mobile) {
		boolean flag = false;
		
		try {
			// 13*********,14*********,15*********,17*********,18*********  
            Pattern p = Pattern.compile("^(((13[0-9])|(14[5,7])|(15([0-3]|[5-9]))|(17[0,6,7,8])|(18[0-9]))\\d{8})$");  
  
            Matcher m = p.matcher(mobile);  
            flag = m.matches(); 
			
		} catch (Exception e) {
			flag = false;
		}
		
		return flag;
	}
	
	
	/*验证创建日期格式*/
	public boolean isValidDate(String strDate) {
		boolean bool = true;
		
		try {
			String format = "((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) "
					+ "([01]?[0-9]|2[0-3]):[0-5][0-9]";
			Pattern pattern = Pattern.compile(format);
			Matcher matcher = pattern.matcher(strDate);
			
			if (matcher.matches()) {
				pattern = Pattern.compile("(\\d{4})-(\\d+)-(\\d+).*");
				matcher = pattern.matcher(strDate);
				if (matcher.matches()) {
					int y = Integer.valueOf(matcher.group(1));
					int m = Integer.valueOf(matcher.group(2));
					int d = Integer.valueOf(matcher.group(3));
					if (d > 28) {
						Calendar c = Calendar.getInstance();
						c.set(y, m-1, 1);
						int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
						bool = (lastDay >= d);
					}
				}
				bool=true;
			} else {
				bool=false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bool;
	}
}
