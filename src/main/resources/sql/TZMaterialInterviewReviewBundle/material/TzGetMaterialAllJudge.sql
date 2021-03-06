SELECT 
	A.OPRID,
	B.TZ_DLZH_ID,
	B.TZ_REALNAME,
	C.TZ_ZY_SJ TZ_MOBILE,
	C.TZ_ZY_EMAIL TZ_EMAIL
FROM PS_TZ_JUSR_REL_TBL A
	LEFT JOIN PS_TZ_AQ_YHXX_TBL B
	ON A.OPRID=B.OPRID
	LEFT JOIN PS_TZ_LXFSINFO_TBL C
    ON B.OPRID=C.TZ_LYDX_ID
    AND B.TZ_RYLX=C.TZ_LXFS_LY
WHERE 
	A.TZ_JG_ID=? AND A.TZ_JUGTYP_ID=?