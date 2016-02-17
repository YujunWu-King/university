select 
	count(distinct TZ_CLASS_ID) 
from 
	PS_TZ_CLS_HCGZCS_T 
where 
	TZ_CLASS_ID<>? 
	and TZ_CLS_HCGZ_ID in (
		select 
			distinct B.TZ_CLS_HCGZ_ID 
		from 
			PS_TZ_CLS_HCGZCS_T A,
			PS_TZ_CLS_HCGZ_T B
		where 
			A.TZ_CLS_HCGZ_ID=B.TZ_CLS_HCGZ_ID 
			and B.TZ_JG_ID=? 
			and A.TZ_CLASS_ID=?
	)