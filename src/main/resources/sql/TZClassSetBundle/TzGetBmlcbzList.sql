select 
	TZ_APPPRO_ID,
	TZ_SORT_NUM,
	TZ_APPPRO_NAME
from 
	PS_TZ_APPPRO_STP_T
where
	TZ_APPPRO_TMP_ID=?
order by TZ_SORT_NUM asc
limit ?,?