Ext.define('KitchenSink.view.activity.applicants.applicantsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.applicantsStore',
    model: 'KitchenSink.view.activity.applicants.applicantsModel',
	autoLoad: false,
	pageSize: 500,
	comID: 'TZ_GD_BMRGL_COM',
	pageID: 'TZ_GD_BMRGL_STD',
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applicants/applicants.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	
	*/
	tzStoreParams: '{"cfgSrhId":"TZ_GD_BMRGL_COM.TZ_GD_BMRGL_STD.TZ_NAUDLIST_G_V"}',
	proxy: Ext.tzListProxy()
});
