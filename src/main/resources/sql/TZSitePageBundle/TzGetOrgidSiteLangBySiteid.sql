select 
	TZ_JG_ID, 
	TZ_SITE_LANG 
from 
	PS_TZ_SITEI_DEFN_T 
where 
	TZ_SITEI_ID=? 
	and TZ_SITEI_ENABLE='Y'