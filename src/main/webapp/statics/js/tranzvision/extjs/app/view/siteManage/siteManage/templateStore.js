Ext.define('KitchenSink.view.siteManage.siteManage.templateStore', {
    extend: 'Ext.data.Store',
    alias: 'store.templateMStore',
    model: 'KitchenSink.view.siteManage.siteManage.templateModel',
    comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_GD_ZDDY_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
    /*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/templates.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
    proxy: Ext.tzListProxy()
});

