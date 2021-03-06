select
	ifnull(B.TZ_TEMP_NAME,"") TZ_TEMP_NAME,
	A.TZ_MENU_ID,
	A.TZ_MENU_NAME,
	A.TZ_MENU_STATE,
	ifnull(A.TZ_MENU_PATH,"") TZ_MENU_PATH,
	ifnull(A.TZ_MENU_TYPE,"") TZ_MENU_TYPE,
	ifnull(A.TZ_TEMP_ID,"") TZ_TEMP_ID,
	ifnull(A.TZ_PAGE_NAME,"") TZ_PAGE_NAME,
	ifnull(A.TZ_D_MENU_ID,"") TZ_D_MENU_ID,
	A.TZ_MENU_LEVEL,
	A.TZ_MENU_XH,
	ifnull(A.TZ_F_MENU_ID,"") TZ_F_MENU_ID,
	ifnull(C.TZ_MENU_NAME,"") TZ_DEFAULT_PAGE,
	ifnull(A.TZ_MENU_STYLE,"") TZ_MENU_STYLE,
	ifnull(A.TZ_ATTACHSYSFILENA,"") TZ_ATTACHSYSFILENA,
	ifnull(A.TZ_IMAGE_TITLE,"") TZ_IMAGE_TITLE,
	ifnull(A.TZ_IMAGE_DESC,"") TZ_IMAGE_DESC,
	
	ifnull(A.TZ_MENU_SHOW,"") TZ_MENU_SHOW
from 
	PS_TZ_SITEI_MENU_T A
	left join
		PS_TZ_SITEI_TEMP_T B 
	on  (
		A.TZ_SITEI_ID=B.TZ_SITEI_ID
		and A.TZ_TEMP_ID=B.TZ_TEMP_ID
	)
	left join
		PS_TZ_SITEI_MENU_T C 
	on  (
		A.TZ_SITEI_ID=C.TZ_SITEI_ID
		and A.TZ_D_MENU_ID=C.TZ_MENU_ID
	)
where ?=A.TZ_SITEI_ID;