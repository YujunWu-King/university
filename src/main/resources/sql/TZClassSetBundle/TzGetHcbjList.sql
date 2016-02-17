select 
	distinct C.TZ_CLASS_ID,
	D.TZ_CLASS_NAME 
from 
	PS_TZ_CLS_HCGZCS_T C,
	PS_TZ_CLASS_INF_T D
where 
	C.TZ_CLASS_ID=D.TZ_CLASS_ID 
	and C.TZ_CLASS_ID<>? 
	and C.TZ_CLS_HCGZ_ID in (
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
order by TZ_CLASS_ID desc