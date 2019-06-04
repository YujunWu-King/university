package com.tranzvision.gd.TZEnrollmentClueBundle.service.impl;


import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsInfoTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.dao.PsTzXsxsZdfpTMapper;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsZdfpT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsZdfpTKey;
import com.tranzvision.gd.util.sql.SqlQuery;

/**
 * 线索自动分配方法
 * @author LuYan 2017-10-17
 *
 */
@Service
public class TzClueAutoAssign  {
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private PsTzXsxsZdfpTMapper psTzXsxsZdfpTMapper;
	@Autowired
	private PsTzXsxsInfoTMapper psTzXsxsInfoTMapper;
	

	
    /**
     * 一般前台调用，如果已关闭自动分配，不执行
     * @param orgId
     * @param oprid
     * @param clueId
     * @param countryId
     * @param localId
     * @param errorMsg
     * @return
     */
	public String autoAssign(String orgId,String oprid,String clueId,String countryId,String localId,String[] errorMsg) {
		String strRet = "";
		
		try {
			
			String  autoFlag = sqlQuery.queryForObject("SELECT TZ_ZDFP_FLG FROM PS_TZ_ZDFP_FLAG_T WHERE TZ_JG_ID=?", new Object[]{orgId},"String");
			
			if("Y".equals(autoFlag)) {
				//开启自动分配
						
				if(clueId!=null && !"".equals(clueId)) {
				
					//如果有国籍，先按国籍来分配
					//外籍/港澳台不考虑常住地，分给对应国籍的负责人
					//中国大陆根据常住地分配
					//没有国籍，按照常住地分配
					
					String distributionLocation = "",distributionLocationDesc = "";
					
					if(countryId!=null && !"".equals(countryId)) {
						if("HK".equals(countryId)) {
							distributionLocation = countryId;
							distributionLocationDesc = "中国香港";
						} 
						if("TW".equals(countryId)) {
							distributionLocation = countryId;
							distributionLocationDesc = "中国台湾";
						} 
						if("MAC".equals(countryId)) {
							distributionLocation = countryId;
							distributionLocationDesc = "中国澳门";
						} 
						if("FORE".equals(countryId)) {
							distributionLocation = countryId;
							distributionLocationDesc = "外籍";
						} 
						if("CHN".equals(countryId)) {
							if(localId!=null && !"".equals(localId)) {
								distributionLocation = localId;
							}
						}
					} else {
						if(localId!=null && !"".equals(localId)) {
							distributionLocation = localId;
						}
					}
					
					String clueState = "", zrrOprid = "";
					String sql = "";
					String lastOprid = "";
					
					if(distributionLocation!=null && !"".equals(distributionLocation)) {
						
						//查询主管地区为分配地区的责任人信息
						sql = "SELECT OPRID FROM PS_TZ_AQ_DQ_T WHERE TZ_TYPE_LABEL='Z' AND TZ_AQDQ_LABEL=? ORDER BY CAST(SUBSTRING(OPRID,4) as signed)";
						List<Map<String, Object>> listZrrTmp = sqlQuery.queryForList(sql, new Object[]{distributionLocation});
						
						List<String> listZrr = new ArrayList<String>();
						for(Map<String, Object> mapZrrTmp : listZrrTmp) {
							String zrrOpridTmp = mapZrrTmp.get("OPRID") == null ? "" : mapZrrTmp.get("OPRID").toString();
							listZrr.add(zrrOpridTmp);
						}
						
						if(listZrr.size()>0) {
							
							//查询自动分配记录表中地区的情况
							sql = "SELECT TZ_LAST_OPRID FROM PS_TZ_XSXS_ZDFP_T WHERE TZ_JG_ID=? AND TZ_LABEL_NAME=?";
							lastOprid = sqlQuery.queryForObject(sql,new Object[]{orgId,distributionLocation},"String");
							
							if(lastOprid!=null && !"".equals(lastOprid)) {
								//如果最后分配的责任人已经是list中的最后一个，取第一个人员为责任人
								if(lastOprid.equals(listZrr.get(listZrr.size()-1))) {
									zrrOprid = listZrr.get(0);
								} else {
									//取最后分配的责任人在list中index
									int lastOpridIndex = listZrr.indexOf(lastOprid);
									if(lastOpridIndex>-1) {
										//取到了，取下一个人员为下一个责任人
										zrrOprid = listZrr.get(lastOpridIndex+1);
									} else {
										//没有取到，则最后分配的责任人已经移除了地区
										String lastOpridNumber = lastOprid.substring(3);
										sql = "SELECT OPRID FROM PS_TZ_AQ_DQ_T WHERE TZ_TYPE_LABEL='Z' AND TZ_AQDQ_LABEL=? AND CAST(SUBSTRING(OPRID,4) as signed) > ? LIMIT 0,1";
										zrrOprid = sqlQuery.queryForObject(sql, new Object[]{distributionLocation,lastOpridNumber},"String");
										//无法获取到责任人时，取第一个[某个地区只有一个责任人，移除后新加一个责任人时，无法获取到责任人 20190428By WRL]
										if(StringUtils.isBlank(zrrOprid)){
											zrrOprid = listZrr.get(0);
										}
									}	
								}	
							} else {
								//自动分配记录表中还没有数据，取第一个人员为责任人
								zrrOprid = listZrr.get(0);
							}
																		
						
							//自动分配记录表更新
							PsTzXsxsZdfpTKey psTzXsxsZdfpTKey = new PsTzXsxsZdfpTKey();
							psTzXsxsZdfpTKey.setTzJgId(orgId);
							psTzXsxsZdfpTKey.setTzLabelName(distributionLocation);
							
							PsTzXsxsZdfpT psTzXsxsZdfpT = psTzXsxsZdfpTMapper.selectByPrimaryKey(psTzXsxsZdfpTKey);
							if(psTzXsxsZdfpT==null) {
								psTzXsxsZdfpT = new PsTzXsxsZdfpT();
								psTzXsxsZdfpT.setTzJgId(orgId);
								psTzXsxsZdfpT.setTzLabelName(distributionLocation);
								psTzXsxsZdfpT.setTzLastOprid(zrrOprid);
								psTzXsxsZdfpT.setRowAddedDttm(new Date());
								psTzXsxsZdfpT.setRowAddedOprid(oprid);
								psTzXsxsZdfpT.setRowLastmantDttm(new Date());
								psTzXsxsZdfpT.setRowLastmantOprid(oprid);
								psTzXsxsZdfpTMapper.insertSelective(psTzXsxsZdfpT);
							} else {
								psTzXsxsZdfpT.setTzLastOprid(zrrOprid);
								psTzXsxsZdfpT.setRowLastmantDttm(new Date());
								psTzXsxsZdfpT.setRowLastmantOprid(oprid);
								psTzXsxsZdfpTMapper.updateByPrimaryKeySelective(psTzXsxsZdfpT);
							}
									
							if(zrrOprid!=null && !"".equals(zrrOprid)) {
								//跟进中
								clueState="C";
							} else {
								clueState="A";
							}
						
						} else {
							//没有对应的责任人，无法分配线索
							clueState="A";
						}
						
					} else {
						//没有常住地，默认未分配
						clueState="A";
					}
					
					PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = psTzXsxsInfoTMapper.selectByPrimaryKey(clueId);
					psTzXsxsInfoT.setTzLeadId(clueId);
					psTzXsxsInfoT.setTzLeadStatus(clueState);
					psTzXsxsInfoT.setTzZrOprid(zrrOprid);
					//如果国籍是外籍港澳台，写入备注。拼接在原有备注后。
					if(distributionLocationDesc!=null && !"".equals(distributionLocationDesc)) {
						String bz = "";
						String bzYuan = sqlQuery.queryForObject("SELECT TZ_BZ FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID=?", new Object[]{clueId},"String");
						if(bzYuan!=null && !"".equals(bzYuan)) {
							bz = bzYuan + "\r\n" + distributionLocationDesc;
						} else {
							bz = distributionLocationDesc;
						}
						psTzXsxsInfoT.setTzBz(bz);
					}
					psTzXsxsInfoT.setRowLastmantDttm(new Date());
					psTzXsxsInfoT.setRowLastmantOprid(oprid);
					psTzXsxsInfoTMapper.updateByPrimaryKey(psTzXsxsInfoT);
			
				} else {
					errorMsg[0] = "1";
					errorMsg[1] = "参数错误！";
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
	
	/**
     * 后台调用，不受自动分配标志影响
     * @param orgId
     * @param oprid
     * @param clueId
     * @param countryId
     * @param localId
     * @param errorMsg
     * @return
     */
	public String autoAssign(String orgId,String oprid,String clueId,String countryId,String localId,String adminFlag,String[] errorMsg) {
		String strRet = "";
		
		try {
				
			if(clueId!=null && !"".equals(clueId)) {
			
				//如果有国籍，先按国籍来分配
				//外籍/港澳台不考虑常住地，分给对应国籍的负责人
				//中国大陆根据常住地分配
				//没有国籍，按照常住地分配
				
				String distributionLocation = "",distributionLocationDesc = "";
				
				if(countryId!=null && !"".equals(countryId)) {
					if("HK".equals(countryId)) {
						distributionLocation = countryId;
						distributionLocationDesc = "中国香港";
					} 
					if("TW".equals(countryId)) {
						distributionLocation = countryId;
						distributionLocationDesc = "中国台湾";
					} 
					if("MAC".equals(countryId)) {
						distributionLocation = countryId;
						distributionLocationDesc = "中国澳门";
					} 
					if("FORE".equals(countryId)) {
						distributionLocation = countryId;
						distributionLocationDesc = "外籍";
					} 
					if("CHN".equals(countryId)) {
						if(localId!=null && !"".equals(localId)) {
							distributionLocation = localId;
						}
					}
				} else {
					if(localId!=null && !"".equals(localId)) {
						distributionLocation = localId;
					}
				}
				
				String clueState = "", zrrOprid = "";
				String sql = "";
				String lastOprid = "";
					
				if(distributionLocation!=null && !"".equals(distributionLocation)) {
					
					//查询主管地区为分配地区的责任人信息
					sql = "SELECT OPRID FROM PS_TZ_AQ_DQ_T WHERE TZ_TYPE_LABEL='Z' AND TZ_AQDQ_LABEL=? ORDER BY CAST(SUBSTRING(OPRID,4) as signed)";
					List<Map<String, Object>> listZrrTmp = sqlQuery.queryForList(sql, new Object[]{distributionLocation});
					
					List<String> listZrr = new ArrayList<String>();
					for(Map<String, Object> mapZrrTmp : listZrrTmp) {
						String zrrOpridTmp = mapZrrTmp.get("OPRID") == null ? "" : mapZrrTmp.get("OPRID").toString();
						listZrr.add(zrrOpridTmp);
					}
					
					if(listZrr.size()>0) {
						
						//查询自动分配记录表中地区的情况
						sql = "SELECT TZ_LAST_OPRID FROM PS_TZ_XSXS_ZDFP_T WHERE TZ_JG_ID=? AND TZ_LABEL_NAME=?";
						lastOprid = sqlQuery.queryForObject(sql,new Object[]{orgId,distributionLocation},"String");
						
						if(lastOprid!=null && !"".equals(lastOprid)) {
							//如果最后分配的责任人已经是list中的最后一个，取第一个人员为责任人
							if(lastOprid.equals(listZrr.get(listZrr.size()-1))) {
								zrrOprid = listZrr.get(0);
							} else {
								//取最后分配的责任人在list中index
								int lastOpridIndex = listZrr.indexOf(lastOprid);
								if(lastOpridIndex>-1) {
									//取到了，取下一个人员为下一个责任人
									zrrOprid = listZrr.get(lastOpridIndex+1);
								} else {
									//没有取到，则最后分配的责任人已经移除了地区
									String lastOpridNumber = lastOprid.substring(3);
									sql = "SELECT OPRID FROM PS_TZ_AQ_DQ_T WHERE TZ_TYPE_LABEL='Z' AND TZ_AQDQ_LABEL=? AND CAST(SUBSTRING(OPRID,4) as signed) > ? LIMIT 0,1";
									zrrOprid = sqlQuery.queryForObject(sql, new Object[]{distributionLocation,lastOpridNumber},"String");
									//无法获取到责任人时，取第一个[某个地区只有一个责任人，移除后新加一个责任人时，无法获取到责任人 20190428By WRL]
									if(StringUtils.isBlank(zrrOprid)){
										zrrOprid = listZrr.get(0);
									}
								}	
							}	
						} else {
							//自动分配记录表中还没有数据，取第一个人员为责任人
							zrrOprid = listZrr.get(0);
						}
																	
					
						//自动分配记录表更新
						PsTzXsxsZdfpTKey psTzXsxsZdfpTKey = new PsTzXsxsZdfpTKey();
						psTzXsxsZdfpTKey.setTzJgId(orgId);
						psTzXsxsZdfpTKey.setTzLabelName(distributionLocation);
						
						PsTzXsxsZdfpT psTzXsxsZdfpT = psTzXsxsZdfpTMapper.selectByPrimaryKey(psTzXsxsZdfpTKey);
						if(psTzXsxsZdfpT==null) {
							psTzXsxsZdfpT = new PsTzXsxsZdfpT();
							psTzXsxsZdfpT.setTzJgId(orgId);
							psTzXsxsZdfpT.setTzLabelName(distributionLocation);
							psTzXsxsZdfpT.setTzLastOprid(zrrOprid);
							psTzXsxsZdfpT.setRowAddedDttm(new Date());
							psTzXsxsZdfpT.setRowAddedOprid(oprid);
							psTzXsxsZdfpT.setRowLastmantDttm(new Date());
							psTzXsxsZdfpT.setRowLastmantOprid(oprid);
							psTzXsxsZdfpTMapper.insertSelective(psTzXsxsZdfpT);
						} else {
							psTzXsxsZdfpT.setTzLastOprid(zrrOprid);
							psTzXsxsZdfpT.setRowLastmantDttm(new Date());
							psTzXsxsZdfpT.setRowLastmantOprid(oprid);
							psTzXsxsZdfpTMapper.updateByPrimaryKeySelective(psTzXsxsZdfpT);
						}
								
						if(zrrOprid!=null && !"".equals(zrrOprid)) {
							//跟进中
							clueState="C";
						} else {
							clueState="A";
						}
					
					} else {
						//没有对应的责任人，无法分配线索
						clueState="A";
					}
					
				} else {
					//没有常住地，默认未分配
					clueState="A";
				}
					
				PsTzXsxsInfoTWithBLOBs psTzXsxsInfoT = psTzXsxsInfoTMapper.selectByPrimaryKey(clueId);
				psTzXsxsInfoT.setTzLeadId(clueId);
				psTzXsxsInfoT.setTzLeadStatus(clueState);
				psTzXsxsInfoT.setTzZrOprid(zrrOprid);
				//如果国籍是外籍港澳台，写入备注。拼接在原有备注后。
				if(distributionLocationDesc!=null && !"".equals(distributionLocationDesc)) {
					String bz = "";
					String bzYuan = sqlQuery.queryForObject("SELECT TZ_BZ FROM PS_TZ_XSXS_INFO_T WHERE TZ_LEAD_ID=?", new Object[]{clueId},"String");
					if(bzYuan!=null && !"".equals(bzYuan)) {
						bz = bzYuan + "\r\n" + distributionLocationDesc;
					} else {
						bz = distributionLocationDesc;
					}
					psTzXsxsInfoT.setTzBz(bz);
				}
				psTzXsxsInfoT.setRowLastmantDttm(new Date());
				psTzXsxsInfoT.setRowLastmantOprid(oprid);
				psTzXsxsInfoTMapper.updateByPrimaryKey(psTzXsxsInfoT);
		
			} else {
				errorMsg[0] = "1";
				errorMsg[1] = "参数错误！";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMsg[0] = "1";
			errorMsg[1] = e.toString();
		}
		
		return strRet;
	}
	
}
