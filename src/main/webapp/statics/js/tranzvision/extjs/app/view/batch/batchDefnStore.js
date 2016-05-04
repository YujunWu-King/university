Ext.define('KitchenSink.view.batch.batchDefnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comStore',
    model: 'KitchenSink.view.batch.batchDefnModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_BATCH_DEFN_COM',
	pageID: 'TZ_BATCH_MNG_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_BATCH_DEFN_COM.TZ_BATCH_MNG_STD.TZ_JINC_DY_T","condition":{}}',
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
