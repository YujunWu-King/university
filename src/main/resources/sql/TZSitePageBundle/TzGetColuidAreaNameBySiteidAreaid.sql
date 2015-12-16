select 
	TZ_COLU_ID,
	TZ_AREA_NAME 
from 
	PS_TZ_SITEI_AREA_T PT1 
where 
	TZ_SITEI_ID=? 
	and TZ_AREA_ID=? 
	and TZ_AREA_STATE='Y'