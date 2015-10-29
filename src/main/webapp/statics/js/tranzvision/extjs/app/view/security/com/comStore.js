Ext.define('KitchenSink.view.security.com.comStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comStore',
    model: 'KitchenSink.view.security.com.comModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_AQ_COMREG_COM',
	pageID: 'TZ_AQ_COMGL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_AQ_COMREG_COM.TZ_AQ_COMGL_STD.TZ_GD_COMZC_VW","condition":{}}',
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
