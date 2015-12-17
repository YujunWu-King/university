select 
	TZ_MENU_ID 
from 
	PS_TZ_SITEI_MENU_T 
where 
	TZ_SITEI_ID=? 
	and TZ_MENU_STATE='Y' 
order by TZ_MENU_XH
limit 0,1