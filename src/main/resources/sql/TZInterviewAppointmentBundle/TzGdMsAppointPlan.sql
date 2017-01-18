SELECT 
    A.TZ_CLASS_ID,
    C.TZ_BATCH_ID,
    C.TZ_MS_PLAN_SEQ,
    C.TZ_MSYY_COUNT,
    A.OPRID,
    C.TZ_MS_DATE,
    C.TZ_START_TM,
    C.TZ_END_TM,
    C.TZ_MS_LOCATION,
    C.TZ_MS_ARR_DEMO
FROM
    PS_TZ_FORM_WRK_T A,
    PS_TZ_MSPS_KSH_TBL B,
    PS_TZ_MSSJ_ARR_TBL C
WHERE
    A.TZ_CLASS_ID = B.TZ_CLASS_ID
        AND A.TZ_APP_INS_ID = B.TZ_APP_INS_ID
        AND A.OPRID = ?
        AND A.TZ_CLASS_ID = C.TZ_CLASS_ID
        AND B.TZ_APPLY_PC_ID = C.TZ_BATCH_ID 
        AND EXISTS( SELECT 
            'Y'
        FROM
            PS_TZ_CLASS_INF_T
        WHERE
            TZ_CLASS_ID = A.TZ_CLASS_ID
                AND TZ_IS_APP_OPEN = 'Y')
        AND EXISTS( SELECT 
            'Y'
        FROM
            PS_TZ_MSYY_SET_TBL
        WHERE
            TZ_CLASS_ID = C.TZ_CLASS_ID
                AND TZ_BATCH_ID = C.TZ_BATCH_ID
                AND TZ_OPEN_STA = 'Y')
ORDER BY TZ_CLASS_ID,TZ_MS_DATE,TZ_START_TM