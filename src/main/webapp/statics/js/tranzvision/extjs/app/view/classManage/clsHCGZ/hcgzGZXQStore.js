Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzGZXQStore', {
    extend: 'Ext.data.Store',
    alias: 'store.hcgzGZXQStore',
    model: 'KitchenSink.view.classManage.clsHCGZ.hcgzGZXQModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_CLS_HCGZ_COM',
    pageID: 'TZ_CLS_HCGZXQ_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
