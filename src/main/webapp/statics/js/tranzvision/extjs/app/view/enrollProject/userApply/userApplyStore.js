Ext.define('KitchenSink.view.enrollProject.userApply.userApplyStore', {
    extend: 'Ext.data.Store',
    alias: 'store.userApplyStore',
    model: 'KitchenSink.view.enrollProject.userApply.userApplyModel',
	autoLoad: true,
	pageSize: 500,
	comID: 'TZ_UM_USERAPPLY_COM',
	pageID: 'TZ_UM_USERMG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_UM_USERAPPLY_COM.TZ_UM_USERMG_STD.TZ_USER_APPLY_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	//tzStoreParams: '{"cfgSrhId":"TZ_UM_USERAPPLY_COM.TZ_UM_USERMG_STD.TZ_USER_APPLY_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'","TZ_IS_CMPL-operator": "01","TZ_IS_CMPL-value": "Y","ACCTLOCK-operator":"01","ACCTLOCK-value":"0"}}',
	proxy: Ext.tzListProxy()
});
