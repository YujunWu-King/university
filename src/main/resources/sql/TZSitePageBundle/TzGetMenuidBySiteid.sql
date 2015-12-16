select 
	TZ_MENU_ID 
from 
	PS_TZ_SITEI_MENU_T PT 
where 
	PT.TZ_SITEI_ID=? 
	and TZ_MENU_XH=(
		select 
			min(TZ_MENU_XH) 
		from 
			PS_TZ_SITEI_MENU_T 
		where 
			TZ_SITEI_ID=PT.TZ_SITEI_ID
	)