Ext.define('KitchenSink.view.security.menu.funcMenuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.funcMenuStore',
    model: 'KitchenSink.view.security.menu.menuModel',
		autoLoad: true,
    pageSize: 10,

    comID: 'TZ_AQ_MENU_COM',
    pageID: 'TZ_AQ_MENULIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_AQ_MENU_COM.TZ_AQ_MENULIST_STD.TZ_GD_GNCD_VW"}',
		/*
			proxy: {
						type: 'ajax',
						url : '/tranzvision/kitchensink/app/view/security/menu/menus.json',
						reader: {
							type: 'json',
							rootProperty: 'root'
						}
			}
		*/
    proxy: Ext.tzListProxy()
});
