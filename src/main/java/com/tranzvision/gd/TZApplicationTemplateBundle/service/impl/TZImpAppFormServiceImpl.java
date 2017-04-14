package com.tranzvision.gd.TZApplicationTemplateBundle.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAccountMgBundle.dao.PsoprdefnMapper;
import com.tranzvision.gd.TZAccountMgBundle.model.Psoprdefn;
import com.tranzvision.gd.TZBaseBundle.service.impl.FrameworkImpl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhhsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.encrypt.DESUtil;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service("com.tranzvision.gd.TZApplicationTemplateBundle.service.impl.TZImpAppFormServiceImpl")
public class TZImpAppFormServiceImpl extends FrameworkImpl {
	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;

	@Autowired
	private PsoprdefnMapper psoprdefnMapper;

	@Autowired
	private TZGDObject tzGDObject;

	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;

	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;

	@Autowired
	private PsTzAppDhhsTMapper psTzAppDhhsTMapper;

	/**
	 * 批量导入报名表
	 * 
	 * @param min
	 *            报名表编号最小值
	 * @param max
	 *            报名表编号最大值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String impAppForm(String clsid, int min, int max) {
		String[] errMsg = { "0", "" };
		String retMsg = "";

		/*--- 模板内容 Begin---*/
		String tplJson = "";
		if (StringUtils.isBlank(tplJson)) {
			try {
				/* 初始化模板文件内容，是通过模板报文将其中的children改成[]后生成的，可通过模板报文、实例报文对比修改 */
				tplJson = tzGDObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APPTPL_JSON_STR_INIT_HTML");
			} catch (TzSystemException e) {
				e.printStackTrace();
			}
		}
		/*--- 模板内容 End ---*/

		/*---模板编号Begin---*/
		String tplSql = "SELECT TZ_APP_MODAL_ID FROM PS_TZ_CLASS_INF_T WHERE TZ_CLASS_ID = ?";
		String strTplId = sqlQuery.queryForObject(tplSql, new Object[] { clsid }, "String");
		/*---模板编号End---*/
		if (StringUtils.isBlank(strTplId)) {
			retMsg = "班级未配置报名表模板";
			return retMsg;
		}
		Long numAppInsId = 	200010L;
							
		String maxSql = "SELECT MAX(cast(TZ_APP_INS_ID as unsigned int)) + 1 FROM PS_TZ_APP_INS_T WHERE cast(TZ_APP_INS_ID as unsigned int) < 400000";
		Long maxInsId = sqlQuery.queryForObject(maxSql, new Object[] {}, "long");

		if (maxInsId != null && maxInsId > numAppInsId) {
			numAppInsId = maxInsId;
		}

