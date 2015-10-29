Ext.define('KitchenSink.view.activity.attachmentStore', {
    extend: 'Ext.data.Store',
    alias: 'store.attachmentStore',
    model: 'KitchenSink.view.activity.attachmentModel',
    comID: 'TZ_HD_MANAGER_COM',
		pageID: 'TZ_HD_INFO_STD',
		tzStoreParams: '',
    pageSize: 5,
	  //autoLoad: true,
	  /*
		proxy: {
			type: 'ajax',
			url : '/tranzvision/kitchensink/app/view/activity/attachments.json',
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'root'
			}
		}	
		*/
		proxy: Ext.tzListProxy()
});
