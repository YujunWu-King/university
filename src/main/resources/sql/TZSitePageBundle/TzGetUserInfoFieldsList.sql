select 
	TZ_REG_FIELD_ID,
	TZ_REG_FIELD_NAME,
	TZ_FIELD_TYPE 
from 
	PS_TZ_REG_FIELD_T 
where 
	TZ_SITEI_ID =? 
	and TZ_ENABLE = 'Y' 
	and TZ_IS_SHOWWZSY = 'Y' 
order by TZ_ORDER