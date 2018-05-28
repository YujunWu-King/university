SELECT A.TZ_LEAD_ID,
       A.TZ_WT_TYPE,
       A.TZ_LEAD_DESCR,
       A.TZ_ORDER_NUM,
	   B.TZ_REALNAME,
	   B.TZ_MOBILE,
	   B.TZ_COMP_CNAME,
	   B.TZ_RSFCREATE_WAY,
       (SELECT X.TZ_ZHZ_DMS FROM PS_TZ_PT_ZHZXX_TBL X 
        WHERE X.TZ_ZHZJH_ID='TZ_RSFCREATE_WAY' AND X.TZ_ZHZ_ID=B.TZ_RSFCREATE_WAY AND X.TZ_EFF_STATUS='A') TZ_RSFCREATE_WAY_DESC,
       B.TZ_XSQU_ID,
       (SELECT C.TZ_LABEL_DESC FROM PS_TZ_XSXS_DQBQ_T C WHERE C.TZ_LABEL_NAME=B.TZ_XSQU_ID) TZ_LOCAL_NAME,
       B.ROW_ADDED_DTTM,
       B.TZ_ZR_OPRID,
       (SELECT D.TZ_REALNAME FROM PS_TZ_AQ_YHXX_TBL D WHERE D.OPRID=B.TZ_ZR_OPRID) TZ_ZRR_NAME
FROM PS_TZ_WTXS_TMP_T A,PS_TZ_XSXS_INFO_T B
WHERE A.TZ_LEAD_ID=B.TZ_LEAD_ID
      AND A.TZ_JG_ID=? AND A.OPRID=?
ORDER BY A.TZ_WT_TYPE,A.TZ_ORDER_NUM