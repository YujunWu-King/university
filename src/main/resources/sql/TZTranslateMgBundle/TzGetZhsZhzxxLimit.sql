select 
	TZ_ZHZ_ID,
	TZ_EFF_DATE,
	TZ_EFF_STATUS,
	TZ_ZHZ_DMS,
	TZ_ZHZ_CMS 
from 
	PS_TZ_PT_ZHZXX_TBL 
where 
	TZ_ZHZJH_ID=? 
limit ?,?