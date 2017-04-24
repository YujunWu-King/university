package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
					
					//报名表信息表定义
					Map<String, String> ksMap = new HashMap<String, String>();
					String ksMapkey = "";
					String ksMapvalue = "";
					
					
					//查询报名表信息
					String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? ";
					List<Map<String, Object>> listMap = SqlQuery.queryForList(ks_valuesql, new Object[] { TZ_APP_ID });
					for (Map<String, Object> map : listMap) {
						ksMapkey = map.get("TZ_XXX_BH").toString();
						ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
						ksMap.put(ksMapkey, ksMapvalue);
						
						
						
					}
					
					//声明float型字段“得分”，string型字段“打分记录”,float最高分
					float Score;
					float Highest=0;
					
					String MarkRecord = null;
					
				
					String valuesql = "SELECT TZ_XXX_BH, TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH LIKE '%TZ_44exam_type%')";
					List<Map<String, Object>> SqlCon2 = SqlQuery.queryForList(valuesql, new Object[] { TZ_APP_ID});
					
					//定义成绩list
					List<Float> WYScore = new ArrayList<Float>();
					
					//根据报名表ID查询考生循环考生英语成绩，查询考生英语成绩类型
					for (Map<String, Object> map2 : SqlCon2) {
						String WYLXXXX = map2.get("TZ_XXX_BH").toString();		//外语类型信息项
						String WYLX = map2.get("TZ_APP_S_TEXT").toString();		//外语类型
						String LX=null;
						
						String WYCJXXX=WYLXXXX.replace("type", "score");		//外语成绩信息项
						String EngScoreSql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?)";
						String WYCJori = SqlQuery.queryForObject(EngScoreSql, new Object[] { TZ_APP_ID,WYCJXXX },"String");	//外语成绩
						String WYCJ=WYCJori;
						float FSCJF;
						
						switch(WYLX){
						case "ENG_LEV_T9":
							WYLX="ZYYY";
							LX="专业英语";
						break;
						case "ENG_LEV_T10":
							WYLX="GJKY";
							LX="高级口译";
						break;
						case "ENG_LEV_T11":
							WYLX="ZJKY";
							LX="中级口译";
						break;
						case "ENG_LEV_T12":
							WYLX="BEC";
							LX="BEC";
						break;
						case "ENG_LEV_T1":
							WYLX="GRE";
							LX="GRE";
						break;
						case "ENG_LEV_T2":
							WYLX="GMAT";
							LX="GMAT";
						break;
						case "ENG_LEV_T3":
							WYLX="TOFEL";
							LX="TOFEL";
						break;
						case "ENG_LEV_T4":
							WYLX="IELTS";
							LX="IELTS";
						break;
						case "ENG_LEV_T5":
							WYLX="710E6";
							LX="英语六级（710分制）";
						break;
						case "ENG_LEV_T6":
							WYLX="710E4";
							LX="英语四级（710分制）";
						break;
						case "ENG_LEV_T7":
							WYLX="100E6";
							LX="英语六级（100分制）";
						break;
						case "ENG_LEV_T8":
							WYLX="100E4";
							LX="英语四级（100分制）";
						break;
						case "ENG_LEV_T13":
							WYLX="TOEIC";
							LX="TOEIC";
						break;
						}
						
						if(WYCJ!=null&&!WYCJ.equals("")){
						switch(WYCJ){
						case "专业八级":
							WYCJ="A";
						break;
						case "专业四级":
							WYCJ="B";
						break;
						case "拿到资格证书":
							WYCJ="A";
						break;
						case "拿到笔试证书":
							WYCJ="B";
						break;
						case "高级":
							WYCJ="A";
						break;
						case "中极":
							WYCJ="B";
						break;
						case "初级":
							WYCJ="C";
						break;
						
						}
						}
						if(WYLX.equals("GRE")||WYLX.equals("GMAT")||WYLX.equals("TOFEL")||WYLX.equals("IELTS")||WYLX.equals("710E6")||WYLX.equals("710E4")||WYLX.equals("100E6")||WYLX.equals("100E4")||WYLX.equals("TOEIC")){
							float CJ=Float.parseFloat(WYCJ);
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK2>? AND TZ_CSMB_CK3<=?";
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] {WYLX,CJ,CJ},"String");
							if(FSCJ!=null&&!FSCJ.equals("")){
							FSCJF=Float.parseFloat(FSCJ);
					//		System.out.println("本次分数型分数："+FSCJF+"本次类型："+WYLX);
								if(FSCJF>Highest){
									Highest=FSCJF;
								}
							}
							
						//证书类型，查询报名表中的证书等级字段
						}else if(WYLX.equals("ZYYY")||WYLX.equals("GJKY")||WYLX.equals("ZJKY")||WYLX.equals("BEC")){
							String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_WY_T where TZ_CSMB_DESC =? and TZ_CSMB_CK1=?";
							String FSCJ = SqlQuery.queryForObject(sql2,  new Object[] {WYLX,WYCJ},"String");
							FSCJF=Float.parseFloat(FSCJ);
							WYScore.add((float)FSCJF);
								if(FSCJF>Highest){
									Highest=FSCJF;
								}
				//			System.out.println("证书类型分数："+FSCJF);
						}
						
						//记录打分记录：英语成绩类型：GMAT>=750|其他语种：日语二级|100分
						if(MarkRecord != null){
						/*	System.out.println("类型"+LX);
							System.out.println("成绩"+WYCJori);
							System.out.println("是这个报名表"+TZ_APP_ID);*/
							
							MarkRecord=MarkRecord+"英语成绩类型：".concat(LX).concat("=").concat(WYCJori)+"|";//.concat("|学位：").concat(XW);
						}else{
							
						/*	System.out.println("类型"+LX);
							System.out.println("成绩"+WYCJori);
							System.out.println("是这个报名表"+TZ_APP_ID);*/
							
							MarkRecord="英语成绩类型：".concat(LX).concat("=").concat(WYCJori)+"|";//.concat("|学位：").concat(XW);
						}
					}
					
					String QTYZsql = "SELECT TZ_XXX_BH, TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH LIKE '%TZ_18othlang%')";
					List<Map<String, Object>> QTYZMap = SqlQuery.queryForList(QTYZsql, new Object[] { TZ_APP_ID});
					for (Map<String, Object> QTmap2 : QTYZMap) {
						String QTYZ = QTmap2.get("TZ_XXX_BH").toString();		//其他语种信息项
						String QTYZLX = QTmap2.get("TZ_APP_S_TEXT").toString();	
					
						//其他语种成绩
						if(QTYZ!=null && !QTYZ.equals("")){
							if(QTYZLX!=null && !QTYZLX.equals("")){
							
							if(80>Highest){
								Highest=80;
							}
												
							
							switch(QTYZLX){
							case "1":
								QTYZLX="日语";
							break;	
							case "2":
								QTYZLX="法语";
							break;	
							case "3":
								QTYZLX="韩语";
							break;	
							case "4":
								QTYZLX="俄语";
							break;	
							case "5":
								QTYZLX="西班牙语";
							break;	
							case "6":
								QTYZLX="其他语言";
							break;	
							}
							
							
							MarkRecord=MarkRecord+"其他语种：".concat(QTYZLX)+"|";
							
						}
					  }	
						
					}
					
					MarkRecord=MarkRecord.substring(0,MarkRecord.length()-1);
					
					
					
					
					String uniScholContry = ksMap.get("TZ_11luniversitycountry");
					String Contry1 = ksMap.get("TZ_10hdegreeunicountry");
					String Contry2 = ksMap.get("TZ_12ouniversitycountry");
					String Contry3 = ksMap.get("TZ_13ouniver3country");
					String XW = ksMap.get("TZ_10hxuewei");

					
					// 判断 是否有海外学历
					if (ksMap.get("TZ_11luniversitycountry") == null ? true
							: uniScholContry.equals("中国") && ksMap.get("TZ_10hdegreeunicountry") == null ? true
									: Contry1.equals("中国") && ksMap.get("TZ_12ouniversitycountry") == null ? true
											: Contry2.equals("中国") && ksMap.get("TZ_13ouniver3country") == null ? true
													: Contry3.equals("中国")) {
					} else {
						if(XW.equals("1")||XW.equals("2")){
							WYScore.add((float)90);
							
							if(90>Highest){
								Highest=90;
							}
//							System.out.println("海外硕士："+WYScore);
						}else{
						WYScore.add((float)100);
						
						if(100>Highest){
							Highest=100;
						}
//						System.out.println("海外学士："+WYScore);
						}
					}
					

					
					//对比考生的英语成绩得分、外语成绩得分，海外院校得分，取得分最大的外语成绩
					//Score=Collections.max(WYScore);
					Score=Highest;
					MarkRecord=MarkRecord+"|"+String.valueOf(Score).concat("分");
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
						
						//删除已有数据	
						PsTzCjxTblKey psTzCjxTblKey=new PsTzCjxTblKey();
						
						psTzCjxTblKey.setTzScoreInsId(tzScoreInsId);
						psTzCjxTblKey.setTzScoreItemId(TZ_SCORE_ITEM);
						
						psTzCjxTblMapper.deleteByPrimaryKey(psTzCjxTblKey);
					
						//插入	
						psTzCjxTblMapper.insert(psTzCjxTblWithBLOBs);
						
						return Score;
					
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			}
			
}
