select
	E.TZ_MENU_NUM,
	E.TZ_MENU_MC_LANG,
	E.TZ_YXX,
	ifnull(E.TZ_COM_ID,"") TZ_COM_ID,
	ifnull(E.TZ_MENU_LIMG,"") TZ_MENU_LIMG,
	ifnull(E.TZ_MENU_SIMG,"") TZ_MENU_SIMG,
	ifnull(E.TZ_MENU_NRID,"") TZ_MENU_NRID,
	ifnull(E.TZ_COM_MC,"") TZ_COM_MC
from 
	PSTREENODE F, 
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
		where 
			exists( 
		 		select 
		 			'X' 
		  		from 
		  			PSTREENODE A 
		 		where 
		 			A.TREE_NAME=? 
		   			and A.PARENT_NODE_NAME=? 
		   			and C.TZ_MENU_NUM=A.TREE_NODE 
		   	) 
	) E
where
	E.TREE_NAME=F.TREE_NAME 
   	and E.TZ_MENU_NUM=F.TREE_NODE 
   	and E.TREE_NAME =? 
order by 
	F.TREE_NODE_NUM asc