		String sql = "SELECT * FROM PS_TZ_APPINS_TBL WHERE cast(TZ_APP_INS_ID as unsigned int) >= ? AND cast(TZ_APP_INS_ID as unsigned int) < ? order by cast(TZ_APP_INS_ID as unsigned int)";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { min, max });
		for (Object obj : resultlist) {
			errMsg[0] = "0";
			errMsg[1] = "";
			Date dateNow = new Date();

			Map<String, Object> result = (Map<String, Object>) obj;
			String attrInsid = result.get("TZ_APP_INS_ID") == null ? "0" : String.valueOf(result.get("TZ_APP_INS_ID"));
			String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
			String attrText1 = result.get("TEXTAREA_1") == null ? "" : String.valueOf(result.get("TEXTAREA_1"));
			String attrText2 = result.get("TEXTAREA_2") == null ? "" : String.valueOf(result.get("TEXTAREA_2"));
			String attrText3 = result.get("TEXTAREA_3") == null ? "" : String.valueOf(result.get("TEXTAREA_3"));

			/*---检查考生是否存在 Begin ---*/
			String isHasOpr = "SELECT 'Y' FROM PS_TZ_REG_USER_T WHERE OPRID = ? LIMIT 0,1";
			String isHas = sqlQuery.queryForObject(isHasOpr, new Object[] { attrOprid }, "String");
			if (!StringUtils.equals("Y", isHas)) {
				retMsg = retMsg + "<br>" + attrOprid + "(" + attrInsid + ")   ----->    考生不存在";
				continue;
			}
			/*---检查考生是否存在 END	---*/

			/*---检查是否已经报名Begin  ---*/
			String isHasIns = "SELECT 'Y' FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ? LIMIT 0,1";
			isHas = sqlQuery.queryForObject(isHasIns, new Object[] { clsid, attrOprid }, "String");
			if (StringUtils.equals("Y", isHas)) {
				retMsg = retMsg + "<br>" + attrOprid + "(" + attrInsid + ")   ----->    报名表已存在";
				continue;
			}
			/*---检查是否已经报名End  ---*/

			/*---检查新编号已经存在Begin  ---*/
			String isChong = "SELECT 'Y' FROM PS_TZ_APPINS_TBL WHERE TZ_APP_INS_ID = ? LIMIT 0,1";
			String isC = sqlQuery.queryForObject(isChong, new Object[] { numAppInsId }, "String");
			if (StringUtils.equals("Y", isC)) {
				retMsg = retMsg + "<br>" + numAppInsId + "(" + attrInsid + ")   ----->    新编号已经存在";
				logger.info(numAppInsId + "(" + attrInsid + ")   ----->    新编号已经存在");
				continue;
			}
			/*---检查新编号已经存在End  ---*/
			
			// 报名表报文
			String strJsonData = "{}";
			try {
				strJsonData = tzGDObject.getText(tplJson, attrText1, attrText2, attrText3);
			} catch (TzSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/*---创建报名表实例 Begin ---*/
			PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
			psTzAppInsT.setTzAppInsId(numAppInsId);
			psTzAppInsT.setTzAppTplId(strTplId);
			psTzAppInsT.setTzAppInsVersion("");
			psTzAppInsT.setTzAppFormSta("S");
			psTzAppInsT.setTzAppinsJsonStr(strJsonData);
			psTzAppInsT.setRowAddedOprid(attrOprid);
			psTzAppInsT.setRowAddedDttm(dateNow);
			psTzAppInsT.setRowLastmantOprid(attrOprid);
			psTzAppInsT.setRowLastmantDttm(dateNow);
			int insSize = psTzAppInsTMapper.insert(psTzAppInsT);
			if (insSize < 1) {
				logger.info("---报名表实例创建失败 ---InsId: " + numAppInsId + "    TPL: " + strTplId + "  OprId: " + attrOprid);
				retMsg = retMsg + "<br>" + "---报名表实例创建失败 ---InsId: " + numAppInsId + "    TPL: " + strTplId
						+ "  OprId: " + attrOprid;
				continue;
			} else {
				/*---创建报名表存储表  Begin ---*/
				PsTzAppCcT psTzAppCcT = new PsTzAppCcT();
				psTzAppCcT.setTzAppInsId(numAppInsId);
				psTzAppCcT.setTzXxxBh("TZ_26TZ_TZ_26_1");
				psTzAppCcT.setTzAppLText(attrText1);
				psTzAppCcTMapper.insert(psTzAppCcT);

				PsTzAppCcT psTzAppCcT1 = new PsTzAppCcT();
				psTzAppCcT1.setTzAppInsId(numAppInsId);
				psTzAppCcT1.setTzXxxBh("TZ_26TZ_TZ_26_2");
				psTzAppCcT1.setTzAppLText("");
				psTzAppCcTMapper.insert(psTzAppCcT1);

				PsTzAppCcT psTzAppCcT2 = new PsTzAppCcT();
				psTzAppCcT2.setTzAppInsId(numAppInsId);
				psTzAppCcT2.setTzXxxBh("TZ_26TZ_TZ_26_3");
				psTzAppCcT2.setTzAppLText(attrText3);
				psTzAppCcTMapper.insert(psTzAppCcT2);

				PsTzAppCcT psTzAppCcT3 = new PsTzAppCcT();
				psTzAppCcT3.setTzAppInsId(numAppInsId);
				psTzAppCcT3.setTzXxxBh("TZ_26TZ_TZ_26_4");
				psTzAppCcT3.setTzAppLText(attrText2);
				psTzAppCcTMapper.insert(psTzAppCcT3);
				/*---创建报名表存储表 End   ---*/

				/*---创建报名表PS_TZ_APP_DHHS_T Begin ---*/
				PsTzAppDhhsT psTzAppDhhsT = new PsTzAppDhhsT();
				psTzAppDhhsT.setTzAppInsId(numAppInsId);
				psTzAppDhhsT.setTzXxxBh("TZ_26");
				psTzAppDhhsT.setTzXxxLine((short) 4);
				psTzAppDhhsTMapper.insert(psTzAppDhhsT);
				/*---创建报名表PS_TZ_APP_DHHS_T End   ---*/

				/*---创建报名表工作表 Begin ---*/
				int count = 0;
				String wksql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				count = sqlQuery.queryForObject(wksql, new Object[] { clsid, attrOprid }, "Integer");
				if (count > 0) {
					PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
					psTzFormWrkT.setTzClassId(clsid);
					psTzFormWrkT.setOprid(attrOprid);
					psTzFormWrkT.setTzAppInsId(numAppInsId);
					psTzFormWrkT.setRowLastmantOprid(attrOprid);
					psTzFormWrkT.setRowLastmantDttm(dateNow);
					psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
					logger.info(
							"---更新工作表---" + attrOprid + "   ----clsid：---" + clsid + "    ---->InsId：" + numAppInsId);
				} else {
					PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
					psTzFormWrkT.setTzClassId(clsid);
					psTzFormWrkT.setOprid(attrOprid);
					psTzFormWrkT.setTzAppInsId(numAppInsId);
					psTzFormWrkT.setRowAddedOprid(attrOprid);
					psTzFormWrkT.setRowAddedDttm(dateNow);
					psTzFormWrkT.setRowLastmantOprid(attrOprid);
					psTzFormWrkT.setRowLastmantDttm(dateNow);
					int inswksize = psTzFormWrkTMapper.insert(psTzFormWrkT);
					if (inswksize < 1) {
						logger.info("---插入工作表失败---" + attrOprid + "   ----clsid：---" + clsid + "    ---->InsId："
								+ numAppInsId);
						retMsg = retMsg + "<br>" + "---插入工作表失败---" + attrOprid + "   ----clsid：---" + clsid
								+ "    ---->InsId：" + numAppInsId;
						continue;
					}

				}
				/*---创建报名表工作表 End   ---*/
			}
			/*---创建报名表实例 End   ---*/
			numAppInsId++;
		}
		return retMsg;
	}

	/**
	 * 批量导入推荐信
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String impAppLetter(String clsid, int min, int max) {

		String[] errMsg = { "0", "" };
		String retMsg = "";

		String sql = "SELECT * FROM PS_TZ_LETTER_INS_TBL WHERE OPRID IN (SELECT OPRID FROM PS_TZ_APPINS_TBL WHERE cast(TZ_APP_INS_ID as unsigned int) >= ? AND cast(TZ_APP_INS_ID as unsigned int) < ? order by cast(TZ_APP_INS_ID as unsigned int))";
		List<?> resultlist = sqlQuery.queryForList(sql, new Object[] { min, max });

		Long numTjxAppInsId = 200010L;
		String maxSql = "SELECT MAX(cast(TZ_APP_INS_ID as unsigned int)) + 1 FROM PS_TZ_APP_INS_T WHERE cast(TZ_APP_INS_ID as unsigned int) < 400000";
		Long maxInsId = sqlQuery.queryForObject(maxSql, new Object[] {}, "long");

		if (maxInsId != null && maxInsId > numTjxAppInsId) {
			numTjxAppInsId = maxInsId;
		}
		
		Long refLetId = 200010L;
		// 初始化推荐信内容
		String engTplJson = "";
		String zhsTplJson = "";
		try {
			engTplJson = tzGDObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APPTPLE_LETTER_ENG_INITHTML");
			zhsTplJson = tzGDObject.getHTMLText("HTML.TZApplicationTemplateBundle.TZ_APPTPLE_LETTER_ZHS_INITHTML");
		} catch (TzSystemException e) {
			e.printStackTrace();
		}
		for (Object obj : resultlist) {
			errMsg[0] = "0";
			errMsg[1] = "";
			Date dateNow = new Date();

			Map<String, Object> result = (Map<String, Object>) obj;

			String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
			String attrText1 = result.get("TEXTAREA_1") == null ? "" : String.valueOf(result.get("TEXTAREA_1"));
			String attrText2 = result.get("TEXTAREA_2") == null ? "" : String.valueOf(result.get("TEXTAREA_2"));
			String attrText3 = result.get("TEXTAREA_3") == null ? "" : String.valueOf(result.get("TEXTAREA_3"));
			String attrEmail = result.get("TZ_EMAIL") == null ? "" : String.valueOf(result.get("TZ_EMAIL"));
			String lang = result.get("LANGUAGE") == null ? "ZHS" : String.valueOf(result.get("LANGUAGE"));

			// 对应的考生是否存在
			String isHasOpr = "SELECT 'Y' FROM PS_TZ_REG_USER_T WHERE OPRID = ? LIMIT 0,1";
			String isHas = sqlQuery.queryForObject(isHasOpr, new Object[] { attrOprid }, "String");
			if (!StringUtils.equals("Y", isHas)) {
				logger.info("---考生不存在 ---attrOprid: " + attrOprid);
				retMsg = retMsg + "<br>" + attrOprid + "----->    考生不存在";
				continue;
			}

			// 报名表实例编号
			Long numAppinsId = sqlQuery.queryForObject(
					"SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ? limit 0,1",
					new Object[] { clsid, attrOprid }, "long");
			if (numAppinsId == null || numAppinsId < 1) {
				logger.info("学生" + attrOprid + "   -----> 无对应报名表   ");
				retMsg = retMsg + "<br>学生" + attrOprid + "   -----> 无对应报名表   " + errMsg[1];
				continue;
			}
			String strTplId = "";
			String strJsonData = "{}";
			try {
				if (StringUtils.equals("ZHS", lang)) {
					strTplId = "148"; // 中文推荐信模板
					strJsonData = tzGDObject.getText(zhsTplJson, attrText1, attrText2, attrText3);
				} else {
					strTplId = "149"; // 英文推荐信模板
					strJsonData = tzGDObject.getText(engTplJson, attrText1, attrText2, attrText3);
				}
			} catch (TzSystemException e) {
				e.printStackTrace();
			}

			/*---创建报名表实例 Begin ---*/
			String tjxSql = "SELECT TZ_REF_LETTER_ID FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID = ? AND OPRID = ? AND TZ_EMAIL = ?";
			String strTjxId = "";
			strTjxId = sqlQuery.queryForObject(tjxSql, new Object[] { numAppinsId, attrOprid, attrEmail }, "String");
			if (strTjxId == null || "".equals(strTjxId)) {
				String str_seq1 = String.valueOf((int) (Math.random() * 10000000));
				String str_seq2 = "00000000000000" + String.valueOf(refLetId);
				str_seq2 = str_seq2.substring(str_seq2.length() - 15, str_seq2.length());
				strTjxId = str_seq1 + str_seq2;
				refLetId ++;
			} else {
				logger.info("推荐信推荐人重复 " + numAppinsId + "   ---" + attrOprid + "    ---" + attrEmail);
				retMsg = retMsg + "<br>推荐信推荐人重复 " + numAppinsId + "   ---" + attrOprid + "    ---" + attrEmail;
				continue;
			}

			PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
			psTzAppInsT.setTzAppInsId(numTjxAppInsId);
			psTzAppInsT.setTzAppTplId(strTplId);
			psTzAppInsT.setTzAppInsVersion("");
			psTzAppInsT.setTzAppFormSta("U");
			psTzAppInsT.setTzAppinsJsonStr(strJsonData);
			psTzAppInsT.setRowAddedOprid(attrOprid);
			psTzAppInsT.setRowAddedDttm(dateNow);
			psTzAppInsT.setRowLastmantOprid(attrOprid);
			psTzAppInsT.setRowLastmantDttm(dateNow);
			int insSize = psTzAppInsTMapper.insert(psTzAppInsT);
			if (insSize < 1) {
				logger.info("---推荐信报名表实例创建失败 ---InsId: " + numTjxAppInsId + "    TPL: " + strTplId + "  OprId: "
						+ attrOprid);
				retMsg = retMsg + "<br>" + "---推荐信报名表实例创建失败 ---InsId: " + numTjxAppInsId + "    TPL: " + strTplId
						+ "  OprId: " + attrOprid;
				continue;
			} else {
				// 存储表
				if (StringUtils.equals("ZHS", lang)) {
					// 中文推荐信
					PsTzAppCcT psTzAppCcT = new PsTzAppCcT();
					psTzAppCcT.setTzAppInsId(numTjxAppInsId);
					psTzAppCcT.setTzXxxBh("TZ_8");
					psTzAppCcT.setTzAppLText(attrText1);
					psTzAppCcTMapper.insert(psTzAppCcT);

					PsTzAppCcT psTzAppCcT1 = new PsTzAppCcT();
					psTzAppCcT1.setTzAppInsId(numTjxAppInsId);
					psTzAppCcT1.setTzXxxBh("TZ_7");
					psTzAppCcT1.setTzAppLText(attrText2);
					psTzAppCcTMapper.insert(psTzAppCcT1);

					PsTzAppCcT psTzAppCcT2 = new PsTzAppCcT();
					psTzAppCcT2.setTzAppInsId(numTjxAppInsId);
					psTzAppCcT2.setTzXxxBh("TZ_64");
					psTzAppCcT2.setTzAppLText(attrText3);
					psTzAppCcTMapper.insert(psTzAppCcT2);
				} else {
					// 英文推荐信
					PsTzAppCcT psTzAppCcT = new PsTzAppCcT();
					psTzAppCcT.setTzAppInsId(numTjxAppInsId);
					psTzAppCcT.setTzXxxBh("TZ_6");
					psTzAppCcT.setTzAppLText(attrText1);
					psTzAppCcTMapper.insert(psTzAppCcT);

					PsTzAppCcT psTzAppCcT1 = new PsTzAppCcT();
					psTzAppCcT1.setTzAppInsId(numTjxAppInsId);
					psTzAppCcT1.setTzXxxBh("TZ_7");
					psTzAppCcT1.setTzAppLText(attrText2);
					psTzAppCcTMapper.insert(psTzAppCcT1);

					PsTzAppCcT psTzAppCcT2 = new PsTzAppCcT();
					psTzAppCcT2.setTzAppInsId(numTjxAppInsId);
					psTzAppCcT2.setTzXxxBh("TZ_8");
					psTzAppCcT2.setTzAppLText(attrText3);
					psTzAppCcTMapper.insert(psTzAppCcT2);
				}

				/* 推荐信表 */
				// 推荐人编号
				String strTjrId = sqlQuery.queryForObject(
						"SELECT COUNT(*) + 1 FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID = ? AND OPRID = ?",
						new Object[] { numAppinsId, attrOprid }, "String");

				PsTzKsTjxTbl psTzKsTjxTbl = new PsTzKsTjxTbl();
				psTzKsTjxTbl.setTzRefLetterId(strTjxId);
				psTzKsTjxTbl.setTzAppInsId(numAppinsId);
				psTzKsTjxTbl.setTzTjxAppInsId(numTjxAppInsId);
				psTzKsTjxTbl.setOprid(attrOprid);
				String strTjxType = "E";
				if (StringUtils.equals("ZHS", lang)) {
					strTjxType = "C";
				}
				psTzKsTjxTbl.setTzTjxType(strTjxType);

				psTzKsTjxTbl.setTzTjrId(strTjrId);
				psTzKsTjxTbl.setTzMbaTjxYx("Y");
				psTzKsTjxTbl.setTzEmail(attrEmail);
				psTzKsTjxTbl.setTzReflettertype("S");
				psTzKsTjxTbl.setRowAddedDttm(dateNow);
				psTzKsTjxTbl.setRowAddedOprid(attrOprid);
				psTzKsTjxTbl.setRowLastmantDttm(dateNow);
				psTzKsTjxTbl.setRowLastmantOprid(attrOprid);
				int size = psTzKsTjxTblMapper.insert(psTzKsTjxTbl);

				if (size < 1) {
					logger.info("---插入推荐信表失败---InsId: " + numAppinsId + "    tjxInsId: " + numTjxAppInsId + "  OprId: "
							+ attrOprid + "  " + attrEmail);
					retMsg = retMsg + "<br>" + "---插入推荐信表失败---InsId: " + numAppinsId + "    tjxInsId: " + numTjxAppInsId
							+ "  OprId: " + attrOprid + "  " + attrEmail;
				}
			}
			logger.info("  OK   " + numAppinsId + "    tjxInsId: " + numTjxAppInsId + "  OprId: " + attrOprid + "  " + attrEmail);
			numTjxAppInsId++;
		}
		return retMsg;
	}

	// 删除报名表存储表信息
	public void delAppIns(Long numAppInsId) {

		Object[] args = new Object[] { numAppInsId };
		sqlQuery.update("DELETE FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = ?", args);
	}

	/**
	 * 删除MBA历史账户设计到的报名表
	 * 
	 * @return
	 */
	public String delAppAll() {
		String sql1 = "DELETE FROM PS_TZ_APP_CC_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql2 = "DELETE FROM PS_TZ_APP_DHCC_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql3 = "DELETE FROM PS_TZ_FORM_ATT_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql5 = "DELETE FROM PS_TZ_APP_DHHS_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql6 = "DELETE FROM PS_TZ_APP_HIDDEN_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql7 = "DELETE FROM PS_TZ_APP_INS_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql8 = "DELETE FROM PS_TZ_FORM_WRK_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";

		int del1 = sqlQuery.update(sql1);
		logger.info("删除报名表相关 - PS_TZ_APP_CC_T  " + del1);
		int del2 = sqlQuery.update(sql2);
		logger.info("删除报名表相关 - PS_TZ_APP_DHCC_T  " + del2);
		int del3 = sqlQuery.update(sql3);
		logger.info("删除报名表相关 - PS_TZ_FORM_ATT_T  " + del3);
		int del5 = sqlQuery.update(sql5);
		logger.info("删除报名表相关 - PS_TZ_APP_DHHS_T  " + del5);
		int del6 = sqlQuery.update(sql6);
		logger.info("删除报名表相关 - PS_TZ_APP_HIDDEN_T  " + del6);
		int del7 = sqlQuery.update(sql7);
		logger.info("删除报名表相关 - PS_TZ_APP_INS_T  " + del7);
		int del8 = sqlQuery.update(sql8);
		logger.info("删除报名表相关 - PS_TZ_FORM_WRK_T  " + del8);

		String ret = del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del5 + "    -->" + del6 + "    -->" + del7
				+ "    -->" + del8;
		return ret;
	}

	/**
	 * 初始化指定邮箱对应的账号密码为123456
	 * 
	 * @param mail
	 * @return
	 */
	public boolean changePassword(String mail) {
		String oprSql = "SELECT OPRID FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_EMAIL = ?";
		String oprId = sqlQuery.queryForObject(oprSql, new String[] { mail }, "String");

		Psoprdefn psoprdefn = new Psoprdefn();
		psoprdefn.setOprid(oprId);
		String password = DESUtil.encrypt("123456", "TZGD_Tranzvision");
		psoprdefn.setOperpswd(password);
		int change = psoprdefnMapper.updateByPrimaryKeySelective(psoprdefn);
		if (change > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除推荐信
	 * 
	 * @return
	 */
	public String delLetterAll() {

		String sql1 = "DELETE FROM PS_TZ_APP_CC_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql2 = "DELETE FROM PS_TZ_APP_DHCC_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql3 = "DELETE FROM PS_TZ_FORM_ATT_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql5 = "DELETE FROM PS_TZ_APP_DHHS_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql6 = "DELETE FROM PS_TZ_APP_HIDDEN_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql7 = "DELETE FROM PS_TZ_APP_INS_T WHERE cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";
		String sql8 = "DELETE FROM PS_TZ_KS_TJX_TBL WHERE  cast(TZ_APP_INS_ID as unsigned int) >= 200010 AND cast(TZ_APP_INS_ID as unsigned int) < 400000";

		int del1 = sqlQuery.update(sql1);
		logger.info("删除推荐信相关 - PS_TZ_APP_CC_T  " + del1);
		int del2 = sqlQuery.update(sql2);
		logger.info("删除推荐信相关 - PS_TZ_APP_DHCC_T  " + del2);
		int del3 = sqlQuery.update(sql3);
		logger.info("删除推荐信相关 - PS_TZ_FORM_ATT_T  " + del3);
		int del5 = sqlQuery.update(sql5);
		logger.info("删除推荐信相关 - PS_TZ_APP_DHHS_T  " + del5);
		int del6 = sqlQuery.update(sql6);
		logger.info("删除推荐信相关 - PS_TZ_APP_HIDDEN_T  " + del6);
		int del7 = sqlQuery.update(sql7);
		logger.info("删除推荐信相关 - PS_TZ_APP_INS_T  " + del7);
		int del8 = sqlQuery.update(sql8);
		logger.info("删除推荐信相关 - PS_TZ_KS_TJX_TBL  " + del8);

		String ret = del1 + "    -->" + del2 + "    -->" + del3 + "    -->" + del5 + "    -->" + del6 + "    -->" + del7
				+ "    -->" + del8;
		return ret;
	}

	/**
	 * 删除重复报名表
	 * 
	 * @return
	 */
	public String dInsRepeat() {
		String sql = "SELECT M.OPRID,M.TZ_APP_INS_ID FROM PS_TZ_APPINS_TBL M,(SELECT OPRID,COUNT(1) FROM PS_TZ_APPINS_TBL GROUP BY OPRID HAVING COUNT(1) > 1 ) C WHERE M.OPRID = C.OPRID ORDER BY OPRID DESC,cast(M.TZ_APP_INS_ID as unsigned int) DESC";
		List<?> resultlist = sqlQuery.queryForList(sql);
		String tmpOprid = "";
		String msg = "";

		for (Object obj : resultlist) {
			Map<String, Object> result = (Map<String, Object>) obj;
			String attrOprid = result.get("OPRID") == null ? "" : String.valueOf(result.get("OPRID"));
			String attrInsId = result.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(result.get("TZ_APP_INS_ID"));

			if (StringUtils.equals(tmpOprid, attrOprid)) {
				int del = sqlQuery.update("DELETE FROM PS_TZ_APPINS_TBL WHERE TZ_APP_INS_ID = ?",
						new Object[] { attrInsId });
				msg = msg + "<br>OPRID : " + attrOprid + "    -->  EMAIL : " + attrInsId + "   --> DELE :  = " + del;
			} else {
				tmpOprid = attrOprid;
			}
		}
		return msg;
	}

}