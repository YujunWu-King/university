Ext.define('KitchenSink.view.enrollmentManagement.applicationForm.stuStore', {
    extend: 'Ext.data.Store',
    alias: 'store.appFormStuStore',
    model: 'KitchenSink.view.enrollmentManagement.applicationForm.stuModel',
    autoLoad: false,
    pageSize: 1000,
    comID: 'TZ_BMGL_BMBSH_COM',
    pageID: 'TZ_BMGL_STU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});

