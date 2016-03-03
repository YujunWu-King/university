select 
	TZ_CYR_NAME,
	TZ_REG_TIME 
from 
	PS_TZ_NAUDLIST_T 
where 
	TZ_ART_ID=? 
	and TZ_NREG_STAT='1' 
order by TZ_REG_TIME