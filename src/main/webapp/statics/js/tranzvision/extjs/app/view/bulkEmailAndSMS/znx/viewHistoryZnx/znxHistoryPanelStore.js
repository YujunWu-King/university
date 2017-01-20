Ext.define('KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelStore', {
    extend: 'Ext.data.Store',
    alias: 'store.znxHistoryPanelStore',
    model: 'KitchenSink.view.bulkEmailAndSMS.znx.viewHistoryZnx.znxHistoryPanelModel',
    autoLoad: false,
    pageSize: 10,
    comID: 'TZ_ZNXQ_VIEWTY_COM',
    pageID: 'TZ_ZNXQ_VIEWTY_STD',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()
});
