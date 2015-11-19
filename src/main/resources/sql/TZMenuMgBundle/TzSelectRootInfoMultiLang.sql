select
	D.TZ_MENU_NUM,
	ifnull(D.TZ_MENU_MC_LANG,"") TZ_MENU_MC_LANG,
	ifnull(D.TZ_YXX,"") TZ_YXX,
	ifnull(D.TZ_COM_ID,"") TZ_COM_ID,
	ifnull(D.TZ_MENU_LIMG,"") TZ_MENU_LIMG,
	ifnull(D.TZ_MENU_SIMG,"") TZ_MENU_SIMG,
	ifnull(D.TZ_MENU_NRID,"") TZ_MENU_NRID,
	ifnull(D.TZ_COM_MC,"") TZ_COM_MC
from 
	PSTREENODE A, 
	PS_TZ_JG_BASE_T B, 
	(
		select 
			C.*,
		    if(
		    	H.TZ_MENU_MC is NULL,
		    	C.TZ_MENU_MC,
		    	if(
		    		ltrim(rtrim(H.TZ_MENU_MC))='',
		    		C.TZ_MENU_MC,
		    		H.TZ_MENU_MC
		    	)
		    ) TZ_MENU_MC_LANG,
		    G.TZ_COM_MC
		from
			PS_TZ_AQ_CDJD_TBL C
		left join
			PS_TZ_AQ_CDJD_LNG H 
		on  (
		   C.TREE_NAME=H.TREE_NAME
		   and C.TZ_MENU_NUM=H.TZ_MENU_NUM
		   and ?=H.TZ_LANGUAGE_ID
		   and ?<>H.TZ_LANGUAGE_ID
		)
		left join
			PS_TZ_AQ_COMZC_TBL G
		on (
			C.TZ_COM_ID = G.TZ_COM_ID
		)
	) D
where
	A.TREE_NAME=? 
   	and A.TREE_LEVEL_NUM=2 
   	and A.TREE_NODE = D.TZ_MENU_NUM 
   	and A.TREE_NODE= B.TZ_JG_ID 
   	and D.TZ_MENU_NUM=?