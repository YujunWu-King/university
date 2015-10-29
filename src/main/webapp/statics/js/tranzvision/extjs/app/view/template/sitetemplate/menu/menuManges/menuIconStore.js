Ext.define('KitchenSink.view.template.sitetemplate.menu.menuManges.menuIconStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuIconStore',
    model: 'KitchenSink.view.template.sitetemplate.menu.menuManges.menuIconModel',
    comID: 'TZ_GD_ZDMB_COM',
	pageID: 'TZ_GD_MENUICON_STD',
	tzStoreParams: '',
    pageSize: 10,
	//autoLoad: true,
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/template/sitetemplate/skins.json',
				reader: {
					type: 'json',
					totalProperty: 'total',
					rootProperty: 'root'
				}
			}*/
    proxy: Ext.tzListProxy()
});
