Ext.define('KitchenSink.view.enrollProject.userMg.demoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.demoStore',
    model: 'KitchenSink.view.enrollProject.userMg.demoModel',
	autoLoad: true,
	pageSize: 500,
	comID: 'TZ_UM_USERMG_COM',
	pageID: 'TZ_DEMO_STD',
	//tzStoreParams: '{"cfgSrhId":"TZ_UM_USERMG_COM.TZ_UM_USERMG_STD.TZ_REG_USER_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	tzStoreParams: '{"cfgSrhId":"TZ_UM_USERMG_COM.TZ_DEMO_STD.CLASS_USER_BM","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	proxy: Ext.tzListProxy(),
	remoteSort:true
});
