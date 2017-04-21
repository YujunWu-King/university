SELECT 
    (COUNT(1)+1) as WAIT_NUM
FROM
    PS_TZ_NAUDLIST_T A
WHERE
    TZ_ART_ID = ? AND TZ_NREG_STAT = '4'
        AND EXISTS( SELECT 
            'x'
        FROM
            PS_TZ_NAUDLIST_T
        WHERE
            TZ_ART_ID = A.TZ_ART_ID
                AND TZ_REG_TIME > A.TZ_REG_TIME
                AND TZ_HD_BMR_ID = ?)