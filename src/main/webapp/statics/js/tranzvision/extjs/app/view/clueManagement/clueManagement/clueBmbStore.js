Ext.define('KitchenSink.view.clueManagement.clueManagement.clueBmbStore',{
    extend: 'Ext.data.Store',
    alias: 'store.clueBmbStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.clueBmbModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_XSXS_INFO_COM',
    pageID: 'TZ_XSXS_DETAIL_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});

