Ext.define('KitchenSink.view.weChatServiceManagement.weChatServiceStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatServiceStore',
    model: 'KitchenSink.view.weChatServiceManagement.weChatServiceModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_WXSERVICE_COM',
	pageID: 'TZ_GD_FWHLIST_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_GD_WXSERVICE_COM.TZ_GD_FWHLIST_STD.TZ_WX_APPSE_VW"}',
	proxy: Ext.tzListProxy()
});
