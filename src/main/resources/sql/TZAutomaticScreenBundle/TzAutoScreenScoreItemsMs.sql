SELECT 
    TZ_SCORE_ITEM_ID, DESCR
FROM
    PS_TZ_MODAL_DT_TBL A
WHERE
    TZ_JG_ID = ?
        AND TREE_NAME = ?
        AND TZ_SCORE_ITEM_ID<>'Total'
        AND TZ_SCORE_ITEM_TYPE = 'A'
        AND EXISTS( SELECT 
            'Y'
        FROM
            PSTREENODE
        WHERE
            TREE_NAME = A.TREE_NAME
                AND TREE_NODE = A.TZ_SCORE_ITEM_ID)
