SELECT TMP.TZ_APP_INS_ID 
FROM (
	SELECT A.TZ_APP_INS_ID
	FROM PS_TZ_MP_PW_KS_TBL A,
	 	PS_TZ_MSPS_KSH_TBL E
	WHERE A.TZ_CLASS_ID=E.TZ_CLASS_ID 
	AND A.TZ_APPLY_PC_ID=E.TZ_APPLY_PC_ID 
	AND A.TZ_APP_INS_ID=E.TZ_APP_INS_ID
	AND E.TZ_GROUP_ID=?
	AND A.TZ_CLASS_ID=?
	AND A.TZ_APPLY_PC_ID=?
	AND A.TZ_PWEI_OPRID=?
	ORDER BY E.TZ_ORDER 
) TMP
WHERE TMP.TZ_APP_INS_ID>?
LIMIT 0,1