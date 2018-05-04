Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.stuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewStuStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.stuModel',
    autoLoad: false,
    pageSize: 50,
    comID: 'TZ_MSXCFZ_COM',
    pageID: 'TZ_MSGL_STU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});