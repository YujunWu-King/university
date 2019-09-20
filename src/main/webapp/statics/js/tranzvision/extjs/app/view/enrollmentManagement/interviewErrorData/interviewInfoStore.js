//面试异常数据处理  异常数据详情Store
Ext.define('KitchenSink.view.enrollmentManagement.interviewErrorData.interviewInfoStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewInfoStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewErrorData.interviewInfoModel',
    autoLoad: false,
    pageSize: 50,
    comID: 'TZ_MSYCSJ_COM',
    pageID: 'TZ_MSYCSJINFO_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});