Ext.define('KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.mspsksstore',
    model: 'KitchenSink.view.viewPsStudentListInfo.ViewPsStudentListModel',
    pageSize:200,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_KS_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});