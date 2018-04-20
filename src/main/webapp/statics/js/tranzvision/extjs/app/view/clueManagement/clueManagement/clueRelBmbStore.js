Ext.define('KitchenSink.view.clueManagement.clueManagement.clueRelBmbStore',{
    extend: 'Ext.data.Store',
    alias: 'store.clueRelBmbStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.clueRelBmbModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_XSXS_INFO_COM',
    pageID: 'TZ_XSXS_BMB_STD',
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});

