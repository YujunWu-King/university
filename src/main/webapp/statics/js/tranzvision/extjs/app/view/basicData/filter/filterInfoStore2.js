Ext.define('KitchenSink.view.basicData.filter.filterInfoStore2', {
    extend: 'Ext.data.Store',
    alias: 'store.filterInfoStore2',
    model: 'KitchenSink.view.basicData.filter.filterInfoModel',
    autoLoad: true,
    pageSize: 100,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_FILTER_DEF_STD',
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

