package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


//import com.tranzvision.gd.TZUnifiedBaseBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfZYBJServiceImpl")
public class TzZddfZYBJServiceImpl extends TzZddfServiceImpl {
	

	@Autowired
	private SqlQuery SqlQuery;

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
			

			
			//如果单位性质是政府机构或事业单位，查询报名表职称字段，到表TZ_CSMB_ZY_T查询对应的得分
			if("ZFJG".equals(GSXZ)||"SYDW".equals(GSXZ)){
			String sql = "SELECT DISTINCT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T where  (TZ_CSMB_CK1='ZFJG' or TZ_CSMB_CK1='SYDW') and TZ_CSMB_CK2=?";
			String FSCJ = SqlQuery.queryForObject(sql, new Object[] {ZWLX},"String");
			Score=Float.parseFloat(FSCJ);
			
			MarkRecord="公司类型：".concat(GSXZ).concat("|职位类型：").concat(ZWLX);
			
			
			//如果单位性质是企业类，查询报名表中的个人年收入字段，到表TZ_CSMB_ZY_T查询对应的得分
			}else if("QY".equals(GSXZ)){
				String NSR = null;	//年收入字段
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_CK3 <=? AND TZ_CSMB_CK2 >? AND TZ_CSMB_CK1 = 'QY'";
				String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] {NSR,NSR},"String");
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
					String FSCJ = SqlQuery.queryForObject(sql, new Object[] {RZ,RZE,RZE},"String");
					Score1=Float.parseFloat(FSCJ);
					
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'YS' AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] {YS,YS},"String");
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
					String FSCJ = SqlQuery.queryForObject(sql, new Object[] {ZYZJ,ZYZJ},"String");
					Score1=Float.parseFloat(FSCJ);
					
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'JZZC' AND TZ_CSMB_TJ1 = 'JZ' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] {JZZC,JZZC},"String");
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
					String FSCJ = SqlQuery.queryForObject(sql, new Object[] {QTYS,QTYS},"String");
					Score1=Float.parseFloat(FSCJ);
					
					String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'LR' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] {QTLR,QTLR},"String");
					Score2=Float.parseFloat(FSCJ2);
					
					String sql3 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= 'GM' AND TZ_CSMB_TJ1 = 'QT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
					String FSCJ3 = SqlQuery.queryForObject(sql3, new Object[] {QTGM,QTGM},"String");
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
