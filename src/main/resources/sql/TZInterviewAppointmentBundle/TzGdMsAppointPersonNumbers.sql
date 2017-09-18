SELECT 
    TZ_MSYY_COUNT,
    (SELECT 
            COUNT(1)
        FROM
            PS_TZ_MSYY_KS_TBL
        WHERE
            TZ_CLASS_ID = A.TZ_CLASS_ID
                AND TZ_BATCH_ID = A.TZ_BATCH_ID
                AND TZ_MS_PLAN_SEQ = A.TZ_MS_PLAN_SEQ) AS TZ_YY_COUNT
FROM
    PS_TZ_MSSJ_ARR_TBL A
WHERE
    TZ_CLASS_ID = ?
        AND TZ_BATCH_ID = ? and TZ_MS_PLAN_SEQ = ?