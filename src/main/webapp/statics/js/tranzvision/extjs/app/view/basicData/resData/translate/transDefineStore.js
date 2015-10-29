Ext.define('KitchenSink.view.basicData.resData.translate.transDefineStore', {
    extend: 'Ext.data.Store',
    alias: 'store.transDefineStore',
    model: 'KitchenSink.view.basicData.resData.translate.transDefineModel',
	autoLoad: false,
	pageSize: 10,		
	comID: 'TZ_GD_TRANSLATE_COM',
	pageID: 'TZ_GD_TRANSDY_STD',
	tzStoreParams: '',
	proxy: Ext.tzListProxy()
});
