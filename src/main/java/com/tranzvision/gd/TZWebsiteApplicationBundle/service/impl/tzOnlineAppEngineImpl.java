package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.TZAuthBundle.service.impl.TzLoginServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.CreateTaskServiceImpl;
import com.tranzvision.gd.TZEmailSmsSendBundle.service.impl.SendSmsOrMalServiceImpl;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzLxfsInfoTblMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.dao.PsTzRegUserTMapper;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzLxfsInfoTbl;
import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzRegUserT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCcTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppCompTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhccTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppDhhsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppHiddenTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppInsTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzAppKsInExtTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormAttTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzFormWrkTMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.dao.PsTzKsTjxTblMapper;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCcT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppCompTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhccT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppDhhsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppHiddenT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppInsT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzAppKsInExtTbl;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormWrkT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzKsTjxTbl;
import com.tranzvision.gd.util.Calendar.DateUtil;
import com.tranzvision.gd.util.base.JacksonUtil;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.sql.GetSeqNum;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

@Service
public class tzOnlineAppEngineImpl {

	@Autowired
	private SqlQuery sqlQuery;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PsTzAppCompTblMapper psTzAppCompTblMapper;

	@Autowired
	private PsTzRegUserTMapper psTzRegUserTMapper;

	@Autowired
	private TZGDObject tzSQLObject;

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private tzOnlineAppHisServiceImpl tzOnlineAppHisServiceImpl;

	@Autowired
	private PsTzAppCcTMapper psTzAppCcTMapper;

	@Autowired
	private TzLoginServiceImpl tzLoginServiceImpl;

	@Autowired
	private PsTzAppInsTMapper psTzAppInsTMapper;

	@Autowired
	private PsTzAppDhccTMapper psTzAppDhccTMapper;

	@Autowired
	private PsTzFormAttTMapper psTzFormAttTMapper;

	@Autowired
	private PsTzLxfsInfoTblMapper psTzLxfsInfoTblMapper;

	@Autowired
	private GetSeqNum getSeqNum;

	@Autowired
	private PsTzFormWrkTMapper psTzFormWrkTMapper;

	@Autowired
	private PsTzKsTjxTblMapper psTzKsTjxTblMapper;

	@Autowired
	private PsTzAppDhhsTMapper psTzAppDhhsTMapper;

	@Autowired
	private PsTzAppHiddenTMapper psTzAppHiddenTMapper;

	@Autowired
	private CreateTaskServiceImpl createTaskServiceImpl;

	@Autowired
	private SendSmsOrMalServiceImpl sendSmsOrMalServiceImpl;

	@Autowired
	private PsTzAppKsInExtTblMapper psTzAppKsInExtTblMapper;

	@SuppressWarnings("unchecked")
	public String checkAppViewQx(String strTplId, String oprid, String orgid, String strClassId) {

		String strHasPermission = "";
		String sql = "";
		sql = "SELECT 'Y' FROM PS_TZ_APPTPL_R_T A,PSROLEUSER B WHERE A.ROLENAME = B.ROLENAME AND A.TZ_JG_ID = ? AND A.TZ_APP_TPL_ID = ? AND B.ROLEUSER = ?";
		strHasPermission = sqlQuery.queryForObject(sql, new Object[] { orgid, strTplId, oprid }, "String");
		// 对当前模版是否有访问权限
		if ("".equals(strHasPermission) || strHasPermission == null) {
			// 是否材料评审评委
			sql = "SELECT 'Y' FROM PS_TZ_JUSR_REL_TBL WHERE OPRID = ?";
			strHasPermission = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");

			if ("".equals(strHasPermission) || strHasPermission == null) {
				// 是否是班级管理人员
				sql = "SELECT 'Y' FROM PS_TZ_CLS_ADMIN_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				strHasPermission = sqlQuery.queryForObject(sql, new Object[] { strClassId, oprid }, "String");
			}
		}

		if (strHasPermission == null)
			strHasPermission = "";

		return strHasPermission;

	}

	// 检查推荐信对应页面完成状态
	public void checkRefletter(Long numAppInsId, String strTplId) {

		String strMsg = "";
		/* 信息项编号 */
		String strXxxBh = "";

		/* 信息项名称 */
		String strXxxMc = "";

		/* 控件类名称 */
		String strComMc = "";

		/* 分页号 */
		int numPageNo;

		/* 信息项日期格式 */
		String strXxxRqgs = "";

		/* 信息项日期年份最小值 */
		String strXxxXfmin = "";

		/* 信息项日期年份最大值 */
		String strXxxXfmax = "";

		/* 信息项多选最少选择数量 */
		String strXxxZsxzgs = "";

		/* 信息项多选最多选择数量 */
		String strXxxZdxzgs = "";

		/* 信息项文件允许上传类型 */
		String strXxxYxsclx = "";

		/* 信息项文件允许上传大小 */
		String strXxxYxscdx = "";

		/* 信息项是否必填 */
		String strXxxBtBz = "";

		/* 信息项是否启用字数范围 */
		String strXxxCharBz = "";

		/* 信息项字数最小长度 */
		int numXxxMinlen;

		/* 信息项字数最大长度 */
		long numXxxMaxlen;

		/* 信息项是否启用数字范围 */
		String strXxxNumBz = "";

		/* 信息项字数最小长度 */
		int numXxxMin;

		/* 信息项字数最大长度 */
		long numXxxMax;

		/* 信息项字段小数位数 */
		String strXxxXsws = "";

		/* 信息项字段固定格式校验 */
		String strXxxGdgsjy = "";

		/* 信息项字段是否多容器 */
		String strXxxDrqBz = "";

		/* 信息项最小行记录数 */
		int numXxxMinLine;

		/* 信息项最大行记录数 */
		int numXxxMaxLine;

		/* 推荐信收集齐前是否允许提交报名表 */
		String strTjxSub = "";

		/* 信息项校验规则 */
		// String strJygzId;

		String strJygzTsxx;

		/* 信息项校验程序 */
		String strPath, strName, strMethod;

		String strPageXxxBh = "";

		String strPageNo = "";

		try {

			String sqlGetPageNo = "SELECT TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = 'recommendletter' LIMIT 1";

			strPageNo = sqlQuery.queryForObject(sqlGetPageNo, new Object[] { strTplId }, "String");

			if (strPageNo != null) {
				numPageNo = Integer.parseInt(strPageNo);
				String sqlGetPageXxxBh = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_PAGE_NO = ? AND TZ_COM_LMC = 'Page' LIMIT 1";
				strPageXxxBh = sqlQuery.queryForObject(sqlGetPageXxxBh, new Object[] { strTplId, numPageNo }, "String");
				if (strPageXxxBh == null)
					strPageXxxBh = "";
				String sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_ONLINE_CHECK_BYPAGE_SQL");
				List<?> listData = sqlQuery.queryForList(sql, new Object[] { strTplId, numPageNo });
				Map<String, Object> MapData = null;
				tzOnlineAppUtility tzOnlineAppUtility = null;
				for (Object objData : listData) {
					MapData = (Map<String, Object>) objData;
					strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
					strXxxMc = MapData.get("TZ_XXX_MC") == null ? "" : String.valueOf(MapData.get("TZ_XXX_MC"));
					strComMc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
					strXxxRqgs = MapData.get("TZ_XXX_RQGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_RQGS"));
					strXxxXfmin = MapData.get("TZ_XXX_NFMIN") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_NFMIN"));
					strXxxXfmax = MapData.get("TZ_XXX_NFMAX") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_NFMAX"));
					strXxxZsxzgs = MapData.get("TZ_XXX_ZSXZGS") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_ZSXZGS"));
					strXxxZdxzgs = MapData.get("TZ_XXX_ZDXZGS") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_ZDXZGS"));
					strXxxYxsclx = MapData.get("TZ_XXX_YXSCLX") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_YXSCLX"));
					strXxxYxscdx = MapData.get("TZ_XXX_YXSCDX") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_YXSCDX"));
					strXxxBtBz = MapData.get("TZ_XXX_BT_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BT_BZ"));
					strXxxCharBz = MapData.get("TZ_XXX_CHAR_BZ") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_CHAR_BZ"));
					numXxxMinlen = MapData.get("TZ_XXX_MINLEN") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MINLEN")));
					numXxxMaxlen = MapData.get("TZ_XXX_MAXLEN") == null ? 0
							: Long.parseLong(String.valueOf(MapData.get("TZ_XXX_MAXLEN")));
					strXxxNumBz = MapData.get("TZ_XXX_NUM_BZ") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_NUM_BZ"));
					numXxxMin = MapData.get("TZ_XXX_MIN") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN")));
					numXxxMax = MapData.get("TZ_XXX_MAX") == null ? 0
							: Long.parseLong(String.valueOf(MapData.get("TZ_XXX_MAX")));
					strXxxXsws = MapData.get("TZ_XXX_XSWS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_XSWS"));
					strXxxGdgsjy = MapData.get("TZ_XXX_GDGSJY") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_GDGSJY"));
					strXxxDrqBz = MapData.get("TZ_XXX_DRQ_BZ") == null ? ""
							: String.valueOf(MapData.get("TZ_XXX_DRQ_BZ"));
					numXxxMinLine = MapData.get("TZ_XXX_MIN_LINE") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN_LINE")));
					numXxxMaxLine = MapData.get("TZ_XXX_MAX_LINE") == null ? 0
							: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAX_LINE")));
					strTjxSub = MapData.get("TZ_TJX_SUB") == null ? "" : String.valueOf(MapData.get("TZ_TJX_SUB"));
					strPath = MapData.get("TZ_APPCLS_PATH") == null ? ""
							: String.valueOf(MapData.get("TZ_APPCLS_PATH"));
					strName = MapData.get("TZ_APPCLS_NAME") == null ? ""
							: String.valueOf(MapData.get("TZ_APPCLS_NAME"));
					strMethod = MapData.get("TZ_APPCLS_METHOD") == null ? ""
							: String.valueOf(MapData.get("TZ_APPCLS_METHOD"));
					strJygzTsxx = MapData.get("TZ_JYGZ_TSXX") == null ? ""
							: String.valueOf(MapData.get("TZ_JYGZ_TSXX"));

					if (!"".equals(strPath) && !"".equals(strName) && !"".equals(strMethod)) {
						tzOnlineAppUtility = (tzOnlineAppUtility) ctx.getBean(strPath + "." + strName);
						String strReturn = "";
						switch (strMethod) {
						case "requireValidator":
							strReturn = tzOnlineAppUtility.requireValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "ahphValidator":
							strReturn = tzOnlineAppUtility.ahphValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "charLenValidator":
							strReturn = tzOnlineAppUtility.charLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "valueValidator":
							strReturn = tzOnlineAppUtility.valueValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "regularValidator":
							strReturn = tzOnlineAppUtility.regularValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "dHLineValidator":
							strReturn = tzOnlineAppUtility.dHLineValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "refLetterValidator":
							strReturn = tzOnlineAppUtility.refLetterValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "rowLenValidator":
							strReturn = tzOnlineAppUtility.rowLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						}
						if (!"".equals(strReturn)) {
							strMsg = strReturn;
							break;
						}
					}
				}
				if (!"".equals(strPageXxxBh)) {
					if ("".equals(strMsg)) {
						/* 是否存在未发送推荐信的推荐人信息 */
						String sqlGetRefLetterCount = "SELECT COUNT(*) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME = '' OR A.ATTACHUSERFILE = ' ') AND NOT EXISTS (SELECT * FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U')) "
								+ " AND A.TZ_APP_INS_ID = ? AND A.TZ_MBA_TJX_YX = 'Y' AND TZ_REFLETTERTYPE = ''";
						Integer numRefletter = sqlQuery.queryForObject(sqlGetRefLetterCount,
								new Object[] { numAppInsId }, "Integer");
						if (numRefletter > 0) {
							this.savePageCompleteState(numAppInsId, strPageXxxBh, "N");
						} else {
							this.savePageCompleteState(numAppInsId, strPageXxxBh, "Y");
						}
						/* 如果推荐人信息没有填写 */
						String sqlGetRefLetterCount2 = "SELECT COUNT(*) FROM PS_TZ_KS_TJX_TBL A WHERE A.TZ_APP_INS_ID = ? AND A.TZ_MBA_TJX_YX = 'Y'";
						Integer numRefletter2 = sqlQuery.queryForObject(sqlGetRefLetterCount2,
								new Object[] { numAppInsId }, "Integer");
						if (numRefletter2 == 0) {
							this.savePageCompleteState(numAppInsId, strPageXxxBh, "B");
						}
					} else {
						this.savePageCompleteState(numAppInsId, strPageXxxBh, "N");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 更新页面完成状态
	public void savePageCompleteState(Long numAppInsId, String strXxxBh, String strPageCompleteState) {

		String sql = "SELECT COUNT(1) FROM PS_TZ_APP_COMP_TBL WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
		int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strXxxBh }, "Integer");
		if (count > 0) {
			PsTzAppCompTbl psTzAppCompTbl = new PsTzAppCompTbl();
			psTzAppCompTbl.setTzAppInsId(numAppInsId);
			psTzAppCompTbl.setTzXxxBh(strXxxBh);
			psTzAppCompTbl.setTzHasComplete(strPageCompleteState);
			psTzAppCompTblMapper.updateByPrimaryKeySelective(psTzAppCompTbl);
		} else {
			PsTzAppCompTbl psTzAppCompTbl = new PsTzAppCompTbl();
			psTzAppCompTbl.setTzAppInsId(numAppInsId);
			psTzAppCompTbl.setTzXxxBh(strXxxBh);
			psTzAppCompTbl.setTzHasComplete(strPageCompleteState);
			psTzAppCompTblMapper.insert(psTzAppCompTbl);
		}
	}

