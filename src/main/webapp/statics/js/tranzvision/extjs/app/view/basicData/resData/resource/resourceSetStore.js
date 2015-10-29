Ext.define('KitchenSink.view.basicData.resData.resource.resourceSetStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceSetStore',
    model: 'KitchenSink.view.basicData.resData.resource.resourceSetModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_ZY_RESSET_COM',
    pageID: 'TZ_RESSET_MANG_STD',
    tzStoreParams: '{"cfgSrhId":"TZ_ZY_RESSET_COM.TZ_RESSET_MANG_STD.TZ_PT_ZYJH_TBL","condition":{}}',
    proxy: Ext.tzListProxy()
});
