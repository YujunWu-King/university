Ext.define('KitchenSink.view.weChatLog.weChatLogInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatLogInfoStore',
    model: 'KitchenSink.view.weChatLog.weChatLogInfoModel',
	pageSize: 3,
	comID: 'TZ_GD_WXSERVICE_COM',
	pageID: 'TZ_GD_LOGINFO_STD',
    tzStoreParams: '{}',
	proxy: Ext.tzListProxy()
});
