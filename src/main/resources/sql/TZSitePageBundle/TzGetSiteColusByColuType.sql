select 
	distinct(TZ_COLU_ID) TZ_COLU_ID,
	TZ_COLU_NAME 
from 
	PS_TZ_SITEI_COLU_T 
where  
	TZ_SITEI_ID=? 
	and TZ_CONT_TYPE=?