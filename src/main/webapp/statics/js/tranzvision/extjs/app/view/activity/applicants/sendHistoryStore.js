Ext.define('KitchenSink.view.activity.applicants.sendHistoryStore', {
    extend: 'Ext.data.Store',
    alias: 'store.sendHistoryStore',
    model: 'KitchenSink.view.activity.applicants.sendHistoryModel',
	autoLoad: false,
	pageSize: 10,
	comID: 'TZ_GD_BMRGL_COM',
	pageID: 'TZ_GD_HJFSLS_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_HJFSLS_STD.TZ_HDHJFSLS_VW"}',
	proxy: Ext.tzListProxy()
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applicants/sendHistory.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
	}
	*/	
});
