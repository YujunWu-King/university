Ext.define('KitchenSink.view.siteManage.siteManage.menuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.menuMStore',
    model: 'KitchenSink.view.siteManage.siteManage.menuModel',
    comID: 'TZ_GD_ZDGL_COM',
	pageID: 'TZ_GD_ZDDY_STD',
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