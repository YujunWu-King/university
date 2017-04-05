package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tranzvision.gd.TZAudMgBundle.dao.PsTzAudDefnTMapper;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudDefnT;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZUnifiedBaseBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

public class TzZddfGJBJServiceImpl {
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	//获取参数：成绩单ID、国际化背景成绩项ID、报名表ID
	public float getGJHScore(String TZ_SCORE_INS_ID,String TZ_SCORE_ITEM_ID,String[] paramters) {
		try {
			//声明float型字段“得分”，string型字段“打分记录”
			float Score;
			String MarkRecord = null;
			
			//根据报名表ID查询考生境外工作经历，国家类别和时间，根据查询结果
			String GJLB = paramters[0];	//国家类别
			String SJ = paramters[1];	//时间
		
			float SJF= Float.parseFloat(SJ);

			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			
			if("A".equals(GJLB)){
			String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='A'";
			String FSCJ = jdbcTemplate.queryForObject(sql, String.class, new Object[] {SJF,SJF});
			Score=Float.parseFloat(FSCJ);
			}else{
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='B'";
				String FSCJ = jdbcTemplate.queryForObject(sql2, String.class, new Object[] {SJF,SJF});
				Score=Float.parseFloat(FSCJ);
			}
			
			MarkRecord="国家类型：".concat(GJLB).concat("|时间：").concat(SJ);//.concat("|学位：").concat(XW);
			MarkRecord=MarkRecord+("|")+String.valueOf(Score).concat("分");
			
				//插入表TZ_CJX_TBL
				PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs=new PsTzCjxTblWithBLOBs();
					//成绩单ID
					Long tzScoreInsId=Long.parseLong(TZ_SCORE_INS_ID);
					psTzCjxTblWithBLOBs.setTzScoreInsId(tzScoreInsId);
					//成绩项ID
					psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM_ID);
					//分值
					BigDecimal BigDeScore = new BigDecimal(Float.toString(Score));
					psTzCjxTblWithBLOBs.setTzScoreNum(BigDeScore);
					//打分记录
					psTzCjxTblWithBLOBs.setTzScoreDfgc(MarkRecord);
				
				psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
				
				return Score;
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
