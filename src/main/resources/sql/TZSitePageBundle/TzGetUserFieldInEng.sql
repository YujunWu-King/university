select 
	TZ_REG_FIELD_NAME 
from 
	PS_TZ_REGFIELD_ENG 
where  
	TZ_JG_ID = ? 
	and TZ_REG_FIELD_ID = ? 
	and LANGUAGE_CD = ?