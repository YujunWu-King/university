select 
	TZ_SBMINF_ID,
	TZ_SORT_NUM,
	TZ_CONT_INTRO,
	TZ_REMARK
from 
	PS_TZ_CLS_DJZL_T
where
	TZ_CLASS_ID=?
order by TZ_SORT_NUM asc
limit ?,?