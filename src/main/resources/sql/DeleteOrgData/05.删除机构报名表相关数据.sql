/*报名表模板信息项扁平化表*/
delete from PS_TZ_TEMP_FIELD_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*报名表模板信息项可选值定义表*/
delete from PS_TZ_APPXXX_KXZ_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*报名表模板信息项校验规则表*/
delete from PS_TZ_APPXX_JYGZ_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*报名表模板容器信息项配置表*/
delete from PS_TZ_RQ_XXXPZ_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*报名表保存、提交事件列表*/
delete from PS_TZ_APP_EVENTS_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*报名表模板信息项配置表*/
delete from PS_TZ_APP_XXXPZ_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*联系方式表(活动报名的联系人信息)*/
delete from PS_TZ_LXFSINFO_TBL where TZ_LXFS_LY='ZSBM' and TZ_LYDX_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名表工作表*/
delete from PS_TZ_FORM_WRK_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*考生推荐信表*/
delete from PS_TZ_KS_TJX_TBL where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名表附件信息表*/
delete from PS_TZ_FORM_ATT_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名表实例信息项存储表*/
delete from PS_TZ_APP_CC_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*多选信息项存储表*/
delete from PS_TZ_APP_DHCC_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名表审核报名人标签表*/
delete from PS_TZ_FORM_LABEL_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名审核更多信息表*/
delete from PS_TZ_FRM_MORINF_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名表递交资料审核表*/
delete from PS_TZ_FORM_ZLSH_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名流程发布结果表*/
delete from PS_TZ_APPPRO_RST_T where TZ_APP_INS_ID in (
	select TZ_APP_INS_ID from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (
		select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA')
	)
);

/*报名表实例表*/
delete from PS_TZ_APP_INS_T where TZ_APP_TPL_ID in (select TZ_APP_TPL_ID from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA'));

/*报名表模板定义表*/
delete from PS_TZ_APPTPL_DY_T where TZ_JG_ID in ('DYTMBA');

/*颜色类别定义表*/
delete from PS_TZ_COLOR_SORT_T where TZ_JG_ID in ('DYTMBA');

/*标签定义表*/
delete from PS_TZ_LABEL_DFN_T where TZ_JG_ID in ('DYTMBA');

/*报名审核个性化属性定义表*/
delete from PS_TZ_FORM_ATTR_T where TZ_JG_ID in ('DYTMBA');

/*报名审核个性化属性下拉值定义*/
delete from PS_TZ_F_ATTR_OPT_T where TZ_JG_ID in ('DYTMBA');

/*导出报名表字段定义表*/
delete from PS_TZ_EXP_FRMFLD_T where TZ_EXPORT_TMP_ID in (select TZ_EXPORT_TMP_ID from PS_TZ_EXPORT_TMP_T where TZ_JG_ID in ('DYTMBA'));

/*导出字段与报名表字段关联表*/
delete from PS_TZ_FRMFLD_GL_T where TZ_EXPORT_TMP_ID in (select TZ_EXPORT_TMP_ID from PS_TZ_EXPORT_TMP_T where TZ_JG_ID in ('DYTMBA'));

/*导出报名表模板定义表*/
delete from PS_TZ_EXPORT_TMP_T where TZ_JG_ID in ('DYTMBA');

/*报名流程步骤详情表*/
delete from PS_TZ_APPPRO_STP_T where TZ_APPPRO_TMP_ID in (select TZ_APPPRO_TMP_ID from PS_TZ_APPPRO_TMP_T where TZ_JG_ID in ('DYTMBA'));

/*报名流程步骤回复短语表*/
delete from PS_TZ_APPPRO_HF_T where TZ_APPPRO_TMP_ID in (select TZ_APPPRO_TMP_ID from PS_TZ_APPPRO_TMP_T where TZ_JG_ID in ('DYTMBA'));

/*报名流程模板*/
delete from PS_TZ_APPPRO_TMP_T where TZ_JG_ID in ('DYTMBA');