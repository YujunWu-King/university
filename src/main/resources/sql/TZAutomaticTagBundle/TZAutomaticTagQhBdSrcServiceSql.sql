/**
 * 自动标签
 * 标签清华（北大）生源：本科教育经历是清华或北大，且有学士学位
 * TZ_APP_FORM_STA：S 保存；  U 提交；  P 预提交
 * TZ_FORM_SP_STA：A 审批通过； B 拒绝； N 待审核
 */

SELECT X.TZ_APP_INS_ID
FROM PS_TZ_FORM_WRK_T Y,
     PS_TZ_APP_INS_T X
WHERE Y.TZ_APP_INS_ID=X.TZ_APP_INS_ID
  AND X.TZ_APP_FORM_STA='U'
  AND Y.TZ_FORM_SP_STA<>'B'
  AND Y.TZ_BATCH_ID=?
  AND Y.TZ_CLASS_ID=?
  AND EXISTS (
  	SELECT 'Y' FROM dual
	WHERE ((SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_11luniversitysch' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID) IN ('清华大学','北京大学')
		AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_11xl' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID)='3' 
		AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_11TZ_TZ_11_4' and TZ_APP_INS_ID=X.TZ_APP_INS_ID)='1') or
	((SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_12ouniversitysch' and TZ_APP_INS_ID=X.TZ_APP_INS_ID) IN ('清华大学','北京大学')
		AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_12xl2' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID)='1' 
		AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_12xw2' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID)='1') or
	((SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_13ouniver3sch' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID) IN ('清华大学','北京大学')
		AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_13xueli3' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID)='1' 
		AND (SELECT TZ_APP_S_TEXT FROM PS_TZ_APP_CC_T
		WHERE TZ_XXX_BH='TZ_13xuewei3' AND TZ_APP_INS_ID=X.TZ_APP_INS_ID)='1')
  )