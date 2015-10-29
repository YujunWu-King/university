Ext.define('KitchenSink.view.activity.applyItemOptionsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.applyItemOptionsStore',
    model: 'KitchenSink.view.activity.applyItemOptionsModel',
    comID: 'TZ_HD_MANAGER_COM',
		pageID: 'TZ_HD_INFO_STD',
		tzStoreParams: '',
    pageSize: 5,
	  autoLoad: true,
		/* proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/activity/applyItemOptions.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}	
			*/
		proxy: Ext.tzListProxy()
});
