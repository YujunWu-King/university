SELECT
	A.TZ_GENDER,
	A.TZ_REALNAME,
	A.NATIONAL_ID,
	A.TZ_SCH_CNAME,
	A.BIRTHDATE,
	A.TZ_HIGHEST_EDU,
	A.TZ_COMPANY_NAME,
	A.TZ_ZHIWU,
	A.TZ_DEPTMENT
FROM 
	PS_TZ_REG_USER_T A,
  PS_TZ_FORM_WRK_T B
WHERE A.OPRID=B.OPRID
AND B.TZ_APP_INS_ID=?