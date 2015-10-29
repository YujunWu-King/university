Ext.define('KitchenSink.view.basicData.resData.resource.resourceStore', {
    extend: 'Ext.data.Store',
    alias: 'store.resourceStore',
    model: 'KitchenSink.view.basicData.resData.resource.resourceModel',
    pageSize: 5,
    comID: 'TZ_ZY_RESSET_COM',
    pageID: 'TZ_RESSET_INFO_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});

