SELECT
	A.TZ_CLASS_NAME,
	A.TZ_APP_MODAL_ID,
	C.NATIONAL_ID,
	D.TZ_MSH_ID,
	C.TZ_REALNAME,
	DATE_FORMAT(C.BIRTHDATE ,'%Y-%m-%d') BIRTHDATE,
    (year(now())-year(C.BIRTHDATE)) AGE  FROM
PS_TZ_AQ_YHXX_TBL D,
PS_TZ_REG_USER_T C,
PS_TZ_FORM_WRK_T B,
PS_TZ_CLASS_INF_T A 
WHERE B.OPRID=D.OPRID
 AND B.OPRID=C.OPRID 
AND D.TZ_JG_ID=A.TZ_JG_ID
AND A.TZ_CLASS_ID=? AND B.TZ_APP_INS_ID=?