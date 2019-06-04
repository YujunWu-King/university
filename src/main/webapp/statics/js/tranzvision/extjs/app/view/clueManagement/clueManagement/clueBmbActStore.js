Ext.define('KitchenSink.view.clueManagement.clueManagement.clueBmbActStore',{
    extend: 'Ext.data.Store',
    alias: 'store.clueBmbActStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.clueBmbModel',
    pageSize:10,
    autoLoad:false,
    //comID: 'TZ_XSXS_INFO_COM',
    //pageID: 'TZ_XSXS_DETAIL_STD',
    //operateType:"queryAct",
    tzStoreParams: '',
    proxy: Ext.tzListProxy()
});

