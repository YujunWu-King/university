select 
	TZ_DLZH_ID 
from 
	PS_TZ_AQ_YHXX_TBL 
where 
	(TZ_MOBILE=? or upper(TZ_EMAIL)=?) 
	and TZ_RYLX='ZCYH' 
	and TZ_JG_ID=?