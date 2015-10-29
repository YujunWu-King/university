Ext.define('KitchenSink.view.basicData.resData.hardCode.hardCodeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.hardCodeStore',
    model: 'KitchenSink.view.basicData.resData.hardCode.hardCodeModel',
	autoLoad: true,
	pageSize: 10,
	comID: 'TZ_GD_HARDCODE_COM',
	pageID: 'TZ_GD_HARDCODELIST',
    tzStoreParams: '{"cfgSrhId":"TZ_GD_HARDCODE_COM.TZ_GD_HARDCODELIST.TZ_HARDCODE_VW"}',
	proxy: Ext.tzListProxy()
});
