select 
	TZ_SITEI_ID 
from 
	PS_TZ_SITEI_DEFN_T 
where 
	TZ_SITEI_ENABLE='Y' 
	and TZ_JG_ID=?
order by TZ_LASTMANT_DTTM desc 
limit 0,1