Ext.define('KitchenSink.view.security.com.processStore', {
    extend: 'Ext.data.Store',
    alias: 'store.processStore',
    model: 'KitchenSink.view.security.com.processModel',
	comID: 'TZ_AQ_COMREG_COM',
	pageID: 'TZ_AQ_PAGEREG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AQ_COMREG_COM.TZ_AQ_PAGEREG_STD.TZ_PROCESS_DF_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
	pageSize: 5,
	proxy: Ext.tzListProxy()
});
