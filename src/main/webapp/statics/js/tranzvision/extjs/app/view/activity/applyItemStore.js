Ext.define('KitchenSink.view.activity.applyItemStore', {
    extend: 'Ext.data.Store',
    alias: 'store.applyItemStore',
    model: 'KitchenSink.view.activity.applyItemModel',
    comID: 'TZ_HD_MANAGER_COM',
		pageID: 'TZ_HD_INFO_STD',
		tzStoreParams: '',
    pageSize: 5,
	  //autoLoad: true,
		/*proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applyItems.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	
			*/
		proxy: Ext.tzListProxy()
});
