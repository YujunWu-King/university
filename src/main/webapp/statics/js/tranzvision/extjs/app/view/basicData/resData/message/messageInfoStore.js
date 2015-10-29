Ext.define('KitchenSink.view.basicData.resData.message.messageInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.messageInfoStore',
    model: 'KitchenSink.view.basicData.resData.message.messageInfoModel',
	autoLoad: false,
	comID: 'TZ_GD_MESSAGE_COM',
	pageID: 'TZ_GD_MSGINFO_STD',
	tzStoreParams: '{"cfgSrhId": "TZ_GD_MESSAGE_COM.TZ_GD_MSGINFO_STD.TZ_PT_XXDY_VW"}',
	pageSize: 5,
	proxy: Ext.tzListProxy()
});
