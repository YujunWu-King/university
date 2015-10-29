Ext.define('KitchenSink.view.security.plst.comPageStore', {
    extend: 'Ext.data.Store',
    alias: 'store.comPageStore',
    model: 'KitchenSink.view.security.plst.comPageModel',
    pageSize: 5,
    comID: 'TZ_AQ_PLST_COM',
    pageID: 'TZ_PLST_COM_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
