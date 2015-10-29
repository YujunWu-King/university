Ext.define('KitchenSink.view.basicData.filter.filterInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.filterInfoStore',
    model: 'KitchenSink.view.basicData.filter.filterInfoModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_GD_FILTER_COM',
    pageID: 'TZ_FILTER_DEFN_STD',
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

