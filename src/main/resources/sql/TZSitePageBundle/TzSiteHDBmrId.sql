select 
	A.TZ_HD_BMR_ID 
from 
	PS_TZ_NAUDLIST_T A 
left join
	(
		select 
			TZ_LYDX_ID, 
			TZ_ZY_SJ  
		from 
			PS_TZ_LXFSINFO_TBL 
		where 
			TZ_LXFS_LY = 'HDBM'
	) B 
on
	(
		A.TZ_HD_BMR_ID = B.TZ_LYDX_ID
	)
where  
	A.TZ_ART_ID =? 
	and (A.TZ_NREG_STAT='1' or A.TZ_NREG_STAT='4') 
	and A.OPRID =?