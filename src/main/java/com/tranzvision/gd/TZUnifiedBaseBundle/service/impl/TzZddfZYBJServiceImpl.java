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
import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZBaseBundle.service.impl.FliterForm;
import com.tranzvision.gd.TZNegativeListInfeBundle.dao.PsTzCsKsFmTMapper;
//import com.tranzvision.gd.TZUnifiedBaseBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfZYBJServiceImpl")
public class TzZddfZYBJServiceImpl extends TzZddfServiceImpl {
	
	@Autowired
	private TZGDObject TzSQLObject;
	@Autowired
	private SqlQuery SqlQuery;
	@Autowired
	private PsTzCsKsFmTMapper PsTzCsKsFmTMapper;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	
	
	//获取参数：成绩单ID、职业背景成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID,String TZ_SCORE_ID,String TZ_SCORE_ITEM) {
		try {
			//声明float型字段“得分”，string型字段“打分记录”
			float Score = 0;
			String MarkRecord = null;
			float Score1=0;
			float Score2=0;
			
			//根据报名表ID查询考生职业背景，获取公司性质
			String GSXZ = "A";	
			//政府机构或事业单位，获取职位类型
			String ZWLX = "A";	
			
			//创业类型
			String CY = "A";	
			
			//融资情况
			String RZ = "A";
			//融资数额
			String RZE = "A";
			//用户数
			String YH = "A";
			float YHS= Float.parseFloat(YH);
			//营收情况
			String YSQK = "A";
			int YS = Integer.parseInt(YSQK);

			//自有资金
			String ZYZJ = "A";
			//家族企业资产
			String JZZC = "A";
			//其他企业营收
			String QTYS = "A";
			//其他企业利润
			String QTLR ="A";
			//其他企业规模
			String QTGM = "A";
			
			GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
			JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
			
			//如果单位性质是政府机构或事业单位，查询报名表职称字段，到表TZ_CSMB_ZY_T查询对应的得分
			if("ZFJG".equals(GSXZ)||"SYDW".equals(GSXZ)){
			String sql = "SELECT DISTINCT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T where  (TZ_CSMB_CK1='ZFJG' or TZ_CSMB_CK1='SYDW') and TZ_CSMB_CK2=?";
			String FSCJ = jdbcTemplate.queryForObject(sql, String.class, new Object[] {ZWLX});
			Score=Float.parseFloat(FSCJ);
			
			MarkRecord="公司类型：".concat(GSXZ).concat("|职位类型：").concat(ZWLX);
			
			
			//如果单位性质是企业类，查询报名表中的个人年收入字段，到表TZ_CSMB_ZY_T查询对应的得分
			}else if("QY".equals(GSXZ)){
				String NSR = null;	//年收入字段
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_CK3 <=? AND TZ_CSMB_CK2 >? AND TZ_CSMB_CK1 = 'QY'";
				String FSCJ2 = jdbcTemplate.queryForObject(sql2, String.class, new Object[] {NSR,NSR});
				Score=Float.parseFloat(FSCJ2);
				
				MarkRecord="公司类型：".concat("企业类").concat("|年收入：").concat(NSR);
				
			//如果单位性质是创业类，查询报名表中的创业经历	
			}else{
				
				if("IT".equals(CY)){
					//初创
					if("E".equals(RZ)){
						Score=50;
					//尚未获得投资
					}else if("D".equals(RZ)){
						if(YS>5){
							Score=60;
						}else{
							Score=0;
						}
					//A轮B轮天使轮	
					}else{	
					String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ = jdbcTemplate.queryForObject(sql, String.class, new Object[] {RZ,RZE,RZE});
					Score1=Float.parseFloat(FSCJ);
					
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'YS' AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = jdbcTemplate.queryForObject(sql2, String.class, new Object[] {YS,YS});
					Score2=Float.parseFloat(FSCJ2);
					
						if(Score1>Score2){
							Score=Score2;
						}else{
							Score=Score1;
						}
					if(YHS>1000000){
						Score=90;
						}
					}
					MarkRecord="自主创业：".concat("互联网类").concat("|融资情况：").concat(RZ).concat("|营收情况：")+YS+"|用户数："+YHS;
					
				//家族创业类	
				}else if("JZ".equals(CY)){
					String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'ZYZJ' AND TZ_CSMB_TJ1 = 'JZ' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ = jdbcTemplate.queryForObject(sql, String.class, new Object[] {ZYZJ,ZYZJ});
					Score1=Float.parseFloat(FSCJ);
					
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'JZZC' AND TZ_CSMB_TJ1 = 'JZ' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = jdbcTemplate.queryForObject(sql2, String.class, new Object[] {JZZC,JZZC});
					Score2=Float.parseFloat(FSCJ2);
					
						if(Score1>Score2){
							Score=Score2;
						}else{
							Score=Score1;
						}
						MarkRecord="自主创业：".concat("家族企业").concat("|自有资金：").concat(ZYZJ).concat("|家族企业资产：").concat(JZZC);
							
						
						
				//其他企业		
				}else if("QT".equals(CY)){
					String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'YS' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=	? and TZ_CSMB_TJ4>?";
					String FSCJ = jdbcTemplate.queryForObject(sql, String.class, new Object[] {QTYS,QTYS});
					Score1=Float.parseFloat(FSCJ);
					
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'LR' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = jdbcTemplate.queryForObject(sql2, String.class, new Object[] {QTLR,QTLR});
					Score2=Float.parseFloat(FSCJ2);
					
					String sql3 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'GM' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ3 = jdbcTemplate.queryForObject(sql3, String.class, new Object[] {QTGM,QTGM});
					float Score3=Float.parseFloat(FSCJ3);
					
					
					
						if(Score1>Score2){
							Score=Score2;
						}else{
							Score=Score1;
						}
						if(Score>Score3){
							Score=Score3;
						}
				}
				MarkRecord="自主创业：".concat("其他类型").concat("|近12个月收入：").concat(QTYS).concat("|年纯利润：").concat(QTLR).concat("|企业规模：").concat(QTGM);
				
			}
			
			MarkRecord=MarkRecord+("|")+String.valueOf(Score).concat("分");
			
				//插入表TZ_CJX_TBL
				PsTzCjxTblWithBLOBs psTzCjxTblWithBLOBs=new PsTzCjxTblWithBLOBs();
					//成绩单ID
					Long tzScoreInsId=Long.parseLong(TZ_SCORE_ID);
					psTzCjxTblWithBLOBs.setTzScoreInsId(tzScoreInsId);
					//成绩项ID
					psTzCjxTblWithBLOBs.setTzScoreItemId(TZ_SCORE_ITEM);
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
