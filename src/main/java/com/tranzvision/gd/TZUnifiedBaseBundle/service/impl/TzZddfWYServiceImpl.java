package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfWYServiceImpl")
public class TzZddfWYServiceImpl extends TzZddfServiceImpl {


	@Autowired
	private SqlQuery SqlQuery;

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	
	//获取参数：成绩单ID、外语水平成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID,String TZ_SCORE_ID,String TZ_SCORE_ITEM) {
				try {
					//声明float型字段“得分”，string型字段“打分记录”
					float Score;
					String MarkRecord = null;
					
					//根据报名表ID查询考生循环考生英语成绩，查询考生英语成绩类型
					String YYLX ="A";	//类型
					String CJ="A";	//成绩
					String YYZT="A" ;	//状态（是否通过、高中低级）
					
					//根据报名表ID查询考生是否有其他语种的外语成绩
					String QTYZ="A";
					
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
					
					
					for (int i = 0; i < TESTSqlCon2.size(); i++) {
						String WYLX=TESTSqlCon2.get(i);  
						String WYCJ = null;
						String WYJB = null;
						float FSCJF;
						
						//上下限类型，查询报名表中的证书成绩字段
						if(WYLX.equals("GRE")||WYLX.equals("GMAT")||WYLX.equals("TOFEL")||WYLX.equals("IELTS")||WYLX.equals("710E6")||WYLX.equals("710E4")||WYLX.equals("100E6")||WYLX.equals("100E4")||WYLX.equals("TOEIC")){
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK2>? AND TZ_CSMB_CK3<=?;";
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] {WYLX,WYCJ,WYCJ},"String");
							FSCJF=Float.parseFloat(FSCJ);
							WYScore.add(FSCJF);
							
						//证书类型，查询报名表中的证书等级字段
						}else if(WYLX.equals("ZYYY")||WYLX.equals("GJKY")||WYLX.equals("ZJKY")||WYLX.equals("BEC")){
							String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK1=?";
							String FSCJ = SqlQuery.queryForObject(sql2,  new Object[] {WYLX,WYJB},"String");
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
					String QTYZCS ="";
					if(QTYZ!=null && QTYZ.length() != 0){
						MarkRecord.concat("|其他语种：").concat(QTYZCS);
					}
					
					//对比考生的英语成绩得分、外语成绩得分，海外院校得分，取得分最大的外语成绩
					Score=Collections.max(WYScore);
					
					MarkRecord=MarkRecord+String.valueOf(Score).concat("分");
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
