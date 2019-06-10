Ext.define('KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryStore',{
    extend:'Ext.data.Store',
    alias:'store.enrollmentClueSmsHistoryStore',
    model:'KitchenSink.view.clueManagement.clueManagement.enrollmentClueSmsHistoryModel',
    pageSize:20,
    autoLoad: true,
    comID:'TZ_XSXS_ZSXS_COM',
    pageID:'TZ_XSXS_SMSHIS_STD',
    tzStoreParams:'',
    proxy:Ext.tzListProxy()
});