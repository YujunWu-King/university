/*
注意：
重新初始化前需要备份 PS_TZ_SITEI_COLU_T 表的数据；
由于栏目ID字段（TZ_COLU_ID）在每次初始化时都是重新生成的，
所以需要在重新初始化后，将TZ_LM_NR_GL_T（栏目内容关联表）的栏目ID字段（TZ_COLU_ID）更新成新的对应的栏目ID；
否则该站点的文章数据将无法匹配，前台无法显示；
*/

delete from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID='8';

delete from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID='8';

delete from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID='8';

delete from PS_TZ_SITEI_CDPF_T where TZ_SITEI_ID='8';

delete from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID='8';

delete from PS_TZ_SITEI_TEMP_T where TZ_SITEI_ID='8';

delete from PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID='8';

/*删除此表数据前必须先备份*/
/*delete from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID='8';*/

update 
	PS_TZ_SITEI_DEFN_T 
set 
	TZ_SITEM_ID='',TZ_IMG_STOR='',TZ_IMG_VIEW='',
    TZ_ATTS_STOR='',TZ_ATTS_VIEW='',TZ_VIDEO_STOR='',
    TZ_VIDEO_VIEW='',TZ_SKIN_ID='',TZ_SKIN_NAME='',
    TZ_SKIN_CODE='',TZ_INDEX_INITCODE='',TZ_INDEX_SAVECODE='',TZ_INDEX_PRECODE='',
    TZ_LONGIN_INITCODE='',TZ_LONGIN_SAVECODE='' 
where TZ_SITEI_ID='8';