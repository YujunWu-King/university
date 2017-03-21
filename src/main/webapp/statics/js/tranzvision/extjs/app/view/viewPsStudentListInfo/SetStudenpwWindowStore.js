Ext.define('KitchenSink.view.viewPsStudentListInfo.SetStudenpwWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.setpwstore',
    model: 'KitchenSink.view.viewPsStudentListInfo.SetStudenpwWindowModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_SETPW_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});