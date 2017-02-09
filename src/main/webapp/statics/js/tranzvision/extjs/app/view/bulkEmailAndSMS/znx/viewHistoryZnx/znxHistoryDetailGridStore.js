Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryDetailGridStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxHistoryDetailGridStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryDetailGridModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_ZNXQ_VIEWTY_COM',
    pageID: 'TZ_ZNXQ_VWTY_D_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
