select 
	A.TZ_ZHZJH_ID,
	A.TZ_ZHZ_ID,
	A.TZ_EFF_DATE,
	A.TZ_EFF_STATUS,
    COALESCE(B.TZ_ZHZ_DMS,A.TZ_ZHZ_DMS) TZ_ZHZ_DMS,
    COALESCE(B.TZ_ZHZ_CMS,A.TZ_ZHZ_CMS) TZ_ZHZ_CMS
from 
	PS_TZ_PT_ZHZXX_TBL A 
	left join (
		select 
			* 
		from 
			PS_TZ_PT_ZHZXX_LNG 
		where 
			TZ_LANGUAGE_ID=?
	) B 
	on (
		A.TZ_ZHZJH_ID=B.TZ_ZHZJH_ID 
		and A.TZ_ZHZ_ID=B.TZ_ZHZ_ID
	) 
where 
	A.TZ_ZHZJH_ID=? 
	and A.TZ_ZHZ_ID=?