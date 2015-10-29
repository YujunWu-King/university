Ext.define('KitchenSink.view.interviewManagement.interviewManage.msResImpStore', {
    extend: 'Ext.data.Store',
    alias: 'store.msResImpStore',
    model: 'KitchenSink.view.interviewManagement.interviewManage.msResImpModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_MS_MGR_COM',
    pageID: 'TZ_MS_MSRESIMP_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});