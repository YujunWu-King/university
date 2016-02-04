select 
	TZ_APPPRO_ID,
	TZ_SORT_NUM,
	TZ_APPPRO_NAME
from
	PS_TZ_CLS_BMLC_T
where
	TZ_CLASS_ID=?
order by TZ_SORT_NUM asc
limit ?,?