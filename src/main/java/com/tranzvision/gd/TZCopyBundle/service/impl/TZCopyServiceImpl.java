package com.tranzvision.gd.TZCopyBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsTzAqYhxxTblMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.dao.PsroleuserMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTbl;
import com.tranzvision.gd.TZAccountMgBundle.model.PsTzAqYhxxTblKey;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZAccountMgBundle.model.Psroleuser;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTblKey;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkTKey;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
/**
 * 复制账号、报名表实例
 * @author WRL
 *
 */
@Service("com.tranzvision.gd.TZCopyBundle.service.impl.TZCopyServiceImpl")
public class TZCopyServiceImpl extends FrameworkImpl {
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private SqlQuery sqlQuery;
	
	@Autowired
	private PsoprdefnMapper psoprdefnMapper;
	
	@Autowired
	private PsTzAqYhxxTblMapper psTzAqYhxxTblMapper;
	
	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;
	
	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;
	
	@Autowired
	private PsroleuserMapper psroleuserMapper;
	
	@Autowired
	private PsTzFormWrkTMapper	psTzFormWrkTMapper;
	
	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;
	
	@Autowired
	private GetSeqNum getSeqNum;
	/**
	 * 复制oprid账号count次
	 * 
	 * @param oprid
	 * @param prefix
	 * @param count
	 * @return
	 */
	public String createAccount(String oprid, String prefix, int count) {
		String msg = "";
		for(int i = 1;i <= count; i++){
			String pre = StringUtils.leftPad(i + "", 3, "0");
			String newOprid = prefix + "_" + pre;
			
			String ishasSql = "SELECT 'Y' FROM PSOPRDEFN WHERE OPRID = ? LIMIT 0,1";
			String isHas = sqlQuery.queryForObject(ishasSql, new Object[] { newOprid }, "String");
			
			if (StringUtils.equals("Y", isHas)) {
				msg = msg + "<br/>" + newOprid + "  账户已存在！";
				logger.info( newOprid + " ------ 账户已存在");
				continue;
			}
			/*用户账号表*/
			Psoprdefn psoprdefn = psoprdefnMapper.selectByPrimaryKey(oprid);
			psoprdefn.setOprid(newOprid);
			psoprdefn.setLastupdoprid(newOprid);
			int size = psoprdefnMapper.insert(psoprdefn);
			
			if(size > 0){
				/*用户信息表*/
				PsTzAqYhxxTblKey key = new PsTzAqYhxxTblKey();
				key.setTzDlzhId(oprid);
				key.setTzJgId("SEM");
				
				PsTzAqYhxxTbl psTzAqYhxxTbl = psTzAqYhxxTblMapper.selectByPrimaryKey(key);
				psTzAqYhxxTbl.setTzDlzhId(newOprid);
				psTzAqYhxxTbl.setOprid(newOprid);
				
				String newRealname = psTzAqYhxxTbl.getTzRealname() + "-" + pre;
				psTzAqYhxxTbl.setTzRealname(newRealname);
				
				String[] email = psTzAqYhxxTbl.getTzEmail().split("@");
				String newEmail = email[0] + "-" + pre + "@" + email[1];
				psTzAqYhxxTbl.setTzEmail(newEmail);
				
				String newMobile = StringUtils.substring(psTzAqYhxxTbl.getTzMobile(), 0,8) + pre;
				psTzAqYhxxTbl.setTzMobile(newMobile);

				String tzMshId = String.valueOf(getSeqNum.getSeqNum("2019", "TZ_MSH_ID"));
				tzMshId = "2019" + StringUtils.leftPad(tzMshId, 5, "0");
				psTzAqYhxxTbl.setTzMshId(tzMshId);
				psTzAqYhxxTbl.setRowAddedDttm(new Date());
				psTzAqYhxxTbl.setRowAddedOprid(newOprid);
				psTzAqYhxxTbl.setRowLastmantDttm(new Date());
				psTzAqYhxxTbl.setRowLastmantOprid(newOprid);
				psTzAqYhxxTblMapper.insert(psTzAqYhxxTbl);
				
				/*联系方式*/
				PsTzLxfsInfoTblKey lxfskey = new PsTzLxfsInfoTblKey();
				lxfskey.setTzLxfsLy("ZCYH");
				lxfskey.setTzLydxId(oprid);
				PsTzLxfsInfoTbl psTzLxfsInfoTbl = psTzLxfsInfoTblMapper.selectByPrimaryKey(lxfskey);
				
				psTzLxfsInfoTbl.setTzLydxId(newOprid);
				psTzLxfsInfoTbl.setTzZyEmail(newEmail);
				psTzLxfsInfoTbl.setTzZySj(newMobile);
				psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);
				
				/*通过所有校验，保存用户注册信息*/
				PsTzRegUserT psTzRegUserT = psTzRegUserTMapper.selectByPrimaryKey(oprid);
				psTzRegUserT.setOprid(newOprid);
				psTzRegUserT.setTzRealname(newRealname);
				psTzRegUserT.setRowAddedDttm(new Date());
				psTzRegUserT.setRowAddedOprid(newOprid);
				psTzRegUserT.setRowLastmantDttm(new Date());
				psTzRegUserT.setRowLastmantOprid(newOprid);
				psTzRegUserT.setTzMssqh(tzMshId);
				psTzRegUserTMapper.insert(psTzRegUserT);
				
				// 添加角色;
				String roleSQL = " SELECT ROLENAME FROM PS_TZ_JG_ROLE_T WHERE TZ_JG_ID=? AND TZ_ROLE_TYPE='C'";
				List<Map<String, Object>> roleList = sqlQuery.queryForList(roleSQL, new Object[] { "SEM" });
				if (roleList != null && roleList.size() > 0) {
					for (int j = 0; j < roleList.size(); j++) {
						String rolename = (String) roleList.get(j).get("ROLENAME");
						Psroleuser psroleuser = new Psroleuser();
						psroleuser.setRoleuser(newOprid);
						psroleuser.setRolename(rolename);
						psroleuser.setDynamicSw("N");
						psroleuserMapper.insert(psroleuser);
					}
				}
				logger.info( newRealname + "(" +newOprid + ") ------ 创建成功");
			}else{
				logger.info("failure -- 账户创建失败！         " + newOprid);
				msg = msg + "<br/>" + newOprid + "  账户创建失败！";
			}

		}
		return msg;
	}
	/**
	 * 删除指定前缀的账户
	 * 
	 * @param upperCase
	 * @return
	 */
	public String delAccount(String prefix) {
		String sql1 = "DELETE FROM PSOPRDEFN WHERE OPRID REGEXP BINARY '" + prefix + "*'";
		String sql2 = "DELETE FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID REGEXP BINARY '" + prefix + "*'";
		String sql3 = "DELETE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LYDX_ID REGEXP BINARY '" + prefix + "*'";
		String sql4 = "DELETE FROM PS_TZ_REG_USER_T WHERE OPRID REGEXP BINARY '" + prefix + "*'";
		String sql5 = "DELETE FROM PSROLEUSER WHERE ROLEUSER REGEXP BINARY '" + prefix + "*'";
		
		int del1 = sqlQuery.update(sql1);
		int del2 = sqlQuery.update(sql2);
		int del3 = sqlQuery.update(sql3);
		int del4 = sqlQuery.update(sql4);
		int del5 = sqlQuery.update(sql5);
		
		String ret = "-->" + del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del4 + "    -->" + del5;
		return ret;
	}
	
	/**
	 * 创建报名表
	 * @param oprid		种子账户
	 * @param prefix	目标账户前缀
	 * @param clsid		班级编号
	 * @return
	 */
	public String createAppForm(String oprid, String prefix, String clsid) {
		String msg = "";
		/*报名表工作表*/
		PsTzFormWrkTKey psTzFormWrkTKey = new PsTzFormWrkTKey();
		psTzFormWrkTKey.setOprid(oprid);
		psTzFormWrkTKey.setTzClassId(clsid);
		PsTzFormWrkT psTzFormWrkT = psTzFormWrkTMapper.selectByPrimaryKey(psTzFormWrkTKey );
		
		/*报名表实例表*/
		Long tzAppInsId = psTzFormWrkT.getTzAppInsId();
		PsTzAppInsT psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(tzAppInsId);

		String targetOpridSql= "SELECT OPRID FROM PSOPRDEFN WHERE OPRID REGEXP BINARY '" + prefix + "*'";
		List<Map<String, Object>> oprIdList = sqlQuery.queryForList(targetOpridSql);
		if (oprIdList != null && oprIdList.size() > 0) {
			for (int i = 0; i < oprIdList.size(); i++) {
				/*目标账户编号*/
				String targetOprId = (String) oprIdList.get(i).get("OPRID");
				
				/*是否已经报名*/
				String isHasSql = "SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ? limit 0,1";
				String isHas = sqlQuery.queryForObject(isHasSql, new Object[] { clsid,targetOprId }, "String");
				if (StringUtils.equals(isHas, "Y")) {
					logger.info(targetOprId + " 已经报过名！");
					msg = msg + "<br>" + targetOprId + " 已经报过名！";
					continue;
				}
				
				/*新的报名表实例编号*/
				Long tzNewAppInsId = Long.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID"));
				
				/*复制实例表*/
				psTzAppInsT.setTzAppInsId(tzNewAppInsId);
				psTzAppInsT.setRowAddedOprid(targetOprId);
				psTzAppInsT.setRowLastmantOprid(targetOprId);
				int size = psTzAppInsTMapper.insert(psTzAppInsT);
				if(size > 0){
					//SUCCESS
					/*复制工作表*/
					psTzFormWrkT.setOprid(targetOprId);
					psTzFormWrkT.setTzAppInsId(tzNewAppInsId);
					psTzFormWrkT.setRowAddedOprid(targetOprId);
					psTzFormWrkT.setRowLastmantOprid(targetOprId);
					psTzFormWrkTMapper.insert(psTzFormWrkT);
					
					/*PS_TZ_APP_CC_T*/
					String ccSql = "INSERT INTO PS_TZ_APP_CC_T (TZ_APP_INS_ID,TZ_XXX_BH,TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_APP_L_TEXT) SELECT " + tzNewAppInsId + ",TZ_XXX_BH,TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_APP_L_TEXT FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = " + tzAppInsId;
					int ccSize = sqlQuery.update(ccSql);
					
					/*PS_TZ_APP_DHCC_T*/
					String dhccSql = "INSERT INTO PS_TZ_APP_DHCC_T (TZ_APP_INS_ID,TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_IS_CHECKED) SELECT " + tzNewAppInsId + ",TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_IS_CHECKED FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = " + tzAppInsId;
					int dhccSize = sqlQuery.update(dhccSql);
					
					/*PS_TZ_FORM_ATT_T*/
					String attSql = "INSERT INTO PS_TZ_FORM_ATT_T (TZ_APP_INS_ID,TZ_XXX_BH,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE,ROW_ADDED_DTTM,ROW_ADDED_OPRID,ROW_LASTMANT_DTTM,ROW_LASTMANT_OPRID,SYNCID,SYNCDTTM,TZ_ACCESS_PATH) SELECT " + tzNewAppInsId + ",TZ_XXX_BH,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE,ROW_ADDED_DTTM,'" + targetOprId + "',ROW_LASTMANT_DTTM,'" + targetOprId + "',SYNCID,SYNCDTTM,TZ_ACCESS_PATH FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = " + tzAppInsId;
					int attSize = sqlQuery.update(attSql);
					
					/*PS_TZ_APP_DHHS_T*/
					String dhhsSql = "INSERT INTO PS_TZ_APP_DHHS_T (TZ_APP_INS_ID,TZ_XXX_BH,TZ_XXX_LINE) SELECT " + tzNewAppInsId + ",TZ_XXX_BH,TZ_XXX_LINE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = " + tzAppInsId;
					int dhhsSize = sqlQuery.update(dhhsSql);
					
					/*PS_TZ_APP_HIDDEN_T*/
					String hiddenSql = "INSERT INTO PS_TZ_APP_HIDDEN_T (TZ_APP_INS_ID,TZ_XXX_BH,TZ_IS_HIDDEN) SELECT " + tzNewAppInsId + ",TZ_XXX_BH,TZ_IS_HIDDEN FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = " + tzAppInsId;
					int hiddenSize = sqlQuery.update(hiddenSql);
					
					logger.info("----" + ccSize + " --> " + dhccSize + " --> " + attSize + " --> " + dhhsSize + " --> " + hiddenSize);
				}else{
					logger.info("failure . 报名表实例复制失败！    " + targetOprId);
				}
			}
		}else{
			return "没有符合前缀开头的账户！";
		}

		return null;
	}
	public String delAppAll(String prefix) {
		String sql1 = "DELETE FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*')";
		String sql2 = "DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*')";
		String sql3 = "DELETE FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*')";
		String sql4 = "DELETE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*')";
		String sql5 = "DELETE FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*')";
		String sql6 = "DELETE FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID IN (SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*')";
		String sql7 = "DELETE FROM PS_TZ_FORM_WRK_T WHERE OPRID REGEXP BINARY '" + prefix + "_*'";
		
		int del1 = sqlQuery.update(sql1);
		logger.info("删除报名表相关 - PS_TZ_APP_CC_T  " + del1);
		int del2 = sqlQuery.update(sql2);
		logger.info("删除报名表相关 - PS_TZ_APP_DHCC_T  " + del2);
		int del3 = sqlQuery.update(sql3);
		logger.info("删除报名表相关 - PS_TZ_FORM_ATT_T  " + del3);
		int del5 = sqlQuery.update(sql4);
		logger.info("删除报名表相关 - PS_TZ_APP_DHHS_T  " + del5);
		int del6 = sqlQuery.update(sql5);
		logger.info("删除报名表相关 - PS_TZ_APP_HIDDEN_T  " + del6);
		int del7 = sqlQuery.update(sql6);
		logger.info("删除报名表相关 - PS_TZ_APP_INS_T  " + del7);
		int del8 = sqlQuery.update(sql7);
		logger.info("删除报名表相关 - PS_TZ_FORM_WRK_T  " + del8);

		String ret = del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del5 + "    -->" + del6 + "    -->" + del7
				+ "    -->" + del8;
		return ret;
	}
}
