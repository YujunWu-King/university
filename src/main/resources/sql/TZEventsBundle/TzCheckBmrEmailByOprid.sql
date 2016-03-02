select 
	'Y' 
from 
	PS_TZ_NAUDLIST_T A 
left join
	(
		select 
			* 
		from 
			PS_TZ_LXFSINFO_TBL 
		where 
			TZ_LXFS_LY='HDBM'
	) B 
on
	(
		A.TZ_HD_BMR_ID=B.TZ_LYDX_ID
	)
where 
	A.TZ_ART_ID=? 
	and B.TZ_ZY_EMAIL=? 
	and A.OPRID<>?