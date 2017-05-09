Ext.define('KitchenSink.view.processDispatchMg.processDispatchStore', {
    extend: 'Ext.data.Store',
    alias: 'store.processDispatchStore',
    model: 'KitchenSink.view.processDispatchMg.processDispatchModel',
    autoLoad: true,
    pageSize: 10,
    comID: 'TZ_JC_DISPATCH_COM',
    pageID: 'TZ_DISPATCH_MG',
    tzStoreParams: '{"cfgSrhId":"TZ_JC_DISPATCH_COM.TZ_DISPATCH_MG.TZ_PROCESS_DF_VW","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "' + Ext.tzOrgID + '"}}',
    proxy: Ext.tzListProxy()

});
