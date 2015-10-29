Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewArrPreviewStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewArrPreviewModel',
	autoLoad: false,
    pageSize: 0,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_ARR_PRE_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
