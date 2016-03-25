/*站点实例区域集合表*/
delete from PS_TZ_SITEI_AREA_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例区域类型定义表*/
delete from PS_TZ_SITEI_ATYP_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例菜单表*/
delete from PS_TZ_SITEI_MENU_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例菜单功能类型图标*/
delete from PS_TZ_SITEI_CDPF_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例菜单功能类型定义表*/
delete from PS_TZ_SITEI_MTYP_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例栏目定义表*/
delete from PS_TZ_SITEI_COLU_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例模板集合表*/
delete from PS_TZ_SITEI_TEMP_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例菜单图标*/
delete from PS_TZ_SITEI_MNPF_T where TZ_SITEI_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*内容标题图标*/
delete from PS_TZ_ART_TITIMG_T where TZ_ATTACHSYSFILENA in (
	select TZ_ATTACHSYSFILENA from PS_TZ_ART_REC_TBL where TZ_ART_ID in (
		select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
			select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
		)
	)
);

/*站点实例文章内容表*/
delete from PS_TZ_ART_REC_TBL where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*内容附件集附件表*/    
delete from PS_TZ_ART_FJJ_T where TZ_ATTACHSYSFILENA in (
	select TZ_ATTACHSYSFILENA from PS_TZ_ART_FILE_T where TZ_ART_ID in (
		select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
			select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
		)
	)
);
    
/*站点内容附件关联表*/
delete from PS_TZ_ART_FILE_T where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*内容图片集附件表*/
delete from PS_TZ_ART_TPJ_T where TZ_ATTACHSYSFILENA in (
	select TZ_ATTACHSYSFILENA from PS_TZ_ART_PIC_T where TZ_ART_ID in (
		select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
			select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
		)
	)
);

/*站点内容图片集关联表*/
delete from PS_TZ_ART_PIC_T where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*活动基本信息表*/
delete from PS_TZ_ART_HD_TBL where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*活动报名信息项下拉框详细表*/
delete from PS_TZ_XXX_TRANS_T where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*活动报名信息项表*/
delete from PS_TZ_ZXBM_XXX_T where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*联系方式表(活动报名的联系人信息)*/
delete from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='HDBM' and TZ_LYDX_ID in (
	select TZ_HD_BMR_ID from PS_TZ_NAUDLIST_T where TZ_ART_ID in (
		select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
			select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
		)
	)
);

/*活动报名表*/
delete from PS_TZ_NAUDLIST_T where TZ_ART_ID in (
	select TZ_ART_ID from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
		select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*站点栏目内容关联表*/
delete from PS_TZ_LM_NR_GL_T where TZ_SITE_ID in (
	select TZ_SITEI_ID from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA')
);

/*站点实例表*/
delete from PS_TZ_SITEI_DEFN_T where TZ_JG_ID in ('DYTMBA');