insert into PS_TZ_AQ_CDJD_LNG 
	select 
		TREE_NAME, 
 		? as TZ_MENU_NUM, 
 		TZ_LANGUAGE_ID, 
 		TZ_MENU_MC 
	from 
		PS_TZ_AQ_CDJD_LNG 
 	where 
 		TREE_NAME=? 
   		and TZ_MENU_NUM=? 
   		and TZ_MENU_NUM<>?