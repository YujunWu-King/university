Ext.define('KitchenSink.view.batch.circulate.batchCirculateDefnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comStore',
    model: 'KitchenSink.view.batch.circulate.batchCirculateDefnModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_BATCH_XH_COM',
	pageID: 'TZ_BATCH_XHMNG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BATCH_XH_COM.TZ_BATCH_XHMNG_STD.TZ_XUNH_DEFN_T","condition":{}}',
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
