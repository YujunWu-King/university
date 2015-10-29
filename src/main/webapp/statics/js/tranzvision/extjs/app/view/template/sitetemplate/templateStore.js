Ext.define('KitchenSink.view.template.sitetemplate.templateStore', {
    extend: 'Ext.data.Store',
    alias: 'store.templateStore',
    model: 'KitchenSink.view.template.sitetemplate.templateModel',
    comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_ZDMB_STD',
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

