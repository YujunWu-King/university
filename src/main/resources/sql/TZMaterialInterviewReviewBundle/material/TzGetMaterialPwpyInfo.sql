SELECT 
	A.TZ_PWEI_OPRID,
	B.TZ_DLZH_ID,
	C.TZ_PYKS_XX,
	A.TZ_SCORE_INS_ID,
	A.TZ_KSH_PSPM
FROM 
	PS_TZ_CLPS_PW_TBL C,
	PS_TZ_AQ_YHXX_TBL B,
	PS_TZ_CP_PW_KS_TBL A
WHERE 
	C.TZ_CLASS_ID=A.TZ_CLASS_ID 
	AND C.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID 
	AND C.TZ_PWEI_OPRID=A.TZ_PWEI_OPRID
	AND A.TZ_PWEI_OPRID=B.OPRID
	AND A.TZ_CLASS_ID=? 
	AND A.TZ_APPLY_PC_ID=? 
	AND A.TZ_APP_INS_ID=?
