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
			float Score;
			String MarkRecord = null;
			
			//根据报名表ID查询考生境外工作经历，国家类别和时间，根据查询结果
			String GJ = ksMap.get("TZ_20TZ_TZ_20_14firm_type");		//国家
			String is_developed = "select 'Y' from PS_COUNTRY_TBL where is_developed ='Y' and descr=?";
			String GJLB=SqlQuery.queryForObject(is_developed,  new Object[]{GJ}, "String");		//国家类别
			
			String StartDate = ksMap.get("TZ_TZ_42_6com_startdate");		//国家
			String EndDate = ksMap.get("TZ_TZ_42_6com_enddate");			//国家
			String ToToday = ksMap.get("TZ_TZ_42_6com_todate");				//至今
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");	//设置日期格式
			String today= df.format(new Date());						// new Date()为获取当前系统时间
			
			float result;
			if("Y".equals(ToToday)){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				String date1 = StartDate;
				c1.setTime(sdf.parse(date1));
				String date2 = today;
				c2.setTime(sdf.parse(date2));
				int a=c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
				result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
				if(a>0){
					result+=12;
				}
			}else{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c1 = Calendar.getInstance();
				Calendar c2 = Calendar.getInstance();
				String date1 = StartDate;
				c1.setTime(sdf.parse(date1));
				String date2 = EndDate;
				c2.setTime(sdf.parse(date2));
				int a=c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
				result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
				if(a>0){
					result+=12;
				}
			}
			
			float SJF= result;
			String SJ=String.valueOf(SJF);

			
			if("Y".equals(GJLB)){
			String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='A'";
			String FSCJ = SqlQuery.queryForObject(sql, new Object[] {SJF,SJF},"String");
			Score=Float.parseFloat(FSCJ);
			GJLB="发达国家";
			}else{
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='B'";
				String FSCJ = SqlQuery.queryForObject(sql2, new Object[] {SJF,SJF},"String");
				Score=Float.parseFloat(FSCJ);
				GJLB="一般国家";
			}
			
			MarkRecord="国家类型：".concat(GJLB).concat("|时间：").concat(SJ);
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
