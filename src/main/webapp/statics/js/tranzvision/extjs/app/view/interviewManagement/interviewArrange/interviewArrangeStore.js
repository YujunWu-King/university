Ext.define('KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeStore', {
    extend: 'Ext.data.Store',
    alias: 'store.interviewArrangeStore',
    model: 'KitchenSink.view.interviewManagement.interviewArrange.interviewArrangeModel',
	autoLoad: false,
    pageSize: 200,
    comID: 'TZ_MS_ARR_MG_COM',
    pageID: 'TZ_MS_CAL_ARR_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
