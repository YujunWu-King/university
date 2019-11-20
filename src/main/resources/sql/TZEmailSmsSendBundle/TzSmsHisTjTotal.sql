select count(1) from `PS_TZ_DXYJFSRW_TBL` `A` , `PS_TZ_AQ_YHXX_TBL` `B` 
 where `A`.`TZ_TASK_LX` = 'SMS' AND `A`.`ROW_ADDED_OPRID` = `B`.`OPRID` and A.TZ_JG_ID=? AND A.TZ_TMPL_ID=?