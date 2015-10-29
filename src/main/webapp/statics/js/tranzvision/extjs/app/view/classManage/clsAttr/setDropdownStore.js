Ext.define('KitchenSink.view.classManage.clsAttr.setDropdownStore', {
    extend: 'Ext.data.Store',
    alias: 'store.setDropdownStore',
    model: 'KitchenSink.view.classManage.clsAttr.setDropdownModel',
	autoLoad: true,
    pageSize: 10,
		
    comID: 'TZ_GD_BJSX_COM',
    pageID: 'TZ_GD_SETLIST_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
	/*
	proxy: {
			type: 'ajax',
			url : '/tranzvision/kitchensink/app/view/classManage/clsAttr/setDropdownStore.json',
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'root'
			}
	}	*/
		
});
