SELECT 
	B.TZ_CLASS_NAME,
    C.TZ_BATCH_NAME,
	A.TZ_PYKS_RQ,
	A.TZ_PYKS_SJ,
	A.TZ_PYJS_RQ,
	A.TZ_PYJS_SJ,
	A.TZ_MSPS_SM,
	A.TZ_DQPY_ZT,
	(SELECT 
		D.TZ_ZHZ_DMS 
	 FROM 
	 	PS_TZ_PT_ZHZXX_TBL D 
	 WHERE 
	 	D.TZ_ZHZ_ID=A.TZ_DQPY_ZT 
	 AND D.TZ_ZHZJH_ID='TZ_DQPY_ZT'
	 ) TZ_DQPY_ZT_DESC,
	A.TZ_MSPY_NUM,
	(SELECT 
		COUNT(1) 
     FROM 
     	PS_TZ_FORM_WRK_T C,PS_TZ_APP_INS_T D 
     WHERE 
     	D.TZ_APP_INS_ID=C.TZ_APP_INS_ID
	 AND D.TZ_APP_FORM_STA='U' 
	 AND C.TZ_CLASS_ID=A.TZ_CLASS_ID 
	 AND C.TZ_BATCH_ID=A.TZ_APPLY_PC_ID
	 ) TZ_BKKS_NUM,
	 (SELECT 
	 	 COUNT(1) 
	  FROM 
	  	 PS_TZ_MSPS_KSH_TBL E 
	  WHERE 
	  	 E.TZ_CLASS_ID=A.TZ_CLASS_ID 
	  AND E.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID
	  ) TZ_MSPS_KS_NUM,
	  (SELECT 
	 	 COUNT(1) 
	  FROM 
	  	 PS_TZ_CLPS_KSH_TBL F
	  WHERE 
	  	 F.TZ_CLASS_ID=A.TZ_CLASS_ID 
	  AND F.TZ_APPLY_PC_ID=A.TZ_APPLY_PC_ID
	  ) TZ_CLPS_KS_NUM
FROM 
	PS_TZ_MSPS_GZ_TBL A
    LEFT JOIN PS_TZ_CLASS_INF_T B
    ON A.TZ_CLASS_ID=B.TZ_CLASS_ID
    LEFT JOIN PS_TZ_CLS_BATCH_T C
    ON A.TZ_CLASS_ID=C.TZ_CLASS_ID
    AND A.TZ_APPLY_PC_ID=C.TZ_BATCH_ID
WHERE 
	A.TZ_CLASS_ID=?
AND A.TZ_APPLY_PC_ID=?