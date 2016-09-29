Ext.define('KitchenSink.view.activity.viewArtStore', {
    extend: 'Ext.data.Store',
    alias: 'store.viewArtStore',
    model: 'KitchenSink.view.activity.viewArtModel',
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
