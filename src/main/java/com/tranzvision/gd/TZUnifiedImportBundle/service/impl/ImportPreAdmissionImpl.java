package com.tranzvision.gd.TZUnifiedImportBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZUnifiedImportBundle.service.UnifiedImportBase;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;

import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;

//预录取阶段数据导入
@Service("com.tranzvision.gd.TZUnifiedImportBundle.service.impl.ImportPreAdmissionImpl")
public class ImportPreAdmissionImpl implements UnifiedImportBase {

	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;
	@Autowired
	private GetSeqNum getSeqNum;
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;
	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;
	
	/**
	 * 保存导入的数据
	 */
	public String tzSave(List<Map<String, Object>> data,List<String> fields,String targetTbl,int[] result, String[] errMsg) {
		String strRet = "";
		try {
			
			//查询SQL
			String sqlSelectByKey = "SELECT 'Y' FROM TZ_IMP_YLQ_TBL WHERE TZ_MSH_ID=? AND TZ_APP_INS_ID=?";
			
			//更新SQL
			String updateSql = "UPDATE TZ_IMP_YLQ_TBL SET TZ_SCHOLARSHIP_RST=?,TZ_TUITION_REFERENCE=?,TZ_STU_ID=?,TZ_PYXY_ACCEPT=?,TZ_GZZM_ACCEPT=?,TZ_CLASS_RST=?,TZ_EMAIL=?,TZ_INITIAL_PSWD=? WHERE TZ_MSH_ID=? AND TZ_APP_INS_ID=?";

			//插入SQL
			String insertSql = "INSERT INTO TZ_IMP_YLQ_TBL(TZ_MSH_ID,TZ_APP_INS_ID,TZ_SCHOLARSHIP_RST,TZ_TUITION_REFERENCE,TZ_STU_ID,TZ_PYXY_ACCEPT,TZ_GZZM_ACCEPT,TZ_CLASS_RST,TZ_EMAIL,TZ_INITIAL_PSWD) VALUES(?,?,?,?,?,?,?,?,?,?)";
			
			//如果没有报名表新增报名表默认班级和报名表模板
			String strDefaultClass = "",strAppTpl = "";
			
			String orgId= tzLoginServiceImpl.getLoginedManagerOrgid(request);
			String oprId= tzLoginServiceImpl.getLoginedManagerOprid(request);
			
			//开始保存数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID"));
					String TZ_APP_INS_ID = ((String)data.get(i).get("TZ_APP_INS_ID"));
					String TZ_SCHOLARSHIP_RST = ((String)data.get(i).get("TZ_SCHOLARSHIP_RST"));
					String TZ_TUITION_REFERENCE = ((String)data.get(i).get("TZ_TUITION_REFERENCE"));
					String TZ_STU_ID = (String)data.get(i).get("TZ_STU_ID");
					String TZ_PYXY_ACCEPT = (String)data.get(i).get("TZ_PYXY_ACCEPT");
					String TZ_GZZM_ACCEPT = (String)data.get(i).get("TZ_GZZM_ACCEPT");
					String TZ_CLASS_RST = (String)data.get(i).get("TZ_CLASS_RST");
					String TZ_EMAIL = (String)data.get(i).get("TZ_EMAIL");
					String TZ_INITIAL_PSWD = (String)data.get(i).get("TZ_INITIAL_PSWD");
					
					if(TZ_MSH_ID!=null&&!"".equals(TZ_MSH_ID)){
						//检查报名表编号是否存在，如果不存在则为该考生新增一张报名表
						if(TZ_APP_INS_ID==null||"".equals(TZ_APP_INS_ID)||"NEXT".equals(TZ_APP_INS_ID)){
							if("".equals(strDefaultClass)){
								String defaultProject = sqlQuery.queryForObject("SELECT TZ_HARDCODE_VAL FROM PS_TZ_HARDCD_PNT WHERE TZ_HARDCODE_PNT='TZ_IMPORT_DEFAULT_PROJECT'",
										new Object[]{}, "String");
								
								Map<String,Object> map= sqlQuery.queryForMap("SELECT TZ_CLASS_ID,TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_JG_ID=? AND TZ_PRJ_ID=? AND TZ_RX_DT>NOW() ORDER BY TZ_RX_DT ASC LIMIT 0,1",
										new Object[]{orgId,defaultProject});
								if(map!=null){
									strDefaultClass = (String)map.get("TZ_CLASS_ID");
									strAppTpl = (String)map.get("TZ_APP_MODAL_ID");
								}
								
							}
							
							if(strDefaultClass!=null&&!"".equals(strDefaultClass)){
								Long tzAppInsId = Long.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID"));
								TZ_APP_INS_ID = String.valueOf(tzAppInsId);
										
								PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
								psTzAppInsT.setTzAppInsId(tzAppInsId);
								psTzAppInsT.setTzAppTplId(strAppTpl);
								psTzAppInsT.setTzAppFormSta("S");
								psTzAppInsT.setRowAddedOprid(oprId);
								psTzAppInsT.setRowAddedDttm(new Date());
								psTzAppInsT.setRowLastmantOprid(oprId);
								psTzAppInsT.setRowLastmantDttm(new Date());
								psTzAppInsTMapper.insert(psTzAppInsT);
								
								String strAppOprId = sqlQuery.queryForObject("SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?", 
										new Object[]{TZ_MSH_ID}, "String");
								
								PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
								psTzFormWrkT.setTzClassId(strDefaultClass);
								psTzFormWrkT.setOprid(strAppOprId);
								psTzFormWrkT.setTzAppInsId(tzAppInsId);
								psTzFormWrkT.setRowAddedOprid(oprId);
								psTzFormWrkT.setRowAddedDttm(new Date());
								psTzFormWrkT.setRowLastmantOprid(oprId);
								psTzFormWrkT.setRowLastmantDttm(new Date());
								psTzFormWrkTMapper.insert(psTzFormWrkT);
							}
						}
						
						if(TZ_APP_INS_ID==null||"".equals(TZ_APP_INS_ID)||"NEXT".equals(TZ_APP_INS_ID)){
							continue;
						}
						
						//查询数据是否存在
						String dataExist = sqlQuery.queryForObject(sqlSelectByKey,new Object[]{TZ_MSH_ID,TZ_APP_INS_ID}, "String");
						
						if(dataExist!=null){
							//更新模式
							sqlQuery.update(updateSql, new Object[]{TZ_SCHOLARSHIP_RST,TZ_TUITION_REFERENCE,TZ_STU_ID,TZ_PYXY_ACCEPT,
									TZ_GZZM_ACCEPT,TZ_CLASS_RST,TZ_EMAIL,TZ_INITIAL_PSWD,TZ_MSH_ID,TZ_APP_INS_ID});
						}else{
							//新增模式
							sqlQuery.update(insertSql, new Object[]{TZ_MSH_ID,TZ_APP_INS_ID,TZ_SCHOLARSHIP_RST,
									TZ_TUITION_REFERENCE,TZ_STU_ID,TZ_PYXY_ACCEPT,TZ_GZZM_ACCEPT,TZ_CLASS_RST,TZ_EMAIL,TZ_INITIAL_PSWD});
						}
						
					}else{
						continue;
					}					
					
					result[1] = ++result[1];
				}
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
		return strRet;
	}

