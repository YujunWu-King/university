Ext.define('KitchenSink.view.dispatchLoop.dispatchLoopStore', {
    extend: 'Ext.data.Store',
    alias: 'store.dispatchLoopStore',
    model: 'KitchenSink.view.dispatchLoop.dispatchLoopModel',
		autoLoad: true,
		pageSize: 10,
		comID: 'TZ_DD_LOOP_COM',
		pageID: 'TZ_DD_LOOP_LIST',
		tzStoreParams: '{"cfgSrhId":"TZ_DD_LOOP_COM.TZ_DD_LOOP_LIST.TZ_DD_LOOP_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
		proxy: Ext.tzListProxy()
	
});
