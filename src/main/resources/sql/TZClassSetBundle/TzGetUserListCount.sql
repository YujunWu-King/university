select 
	count(1) 
from 
	PS_TZ_AQ_YHXX_TBL A 
where 
	TZ_JG_ID=? 
	and TZ_RYLX='NBYH' 
	and TZ_JIHUO_ZT='Y' 
	and not exists (
		select 1 from PS_TZ_JUSR_REL_TBL where OPRID=A.OPRID
	)