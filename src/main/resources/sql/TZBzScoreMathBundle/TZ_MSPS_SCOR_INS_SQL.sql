SELECT
	A.TZ_SCORE_INS_ID
FROM
	PS_TZ_MP_PW_KS_TBL A,
	PS_TZ_AQ_YHXX_TBL D
WHERE
	D.OPRID = A.TZ_PWEI_OPRID
AND A.TZ_DELETE_ZT <> 'Y'
AND A.TZ_PSHEN_ZT <> 'C'
AND A.TZ_APPLY_PC_ID =?
AND A.TZ_APP_INS_ID =?
AND A.TZ_CLASS_ID =?
AND D.TZ_DLZH_ID =?