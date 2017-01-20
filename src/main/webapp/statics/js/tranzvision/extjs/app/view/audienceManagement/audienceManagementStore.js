Ext.define('KitchenSink.view.audienceManagement.audienceManagementStore', {
    extend: 'Ext.data.Store',
    alias: 'store.audStore',
    model: 'KitchenSink.view.audienceManagement.audienceManagementModel',
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
