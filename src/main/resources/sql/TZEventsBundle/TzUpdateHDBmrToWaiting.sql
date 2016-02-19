update 
	PS_TZ_NAUDLIST_T 
set 
	TZ_NREG_STAT = '4' 
where  
	TZ_ART_ID = ? 
	and TZ_HD_BMR_ID in (
		select 
			TZ_HD_BMR_ID 
		from 
			PS_TZ_NAUDLIST_T 
		where 
			TZ_NREG_STAT = '1' 
			and TZ_ART_ID = ? 
		order by TZ_REG_TIME desc limit 0,?
	)