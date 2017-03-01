SELECT 
    TZ_SCORE_ITEM_ID, a.TZ_ZDCSGZ_ID, TZ_ZDCSGZ
FROM
    PS_TZ_MODAL_DT_TBL a
        LEFT JOIN
    PS_TZ_ZDCS_DFGZ_T c ON (a.TZ_ZDCSGZ_ID = c.TZ_ZDCSGZ_ID)
WHERE
    TZ_SCORE_ITEM_TYPE = 'B'
        AND TZ_IF_ZDCS = 'Y'
        AND EXISTS( SELECT 
            'X'
        FROM
            PS_TZ_RS_MODAL_TBL b
        WHERE
            TZ_JG_ID = a.TZ_JG_ID
                AND TREE_NAME = a.TREE_NAME
                AND TZ_SCORE_MODAL_ID = ?
                AND TZ_JG_ID = ?)
        AND EXISTS( SELECT 
            'Y'
        FROM
            PSTREENODE
        WHERE
            TREE_NAME = a.TREE_NAME
                AND TREE_NODE = a.TZ_SCORE_ITEM_ID)