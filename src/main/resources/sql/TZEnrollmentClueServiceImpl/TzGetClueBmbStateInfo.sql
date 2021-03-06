SELECT A.TZ_LEAD_ID,
       A.TZ_APP_INS_ID,
       B.TZ_FORM_SP_STA,
       C.TZ_MS_PLAN,
       D.TZ_BS_RESULT,
       E.TZ_LQ_STATE,
       F.TZ_KX_PLAN
FROM PS_TZ_XSXS_BMB_T A 
LEFT JOIN PS_TZ_FORM_WRK_T B ON A.TZ_APP_INS_ID=B.TZ_APP_INS_ID
LEFT JOIN PS_TZ_KSBM_EXT_TBL C ON A.TZ_APP_INS_ID=C.TZ_APP_INS_ID
LEFT JOIN TZ_IMP_BSBM_TBL D ON A.TZ_APP_INS_ID=D.TZ_APP_INS_ID
LEFT JOIN TZ_IMP_LQJD_TBL E ON A.TZ_APP_INS_ID=E.TZ_APP_INS_ID
LEFT JOIN TZ_IMP_KXAP_TBL F ON A.TZ_APP_INS_ID=F.TZ_APP_INS_ID
WHERE A.TZ_LEAD_ID=?