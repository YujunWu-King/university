Ext.define('KitchenSink.view.weChatLog.weChatLogListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatLogListStore',
    model: 'KitchenSink.view.weChatLog.weChatLogListModel',
	pageSize: 10,
	comID: 'TZ_GD_WXSERVICE_COM',
	pageID: 'TZ_GD_LOGLIST_STD',
    tzStoreParams: '{}',
//    pageSize: 0,
    proxy: Ext.tzListProxy()
});
