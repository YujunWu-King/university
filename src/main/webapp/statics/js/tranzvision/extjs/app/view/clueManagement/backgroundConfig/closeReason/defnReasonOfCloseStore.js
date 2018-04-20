Ext.define('KitchenSink.view.clueManagement.backgroundConfig.closeReason.defnReasonOfCloseStore', {
    extend: 'Ext.data.Store',
    alias: 'store.defnReasonOfCloseStore',
    model: 'KitchenSink.view.clueManagement.backgroundConfig.closeReason.defnReasonOfCloseModel',
    autoLoad:true,
    comID: 'TZ_GBYY_XSGL_COM',
    pageID: 'TZ_GBYY_XSGL_STD',
    // tzStoreParams:'{"cfgSrhId": "TZ_GBYY_XSGL_COM.TZ_GBYY_XSGL_STD.TZ_GBYY_XSGL_V"}',
    tzStoreParams:'{"cfgSrhId": "TZ_GBYY_XSGL_COM.TZ_GBYY_XSGL_STD.TZ_GBYY_XSGL_V","condition":{"TZ_JG_ID-operator": "01","TZ_JG_ID-value": "'+ Ext.tzOrgID+'"}}',
    pageSize:10,
    proxy: Ext.tzListProxy()
});
