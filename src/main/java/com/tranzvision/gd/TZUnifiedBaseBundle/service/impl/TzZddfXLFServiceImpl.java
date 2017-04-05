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


public class TzZddfXLFServiceImpl {

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	@Autowired
	private SqlQuery jdbcTemplate;
	@Autowired
	private FliterForm fliterForm;
	@Autowired
	private GetSeqNum getSeqNum;
	
	// 获取参数：成绩单ID、学历分成绩项ID、报名表ID
		public float getXlfScore(String TZ_SCORE_INS_ID,String TZ_SCORE_ITEM_ID,String[] paramters) {
			try {
				//根据报名表ID查询考生本科学历的学历、学位、学校ID
				String XL = paramters[0];
				String XW = paramters[1];
				String XX=paramters[2];
				int XXID = Integer.parseInt(XX);
				 
				//声明    学历=研究生，学位等于硕士/博士
				String XLYJS = paramters[3];
				String XWYJS = paramters[4];
				int XLYJSID = Integer.parseInt(XLYJS);
				int XWYJSID = Integer.parseInt(XWYJS);
				
				//根据考生学校ID查询所属学校类型
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_AUD_XM from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
				String XXLX = jdbcTemplate.queryForObject(sql, String.class, new Object[] { XXID });
				
				//声明float型字段“得分”，string型字段“打分记录”；
				float Score;
				String MarkRecord;
				
				//根据考生查询到的学历、学位、学校类型在TZ_CSMB_XLF_T查询对应的得分，如果没有查询到对应得分，得分=0
				String ExistSql = "select 'Y' from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
				String isExist = "";
				isExist = jdbcTemplate.queryForObject(ExistSql, String.class, new Object[] { XXLX,XW,XL });
				if ("Y".equals(isExist)) {
					String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
					String StrScore = jdbcTemplate.queryForObject(SearchSql, String.class, new Object[] { XXLX,XW,XL });
					Score=Float.parseFloat(StrScore);
					
					//查询考生其他教育经历中，是否有学历=研究生，学位等于硕士/博士的教育经历，如果有，得分=得分+5
					if(XWYJS.equals("1")&&XLYJS.equals("1")){
						Score+=5;
					}
					
					//得分如果>100，得分=100；
					if(Score>100){
						Score=100;
					}
					
					//记录打分记录：示例：985|学历：本科|学位：学士|研究生|95分
					MarkRecord="学校类型：".concat(XXLX).concat("|学历：").concat(XL).concat("|学位：").concat(XW);
						//是否拼接研究生；
						if(XWYJS.equals("1")&&XLYJS.equals("1")){
							MarkRecord.concat("研究生");
						}
					MarkRecord=MarkRecord+String.valueOf(Score).concat("分");
					
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
				}else{
					return 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	
}
