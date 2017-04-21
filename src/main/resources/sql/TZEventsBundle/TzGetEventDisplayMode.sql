SELECT 
    CASE
        WHEN
            ? BETWEEN STR_TO_DATE(CONCAT(TZ_APPF_DT, ' ', TZ_APPF_TM),
                    '%Y-%m-%d %H:%i') AND STR_TO_DATE(CONCAT(TZ_APPE_DT, ' ', TZ_APPE_TM),
                    '%Y-%m-%d %H:%i')
        THEN
            'Y'
        WHEN
            ? < STR_TO_DATE(CONCAT(TZ_APPF_DT, ' ', TZ_APPF_TM),
                    '%Y-%m-%d %H:%i')
        THEN
            'B'
        WHEN
            ? > STR_TO_DATE(CONCAT(TZ_APPE_DT, ' ', TZ_APPE_TM),
                    '%Y-%m-%d %H:%i')
        THEN
            'E'
        ELSE 'N'
    END AS VALID_TD,
    TZ_QY_ZXBM,
    TZ_XWS,
    TZ_XSMS
FROM
    PS_TZ_ART_HD_TBL
WHERE
    TZ_ART_ID = ?