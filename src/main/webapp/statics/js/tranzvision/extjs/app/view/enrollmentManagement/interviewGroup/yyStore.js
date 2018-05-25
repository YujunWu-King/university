//面试现场分组  预约列表 Store
Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.yyStore', {
    extend: 'Ext.data.Store',
    alias: 'store.yyStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewGroup.yyModel',
    autoLoad: false,
    pageSize: 50,
    comID: 'TZ_MSXCFZ_COM',
    pageID: 'TZ_MSPS_KSMD_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});