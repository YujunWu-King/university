SELECT 
    CASE
        WHEN
            ? BETWEEN STR_TO_DATE(CONCAT(TZ_OPEN_DT, ' ', TZ_OPEN_TM),
                    '%Y-%m-%d %H:%i') AND STR_TO_DATE(CONCAT(TZ_CLOSE_DT, ' ', TZ_CLOSE_TM),
                    '%Y-%m-%d %H:%i')
        THEN
            'Y'
        WHEN
            ? < STR_TO_DATE(CONCAT(TZ_OPEN_DT, ' ', TZ_OPEN_TM),
                    '%Y-%m-%d %H:%i')
        THEN
            'B'
        WHEN
            ? > STR_TO_DATE(CONCAT(TZ_CLOSE_DT, ' ', TZ_CLOSE_TM),
                    '%Y-%m-%d %H:%i')
        THEN
            'A'
        ELSE 'N'
    END TZ_TIME_VALID,
    TZ_SHOW_FRONT,
    TZ_DESCR
FROM
    PS_TZ_MSYY_SET_TBL
WHERE
    TZ_CLASS_ID = ?
        AND TZ_BATCH_ID = ?