	public Map<String, String> getHistoryOnlineApp(String strAppInsId, String strCopyFrom, String strAppOprId,
			String strAppOrgId, String strTplId, String oprid, String strClassId, String strRefLetterId,
			String strInsData) {
		String strHisAppInsId = "";

		Long numHisAppInsId = 0l;

		if ("".equals(strAppInsId) || strAppInsId == null) {
			if ("Y".equals(strCopyFrom)) {
				String sqlGetHisAppInsId = "SELECT TZ_APP_INS_ID FROM PS_TZ_FORM_WRK_T A ,PS_TZ_CLASS_INF_T B "
						+ "WHERE A.TZ_CLASS_ID = B.TZ_CLASS_ID AND A.OPRID = ? AND B.TZ_JG_ID = ? ORDER BY A.ROW_ADDED_DTTM DESC limit 0,1";
				strHisAppInsId = sqlQuery.queryForObject(sqlGetHisAppInsId, new Object[] { strAppOprId, strAppOrgId },
						"String");
				if (!"".equals(strHisAppInsId) && strHisAppInsId != null) {
					numHisAppInsId = Long.parseLong(strHisAppInsId);
					strInsData = tzOnlineAppHisServiceImpl.getHisAppInfoJson(numHisAppInsId, strTplId);
					// -------------复制推荐信

					// ---0.去年的推荐信不为空，则进行“复制推荐信”操作
					final String LAST_LETTER_SQL = "SELECT * FROM PS_TZ_KS_TJX_TBL WHERE TZ_APP_INS_ID =?";
					List<Map<String, Object>> letterList = new ArrayList<Map<String, Object>>();
					letterList = sqlQuery.queryForList(LAST_LETTER_SQL, new Object[] { numHisAppInsId });
					// 将所有的推荐信 信息复制过去 考生推荐信表
					if (letterList != null) {
						// ---1.向"报名表实例表"，加入报名表实例信息PS_TZ_APP_INS_T
						Long tzAppInsId = Long.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID"));
						PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
						psTzAppInsT.setTzAppInsId(tzAppInsId);// 报名表实例ID
																// 新生成的
						psTzAppInsT.setRowAddedOprid(oprid); // 当前用户ID
						psTzAppInsT.setRowAddedDttm(new Date());
						psTzAppInsT.setRowLastmantDttm(new Date());
						psTzAppInsT.setTzAppTplId(strTplId);// 当前报名表模板ID
						psTzAppInsT.setTzAppinsJsonStr(strInsData);// 从历史表中即将推送到前台的json，存入报名表实例表中
						psTzAppInsTMapper.insertSelective(psTzAppInsT);

						// ---2.将"班级"和"报名表"关系数据 存入 "PS_TZ_FORM_WRK_T"
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(strClassId);// 班级ID
						psTzFormWrkT.setOprid(oprid);// 当前用户
						psTzFormWrkT.setTzAppInsId(tzAppInsId);// 报名表实例ID
						psTzFormWrkT.setRowAddedDttm(new Date());
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkTMapper.insertSelective(psTzFormWrkT);
						String sql = "update PS_TZ_REG_USER_T SET TZ_ALLOW_APPLY='N' where OPRID =?";
						sqlQuery.update(sql, new Object[] { oprid });
						// ////System.out.println("将班级和报名表关系数据 存入 ");

						// ---3.向推荐信相关表中，加入”推荐信“信息 "PS_TZ_KS_TJX_TBL"
						// ////System.out.println(letterList.size());
						Map<String, Object> letterMap = null;
						System.out.println("有" + letterList.size() + "封推荐信");
						for (int i = 0; i < letterList.size(); i++) {
							// ////System.out.println("推荐信读取");
							letterMap = letterList.get(i);

							String str_seq1 = String.valueOf((int) (Math.random() * 10000000));
							String str_seq2 = "00000000000000"
									+ String.valueOf(getSeqNum.getSeqNum("TZ_KS_TJX_TBL", "TZ_REF_LETTER_ID"));
							str_seq2 = str_seq2.substring(str_seq2.length() - 15, str_seq2.length());
							String tzRefLetterId = str_seq1 + str_seq2;
							// String tzRefLetterId = String
							// .valueOf(getSeqNum.getSeqNum("TZ_KS_TJX_TBL",
							// "TZ_REF_LETTER_ID"));
							PsTzKsTjxTbl psTzKsTjxTbl = new PsTzKsTjxTbl();
							psTzKsTjxTbl.setTzRefLetterId(tzRefLetterId);// 主键
																			// 推荐信ID
							psTzKsTjxTbl.setTzAppInsId(tzAppInsId);// 关联报名表
																	// 实例ID
							Long tzTjxAppInsId = Long.valueOf(getSeqNum.getSeqNum("TZ_APP_INS_T", "TZ_APP_INS_ID"));
							psTzKsTjxTbl.setTzTjxAppInsId(tzTjxAppInsId); // 推荐信在
																			// 报名表实例表中的实例ID
																			// 报名表编号
							psTzKsTjxTbl.setOprid(oprid);
							if (letterMap.get("TZ_TJX_TYPE") != null) {
								psTzKsTjxTbl.setTzTjxType(String.valueOf(letterMap.get("TZ_TJX_TYPE")));
							}
							if (letterMap.get("TZ_REFLETTERTYPE") != null) {
								psTzKsTjxTbl.setTzReflettertype(String.valueOf(letterMap.get("TZ_REFLETTERTYPE")));
							}
							if (letterMap.get("TZ_TJR_ID") != null) {
								psTzKsTjxTbl.setTzTjrId(String.valueOf(letterMap.get("TZ_TJR_ID")));
							}
							if (letterMap.get("TZ_MBA_TJX_YX") != null) {
								psTzKsTjxTbl.setTzMbaTjxYx(String.valueOf(letterMap.get("TZ_MBA_TJX_YX")));
							}
							if (letterMap.get("TZ_REFERRER_NAME") != null) {
								psTzKsTjxTbl.setTzReferrerName(String.valueOf(letterMap.get("TZ_REFERRER_NAME")));
							}
							if (letterMap.get("TZ_REFERRER_GNAME") != null) {
								psTzKsTjxTbl.setTzReferrerGname(String.valueOf(letterMap.get("TZ_REFERRER_GNAME")));
							}
							if (letterMap.get("TZ_COMP_CNAME") != null) {
								psTzKsTjxTbl.setTzCompCname(String.valueOf(letterMap.get("TZ_COMP_CNAME")));
							}
							if (letterMap.get("TZ_POSITION") != null) {
								psTzKsTjxTbl.setTzPosition(String.valueOf(letterMap.get("TZ_POSITION")));
							}
							if (letterMap.get("TZ_TJX_TITLE") != null) {
								psTzKsTjxTbl.setTzTjxTitle(String.valueOf(letterMap.get("TZ_TJX_TITLE")));
							}
							if (letterMap.get("TZ_TJR_GX") != null) {
								psTzKsTjxTbl.setTzTjrGx(String.valueOf(letterMap.get("TZ_TJR_GX")));
							}
							if (letterMap.get("TZ_EMAIL") != null) {
								psTzKsTjxTbl.setTzEmail(String.valueOf(letterMap.get("TZ_EMAIL")));
							}
							if (letterMap.get("TZ_PHONE_AREA") != null) {
								psTzKsTjxTbl.setTzPhoneArea(String.valueOf(letterMap.get("TZ_PHONE_AREA")));
							}
							if (letterMap.get("TZ_PHONE") != null) {
								psTzKsTjxTbl.setTzPhone(String.valueOf(letterMap.get("TZ_PHONE")));
							}
							if (letterMap.get("TZ_GENDER") != null) {
								psTzKsTjxTbl.setTzGender(String.valueOf(letterMap.get("TZ_GENDER")));
							}
							if (letterMap.get("TZ_TJX_YL_1") != null) {
								psTzKsTjxTbl.setTzTjxYl1(String.valueOf(letterMap.get("TZ_TJX_YL_1")));
							}
							if (letterMap.get("TZ_TJX_YL_2") != null) {
								psTzKsTjxTbl.setTzTjxYl2(String.valueOf(letterMap.get("TZ_TJX_YL_2")));
							}
							if (letterMap.get("TZ_TJX_YL_3") != null) {
								psTzKsTjxTbl.setTzTjxYl3(String.valueOf(letterMap.get("TZ_TJX_YL_3")));
							}
							if (letterMap.get("TZ_TJX_YL_4") != null) {
								psTzKsTjxTbl.setTzTjxYl4(String.valueOf(letterMap.get("TZ_TJX_YL_4")));
							}
							if (letterMap.get("TZ_TJX_YL_5") != null) {
								psTzKsTjxTbl.setTzTjxYl5(String.valueOf(letterMap.get("TZ_TJX_YL_5")));
							}
							if (letterMap.get("TZ_TJX_YL_6") != null) {
								psTzKsTjxTbl.setTzTjxYl6(String.valueOf(letterMap.get("TZ_TJX_YL_6")));
							}
							if (letterMap.get("TZ_TJX_YL_7") != null) {
								psTzKsTjxTbl.setTzTjxYl7(String.valueOf(letterMap.get("TZ_TJX_YL_7")));
							}
							if (letterMap.get("TZ_TJX_YL_8") != null) {
								psTzKsTjxTbl.setTzTjxYl8(String.valueOf(letterMap.get("TZ_TJX_YL_8")));
							}
							if (letterMap.get("TZ_TJX_YL_9") != null) {
								psTzKsTjxTbl.setTzTjxYl9(String.valueOf(letterMap.get("TZ_TJX_YL_9")));
							}
							if (letterMap.get("TZ_TJX_YL_10") != null) {
								psTzKsTjxTbl.setTzTjxYl10(String.valueOf(letterMap.get("TZ_TJX_YL_10")));
							}
							if (letterMap.get("TZ_ATT_A_URL") != null) {
								psTzKsTjxTbl.setTzAttAUrl(String.valueOf(letterMap.get("TZ_ATT_A_URL")));
							}
							if (letterMap.get("ATTACHSYSFILENAME") != null) {
								psTzKsTjxTbl.setAttachsysfilename(String.valueOf(letterMap.get("ATTACHSYSFILENAME")));
							}
							if (letterMap.get("ATTACHUSERFILE") != null) {
								psTzKsTjxTbl.setAttachuserfile(String.valueOf(letterMap.get("ATTACHUSERFILE")));
							}
							psTzKsTjxTbl.setRowAddedDttm(new Date());
							psTzKsTjxTbl.setRowAddedOprid(oprid);
							psTzKsTjxTbl.setRowLastmantDttm(new Date());
							psTzKsTjxTbl.setRowLastmantOprid(oprid);
							if (letterMap.get("SYNCID") != null && letterMap.get("SYNCID") != "") {
								psTzKsTjxTbl.setSyncid(Integer.valueOf(String.valueOf(letterMap.get("SYNCID"))));
							}
							psTzKsTjxTbl.setSyncdttm(new Date());
							if (letterMap.get("TZ_ACCESS_PATH") != null) {
								psTzKsTjxTbl.setTzAccessPath(String.valueOf(letterMap.get("TZ_ACCESS_PATH")));
							}
							psTzKsTjxTblMapper.insertSelective(psTzKsTjxTbl);
							// ////System.out.println("考生推荐信 信息存入");
							// ---3.2.向“报名表实例表”中加入”推荐信“实例信息 （这表结构设计的有毒）
							// 将“原报名表”关联的“推荐信实例ID”拿出来
							String LAST_LETTER_INS_ID = letterMap.get("TZ_TJX_APP_INS_ID") == null ? ""
									: letterMap.get("TZ_TJX_APP_INS_ID").toString();
							// 已经确认 只有唯一推荐信实例？TZ_APP_FORM_STA='U'表示已提交
							System.out.println("tzTjxAppInsId：" + tzTjxAppInsId);
							System.out.println("LAST_LETTER_INS_ID：" + LAST_LETTER_INS_ID);
							psTzAppInsT = null;

							if (LAST_LETTER_INS_ID != null && !LAST_LETTER_INS_ID.equals("")) {
								psTzAppInsT = psTzAppInsTMapper.selectByPrimaryKey(new Long(LAST_LETTER_INS_ID));
							}

							if (psTzAppInsT != null) {
								PsTzAppInsT psTzAppInsT2 = new PsTzAppInsT();
								psTzAppInsT2.setTzAppInsId(tzTjxAppInsId);// 报名表实例ID
								// 新生成的
								psTzAppInsT2.setRowAddedOprid(oprid); // 当前用户ID
								psTzAppInsT2.setRowLastmantOprid(oprid);
								psTzAppInsT2.setRowAddedDttm(new Date());
								psTzAppInsT2.setRowLastmantDttm(new Date());
								psTzAppInsT2.setTzAppTplId(psTzAppInsT.getTzAppTplId());// 历史推荐信模板ID
								psTzAppInsT2.setTzAppinsJsonStr(psTzAppInsT.getTzAppinsJsonStr());// 历史推荐信字符串信息
								// 新产生的推荐信ID"已提交"状态的推荐信ID 推入前台
								String submitState = psTzAppInsT.getTzAppFormSta();
								if (submitState != null && submitState.equals("U")) {
									strRefLetterId = tzRefLetterId;
								}
								psTzAppInsT2.setTzAppFormSta(submitState);
								psTzAppInsT2.setTzPwd(psTzAppInsT.getTzPwd());
								psTzAppInsTMapper.insertSelective(psTzAppInsT2);

								// 增加PS_TZ_APP_CC_T PS_TZ_APP_DHCC_T
								// PS_TZ_FORM_ATT_T PS_TZ_APP_DHHS_T
								// PS_TZ_APP_HIDDEN_T
								final String addTZ_APP_CC_T = "insert into PS_TZ_APP_CC_T select ?,TZ_XXX_BH,TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_APP_L_TEXT from PS_TZ_APP_CC_T where TZ_APP_INS_ID=?";
								final String addTZ_APP_DHCC_T = "insert into PS_TZ_APP_DHCC_T select ?,TZ_XXX_BH,TZ_XXXKXZ_MC,TZ_APP_S_TEXT,TZ_KXX_QTZ,TZ_IS_CHECKED from PS_TZ_APP_DHCC_T where TZ_APP_INS_ID=?";
								final String addTZ_FORM_ATT_T = "insert into PS_TZ_FORM_ATT_T select ?,TZ_XXX_BH,TZ_INDEX,ATTACHSYSFILENAME,ATTACHUSERFILE,ROW_ADDED_DTTM,ROW_ADDED_OPRID,ROW_LASTMANT_DTTM,ROW_LASTMANT_OPRID,SYNCID,SYNCDTTM,TZ_ACCESS_PATH from PS_TZ_FORM_ATT_T where TZ_APP_INS_ID=?";
								final String addTZ_APP_DHHS_T = "insert into PS_TZ_APP_DHHS_T select ?,TZ_XXX_BH,TZ_XXX_LINE from PS_TZ_APP_DHHS_T where TZ_APP_INS_ID=?";
								final String addTZ_APP_HIDDEN_T = "insert into PS_TZ_APP_HIDDEN_T select ?,TZ_XXX_BH,TZ_IS_HIDDEN from PS_TZ_APP_HIDDEN_T where TZ_APP_INS_ID=?";

								// System.out.println(addTZ_APP_CC_T);
								// System.out.println(addTZ_APP_DHCC_T);
								// System.out.println(addTZ_FORM_ATT_T);
								// System.out.println(addTZ_APP_DHHS_T);
								// System.out.println(addTZ_APP_HIDDEN_T);

								sqlQuery.update(addTZ_APP_CC_T, new Object[] { tzTjxAppInsId, LAST_LETTER_INS_ID });
								sqlQuery.update(addTZ_APP_DHCC_T, new Object[] { tzTjxAppInsId, LAST_LETTER_INS_ID });
								sqlQuery.update(addTZ_FORM_ATT_T, new Object[] { tzTjxAppInsId, LAST_LETTER_INS_ID });
								sqlQuery.update(addTZ_APP_DHHS_T, new Object[] { tzTjxAppInsId, LAST_LETTER_INS_ID });
								sqlQuery.update(addTZ_APP_HIDDEN_T, new Object[] { tzTjxAppInsId, LAST_LETTER_INS_ID });

							}

							// final String SQL_LETTER_INS = "SELECT * FROM
							// PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID=?";
							// if (!LAST_LETTER_INS_ID.equals("")) {
							// List<Map<String, Object>> letterInsList =
							// sqlQuery.queryForList(SQL_LETTER_INS,
							// new Object[] { LAST_LETTER_INS_ID });
							// if (letterInsList != null) {
							// for (int k = 0; k < letterInsList.size(); k++) {
							// Map<String, Object> letterInsMap =
							// letterInsList.get(k);
							// PsTzAppInsT psTzAppInsT2 = new PsTzAppInsT();
							// psTzAppInsT2.setTzAppInsId(tzTjxAppInsId);//
							// 报名表实例ID
							// // 新生成的
							// psTzAppInsT2.setRowAddedOprid(oprid); // 当前用户ID
							// psTzAppInsT2.setRowLastmantOprid(oprid);
							// psTzAppInsT2.setRowAddedDttm(new Date());
							// psTzAppInsT2.setRowLastmantDttm(new Date());
							// psTzAppInsT2.setTzAppTplId(letterInsMap.get("TZ_APP_TPL_ID").toString());//
							// 历史推荐信模板ID
							// psTzAppInsT2
							// .setTzAppinsJsonStr(letterInsMap.get("TZ_APPINS_JSON_STR").toString());//
							// 历史推荐信字符串信息
							// // 新产生的推荐信ID"已提交"状态的推荐信ID 推入前台
							// String submitState =
							// letterInsMap.get("TZ_APP_FORM_STA") == null ? ""
							// : letterInsMap.get("TZ_APP_FORM_STA").toString();
							// if (submitState.equals("U")) {
							// strRefLetterId = tzRefLetterId;
							// }
							// psTzAppInsT2.setTzAppFormSta(submitState);
							// String tzPwd = letterInsMap.get("TZ_PWD") == null
							// ? ""
							// : letterInsMap.get("TZ_PWD").toString();
							// psTzAppInsT2.setTzPwd(tzPwd);
							// psTzAppInsTMapper.insertSelective(psTzAppInsT2);
							// }
							// }
							// }

						}
						// ---4.将新产生的"报名表实例ID"放入 前端html,即：strAppInsId,
						strAppInsId = String.valueOf(tzAppInsId);
					}
					// -----------------复制推荐信 代码结束
				}
			}
		}

		Map<String, String> m = new HashMap<String, String>();
		m.put("strAppInsId", strAppInsId);
		m.put("strInsData", strInsData);
		m.put("strRefLetterId", strRefLetterId);

		return m;
	}

