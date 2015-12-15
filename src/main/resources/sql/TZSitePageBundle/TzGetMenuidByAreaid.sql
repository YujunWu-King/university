select 
	TZ_MENU_ID 
from 
	PS_TZ_SITEI_MENU_T 
where 
	TZ_SITEI_ID=? 
	and TZ_MENU_STATE='Y' 
	and TZ_MENU_COLUMN=(
		select 
			TZ_COLU_ID 
		from 
			PS_TZ_SITEI_AREA_T 
		where 
			TZ_SITEI_ID=? 
			and TZ_AREA_ID=? 
			and TZ_AREA_STATE='Y' 
	)