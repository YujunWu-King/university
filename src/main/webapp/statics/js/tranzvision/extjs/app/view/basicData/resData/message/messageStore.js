Ext.define('KitchenSink.view.basicData.resData.message.messageStore', {
    extend: 'Ext.data.Store',
    alias: 'store.messageStore',
    model: 'KitchenSink.view.basicData.resData.message.messageModel',
	autoLoad: true,
	comID: 'TZ_GD_MESSAGE_COM',
	pageID: 'TZ_GD_MSGlIST_STD',
	tzStoreParams: '{"cfgSrhId":"TZ_GD_MESSAGE_COM.TZ_GD_MSGlIST_STD.TZ_MESSAGES_V","condition":{}}',
	pageSize: 10,
	proxy: Ext.tzListProxy()
});
