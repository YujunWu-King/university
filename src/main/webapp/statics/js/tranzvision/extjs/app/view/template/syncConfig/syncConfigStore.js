Ext.define('KitchenSink.view.template.syncConfig.syncConfigStore', {
    extend: 'Ext.data.Store',
    alias: 'store.syncConfigStore',
    model: 'KitchenSink.view.template.syncConfig.syncConfigModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_APPSYNC_COM',
	pageID: 'TZ_APPSYNC_STD',
	//tzStoreParams: '{"cfgSrhId":"TZ_APPSYNC_COM.TZ_APPSYNC_STD.PS_TZ_APPSYNC_COM","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	tzStoreParams: '{"cfgSrhId":"TZ_APPSYNC_COM.TZ_APPSYNC_STD.TZ_APPSYNC_CONFIG","condition":{}}',
	proxy: Ext.tzListProxy()
});
