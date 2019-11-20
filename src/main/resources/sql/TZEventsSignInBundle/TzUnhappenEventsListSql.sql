SELECT 
    TZ_ART_ID,
    TZ_NACT_NAME,
    TZ_START_DT,
    date_format(TZ_START_DT,'%m月') TZ_MONTH,
   	date_format(TZ_START_DT,'%d') TZ_DAY,
   	 (SELECT 
            COUNT(1)
        FROM
            PS_TZ_NAUDLIST_T
        WHERE
            TZ_ART_ID = A.TZ_ART_ID
                AND TZ_NREG_STAT = '1') TZ_ALL_COUNT,
    (SELECT 
            COUNT(1)
        FROM
            PS_TZ_NAUDLIST_T
        WHERE
            TZ_ART_ID = A.TZ_ART_ID
                AND TZ_NREG_STAT = '1'
                AND TZ_BMCY_ZT IN ('A' , 'E')) TZ_YQD_COUNT,
    (SELECT 
            COUNT(1)
        FROM
            PS_TZ_NAUDLIST_T
        WHERE
            TZ_ART_ID = A.TZ_ART_ID
                AND TZ_NREG_STAT = '1'
                AND (TZ_BMCY_ZT NOT IN ('A' , 'E')
                OR TZ_BMCY_ZT IS NULL)) TZ_WQD_COUNT
FROM
    PS_TZ_ART_HD_TBL A
WHERE
    TZ_START_DT > ? AND TZ_START_DT <= ?
        AND EXISTS( SELECT 
            'Y'
        FROM
            PS_TZ_LM_NR_GL_T
        WHERE
            TZ_ART_ID = A.TZ_ART_ID
                AND TZ_ART_PUB_STATE = 'Y')
        AND EXISTS( SELECT 
            'Y'
        FROM
            PS_TZ_LM_NR_GL_T A1,
            PS_TZ_SITEI_DEFN_T A2
        WHERE
            A1.TZ_SITE_ID = A2.TZ_SITEI_ID
                AND A1.TZ_ART_ID = A.TZ_ART_ID
                AND A2.TZ_JG_ID = ?)
ORDER BY TZ_START_DT DESC