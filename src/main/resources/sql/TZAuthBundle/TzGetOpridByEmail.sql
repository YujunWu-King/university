select 
	OPRID,
	TZ_DLZH_ID,
	TZ_JG_ID 
from 
	PS_TZ_AQ_YHXX_TBL 
where 
	TZ_EMAIL=? 
	and TZ_JG_ID=? 
	and TZ_JIHUO_ZT='Y' 
	and TZ_YXBD_BZ='Y' 
	and TZ_RYLX='NBYH'