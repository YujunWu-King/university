Ext.define('KitchenSink.view.processDispatchMg.processMonitorStore', {
    extend: 'Ext.data.Store',
    alias: 'store.processMonitorStore',
    model: 'KitchenSink.view.processDispatchMg.processMonitorModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_JC_DISPATCH_COM',
    pageID: 'TZ_MONITOR_LIST',
    tzStoreParams: '{"cfgSrhId":"TZ_JC_DISPATCH_COM.TZ_MONITOR_LIST.TZ_JC_MONITOR_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    proxy: Ext.tzListProxy()

});
