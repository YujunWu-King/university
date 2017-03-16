package com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tranzvision.gd.util.base.ObjectDoMethod;
import com.tranzvision.gd.util.base.TzSystemException;
import com.tranzvision.gd.util.captcha.Patchca;
import com.tranzvision.gd.util.sql.SqlQuery;
import com.tranzvision.gd.util.sql.TZGDObject;

/**
 * 报名表后台校验表单；原：TZ_ONLINE_REG_PKG:TZ_GD_APP_UTILITY
 * 
 * @author 张彬彬
 * @since 2016-2-16
 */
@Service("com.tranzvision.gd.TZWebsiteApplicationBundle.service.impl.tzOnlineAppUtility")
public class tzOnlineAppUtility {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private SqlQuery sqlQuery;
	@Autowired
	private TZGDObject tzSQLObject;
	@Autowired
	private ApplicationContext ctx;

	public String requireValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo) {
		return "实例编号:" + String.valueOf(numAppInsId) + "模版编号:" + strTplId;
	};

	// 必填校验
	public String requireValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {

		String returnMessage = "";

		String strDxxxBh = "";

		String strXxxBhLike = "";

		String sql = "";

		String getChildrenSql = "";

		String strXxxValue = "";

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
					// sqlGetDate = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_VW2
					// WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND
					// TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = ?
					// AND TZ_LINE_NUM = ?";
					strStartDate = sqlQuery.queryForObject(sqlGetDate, new Object[] { numAppInsId, strTplId, strDxxxBh,
							strXxxBhLike + "%", "com_startdate", numLineNum }, "String");
					// sqlGetDate = "SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_VW2
					// WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND
					// TZ_D_XXX_BH = ? AND TZ_XXX_BH LIKE ? AND TZ_XXX_NO = ?
					// AND TZ_LINE_NUM = ?";
					strEndDate = sqlQuery.queryForObject(sqlGetDate, new Object[] { numAppInsId, strTplId, strDxxxBh,
							strXxxBhLike + "%", "com_enddate", numLineNum }, "String");
					if ("Y".equals(strToToday)) {
						if ("".equals(strStartDate) || strStartDate == null) {
							// 校验失败
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
						}
					} else {
						if ("".equals(strStartDate) || strStartDate == null || "".equals(strEndDate)
								|| strEndDate == null) {
							// 校验失败
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
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
						// 校验失败
						returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
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
				if (numFile > 0) {
					returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
				}
				break;
			case "Radio":
			case "Check":
				String strXxxBh2 = "";
				sql = tzSQLObject.getSQLText("SQL.TZWebsiteApplicationBundle.TZ_APP_XXX_OPTION_CHECK_SQL");
				strXxxBh2 = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strTplId, strXxxBh }, "String");
				if (!"".equals(strXxxBh2) && strXxxBh2 != null) {
					returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
				}
				break;
			case "ChooseClass": // 班级选择控件，校验批次是否选择了
				// String strXxxBh2 = "";
				//System.out.println("11111");
				sql = "select TZ_APP_S_TEXT from PS_TZ_APP_CC_T where TZ_APP_INS_ID =? and TZ_XXX_BH like '%CC_Batch' ";
				//System.out.println("sql:"+sql);
				strXxxBh2 = sqlQuery.queryForObject(sql, new Object[] { numAppInsId }, "String");
				//System.out.println("strXxxBh2:"+strXxxBh2);
				if ("".equals(strXxxBh2) || strXxxBh2 == null) {
					returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
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
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
				} else {
					// 校验失败
					returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
					break;
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
						// 校验失败
						// System.out.println(strXxxMc, strJygzTsxx);
						returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
						break;
					}

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnMessage;
	}

	// 英文字母校验
	public String ahphValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {
		String returnMessage = "";

		String strXxxValue = "";

		if ("EnglishAlphabet".equals(strComMc)) {
			// 英文字母格式校验
			String getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";

			List<?> ListValues = sqlQuery.queryForList(getChildrenSql,
					new Object[] { numAppInsId, strTplId, strXxxBh });
			for (Object ObjValue : ListValues) {
				Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
				strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
				if (!"".equals(strXxxValue) && strXxxValue != null) {
					// 校验失败
					boolean isAhphLetter = this.isEnglishLetter(strXxxValue);
					if (!isAhphLetter) {
						returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
						break;
					}
				}
			}
		}

		return returnMessage;
	}

	// 英文字母校验
	public String VerificationCodeValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc,
			String strComMc, int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax,
			String strXxxZsxzgs, String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz,
			String strXxxCharBz, int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax,
			String strXxxXsws, String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine,
			String strTjxSub, String strJygzTsxx) {
		String returnMessage = "";

		String strXxxValue = "";

		if ("VerificationCode".equals(strComMc)) {
			// 验证码格式校验
			String getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";

			List<?> ListValues = sqlQuery.queryForList(getChildrenSql,
					new Object[] { numAppInsId, strTplId, strXxxBh });
			for (Object ObjValue : ListValues) {
				@SuppressWarnings("unchecked")
				Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
				strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
				if (!"".equals(strXxxValue) && strXxxValue != null) {
					// 校验验证码
					Patchca patchca = new Patchca();
					if (!patchca.verifyToken(request, strXxxValue)) {
						returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
						break;
					}
				}
			}
		}

		return returnMessage;
	}

	// 字符长度校验
	public String charLenValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {
		String returnMessage = "";

		String getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";

		String strXxxValue = "";

		List<?> ListValues = sqlQuery.queryForList(getChildrenSql, new Object[] { numAppInsId, strTplId, strXxxBh });
		for (Object ObjValue : ListValues) {
			Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
			strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
			if (!"".equals(strXxxValue) && strXxxValue != null) {
				// 校验失败
				int numXxxValueLen = strXxxValue.length();
				if (numXxxMinlen > 0) {
					if (numXxxValueLen < numXxxMinlen) {
						returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
					}
				}
				if ("".equals(returnMessage)) {
					if (numXxxMaxlen > 0) {
						if (numXxxValueLen > numXxxMaxlen) {
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
				}
			}
		}

		return returnMessage;
	}

	// 多行文本框 行数校验
	public String rowLenValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {
		String returnMessage = "";

		String getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";

		String strXxxValue = "";

		List<?> ListValues = sqlQuery.queryForList(getChildrenSql, new Object[] { numAppInsId, strTplId, strXxxBh });
		for (Object ObjValue : ListValues) {
			Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
			strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
			if (!"".equals(strXxxValue) && strXxxValue != null) {
				// 校验失败
				int numXxxValueLen = this.getCount(strXxxValue, "\n") + 1;
				System.out.println("strXxxValue:" + numXxxValueLen);
				System.out.println("numXxxMinLine:" + numXxxMinLine);
				System.out.println("numXxxMaxLine:" + numXxxMaxLine);
				if (numXxxMinLine > 0) {
					if (numXxxValueLen < numXxxMinLine) {
						returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
					}
				}
				if ("".equals(returnMessage)) {
					if (numXxxMaxLine > 0) {
						if (numXxxValueLen > numXxxMaxLine) {
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
				}
			}
		}
		System.out.println(returnMessage);
		return returnMessage;
	}

	private int getCount(String str, String sub) {
		int index = 0;
		int count = 0;
		while ((index = str.indexOf(sub, index)) != -1) {

			index = index + sub.length();
			count++;
		}
		return count;
	}

	// 值校验
	public String valueValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {
		String returnMessage = "";

		String getChildrenSql = "";

		String strXxxValue = "";

		try {
			switch (strComMc) {
			case "DigitalTextBox":
				getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";

				strXxxValue = "";

				List<?> ListValues = sqlQuery.queryForList(getChildrenSql,
						new Object[] { numAppInsId, strTplId, strXxxBh });
				for (Object ObjValue : ListValues) {
					Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
					strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
					if (!"".equals(strXxxValue) && strXxxValue != null) {
						// 校验失败
						boolean isNumber = this.isNumber(strXxxValue);

						if (isNumber) {
							boolean isDouble = this.isDouble(strXxxValue);
							if (isDouble) {
								BigDecimal bd = new BigDecimal(String.valueOf(strXxxValue));
								int numScaleLen = bd.scale();
								if (this.isInteger(strXxxXsws)) {
									int numXxxXsws = Integer.parseInt(strXxxXsws);
									if (numScaleLen > numXxxXsws) {
										returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
										break;
									}
								}
							}

							if ("".equals(returnMessage)) {
								Double numValue = Double.parseDouble(strXxxValue);
								if (numValue > numXxxMax || numValue < numXxxMin) {
									returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
									break;
								}
							}
						} else {
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
				}
			case "DateInputBox":
				break;
			case "DateComboBox":
				break;
			case "BirthdayAndAge":
				break;
			case "Check":
				String sql = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 0,1";
				String strDxxxBh = sqlQuery.queryForObject(sql, new Object[] { strTplId, strXxxBh }, "String");
				if ("".equals(strDxxxBh) || strDxxxBh == null) {
					strDxxxBh = strXxxBh;
				}
				String sqlGetDhXxx = "SELECT DISTINCT TZ_XXX_BH FROM PS_TZ_APP_DHCC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";
				List<?> ListDhXxx = sqlQuery.queryForList(sqlGetDhXxx,
						new Object[] { numAppInsId, strTplId, strXxxBh });
				for (Object ObjDhXxx : ListDhXxx) {
					Map<String, Object> MapDhXxx = (Map<String, Object>) ObjDhXxx;
					String strXxxBh2 = MapDhXxx.get("TZ_XXX_BH") == null ? ""
							: String.valueOf(MapDhXxx.get("TZ_XXX_BH"));
					sql = "SELECT COUNT(1) FROM PS_TZ_APP_DHCC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? AND TZ_XXX_BH = ? AND TZ_IS_CHECKED = 'Y'";
					int numCheckCount = sqlQuery.queryForObject(sql,
							new Object[] { numAppInsId, strTplId, strXxxBh, strXxxBh2 }, "Integer");
					if (this.isInteger(strXxxZsxzgs)) {
						int numXxxZsxzgs = Integer.parseInt(strXxxZsxzgs);
						if (numXxxZsxzgs > 0 && numCheckCount < numXxxZsxzgs) {
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
					if (this.isInteger(strXxxZdxzgs)) {
						int numXxxZdxzgs = Integer.parseInt(strXxxZdxzgs);
						if (numXxxZdxzgs > 0 && numCheckCount > numXxxZdxzgs) {
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMessage;
	}

	// 规则校验
	public String regularValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {
		// System.out.println("---------regularValidator");
		String returnMessage = "";

		String getChildrenSql = "";

		String strXxxValue = "";
		// System.out.println("strXxxGdgsjy:"+strXxxGdgsjy);

		try {
			switch (strComMc) {
			case "SingleTextBox":
			case "MultilineTextBox":
				getChildrenSql = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ?";

				strXxxValue = "";

				List<?> ListValues = sqlQuery.queryForList(getChildrenSql,
						new Object[] { numAppInsId, strTplId, strXxxBh });
				for (Object ObjValue : ListValues) {
					Map<String, Object> MapValue = (Map<String, Object>) ObjValue;
					strXxxValue = MapValue.get("TZ_VALUE") == null ? "" : String.valueOf(MapValue.get("TZ_VALUE"));
					if (!"".equals(strXxxValue) && strXxxValue != null) {
						//
						// System.out.println("strXxxValue:"+strXxxValue);
						switch (strXxxGdgsjy) {
						case "email":
							boolean isEmail = this.isValidEmail(strXxxValue);
							if (isEmail == false) {
								returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							}
							break;
						case "telphone":
							boolean isPhone = this.isValidPhone(strXxxValue);
							if (isPhone == false) {
								returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							}
							break;
						case "idcard":
							boolean isIdCard = this.isValidIdcard(strXxxValue);
							if (isIdCard == false) {
								returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							}
							break;
						case "url":
							boolean isUrl = this.isValidUrl(strXxxValue);
							if (isUrl == false) {
								returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							}
							break;
						case "certNo":
							boolean isCcertificateNo = this.isCcertificateNo(strXxxValue);
							if (isCcertificateNo == false) {
								returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							}
							break;
						}
						if (!"".equals(returnMessage) && returnMessage != null) {
							break;
						}
					}
				}
				break;
			case "CertificateNum":
				String sql = "SELECT TZ_D_XXX_BH FROM PS_TZ_TEMP_FIELD_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_NO = ? LIMIT 0,1";
				String strDxxxBh = sqlQuery.queryForObject(sql, new Object[] { strTplId, strXxxBh }, "String");

				if ("".equals(strDxxxBh) || strDxxxBh == null) {
					strDxxxBh = strXxxBh;
				}
				String sqlGetCertificateType = "SELECT TZ_XXX_BH FROM PS_TZ_APP_CC_VW "
						+ "WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_NO = 'com_CerType' AND TZ_APP_S_TEXT = '1'";
				List<?> ListXxxBh = sqlQuery.queryForList(sqlGetCertificateType,
						new Object[] { numAppInsId, strTplId, strDxxxBh });
				for (Object ObjValue : ListXxxBh) {
					Map<String, Object> MapXxxBh = (Map<String, Object>) ObjValue;
					String strCertificateTypeXxxBh = MapXxxBh.get("TZ_XXX_BH") == null ? ""
							: String.valueOf(MapXxxBh.get("TZ_XXX_BH"));
					String strCertificateNoXxxBh = "";

					if (!"".equals(strCertificateTypeXxxBh)) {
						strCertificateNoXxxBh = strCertificateTypeXxxBh.replace("com_CerType", "com_CerNum");
						String sqlGetCertificateNo = "SELECT if(TZ_APP_S_TEXT = ''||TZ_APP_S_TEXT is null,TZ_APP_L_TEXT,TZ_APP_S_TEXT) TZ_VALUE "
								+ "FROM PS_TZ_APP_CC_VW WHERE TZ_APP_INS_ID = ? AND TZ_APP_TPL_ID = ? AND TZ_D_XXX_BH = ? AND TZ_XXX_BH = ? AND TZ_XXX_NO = 'com_CerNum'";
						String strGetCertificateNo = sqlQuery.queryForObject(sqlGetCertificateNo,
								new Object[] { numAppInsId, strTplId, strDxxxBh, strCertificateNoXxxBh }, "String");

						boolean isIdCard = this.isValidIdcard(strGetCertificateNo);
						if (isIdCard == false) {
							returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
							break;
						}
					}
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnMessage;
	}

	// 多行行数校验
	public String dHLineValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc, String strComMc,
			int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax, String strXxxZsxzgs,
			String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz, String strXxxCharBz,
			int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax, String strXxxXsws,
			String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine, String strTjxSub,
			String strJygzTsxx) {
		String returnMessage = "";

		try {
			switch (strComMc) {
			case "EduExperience":
			case "workExperience":
			case "DHContainer":
				// 行数校验
				String sql = "SELECT TZ_XXX_LINE FROM PS_TZ_APP_DHHS_T WHERE TZ_APP_INS_ID = ? AND TZ_XXX_BH = ?";
				int numDhLine = sqlQuery.queryForObject(sql, new Object[] { numAppInsId, strXxxBh }, "Integer");
				if (numDhLine < numXxxMinLine) {
					returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMessage;
	}

	// 推荐信校验
	public String refLetterValidator(Long numAppInsId, String strTplId, String strXxxBh, String strXxxMc,
			String strComMc, int numPageNo, String strXxxRqgs, String strXxxXfmin, String strXxxXfmax,
			String strXxxZsxzgs, String strXxxZdxzgs, String strXxxYxsclx, String strXxxYxscdx, String strXxxBtBz,
			String strXxxCharBz, int numXxxMinlen, long numXxxMaxlen, String strXxxNumBz, int numXxxMin, long numXxxMax,
			String strXxxXsws, String strXxxGdgsjy, String strXxxDrqBz, int numXxxMinLine, int numXxxMaxLine,
			String strTjxSub, String strJygzTsxx) {
		String returnMessage = "";

		// 是否需要校验推荐信
		String strIsCheck = "Y";

		String sql = "SELECT TZ_REF_CHECK,TZ_REF_CHECK_APP FROM PS_TZ_APP_XXXPZ_T WHERE TZ_APP_TPL_ID = ? AND TZ_XXX_BH = ?";
		Map<String, Object> Map = sqlQuery.queryForMap(sql, new Object[] { strTplId, strXxxBh });
		String strIsCheckRef = Map.get("TZ_REF_CHECK") == null ? "" : String.valueOf(Map.get("TZ_REF_CHECK"));
		String strCheckAppClassId = Map.get("TZ_REF_CHECK_APP") == null ? ""
				: String.valueOf(Map.get("TZ_REF_CHECK_APP"));
		if ("Y".equals(strIsCheckRef)) {
			if (!"".equals(strCheckAppClassId)) {
				sql = "SELECT TZ_APPCLS_PATH,TZ_APPCLS_NAME,TZ_APPCLS_METHOD FROM PS_TZ_APPCLS_TBL WHERE TZ_APPCLS_ID = ?";
				Map<String, Object> MapAppClass = sqlQuery.queryForMap(sql, new Object[] { strCheckAppClassId });
				String strAppClassPath = "";
				String strAppClassName = "";
				String strAppClassMethod = "";
				strAppClassPath = MapAppClass.get("TZ_APPCLS_PATH") == null ? ""
						: String.valueOf(MapAppClass.get("TZ_APPCLS_PATH"));
				strAppClassName = MapAppClass.get("TZ_APPCLS_NAME") == null ? ""
						: String.valueOf(MapAppClass.get("TZ_APPCLS_NAME"));
				strAppClassMethod = MapAppClass.get("TZ_APPCLS_METHOD") == null ? ""
						: String.valueOf(MapAppClass.get("TZ_APPCLS_METHOD"));
				try {
					tzOnlineAppEventServiceImpl tzOnlineAppEventServiceImpl = (tzOnlineAppEventServiceImpl) ctx
							.getBean(strAppClassPath + "." + strAppClassName);
					switch (strAppClassMethod) {
					// 根据报名表配置的方法名称去调用不同的方法
					}
				} catch (Exception e) {
					e.printStackTrace();
					strIsCheck = "Y";
				}
			}
		}

		if ("Y".equals(strIsCheck) && "recommendletter".equals(strComMc)) {
			String sqlGetRefLetterCount = "";
			// 查看申请人已经提交的推荐信数量
			int numRefletter = 0;
			// 是否需要推荐人提交了推荐信
			if ("Y".equals(strTjxSub)) {
				sqlGetRefLetterCount = "SELECT COUNT(*) FROM PS_TZ_KS_TJX_TBL A WHERE ((A.ATTACHSYSFILENAME <> ' ' AND A.ATTACHUSERFILE <> ' ') OR EXISTS (SELECT * FROM PS_TZ_APP_INS_T B WHERE A.TZ_TJX_APP_INS_ID = B.TZ_APP_INS_ID AND B.TZ_APP_FORM_STA = 'U')) AND A.TZ_APP_INS_ID = ? AND A.TZ_MBA_TJX_YX = 'Y'";
			} else {
				sqlGetRefLetterCount = "SELECT COUNT('Y') FROM PS_TZ_KS_TJX_TBL WHERE TZ_MBA_TJX_YX = 'Y' AND TZ_APP_INS_ID = ?";
			}
			numRefletter = sqlQuery.queryForObject(sqlGetRefLetterCount, new Object[] { numAppInsId }, "Integer");
			if (numXxxMinLine > 0) {
				if (numRefletter < numXxxMinLine) {
					returnMessage = this.getMsg(strXxxMc, strJygzTsxx);
				}
			}
		}

		return returnMessage;
	}

	private String getMsg(String strXxxMc, String strJygzTsxx) {
		return "“" + strXxxMc + "”" + ":" + strJygzTsxx;

	}

	// 是否是英文字符
	private boolean isEnglishLetter(String strValue) {

		strValue = strValue.replaceAll(" ", "A");

		boolean isMatch = strValue.matches("^[a-zA-Z]*");

		return isMatch;

	}

	// 是否是Email
	private boolean isValidEmail(String strValue) {

		boolean isMatch = strValue.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

		return isMatch;

	}

	// 是否是电话号码
	private boolean isValidPhone(String strValue) {

		boolean isMatch = true;

		int numPhoneLen = strValue.length();

		if (numPhoneLen > 24 || numPhoneLen < 6) {
			isMatch = false;
		}

		return isMatch;

	}

	// 是否是证书编号 证书编号限定只能填写数字、字母和中横线
	private boolean isCcertificateNo(String strValue) {

		boolean isMatch = strValue.matches("^[0-9a-zA-Z\\-]*$");

		return isMatch;

	}

	// 是否是Url
	private boolean isValidUrl(String strValue) {

		String regex = "^((https|http|ftp|rtsp|mms)?://)" + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" // ftp的user@
				+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
				+ "|" // 允许IP和DOMAIN（域名）
				+ "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
				+ "[a-z]{2,6})" // first level domain- .com or .museum
				+ "(:[0-9]{1,4})?" // 端口- :80
				+ "((/?)|" // a slash isn't required if there is no file name
				+ "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";

		boolean isMatch = strValue.matches(regex);

		return isMatch;

	}

	// 是否是IdCard
	private boolean isValidIdcard(String strValue) {

		boolean isMatch = strValue.matches("(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");

		return isMatch;

	}

	// 判断是否整数
	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// 判断是否浮点数
	private boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			if (value.contains(".")) {
				return true;
			} else {
				return false;
			}
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// 判断是否为数字
	private boolean isNumber(String value) {
		return this.isInteger(value) || this.isDouble(value);
	}

	// 获取浮点数
}
