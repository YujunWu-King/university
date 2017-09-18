SELECT 
    TZ_SCORE_CKZL
FROM
    PS_TZ_MODAL_DT_TBL A
WHERE
    TZ_SCORE_ITEM_ID = ?
        AND EXISTS( SELECT 
            'x'
        FROM
            PS_TZ_RS_MODAL_TBL
        WHERE
            TZ_JG_ID = ? 
            	AND TZ_SCORE_MODAL_ID = ?
                AND TZ_JG_ID = A.TZ_JG_ID
                AND TREE_NAME = A.TREE_NAME)