	public String getUserInfo(String strAppInsId, String strTplType, String strSiteId) {

		// 当前登陆人
		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String orgid = tzLoginServiceImpl.getLoginedManagerOrgid(request);
		String strField = "";
		String strFieldValue = "";
		String strUserInfo = "";
		JacksonUtil jacksonUtil = new JacksonUtil();
		Map<String, Object> map = new HashMap<String, Object>();

		// System.out.println("orgid:" + orgid);
		// System.out.println("oprid:" + oprid);
		String sqlGetField = "SELECT TZ_REG_FIELD_ID FROM PS_TZ_REG_FIELD_T WHERE TZ_JG_ID = ? AND TZ_SITEI_ID = ? ORDER BY TZ_ORDER";
		List<?> listData = sqlQuery.queryForList(sqlGetField, new Object[] { orgid, strSiteId });
		String sql = "";
		Map<String, Object> mapData = null;
		for (Object objData : listData) {
			strFieldValue = "";
			mapData = (Map<String, Object>) objData;
			strField = mapData.get("TZ_REG_FIELD_ID") == null ? "" : String.valueOf(mapData.get("TZ_REG_FIELD_ID"));
			if ("TZ_PASSWORD".equals(strField) || "TZ_REPASSWORD".equals(strField)) {
				continue;
			}
			;
			try {
				if (oprid != null && !oprid.equals("")) {
					if ("TZ_SKYPE".equals(strField) || "TZ_MOBILE".equals(strField) || "TZ_EMAIL".equals(strField)) {
						if ("TZ_MOBILE".equals(strField)) {
							sql = "SELECT TZ_ZY_SJ FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZCYH' AND TZ_LYDX_ID = ?";
							strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
						} else if ("TZ_EMAIL".equals(strField)) {
							sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZCYH' AND TZ_LYDX_ID = ?";
							strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
						} else {
							sql = "SELECT TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZCYH' AND TZ_LYDX_ID = ?";
							strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
						}
					} else {
						if ("TZ_REALNAME".equals(strField)) {
							sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE TZ_JG_ID =? AND OPRID = ?";
							strFieldValue = sqlQuery.queryForObject(sql, new Object[] { orgid, oprid }, "String");
						} else {
							// 项目字段没对应;
							if ("TZ_PROJECT".equals(strField)) {
								sql = "SELECT TZ_PRJ_ID FROM PS_TZ_REG_USER_T WHERE OPRID =?";
								strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
							} else {
								sql = "SELECT " + strField + " FROM PS_TZ_REG_USER_T WHERE OPRID = ?";
								strFieldValue = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
							}
						}
					}
				}
				if (strFieldValue == null)
					strFieldValue = "";

				map.put(strField, strFieldValue);

			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		// 插入面试申请号码, 推荐信 不需要登录，所以没有oprid
		//// //System.out.println("strAppInsId:" + strAppInsId);
		String TZ_MSH_ID = "";
		//// //System.out.println("oprid:" + oprid);
		if (strTplType.equals("TJX")) {
			sql = "SELECT A.TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL A,PS_TZ_KS_TJX_TBL B WHERE A.OPRID=B.OPRID AND B.TZ_TJX_APP_INS_ID=?";
			TZ_MSH_ID = sqlQuery.queryForObject(sql, new Object[] { strAppInsId }, "String");
			if (TZ_MSH_ID == null) {
				TZ_MSH_ID = "";
			}
		} else {
			// 首次填写报名表
			if (strAppInsId == null || strAppInsId.equals("") || strAppInsId.equals("0")) {
				sql = "SELECT TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				TZ_MSH_ID = sqlQuery.queryForObject(sql, new Object[] { oprid }, "String");
				if (TZ_MSH_ID == null) {
					TZ_MSH_ID = "";
				}
			} else {
				sql = "SELECT A.TZ_MSH_ID FROM PS_TZ_AQ_YHXX_TBL A,PS_TZ_FORM_WRK_T B WHERE A.OPRID=B.OPRID AND B.TZ_APP_INS_ID=?";
				TZ_MSH_ID = sqlQuery.queryForObject(sql, new Object[] { strAppInsId }, "String");
				if (TZ_MSH_ID == null) {
					TZ_MSH_ID = "";
				}
			}
		}
		//// //System.out.println("TZ_MSH_ID:" + TZ_MSH_ID);
		map.put("TZ_MSH_ID", TZ_MSH_ID);

		strUserInfo = jacksonUtil.Map2json(map);

		return strUserInfo;
	}

	// 报名表预提交
	@SuppressWarnings("unchecked")
	public String preAppForm(Long numAppInsId) {
		String returnMsg = "";
		PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
		psTzAppInsT.setTzAppInsId(numAppInsId);
		psTzAppInsT.setTzAppPreSta("P");

		psTzAppInsT.setRowLastmantDttm(new Date());

		psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
		return returnMsg;
	}

	// 报名表保存
	@SuppressWarnings("unchecked")
	public String saveAppForm(String strTplId, Long numAppInsId, String strClassId, String strAppOprId,
			String strJsonData, String strTplType, String strIsGuest, String strAppInsVersion, String strAppInsState,
			String strBathId, String newClassId, String pwd, String strOtype, String isPwd, String strTjxId,Boolean isMobile) {

		String returnMsg = "";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		// Long numAppInsId = Long.parseLong(strAppInsId);
		try {
			String sql = "";
			// int count = 0;
			// String TZ_APP_FORM_STA = null;
			String INS_ID = null;
			boolean chageClass = false;

			String clientType="";
			if(isMobile){
				clientType="M";
			}else{
				clientType="P";
			}
			if (newClassId != null && !newClassId.equals(strClassId)) {
				chageClass = true;
			}

			// modity by caoy 保存的时候，如果是 预提交状态 那么状态不改变
			// 更换班级 需要变更 报名表和WOrk表 只需要保存一份实例
			sql = "SELECT TZ_APP_INS_ID,TZ_APP_FORM_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			// count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId
			// }, "Integer");
			Map<String, Object> mapData = null;

			mapData = sqlQuery.queryForMap(sql, new Object[] { numAppInsId });

			if (mapData != null) {
				// TZ_APP_FORM_STA = mapData.get("TZ_APP_FORM_STA") == null ? ""
				// : String.valueOf(mapData.get("TZ_APP_FORM_STA"));

				INS_ID = mapData.get("TZ_APP_INS_ID") == null ? "" : String.valueOf(mapData.get("TZ_APP_INS_ID"));
			}

			// System.out.println("TZ_APPINS_JSON_STR:" + TZ_APP_FORM_STA);
			// System.out.println("strOtype:" + strOtype);
			// if (count > 0) {
			if (INS_ID != null && !INS_ID.equals("") && Long.parseLong(INS_ID) > 0) {
				PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
				psTzAppInsT.setTzAppInsId(numAppInsId);
				psTzAppInsT.setTzAppTplId(strTplId);
				psTzAppInsT.setTzAppInsVersion(strAppInsVersion);

				// if (strOtype.equals("PRE")) {
				// psTzAppInsT.setTzAppFormSta("P");
				// } else {
				// if (TZ_APP_FORM_STA.equals("P") &&
				// strAppInsState.equals("S")) {
				//
				// } else {
				psTzAppInsT.setTzAppFormSta(strAppInsState);
				// }
				// }
				psTzAppInsT.setTzAppinsJsonStr(strJsonData);
				psTzAppInsT.setRowLastmantOprid(oprid);
				psTzAppInsT.setRowLastmantDttm(new Date());
				psTzAppInsT.setTzAppSubMed(clientType);
				if (isPwd != null && isPwd.equals("Y")) {
					psTzAppInsT.setTzPwd(pwd);
				}
				psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
			} else {
				PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
				psTzAppInsT.setTzAppInsId(numAppInsId);
				psTzAppInsT.setTzAppTplId(strTplId);
				psTzAppInsT.setTzAppInsVersion(strAppInsVersion);
				// if (strOtype.equals("PRE")) {
				// psTzAppInsT.setTzAppFormSta("P");
				// } else {
				psTzAppInsT.setTzAppFormSta(strAppInsState);
				// }
				psTzAppInsT.setTzAppinsJsonStr(strJsonData);
				psTzAppInsT.setRowAddedOprid(oprid);
				psTzAppInsT.setRowAddedDttm(new Date());
				psTzAppInsT.setRowLastmantOprid(oprid);
				psTzAppInsT.setRowLastmantDttm(new Date());
				if (isPwd != null && isPwd.equals("Y")) {
					psTzAppInsT.setTzPwd(pwd);
				}
				psTzAppInsT.setTzAppCreMed(clientType);
				psTzAppInsTMapper.insert(psTzAppInsT);
			}

			if ("BMB".equals(strTplType)) {

				// 创建报名表的时候，需要将考生的是否允许继续申请状态设置成否
				sql = "update PS_TZ_REG_USER_T set TZ_ALLOW_APPLY = 'N' where OPRID=?";
				sqlQuery.update(sql, new Object[] { strAppOprId });
				int count = 0;
				// 如果是 变更班级，那么
				if (chageClass) {

					sql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
					count = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "Integer");
					if (count > 0) {
						StringBuffer sb = new StringBuffer();
						sb.append("UPDATE PS_TZ_FORM_WRK_T SET TZ_CLASS_ID=?,OPRID=?,TZ_APP_INS_ID=?,");
						sb.append("TZ_BATCH_ID=?,ROW_ADDED_DTTM=?,ROW_ADDED_OPRID=?,ROW_LASTMANT_DTTM=?,");
						sb.append("ROW_LASTMANT_OPRID=? where TZ_CLASS_ID = ? AND OPRID = ?");
						sqlQuery.update(sb.toString(), new Object[] { newClassId, strAppOprId, numAppInsId, strBathId,
								new Date(), oprid, new Date(), oprid, strClassId, strAppOprId });
					} else {
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(newClassId);
						psTzFormWrkT.setOprid(strAppOprId);
						psTzFormWrkT.setTzAppInsId(numAppInsId);
						psTzFormWrkT.setRowAddedOprid(oprid);
						psTzFormWrkT.setRowAddedDttm(new Date());
						psTzFormWrkT.setRowLastmantOprid(oprid);
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkT.setTzBatchId(strBathId);
						psTzFormWrkTMapper.insert(psTzFormWrkT);
					}
				} else {
					count = 0;
					sql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
					count = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "Integer");
					if (count > 0) {
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(strClassId);
						psTzFormWrkT.setOprid(strAppOprId);
						psTzFormWrkT.setTzAppInsId(numAppInsId);
						psTzFormWrkT.setRowLastmantOprid(oprid);
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkT.setTzBatchId(strBathId);
						psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
					} else {
						PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
						psTzFormWrkT.setTzClassId(strClassId);
						psTzFormWrkT.setOprid(strAppOprId);
						psTzFormWrkT.setTzAppInsId(numAppInsId);
						psTzFormWrkT.setRowAddedOprid(oprid);
						psTzFormWrkT.setRowAddedDttm(new Date());
						psTzFormWrkT.setRowLastmantOprid(oprid);
						psTzFormWrkT.setRowLastmantDttm(new Date());
						psTzFormWrkT.setTzBatchId(strBathId);
						psTzFormWrkTMapper.insert(psTzFormWrkT);
					}
				}
			}
			// 保存数据到结构化表
			// 解析json
			JacksonUtil jacksonUtil = new JacksonUtil();
			// jacksonUtil.json2Map(strJsonData);
			// System.out.println("保存传入数据:"+strJsonData);
			Map<String, Object> mapAppData = jacksonUtil.parseJson2Map(strJsonData);

			if (mapAppData != null) {
				this.delAppIns(numAppInsId);
				for (Entry<String, Object> entry : mapAppData.entrySet()) {
					Map<String, Object> mapJsonItems = (Map<String, Object>) entry.getValue();
					String strClassName = "";
					if (mapJsonItems.containsKey("classname")) {
						strClassName = String.valueOf(mapJsonItems.get("classname"));
					}
					String strIsDoubleLine = "";
					if (mapJsonItems.containsKey("isDoubleLine")) {
						strIsDoubleLine = String.valueOf(mapJsonItems.get("isDoubleLine"));
					}
					String strIsSingleLine = "";
					if (mapJsonItems.containsKey("isSingleLine")) {
						strIsSingleLine = String.valueOf(mapJsonItems.get("isSingleLine"));
					}
					String strOthervalue = "";
					if (mapJsonItems.containsKey("othervalue")) {
						strOthervalue = String.valueOf(mapJsonItems.get("othervalue"));
					}
					String strItemIdLevel0 = "";
					if (mapJsonItems.containsKey("itemId")) {
						strItemIdLevel0 = String.valueOf(mapJsonItems.get("itemId"));
					}
					if (mapJsonItems.containsKey("children")) {

						List<?> mapChildrens1 = (ArrayList<?>) mapJsonItems.get("children");
						if ("Y".equals(strIsDoubleLine)) {
							// modity by caoy
							if (strClassName.equals("LayoutControls")) {
								this.saveDhLineNum(strItemIdLevel0, numAppInsId,
										(short) ((Map<String, Object>) mapChildrens1.get(0)).size());
							} else {
								this.saveDhLineNum(strItemIdLevel0, numAppInsId, (short) mapChildrens1.size());
							}
							// this.saveDhLineNum(strItemIdLevel0, numAppInsId,
							// (short) mapChildrens1.size());
							for (Object children1 : mapChildrens1) {
								// 多行容器
								Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
								for (Entry<String, Object> entryChildren : mapChildren1.entrySet()) {
									Map<String, Object> mapJsonChildrenItems = (Map<String, Object>) entryChildren
											.getValue();
									String strItemIdLevel1 = "";
									if (mapJsonChildrenItems.containsKey("itemId")) {
										strItemIdLevel1 = String.valueOf(mapJsonChildrenItems.get("itemId"));
									}
									if (mapJsonChildrenItems.containsKey("children")) {
										// 多行容器下的子容器 modity by caoy
										// 解决分组框的某些组合控件的问题
										//// //System.out.println("111:" +
										// mapJsonChildrenItems.get("children"));
										List<Map<String, Object>> mapChildrens2 = null;
										try {
											mapChildrens2 = (ArrayList<Map<String, Object>>) mapJsonChildrenItems
													.get("children");
										} catch (Exception e) {
											// e.printStackTrace();
											mapChildrens2 = new ArrayList<Map<String, Object>>();
											Map<String, Object> cmap = (Map<String, Object>) mapJsonChildrenItems
													.get("children");
											Map<String, Object> ccmap = null;
											for (String key : cmap.keySet()) {
												ccmap = (Map<String, Object>) cmap.get(key);
												mapChildrens2.add(ccmap);
											}
										}

										// ////System.out.println("Size:" +
										// mapChildrens2.size());

										String strIsSingleLine2 = "";
										if (mapJsonChildrenItems.containsKey("isSingleLine")) {
											strIsSingleLine2 = String.valueOf(mapJsonChildrenItems.get("isSingleLine"));
										}
										if ("Y".equals(strIsSingleLine2)) {
											// 多行容器中的单行容器
											for (Object children2 : mapChildrens2) {
												Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
												this.savePerXxxIns(strItemIdLevel0 + strItemIdLevel1, mapChildren2,
														numAppInsId);
											}
										} else {
											// 多行容器中的附件
											String strStorageType = "";
											if (mapJsonChildrenItems.containsKey("StorageType")) {
												strStorageType = mapJsonChildrenItems.get("StorageType") == null ? ""
														: String.valueOf(mapJsonChildrenItems.get("StorageType"));
												if ("F".equals(strStorageType)) {
													for (Object children2 : mapChildrens2) {
														Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
														this.savePerAttrInfo(strItemIdLevel0 + strItemIdLevel1,
																mapChildren2, numAppInsId);
														String strIsHidden = "";
														if (mapJsonChildrenItems.containsKey("isHidden")) {
															strIsHidden = mapJsonChildrenItems.get("isHidden") == null
																	? ""
																	: String.valueOf(
																			mapJsonChildrenItems.get("isHidden"));
														}
														this.saveXxxHidden(numAppInsId,
																strItemIdLevel0 + strItemIdLevel1, strIsHidden);
													}
												}
											}
										}
									} else {
										// 多行容器中的单选框.复选框、一般字段
										String strStorageType = "";
										strStorageType = mapJsonChildrenItems.get("StorageType") == null ? ""
												: String.valueOf(mapJsonChildrenItems.get("StorageType"));
										if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
											// 多行容器中的普通字段
											this.savePerXxxIns(strItemIdLevel0, mapJsonChildrenItems, numAppInsId);
										} else if ("D".equals(strStorageType)) {
											// 单选框或者复选框
											if (mapJsonChildrenItems.containsKey("option")) {
												Map<String, Object> mapOptions = (Map<String, Object>) mapJsonChildrenItems
														.get("option");
												for (Entry<String, Object> entryOption : mapOptions.entrySet()) {
													Map<String, Object> mapOption = (Map<String, Object>) entryOption
															.getValue();
													this.savePerXxxIns2(strItemIdLevel0 + strItemIdLevel1, "",
															mapOption, numAppInsId);
												}
											}
										} else if ("F".equals(strStorageType)) {
											// 推荐信附件信息和其他固定容器附件
											if (!"recommendletter".equals(strClassName)) {
												this.savePerAttrInfo(strItemIdLevel0 + strItemIdLevel1,
														mapJsonChildrenItems, numAppInsId);
											}
										}
									}
								}
							}
						} else if ("Y".equals(strIsSingleLine)) {
							// 如果是单行容器
							for (Object children1 : mapChildrens1) {
								Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
								String strStorageType = "";
								if (mapChildren1.containsKey("StorageType")) {
									strStorageType = mapChildren1.get("StorageType") == null ? ""
											: String.valueOf(mapChildren1.get("StorageType"));
								}
								if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
									this.savePerXxxIns(strItemIdLevel0, mapChildren1, numAppInsId);
								}
							}
						} else {
							// 如果是附件信息
							String strStorageType = "";
							if (mapJsonItems.containsKey("StorageType")) {
								strStorageType = mapJsonItems.get("StorageType") == null ? ""
										: String.valueOf(mapJsonItems.get("StorageType"));
							}
							if ("F".equals(strStorageType)) {
								for (Object children1 : mapChildrens1) {
									Map<String, Object> mapChildren1 = (Map<String, Object>) children1;
									this.savePerAttrInfo(strItemIdLevel0, mapChildren1, numAppInsId);
									String strIsHidden = "";
									if (mapJsonItems.containsKey("isHidden")) {
										strIsHidden = mapJsonItems.get("isHidden") == null ? ""
												: String.valueOf(mapJsonItems.get("isHidden"));
									}
									this.saveXxxHidden(numAppInsId, strItemIdLevel0, strIsHidden);
								}
							}
						}
					} else {
						// 没有Children节点
						String strStorageType = "";
						if (mapJsonItems.containsKey("StorageType")) {
							strStorageType = mapJsonItems.get("StorageType") == null ? ""
									: String.valueOf(mapJsonItems.get("StorageType"));
						}
						if ("D".equals(strStorageType)) {
							// 如果是多项框或者单选框
							if (mapJsonItems.containsKey("option")) {
								Map<String, Object> mapOptions = (Map<String, Object>) mapJsonItems.get("option");
								for (Entry<String, Object> entryOption : mapOptions.entrySet()) {
									Map<String, Object> mapOption = (Map<String, Object>) entryOption.getValue();
									this.savePerXxxIns2(strItemIdLevel0, "", mapOption, numAppInsId);
								}
							}
						} else if ("S".equals(strStorageType) || "L".equals(strStorageType)) {
							this.savePerXxxIns("", mapJsonItems, numAppInsId);
							if ("bmrPhoto".equals(strClassName)) {
								// this.saveBmrPhoto("", mapJsonItems,
								// numAppInsId);
							}
						}
					}
				}
			}

