Ext.define('KitchenSink.view.judgesManagement.secretaryAccount.secretaryAccStore', {
    extend: 'Ext.data.Store',
    alias: 'store.judgeAccStore',
    model: 'KitchenSink.view.judgesManagement.secretaryAccount.secretaryAccModel',
	autoLoad: true,
	pageSize: 10,
	
	comID: 'TZ_SEC_ACC_COM',
	pageID: 'TZ_SECACC_GL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_SEC_ACC_COM.TZ_SECACC_GL_STD.TZ_MSZHGL_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy()
});
