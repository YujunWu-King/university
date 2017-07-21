SELECT 
    A.TZ_ZNX_MSGID
FROM
    PS_TZ_ZNX_MSG_T A
        JOIN
    PS_TZ_ZNX_REC_T B ON (A.TZ_ZNX_MSGID = B.TZ_ZNX_MSGID
        AND B.TZ_REC_DELSTATUS <> 'Y')
WHERE
    TZ_ZNX_RECID = ?
        AND CONVERT( A.TZ_ZNX_MSGID, SIGNED) > CONVERT(?, SIGNED)
        AND TZ_MSG_SUBJECT LIKE ?
ORDER BY A.ROW_ADDED_DTTM ASC , CONVERT( A.TZ_ZNX_MSGID , SIGNED) ASC
LIMIT 1