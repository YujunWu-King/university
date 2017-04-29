package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tranzvision.gd.TZAutomaticScreenBundle.dao.PsTzCjxTblMapper;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;


@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfGJBJServiceImpl")
public class TzZddfGJBJServiceImpl extends TzZddfServiceImpl {
	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;

	@Autowired
	private SqlQuery SqlQuery;
	
	//获取参数：成绩单ID、国际化背景成绩项ID、报名表ID
	@Override
	public float AutoCalculate(String TZ_APP_ID,String TZ_SCORE_ID,String TZ_SCORE_ITEM) {
		try {


			//报名表信息初始化
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
			
			//声明float型字段“得分”，string型字段“打分记录”
			float Score = 0;
			String MarkRecord = "";
//			float result = 0;
			float FDresult = 0;			//发达国家时间 
			float YBresult = 0;			//一般国家时间 
			
			int i=0;
			//根据报名表ID查询考生境外工作经历，国家类别和时间，根据查询结果
			
			String valuesql = "SELECT TZ_XXX_BH, TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE( TZ_APP_INS_ID = ? AND TZ_XXX_BH LIKE '%TZ_42TZ_TZ_42_1%')";
			List<Map<String, Object>> SearchSql = SqlQuery.queryForList(valuesql, new Object[] { TZ_APP_ID});
			for (Map<String, Object> GJmap : SearchSql) {
				String GJXXX = GJmap.get("TZ_XXX_BH").toString();		//国家信息项
				String GJ = GJmap.get("TZ_APP_S_TEXT").toString();		//国家
				

				
				if(GJXXX.length()==15){			//第一条记录

					
					
					String is_developed =" select is_developed from PS_COUNTRY_TBL where descr=? ";
					String GJLB=SqlQuery.queryForObject(is_developed,  new Object[]{GJ}, "String");		//国家类别	N
					
					String StartDate = ksMap.get("TZ_42TZ_TZ_42_6com_startdate");		//开始日期	2010-04-27
					String EndDate = ksMap.get("TZ_42TZ_TZ_42_6com_enddate");			//结束日期	2010-12-31
					String ToToday = ksMap.get("TZ_42TZ_TZ_42_6com_todate");			//至今	N
					
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");	//设置日期格式
					String today= df.format(new Date());						// new Date()为获取当前系统时间
					
					
					if (StartDate != null && !StartDate.equals("")) {
						if(EndDate != null && !EndDate.equals("")){
							if("Y".equals(ToToday)){		//至今
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								Calendar c1 = Calendar.getInstance();
								Calendar c2 = Calendar.getInstance();
								String date1 = StartDate;
								c1.setTime(sdf.parse(date1));
								String date2 = today;
								c2.setTime(sdf.parse(date2));
								int a=c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
								
								if("Y".equals(GJLB)){		//发达国家
									int FDresult1 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
										
									if(a>0){
										FDresult=FDresult+FDresult1+a*12;
									}else{
										FDresult=FDresult+FDresult1;
									}
									
								}else{						//一般国家
									int YBresult1 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
										
									if(a>0){
										YBresult=YBresult+YBresult1+a*12;
										
									}else{
										YBresult=YBresult+YBresult1;
									}
								}
								
							}else{			//结束日期
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								Calendar cc1 = Calendar.getInstance();
								Calendar cc2 = Calendar.getInstance();
								String dateA = StartDate;
								cc1.setTime(sdf2.parse(dateA));
								String dateB = EndDate;
								cc2.setTime(sdf2.parse(dateB));
								int d=cc2.get(Calendar.YEAR)-cc1.get(Calendar.YEAR);
								
								if("Y".equals(GJLB)){		//发达国家
									int FDresult2 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
										
									if(d>0){
										FDresult=FDresult+FDresult2+d*12;
									}else{
										FDresult=FDresult+FDresult2;
									}
								}else{						//一般国家
									int YBresult2 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
										
									if(d>0){
										YBresult=YBresult+YBresult2+d*12;
									}else{
										YBresult=YBresult+YBresult2;
									}
								}
								
							}
						}else if(ToToday != null && !ToToday.equals("")){
							if("Y".equals(ToToday)){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								Calendar c1 = Calendar.getInstance();
								Calendar c2 = Calendar.getInstance();
								String date1 = StartDate;
								c1.setTime(sdf.parse(date1));
								String date2 = today;
								c2.setTime(sdf.parse(date2));
								int a=c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
								if("Y".equals(GJLB)){		//发达国家
									int FDresult7 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
										
									if(a>0){
										FDresult=FDresult+FDresult7+a*12;
									}else{
										FDresult=FDresult+FDresult7;
									}
								}else{						//一般国家
									int YBresult7 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
										
									if(a>0){
										YBresult=YBresult+YBresult7+a*12;
									}else{
										YBresult=YBresult+YBresult7;
									}
								}
							}else{
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								Calendar cc1 = Calendar.getInstance();
								Calendar cc2 = Calendar.getInstance();
								String dateA = StartDate;
								cc1.setTime(sdf2.parse(dateA));
								String dateB = EndDate;
								cc2.setTime(sdf2.parse(dateB));
								int d=cc2.get(Calendar.YEAR)-cc1.get(Calendar.YEAR);
								if("Y".equals(GJLB)){		//发达国家
									int FDresult8 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
										
									if(d>0){
										FDresult=FDresult+FDresult8+d*12;
									}else{
										FDresult=FDresult+FDresult8;
									}
									
								}else{						//一般国家
									int YBresult8 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
										
									if(d>0){
										YBresult=YBresult+YBresult8+d*12;
									}else{
										YBresult=YBresult+YBresult8;
									}
								}
							  }
							}else{
							MarkRecord="结束时间尚未填写";
							Score=0;
						}
					}else{
						Score=0;
						MarkRecord="开始时间尚未填写";
					}
					
					
				}else{
					
					String addition = GJXXX.substring(GJXXX.length()-2);	//第?+1条
					
					String is_developed =" select is_developed from PS_COUNTRY_TBL where descr=?; ";
					String GJLB=SqlQuery.queryForObject(is_developed,  new Object[]{GJ}, "String");		//国家类别
					
					String StartDateO="TZ_42TZ_TZ_42_6com_startdate".concat(addition);
					String EndDateO ="TZ_42TZ_TZ_42_6com_enddate".concat(addition);
					String ToTodayO="TZ_42TZ_TZ_42_6com_todate".concat(addition);
					
					String StartDate = ksMap.get(StartDateO);		//开始日期
					String EndDate = ksMap.get(EndDateO);			//结束日期
					String ToToday = ksMap.get(ToTodayO);			//至今
					
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");	//设置日期格式
					String today= df.format(new Date());						// new Date()为获取当前系统时间
					
					
					if (StartDate != null && !StartDate.equals("")) {
						if(EndDate != null && !EndDate.equals("")){
							if("Y".equals(ToToday)){		//至今
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								Calendar c1 = Calendar.getInstance();
								Calendar c2 = Calendar.getInstance();
								String date1 = StartDate;
								c1.setTime(sdf.parse(date1));
								String date2 = today;
								c2.setTime(sdf.parse(date2));
								int a=c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
								
								if("Y".equals(GJLB)){		//发达国家
									int FDresult5 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
									
									if(a>0){
										FDresult=FDresult+FDresult5+a*12;
									}else{
										FDresult=FDresult+FDresult5;
									}
									
								}else{						//一般国家
									int YBresult5 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
									
									if(a>0){
										YBresult=YBresult+YBresult5+a*12;
									}else{
										YBresult=YBresult+YBresult5;
									}
								}
								
							}else{			//结束日期
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								Calendar cc1 = Calendar.getInstance();
								Calendar cc2 = Calendar.getInstance();
								String dateA = StartDate;
								cc1.setTime(sdf2.parse(dateA));
								String dateB = EndDate;
								cc2.setTime(sdf2.parse(dateB));
								int d=cc2.get(Calendar.YEAR)-cc1.get(Calendar.YEAR);
								
								if("Y".equals(GJLB)){		//发达国家
									int FDresult6 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
									
									if(d>0){
										FDresult=FDresult+FDresult6+d*12;										
									}else{
										FDresult=FDresult+FDresult6;
									}
									
								}else{						//一般国家
									int YBresult6 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
									
									if(d>0){
										YBresult=YBresult+YBresult6+d*12;
										
									}else{
										YBresult=YBresult+YBresult6;
									}
								}
								
							}
						}else if(ToToday != null && !ToToday.equals("")){
							if("Y".equals(ToToday)){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								Calendar c1 = Calendar.getInstance();
								Calendar c2 = Calendar.getInstance();
								String date1 = StartDate;
								c1.setTime(sdf.parse(date1));
								String date2 = today;
								c2.setTime(sdf.parse(date2));
								int a=c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
								if("Y".equals(GJLB)){		//发达国家
									int FDresult7 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
									
									if(a>0){
										FDresult=FDresult+FDresult7+a*12;
									}else{
										FDresult=FDresult+FDresult7;
									}
									
								}else{						//一般国家
									int YBresult7 = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
									if(a>0){
										YBresult=YBresult+YBresult7+a*12;
									}else{
										YBresult=YBresult+YBresult7;
									}
								}
							}else{
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								Calendar cc1 = Calendar.getInstance();
								Calendar cc2 = Calendar.getInstance();
								String dateA = StartDate;
								cc1.setTime(sdf2.parse(dateA));
								String dateB = EndDate;
								cc2.setTime(sdf2.parse(dateB));
								int d=cc2.get(Calendar.YEAR)-cc1.get(Calendar.YEAR);
								if("Y".equals(GJLB)){		//发达国家
									int FDresult8 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
									if(d>0){
										FDresult=FDresult+FDresult8+d*12;
									}else{
										FDresult=FDresult+FDresult8;
									}
									
								}else{						//一般国家
									int YBresult8 = cc2.get(Calendar.MONTH) - cc1.get(Calendar.MONTH);
									if(d>0){
										YBresult=YBresult+YBresult8+d*12;
									}else{
										YBresult=YBresult+YBresult8;
									}
								}
							}
						}else{
							MarkRecord="结束时间尚未填写";
							Score=0;
						}
					}else{
						Score=0;
						MarkRecord="开始时间尚未填写";
					}
					
				}
			
			}
			

			float FDSJF= FDresult;
			float YBSJF= YBresult;
			String FDSJ=String.valueOf(FDSJF);
			String YBSJ=String.valueOf(YBSJF);

			
			
				String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='A'";
				String FSCJ1 = SqlQuery.queryForObject(sql, new Object[] {FDSJF,FDSJF},"String");
				float Score1=Float.parseFloat(FSCJ1);
				
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='B'";
				String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] {YBSJF,YBSJF},"String");
				float Score2=Float.parseFloat(FSCJ2);
				
				if(FDSJF==0&&YBSJF==0){
					MarkRecord="无境外工作经历";
				}else{
				
					if(Score1>Score2||Score1==Score2){
						Score=Score1;
						MarkRecord="国家类型：".concat("发达国家").concat("|时间：").concat(FDSJ.substring(0,FDSJ.indexOf(".")))+"个月";
						
					}else{
						Score=Score2;
						MarkRecord="国家类型：".concat("一般国家").concat("|时间：").concat(YBSJ.substring(0,YBSJ.indexOf(".")))+"个月";
						
						}
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
