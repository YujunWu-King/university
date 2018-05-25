//面试现场分组  考生列表 Store
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.stuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewStuStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewGroup.stuModel',
    autoLoad: false,
    pageSize: 50,
    comID: 'TZ_MSXCFZ_COM',
    pageID: 'TZ_MSGL_STU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});