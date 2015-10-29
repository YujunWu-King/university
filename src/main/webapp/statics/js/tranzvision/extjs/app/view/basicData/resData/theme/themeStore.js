Ext.define('KitchenSink.view.basicData.resData.theme.themeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.themeStore',
    model: 'KitchenSink.view.basicData.resData.theme.themeModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_THEMEGL_COM',
	pageID: 'TZ_GD_THEMEGL_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GD_THEMEGL_COM.TZ_GD_THEMEGL_STD.TZ_PT_ZTXX_V","condition":{}}',
	proxy: Ext.tzListProxy()
	/*
	proxy: {
				type: 'ajax',
				url : '/tranzvision/kitchensink/app/view/basicData/resData/theme/themes.json',
				reader: {
					type: 'json',
					rootProperty: ''
				}
			}	
			*/
});
