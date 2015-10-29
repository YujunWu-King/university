Ext.define('KitchenSink.view.siteManage.siteManage.columnStore', {
    extend: 'Ext.data.Store',
    alias: 'store.columnStoreI',
    model: 'KitchenSink.view.siteManage.siteManage.columnModel',
    comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_GD_ZDDY_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
    /*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/columns.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
    proxy: Ext.tzListProxy()
});