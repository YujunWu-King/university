Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewArrangeAddStuStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeAddStuModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_ASTU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});