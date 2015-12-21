select 
	OPRID,
	TZ_DLZH_ID,
	TZ_JG_ID,
	TZ_JIHUO_ZT
from 
	PS_TZ_AQ_YHXX_TBL 
where 
	TZ_DLZH_ID=? 
	and TZ_JG_ID=? 
	and TZ_RYLX='ZCYH'