/*用户登录日志记录表*/
/*
delete from PS_TZ_AQ_ACLOG_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');
*/

/*操作员定义（PS表）*/
delete from PSOPRDEFN where OPRID in (select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*联系方式表*/
delete from PS_TZ_LXFSINFO_TBL where TZ_LYDX_ID in (select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*用户个人照片信息表*/
delete from PS_TZ_OPR_PHOTO_T where TZ_ATTACHSYSFILENA in (
	select TZ_ATTACHSYSFILENA from PS_TZ_OPR_PHT_GL_T where OPRID in (
		select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN')
	)
);

/*用户与照片关联表*/
delete from PS_TZ_OPR_PHT_GL_T where OPRID in (select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*用户注册信息模板表*/
delete from PS_TZ_USERREG_MB_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*用户注册项信息字段存储表*/
delete from PS_TZ_REG_FIELD_T where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*用户注册信息字段选项值表*/
delete from PS_TZ_YHZC_XXZ_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*手机验证码表*/
delete from PS_TZ_SHJI_YZM_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*电子邮箱验证码表*/
delete from PS_TZ_DZYX_YZM_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*用户注册项信息字段存储表（双语表）*/
delete from PS_TZ_REGFIELD_ENG where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*用户注册信息字段选项值表（双语表）*/
delete from PS_TZ_YHZC_XXZ_ENG where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');

/*用户账号和微信账号绑定表*/
delete from PS_TZ_OPRD_WX_T where OPRID in (select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*用户注册信息存储表*/
delete from PS_TZ_REG_USER_T where OPRID in (select OPRID from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN'));

/*用户信息记录表*/
delete from PS_TZ_AQ_YHXX_TBL where TZ_JG_ID in (select TZ_JG_ID from PS_TZ_JG_BASE_T where TZ_JG_ID<>'ADMIN');
