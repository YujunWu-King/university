select 
	TZ_AREA_TYPE_ID 
from 
	PS_TZ_SITEI_ATYP_T 
where 
	TZ_SITEI_ID=? 
	and TZ_AREA_TYPE=? 
	and TZ_AREA_TYPE_STATE='Y'