Ext.define('KitchenSink.view.viewPsStudentListInfo.AddGlyWindowStore', {
    extend: 'Ext.data.Store',
    alias: 'store.addglystore',
    model: 'KitchenSink.view.viewPsStudentListInfo.AddGlyWindowModel',
    pageSize:10,
    autoLoad:false,
    comID: 'TZ_REVIEW_MS_COM',
    pageID: 'TZ_MSPS_JUDGES_STD',
    tzStoreParams:'',
    proxy: Ext.tzListProxy()
});