Ext.define('KitchenSink.view.security.plst.plstComStore', {
    extend: 'Ext.data.Store',
    alias: 'store.plstComStore',
    model: 'KitchenSink.view.security.plst.plstComModel',
    pageSize: 5,
    comID: 'TZ_AQ_PLST_COM',
    pageID: 'TZ_PLST_INFO_STD',
    tzStoreParams:'{"cfgSrhId": "TZ_AQ_PLST_COM.TZ_PLST_INFO_STD.TZ_AQ_COMSQ_V"}',
    proxy: Ext.tzListProxy()
});
