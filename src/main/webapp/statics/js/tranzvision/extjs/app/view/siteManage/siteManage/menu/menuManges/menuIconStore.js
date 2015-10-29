Ext.define('KitchenSink.view.siteManage.siteManage.menu.menuManges.menuIconStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuIconStore',
    model: 'KitchenSink.view.siteManage.siteManage.menu.menuManges.menuIconModel',
    comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_GD_ICON1_STD',
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
