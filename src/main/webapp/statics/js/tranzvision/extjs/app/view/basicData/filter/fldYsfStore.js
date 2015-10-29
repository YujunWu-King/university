Ext.define('KitchenSink.view.basicData.filter.fldYsfStore', {
    extend: 'Ext.data.Store',
    alias: 'store.fldYsfStore',
    model: 'KitchenSink.view.basicData.filter.fldYsfModel',
    //autoLoad: true,
    pageSize: 15,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_FILTER_FLD_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
    /*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/basicData/filter/filterInfos.json',
				reader: {
					type: 'json',
					rootProperty: ''
				}
			}	
	*/	
});

