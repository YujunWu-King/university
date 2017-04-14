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
//import com.tranzvision.gd.TZUnifiedBaseBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.base.GetSpringBeanUtil;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;



public class ClmsZddfImpl {

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

		
		
		//获取参数：成绩单ID、外语水平成绩项ID、报名表ID
		public float getWYScore(String TZ_SCORE_INS_ID,String TZ_SCORE_ITEM_ID,String[] paramters) {
			try {
				//声明float型字段“得分”，string型字段“打分记录”
				float Score;
				String MarkRecord = null;
				
				//根据报名表ID查询考生循环考生英语成绩，查询考生英语成绩类型
				String YYLX = paramters[0];	//类型
				String CJ = paramters[1];	//成绩
				String YYZT = paramters[2];	//状态（是否通过、高中低级）
				
				//根据报名表ID查询考生是否有其他语种的外语成绩
				String QTYZ = paramters[6];
				
				float YYCJ= Float.parseFloat(CJ);
	
		/*		
				
				//根据报名表ID查询参考代码
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				String sql = "select TZ_AUD_XM from PS_TZ_AUDCYUAN_T where TZ_AUDIENCE_ID=?";
				String XXLX = jdbcTemplate.queryForObject(sql, String.class, new Object[] { XXID });
				 
				String TESTSql  = "SELECT TZ_AUD_NAM FROM PS_TZ_AUD_DEFN_T WHERE TZ_AUD_STAT=2 ";
				//	String TESTSqlCon = jdbcTemplate.queryForObject(TESTSql, new Object[] { strAudId }, "String");
					List<String> TESTSqlCon2 =jdbcTemplate.queryForList(TESTSql, "String");
				//	Map<String, Object> entry1=jdbcTemplate.queryForMap(TESTSql);    
					System.out.println(TESTSqlCon2.get(2));
				//	System.out.println(entry1);
				
				*/
				List<String> TESTSqlCon2=new ArrayList<String>(); 
				
				List<Float> WYScore = new ArrayList<Float>();
				
				GetSpringBeanUtil getSpringBeanUtil = new GetSpringBeanUtil();
				JdbcTemplate jdbcTemplate = (JdbcTemplate) getSpringBeanUtil.getSpringBeanByID("jdbcTemplate");
				
				for (int i = 0; i < TESTSqlCon2.size(); i++) {
					String WYLX=TESTSqlCon2.get(i);  
					String WYCJ = null;
					String WYJB = null;
					float FSCJF;
					
					//上下限类型，查询报名表中的证书成绩字段
					if(WYLX.equals("GRE")||WYLX.equals("GMAT")||WYLX.equals("TOFEL")||WYLX.equals("IELTS")||WYLX.equals("710E6")||WYLX.equals("710E4")||WYLX.equals("100E6")||WYLX.equals("100E4")||WYLX.equals("TOEIC")){
						String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK2>? AND TZ_CSMB_CK3<=?;";
						String FSCJ = jdbcTemplate.queryForObject(sql, String.class, new Object[] {WYLX,WYCJ,WYCJ});
						FSCJF=Float.parseFloat(FSCJ);
						WYScore.add(FSCJF);
						
					//证书类型，查询报名表中的证书等级字段
					}else if(WYLX.equals("ZYYY")||WYLX.equals("GJKY")||WYLX.equals("ZJKY")||WYLX.equals("BEC")){
						String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK1=?";
						String FSCJ = jdbcTemplate.queryForObject(sql2, String.class, new Object[] {WYLX,WYJB});
						FSCJF=Float.parseFloat(FSCJ);
						WYScore.add(FSCJF);
						
					}
				
				
				
				if(QTYZ!=null && QTYZ.length() != 0){
					WYScore.add((float)80);
				}
				
				//根据报名表ID查询考生是否有海外院校学位，获取考生学位
				if("Y".equals("海外院校")&&"Y".equals("学士")){
					WYScore.add((float)100);
				}else if("Y".equals("海外院校")&&"Y".equals("硕士")){
					WYScore.add((float)90);
				}
				
			
				
			
					//记录打分记录：英语成绩类型：GMAT>=750|其他语种：日语二级|100分
					MarkRecord="英语成绩类型：".concat(WYLX).concat("=").concat(WYCJ);//.concat("|学位：").concat(XW);
					
					
				}
				//其他语种成绩
				String QTYZCS = paramters[66];
				if(QTYZ!=null && QTYZ.length() != 0){
					MarkRecord.concat("|其他语种：").concat(QTYZCS);
				}
				
				//对比考生的英语成绩得分、外语成绩得分，海外院校得分，取得分最大的外语成绩
				Score=Collections.max(WYScore);
				
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
				
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
		
		
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
		
				//获取参数：成绩单ID、职业背景成绩项ID、报名表ID
				public float getZYScore(String TZ_SCORE_INS_ID,String TZ_SCORE_ITEM_ID,String[] paramters) {
					try {
						//声明float型字段“得分”，string型字段“打分记录”
						float Score = 0;
						String MarkRecord = null;
						float Score1=0;
						float Score2=0;
						
						//根据报名表ID查询考生职业背景，获取公司性质
						String GSXZ = paramters[0];	
						//政府机构或事业单位，获取职位类型
						String ZWLX = paramters[0];	
						
						//创业类型
						String CY = paramters[1];	
						
						//融资情况
						String RZ = paramters[1];
						//融资数额
						String RZE = paramters[1];
						//用户数
						String YH = paramters[1];
						float YHS= Float.parseFloat(YH);
						//营收情况
						String YSQK = paramters[1];
						int YS = Integer.parseInt(YSQK);
			
						//自有资金
						String ZYZJ = paramters[1];
						//家族企业资产
						String JZZC = paramters[1];
						//其他企业营收
						String QTYS = paramters[1];
						//其他企业利润
						String QTLR = paramters[1];
						//其他企业规模
						String QTGM = paramters[1];
						
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
