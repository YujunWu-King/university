select 
	TZ_SBMINF_ID,
	TZ_SORT_NUM,
	TZ_CONT_INTRO,
	TZ_REMARK 
from
	PS_TZ_SBMINF_STP_T
where 
	TZ_SBMINF_TMP_ID=?
order by TZ_SORT_NUM asc
limit ?,?