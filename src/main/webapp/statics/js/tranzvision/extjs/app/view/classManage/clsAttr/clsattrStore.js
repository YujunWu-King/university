Ext.define('KitchenSink.view.classManage.clsAttr.clsattrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.clsattrStore',
    model: 'KitchenSink.view.classManage.clsAttr.clsattrModel',
	autoLoad: true,
    pageSize: 20,
		
    comID: 'TZ_GD_BJSX_COM',
    pageID: 'TZ_GD_BJSX_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
	/*
	proxy: {
			type: 'ajax',
			url : '/tranzvision/kitchensink/app/view/classManage/clsAttr/clsattrStore.json',
			reader: {
				type: 'json',
				totalProperty: 'total',
				rootProperty: 'root'
			}
	}*/	
		
});
