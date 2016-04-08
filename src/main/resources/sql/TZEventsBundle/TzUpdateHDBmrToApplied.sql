update 
	PS_TZ_NAUDLIST_T 
set 
	TZ_NREG_STAT = '1'  
where  
	TZ_ART_ID = ? 
	and TZ_HD_BMR_ID in (:BMRIDS)