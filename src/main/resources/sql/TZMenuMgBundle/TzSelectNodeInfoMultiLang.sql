select
	A.TZ_MENU_NUM,
    if(
    	B.TZ_MENU_MC is NULL,
    	A.TZ_MENU_MC,
    	if(
    		ltrim(rtrim(B.TZ_MENU_MC))='',
    		A.TZ_MENU_MC,
    		B.TZ_MENU_MC
    	)
    ) TZ_MENU_MC,
    A.TZ_YXX,
    A.TZ_COM_ID,
    A.TZ_MENU_LIMG,
    A.TZ_MENU_SIMG,
    A.TZ_MENU_NRID 
from 
	PS_TZ_AQ_CDJD_TBL A
left join 
    PS_TZ_AQ_CDJD_LNG B 
on (
	A.TREE_NAME=B.TREE_NAME
    and A.TZ_MENU_NUM=B.TZ_MENU_NUM
    and ?=B.TZ_LANGUAGE_ID
    and ?<>B.TZ_LANGUAGE_ID
)
where 
	A.TREE_NAME=? 
	AND A.TZ_MENU_NUM=?