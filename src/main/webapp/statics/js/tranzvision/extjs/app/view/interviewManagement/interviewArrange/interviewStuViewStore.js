Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewStuViewStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewStuViewModel',
	autoLoad: false,
    pageSize: 200,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MSKS_VIEW_STD',
    tzStoreParams: '{}',
    groupField: 'msPlanSeq',
    proxy: Ext.tzListProxy()
});
