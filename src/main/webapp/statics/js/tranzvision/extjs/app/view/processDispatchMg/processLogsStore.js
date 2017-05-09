Ext.define('KitchenSink.view.processDispatchMg.processLogsStore', {
    extend: 'Ext.data.Store',
    alias: 'store.processLogsStore',
    model: 'KitchenSink.view.processDispatchMg.processLogsModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_JC_DISPATCH_COM',
    pageID: 'TZ_PROCESS_LOG',
    tzStoreParams: '{}',
    proxy: Ext.tzListProxy()

});
