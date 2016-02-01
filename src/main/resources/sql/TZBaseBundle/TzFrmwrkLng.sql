select 
	if(TZ_XXJH_ID1 is null,TZ_XXJH_ID2,TZ_XXJH_ID1) TZ_XXJH_ID,
    if(TZ_XXJH_ID1 is null,TZ_MSG_ID2,TZ_MSG_ID1) TZ_MSG_ID,
    if(TZ_XXJH_ID1 is null,TZ_MSG_TEXT2,TZ_MSG_TEXT1) TZ_MSG_TEXT 
from ( 
	select 
		TMP3.TZ_XXJH_ID as TZ_XXJH_ID1,
		TMP3.TZ_MSG_ID as TZ_MSG_ID1,
		TMP3.TZ_MSG_TEXT as TZ_MSG_TEXT1,
		TMP4.TZ_XXJH_ID as TZ_XXJH_ID2,
		TMP4.TZ_MSG_ID as TZ_MSG_ID2,
		TMP4.TZ_MSG_TEXT as TZ_MSG_TEXT2
	from (
		select
			A.TZ_XXJH_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_ID,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_ID,B.TZ_MSG_ID)) TZ_MSG_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_TEXT,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_TEXT,B.TZ_MSG_TEXT)) TZ_MSG_TEXT 
		from 
			PS_TZ_PT_XXDY_TBL A
		left join
	        (
				select
					*
				from
					PS_TZ_PT_XXDY_TBL 
				where 
					TZ_LANGUAGE_ID=?
	        ) B
		on
			A.TZ_XXJH_ID=B.TZ_XXJH_ID
	        and A.TZ_JG_ID=B.TZ_JG_ID
	        and A.TZ_MSG_ID=B.TZ_MSG_ID
		where 
			UPPER(A.TZ_LANGUAGE_ID)=( 
				select 
					UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD 
				from 
					PS_TZ_HARDCD_PNT 
				where 
					TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' 
			) 
			and A.TZ_XXJH_ID=? 
			and A.TZ_JG_ID=?
		) TMP3
	left join (
		select
			A.TZ_XXJH_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_ID,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_ID,B.TZ_MSG_ID)) TZ_MSG_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_TEXT,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_TEXT,B.TZ_MSG_TEXT)) TZ_MSG_TEXT 
		from 
			PS_TZ_PT_XXDY_TBL A
		left join
	        (
				select
					*
				from
					PS_TZ_PT_XXDY_TBL 
				where 
					TZ_LANGUAGE_ID=?
	        ) B
		on
			A.TZ_XXJH_ID=B.TZ_XXJH_ID
	        and A.TZ_JG_ID=B.TZ_JG_ID
	        and A.TZ_MSG_ID=B.TZ_MSG_ID
		where 
			UPPER(A.TZ_LANGUAGE_ID)=( 
				select 
					UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD 
				from 
					PS_TZ_HARDCD_PNT 
				where 
					TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' 
			) 
			and A.TZ_XXJH_ID=? 
			and A.TZ_JG_ID=?
		) TMP4 
	on(
		TMP3.TZ_XXJH_ID=TMP4.TZ_XXJH_ID and TMP3.TZ_MSG_ID=TMP4.TZ_MSG_ID
	) 
union all 
	select 
		TMP5.TZ_XXJH_ID as TZ_XXJH_ID1,
		TMP5.TZ_MSG_ID as TZ_MSG_ID1,
		TMP5.TZ_MSG_TEXT as TZ_MSG_TEXT1,
		TMP6.TZ_XXJH_ID as TZ_XXJH_ID2,
		TMP6.TZ_MSG_ID as TZ_MSG_ID2,
		TMP6.TZ_MSG_TEXT as TZ_MSG_TEXT2
	from (
		select
			A.TZ_XXJH_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_ID,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_ID,B.TZ_MSG_ID)) TZ_MSG_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_TEXT,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_TEXT,B.TZ_MSG_TEXT)) TZ_MSG_TEXT 
		from 
			PS_TZ_PT_XXDY_TBL A
		left join
	        (
				select
					*
				from
					PS_TZ_PT_XXDY_TBL 
				where 
					TZ_LANGUAGE_ID=?
	        ) B
		on
			A.TZ_XXJH_ID=B.TZ_XXJH_ID
	        and A.TZ_JG_ID=B.TZ_JG_ID
	        and A.TZ_MSG_ID=B.TZ_MSG_ID
		where 
			UPPER(A.TZ_LANGUAGE_ID)=( 
				select 
					UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD 
				from 
					PS_TZ_HARDCD_PNT 
				where 
					TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' 
			) 
			and A.TZ_XXJH_ID=? 
			and A.TZ_JG_ID=?
		) TMP5
	right join (
		select
			A.TZ_XXJH_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_ID,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_ID,B.TZ_MSG_ID)) TZ_MSG_ID, 
	        if(B.TZ_MSG_ID is null,A.TZ_MSG_TEXT,if(ltrim(rtrim(B.TZ_MSG_ID))='',A.TZ_MSG_TEXT,B.TZ_MSG_TEXT)) TZ_MSG_TEXT 
		from 
			PS_TZ_PT_XXDY_TBL A
		left join
	        (
				select
					*
				from
					PS_TZ_PT_XXDY_TBL 
				where 
					TZ_LANGUAGE_ID=?
	        ) B
		on
			A.TZ_XXJH_ID=B.TZ_XXJH_ID
	        and A.TZ_JG_ID=B.TZ_JG_ID
	        and A.TZ_MSG_ID=B.TZ_MSG_ID
		where 
			UPPER(A.TZ_LANGUAGE_ID)=( 
				select 
					UPPER(TZ_HARDCODE_VAL) TZ_LANGUAGE_CD 
				from 
					PS_TZ_HARDCD_PNT 
				where 
					TZ_HARDCODE_PNT='TZGD_BASIC_LANGUAGE' 
			) 
			and A.TZ_XXJH_ID=? 
			and A.TZ_JG_ID=?
		) TMP6 
	on(
		TMP5.TZ_XXJH_ID=TMP6.TZ_XXJH_ID and TMP5.TZ_MSG_ID=TMP6.TZ_MSG_ID
	) 
	where TMP5.TZ_XXJH_ID is null or TMP5.TZ_MSG_ID is null
) TMP1