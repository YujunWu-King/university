Ext.define('KitchenSink.view.interviewManagement.classBatchChoose.msBatchStore', {
    extend: 'Ext.data.Store',
    alias: 'store.msBatchStore',
    model: 'KitchenSink.view.interviewManagement.classBatchChoose.msBatchModel',
	autoLoad: false,
    pageSize: 10,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_CHS_BATCH_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
