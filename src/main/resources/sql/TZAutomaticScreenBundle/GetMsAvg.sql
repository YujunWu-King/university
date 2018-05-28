SELECT
	TZ_SCORE_ITEM_ID,
	avg(TZ_SCORE_NUM) AS TZ_SCORE_NUM
FROM
	PS_TZ_CJX_TBL
WHERE
	TZ_SCORE_INS_ID IN (
		SELECT
			TZ_SCORE_INS_ID
		FROM
			PS_TZ_MP_PW_KS_TBL 
		WHERE
			TZ_CLASS_ID = ?
		AND TZ_APPLY_PC_ID = ?
		AND TZ_APP_INS_ID = ?
	) AND TZ_SCORE_NUM >0
GROUP BY
	TZ_SCORE_ITEM_ID