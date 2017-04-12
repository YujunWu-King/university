package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			String MarkRecord = null;
			float Score1=0;
			float Score2=0;
			
			//根据报名表ID查询考生职业背景，获取公司性质
			String GSXZ = ksMap.get("TZ_20TZ_TZ_20_14firm_type");
			//政府机构或事业单位，获取岗位类型
			String ZWLXori=ksMap.get("TZ_20TZ_TZ_20_14position_type");
			String ZWLX = ZWLXori;
			//职位类型转换
			switch(ZWLX){
				case "B1":
					ZWLX="1";
				break;
				case "B2":
					ZWLX="2";
				break;
				case "B3":
					ZWLX="3";
				break;
				case "B4":
					ZWLX="4";
				break;
				case "B5":
					ZWLX="5";
				break;
				case "B6":
					ZWLX="6";
				break;
			
			default:
				ZWLX="6";
				break;
			}
			//年收入字段
			String NSR = ksMap.get("TZ_20TZ_TZ_20_21");		
			
			//创业类型
			String CY = ksMap.get("TZ_20TZ_TZ_20_111business_type");	
			
			
			//融资情况
			String RZ = ksMap.get("TZ_20TZ_TZ_20_111financing_type");	
			switch (RZ){
			case "ANGEL_INVEST":
				RZ="C";
			break;
			case "A_FINANCING":
				RZ="B";
			break;
			case "B_FINANCING":
				RZ="A";
			break;
			}
			//融资数额
			String RZE1 =  ksMap.get("TZ_20TZ_TZ_20_111financing_binput");		//B轮融资额
			String RZE2 =  ksMap.get("TZ_20TZ_TZ_20_111financing_ainput");		//A轮融资额
			String RZE3 =  ksMap.get("TZ_20TZ_TZ_20_111financing_anginput");	//天使轮融资额
					 
			
			//用户数
			String YH = ksMap.get("TZ_20TZ_TZ_20_111user_num");
			float YHS= Float.parseFloat(YH);
			//营收情况（近12个月收入）
			String YSQK = ksMap.get("TZ_20TZ_TZ_20_111income_y");
			int YS = Integer.parseInt(YSQK);

			//自有资金
			String ZYZJ = ksMap.get("TZ_20TZ_TZ_20_111own_money");
			//家族企业资产
			String JZZC = ksMap.get("TZ_20TZ_TZ_20_111family_money");
			
			//其他企业营收（近12个月收入）
			String QTYS = ksMap.get("TZ_20TZ_TZ_20_111income_o");
			//其他企业利润（年纯利润）
			String QTLR =ksMap.get("TZ_20TZ_TZ_20_111year_profit");
			//其他企业规模
			String QTGM = ksMap.get("TZ_20TZ_TZ_20_111firm_scale");
			
			

			
			//如果单位性质是政府机构或事业单位，查询报名表职称字段，到表TZ_CSMB_ZY_T查询对应的得分
			if("05".equals(GSXZ)||"06".equals(GSXZ)){
			String sql = "SELECT DISTINCT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T where  (TZ_CSMB_CK1='ZFJG' or TZ_CSMB_CK1='SYDW') and TZ_CSMB_CK2=?";
			String FSCJ = SqlQuery.queryForObject(sql, new Object[] {ZWLX},"String");
			Score=Float.parseFloat(FSCJ);
			
			if("05".equals(GSXZ)){
				MarkRecord="公司类型：".concat("政府机关").concat("|职位类型：").concat(ZWLXori);
			}else{
				MarkRecord="公司类型：".concat("事业单位").concat("|职位类型：").concat(ZWLXori);	
			}
			
			//如果单位性质是企业类，查询报名表中的个人年收入字段，到表TZ_CSMB_ZY_T查询对应的得分
			}else if("01".equals(GSXZ)||"03".equals(GSXZ)||"04".equals(GSXZ)){
				
				String sql2 = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_CK3 <=? AND TZ_CSMB_CK2 >? AND TZ_CSMB_CK1 = 'QY'";
				String FSCJ2 = SqlQuery.queryForObject(sql2, new Object[] {NSR,NSR},"String");
				Score=Float.parseFloat(FSCJ2);
				
				MarkRecord="公司类型：".concat("企业类").concat("|年收入：").concat(NSR);
				
			//如果单位性质是创业类，查询报名表中的创业经历	
			}else if("02".equals(GSXZ)){
				//互联网类
				if("01".equals(CY)){
					//初创
					if("NEW_CREATE".equals(RZ)){
						Score=50;
					//尚未获得投资
					}else if("NO_FINANCING".equals(RZ)){
						if(YS>5){
							Score=60;
						}else{
							Score=0;
						}
					//A轮B轮天使轮	
					}else{	
						if("ANGEL_INVEST".equals(RZ)){
							//天使轮
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] {RZ,RZE3,RZE3},"String");
							Score1=Float.parseFloat(FSCJ);
						}else if("A_FINANCING".equals(RZ)){
							//A轮融资
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] {RZ,RZE2,RZE2},"String");
							Score1=Float.parseFloat(FSCJ);
						}else{
							//B轮融资
							String sql = "SELECT TZ_CSMB_SCOR FROM PS_TZ_CSMB_ZY_T WHERE TZ_CSMB_TJ2= ? AND TZ_CSMB_TJ1 = 'IT' and TZ_CSMB_TJ3<=? and TZ_CSMB_TJ4>?";
							String FSCJ = SqlQuery.queryForObject(sql, new Object[] {RZ,RZE1,RZE1},"String");
							Score1=Float.parseFloat(FSCJ);
						}
							
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
					
					String RZQK=null;
					switch(RZ){
					case "NEW_CREATE":
						RZQK="初创";
					break;	
					case "NO_FINANCING":
						RZQK="尚未获得投资";
					break;	
					case "ANGEL_INVEST":
						RZQK="天使投资";
					break;
					case "A_FINANCING":
						RZQK="A轮融资";
					break;
					case "B_FINANCING":
						RZQK="B轮融资";
					break;	
					}
					
					MarkRecord="自主创业：".concat("互联网类").concat("|融资情况：").concat(RZQK).concat("|营收情况：")+YS+"|用户数："+YHS;
					
				//家族创业类	
				}else if("02".equals(CY)){
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
				}else if("03".equals(CY)){
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
				}else{
					Score=0;
				}
				MarkRecord="自主创业：".concat("其他类型").concat("|近12个月收入：").concat(QTYS).concat("|年纯利润：").concat(QTLR).concat("|企业规模：").concat(QTGM);
				
			}else{
				Score=0;
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
