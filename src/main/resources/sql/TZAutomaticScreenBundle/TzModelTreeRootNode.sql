SELECT 
    a.TREE_NAME, TREE_NODE_NUM, TREE_NODE
FROM
    PSTREENODE a,
    PS_TZ_RS_MODAL_TBL b
WHERE
    a.TREE_NAME = b.TREE_NAME
        AND PARENT_NODE_NUM = 0
        AND TZ_SCORE_MODAL_ID = ?
        AND TZ_JG_ID = ?