select 
	A.TZ_BATCH_ID,
	A.TZ_BATCH_NAME,
	A.TZ_START_DT,
	A.TZ_END_DT,
	A.TZ_APP_END_DT,
	A.TZ_APP_PUB_STATUS,
	B.TZ_ZHZ_DMS
from 
	PS_TZ_CLS_BATCH_T A 
left join 
	(
		select
			TZ_ZHZ_ID,
			TZ_ZHZ_DMS
		from
			PS_TZ_PT_ZHZXX_TBL
		where 
			TZ_ZHZJH_ID='TZ_APP_PUB_STATUS'
			and TZ_EFF_STATUS='A'
	) B
on
	A.TZ_APP_PUB_STATUS=B.TZ_ZHZ_ID
where 
	A.TZ_CLASS_ID=?
order by TZ_START_DT asc
limit ?,?