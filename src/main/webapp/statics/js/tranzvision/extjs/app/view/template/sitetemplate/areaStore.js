Ext.define('KitchenSink.view.template.sitetemplate.areaStore', {
    extend: 'Ext.data.Store',
    alias: 'store.areaStore',
    model: 'KitchenSink.view.template.sitetemplate.areaModel',
    comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_ZDMB_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
    /*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/areas.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/	
    proxy: Ext.tzListProxy()
});
