Ext.define('KitchenSink.view.batchProcess.processDefineStore', {
    extend: 'Ext.data.Store',
    alias: 'store.processDefineStore',
    model: 'KitchenSink.view.batchProcess.processDefineModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_PROCESS_DF_COM',
		pageID: 'TZ_PROCESS_LIST',
		tzStoreParams: '{"cfgSrhId":"TZ_PROCESS_DF_COM.TZ_PROCESS_LIST.TZ_PROCESS_DF_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
		proxy: Ext.tzListProxy()
	
});
