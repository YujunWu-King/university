Ext.define('KitchenSink.view.clueManagement.clueManagement.import.clueImportStore',{
    extend: 'Ext.data.Store',
    alias: 'store.enrollClueImpStore',
    model: 'KitchenSink.view.clueManagement.clueManagement.import.clueImportModel',
    autoLoad: false,
    pageSize:100,
    comID: 'TZ_XSXS_DRDC_COM',
    pageID: 'TZ_XSXS_IMP_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
