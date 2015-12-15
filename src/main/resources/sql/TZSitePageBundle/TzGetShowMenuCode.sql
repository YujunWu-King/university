select 
	TZ_SHOW_MENU_CODE 
from 
	PS_TZ_SITEI_MTYP_T 
where 
	TZ_SITEI_ID =? 
	and TZ_MENU_TYPE_ID = (
		select 
			TZ_MENU_TYPE_ID 
		from 
			PS_TZ_SITEI_MENU_T 
		where 
			TZ_SITEI_ID=? 
			and TZ_MENU_ID=? 
			and TZ_MENU_STATE='Y'
	) 
	and TZ_TYPE_STATE='Y'