Ext.define('KitchenSink.view.weChatLog.weChatLogInfoModel', {
    extend: 'Ext.data.Store',
    alias: 'store.weChatLogInfoStore',
    model: 'KitchenSink.view.weChatLog.weChatLogInfoModel',
	autoLoad: true,
	pageSize: 10,
	comID: '',
	pageID: '',
    tzStoreParams: '{"cfgSrhId":""}',
	proxy: Ext.tzListProxy()
});
