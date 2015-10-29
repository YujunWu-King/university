Ext.define('KitchenSink.view.interviewManagement.interviewManage.msBatchStore', {
    extend: 'Ext.data.Store',
    alias: 'store.msMgrBatchStore',
    model: 'KitchenSink.view.interviewManagement.interviewManage.msBatchModel',
	autoLoad: true,
    pageSize: 10,
    comID: 'TZ_MS_MGR_COM',
    pageID: 'TZ_MS_CHSBAT_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
