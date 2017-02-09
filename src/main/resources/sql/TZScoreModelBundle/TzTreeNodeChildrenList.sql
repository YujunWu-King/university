SELECT 
    TZ_SCORE_ITEM_ID, DESCR
FROM
    PSTREENODE F,
    (SELECT 
        TREE_NAME, TZ_SCORE_ITEM_ID, DESCR
    FROM
        PS_TZ_MODAL_DT_TBL A
    WHERE
        A.TZ_JG_ID = ?
            AND A.TREE_NAME = ?
            AND EXISTS( SELECT 
                'Y'
            FROM
                PSTREENODE B
            WHERE
                B.TREE_NAME = A.TREE_NAME
                    AND B.PARENT_NODE_NAME = ?
                    AND B.TREE_NODE = A.TZ_SCORE_ITEM_ID)) E
WHERE
    E.TREE_NAME = F.TREE_NAME
        AND E.TZ_SCORE_ITEM_ID = F.TREE_NODE
        AND E.TREE_NAME = ?
ORDER BY F.TREE_NODE_NUM ASC