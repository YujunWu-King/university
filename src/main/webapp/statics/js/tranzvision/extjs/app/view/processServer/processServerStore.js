Ext.define('KitchenSink.view.processServer.processServerStore', {
    extend: 'Ext.data.Store',
    alias: 'store.processServerStore',
    model: 'KitchenSink.view.processServer.processServerModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_PROCESS_FW_COM',
		pageID: 'TZ_PROCESS_FW_LIST',
		tzStoreParams: '{"cfgSrhId":"TZ_PROCESS_FW_COM.TZ_PROCESS_FW_LIST.TZ_JC_SERVER_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
		proxy: Ext.tzListProxy()
	
});
