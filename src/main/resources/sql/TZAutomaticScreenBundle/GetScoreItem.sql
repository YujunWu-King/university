SELECT
	A.TZ_SCORE_ITEM_ID
FROM
	PS_TZ_MODAL_DT_TBL A,
	PS_TZ_RS_MODAL_TBL B
WHERE
	B.TZ_JG_ID = ?
AND B.TZ_SCORE_MODAL_ID = ?
AND A.TREE_NAME = B.TREE_NAME