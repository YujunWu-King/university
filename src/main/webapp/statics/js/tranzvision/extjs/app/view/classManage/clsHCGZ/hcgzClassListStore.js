Ext.define('KitchenSink.view.classManage.clsHCGZ.hcgzClassListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.hcgzClassListStore',
    model: 'KitchenSink.view.classManage.clsHCGZ.hcgzClassListModel',
    autoLoad: true,
    pageSize: 0,
    comID: 'TZ_CLS_HCGZ_COM',
    pageID: 'TZ_CLS_HCGZCLS_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
