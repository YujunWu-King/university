package com.tranzvision.gd.TZUnifiedBaseBundle.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAutomaticScreenBundle.dao.*;
import com.tranzvision.gd.TZUnifiedBaseBundle.model.*;
import com.tranzvision.gd.util.sql.SqlQuery;

@Service("com.tranzvision.gd.TZUnifiedBaseBundle.service.impl.TzZddfXLFServiceImpl")
public class TzZddfXLFServiceImpl extends TzZddfServiceImpl {

	@Autowired
	private PsTzCjxTblMapper psTzCjxTblMapper;
	@Autowired
	private SqlQuery SqlQuery;

	
	//获取参数：成绩单ID、学历分成绩项ID、报名表ID
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
				
				
				//根据报名表ID查询考生本科学历的学历、学位、学校ID
				String XL = ksMap.get("TZ_10highdegree");		//学历
				String XW = ksMap.get("TZ_10hxuewei");			//学位
				String XX= ksMap.get("TZ_11luniversitysch");	//学校
				String XLFL= ksMap.get("TZ_11fl");				//学历分类						
				
				 
				//声明float型字段“得分”，string型字段“打分记录”；
				float Score;
				String MarkRecord;
				String XXLX="";
				String XXType="";
				
				//判断是否为国外学校
				String uniScholContry = ksMap.get("TZ_11luniversitycountry");
				if ("中国大陆".equals(uniScholContry)) {	
				} else {	
					
					//如果是外国学校，截取（前的内容			
					if(XX.indexOf("(")>0){	
						XX=XX.substring(0, XX.indexOf("("));						
					}else{
						XX="邓莱里文艺理工学院";
					}
				}
				
				
				
				
				//根据考生学校ID查询所属学校类型
				String sql = "SELECT TZ_SCHOOL_TYPE FROM PS_TZ_SCH_LIB_TBL where TZ_SCHOOL_NAME=?";
				XXLX = SqlQuery.queryForObject(sql, new Object[] { XX },"String");
				
				if(XXLX !=null&& !XXLX.equals("")){
				switch (Integer.parseInt(XXLX)) {
					case 1:
						// 清华大学;
						XXLX="1";
						XXType="清华大学";
						break;
					case 2:
						// 北京大学;
						XXLX="1";
						XXType="北京大学";
						break;
					case 3:
						// 985;
						XXLX="3";
						XXType="985";
						break;
					case 4:
						// 211;
						XXLX="4";
						XXType="211";
						break;
					case 5:
						// 成人;
						XXLX="6";
						XXType="其他";
						break;
					case 6:
						// *985;
						XXLX="2";
						XXType="*985";
						break;
					case 7:
						// 海外1;
						XXLX="7";
						XXType="海外1";
						break;
					case 8:
						// 海外2;
						XXLX="8";
						XXType="海外2";
						break;
					case 9:
						// 普通本科;
						XXLX="5";
						XXType="普通本科";
						break;
					
					default:
						// 其他;
						XXLX="6";
						XXType="其他";
						break;
				}
				}else{
					XXLX="6";
					XXType="其他";
				}

				//学历
				//数据库：1是本科，2自考本科 	3成教本科   4专升本
				//传入参数：1是本科	2自考本科 	3成教本科   4专升本
				
				String XLF="1";
								
				switch(XLFL){
					case"1":
						XLF="1";
						break;
					case"2":
						XLF="2";
						break;
					case"3":
						XLF="3";
						break;
					case"4":
						XLF="4";
						break;
				}
				
				
				//学位
				//数据库：1是学士，2是无
				//传入参数：1博士	2硕士	3学士	4无
				String XWF;
				if("4".equals(XW)){
					XWF="2";
				}else {
					XWF="1";
				}
							  
				
				
				//根据考生查询到的学历、学位、学校类型在TZ_CSMB_XLF_T查询对应的得分，如果没有查询到对应得分，得分=0
				String ExistSql = "select 'Y' from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
				String isExist = "";
				isExist = SqlQuery.queryForObject(ExistSql, new Object[] { XXLX,XWF,XLF },"String");
				
				
				
				if ("Y".equals(isExist)) {
					String SearchSql = "select TZ_CSMB_SCOR from PS_TZ_CSMB_XLF_T where TZ_CSMB_CK3=? and  TZ_CSMB_CK2=? and TZ_CSMB_CK1=?";
					String StrScore = SqlQuery.queryForObject(SearchSql,  new Object[] { XXLX,XWF,XLF },"String");
					Score=Float.parseFloat(StrScore);
					
					
					//PS:  学历=研究生TZ_10highdegree（1、3），学位TZ_10hxuewei=硕士/博士（1，2）
								
					//查询考生其他教育经历中，是否有学历=研究生，学位等于硕士/博士的教育经历，如果有，得分=得分+5
					
					//学历
					//数据库：2是本科，1为研究生
					//传入参数：1博士	2本科	3硕士 
					
					//学位
					//数据库：1是学士，2是无
					//传入参数：1博士	2硕士	3学士	4无
					
					if(XW.equals("2")||XW.equals("1")){
						if(XL.equals("3")||XL.equals("1")){
						Score+=5;
						}
					}
					
					
					//得分如果>100，得分=100；
					if(Score>100){
						Score=100;
					}
					
					String XLMS=null;
					String XWMS=null;
					
					
					switch(XLFL){
					case"1":
						XLMS="普通本科";
						break;
					case"2":
						XLMS="自考本科";
						break;
					case"3":
						XLMS="成教本科";
						break;
					case"4":
						XLMS="专升本";
						break;
					}
					
					
					switch(XW){
					case "1":
						XWMS="博士";
						break;
					case "2":
						XWMS="硕士";
						break;
					case "3":
						XWMS="学士";
						break;
						default:XWMS="无";
					}
				
					
					
					//记录打分记录：示例：985|学历：本科|学位：学士|研究生|95分
					MarkRecord="学校类型：".concat(XXType).concat("|学历：").concat(XLMS).concat("|学位：").concat(XWMS);
					
					//是否拼接研究生；
					if(XW.equals("2")||XW.equals("1")){
						if(XL.equals("3")||XL.equals("1")){
							MarkRecord.concat("|研究生");
						}
					}
					
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
				}else{
					return 0;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	
}
