select 
	TZ_TYPE_IMG,
	TZ_NOW_IMG 
from 
	PS_TZ_SITEI_CDPF_T 
where
	TZ_SITEI_ID=? 
	and TZ_MENU_TYPE_ID=?  
	and TZ_SKIN_ID=(
		select 
			TZ_SKIN_ID 
		from 
			PS_TZ_SITEI_DEFN_T 
		where 
			TZ_SITEI_ID = ?
	)