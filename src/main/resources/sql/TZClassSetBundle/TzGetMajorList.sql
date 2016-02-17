select 
	TZ_MAJOR_ID,
	TZ_MAJOR_NAME,
	TZ_SORT_NUM 
from 
	PS_TZ_CLS_MAJOR_T 
where 
	TZ_CLASS_ID=? 
order by TZ_SORT_NUM asc 
limit ?,?