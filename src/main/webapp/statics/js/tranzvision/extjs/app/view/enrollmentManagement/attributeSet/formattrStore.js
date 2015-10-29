Ext.define('KitchenSink.view.enrollmentManagement.attributeSet.formattrStore', {
    extend: 'Ext.data.Store',
    alias: 'store.formattrStore',
    model: 'KitchenSink.view.enrollmentManagement.attributeSet.formattrModel',
	autoLoad: true,
    pageSize: 10,
		
    comID: 'TZ_BMGL_SXSZ_COM',
    pageID: 'TZ_BMGL_SXSZ_STD',
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
