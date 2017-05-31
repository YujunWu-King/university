SELECT
	C.TZ_SCORE_ITEM_ID,
	C.DESCR,
	C.TZ_SCORE_ITEM_TYPE
FROM
	PSTREENODE D,
	PS_TZ_CLASS_INF_T A,
	PS_TZ_MODAL_DT_TBL C,
	PS_TZ_RS_MODAL_TBL B
WHERE
	D.TREE_NAME = B.TREE_NAME
AND D.TREE_NODE = C.TZ_SCORE_ITEM_ID
AND B.TZ_JG_ID = C.TZ_JG_ID
AND B.TREE_NAME = C.TREE_NAME
AND A.TZ_MSCJ_SCOR_MD_ID = B.TZ_SCORE_MODAL_ID
AND B.TZ_JG_ID = A.TZ_JG_ID
AND A.TZ_CLASS_ID =?
ORDER BY
	D.TREE_NODE_NUM  