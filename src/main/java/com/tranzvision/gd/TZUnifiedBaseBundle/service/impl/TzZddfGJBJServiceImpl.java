package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
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
			//声明float型字段“得分”，string型字段“打分记录”
			float Score;
			String MarkRecord = null;
			
			//根据报名表ID查询考生境外工作经历，国家类别和时间，根据查询结果
			String GJLB = "A";	//国家类别
			String SJ = "A";	//时间
		
			float SJF= Float.parseFloat(SJ);

			
			if("A".equals(GJLB)){
			String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='A'";
			String FSCJ = SqlQuery.queryForObject(sql, new Object[] {SJF,SJF},"String");
			Score=Float.parseFloat(FSCJ);
			}else{
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_GJH_T where  TZ_CSMB_CK1>? AND TZ_CSMB_CK2<=? AND TZ_CSMB_DESC='B'";
				String FSCJ = SqlQuery.queryForObject(sql2, new Object[] {SJF,SJF},"String");
				Score=Float.parseFloat(FSCJ);
			}
			
			MarkRecord="国家类型：".concat(GJLB).concat("|时间：").concat(SJ);//.concat("|学位：").concat(XW);
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
