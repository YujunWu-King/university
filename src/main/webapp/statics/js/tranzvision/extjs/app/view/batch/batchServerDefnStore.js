Ext.define('KitchenSink.view.batch.batchServerDefnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comStore',
    model: 'KitchenSink.view.batch.batchServerDefnModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_BATCH_SERVER_COM',
	pageID: 'TZ_BATCH_SERVM_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BATCH_SERVER_COM.TZ_BATCH_SERVM_STD.TZ_JC_FWQDX_T","condition":{}}',
	/*proxy: {
		type: 'ajax',
		url : '/tranzvision/kitchensink/app/view/security/com/coms.json',
		reader: {
			type: 'json',
			rootProperty: ''
		}
	}*/
	proxy: Ext.tzListProxy()
});