			// 推荐信特别处理，保存推荐人信息
			if (strTplType.equals("TJX")) {
				System.out.println("保存推荐人信息处理开始");
				System.out.println("numAppInsId：" + numAppInsId);
				System.out.println("strTplId：" + strTplId);
				System.out.println("strTjxId：" + strTjxId);
				this.saveTJX(String.valueOf(numAppInsId), strTplId, strTjxId);
				System.out.println("保存推荐人信息处理结束");
			}

		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = e.toString();
		}
		return returnMsg;
	}

	// 推荐信保存推荐人信息
	public void saveTJX(String numAppInsId, String strTplID, String strTjxId) {
		String sql = "select TZ_XXX_BH from PS_TZ_APP_XXXPZ_T where TZ_APP_TPL_ID=? and TZ_COM_LMC='recommendInfo' limit 0,1";
		String TZ_XXX_BH = sqlQuery.queryForObject(sql, new Object[] { strTplID }, "String");
		// System.out.println("TZ_XXX_BH：" + TZ_XXX_BH);
		if (TZ_XXX_BH != null && !TZ_XXX_BH.equals("") && strTjxId != null && !strTjxId.equals("")) {

			String TZ_REFERRER_NAME = "";
			String TZ_REFERRER_GNAME = "";
			String TZ_COMP_CNAME = "";
			String TZ_POSITION = "";
			String TZ_TJR_GX = "";
			String TZ_EMAIL = "";
			String TZ_PHONE = "";
			String SEX = "";
			String TZ_TJX_YL_1 = "";
			String TZ_TJX_YL_2 = "";
			String TZ_TJX_YL_3 = "";
			String TZ_TJX_YL_4 = "";
			String TZ_TJX_YL_5 = "";
			String TZ_TJX_YL_6 = "";
			String TZ_TJX_YL_7 = "";
			String TZ_TJX_YL_8 = "";
			String TZ_TJX_YL_9 = "";
			String TZ_TJX_YL_10 = "";

			String itemId = "";
			String value = "";
			sql = "select TZ_XXX_BH,TZ_APP_S_TEXT from PS_TZ_APP_CC_T where TZ_APP_INS_ID = ? and TZ_XXX_BH like ?";
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { numAppInsId, TZ_XXX_BH + "r_%" });
			Map<String, Object> MapData = null;
			for (Object objData : listData) {
				MapData = (Map<String, Object>) objData;
				itemId = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
				value = MapData.get("TZ_APP_S_TEXT") == null ? "" : String.valueOf(MapData.get("TZ_APP_S_TEXT"));
				if (value != null && !value.equals("")) {
					if (itemId.endsWith("r_name")) {
						TZ_REFERRER_NAME = value;
					} else if (itemId.endsWith("r_gname")) {
						TZ_REFERRER_GNAME = value;
					} else if (itemId.endsWith("r_company")) {
						TZ_COMP_CNAME = value;
					} else if (itemId.endsWith("r_post")) {
						TZ_POSITION = value;
					} else if (itemId.endsWith("r_phone")) {
						TZ_PHONE = value;
					} else if (itemId.endsWith("r_email")) {
						TZ_EMAIL = value;
					} else if (itemId.endsWith("r_relation")) {
						TZ_TJR_GX = value;
					} else if (itemId.endsWith("r_by1")) {
						TZ_TJX_YL_1 = value;
					} else if (itemId.endsWith("r_by2")) {
						TZ_TJX_YL_2 = value;
					} else if (itemId.endsWith("r_by3")) {
						TZ_TJX_YL_3 = value;
					} else if (itemId.endsWith("r_by4")) {
						TZ_TJX_YL_4 = value;
					} else if (itemId.endsWith("r_by5")) {
						TZ_TJX_YL_5 = value;
					} else if (itemId.endsWith("r_by6")) {
						TZ_TJX_YL_6 = value;
					} else if (itemId.endsWith("r_by7")) {
						TZ_TJX_YL_7 = value;
					} else if (itemId.endsWith("r_by8")) {
						TZ_TJX_YL_8 = value;
					} else if (itemId.endsWith("r_by9")) {
						TZ_TJX_YL_9 = value;
					} else if (itemId.endsWith("r_by10")) {
						TZ_TJX_YL_10 = value;
					}
				}
			}

			sql = "select TZ_XXXKXZ_MC from PS_TZ_APP_DHCC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=? and TZ_IS_CHECKED='Y' limit 0,1";
			SEX = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, TZ_XXX_BH + "r_sex" }, "String");
			if (SEX == null) {
				SEX = "";
			}

			PsTzKsTjxTbl psTzKsTjxTbl = new PsTzKsTjxTbl();

			// System.out.println("TZ_REFERRER_NAME：" + TZ_REFERRER_NAME);
			// System.out.println("TZ_REFERRER_GNAME：" + TZ_REFERRER_GNAME);
			// System.out.println("TZ_COMP_CNAME：" + TZ_COMP_CNAME);
			// System.out.println("TZ_POSITION：" + TZ_POSITION);
			// System.out.println("TZ_EMAIL：" + TZ_EMAIL);
			// System.out.println("TZ_PHONE：" + TZ_PHONE);
			// System.out.println("SEX：" + SEX);
			// System.out.println("TZ_TJX_YL_1：" + TZ_TJX_YL_1);
			// System.out.println("TZ_TJX_YL_2：" + TZ_TJX_YL_2);
			// System.out.println("TZ_TJX_YL_3：" + TZ_TJX_YL_3);
			// System.out.println("TZ_TJX_YL_4：" + TZ_TJX_YL_4);
			// System.out.println("TZ_TJX_YL_5：" + TZ_TJX_YL_5);
			// System.out.println("TZ_TJX_YL_6：" + TZ_TJX_YL_6);
			// System.out.println("TZ_TJX_YL_7：" + TZ_TJX_YL_7);
			// System.out.println("TZ_TJX_YL_8：" + TZ_TJX_YL_8);
			// System.out.println("TZ_TJX_YL_9：" + TZ_TJX_YL_9);
			// System.out.println("TZ_TJX_YL_10：" + TZ_TJX_YL_10);
			// System.out.println("TZ_TJR_GX：" + TZ_TJR_GX);

			psTzKsTjxTbl.setTzRefLetterId(strTjxId);
			psTzKsTjxTbl.setTzReferrerGname(TZ_REFERRER_GNAME);
			psTzKsTjxTbl.setTzReferrerName(TZ_REFERRER_NAME);
			psTzKsTjxTbl.setTzCompCname(TZ_COMP_CNAME);
			psTzKsTjxTbl.setTzPosition(TZ_POSITION);
			psTzKsTjxTbl.setTzEmail(TZ_EMAIL);
			psTzKsTjxTbl.setTzPhone(TZ_PHONE);
			psTzKsTjxTbl.setTzGender(SEX);

			psTzKsTjxTbl.setTzTjxYl1(TZ_TJX_YL_1);
			psTzKsTjxTbl.setTzTjxYl2(TZ_TJX_YL_2);
			psTzKsTjxTbl.setTzTjxYl3(TZ_TJX_YL_3);
			psTzKsTjxTbl.setTzTjxYl4(TZ_TJX_YL_4);
			psTzKsTjxTbl.setTzTjxYl5(TZ_TJX_YL_5);
			psTzKsTjxTbl.setTzTjxYl6(TZ_TJX_YL_6);
			psTzKsTjxTbl.setTzTjxYl7(TZ_TJX_YL_7);
			psTzKsTjxTbl.setTzTjxYl8(TZ_TJX_YL_8);
			psTzKsTjxTbl.setTzTjxYl9(TZ_TJX_YL_9);
			psTzKsTjxTbl.setTzTjxYl10(TZ_TJX_YL_10);
			psTzKsTjxTbl.setTzTjrGx(TZ_TJR_GX);
			psTzKsTjxTbl.setRowLastmantDttm(new Date());
			psTzKsTjxTblMapper.updateByPrimaryKeySelective(psTzKsTjxTbl);
		}

	}

	// 报名表提交
	public String submitAppForm(Long numAppInsId, String strClassId, String strAppOprId, String strTplType,
			String strBathId, String pwd, String isPwd) {

		String returnMsg = "";

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);

		try {
			String sql = "";
			int count = 0;
			sql = "SELECT COUNT(1) FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
			count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "Integer");
			if (count > 0) {
				PsTzAppInsT psTzAppInsT = new PsTzAppInsT();
				psTzAppInsT.setTzAppInsId(numAppInsId);
				psTzAppInsT.setTzAppFormSta("U");
				psTzAppInsT.setTzAppPreSta("P");
				psTzAppInsT.setTzAppSubDttm(new Date());
				psTzAppInsT.setRowLastmantOprid(oprid);
				psTzAppInsT.setRowLastmantDttm(new Date());
				if (isPwd != null && isPwd.equals("Y")) {
					psTzAppInsT.setTzPwd(pwd);
				}
				psTzAppInsTMapper.updateByPrimaryKeySelective(psTzAppInsT);
			} else {
				returnMsg = "failed";
			}

			if ("BMB".equals(strTplType)) {
				count = 0;
				sql = "SELECT COUNT(1) FROM PS_TZ_FORM_WRK_T WHERE TZ_CLASS_ID = ? AND OPRID = ?";
				count = sqlQuery.queryForObject(sql, new Object[] { strClassId, strAppOprId }, "Integer");
				if (count > 0) {
					PsTzFormWrkT psTzFormWrkT = new PsTzFormWrkT();
					psTzFormWrkT.setTzClassId(strClassId);
					psTzFormWrkT.setTzBatchId(strBathId);
					psTzFormWrkT.setOprid(strAppOprId);
					psTzFormWrkT.setTzAppInsId(numAppInsId);
					psTzFormWrkT.setTzFormSpSta("N");
					psTzFormWrkT.setRowLastmantOprid(oprid);
					psTzFormWrkT.setRowLastmantDttm(new Date());
					psTzFormWrkTMapper.updateByPrimaryKeySelective(psTzFormWrkT);
				} else {
					returnMsg = "failed";

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = e.toString();
		}
		return returnMsg;
	}

	// 删除报名表存储表信息
	public void delAppIns(Long numAppInsId) {

		Object[] args = new Object[] { numAppInsId };
		sqlQuery.update("DELETE FROM PS_TZ_APP_CC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_FORM_ATT_T WHERE TZ_APP_INS_ID = ?", args);
		// sqlQuery.update("DELETE FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID =
		// ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ?", args);
		sqlQuery.update("DELETE FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = ?", args);
	}

	// 将json数据解析保存到报名表存储表
	public void savePerXxxIns(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String strItemId = "";
		if (xxxObject.containsKey("itemId")) {
			strItemId = String.valueOf(xxxObject.get("itemId"));
		}
		if (!"".equals(strParentItemId) && strParentItemId != null) {
			strItemId = strParentItemId + strItemId;
		}
		// 数据存储类型
		String strStorageType = "";
		// 存储值
		String strValueL = "";
		String strValueS = "";
		String strValue = "";
		// 控件类名称
		String strClassName = "";

		if (xxxObject.containsKey("StorageType")) {
			strStorageType = xxxObject.get("StorageType") == null ? "" : String.valueOf(xxxObject.get("StorageType"));
			if ("L".equals(strStorageType)) {
				strValueL = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
			} else {
				strValueS = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
				strValueL = xxxObject.get("wzsm") == null ? "" : String.valueOf(xxxObject.get("wzsm"));
			}
		}
		// 如果是推荐信title Start
		String sql = "";
		if (xxxObject.containsKey("classname")) {
			strClassName = xxxObject.get("classname") == null ? "" : String.valueOf(xxxObject.get("classname"));
			if ("RefferTitle".equals(strClassName)) {
				sql = "SELECT B.TZ_APP_TPL_LAN FROM PS_TZ_APP_INS_T A,PS_TZ_APPTPL_DY_T B WHERE A.TZ_APP_INS_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID";
				String strTplLang = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
				strValue = xxxObject.get("value") == null ? "" : String.valueOf(xxxObject.get("value"));
				sql = "SELECT TZ_ZHZ_DMS,TZ_ZHZ_CMS FROM PS_TZ_PT_ZHZXX_TBL WHERE TZ_ZHZJH_ID = 'TZ_APP_REF_TITLE' AND TZ_ZHZ_ID = ?";
				Map<String, Object> Map = sqlQuery.queryForMap(sql, new Object[] { strValue });
				String strDms = "";
				String strZms = "";
				if (Map != null) {
					strDms = Map.get("TZ_ZHZ_DMS") == null ? "" : String.valueOf(Map.get("TZ_ZHZ_DMS"));
					strZms = Map.get("TZ_ZHZ_CMS") == null ? "" : String.valueOf(Map.get("TZ_ZHZ_CMS"));
				}

				if ("ENG".equals(strTplLang)) {
					if (!"".equals(strZms)) {
						strValueS = strZms;
					}
				} else {
					if (!"".equals(strDms)) {
						strValueS = strDms;
					}
				}
			}
		}

		if (strValueS.length() > 254) {
			strValueS = strValueS.substring(0, 254);
		}

		// 如果是推荐信title End
		PsTzAppCcT psTzAppCcT = new PsTzAppCcT();
		psTzAppCcT.setTzAppInsId(numAppInsId);
		psTzAppCcT.setTzXxxBh(strItemId);
		psTzAppCcT.setTzAppSText(strValueS);
		psTzAppCcT.setTzAppLText(strValueL);
		psTzAppCcTMapper.insert(psTzAppCcT);

		// 是否隐藏
		String strIsHidden = "";
		if (xxxObject.containsKey("isHidden")) {
			strIsHidden = xxxObject.get("isHidden") == null ? "" : String.valueOf(xxxObject.get("isHidden"));
			if ("".equals(strIsHidden)) {
				strIsHidden = "N";
			}
		} else {
			strIsHidden = "N";
		}
		this.saveXxxHidden(numAppInsId, strItemId, strIsHidden);
	}

	/* 保存多行容器的行数信息 */
	public void saveDhLineNum(String strItemId, Long numAppInsId, short numLineDh) {
		String sql = "SELECT COUNT(1) FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
		int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strItemId }, "Integer");
		if (count > 0) {
			PsTzAppDhhsT psTzAppDhhsT = new PsTzAppDhhsT();
			psTzAppDhhsT.setTzAppInsId(numAppInsId);
			psTzAppDhhsT.setTzXxxBh(strItemId);
			psTzAppDhhsT.setTzXxxLine(numLineDh);
			psTzAppDhhsTMapper.updateByPrimaryKeySelective(psTzAppDhhsT);
		} else {
			PsTzAppDhhsT psTzAppDhhsT = new PsTzAppDhhsT();
			psTzAppDhhsT.setTzAppInsId(numAppInsId);
			psTzAppDhhsT.setTzXxxBh(strItemId);
			psTzAppDhhsT.setTzXxxLine(numLineDh);
			psTzAppDhhsTMapper.insert(psTzAppDhhsT);
		}
	}

	/* 设置字段是否隐藏 */
	public void saveXxxHidden(Long numAppInsId, String strItemId, String strIsHidden) {
		/**/
		String sql = "SELECT COUNT(1) FROM PS_TZ_APP_HIDDEN_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
		int count = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strItemId }, "Integer");
		if (count > 0) {
			PsTzAppHiddenT psTzAppHiddenT = new PsTzAppHiddenT();
			psTzAppHiddenT.setTzAppInsId(numAppInsId);
			psTzAppHiddenT.setTzXxxBh(strItemId);
			psTzAppHiddenT.setTzIsHidden(strIsHidden);
			psTzAppHiddenTMapper.updateByPrimaryKeySelective(psTzAppHiddenT);
		} else {
			PsTzAppHiddenT psTzAppHiddenT = new PsTzAppHiddenT();
			psTzAppHiddenT.setTzAppInsId(numAppInsId);
			psTzAppHiddenT.setTzXxxBh(strItemId);
			psTzAppHiddenT.setTzIsHidden(strIsHidden);
			psTzAppHiddenTMapper.insert(psTzAppHiddenT);
		}
	}

	// 将json数据解析保存到报名表附件存储表
	public void savePerAttrInfo(String strParentItemId, Map<String, Object> xxxObject, Long numAppInsId) {

		String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
		String strSysFileName = "";
		if (xxxObject.containsKey("sysFileName")) {
			strSysFileName = xxxObject.get("sysFileName") == null ? "" : String.valueOf(xxxObject.get("sysFileName"));
		}
		String strUseFileName = "";
		if (xxxObject.containsKey("fileName")) {
			strUseFileName = xxxObject.get("fileName") == null ? "" : String.valueOf(xxxObject.get("fileName"));
		} else {
			if (xxxObject.containsKey("filename")) {
				strUseFileName = xxxObject.get("filename") == null ? "" : String.valueOf(xxxObject.get("filename"));
			}
		}
		String strOrderBy = "";
		if (xxxObject.containsKey("orderby")) {
			strOrderBy = xxxObject.get("orderby") == null ? "" : String.valueOf(xxxObject.get("orderby"));
		}
		int numOrderBy = 0;
		if ("".equals(strOrderBy) || strOrderBy == null) {
			numOrderBy = 0;
		} else {
			numOrderBy = Integer.parseInt(strOrderBy);
		}

		String strPath = "";
		if (xxxObject.containsKey("accessPath")) {
			strPath = xxxObject.get("accessPath") == null ? "" : String.valueOf(xxxObject.get("accessPath"));
		}

		if (!"".equals(strSysFileName) && strSysFileName != null && !"".equals(strUseFileName)
				&& strUseFileName != null) {
			PsTzFormAttT psTzFormAttT = new PsTzFormAttT();
			psTzFormAttT.setTzAppInsId(numAppInsId);
			psTzFormAttT.setTzXxxBh(strParentItemId);
			psTzFormAttT.setTzIndex(numOrderBy);
			psTzFormAttT.setTzAccessPath(strPath);
			psTzFormAttT.setAttachsysfilename(strSysFileName);
			psTzFormAttT.setAttachuserfile(strUseFileName);
			psTzFormAttT.setRowAddedOprid(oprid);
			psTzFormAttT.setRowAddedDttm(new Date());
			psTzFormAttT.setRowLastmantOprid(oprid);
			psTzFormAttT.setRowLastmantDttm(new Date());
			psTzFormAttTMapper.insert(psTzFormAttT);
		}
	}

	// 检查是否填写完成
	/**
	 * 
	 * @param numAppInsId
	 * @param strTplId
	 * @param strPageId
	 * @param strOtype
	 * @return
	 */
	public String checkFiledValid(Long numAppInsId, String strTplId, String strPageId, String strOtype,
			String strTplType) {
		String returnMsg = "";

		/* 信息项编号 */
		String strXxxBh = "";

		/* 信息项名称 */
		String strXxxMc = "";

		/* 控件类名称 */
		String strComMc = "";

		/* 分页号 */
		int numPageNo;

		/* 信息项日期格式 */
		String strXxxRqgs = "";

		/* 信息项日期年份最小值 */
		String strXxxXfmin = "";

		/* 信息项日期年份最大值 */
		String strXxxXfmax = "";

		/* 信息项多选最少选择数量 */
		String strXxxZsxzgs = "";

		/* 信息项多选最多选择数量 */
		String strXxxZdxzgs = "";

		/* 信息项文件允许上传类型 */
		String strXxxYxsclx = "";

		/* 信息项文件允许上传大小 */
		String strXxxYxscdx = "";

		/* 信息项是否必填 */
		String strXxxBtBz = "";

		/* 信息项是否启用字数范围 */
		String strXxxCharBz = "";

		/* 信息项字数最小长度 */
		int numXxxMinlen;

		/* 信息项字数最大长度 */
		long numXxxMaxlen;

		/* 信息项是否启用数字范围 */
		String strXxxNumBz = "";

		/* 信息项字数最小长度 */
		int numXxxMin;

		/* 信息项字数最大长度 */
		long numXxxMax;

		/* 信息项字段小数位数 */
		String strXxxXsws = "";

		/* 信息项字段固定格式校验 */
		String strXxxGdgsjy = "";

		/* 信息项字段是否多容器 */
		String strXxxDrqBz = "";

		/* 信息项最小行记录数 */
		int numXxxMinLine;

		/* 信息项最大行记录数 */
		int numXxxMaxLine;

		/* 推荐信收集齐前是否允许提交报名表 */
		String strTjxSub = "";

		/* 信息项校验规则 */
		String strJygzId;

		String strJygzTsxx;

		/* 信息项校验程序 */
		String strPath, strName, strMethod;

		// modity by caoy 页面从1开始
		int numCurrentPageNo = 1;

		ArrayList<Integer> listPageNo = new ArrayList<Integer>();

		try {
			if (!"".equals(strPageId)) {
				String sqlGetPageNo = "SELECT TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";
				numCurrentPageNo = sqlQuery.queryForObject(sqlGetPageNo, new Object[] { strTplId, strPageId },
						"Integer");
			}
			String sql = "";
			List<?> listData = null;

			// 保存的時候 如果本页 有预提交按钮，检查是否 已经预备提交了
			if ("save".equals(strOtype)) {
				sql = "SELECT TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = ?";
				String strPretPageNo = sqlQuery.queryForObject(sql, new Object[] { strTplId, "PreButtom" }, "String");

				if (strPretPageNo != null && !strPretPageNo.equals("")) {
					int PretPageNo = Integer.parseInt(strPretPageNo);
					if (PretPageNo == numCurrentPageNo) {
						sql = "SELECT TZ_APP_PRE_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?";
						String TZ_APP_FORM_STA = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
						if (TZ_APP_FORM_STA.equals("P")) {
							return "";
						} else {
							return "NOPRE";
						}
					}
				}
			}

			if ("pre".equals(strOtype)) {
				sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_ONLINE_CHECK_SQL2");

				listData = sqlQuery.queryForList(sql, new Object[] { strTplId, numCurrentPageNo });
			} else {
				sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_ONLINE_CHECK_SQL");
				listData = sqlQuery.queryForList(sql, new Object[] { strTplId });
			}
			Map<String, Object> MapData = null;
			for (Object objData : listData) {
				MapData = (Map<String, Object>) objData;
				strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
				strXxxMc = MapData.get("TZ_XXX_MC") == null ? "" : String.valueOf(MapData.get("TZ_XXX_MC"));
				strComMc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
				numPageNo = MapData.get("TZ_PAGE_NO") == null ? 0
						: Integer.valueOf(String.valueOf(MapData.get("TZ_PAGE_NO")));
				strXxxRqgs = MapData.get("TZ_XXX_RQGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_RQGS"));
				strXxxXfmin = MapData.get("TZ_XXX_NFMIN") == null ? "" : String.valueOf(MapData.get("TZ_XXX_NFMIN"));
				strXxxXfmax = MapData.get("TZ_XXX_NFMAX") == null ? "" : String.valueOf(MapData.get("TZ_XXX_NFMAX"));
				strXxxZsxzgs = MapData.get("TZ_XXX_ZSXZGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_ZSXZGS"));
				strXxxZdxzgs = MapData.get("TZ_XXX_ZDXZGS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_ZDXZGS"));
				strXxxYxsclx = MapData.get("TZ_XXX_YXSCLX") == null ? "" : String.valueOf(MapData.get("TZ_XXX_YXSCLX"));
				strXxxYxscdx = MapData.get("TZ_XXX_YXSCDX") == null ? "" : String.valueOf(MapData.get("TZ_XXX_YXSCDX"));
				strXxxBtBz = MapData.get("TZ_XXX_BT_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BT_BZ"));
				strXxxCharBz = MapData.get("TZ_XXX_CHAR_BZ") == null ? ""
						: String.valueOf(MapData.get("TZ_XXX_CHAR_BZ"));
				numXxxMinlen = MapData.get("TZ_XXX_MINLEN") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MINLEN")));
				numXxxMaxlen = MapData.get("TZ_XXX_MAXLEN") == null ? 0
						: Long.parseLong(String.valueOf(MapData.get("TZ_XXX_MAXLEN")));
				strXxxNumBz = MapData.get("TZ_XXX_NUM_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_NUM_BZ"));
				numXxxMin = MapData.get("TZ_XXX_MIN") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN")));
				numXxxMax = MapData.get("TZ_XXX_MAX") == null ? 0
						: Long.parseLong(String.valueOf(MapData.get("TZ_XXX_MAX")));
				strXxxXsws = MapData.get("TZ_XXX_XSWS") == null ? "" : String.valueOf(MapData.get("TZ_XXX_XSWS"));
				strXxxGdgsjy = MapData.get("TZ_XXX_GDGSJY") == null ? "" : String.valueOf(MapData.get("TZ_XXX_GDGSJY"));
				strXxxDrqBz = MapData.get("TZ_XXX_DRQ_BZ") == null ? "" : String.valueOf(MapData.get("TZ_XXX_DRQ_BZ"));
				numXxxMinLine = MapData.get("TZ_XXX_MIN_LINE") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MIN_LINE")));
				numXxxMaxLine = MapData.get("TZ_XXX_MAX_LINE") == null ? 0
						: Integer.parseInt(String.valueOf(MapData.get("TZ_XXX_MAX_LINE")));
				strTjxSub = MapData.get("TZ_TJX_SUB") == null ? "" : String.valueOf(MapData.get("TZ_TJX_SUB"));
				strPath = MapData.get("TZ_APPCLS_PATH") == null ? "" : String.valueOf(MapData.get("TZ_APPCLS_PATH"));
				strName = MapData.get("TZ_APPCLS_NAME") == null ? "" : String.valueOf(MapData.get("TZ_APPCLS_NAME"));
				strMethod = MapData.get("TZ_APPCLS_METHOD") == null ? ""
						: String.valueOf(MapData.get("TZ_APPCLS_METHOD"));
				strJygzTsxx = MapData.get("TZ_JYGZ_TSXX") == null ? "" : String.valueOf(MapData.get("TZ_JYGZ_TSXX"));

				if ("save".equals(strOtype)) {
					if (numCurrentPageNo == numPageNo) {
						tzOnlineAppUtility tzOnlineAppUtility = (tzOnlineAppUtility) ctx
								.getBean(strPath + "." + strName);
						String strReturn = "";
						switch (strMethod) {
						case "requireValidator":
							strReturn = tzOnlineAppUtility.requireValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "ahphValidator":
							strReturn = tzOnlineAppUtility.ahphValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "charLenValidator":
							strReturn = tzOnlineAppUtility.charLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "valueValidator":
							strReturn = tzOnlineAppUtility.valueValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "regularValidator":
							strReturn = tzOnlineAppUtility.regularValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "dHLineValidator":
							strReturn = tzOnlineAppUtility.dHLineValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "refLetterValidator":
							strReturn = tzOnlineAppUtility.refLetterValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						case "rowLenValidator":
							strReturn = tzOnlineAppUtility.rowLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
									strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
									strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
									numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy,
									strXxxDrqBz, numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
							break;
						}
						if (!"".equals(strReturn)) {
							returnMsg = strReturn;
							break;
						}
					}
				} else {
					tzOnlineAppUtility tzOnlineAppUtility = (tzOnlineAppUtility) ctx.getBean(strPath + "." + strName);
					String strReturn = "";
					switch (strMethod) {
					case "requireValidator":
						strReturn = tzOnlineAppUtility.requireValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "ahphValidator":
						strReturn = tzOnlineAppUtility.ahphValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "charLenValidator":
						strReturn = tzOnlineAppUtility.charLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "valueValidator":
						strReturn = tzOnlineAppUtility.valueValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "regularValidator":
						strReturn = tzOnlineAppUtility.regularValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "dHLineValidator":
						strReturn = tzOnlineAppUtility.dHLineValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "refLetterValidator":
						strReturn = tzOnlineAppUtility.refLetterValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "VerificationCodeValidator":
						strReturn = tzOnlineAppUtility.VerificationCodeValidator(numAppInsId, strTplId, strXxxBh,
								strXxxMc, strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs,
								strXxxZdxzgs, strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen,
								numXxxMaxlen, strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz,
								numXxxMinLine, numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					case "rowLenValidator":
						strReturn = tzOnlineAppUtility.rowLenValidator(numAppInsId, strTplId, strXxxBh, strXxxMc,
								strComMc, numPageNo, strXxxRqgs, strXxxXfmin, strXxxXfmax, strXxxZsxzgs, strXxxZdxzgs,
								strXxxYxsclx, strXxxYxscdx, strXxxBtBz, strXxxCharBz, numXxxMinlen, numXxxMaxlen,
								strXxxNumBz, numXxxMin, numXxxMax, strXxxXsws, strXxxGdgsjy, strXxxDrqBz, numXxxMinLine,
								numXxxMaxLine, strTjxSub, strJygzTsxx);
						break;
					}
					if (!"".equals(strReturn)) {
						if (!listPageNo.contains(numPageNo)) {
							listPageNo.add(numPageNo);
						}
						returnMsg = returnMsg + strReturn + "<br/>";
					}
				}
			}

			if ("submit".equals(strOtype)) {
				// 推荐信校验,特殊的例子
				if (strTplType.equals("TJX")) {
					System.out.println("推荐信推荐人信息校验开始");
					String strJsonData = sqlQuery.queryForObject(
							"select TZ_APPINS_JSON_STR from PS_TZ_APP_INS_T where TZ_APP_INS_ID=?",
							new Object[] { numAppInsId }, "String");
					// System.out.println("strJsonData:" + strJsonData);
					JacksonUtil jacksonUtil = new JacksonUtil();
					Map<String, Object> mapAppData = jacksonUtil.parseJson2Map(strJsonData);

					if (mapAppData != null) {
						Map<String, Object> mapJsonItems = null;
						List<String> itemName = new ArrayList<String>();

						for (Entry<String, Object> entry : mapAppData.entrySet()) {
							mapJsonItems = (Map<String, Object>) entry.getValue();
							String strClassName = "";
							if (mapJsonItems.containsKey("classname")) {
								strClassName = String.valueOf(mapJsonItems.get("classname"));
							}
							if (strClassName.equals("recommendInfo")) {
								if (mapJsonItems.containsKey("children")) {

									List<Map<String, Object>> mapChildrens = (ArrayList<Map<String, Object>>) mapJsonItems
											.get("children");

									Map<String, Object> cmap = (Map<String, Object>) mapChildrens.get(0);
									// System.out.println(cmap.toString());
									Map<String, Object> ccmap = null;

									List<Map<String, Object>> mapChildrens2 = new ArrayList<Map<String, Object>>();
									for (String key : cmap.keySet()) {
										ccmap = (Map<String, Object>) cmap.get(key);
										// System.out.println(ccmap.toString());
										mapChildrens2.add(ccmap);
									}

									for (Object children2 : mapChildrens2) {
										Map<String, Object> mapChildren2 = (Map<String, Object>) children2;
										String useby = String.valueOf(mapChildren2.get("useby"));
										// System.out.println("useby:" + useby);
										if (useby.equals("Y")) {
											// System.out.println("需要填写的项:" +
											// String.valueOf(mapChildren2.get("itemId")));
											itemName.add(String.valueOf(mapChildren2.get("itemId")));
										}
									}
								}
							}
						}
						sql = "select TZ_XXX_BH from PS_TZ_APP_XXXPZ_T where TZ_APP_TPL_ID=? and TZ_COM_LMC='recommendInfo' limit 0,1";
						String TZ_XXX_BH = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");
						sql = "select TZ_XXX_BH from PS_TZ_APP_CC_T where TZ_APP_INS_ID = ? and TZ_XXX_BH like ? and TZ_APP_S_TEXT!=''";
						List<?> listhave = sqlQuery.queryForList(sql, new Object[] { numAppInsId, TZ_XXX_BH + "r_%" });
						List<String> have = new ArrayList<String>();
						String strxx = "";
						for (Object ObjValue : listhave) {
							mapJsonItems = (Map<String, Object>) ObjValue;
							strxx = mapJsonItems.get("TZ_XXX_BH") == null ? ""
									: String.valueOf(mapJsonItems.get("TZ_XXX_BH"));
							// System.out.println("已经填写的项:" + strxx);
							have.add(strxx);
						}

						sql = "select TZ_XXXKXZ_MC from PS_TZ_APP_DHCC_T where TZ_APP_INS_ID=? and TZ_XXX_BH=? and TZ_IS_CHECKED='Y' limit 0,1";
						String SEX = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, TZ_XXX_BH + "r_sex" },
								"String");
						if (SEX != null && !SEX.equals("")) {
							have.add(TZ_XXX_BH + "r_sex");
						}

						boolean check = false;
						boolean Listcheck = true;
						for (String str : itemName) {
							check = false;
							for (String str2 : have) {
								if (str2.endsWith(str)) {
									check = true;
									break;
								}
							}
							if (!check) {
								Listcheck = false;
								break;
							}
						}
						if (!Listcheck) {
							// System.out.println("推荐信推荐人信息校验失败");
							sql = "select TZ_APP_TPL_LAN from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID=?";
							String LAN = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");

							if (LAN.equals("ENG")) {
								returnMsg = returnMsg + "Reference information is incomplete" + "<br/>";
							} else {
								returnMsg = returnMsg + "推荐人信息不完整" + "<br/>";
							}
							listPageNo.add(0);
						}
					}
					System.out.println("推荐信推荐人信息校验结束");
				}

				// 提交的时候 ，校验是否 已经预提交 ，如果没有预备提交，那么该页不设置打勾
				// 校验的时候排除推荐信
				/*
				 * sql =
				 * "SELECT A.TZ_USE_TYPE,B.TZ_APP_FORM_STA FROM PS_TZ_APPTPL_DY_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND B.TZ_APP_INS_ID=?"
				 * ;
				 * 
				 * Map<String, Object> dataMap = sqlQuery.queryForMap(sql, new
				 * Object[] { numAppInsId });
				 * 
				 * if (dataMap != null) {
				 * 
				 * String TZ_USE_TYPE = dataMap.get("TZ_USE_TYPE") == null ? ""
				 * : String.valueOf(dataMap.get("TZ_USE_TYPE"));
				 * 
				 * String TZ_APP_FORM_STA = dataMap.get("TZ_APP_FORM_STA") ==
				 * null ? "" : String.valueOf(dataMap.get("TZ_APP_FORM_STA"));
				 * 
				 * if (TZ_USE_TYPE.equals("BMB") &&
				 * !TZ_APP_FORM_STA.equals("P")) { returnMsg = returnMsg +
				 * "请先预提交" + "<br/>";
				 * 
				 * sql =
				 * "SELECT TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = ?"
				 * ; String strPretPageNo = sqlQuery.queryForObject(sql, new
				 * Object[] { strTplId, "PreButtom" }, "String");
				 * 
				 * if (strPretPageNo != null && !strPretPageNo.equals("")) {
				 * listPageNo.add(Integer.parseInt(strPretPageNo)); } } }
				 */

				// 页面全部设置成完成

				// 提交进行证件号校验--开始 by hjl
				String tz_idnum = "TZ_6idnum";// 证件id 写死
				String oprid = tzLoginServiceImpl.getLoginedManagerOprid(request);
				String idnumsql = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? AND TZ_XXX_BH=?";
				String stridnum = sqlQuery.queryForObject(idnumsql, new Object[] { numAppInsId, tz_idnum }, "String");
				if (!"".equals(stridnum)) {
					String haidnumsql = "SELECT COUNT(1)  FROM PS_TZ_REG_USER_T WHERE NATIONAL_ID=? AND  OPRID<>? ";
					int idcount = sqlQuery.queryForObject(haidnumsql, new Object[] { stridnum, oprid }, "Integer");
					if (idcount > 0) {
						String sqlluange = "select TZ_APP_TPL_LAN from PS_TZ_APPTPL_DY_T where TZ_APP_TPL_ID=?";
						String luange = sqlQuery.queryForObject(sqlluange, new Object[] { strTplId }, "String");
						if (luange.equals("ENG")) {

							returnMsg = returnMsg
									+ "“NATION ID”:There are other account number and your certificate number"
									+ "<br/>";
						} else {
							returnMsg = returnMsg + "“身份证”:有其他账号的证件号与您的证件号相同!" + "<br/>";
						}
					} else {

					}
				} else {

				}

				// 提交进行证件号校验--结束

				String sqlGetPageXxxBh = "SELECT TZ_XXX_BH,TZ_PAGE_NO FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = ? AND  TZ_PAGE_NO>0 ";
				List<?> ListPageXxxBh = sqlQuery.queryForList(sqlGetPageXxxBh, new Object[] { strTplId, "Page" });
				Map<String, Object> MapXxxBh = null;
				String strPageXxxBh = "";
				Integer numPageNo1;
				for (Object ObjValue : ListPageXxxBh) {
					MapXxxBh = (Map<String, Object>) ObjValue;
					strPageXxxBh = MapXxxBh.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapXxxBh.get("TZ_XXX_BH"));
					numPageNo1 = MapXxxBh.get("TZ_PAGE_NO") == null ? 0
							: Integer.valueOf(String.valueOf(MapXxxBh.get("TZ_PAGE_NO")));
					if (strPageXxxBh != null && !"".equals(strPageXxxBh)) {
						this.savePageCompleteState(numAppInsId, strPageXxxBh, "Y");
						/* 处理非必填页面，如果页面非必填，保存时给完成状态设置成"B" */
						String getNoRequiredPageSql = tzSQLObject
								.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GET_NOREQUIRE_PAGE_SQL");
						int countNoRequired = sqlQuery.queryForObject(getNoRequiredPageSql,
								new Object[] { strTplId, strPageXxxBh }, "Integer");
						if (countNoRequired > 0) {
							boolean isWriteNoRequired = this.isWriteNoRequiredPage(numAppInsId, strTplId, numPageNo1);
							if (isWriteNoRequired) {
							} else {
								this.savePageCompleteState(numAppInsId, strPageXxxBh, "B");
							}
							if ("TZ_35".equals(strPageXxxBh)) {
								/* 不是预提交页面，则不用更新页面完成状态为无 */
								String strPreSubmit = sqlQuery.queryForObject(
										"SELECT TZ_APP_PRE_STA FROM PS_TZ_APP_INS_T WHERE TZ_APP_INS_ID = ?",
										new Object[] { numAppInsId }, "String");
								if ("P".equals(strPreSubmit)) {
									/* 不是预提交页面，则不用更新页面完成状态为无 */
									this.savePageCompleteState(numAppInsId, strPageXxxBh, "Y");
								} else {
									this.savePageCompleteState(numAppInsId, strPageXxxBh, "N");
								}

							}
						}
					}
				}

				for (Integer numPageNo2 : listPageNo) {
					String sqlGetXxxBh = "SELECT TZ_XXX_BH FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_COM_LMC = ? AND TZ_PAGE_NO = ?";

					String strXxxBh2 = sqlQuery.queryForObject(sqlGetXxxBh,
							new Object[] { strTplId, "Page", numPageNo2 }, "String");
					// System.out.println("未完成页面"+numPageNo2+"信息项编号:" +
					// strXxxBh2);
					if (strXxxBh2 != null && !"".equals(strXxxBh2)) {
						this.savePageCompleteState(numAppInsId, strXxxBh2, "N");
					}
				}
				/* 如果页面是推荐信页面 */
				this.checkRefletter(numAppInsId, strTplId);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMsg;
	}

	// 空页面校验
	public boolean isWriteNoRequiredPage(Long numAppInsId, String strTplId, Integer numPageNo) {

		boolean b_flag = false;
		String sql;
		try {
			sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_GET_NOREQUIRE_PAGE_XXX_SQL");
			List<?> listData = sqlQuery.queryForList(sql, new Object[] { strTplId, numPageNo });

			Map<String, Object> MapData = null;
			String strXxxBh;
			String strXxxMc;
			String strComMc;

			for (Object objData : listData) {
				MapData = (Map<String, Object>) objData;
				strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
				strXxxMc = MapData.get("TZ_XXX_MC") == null ? "" : String.valueOf(MapData.get("TZ_XXX_MC"));
				strComMc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
				b_flag = this.blankValidator(numAppInsId, strTplId, strXxxBh, strXxxMc, strComMc);
				if (b_flag) {
					break;
				}
			}
		} catch (TzSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b_flag;
	}

	// 同步报名人联系方式
	public void savaContactInfo(Long numAppInsId, String strTplId, String strAppOprId) {
		// 注册信息

		/* 主要手机 */
		String strZysj = "";
		String strZysjHb = "";
		/* 备用手机 */
		String strBysj = "";
		String strBysjHb = "";
		/* 主要电话 */
		String strZydh = "";
		String strZydhHb = "";
		/* 备用电话 */
		String strBydh = "";
		String strBydhHb = "";
		/* 主要邮箱 */
		String strZyyx = "";
		String strZyyxHb = "";
		/* 备用邮箱 */
		String strByyx = "";
		String strByyxHb = "";
		/* 主要地址 */
		String strZydz = "";
		String strZydzHb = "";
		/* 主要邮编 */
		String strZyyb = "";
		String strZyybHb = "";
		/* 备要地址 */
		String strBydz = "";
		String strBydzHb = "";
		/* 备要邮编 */
		String strByyb = "";
		String strByybHb = "";
		/* 微信 */
		String strWx = "";
		String strWxHb = "";
		/* skype帐号 */
		String strSkype = "";
		String strSkypeHb = "";

		/* idcard */

		String strIdCard = "";

		String strDxxxBh = "";
		String strXxxBhLike = "";

		String strComLmc;
		String strXxxBh;
		String strSyncType = "";
		String strSyncSep = "";
		String sqlGetSyncXxx = "";
		sqlGetSyncXxx = "SELECT A.TZ_COM_LMC,A.TZ_XXX_BH,B.TZ_SYNC_TYPE,B.TZ_SYNC_SEP FROM PS_TZ_APP_XXXPZ_T A,PS_TZ_APPXX_SYNC_T B WHERE A.TZ_APP_TPL_ID = ? AND A.TZ_APP_TPL_ID = B.TZ_APP_TPL_ID AND A.TZ_XXX_BH = B.TZ_XXX_BH AND B.TZ_QY_BZ = 'Y' AND B.TZ_SYNC_TYPE <> ' ' ORDER BY B.TZ_SYNC_ORDER";
		List<?> listData = sqlQuery.queryForList(sqlGetSyncXxx, new Object[] { strTplId });
		for (Object objData : listData) {
			Map<String, Object> MapData = (Map<String, Object>) objData;
			strComLmc = MapData.get("TZ_COM_LMC") == null ? "" : String.valueOf(MapData.get("TZ_COM_LMC"));
			strXxxBh = MapData.get("TZ_XXX_BH") == null ? "" : String.valueOf(MapData.get("TZ_XXX_BH"));
			strSyncType = MapData.get("TZ_SYNC_TYPE") == null ? "" : String.valueOf(MapData.get("TZ_SYNC_TYPE"));
			strSyncSep = MapData.get("TZ_SYNC_SEP") == null ? "" : String.valueOf(MapData.get("TZ_SYNC_SEP"));
			// 查看是否在容器中
			String sql = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 0,1";
			strDxxxBh = sqlQuery.queryForObject(sql, new Object[] { strTplId, strXxxBh }, "String");
			if (!"".equals(strDxxxBh) && strDxxxBh != null) {
				strXxxBhLike = strDxxxBh + strXxxBh;
			} else {
				strDxxxBh = strXxxBh;
				strXxxBhLike = strXxxBh;
			}

			String strPhoneArea = "";
			String strPhoneNo = "";

			String strProvince = "";
			String strAddress = "";

			switch (strSyncType) {
			case "ZYSJ":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strZysj = strPhoneArea + "-" + strPhoneNo;
						} else {
							strZysj = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strZysj = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 主要手机合并
				if (!"".equals(strZysjHb) && strZysjHb != null) {
					if (!"".equals(strZysj) && strZysj != null) {
						strZysjHb = strZysjHb + strSyncSep + strZysj;
					}
				} else {
					if (!"".equals(strZysj) && strZysj != null) {
						strZysjHb = strZysj;
					}
				}
				break;
			case "BYSJ":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strBysj = strPhoneArea + "-" + strPhoneNo;
						} else {
							strBysj = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strBysj = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 备用手机合并
				if (!"".equals(strBysjHb) && strBysjHb != null) {
					if (!"".equals(strBysj) && strBysj != null) {
						strBysjHb = strBysjHb + strSyncSep + strBysj;
					}
				} else {
					if (!"".equals(strBysj) && strBysj != null) {
						strBysjHb = strBysj;
					}
				}
				break;
			case "ZYDH":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strZydh = strPhoneArea + "-" + strPhoneNo;
						} else {
							strZydh = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strZydh = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 主要电话合并
				if (!"".equals(strZydhHb) && strZydhHb != null) {
					if (!"".equals(strZydh) && strZydh != null) {
						strZydhHb = strBysjHb + strSyncSep + strZydh;
					}
				} else {
					if (!"".equals(strZydh) && strZydh != null) {
						strZydhHb = strZydh;
					}
				}
				break;
			case "BYDH":
				if ("mobilePhone".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_area'";
					strPhoneArea = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'mobile_no'";
					strPhoneNo = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strPhoneNo) && strPhoneNo != null) {
						if (!"".equals(strPhoneArea) && strPhoneArea != null) {
							strBydh = strPhoneArea + "-" + strPhoneNo;
						} else {
							strBydh = strPhoneNo;
						}
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strBysj = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 备用电话合并
				if (!"".equals(strBydhHb) && strBydhHb != null) {
					if (!"".equals(strBydh) && strBydh != null) {
						strBydhHb = strBydhHb + strSyncSep + strBydh;
					}
				} else {
					if (!"".equals(strBydh) && strBydh != null) {
						strBydhHb = strBydh;
					}
				}
				break;
			case "ZYYX":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strZyyx = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 主要邮箱合并
				if (!"".equals(strZyyxHb) && strZyyxHb != null) {
					if (!"".equals(strZyyx) && strZyyx != null) {
						strZyyxHb = strZyyxHb + strSyncSep + strZyyx;
					}
				} else {
					if (!"".equals(strZyyx) && strZyyx != null) {
						strZyyxHb = strZyyx;
					}
				}
				break;
			case "BYYX":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strByyx = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 备用邮箱合并
				if (!"".equals(strByyxHb) && strByyxHb != null) {
					if (!"".equals(strZyyx) && strZyyx != null) {
						strByyxHb = strByyxHb + strSyncSep + strByyx;
					}
				} else {
					if (!"".equals(strByyx) && strByyx != null) {
						strByyxHb = strByyx;
					}
				}
				break;
			case "ZYDZ":
				if ("MailingAddress".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'province'";
					strProvince = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'address'";
					strAddress = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strProvince) && strProvince != null && !"".equals(strAddress)
							&& strAddress != null) {
						strZydz = strProvince + strAddress;
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strZydz = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 主要地址合并
				if (!"".equals(strZydzHb) && strZydzHb != null) {
					if (!"".equals(strZydz) && strZydz != null) {
						strZydzHb = strZydzHb + strSyncSep + strZydz;
					}
				} else {
					if (!"".equals(strZydz) && strZydz != null) {
						strZydzHb = strZydz;
					}
				}
				break;
			case "ZYYB":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strZyyb = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 主要邮编合并
				if (!"".equals(strZyybHb) && strZyybHb != null) {
					if (!"".equals(strZyyb) && strZyyb != null) {
						strByyxHb = strByyxHb + strSyncSep + strZyyb;
					}
				} else {
					if (!"".equals(strZyyb) && strZyyb != null) {
						strZyybHb = strZyyb;
					}
				}
				break;
			case "BYDZ":
				if ("MailingAddress".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'province'";
					strProvince = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'address'";
					strAddress = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");
					if (!"".equals(strProvince) && strProvince != null && !"".equals(strAddress)
							&& strAddress != null) {
						strBydz = strProvince + strAddress;
					}
				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strBydz = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				}
				// 次要地址合并
				if (!"".equals(strBydzHb) && strBydzHb != null) {
					if (!"".equals(strBydz) && strBydz != null) {
						strBydzHb = strZydzHb + strSyncSep + strBydz;
					}
				} else {
					if (!"".equals(strBydz) && strBydz != null) {
						strBydzHb = strBydz;
					}
				}
				break;
			case "BYYB":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strByyb = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 备用邮编合并
				if (!"".equals(strByybHb) && strByybHb != null) {
					if (!"".equals(strByyb) && strByyb != null) {
						strByybHb = strByybHb + strSyncSep + strByyb;
					}
				} else {
					if (!"".equals(strByyb) && strByyb != null) {
						strByybHb = strByyb;
					}
				}
				break;
			case "WX":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strWx = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// 微信合并
				if (!"".equals(strWxHb) && strWxHb != null) {
					if (!"".equals(strWx) && strWx != null) {
						strWxHb = strByybHb + strSyncSep + strWx;
					}
				} else {
					if (!"".equals(strWx) && strWx != null) {
						strWxHb = strWx;
					}
				}
				break;
			case "SKY":
				sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				strSkype = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				// Skype合并
				if (!"".equals(strSkypeHb) && strSkypeHb != null) {
					if (!"".equals(strSkype) && strSkype != null) {
						strSkypeHb = strByybHb + strSyncSep + strSkype;
					}
				} else {
					if (!"".equals(strSkype) && strSkype != null) {
						strSkypeHb = strSkype;
					}
				}
				break;
			case "IDCARD":
				/* 证件号码同步 */
				if ("CertificateNum".equals(strComLmc)) {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = 'com_CerNum'";
					strIdCard = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" }, "String");

				} else {
					sql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
					strIdCard = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh },
							"String");
				}
				break;
			}
		}

		// 查询注册信息中的数据
		/* 主要手机 */
		String strZysjZc = "";
		/* 备用手机 */
		String strBysjZc = "";
		/* 主要电话 */
		String strZydhZc = "";
		/* 备用电话 */
		String strBydhZc = "";
		/* 主要邮箱 */
		String strZyyxZc = "";
		/* 备用邮箱 */
		String strByyxZc = "";
		/* 主要地址 */
		String strZydzZc = "";
		/* 主要邮编 */
		String strZyybZc = "";
		/* 备要地址 */
		String strBydzZc = "";
		/* 备要邮编 */
		String strByybZc = "";
		/* 微信 */
		String strWxZc = "";
		/* skype帐号 */
		String strSkypeZc = "";

		String sqlGetZcInfo = "SELECT TZ_ZY_SJ,TZ_CY_SJ,TZ_ZY_DH,TZ_CY_DH,TZ_ZY_EMAIL,TZ_CY_EMAIL,TZ_ZY_TXDZ,TZ_ZY_TXYB,TZ_CY_TXDZ,TZ_CY_TXYB,TZ_WEIXIN,TZ_SKYPE FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = ? AND TZ_LYDX_ID = ?";
		Map<String, Object> MapGetZcInfo = sqlQuery.queryForMap(sqlGetZcInfo, new Object[] { "ZCYH", strAppOprId });
		if (MapGetZcInfo != null) {
			strZysjZc = MapGetZcInfo.get("TZ_ZY_SJ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_SJ"));
			strBysjZc = MapGetZcInfo.get("TZ_CY_SJ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_SJ"));
			strZydhZc = MapGetZcInfo.get("TZ_ZY_DH") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_DH"));
			strBydhZc = MapGetZcInfo.get("TZ_CY_DH") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_DH"));
			strZyyxZc = MapGetZcInfo.get("TZ_ZY_EMAIL") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_EMAIL"));
			strByyxZc = MapGetZcInfo.get("TZ_CY_EMAIL") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_EMAIL"));
			strZydzZc = MapGetZcInfo.get("TZ_ZY_TXDZ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_TXDZ"));
			strZyybZc = MapGetZcInfo.get("TZ_ZY_TXYB") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_ZY_TXYB"));
			strBydzZc = MapGetZcInfo.get("TZ_CY_TXDZ") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_TXDZ"));
			strByybZc = MapGetZcInfo.get("TZ_CY_TXYB") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_CY_TXYB"));
			strWxZc = MapGetZcInfo.get("TZ_WEIXIN") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_WEIXIN"));
			strSkypeZc = MapGetZcInfo.get("TZ_SKYPE") == null ? "" : String.valueOf(MapGetZcInfo.get("TZ_SKYPE"));
			if ("".equals(strZysjHb) || strZysjHb == null) {
				strZysjHb = strZysjZc;
			}
			if ("".equals(strBysjHb) || strBysjHb == null) {
				strBysjHb = strBysjZc;
			}
			if ("".equals(strZydhHb) || strZydhHb == null) {
				strZydhHb = strZydhZc;
			}
			if ("".equals(strBydhHb) || strBydhHb == null) {
				strBydhHb = strBydhZc;
			}
			if ("".equals(strZyyxHb) || strZyyxHb == null) {
				strZyyxHb = strZyyxZc;
			}
			if ("".equals(strByyxHb) || strByyxHb == null) {
				strByyxHb = strByyxZc;
			}
			if ("".equals(strZydzHb) || strZydzHb == null) {
				strZydzHb = strZydzZc;
			}
			if ("".equals(strZyybHb) || strZyybHb == null) {
				strZyybHb = strZyybZc;
			}
			if ("".equals(strBydzHb) || strBydzHb == null) {
				strBydzHb = strBydzZc;
			}
			if ("".equals(strByybHb) || strByybHb == null) {
				strByybHb = strByybZc;
			}
			if ("".equals(strWxHb) || strWxHb == null) {
				strWxHb = strWxZc;
			}
			if ("".equals(strSkypeHb) || strSkypeHb == null) {
				strSkypeHb = strSkypeZc;
			}
		}

		// mysql如果字符过长会报错，需要截取长度
		if (strZysjHb.length() > 20) {
			strZysjHb = strZysjHb.substring(0, 20);
		}
		if (strBysjHb.length() > 20) {
			strBysjHb = strBysjHb.substring(0, 20);
		}
		if (strZydhHb.length() > 20) {
			strZydhHb = strZydhHb.substring(0, 20);
		}
		if (strBydhHb.length() > 20) {
			strBydhHb = strBydhHb.substring(0, 20);
		}
		if (strZyyxHb.length() > 100) {
			strZyyxHb = strZyyxHb.substring(0, 100);
		}
		if (strByyxHb.length() > 100) {
			strByyxHb = strByyxHb.substring(0, 100);
		}
		if (strZydzHb.length() > 254) {
			strZydzHb = strZydzHb.substring(0, 254);
		}
		if (strZyybHb.length() > 10) {
			strZyybHb = strZyybHb.substring(0, 10);
		}
		if (strBydzHb.length() > 254) {
			strBydzHb = strBydzHb.substring(0, 254);
		}
		if (strByybHb.length() > 10) {
			strByybHb = strByybHb.substring(0, 10);
		}
		if (strWxHb.length() > 20) {
			strWxHb = strWxHb.substring(0, 20);
		}
		if (strSkypeHb.length() > 70) {
			strSkypeHb = strSkypeHb.substring(0, 70);
		}
		if (strIdCard.length() > 20) {
			strIdCard = strIdCard.substring(0, 20);
		}

		String sqlCount = "SELECT COUNT(1) FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY = 'ZSBM' AND TZ_LYDX_ID = ?";
		String strAppInsId = String.valueOf(numAppInsId);
		int count = sqlQuery.queryForObject(sqlCount, new Object[] { strAppInsId }, "Integer");
		if (count > 0) {
			PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
			psTzLxfsInfoTbl.setTzLxfsLy("ZSBM");
			psTzLxfsInfoTbl.setTzLydxId(strAppInsId);
			psTzLxfsInfoTbl.setTzZySj(strZysjHb);
			psTzLxfsInfoTbl.setTzCySj(strBysjHb);
			psTzLxfsInfoTbl.setTzZyDh(strZydhHb);
			psTzLxfsInfoTbl.setTzCyDh(strBydhHb);
			psTzLxfsInfoTbl.setTzZyEmail(strZyyxHb);
			psTzLxfsInfoTbl.setTzCyEmail(strByyxHb);
			psTzLxfsInfoTbl.setTzZyTxdz(strZydzHb);
			psTzLxfsInfoTbl.setTzZyTxyb(strZyybHb);
			psTzLxfsInfoTbl.setTzCyTxdz(strBydzHb);
			psTzLxfsInfoTbl.setTzCyTxyb(strByybHb);
			psTzLxfsInfoTbl.setTzWeixin(strWxHb);
			psTzLxfsInfoTbl.setTzSkype(strSkypeHb);
			psTzLxfsInfoTblMapper.updateByPrimaryKeySelective(psTzLxfsInfoTbl);
		} else {
			PsTzLxfsInfoTbl psTzLxfsInfoTbl = new PsTzLxfsInfoTbl();
			psTzLxfsInfoTbl.setTzLxfsLy("ZSBM");
			psTzLxfsInfoTbl.setTzLydxId(strAppInsId);
			psTzLxfsInfoTbl.setTzZySj(strZysjHb);
			psTzLxfsInfoTbl.setTzCySj(strBysjHb);
			psTzLxfsInfoTbl.setTzZyDh(strZydhHb);
			psTzLxfsInfoTbl.setTzCyDh(strBydhHb);
			psTzLxfsInfoTbl.setTzZyEmail(strZyyxHb);
			psTzLxfsInfoTbl.setTzCyEmail(strByyxHb);
			psTzLxfsInfoTbl.setTzZyTxdz(strZydzHb);
			psTzLxfsInfoTbl.setTzZyTxyb(strZyybHb);
			psTzLxfsInfoTbl.setTzCyTxdz(strBydzHb);
			psTzLxfsInfoTbl.setTzCyTxyb(strByybHb);
			psTzLxfsInfoTbl.setTzWeixin(strWxHb);
			psTzLxfsInfoTbl.setTzSkype(strSkypeHb);
			psTzLxfsInfoTblMapper.insert(psTzLxfsInfoTbl);
		}

		/* 同步身份证信息 */
		if (!"".equals(strIdCard) && strIdCard != null) {
			String sqlRegInfoCount = "SELECT COUNT(1) FROM PS_TZ_REG_USER_T WHERE OPRID = ?";

			int regInfocount = sqlQuery.queryForObject(sqlRegInfoCount, new Object[] { strAppOprId }, "Integer");
			if (regInfocount > 0) {
				PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
				psTzRegUserT.setOprid(strAppOprId);
				psTzRegUserT.setNationalId(strIdCard);
				psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);
			}
		}
	}

	/* 报名表提交后发送邮件 */
	public String sendSubmitEmail(Long numAppInsId, String strTplId, String strAppOprId, String strAppOrgId,
			String strTplType) {

		String returnMsg = "true";

		// 收件人Email
		String strEmail = "";
		// 收件人姓名
		String strName = "";
		// 邮件模版
		String strEmlTmpId = "";
		String sql = "SELECT TZ_EML_MODAL_ID FROM PS_TZ_APPTPL_DY_T WHERE TZ_APP_TPL_ID = ? AND TZ_ISSENDMAIL = 'Y'";
		strEmlTmpId = sqlQuery.queryForObject(sql, new Object[] { strTplId }, "String");
		if (!"".equals(strEmlTmpId) && strEmlTmpId != null) {
			if ("BMB".equals(strTplType)) {
				sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
				strName = sqlQuery.queryForObject(sql, new Object[] { strAppOprId }, "String");
				sql = "SELECT TZ_ZY_EMAIL FROM PS_TZ_LXFSINFO_TBL WHERE TZ_LXFS_LY='ZSBM' AND TZ_LYDX_ID=?";
				strEmail = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
				// 创建邮件短信发送任务
				String strTaskId = createTaskServiceImpl.createTaskIns(strAppOrgId, strEmlTmpId, "MAL", "A");
				if (strTaskId == null || "".equals(strTaskId)) {
					return "false";
				}
				// 创建短信、邮件发送的听众;
				String createAudience = createTaskServiceImpl.createAudience(strTaskId, strAppOrgId, "报名表提交发送邮件",
						"BMB");
				if ("".equals(createAudience) || createAudience == null) {
					return "false";
				}
				// 为听众添加听众成员
				boolean addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", strEmail,
						"", "", strAppOprId, "", "", String.valueOf(numAppInsId));
				if (!addAudCy) {
					return "false";
				}
				// 得到创建的任务ID
				if ("".equals(strTaskId) || strTaskId == null) {
					return "false";
				} else {
					// 发送邮件
					sendSmsOrMalServiceImpl.send(strTaskId, "");
				}
			} else {
				return "true";
			}
		} else {
			return "true";
		}
		return returnMsg;
	}

	/**
	 * 发送站内信
	 * 
	 * @param numAppInsId
	 * @param siteEmailID
	 * @param strAppOprId
	 * @param strAppOrgId
	 * @param strAudienceDesc
	 * @param strAudLy
	 * @param strTplId
	 * @return
	 */
	public String sendSiteEmail(Long numAppInsId, String siteEmailID, String strAppOprId, String strAppOrgId,
			String strAudienceDesc, String strAudLy, String strRefLetterId) {
		String sql = "";
		// 推荐信提交成功的需要特殊处理
		if (siteEmailID.equals("TZ_TJX_SUBSUC")) {
			sql = "SELECT OPRID FROM PS_TZ_KS_TJX_TBL WHERE TZ_TJX_APP_INS_ID=?";
			strAppOprId = sqlQuery.queryForObject(sql, new Object[] { String.valueOf(numAppInsId) }, "String");
			sql = "SELECT TZ_JG_ID FROM PS_TZ_APPTPL_DY_T A,PS_TZ_APP_INS_T B WHERE A.TZ_APP_TPL_ID=B.TZ_APP_TPL_ID AND B.TZ_APP_INS_ID=?";
			strAppOrgId = sqlQuery.queryForObject(sql, new Object[] { String.valueOf(numAppInsId) }, "String");

		}

		System.out.println("strAppOprId:" + strAppOprId);
		System.out.println("strAppOrgId:" + strAppOrgId);

		String returnMsg = "true";
		// 收件人姓名
		String strName = "";
		sql = "SELECT TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL WHERE OPRID=?";
		strName = sqlQuery.queryForObject(sql, new Object[] { strAppOprId }, "String");

		// 创建站内信发送任务 创建任务的时候，类型为“ZNX”， oprid是收站内信的人。 手机和邮箱为空字符串就可以了
		String strTaskId = createTaskServiceImpl.createTaskIns(strAppOrgId, siteEmailID, "ZNX", "A");
		if (strTaskId == null || "".equals(strTaskId)) {
			return "false";
		}
		// 创建听众;
		String createAudience = createTaskServiceImpl.createAudience(strTaskId, strAppOrgId, strAudienceDesc, strAudLy);
		if ("".equals(createAudience) || createAudience == null) {
			return "false";
		}
		// 为听众添加听众成员
		boolean addAudCy = false;
		if (siteEmailID.equals("TZ_TJX_SUBSUC")) {
			addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", "", "", "", strAppOprId,
					"", "", strRefLetterId);
		} else {
			addAudCy = createTaskServiceImpl.addAudCy(createAudience, strName, strName, "", "", "", "", "", strAppOprId,
					"", "", String.valueOf(numAppInsId));
		}

		if (!addAudCy) {
			return "false";
		}
		// 得到创建的任务ID
		if ("".equals(strTaskId) || strTaskId == null) {
			return "false";
		} else {
			// 发送
			sendSmsOrMalServiceImpl.send(strTaskId, "");
		}

		return returnMsg;
	}

	// 将json数据解析保存到报名表存储表
	private void savePerXxxIns2(String strParentItemId, String strOtherValue, Map<String, Object> xxxObject,
			Long numAppInsId) {

		String strIsChecked = "";
		if (xxxObject.containsKey("checked")) {
			strIsChecked = xxxObject.get("checked") == null ? "" : String.valueOf(xxxObject.get("checked"));
		}
		if (!"Y".equals(strIsChecked)) {
			strIsChecked = "N";
		}
		String strCode = "";
		if (xxxObject.containsKey("code")) {
			strCode = xxxObject.get("code") == null ? "" : String.valueOf(xxxObject.get("code"));
		}
		String strTxt = "";
		if (xxxObject.containsKey("txt")) {
			strTxt = xxxObject.get("txt") == null ? "" : String.valueOf(xxxObject.get("txt"));
		}
		if (xxxObject.containsKey("othervalue")) {
			strOtherValue = xxxObject.get("othervalue") == null ? "" : String.valueOf(xxxObject.get("othervalue"));
		}

		PsTzAppDhccT psTzAppDhccT = new PsTzAppDhccT();
		psTzAppDhccT.setTzAppInsId(numAppInsId);
		psTzAppDhccT.setTzXxxBh(strParentItemId);
		psTzAppDhccT.setTzIsChecked(strIsChecked);
		psTzAppDhccT.setTzXxxkxzMc(strCode);
		psTzAppDhccT.setTzAppSText(strTxt);
		psTzAppDhccT.setTzKxxQtz(strOtherValue);
		psTzAppDhccTMapper.insert(psTzAppDhccT);

	}

	// MBA报名表提交后需要回写表(注：写死的自适应于清华MBA配置的报名表)
	public void savaAppKsInfoExt(Long numAppInsId, String strAppOprId) {
		System.out.println("保存注册信息开始");
		DateUtil DateTrans = new DateUtil();
		Map<String, String> ksMap = new HashMap<String, String>();
		String ksMapkey = "";
		String ksMapvalue = "";

		Map<String, String> sex_ksMap = new HashMap<String, String>();
		String sex_ksMapkey = "";
		String sex_ksMapvalue = "";

		// 真实姓名
		String name = "";
		// 考生性别
		String sex = "";
		// 出生日期
		String brithday = "";
		// 本科院校国家
		String uniScholContry = "";
		// 本科院校国家英文简写
		String uniScholContryEn = "";
		// 本科院校名称
		String uniSchoolName = "";
		// 本科毕业时间
		String unipsoinTime = "";
		// 本科专业
		String unimajor = "";
		// 本科专业类型
		String unimajortype = "";
		// 最高学历
		String maxHeight = "";
		// 最高学历毕业时间
		String maxHeighTime = "";
		// 是否拥有海外学历
		String isOutLeft = "";
		// 工作所在省市
		String workProvince = "";
		// 工作单位
		String workPlace = "";
		// 公司性质
		String compNautre = "";
		// 行业类别
		String compaType = "";
		// 工作部门
		String workDepart = "";
		// 工作职能类型
		String workznlx = "";
		// 工作职位
		String jobPositio = "";
		// 职位类型
		String positiType = "";
		// 邮寄地址
		String Address = "";
		// 邮政编码
		String AddCode = "";
		// 紧急联系人
		String conName = "";
		// 紧急联系人性别
		String connameSex = "";
		// 紧急联系人电话
		String connamePhone = "";
		// 管理工作年限
		String AdminworkYear = "";
		// 直接下属人数
		String partperNum = "";
		// 年收入
		String income = "";
		// 国家
		String Contry1 = "";
		String Contry2 = "";
		String Contry3 = "";
		// 邮箱
		String email = "";
		// 电话
		String mobilPhone = "";
		// 是否自主创业
		String isOwrCompany = "";
		// 报考自愿方向
		String bkfx = "";
		// 证件号
		String idnum = "";

		// String
		try {
			String ks_sex = "SELECT TZ_XXX_BH,TZ_XXXKXZ_MC FROM PS_TZ_APP_DHCC_T WHERE TZ_APP_INS_ID=? AND TZ_IS_CHECKED='Y'";
			List<Map<String, Object>> listMap_sex = sqlQuery.queryForList(ks_sex, new Object[] { numAppInsId });
			for (Map<String, Object> map : listMap_sex) {
				sex_ksMapkey = map.get("TZ_XXX_BH").toString();
				sex_ksMapvalue = map.get("TZ_XXXKXZ_MC").toString();
				sex_ksMap.put(sex_ksMapkey, sex_ksMapvalue);
				if (sex_ksMap.containsKey(sex_ksMapkey)) {

				}
			}

			String ks_valuesql = "SELECT TZ_XXX_BH,TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T WHERE  TZ_APP_INS_ID=? ";
			List<Map<String, Object>> listMap = sqlQuery.queryForList(ks_valuesql, new Object[] { numAppInsId });
			for (Map<String, Object> map : listMap) {
				ksMapkey = map.get("TZ_XXX_BH").toString();
				ksMapvalue = map.get("TZ_APP_S_TEXT").toString();
				ksMap.put(ksMapkey, ksMapvalue);
			}
			for (Map.Entry<String, String> entry : ksMap.entrySet()) {

				/// System.out.println("key= " + entry.getKey() + " and value= "
				/// + entry.getValue());

			}
			name = ksMap.get("TZ_6name");

			sex = sex_ksMap.get("TZ_6gender");

			brithday = ksMap.get("TZ_6birthday");

			uniScholContry = ksMap.get("TZ_11luniversitycountry");
			uniScholContryEn = sqlQuery.queryForObject("SELECT country from PS_COUNTRY_TBL where descr=?",
					new Object[] { uniScholContry }, "String");

			Contry1 = ksMap.get("TZ_10hdegreeunicountry");
			Contry2 = ksMap.get("TZ_12ouniversitycountry");
			Contry3 = ksMap.get("TZ_13ouniver3country");

			// System.out.println(uniScholContry + ":" + Contry1 + ":" + Contry2
			// + ":" + Contry3);
			// 判断 是否有海外学历
			if ((ksMap.get("TZ_11luniversitycountry") == null
					|| "".equals(uniScholContry))
							? true
							: uniScholContry.equals("中国大陆")
									&& (ksMap.get("TZ_10hdegreeunicountry") == null || "".equals(Contry1))
											? true
											: Contry1.equals("中国大陆")
													&& (ksMap.get("TZ_12ouniversitycountry") == null
															|| "".equals(Contry2))
																	? true
																	: Contry2.equals("中国大陆") && (ksMap
																			.get("TZ_13ouniver3country") == null
																			|| "".equals(Contry3)) ? true
																					: Contry3.equals("中国大陆")) {
				isOutLeft = String.valueOf('N');
			} else {
				isOutLeft = String.valueOf('Y');
			}

			uniSchoolName = ksMap.get("TZ_11luniversitysch");

			unipsoinTime = ksMap.get("TZ_11periodcom_enddate");

			unimajor = ksMap.get("TZ_11major");

			unimajortype = ksMap.get("TZ_11majortype");

			maxHeight = ksMap.get("TZ_10highdegree");

			maxHeighTime = ksMap.get("TZ_10hdegreeperiodcom_enddate");

			workProvince = ksMap.get("TZ_20TZ_TZ_20_9");

			workPlace = ksMap.get("TZ_20TZ_TZ_20_6");

			compNautre = ksMap.get("TZ_20TZ_TZ_20_14firm_type");

			compaType = ksMap.get("TZ_20TZ_TZ_20_13");

			workDepart = ksMap.get("TZ_20TZ_TZ_20_15");

			workznlx = ksMap.get("TZ_20TZ_TZ_20_30");

			jobPositio = ksMap.get("TZ_20TZ_TZ_20_16");

			positiType = ksMap.get("TZ_20TZ_TZ_20_14position_type");

			Address = ksMap.get("TZ_6address");

			AddCode = ksMap.get("TZ_6code");

			conName = ksMap.get("TZ_8conname");

			connameSex = sex_ksMap.get("TZ_8congender");

			connamePhone = ksMap.get("TZ_8conmobile");

			AdminworkYear = ksMap.get("TZ_20TZ_TZ_20_3");

			partperNum = ksMap.get("TZ_20TZ_TZ_20_23");

			income = ksMap.get("TZ_20TZ_TZ_20_21");

			bkfx = ksMap.get("TZ_3TZ_TZ_3_3");

			idnum = ksMap.get("TZ_6idnum");

			if (ksMap.get("TZ_17TZ_TZ_17_1") == null || ksMap.get("TZ_17TZ_TZ_17_1").equals("")
					|| ksMap.get("TZ_17TZ_TZ_17_1").equals(" ")) {
				isOwrCompany = "N";
			} else {
				isOwrCompany = "Y";
			}

			email = ksMap.get("TZ_6email");
			mobilPhone = ksMap.get("TZ_6mobile");

			String sql_ReisY = "SELECT 'Y' FROM PS_TZ_REG_USER_T WHERE OPRID=?";

			PsTzRegUserT psTzRegUserT = new PsTzRegUserT();
			psTzRegUserT.setOprid(strAppOprId);
			psTzRegUserT.setTzRealname(name);
			psTzRegUserT.setTzGender(sex);
			if (brithday == null) {
				brithday = "";
			}
			psTzRegUserT.setBirthdate(DateUtil.parse(brithday));
			psTzRegUserT.setTzSchCountry(uniScholContryEn);
			psTzRegUserT.setTzSchCname(uniSchoolName);
			psTzRegUserT.setTzComment1(unipsoinTime);
			psTzRegUserT.setTzComment17(unimajor);
			psTzRegUserT.setTzComment2(unimajortype);
			psTzRegUserT.setTzHighestEdu(maxHeight);
			psTzRegUserT.setTzComment3(maxHeighTime);
			psTzRegUserT.setTzComment4(isOutLeft);
			psTzRegUserT.setTzLenProid(workProvince);
			psTzRegUserT.setTzCompanyName(workPlace);
			psTzRegUserT.setTzComment5(compNautre);
			psTzRegUserT.setTzCompIndustry(compaType);
			psTzRegUserT.setTzDeptment(workDepart);
			psTzRegUserT.setTzComment16(jobPositio);
			psTzRegUserT.setTzComment15(workznlx);
			psTzRegUserT.setTzComment7(Address);
			psTzRegUserT.setTzComment8(AddCode);
			psTzRegUserT.setTzComment9(conName);
			psTzRegUserT.setTzComment10(connameSex);
			psTzRegUserT.setTzComment11(connamePhone);
			psTzRegUserT.setTzComment12(AdminworkYear);
			psTzRegUserT.setTzComment13(partperNum);
			psTzRegUserT.setTzComment14(income);
			psTzRegUserT.setNationalId(idnum);
			psTzRegUserT.setNationalIdType("A");// 证件类型A：为身份证
			psTzRegUserT.setTzAllowApply("N");// 添加是否允许继续申请 为N/不允许

			String ReisY = sqlQuery.queryForObject(sql_ReisY, new Object[] { strAppOprId }, "String");
			if (ReisY != null && ReisY.equals("Y")) {
				psTzRegUserTMapper.updateByPrimaryKeySelective(psTzRegUserT);

			} else {
				psTzRegUserTMapper.insertSelective(psTzRegUserT);
			}

			String sql_LxisY = "SELECT 'Y' FROM PS_TZ_LXFSINFO_TBL  WHERE TZ_LXFS_LY='ZCYH' AND TZ_LYDX_ID=?";
			PsTzLxfsInfoTbl psTzLxfInfoTbl = new PsTzLxfsInfoTbl();
			psTzLxfInfoTbl.setTzLxfsLy("ZCYH");
			psTzLxfInfoTbl.setTzLydxId(strAppOprId);
			psTzLxfInfoTbl.setTzZyEmail(email);
			psTzLxfInfoTbl.setTzZySj(mobilPhone);
			String LxisY = sqlQuery.queryForObject(sql_LxisY, new Object[] { strAppOprId }, "String");
			// System.out.println("LxisY:" + LxisY);
			if (LxisY != null && LxisY.equals("Y")) {

				psTzLxfsInfoTblMapper.updateByPrimaryKeySelective(psTzLxfInfoTbl);

			} else {
				psTzLxfsInfoTblMapper.insertSelective(psTzLxfInfoTbl);

			}
			String sql_ksExtisY = "SELECT 'Y' FROM  PS_TZ_APP_KS_INFO_EXT_T WHERE TZ_OPRID=?";
			PsTzAppKsInExtTbl psTzAppKsInExtTbl = new PsTzAppKsInExtTbl();
			String ksExtisY = sqlQuery.queryForObject(sql_ksExtisY, new Object[] { strAppOprId }, "String");
			psTzAppKsInExtTbl.setTzOprid(strAppOprId);
			psTzAppKsInExtTbl.setTzAppMajorName(bkfx);
			psTzAppKsInExtTbl.setTzSelfEmpFlg(isOwrCompany);
			if (ksExtisY != null && ksExtisY.equals("Y")) {

				psTzAppKsInExtTblMapper.updateByPrimaryKeySelective(psTzAppKsInExtTbl);

			} else {
				psTzAppKsInExtTblMapper.insertSelective(psTzAppKsInExtTbl);
			}
			
			//同步注册信息表 PS_TZ_AQ_YHXX_TBL;
			
		String updateSql="UPDATE PS_TZ_AQ_YHXX_TBL SET TZ_REALNAME=? WHERE OPRID=? AND TZ_JG_ID='SEM' AND TZ_DLZH_ID=?"; 
			sqlQuery.update(updateSql,new Object[] { name,strAppOprId,strAppOprId });

		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}

	// 空校验，填写了信息则直接返回
	public boolean blankValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc,
			String strComMc) {

		String strDxxxBh = "";

		String strXxxBhLike = "";

		String sql = "";

		String getChildrenSql = "";

		String strXxxValue = "";

		boolean b_flag = false;

		try {

			switch (strComMc) {
			case "EduExperience":
				break;
			case "workExperience":
				break;
			case "DHContainer":
				break;
			case "LayoutControls":
				break;
			case "DateComboBox":
				int numLineNum = 0;
				String strToToday = "";
				String strStartDate = "";
				String strEndDate = "";
				// 查看是否在容器中
				sql = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 0,1";
				strDxxxBh = sqlQuery.queryForObject(sql, new Object[] { strTplId, strXxxBh }, "String");
				if (!"".equals(strDxxxBh) && strDxxxBh != null) {
					strXxxBhLike = strDxxxBh + strXxxBh;
				} else {
					strDxxxBh = strXxxBh;
					strXxxBhLike = strXxxBh;
				}
				// System.out.println("11111");
				getChildrenSql = "SELECT DISTINCT TZ_LINE_NUM FROM PS_TZ_APP_CC_VW2 WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ?";

				List<?> ListLineNum = sqlQuery.queryForList(getChildrenSql,
						new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" });
				String sqlGetDate = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_VW2 WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = ? AND TZ_LINE_NUM = ?";
				for (Object ObjLineNum : ListLineNum) {
					Map<Integer, Object> mapObjLineNum = (Map<Integer, Object>) ObjLineNum;
					numLineNum = mapObjLineNum.get("TZ_LINE_NUM") == null ? 0
							: ((Long) mapObjLineNum.get("TZ_LINE_NUM")).intValue();

					// System.out.println("numLineNum:" + numLineNum);

					// sqlGetDate =
					strToToday = sqlQuery.queryForObject(sqlGetDate, new Object[] { numAppInsId, strTplId, strDxxxBh,
							strXxxBhLike + "%", "com_todate", numLineNum }, "String");
					strStartDate = sqlQuery.queryForObject(sqlGetDate, new Object[] { numAppInsId, strTplId, strDxxxBh,
							strXxxBhLike + "%", "com_startdate", numLineNum }, "String");
					strEndDate = sqlQuery.queryForObject(sqlGetDate, new Object[] { numAppInsId, strTplId, strDxxxBh,
							strXxxBhLike + "%", "com_enddate", numLineNum }, "String");
					if ("Y".equals(strToToday)) {
						b_flag = true;
						break;
					} else {
						if ("".equals(strStartDate) || strStartDate == null || "".equals(strEndDate)
								|| strEndDate == null) {
						} else {
							b_flag = true;
							break;
						}
					}
				}
				break;
			case "BirthdayAndAge":
			case "mobilePhone":
			case "YearsAndMonth":
				// 查看是否在容器中
				sql = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 0,1";
				strDxxxBh = sqlQuery.queryForObject(sql, new Object[] { strTplId, strXxxBh }, "String");
				if (!"".equals(strDxxxBh) && strDxxxBh != null) {
					strXxxBhLike = strDxxxBh + strXxxBh;
				} else {
					strDxxxBh = strXxxBh;
					strXxxBhLike = strXxxBh;
				}

				getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ?";

				List<?> ListValues = sqlQuery.queryForList(getChildrenSql,
						new Object[] { numAppInsId, strTplId, strDxxxBh, strXxxBhLike + "%" });
				for (Object ObjValue : ListValues) {
					Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
					strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
					if ("".equals(strXxxValue) || strXxxValue == null) {
					} else {
						b_flag = true;
						break;
					}
				}
				break;
			case "AttachmentUpload":
			case "imagesUpload":
				int numFile = 0;
				sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_ATT_HD_JY_SQL");
				numFile = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, numAppInsId, strTplId, strXxxBh },
						"Integer");
				if (numFile == 0) {
					b_flag = true;
				}
				break;
			case "Radio":
			case "Check":
				String strXxxBh2 = "";
				sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_XXX_OPTION_CHECK_SQL");
				strXxxBh2 = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				if (!"".equals(strXxxBh2) && strXxxBh2 != null) {
					b_flag = true;
					break;
				}
				break;
			case "ChooseClass": // 班级选择控件，校验批次是否选择了
				// String strXxxBh2 = "";
				// System.out.println("11111");
				sql = "select TZ_APP_S_TEXT from PS_TZ_APP_CC_T where TZ_APP_INS_ID =? and TZ_XXX_BH like '%CC_Batch' ";
				// System.out.println("sql:"+sql);
				strXxxBh2 = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
				// System.out.println("strXxxBh2:"+strXxxBh2);
				if ("".equals(strXxxBh2) || strXxxBh2 == null) {
				} else {
					b_flag = true;
				}
				break;
			case "CheckBox":
				getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? AND TZ_IS_HIDDEN <> 'Y'";

				List<?> ListValues1 = sqlQuery.queryForList(getChildrenSql,
						new Object[] { numAppInsId, strTplId, strXxxBh });
				if (ListValues1 != null) {
					for (Object ObjValue : ListValues1) {
						Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
						strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
						if ("".equals(strXxxValue) || strXxxValue == null || "N".equals(strXxxValue)) {
							// 校验失败
						} else {
							b_flag = true;
							break;
						}
					}
				}
				break;
			// 公司性质后台校验:
			case "FirmType":

				getChildrenSql = "select * from PS_TZ_APP_CC_T where TZ_APP_INS_ID=? AND TZ_XXX_BH LIKE ?";
				// 区分"公司性质"和"岗位性质":
				String opts[] = new String[] { "firm_type", "position_type" };
				// System.out.println(strComMc);
				System.out.println(strXxxBh);
				// exam_type exam_score exam_date
				for (String opt : opts) {
					Map<String, Object> valMap = new HashMap<String, Object>();
					valMap = sqlQuery.queryForMap(getChildrenSql,
							new Object[] { numAppInsId, "%" + strXxxBh + opt + "%" });
					if (valMap != null) {
						strXxxValue = valMap.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(valMap.get("TZ_APP_S_TEXT"));
						if ("".equals(strXxxValue) || "-1".equals(strXxxValue)) {
						} else {
							b_flag = true;
							break;
						}
					}
				}
				break;
			// 英语水平后台校验
			case "EngLevl":
				getChildrenSql = "select * from PS_TZ_APP_CC_T where TZ_APP_INS_ID=? AND TZ_XXX_BH LIKE ?";
				// 附件部分验证:
				String getAttCount = "select COUNT(1) from PS_TZ_FORM_ATT_T where TZ_APP_INS_ID=? AND TZ_XXX_BH LIKE ?";
				// 查询行数:
				sql = "SELECT TZ_XXX_LINE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
				int comNum = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strXxxBh }, "int");

				for (int i = 0; i < comNum; i++) {
					Map<String, Object> valMap = new HashMap<String, Object>();// exam_score//exam_date
					String opt = "exam_type";
					if (i > 0) {
						opt = opt + "_" + i;
					}
					valMap = sqlQuery.queryForMap(getChildrenSql, new Object[] { numAppInsId, "%" + strXxxBh + opt });
					if (valMap != null) {
						strXxxValue = valMap.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(valMap.get("TZ_APP_S_TEXT"));
						if (strXxxValue.equals("无")) {
							b_flag = true;
							break;
						}
					}
					opt = "exam_upload";
					if (i > 0) {
						opt = opt + "_" + i;
					}
					// System.out.println("EngLevl-strXxxBh:"+strXxxBh+opt);
					int attCount = sqlQuery.queryForObject(getAttCount,
							new Object[] { numAppInsId, "%" + strXxxBh + opt }, "int");

					if (attCount == 0) {
					} else {
						b_flag = true;
						break;
					}
					// input部分验证:

				}
				if (b_flag) {
					break;
				}
				for (int i = 0; i < comNum; i++) {
					// 日期控件处理1.2.3.4.13含有日期 需要验证日期
					String hasDateOpt = "GRE-GMAT-TOEFL-IELTS-TOEIC（990）";
					// 考试类型 成绩 日期验证:
					// 1.验证成绩类型:
					Map<String, Object> valMap = new HashMap<String, Object>();// exam_score//exam_date
					String opt = "exam_type";
					if (i > 0) {
						opt = opt + "_" + i;
					}
					valMap = sqlQuery.queryForMap(getChildrenSql, new Object[] { numAppInsId, "%" + strXxxBh + opt });
					if (valMap != null) {
						strXxxValue = valMap.get("TZ_APP_S_TEXT") == null ? ""
								: String.valueOf(valMap.get("TZ_APP_S_TEXT"));
						if (strXxxValue.equals("无")) {
							continue;
						}
						if ("".equals(strXxxValue) || "-1".equals(strXxxValue)) {

						} else if (hasDateOpt.contains(strXxxValue)) {
							// 验证成绩+日期
							opt = "exam_score";
							if (i > 0) {
								opt = opt + "_" + i;
							}
							valMap = sqlQuery.queryForMap(getChildrenSql,
									new Object[] { numAppInsId, "%" + strXxxBh + opt });
							if (valMap != null) {
								strXxxValue = valMap.get("TZ_APP_S_TEXT") == null ? ""
										: String.valueOf(valMap.get("TZ_APP_S_TEXT"));
								if (strXxxValue.equals("")) {

								} else {
									b_flag = true;
									// returnMessage = this.getMsg(strXxxMc,
									// "考试成绩必填");
									break;
								}
							}
							// 验证日期：
							opt = "exam_date";
							if (i > 0) {
								opt = opt + "_" + i;
							}
							valMap = sqlQuery.queryForMap(getChildrenSql,
									new Object[] { numAppInsId, "%" + strXxxBh + opt });
							if (valMap != null) {
								strXxxValue = valMap.get("TZ_APP_S_TEXT") == null ? ""
										: String.valueOf(valMap.get("TZ_APP_S_TEXT"));
								if (strXxxValue.equals("")) {

								} else {
									b_flag = true;
									break;
								}
							}

						} else {
							// 验证成绩
							opt = "exam_score";
							if (i > 0) {
								opt = opt + "_" + i;
							}
							valMap = sqlQuery.queryForMap(getChildrenSql,
									new Object[] { numAppInsId, "%" + strXxxBh + opt });
							if (valMap != null) {
								strXxxValue = valMap.get("TZ_APP_S_TEXT") == null ? ""
										: String.valueOf(valMap.get("TZ_APP_S_TEXT"));
								if (strXxxValue.equals("")) {

								} else {
									b_flag = true;
									break;
								}
							}
						}
					}

				}
				break;
			default:
				getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? AND TZ_IS_HIDDEN <> 'Y'";

				List<?> ListValues2 = sqlQuery.queryForList(getChildrenSql,
						new Object[] { numAppInsId, strTplId, strXxxBh });
				for (Object ObjValue : ListValues2) {
					Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
					strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
					if ("".equals(strXxxValue) || strXxxValue == null) {

					} else {
						b_flag = true;
						break;
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b_flag;
	}

}
