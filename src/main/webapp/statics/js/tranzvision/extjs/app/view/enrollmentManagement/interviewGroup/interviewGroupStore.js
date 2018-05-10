Ext.define('KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewGroupStore',
    model: 'KitchenSink.view.enrollmentManagement.interviewGroup.interviewGroupModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_MSXCFZ_COM',
    pageID: 'TZ_MSGL_MSFZ_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy(),
});

