package com.tranzvision.gd.TZScoreModeManagementBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZScoreModeManagementBundle.dao.PsTzModalDtTblMapper;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblKey;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzModalDtTblWithBLOBs;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;


@Service("com.tranzvision.gd.TZScoreModeManagementBundle.service.impl.TzScoreItemsInfoServiceImpl")
public class TzScoreItemsInfoServiceImpl extends FrameworkImpl{
	@Autowired
	private SqlQuery jdbcTemplate;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	
	@Autowired
	private TZGDObject tzSQLObject;
	
	@Autowired
	private PsTzModalDtTblMapper psTzModalDtTblMapper;
	
	@Override
	public String tzQuery(String strParams, String[] errMsg) {
		// 返回值;
		String strRtn = "{}";
		Map<String, Object> mapRet = new HashMap<String, Object>();
		JacksonUtil jacksonUtil = new JacksonUtil();
		
		String orgId = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		try {
			jacksonUtil.json2Map(strParams);

			String treeName = jacksonUtil.getString("treeName");
			String OpeItemId = jacksonUtil.getString("OpeItemId");
			
			
			PsTzModalDtTblKey psTzModalDtTblKey = new PsTzModalDtTblKey();
			psTzModalDtTblKey.setTzJgId(orgId);
			psTzModalDtTblKey.setTreeName(treeName);
			psTzModalDtTblKey.setTzScoreItemId(OpeItemId);
			PsTzModalDtTblWithBLOBs psTzModalDtTbl = psTzModalDtTblMapper.selectByPrimaryKey(psTzModalDtTblKey);
			if(psTzModalDtTbl != null){
				
				String itemName = psTzModalDtTbl.getDescr() == null ? "" : psTzModalDtTbl.getDescr();
				String itemType = psTzModalDtTbl.getTzScoreItemType() == null ? "" : psTzModalDtTbl.getTzScoreItemType();
				BigDecimal UpHzXs = psTzModalDtTbl.getTzScoreHz() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreHz();
				BigDecimal weightA = psTzModalDtTbl.getTzScoreQz() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreQz();
				
				String lowerOperator = psTzModalDtTbl.getTzMFbdzMxXxJx() == null ? "" : psTzModalDtTbl.getTzMFbdzMxXxJx();
				BigDecimal lowerLimit = psTzModalDtTbl.getTzScoreLimited2() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreLimited2();
				String upperOperator = psTzModalDtTbl.getTzMFbdzMxSxJx() == null ? "" : psTzModalDtTbl.getTzMFbdzMxSxJx();
				BigDecimal upperLimit = psTzModalDtTbl.getTzScoreLimited() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScoreLimited();
				
				Integer wordLowerLimit = psTzModalDtTbl.getTzScorePyZslim0() == null ? 0 : psTzModalDtTbl.getTzScorePyZslim0();
				Integer wordUpperLimit = psTzModalDtTbl.getTzScorePyZslim() == null ? 0 : psTzModalDtTbl.getTzScorePyZslim();
				String xlTransScore = psTzModalDtTbl.getTzScrToScore() == null ? "" : psTzModalDtTbl.getTzScrToScore();
				BigDecimal weightD = psTzModalDtTbl.getTzScrSqz() == null ? new BigDecimal(0) : psTzModalDtTbl.getTzScrSqz();
				
				String refDataSet = psTzModalDtTbl.getTzScoreCkzl() == null ? "" : psTzModalDtTbl.getTzScoreCkzl();
				String standard = psTzModalDtTbl.getTzScoreItemCkwt() == null ? "" : psTzModalDtTbl.getTzScoreItemCkwt();
				String descr = psTzModalDtTbl.getTzScoreItemDfsm() == null ? "" : psTzModalDtTbl.getTzScoreItemDfsm();
				String interviewMethod = psTzModalDtTbl.getTzScoreItemMsff() == null ? "" : psTzModalDtTbl.getTzScoreItemMsff();
				

				mapRet.put("orgId", orgId);
				mapRet.put("treeName", treeName);
				mapRet.put("itemId", OpeItemId);
				mapRet.put("itemName", itemName);
				mapRet.put("itemType", itemType);
				
				mapRet.put("UpHzXs", UpHzXs);
				mapRet.put("weightA", weightA);
				mapRet.put("lowerOperator", lowerOperator);
				mapRet.put("lowerLimit", lowerLimit);
				mapRet.put("upperOperator", upperOperator);
				mapRet.put("upperLimit", upperLimit);
				
				mapRet.put("wordLowerLimit", wordLowerLimit);
				mapRet.put("wordUpperLimit", wordUpperLimit);
				
				mapRet.put("xlTransScore", xlTransScore);
				mapRet.put("weightD", weightD);
				
				mapRet.put("refDataSet", refDataSet);
				mapRet.put("standard", standard);
				mapRet.put("descr", descr);
				mapRet.put("interviewMethod", interviewMethod);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		
		strRtn = jacksonUtil.Map2json(mapRet);
		return strRtn;
	}
}
