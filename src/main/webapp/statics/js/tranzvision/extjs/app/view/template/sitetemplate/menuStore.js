Ext.define('KitchenSink.view.template.sitetemplate.menuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuStore',
    model: 'KitchenSink.view.template.sitetemplate.menuModel',
    comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_ZDMB_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
    /*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/menus.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
    proxy: Ext.tzListProxy()
});