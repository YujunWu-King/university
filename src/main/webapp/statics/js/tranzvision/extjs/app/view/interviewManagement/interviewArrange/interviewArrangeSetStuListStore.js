Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewArrangeSetStuListStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeSetStuListModel',
    autoLoad: false,
    pageSize: 1000,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_SSTU_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});