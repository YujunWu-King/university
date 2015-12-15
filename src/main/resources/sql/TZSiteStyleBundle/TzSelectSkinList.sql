select 
	ifnull(PT1.TZ_SITEM_ID,'') TZ_SITEM_ID,
	ifnull(PT2.TZ_SKIN_ID,'') TZ_SKIN_ID,
	ifnull(PT1.TZ_JGBH,'') TZ_JGBH,
	ifnull(PT2.TZ_SKIN_NAME,'') TZ_SKIN_NAME,
	ifnull(
		( 
			select 
				TZ_IMG_VIEW 
			from 
				PS_TZ_SITEM_IMG_T 
			where 
				TZ_SITEM_ID = PT1.TZ_SITEM_ID 
				and TZ_SKIN_ID=PT2.TZ_SKIN_ID 
				and TZ_IMG_XH=(
					select 
						min(TZ_IMG_XH) 
					from 
						PS_TZ_SITEM_IMG_T 
					where 
						TZ_SITEM_ID=PT1.TZ_SITEM_ID 
						and TZ_SKIN_ID=PT2.TZ_SKIN_ID
				) 
			limit 0 , 1
		) 
	,"") TZ_IMG_VIEW
from 
	PS_TZ_SITEM_DEFN_T PT1,
	PS_TZ_SITEM_SKIN_T PT2 
where 
	PT1.TZ_SITEM_ID = PT2.TZ_SITEM_ID  
	and PT1.TZ_SITEM_ENABLE='Y' 
	and PT1.TZ_SITE_LANG=? 
	and PT1.TZ_SITEM_ID like ?