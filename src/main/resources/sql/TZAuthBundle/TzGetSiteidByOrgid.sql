select 
	TZ_SITEI_ID 
from 
	PS_TZ_SITEI_DEFN_T 
where 
	TZ_JG_ID=? 
	and TZ_SITEI_ENABLE='Y' 
order by TZ_LASTMANT_DTTM desc
limit 0,1