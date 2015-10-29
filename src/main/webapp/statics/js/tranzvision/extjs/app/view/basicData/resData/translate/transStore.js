Ext.define('KitchenSink.view.basicData.resData.translate.transStore', {
    extend: 'Ext.data.Store',
    alias: 'store.transStore',
    model: 'KitchenSink.view.basicData.resData.translate.transModel',
	autoLoad: true,
    pageSize: 10,
	comID: 'TZ_GD_TRANSLATE_COM',
	pageID: 'TZ_GD_TRANSET_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_GD_TRANSLATE_COM.TZ_GD_TRANSET_STD.TZ_PT_ZHZJH_TBL"}',
	proxy: Ext.tzListProxy()
});
