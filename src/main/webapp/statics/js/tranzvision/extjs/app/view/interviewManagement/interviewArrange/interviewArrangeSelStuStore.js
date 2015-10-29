Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewArrangeSelStuStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSelStuModel',
    autoLoad: false,
    pageSize: 0,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_CSTU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});