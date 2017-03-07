SELECT 
    SCH.TZ_SCHLR_NAME,
    CONCAT(WJ.TZ_DC_WJ_KSRQ, ' ', WJ.TZ_DC_WJ_KSSJ) AS TZ_DC_WJ_KRQ,
    CONCAT(WJ.TZ_DC_WJ_JSRQ, ' ', WJ.TZ_DC_WJ_JSSJ) AS TZ_DC_WJ_JRQ,
    WJ.TZ_DC_WJ_URL
FROM
    PS_TZ_SCHLR_TBL SCH,
    PS_TZ_DC_WJ_DY_T WJ,
    (SELECT DISTINCT
        SAU.TZ_DC_WJ_ID
    FROM
        PS_TZ_AUD_LIST_T AUD
    LEFT JOIN PS_TZ_SURVEY_AUD_T SAU ON AUD.TZ_AUD_ID = SAU.TZ_AUD_ID
    WHERE
        AUD.OPRID = ?) AUDWJ
WHERE
    WJ.TZ_DC_WJ_ID = AUDWJ.TZ_DC_WJ_ID
        AND SCH.TZ_DC_WJ_ID = WJ.TZ_DC_WJ_ID
        AND SCH.TZ_JG_ID = ?
        AND SCH.TZ_STATE = 'Y'
        AND WJ.TZ_DC_WJ_ZT = '1'
        AND STR_TO_DATE(CONCAT(WJ.TZ_DC_WJ_KSRQ, ' ', WJ.TZ_DC_WJ_KSSJ),
            '%Y-%m-%d %H:%i:%s') <= DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')
        AND STR_TO_DATE(CONCAT(WJ.TZ_DC_WJ_JSRQ, ' ', WJ.TZ_DC_WJ_JSSJ),
            '%Y-%m-%d %H:%i:%s') >= DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s')
        AND SCH.TZ_SCHLR_ID NOT IN (SELECT 
            TZ_SCHLR_ID
        FROM
            PS_TZ_SCHLR_TBL SCH,
            PS_TZ_DC_WJ_DY_T WJ,
            PS_TZ_DC_INS_T INS
        WHERE
            WJ.TZ_DC_WJ_ID = SCH.TZ_DC_WJ_ID
                AND SCH.TZ_DC_WJ_ID = INS.TZ_DC_WJ_ID
                AND INS.ROW_ADDED_OPRID = ?)
                ORDER BY TZ_DC_WJ_KSRQ ASC