	@Override
	public void tzValidate(List<Map<String, Object>> data, List<String> fields, String targetTbl, Object[] result,
			String[] errMsg) {
		try {

			ArrayList<String> resultMsg = new ArrayList<String>();
			
			//开始校验数据
			if (data != null && data.size()>0){
				for(int i=0;i<data.size();i++){
					String TZ_MSH_ID = ((String)data.get(i).get("TZ_MSH_ID"));
					String TZ_APP_INS_ID = ((String)data.get(i).get("TZ_APP_INS_ID"));
					
					if(TZ_MSH_ID==null||"".equals(TZ_MSH_ID)){
						result[0] = false;
						resultMsg.add("第["+(i+1)+"]行面试申请号不能为空");
					}else{
						//检查面试申请号是否存在用户信息表中
						String TZ_MSH_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_MSH_ID=?", 
								new Object[]{TZ_MSH_ID}, "String");
						if(!"Y".equals(TZ_MSH_ID_EXIST)){
							result[0] = false;
							resultMsg.add("第["+(i+1)+"]行面试申请号不存在");
						}else{
							//检查报名表编号
							if(TZ_APP_INS_ID!=null&&!"".equals(TZ_APP_INS_ID)&&!"NEXT".equals(TZ_APP_INS_ID)){
								String TZ_APP_INS_ID_EXIST = sqlQuery.queryForObject("SELECT 'Y' FROM PS_TZ_FORM_WRK_T A,PS_TZ_AQ_YHXX_TBL B WHERE A.OPRID=B.OPRID AND A.TZ_APP_INS_ID=? AND B.TZ_MSH_ID=? LIMIT 0,1", 
										new Object[]{TZ_APP_INS_ID,TZ_MSH_ID}, "String");
								if(!"Y".equals(TZ_APP_INS_ID_EXIST)){
									result[0] = false;
									resultMsg.add("第["+(i+1)+"]行报名表编号不存在或者与对应的面试申请号不匹配");
								}
							}
						}
					}
				}
			}else{
				resultMsg.add("您没有导入任何数据！");
			}
			
			result[1] = String.join("，", (String[])resultMsg.toArray(new String[resultMsg.size()]));
			
		} catch (Exception e) {
			e.printStackTrace();
			errMsg[0] = "1";
			errMsg[1] = e.toString();
		}
	}
	
}
