Ext.define('KitchenSink.view.siteManage.siteManage.areaStore', {
    extend: 'Ext.data.Store',
    alias: 'store.areaMStore',
    model: 'KitchenSink.view.siteManage.siteManage.areaModel',
    comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_GD_ZDDY_STD',
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
