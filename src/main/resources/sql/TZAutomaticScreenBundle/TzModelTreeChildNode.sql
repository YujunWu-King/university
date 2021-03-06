SELECT 
    TREE_NODE_NUM,
    TREE_NODE,
    (SELECT 
            'Y'
        FROM
            PSTREENODE
        WHERE
            TREE_NAME = A.TREE_NAME
                AND PARENT_NODE_NUM = A.TREE_NODE_NUM
        LIMIT 1) AS TZ_HAS_CHILD,
    TZ_SCORE_ITEM_TYPE,
    TZ_SCORE_HZ,
    TZ_SCORE_QZ
FROM
    PSTREENODE A,
    PS_TZ_MODAL_DT_TBL B
WHERE
    A.PARENT_NODE_NUM = ?
        AND TZ_SCORE_ITEM_TYPE IN ('A' , 'B')
        AND A.TREE_NAME = ?
        AND A.TREE_NAME = B.TREE_NAME
        AND B.TZ_JG_ID = ?
        AND A.TREE_NODE = B.TZ_SCORE_ITEM_ID